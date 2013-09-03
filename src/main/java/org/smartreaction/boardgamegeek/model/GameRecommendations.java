package org.smartreaction.boardgamegeek.model;

import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.UserGame;

public class GameRecommendations
{
    private UserGame userGame;

    private Game game;

    private boolean loading;

    private boolean loaded;

    private boolean error;

    public UserGame getUserGame()
    {
        return userGame;
    }

    public void setUserGame(UserGame userGame)
    {
        this.userGame = userGame;
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public boolean isLoading()
    {
        return loading;
    }

    public void setLoading(boolean loading)
    {
        this.loading = loading;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public void setLoaded(boolean loaded)
    {
        this.loaded = loaded;
    }

    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }
}
