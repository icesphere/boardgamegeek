package org.smartreaction.boardgamegeek.business;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BoardGameGeekUtil
{
    BoardGameCache boardGameCache;

    public BoardGameGeekUtil(BoardGameCache boardGameCache)
    {
        this.boardGameCache = boardGameCache;
    }

    public String getBoardGameGeekHtml(String html)
    {
        Tidy tidy = new Tidy();
        tidy.setXmlOut(true);
        tidy.setPrintBodyOnly(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        tidy.parse(new StringReader(html), ps);
        try {
            return baos.toString("UTF8");
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String convertBoardGameGeekXmlText(String text)
    {
        text = StringEscapeUtils.unescapeHtml(text);

        String imagePattern = "\\[ImageID=(\\d+).*?\\]";
        Pattern pattern = Pattern.compile(imagePattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        StringBuffer result = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(result, "<div style=\"display:inline\"><img src=\"http://cf.geekdo-images.com/images/pic" + matcher.group(1) + "_t.jpg\"/></div>");
        }
        matcher.appendTail(result);
        text = result.toString();

        String boldStartPattern = "\\[b\\]";
        pattern = Pattern.compile(boldStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span style=\"font-weight:bold\">");
        }

        String boldEndPattern = "\\[/b\\]";
        pattern = Pattern.compile(boldEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</span>");
        }

        String quoteStartPattern = "\\[q\\]";
        pattern = Pattern.compile(quoteStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span class=\"quote\">Quote: ");
        }

        String quoteWithPersonStartPattern = "\\[q=\"(.*?)\"\\]";
        pattern = Pattern.compile(quoteWithPersonStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span class=\"quote\">" + matcher.group(1) + " wrote: ");
        }

        String quoteEndPattern = "\\[/q\\]";
        pattern = Pattern.compile(quoteEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</span>");
        }

        String italicsStartPattern = "\\[i\\]";
        pattern = Pattern.compile(italicsStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span style=\"font-style:italic\">");
        }

        String italicsEndPattern = "\\[/i\\]";
        pattern = Pattern.compile(italicsEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</span>");
        }

        String underlineStartPattern = "\\[u\\]";
        pattern = Pattern.compile(underlineStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span style=\"text-decoration:underline\">");
        }

        String underlineEndPattern = "\\[/u\\]";
        pattern = Pattern.compile(underlineEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</span>");
        }

        String urlStartPattern = "\\[url=(.*?)\\]";
        pattern = Pattern.compile(urlStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<a href=\"" + matcher.group(1) + "\">");
        }

        String urlEndPattern = "\\[/url\\]";
        pattern = Pattern.compile(urlEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</a>");
        }

        String colorStartPattern = "\\[COLOR=([^#].*?)\\]";
        pattern = Pattern.compile(colorStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span style=\"color:" + matcher.group(1) + "\">");
        }

        String colorHexStartPattern = "\\[COLOR=#(.*?)\\]";
        pattern = Pattern.compile(colorHexStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span style=\"color:#" + matcher.group(1) + "\">");
        }

        String colorEndPattern = "\\[/COLOR\\]";
        pattern = Pattern.compile(colorEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</span>");
        }

        String bgColorStartPattern = "\\[BGCOLOR=([^#].*?)\\]";
        pattern = Pattern.compile(bgColorStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span style=\"background-color:" + matcher.group(1) + "\">");
        }

        String bgColorHexStartPattern = "\\[BGCOLOR=#(.*?)\\]";
        pattern = Pattern.compile(bgColorHexStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span style=\"background-color:#" + matcher.group(1) + "\">");
        }

        String bgColorEndPattern = "\\[/BGCOLOR\\]";
        pattern = Pattern.compile(bgColorEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</span>");
        }

        String sizeStartPattern = "\\[size=(.*?)\\]";
        pattern = Pattern.compile(sizeStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<span font-size=\"" + matcher.group(1) + "px\">");
        }

        String sizeEndPattern = "\\[/size\\]";
        pattern = Pattern.compile(sizeEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</span>");
        }

        String floatLeftStartPattern = "\\[floatleft\\]";
        pattern = Pattern.compile(floatLeftStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<div style=\"float:left\">");
        }

        String floatLeftEndPattern = "\\[/floatleft\\]";
        pattern = Pattern.compile(floatLeftEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</div>");
        }

        String floatRightStartPattern = "\\[floatright\\]";
        pattern = Pattern.compile(floatRightStartPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<div style=\"float:right\">");
        }

        String floatRightEndPattern = "\\[/floatright\\]";
        pattern = Pattern.compile(floatRightEndPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</div>");
        }

        String geekListPattern = "\\[geeklist=(\\d+)\\](.*?)\\[/geeklist\\]";
        pattern = Pattern.compile(geekListPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        result = new StringBuffer();
        while (matcher.find()) {
            String anchorText = matcher.group(2);
            if (StringUtils.isEmpty(anchorText)) {
                matcher.appendReplacement(result, "<a href=\"geeklist.xhtml?id=" + matcher.group(1) + "\">geeklist "+matcher.group(1)+"</a>");
            }
            else {
                matcher.appendReplacement(result, "<a href=\"geeklist.xhtml?id=" + matcher.group(1) + "\">"+anchorText+"</a>");
            }
        }
        matcher.appendTail(result);
        text = result.toString();

        String thingPattern = "\\[thing=(\\d+)\\](.*?)\\[/thing\\]";
        pattern = Pattern.compile(thingPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        result = new StringBuffer();
        while (matcher.find()) {
            String thingText = matcher.group(2);
            if (StringUtils.isEmpty(thingText)) {
                try {
                    Long gameId = Long.valueOf(matcher.group(1));
                    Game game = boardGameCache.getGame(gameId);
                    matcher.appendReplacement(result, "<a href=\"game.xhtml?id=" + gameId + "\">" + game.getName() + "</a>");
                }
                catch (Exception e) {
                    matcher.appendReplacement(result, "<a href=\"game.xhtml?id=" + matcher.group(1) + "\">boardgame " + matcher.group(1) + "</a>");
                }
            }
            else {
                matcher.appendReplacement(result, "<a href=\"game.xhtml?id=" + matcher.group(1) + "\">"+thingText+"</a>");
            }
        }
        matcher.appendTail(result);
        text = result.toString();

        String gameIdPattern = "\\[gameid=(\\d+)\\]";
        pattern = Pattern.compile(gameIdPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        result = new StringBuffer();
        while (matcher.find()) {
            try {
                Long gameId = Long.valueOf(matcher.group(1));
                Game game = boardGameCache.getGame(gameId);
                matcher.appendReplacement(result, "<a href=\"game.xhtml?id=" + gameId + "\">" + game.getName() + "</a>");
            }
            catch (Exception e) {
                matcher.appendReplacement(result, "<a href=\"game.xhtml?id=" + matcher.group(1) + "\">boardgame " + matcher.group(1) + "</a>");
            }
        }
        matcher.appendTail(result);
        text = result.toString();

        String userPattern = "\\[user=(.*?)\\](.*?)\\[/user\\]";
        pattern = Pattern.compile(userPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        result = new StringBuffer();
        while (matcher.find()) {
            String anchorText = matcher.group(2);
            if (StringUtils.isEmpty(anchorText)) {
                matcher.appendReplacement(result, "<a href=\"" + BoardGameGeekConstants.BBG_WEBSITE + "/user/" + matcher.group(1) + "\">" + matcher.group(1) + "</a>");
            }
            else {
                matcher.appendReplacement(result, "<a href=\"" + BoardGameGeekConstants.BBG_WEBSITE + "/user/" + matcher.group(1) + "\">" + anchorText + "</a>");
            }
        }
        matcher.appendTail(result);
        text = result.toString();

        String strikeStartPattern = "\\[\\-\\]";
        pattern = Pattern.compile(strikeStartPattern);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<strike>");
        }

        String strikeEndPattern = "\\[/\\-\\]";
        pattern = Pattern.compile(strikeEndPattern);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("</strike>");
        }

        String clearPattern = "\\[clear\\]";
        pattern = Pattern.compile(clearPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<div style=\"clear:both\"></div>");
        }

        String hrPattern = "\\[hr\\]";
        pattern = Pattern.compile(hrPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            text = matcher.replaceAll("<hr/>");
        }

        String threadPattern = "\\[thread=(\\d+)\\](.*?)\\[/thread\\]";
        pattern = Pattern.compile(threadPattern, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(text);
        result = new StringBuffer();
        while (matcher.find()) {
            String threadText = matcher.group(2);
            if (StringUtils.isEmpty(threadText)) {
                matcher.appendReplacement(result, "<a href=\"" + BoardGameGeekConstants.BBG_WEBSITE + "/thread/" + matcher.group(1) + "\">thread " + matcher.group(1) + "</a>");
            }
            else {
                matcher.appendReplacement(result, "<a href=\"" + BoardGameGeekConstants.BBG_WEBSITE + "/thread/" + matcher.group(1) + "\">" + threadText + "</a>");
            }
        }
        matcher.appendTail(result);
        text = result.toString();

        return text;
    }
}
