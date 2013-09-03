package org.smartreaction.boardgamegeek.model;

import java.util.Date;

public class ForumSubscription
{
    private String forum;
    private String subject;
    private long forumId;
    private Date lastUpdated;

    public String getForum()
    {
        return forum;
    }

    public void setForum(String forum)
    {
        this.forum = forum;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public long getForumId()
    {
        return forumId;
    }

    public void setForumId(long forumId)
    {
        this.forumId = forumId;
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
