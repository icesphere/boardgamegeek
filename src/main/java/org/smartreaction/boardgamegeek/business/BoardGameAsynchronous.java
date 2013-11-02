package org.smartreaction.boardgamegeek.business;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.GameComment;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

@Stateless
public class BoardGameAsynchronous
{
    @EJB
    BoardGameUtil boardGameUtil;

    @Asynchronous
    public void refreshGame(Game game, BoardGameCache boardGameCache)
    {
        try {
            boardGameUtil.syncGame(game);
            boardGameCache.games.put(game.getId(), game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Asynchronous
    public void refreshGameComments(Game game, BoardGameCache boardGameCache)
    {
        List<GameComment> gameComments = boardGameUtil.loadGameComments(game);
        boardGameCache.gameComments.put(game.getId(), gameComments);
    }

    @Asynchronous
    public void asynchronousPost(WebResource.Builder builder, MultivaluedMap<String, String> formParams)
    {
        builder.post(ClientResponse.class, formParams);
    }
}
