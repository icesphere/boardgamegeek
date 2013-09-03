package org.smartreaction.boardgamegeek.model;

import java.util.List;

public class GeekListDetail
{
    private long geekListId;
    private String title;
    private String description;
    private String username;
    private List<GeekListEntry> entries;
    private List<GeekListComment> comments;
    private int thumbs;

    public long getGeekListId()
    {
        return geekListId;
    }

    public void setGeekListId(long geekListId)
    {
        this.geekListId = geekListId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public List<GeekListEntry> getEntries()
    {
        return entries;
    }

    public void setEntries(List<GeekListEntry> entries)
    {
        this.entries = entries;
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
}
