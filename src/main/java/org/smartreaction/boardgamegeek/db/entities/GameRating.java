package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.*;

@Entity
@Table(name = "game_ratings")
public class GameRating
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "game_id")
    private long gameId;

    @Column
    private String username;

    @Column
    private float rating;

    @Transient
    private boolean loading;

    @Transient
    private boolean loaded;

    @Transient
    private boolean error;

    @Transient
    private int gamesInCommon;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getGameId()
    {
        return gameId;
    }

    public void setGameId(long gameId)
    {
        this.gameId = gameId;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public float getRating()
    {
        return rating;
    }

    public void setRating(float rating)
    {
        this.rating = rating;
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

    public int getGamesInCommon()
    {
        return gamesInCommon;
    }

    public void setGamesInCommon(int gamesInCommon)
    {
        this.gamesInCommon = gamesInCommon;
    }
}
