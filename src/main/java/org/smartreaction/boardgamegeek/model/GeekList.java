package org.smartreaction.boardgamegeek.model;

public class GeekList
{
    private long id;
    private int thumbs;
    private String title;
    private int entries;
    private String creator;
    private String link;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
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

    public int getEntries()
    {
        return entries;
    }

    public void setEntries(int entries)
    {
        this.entries = entries;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getLink()
    {
        return link;
    }

    public void setLink(String link)
    {
        this.link = link;
    }
}
