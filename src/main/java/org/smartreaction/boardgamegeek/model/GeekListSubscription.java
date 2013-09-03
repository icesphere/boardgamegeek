package org.smartreaction.boardgamegeek.model;

import java.util.Date;

public class GeekListSubscription
{
    private String title;
    private long geekListId;
    private Date lastUpdated;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public long getGeekListId()
    {
        return geekListId;
    }

    public void setGeekListId(long geekListId)
    {
        this.geekListId = geekListId;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }
}
