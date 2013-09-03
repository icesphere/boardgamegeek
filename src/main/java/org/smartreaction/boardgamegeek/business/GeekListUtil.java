package org.smartreaction.boardgamegeek.business;

import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.model.GeekList;
import org.smartreaction.boardgamegeek.model.GeekListComment;
import org.smartreaction.boardgamegeek.model.GeekListDetail;
import org.smartreaction.boardgamegeek.model.GeekListEntry;
import org.smartreaction.boardgamegeek.services.BoardGameGeekService;
import org.smartreaction.boardgamegeek.xml.geeklist.Comment;
import org.smartreaction.boardgamegeek.xml.geeklist.Geeklist;
import org.smartreaction.boardgamegeek.xml.geeklist.Item;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Stateless
public class GeekListUtil
{
    @EJB
    BoardGameGeekService boardGameGeekService;

    @EJB
    BoardGameCache boardGameCache;

    BoardGameGeekUtil boardGameGeekUtil;

    @PostConstruct
    public void setup()
    {
        boardGameGeekUtil = new BoardGameGeekUtil(boardGameCache);
    }

    public List<GeekList> getGeekLists(int page, String sort) throws IOException
    {
        List<GeekList> geekLists = new ArrayList<>();

        String url = BoardGameGeekConstants.BBG_WEBSITE + "/geeklist/module?ajax=1&domain=boardgame&nosession=1&objectid=0&objecttype=&pageid=" + page + "&showcontrols=1&showcount=12&sort=" + sort + "&tradelists=0&version=v2";

        URLConnection connection = new URL(url).openConnection();
        connection.setRequestProperty("Accept", "application/json, text/plain, */*");
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        InputStream response = connection.getInputStream();

        if ("gzip".equals(connection.getContentEncoding())) {
            response = new GZIPInputStream(response);
        }

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response, Charset.forName("UTF-8")));

        try (JsonReader reader = Json.createReader(bufferedReader)) {
            JsonObject jsonObject = reader.readObject();

            JsonArray lists = jsonObject.getJsonArray("lists");

            for (int i = 0; i < lists.size(); i++) {
                JsonObject item = lists.getJsonObject(i);
                geekLists.add(getGeekList(item));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            for (String headerKey : connection.getHeaderFields().keySet()) {
                System.out.println(headerKey + ": " + connection.getHeaderField(headerKey));
            }
        }

        return geekLists;
    }

    GeekList getGeekList(JsonObject item)
    {
        GeekList geekList = new GeekList();
        geekList.setThumbs(Integer.parseInt(item.getString("numpositive")));
        geekList.setTitle(item.getString("title"));
        String link = BoardGameGeekConstants.BBG_WEBSITE + item.getString("href");
        geekList.setId(getId(link));
        geekList.setLink(link);
        geekList.setCreator(item.getString("username"));
        geekList.setEntries(Integer.parseInt(item.getString("numitems")));
        return geekList;
    }

    private long getId(String link)
    {
        int start = link.indexOf("/geeklist/");
        return Long.valueOf(link.substring(start + 10, link.lastIndexOf("/")));
    }

    public GeekListDetail getGeekListDetail(long geekListId) throws IOException, JAXBException
    {
        Geeklist xmlGeekList = boardGameGeekService.getGeekList(geekListId);

        GeekListDetail detail = new GeekListDetail();
        detail.setGeekListId(geekListId);
        detail.setTitle(xmlGeekList.getTitle());
        detail.setDescription(boardGameGeekUtil.convertBoardGameGeekXmlText(xmlGeekList.getDescription()));
        detail.setUsername(xmlGeekList.getUsername());
        detail.setComments(getCommentsFromXmlComments(xmlGeekList.getComment()));
        detail.setEntries(getEntriesFromXmlItems(xmlGeekList.getItem()));
        detail.setThumbs(xmlGeekList.getThumbs().intValue());

        return detail;
    }

    private List<GeekListComment> getCommentsFromXmlComments(List<Comment> xmlComments)
    {
        List<GeekListComment> comments = new ArrayList<>(xmlComments.size());
        for (Comment xmlComment : xmlComments) {
            GeekListComment comment = new GeekListComment();
            comment.setComment(boardGameGeekUtil.convertBoardGameGeekXmlText(xmlComment.getContent()));
            comment.setUsername(xmlComment.getUsername());
            comment.setThumbs(xmlComment.getThumbs().intValue());
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            try {
                comment.setPostDate(sdf.parse(xmlComment.getPostdate()));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            comments.add(comment);
        }
        return comments;
    }

    private List<GeekListEntry> getEntriesFromXmlItems(List<Item> xmlItems)
    {
        List<GeekListEntry> entries = new ArrayList<>(xmlItems.size());
        for (Item xmlItem : xmlItems) {
            GeekListEntry entry = new GeekListEntry();
            entry.setGame(boardGameCache.getGame(xmlItem.getObjectid().longValue()));
            entry.setDescription(boardGameGeekUtil.convertBoardGameGeekXmlText(xmlItem.getBody()));
            entry.setComments(getCommentsFromXmlComments(xmlItem.getComment()));
            entry.setThumbs(xmlItem.getThumbs().intValue());
            entry.setUsername(xmlItem.getUsername());
            entry.setEntryId(xmlItem.getId().longValue());
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
            try {
                entry.setPostDate(sdf.parse(xmlItem.getPostdate()));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            entries.add(entry);
        }
        return entries;
    }
}
