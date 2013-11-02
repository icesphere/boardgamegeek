package org.smartreaction.boardgamegeek.business;

import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.GameComment;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class BoardGameRefresher
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
}
