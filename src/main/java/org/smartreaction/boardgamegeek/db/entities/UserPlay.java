package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_plays")
public class UserPlay
{
    @Id
    @Column(name = "play_id")
    private long playId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "game_id")
    private long gameId;

    @Column
    private int quantity;

    @Column(name = "play_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date playDate;

    @Transient
    private Game game;

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getPlayId()
    {
        return playId;
    }

    public void setPlayId(long playId)
    {
        this.playId = playId;
    }

    public long getGameId()
    {
        return gameId;
    }

    public void setGameId(long gameId)
    {
        this.gameId = gameId;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public Date getPlayDate()
    {
        return playDate;
    }

    public void setPlayDate(Date playDate)
    {
        this.playDate = playDate;
    }

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }
}
