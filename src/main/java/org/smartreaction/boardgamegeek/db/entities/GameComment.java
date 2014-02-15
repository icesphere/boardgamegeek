package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "game_comments")
public class GameComment
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

    @Column
    private String comment;

    @Column(name = "comment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentDate;

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

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Date getCommentDate()
    {
        return commentDate;
    }

    public void setCommentDate(Date commentDate)
    {
        this.commentDate = commentDate;
    }
}
