package org.smartreaction.boardgamegeek.model;

import java.util.Date;

public class GeekListComment
{
    private String comment;
    private String username;
    private int thumbs;
    private long id;
    private Date postDate;

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public int getThumbs()
    {
        return thumbs;
    }

    public void setThumbs(int thumbs)
    {
        this.thumbs = thumbs;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public Date getPostDate()
    {
        return postDate;
    }

    public void setPostDate(Date postDate)
    {
        this.postDate = postDate;
    }
}
