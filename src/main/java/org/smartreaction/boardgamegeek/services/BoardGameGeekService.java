package org.smartreaction.boardgamegeek.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.db.dao.GameDao;
import org.smartreaction.boardgamegeek.db.entities.GameComment;
import org.smartreaction.boardgamegeek.db.entities.User;
import org.smartreaction.boardgamegeek.model.ForumThread;
import org.smartreaction.boardgamegeek.model.ThreadArticle;
import org.smartreaction.boardgamegeek.xml.collection.Items;
import org.smartreaction.boardgamegeek.xml.forumlist.Forum;
import org.smartreaction.boardgamegeek.xml.forumlist.Forums;
import org.smartreaction.boardgamegeek.xml.game.Item;
import org.smartreaction.boardgamegeek.xml.geeklist.Geeklist;
import org.smartreaction.boardgamegeek.xml.plays.Play;
import org.smartreaction.boardgamegeek.xml.plays.Plays;
import org.smartreaction.boardgamegeek.xml.thread.Article;
import org.smartreaction.boardgamegeek.xml.thread.Thread;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Stateless
public class BoardGameGeekService
{
    @EJB
    GameDao gameDao;

    public Items getCollection(User user) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.collection");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        String urlString = "http://boardgamegeek.com/xmlapi2/collection?username=" + user.getUsername() + "&stats=1";
        if (user.getCollectionLastUpdated() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd%20HH:mm:ss");
            urlString += "&modifiedsince=" + sdf.format(user.getCollectionLastUpdated());
        }
        URL url = new URL(urlString);
        return (Items) unmarshaller.unmarshal(url);
    }

    public Items getCollectionTopGames(User user) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.collection");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        String urlString = "http://boardgamegeek.com/xmlapi2/collection?username=" + user.getUsername() + "&stats=1&brief=1&minrating=8";
        if (user.getTopGamesLastUpdated() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd%20HH:mm:ss");
            urlString += "&modifiedsince=" + sdf.format(user.getTopGamesLastUpdated());
        }
        URL url = new URL(urlString);
        return (Items) unmarshaller.unmarshal(url);
    }

    public Items getBriefCollection(String username) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.collection");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/collection?username=" + username + "&stats=1&brief=1");
        return (Items) unmarshaller.unmarshal(url);
    }

    public Item getGame(long gameId) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.game");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/thing?id=" + gameId + "&stats=1");
        org.smartreaction.boardgamegeek.xml.game.Items items = (org.smartreaction.boardgamegeek.xml.game.Items) unmarshaller.unmarshal(url);
        return items.getItem();
    }

    public org.smartreaction.boardgamegeek.xml.gamewithrating.Item getGameWithRatings(long gameId) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.gamewithrating");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/thing?id=" + gameId + "&ratingcomments=1");
        org.smartreaction.boardgamegeek.xml.gamewithrating.Items items = (org.smartreaction.boardgamegeek.xml.gamewithrating.Items) unmarshaller.unmarshal(url);
        return items.getItem();
    }

    public org.smartreaction.boardgamegeek.xml.gamewithrating.Item getGameWithComments(long gameId, int page)
    {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.gamewithrating");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            URL url = new URL("http://boardgamegeek.com/xmlapi2/thing?id=" + gameId + "&comments=1&page=" + page);
            org.smartreaction.boardgamegeek.xml.gamewithrating.Items items = (org.smartreaction.boardgamegeek.xml.gamewithrating.Items) unmarshaller.unmarshal(url);
            return items.getItem();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Geeklist getGeekList(long geekListId) throws JAXBException, IOException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.geeklist");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi/geeklist/" + geekListId + "?comments=1");
        URLConnection con = url.openConnection();
        con.setConnectTimeout(20000);
        con.setReadTimeout(120000);
        InputStream in = con.getInputStream();
        return (Geeklist) unmarshaller.unmarshal(in);
    }

    public org.smartreaction.boardgamegeek.xml.hotgames.Items getHotGames()
    {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.hotgames");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            URL url = new URL("http://boardgamegeek.com/xmlapi2/hot?type=boardgame");
            return (org.smartreaction.boardgamegeek.xml.hotgames.Items) unmarshaller.unmarshal(url);
        }
        catch (Throwable t) {
            System.out.println("Error loading hot games");
            t.printStackTrace();
            return null;
        }
    }

    public List<Long> getTopGameIds()
    {
        try {
            List<Long> gameIds = new ArrayList<>();
            Document doc = Jsoup.connect(BoardGameGeekConstants.BGG_WEBSITE + "/browse/boardgame").get();
            Elements gameElements = doc.select("td.collection_thumbnail");
            for (Element element : gameElements) {
                Element link = element.getElementsByTag("a").first();
                gameIds.add(getGameIdFromLink(link.attr("href")));
            }
            return gameIds;
        }
        catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    private Long getGameIdFromLink(String href)
    {
        int boardGameIndex = href.indexOf("/boardgame/");
        String afterBoardGameString;
        if (boardGameIndex != -1) {
            afterBoardGameString = href.substring(boardGameIndex + 11);
        }
        else {
            int boardGameExpansionIndex = href.indexOf("/boardgameexpansion/");
            afterBoardGameString = href.substring(boardGameExpansionIndex + 20);
        }
        int slashIndex = afterBoardGameString.indexOf("/");
        return Long.parseLong(afterBoardGameString.substring(0, slashIndex));
    }

    public List<GameComment> getRecentGameComments(long gameId, int page) throws IOException
    {
        List<GameComment> comments = new ArrayList<>();

        Document doc = Jsoup.connect(BoardGameGeekConstants.BGG_WEBSITE + "/collection/items/boardgame/" + gameId + "/page/" + page + "?comment=1&sort=comment_tstamp&sortdir=desc").timeout(10000).get();

        Element commentTable = doc.getElementsByClass("forum_table").first();

        Elements commentRows = commentTable.children().first().children();

        boolean first = true;

        for (Element commentRow : commentRows) {
            try {
                if (!first) {
                    Elements commentColumns = commentRow.children();

                    GameComment comment = new GameComment();
                    comment.setGameId(gameId);
                    comment.setUsername(getUsername(commentColumns.get(0)));
                    comment.setRating(getRating(commentColumns.get(1)));
                    comment.setComment(getComment(commentColumns.get(2)));
                    comment.setCommentDate(getCommentDate(commentColumns.get(2)));

                    gameDao.createGameComment(comment);

                    comments.add(comment);
                }
                else {
                    first = false;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return comments;
    }

    private String getUsername(Element usernameColumn)
    {
        Element usernameElement = usernameColumn.getElementsByClass("username").get(0);

        return usernameElement.getElementsByTag("a").get(0).text();
    }

    private float getRating(Element ratingColumn)
    {
        try {
            return Float.parseFloat(ratingColumn.getElementsByClass("ratingtext").text());
        }
        catch (NumberFormatException e) {
            return 0;
        }
    }

    private String getComment(Element commentColumn)
    {
        try {
            return commentColumn.children().get(0).children().get(0).text();
        }
        catch (Exception e) {
            return "";
        }
    }

    private Date getCommentDate(Element commentColumn)
    {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(commentColumn.children().get(0).children().get(1).text().trim());
        }
        catch (Exception e) {
            return null;
        }
    }

    public List<Long> searchGames(String searchString, boolean exactSearch)
    {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.gamesearch");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            String queryString = "http://boardgamegeek.com/xmlapi2/search?type=boardgame&query=" + searchString;
            if (exactSearch) {
                queryString += "&exact=1";
            }
            URL url = new URL(queryString);
            org.smartreaction.boardgamegeek.xml.gamesearch.Items items = (org.smartreaction.boardgamegeek.xml.gamesearch.Items) unmarshaller.unmarshal(url);
            List<Long> gameIds = new ArrayList<Long>(items.getItem().size());
            for (org.smartreaction.boardgamegeek.xml.gamesearch.Item item : items.getItem()) {
                gameIds.add(item.getId().longValue());
            }
            return gameIds;
        }
        catch (Throwable t) {
            System.out.println("Error searching games");
            t.printStackTrace();
            return null;
        }
    }

    public ForumThread getThread(long threadId)
    {
        try {
            Thread thread = getThread(threadId, null);
            ForumThread forumThread = new ForumThread();
            forumThread.setThreadId(thread.getId().longValue());
            forumThread.setSubject(thread.getSubject());
            List<ThreadArticle> articles = new ArrayList<ThreadArticle>(thread.getArticles().getArticle().size());
            for (Article article : thread.getArticles().getArticle()) {
                ThreadArticle threadArticle = new ThreadArticle();
                threadArticle.setId(article.getId().longValue());
                threadArticle.setUsername(article.getUsername());
                threadArticle.setContent(article.getBody());
                threadArticle.setPostDate(getThreadDate(article));
                articles.add(threadArticle);
            }
            forumThread.setArticles(articles);
            return forumThread;
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Date getThreadDate(Article article) throws ParseException
    {
        String postDateString = article.getPostdate();
        postDateString = postDateString.substring(0, postDateString.lastIndexOf("-"));
        postDateString = postDateString.replace("T", " ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(postDateString);
    }

    public Thread getThread(long threadId, Long minArticleId)
    {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.thread");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            String urlString = "http://boardgamegeek.com/xmlapi2/thread?id=" + threadId;
            if (minArticleId != null) {
                urlString += "&minarticleid=" + minArticleId;
            }
            URL url = new URL(urlString);
            return (Thread) unmarshaller.unmarshal(url);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Play> getPlays(String username, Long gameId, Date startPlayDate) throws JAXBException, MalformedURLException
    {
        List<Play> allPlays = new ArrayList<>();
        int page = 1;
        boolean finished = false;
        while (!finished) {
            List<Play> plays = getPlays(username, gameId, startPlayDate, page);
            if (!plays.isEmpty()) {
                allPlays.addAll(plays);
                page++;
            }
            else {
                finished = true;
            }
        }

        return allPlays;
    }

    public List<Play> getPlays(String username, Long gameId, Date startPlayDate, int page) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.plays");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StringBuilder sb = new StringBuilder("http://boardgamegeek.com/xmlapi2/plays?username=");
        sb.append(username);
        if (gameId != null) {
            sb.append("&id=").append(gameId);
        }
        if (startPlayDate != null) {
            sb.append("&mindate=");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sb.append(sdf.format(startPlayDate));
        }
        sb.append("&page=").append(page);
        URL url = new URL(sb.toString());
        return ((Plays) unmarshaller.unmarshal(url)).getPlay();
    }

    public List<Forum> getForumList(long gameId) throws JAXBException, IOException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.forumlist");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/forumlist/?type=thing&id=" + gameId);
        URLConnection con = url.openConnection();
        con.setConnectTimeout(20000);
        con.setReadTimeout(120000);
        InputStream in = con.getInputStream();
        return ((Forums) unmarshaller.unmarshal(in)).getForum();
    }

    public List<org.smartreaction.boardgamegeek.xml.forum.Thread> getForumThreads(long forumId)
    {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.forum");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            URL url = new URL("http://boardgamegeek.com/xmlapi2/forum?id=" + forumId);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(20000);
            con.setReadTimeout(120000);
            InputStream in = con.getInputStream();
            return ((org.smartreaction.boardgamegeek.xml.forum.Forum) unmarshaller.unmarshal(in)).getThreads().getThread();
        }
        catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
