package org.smartreaction.boardgamegeek.view;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.business.BoardGameAsynchronous;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.UserGame;
import org.smartreaction.boardgamegeek.model.Play;
import org.smartreaction.boardgamegeek.util.BoardGameGeekUtil;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import java.text.SimpleDateFormat;

@ManagedBean
@SessionScoped
public class BoardGameGeek {
    @EJB
    BoardGameGeekUtil boardGameGeekUtil;

    @EJB
    BoardGameAsynchronous boardGameAsynchronous;

    @ManagedProperty(value="#{userSession}")
    UserSession userSession;

    @ManagedProperty(value="#{boardGameGeekClient}")
    BoardGameGeekClient boardGameGeekClient;

    public String logPlay(Play play)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
        formParams.add("dummy", "1");
        formParams.add("ajax", "1");
        formParams.add("action", "save");
        formParams.add("version", "2");
        formParams.add("objecttype", "thing");
        if (play.getPlayId() > 0) {
            formParams.add("playid", String.valueOf(play.getPlayId()));
        }
        formParams.add("objectid", String.valueOf(play.getGameId()));
        formParams.add("playdate", simpleDateFormat.format(play.getPlayDate()));
        formParams.add("dateinput", simpleDateFormat.format(play.getDateInput()));
        formParams.add("length", String.valueOf(play.getLength()));
        formParams.add("location", play.getLocation());
        formParams.add("quantity", String.valueOf(play.getQuantity()));
        formParams.add("incomplete", play.isIncomplete() ? "1" : "0");
        formParams.add("nowinstats", play.isNoWinStats() ? "1" : "0");
        formParams.add("comments", play.getComments());

        WebResource.Builder builder = boardGameGeekClient.getClient().resource(BoardGameGeekConstants.BBG_WEBSITE + "/geekplay.php").type("application/x-www-form-urlencoded");

        builder = getBuilderWithCookies(builder);

        ClientResponse response = builder.post(ClientResponse.class, formParams);

        int status = response.getStatus();
        if (status == 200) {
            String responseHtml = response.getEntity(String.class);

            if (!play.isExpansion()) {
                for (Long gameId : play.getSelectedExpansions().keySet()) {
                    if ("true".equals(play.getSelectedExpansions().get(gameId))) {
                        logPlay(new Play(play, gameId));
                    }
                }
            }
            return boardGameGeekUtil.parsePlayResponse(responseHtml);
        }
        else {
            return "failed";
        }
    }

    public void markSubscriptionAsRead(long id, boolean forumSubscription)
    {
        MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
        formParams.add("action", "markallread");
        formParams.add("dummy", "1");
        formParams.add("ajax", "1");
        if (forumSubscription) {
            formParams.add("instanceid", "1");
        }
        else {
            formParams.add("instanceid", "2");
        }
        formParams.add("moduletype", "groupobject");
        formParams.add("objectid", String.valueOf(id));
        if (forumSubscription) {
            formParams.add("objecttype", "thread");
        }
        else {
            formParams.add("objecttype", "geeklist");
        }
        formParams.add("ottype", "groupobject");
        formParams.add("pageid", "1");
        formParams.add("sort", "");
        formParams.add("sortdir", "desc");
        formParams.add("viewfilter", "new");

        WebResource.Builder builder = boardGameGeekClient.getClient().resource(BoardGameGeekConstants.BBG_WEBSITE + "/geekviews.php").type("application/x-www-form-urlencoded");

        builder = getBuilderWithCookies(builder);

        boardGameAsynchronous.asynchronousPost(builder, formParams);
    }

    public String changeGameRating(long gameId, float rating)
    {
        UserGame userGame = userSession.getUserGamesMap().get(gameId);

        MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
        formParams.add("action", "savedata");
        formParams.add("dummy", "1");
        formParams.add("ajax", "1");
        formParams.add("fieldname", "rating");
        formParams.add("collid", String.valueOf(userGame.getCollectionId()));
        formParams.add("objecttype", "thing");
        formParams.add("objectid", String.valueOf(gameId));
        formParams.add("rating", String.valueOf(rating));
        formParams.add("B1", "Cancel");

        WebResource.Builder builder = boardGameGeekClient.getClient().resource(BoardGameGeekConstants.BBG_WEBSITE + "/geekcollection.php").type("application/x-www-form-urlencoded");

        builder = getBuilderWithCookies(builder);

        ClientResponse response = builder.post(ClientResponse.class, formParams);

        int status = response.getStatus();
        if (status == 200) {
            userGame.setRating(rating);
            userSession.updateUserGame(userGame);
            return "success";
        }
        else {
            return "failed";
        }
    }

    public String recommendGeeklist(long id, boolean recommend)
    {
        MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
        formParams.add("action", recommend?"recommend":"unrecommend");
        formParams.add("dummy", "1");
        formParams.add("itemid", String.valueOf(id));
        formParams.add("itemtype", "geeklist");
        formParams.add("value", "1");

        WebResource.Builder builder = boardGameGeekClient.getClient().resource(BoardGameGeekConstants.BBG_WEBSITE + "/geekrecommend.php").type("application/x-www-form-urlencoded");

        builder = getBuilderWithCookies(builder);

        ClientResponse response = builder.post(ClientResponse.class, formParams);

        int status = response.getStatus();
        if (status == 200) {
            return "success";
        }
        else {
            return "failed";
        }
    }

    public boolean addGameToCollection(Game game)
    {
        MultivaluedMap<String, String> formParams = new MultivaluedMapImpl();
        formParams.add("ajax", "1");
        formParams.add("action", "additem");
        formParams.add("addowned", "true");
        formParams.add("instanceid", "22");
        formParams.add("objecttype", "thing");
        formParams.add("objectid", String.valueOf(game.getId()));

        WebResource.Builder builder = boardGameGeekClient.getClient().resource(BoardGameGeekConstants.BBG_WEBSITE + "/geekcollection.php").type("application/x-www-form-urlencoded");

        builder = getBuilderWithCookies(builder);

        ClientResponse response = builder.post(ClientResponse.class, formParams);

        int status = response.getStatus();

        return status == 200;
    }

    private WebResource.Builder getBuilderWithCookies(WebResource.Builder builder)
    {
        for (Cookie cookie : userSession.getCookies()) {
            builder = builder.cookie(cookie);
        }
        return builder;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGameGeekClient(BoardGameGeekClient boardGameGeekClient)
    {
        this.boardGameGeekClient = boardGameGeekClient;
    }
}
