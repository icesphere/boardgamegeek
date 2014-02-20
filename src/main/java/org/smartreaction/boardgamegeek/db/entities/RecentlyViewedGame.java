package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "recently_viewed_game")
public class RecentlyViewedGame
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "game_id")
    private long gameId;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "view_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date viewDate;

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

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public Date getViewDate()
    {
        return viewDate;
    }

    public void setViewDate(Date viewDate)
    {
        this.viewDate = viewDate;
    }
}
