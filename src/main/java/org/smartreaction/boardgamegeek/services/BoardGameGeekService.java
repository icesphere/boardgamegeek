package org.smartreaction.boardgamegeek.services;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.model.ForumThread;
import org.smartreaction.boardgamegeek.model.ThreadArticle;
import org.smartreaction.boardgamegeek.xml.collection.Items;
import org.smartreaction.boardgamegeek.xml.game.Item;
import org.smartreaction.boardgamegeek.xml.geeklist.Geeklist;
import org.smartreaction.boardgamegeek.xml.plays.Play;
import org.smartreaction.boardgamegeek.xml.plays.Plays;
import org.smartreaction.boardgamegeek.xml.thread.Article;
import org.smartreaction.boardgamegeek.xml.thread.Thread;

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
    public Items getCollection(String username) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.collection");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/collection?username="+username+"&stats=1");
        return (Items) unmarshaller.unmarshal(url);
    }

    public Items getCollectionTopGames(String username) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.collection");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/collection?username="+username+"&stats=1&brief=1&minrating=8");
        return (Items) unmarshaller.unmarshal(url);
    }

    public Items getBriefCollection(String username) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.collection");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/collection?username="+username+"&stats=1&brief=1");
        return (Items) unmarshaller.unmarshal(url);
    }

    public Item getGame(long gameId) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.game");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/thing?id=" + gameId+"&stats=1");
        org.smartreaction.boardgamegeek.xml.game.Items items = (org.smartreaction.boardgamegeek.xml.game.Items) unmarshaller.unmarshal(url);
        return items.getItem();
    }

    public org.smartreaction.boardgamegeek.xml.gamewithrating.Item getGameWithRatings(long gameId) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.gamewithrating");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        URL url = new URL("http://boardgamegeek.com/xmlapi2/thing?id=" + gameId+"&ratingcomments=1");
        org.smartreaction.boardgamegeek.xml.gamewithrating.Items items = (org.smartreaction.boardgamegeek.xml.gamewithrating.Items) unmarshaller.unmarshal(url);
        return items.getItem();
    }

    public org.smartreaction.boardgamegeek.xml.gamewithrating.Item getGameWithComments(long gameId, int page)
    {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.gamewithrating");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            URL url = new URL("http://boardgamegeek.com/xmlapi2/thing?id=" + gameId+"&comments=1&page="+page);
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
        URL url = new URL("http://boardgamegeek.com/xmlapi/geeklist/"+geekListId+"?comments=1");
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
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Long> getTopGameIds()
    {
        List<Long> gameIds = new ArrayList<Long>();
        try {
            Document doc = Jsoup.connect(BoardGameGeekConstants.BBG_WEBSITE + "/browse/boardgame").get();
            Elements gameElements = doc.select("td.collection_thumbnail");
            for (Element element : gameElements) {
                Element link = element.getElementsByTag("a").first();
                gameIds.add(getGameIdFromLink(link.attr("href")));
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return gameIds;
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

    public List<Long> searchGames(String searchString)
    {
        try {
            JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.gamesearch");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            URL url = new URL("http://boardgamegeek.com/xmlapi2/search?type=boardgame&query="+searchString);
            org.smartreaction.boardgamegeek.xml.gamesearch.Items items = (org.smartreaction.boardgamegeek.xml.gamesearch.Items) unmarshaller.unmarshal(url);
            List<Long> gameIds = new ArrayList<Long>(items.getItem().size());
            for (org.smartreaction.boardgamegeek.xml.gamesearch.Item item : items.getItem()) {
                gameIds.add(item.getId().longValue());
            }
            return gameIds;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
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
            String urlString = "http://boardgamegeek.com/xmlapi2/thread?id="+threadId;
            if (minArticleId != null) {
                urlString += "&minarticleid="+minArticleId;
            }
            URL url = new URL(urlString);
            return (Thread) unmarshaller.unmarshal(url);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Play> getPlays(String username) throws MalformedURLException, JAXBException
    {
        return getPlays(username, null);
    }

    public List<Play> getPlays(String username, Long gameId) throws JAXBException, MalformedURLException
    {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.plays");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StringBuilder sb = new StringBuilder("http://boardgamegeek.com/xmlapi2/plays?username=");
        sb.append(username);
        if (gameId != null) {
            sb.append("&id=").append(gameId);
        }
        URL url = new URL(sb.toString());
        return ((Plays) unmarshaller.unmarshal(url)).getPlay();
    }
}
