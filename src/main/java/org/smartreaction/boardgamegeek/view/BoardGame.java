package org.smartreaction.boardgamegeek.view;

import org.omnifaces.util.Faces;
import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.db.entities.Game;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class BoardGame
{
    @ManagedProperty(value="#{userSession}")
    UserSession userSession;

    @EJB
    BoardGameCache boardGameCache;

    private Game game;

    private boolean gameLoaded;

    @PostConstruct
    public void setup()
    {
        long gameId = Long.parseLong(Faces.getRequestParameter("id"));
        loadGame(gameId);
        gameLoaded = true;
    }

    private void loadGame(long gameId)
    {
        if (game == null) {
            game = boardGameCache.getGame(gameId);
        }
        if (game == null) {
            throw new RuntimeException("Game not found");
        }
    }

    public void syncGame()
    {
        boardGameCache.syncGame(game);
        gameLoaded = true;
    }

    public void refreshGame()
    {
        gameLoaded = false;
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public boolean isGameLoaded()
    {
        return gameLoaded;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }
}
