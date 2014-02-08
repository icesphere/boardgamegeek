package org.smartreaction.boardgamegeek.model;

import org.smartreaction.boardgamegeek.db.entities.Game;

import java.util.Date;
import java.util.List;

public class GeekListEntry
{
    private Game game;
    private String description;
    private List<GeekListComment> comments;
    private int thumbs;
    private String username;
    private long entryId;
    private Date postDate;
    private Date editDate;

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public List<GeekListComment> getComments()
    {
        return comments;
    }

    public void setComments(List<GeekListComment> comments)
    {
        this.comments = comments;
    }

    public int getThumbs()
    {
        return thumbs;
    }

    public void setThumbs(int thumbs)
    {
        this.thumbs = thumbs;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public long getEntryId()
    {
        return entryId;
    }

    public void setEntryId(long entryId)
    {
        this.entryId = entryId;
    }

    public Date getPostDate()
    {
        return postDate;
    }

    public void setPostDate(Date postDate)
    {
        this.postDate = postDate;
    }

    public Date getEditDate()
    {
        return editDate;
    }

    public void setEditDate(Date editDate)
    {
        this.editDate = editDate;
    }
}
