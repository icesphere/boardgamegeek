package org.smartreaction.boardgamegeek.business;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.model.ForumSubscription;
import org.smartreaction.boardgamegeek.model.GeekListSubscription;
import org.smartreaction.boardgamegeek.model.Subscriptions;
import org.smartreaction.boardgamegeek.util.DateUtil;

import javax.ejb.Stateless;
import javax.ws.rs.core.Cookie;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Stateless
public class SubscriptionUtil
{
    public Subscriptions getSubscriptions(List<Cookie> cookies)
    {
        Subscriptions subscriptions = new Subscriptions();

        try {
            Connection connection = Jsoup.connect(BoardGameGeekConstants.BBG_WEBSITE + "/subscriptions");
            connection = connection.timeout(10000);
            for (Cookie cookie : cookies) {
                connection = connection.cookie(cookie.getName(), cookie.getValue());
            }
            Document doc = connection.post();

            Elements forumRows = doc.select("#module_1 tr");
            if (!forumRows.isEmpty()) {
                for (Element forumRow : forumRows) {
                    try {
                        ForumSubscription forumSubscription = getForumSubscription(forumRow);
                        if (forumSubscription != null) {
                            subscriptions.getForumSubscriptions().add(forumSubscription);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            Elements geekListRows = doc.select("#module_2 tr");
            if (!geekListRows.isEmpty()) {
                for (Element geekListRow : geekListRows) {
                    try {
                        GeekListSubscription geekListSubscription = getGeekListSubscription(geekListRow);
                        if (geekListSubscription != null) {
                            subscriptions.getGeekListSubscriptions().add(geekListSubscription);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }

        return subscriptions;
    }

    private ForumSubscription getForumSubscription(Element forumRow) throws ParseException
    {
        ForumSubscription forumSubscription = new ForumSubscription();
        String idString = forumRow.attr("id");
        if (StringUtils.isEmpty(idString)) {
            return null;
        }
        String prefix = "GSUB_itemline_thread_";
        long threadId = Long.parseLong(idString.substring(prefix.length()));
        forumSubscription.setThreadId(threadId);

        Elements columns = forumRow.getElementsByTag("td");

        Element forumColumn = columns.get(0);
        Element forumSpan = forumColumn.getElementsByTag("span").first();
        Elements forumLinks = forumSpan.getElementsByTag("a");
        Element forumLink = forumLinks.get(0);

        String gameUrl = forumLink.attr("href");
        String gameIdSubString = gameUrl.substring(gameUrl.indexOf("/boardgame/") + 11);
        try {
            long gameId = Long.parseLong(gameIdSubString.substring(0, gameIdSubString.indexOf("/")));
            forumSubscription.setGameId(gameId);
        } catch (NumberFormatException ignored) {}

        String game = forumLink.text();
        forumSubscription.setGame(game);

        if (forumLinks.size() > 1) {
            Element forumParentLink = forumLinks.get(1);
            forumSubscription.setForumName(forumParentLink.text());

            String forumUrl = forumParentLink.attr("href");
            String forumIdSubString = forumUrl.substring(forumUrl.indexOf("/forum/") + 7);
            try {
                long forumId = Long.parseLong(forumIdSubString.substring(0, forumIdSubString.indexOf("/")));
                forumSubscription.setForumId(forumId);
            } catch (NumberFormatException ignored) {}
        }

        Element subjectColumn = columns.get(1);
        Element subjectSpan = subjectColumn.getElementsByTag("span").last();
        Element subjectLink = subjectSpan.getElementsByTag("a").first();
        String subject = subjectLink.text();
        forumSubscription.setSubject(subject);

        Element updatedColumn = columns.get(4);
        Elements updatedColumnDivs = updatedColumn.getElementsByTag("div");
        Element lastUpdatedDiv = null;
        for (Element updatedColumnDiv : updatedColumnDivs) {
            if (updatedColumnDiv.text().startsWith("from ")) {
                lastUpdatedDiv = updatedColumnDiv;
                break;
            }
        }
        if (lastUpdatedDiv != null) {
            String lastUpdatedString = lastUpdatedDiv.text();
            lastUpdatedString = lastUpdatedString.substring(5);
            Date lastUpdated = DateUtil.getDateFromBggString(lastUpdatedString);
            forumSubscription.setLastUpdated(lastUpdated);
        }

        return forumSubscription;
    }

    private GeekListSubscription getGeekListSubscription(Element geekListRow) throws ParseException
    {
        GeekListSubscription geekListSubscription = new GeekListSubscription();
        String idString = geekListRow.attr("id");
        if (StringUtils.isEmpty(idString)) {
            return null;
        }
        String prefix = "GSUB_itemline_geeklist_";
        long geekListId = Long.parseLong(idString.substring(prefix.length()));
        geekListSubscription.setGeekListId(geekListId);

        Elements columns = geekListRow.getElementsByTag("td");

        Element titleColumn = columns.get(0);
        Element titleSpan = titleColumn.getElementsByTag("span").last();
        Element titleLink = titleSpan.getElementsByTag("a").first();
        String title = titleLink.text();
        geekListSubscription.setTitle(title);

        Element updatedColumn = columns.get(3);
        Elements updatedColumnDivs = updatedColumn.getElementsByTag("div");
        Element lastUpdatedDiv = null;
        for (Element updatedColumnDiv : updatedColumnDivs) {
            if (updatedColumnDiv.text().startsWith("from ")) {
                lastUpdatedDiv = updatedColumnDiv;
                break;
            }
        }
        if (lastUpdatedDiv != null) {
            String lastUpdatedString = lastUpdatedDiv.text();
            lastUpdatedString = lastUpdatedString.substring(5);
            Date lastUpdated = DateUtil.getDateFromBggString(lastUpdatedString);
            geekListSubscription.setLastUpdated(lastUpdated);
        }

        return geekListSubscription;
    }
}
