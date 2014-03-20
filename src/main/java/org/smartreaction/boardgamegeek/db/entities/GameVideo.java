package org.smartreaction.boardgamegeek.db.entities;

import org.smartreaction.boardgamegeek.BoardGameGeekConstants;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "game_videos")
public class GameVideo
{
    @Id
    private long id;

    @Column(name = "game_id")
    private long gameId;

    private int thumbs;

    private String title;

    private String username;

    @Column(name = "post_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date postDate;

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

    public int getThumbs()
    {
        return thumbs;
    }

    public void setThumbs(int thumbs)
    {
        this.thumbs = thumbs;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Date getPostDate()
    {
        return postDate;
    }

    public void setPostDate(Date postDate)
    {
        this.postDate = postDate;
    }

    public String getLink()
    {
        return BoardGameGeekConstants.BBG_WEBSITE + "/video/" + id;
    }
}
