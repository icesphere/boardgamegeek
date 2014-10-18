package org.smartreaction.boardgamegeek.business;

import junit.framework.TestCase;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.model.GeekList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class GeekListUtilTest extends TestCase
{
    public void testGeekListJson() throws IOException
    {
        for (int j=0; j<30; j++) {
            List<GeekList> geekLists = new ArrayList<>();

            GeekListUtil geekListUtil = new GeekListUtil();

            String url = BoardGameGeekConstants.BGG_WEBSITE + "/geeklist/module?ajax=1&domain=boardgame&nosession=1&objectid=0&objecttype=&pageid=" + 1 + "&showcontrols=1&showcount=12&sort=hot&tradelists=0&version=v2";

            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("Accept", "application/json, text/plain, */*");

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
                    geekLists.add(geekListUtil.getGeekList(item));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                for (String headerKey : connection.getHeaderFields().keySet()) {
                    System.out.println(headerKey + ": " + connection.getHeaderField(headerKey));
                }
            }

            assertFalse(geekLists.isEmpty());
        }
    }
}
