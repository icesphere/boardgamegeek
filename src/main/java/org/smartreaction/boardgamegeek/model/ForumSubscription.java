package org.smartreaction.boardgamegeek.model;

import java.util.Date;

public class ForumSubscription
{
    private String game;
    private String subject;
    private long forumId;
    private Date lastUpdated;
    private long gameId;
    private String forumName;
    private long threadId;

    public String getGame()
    {
        return game;
    }

    public void setGame(String game)
    {
        this.game = game;
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

    public long getGameId()
    {
        return gameId;
    }

    public void setGameId(long gameId)
    {
        this.gameId = gameId;
    }

    public String getForumName()
    {
        return forumName;
    }

    public void setForumName(String forumName)
    {
        this.forumName = forumName;
    }

    public long getThreadId()
    {
        return threadId;
    }

    public void setThreadId(long threadId)
    {
        this.threadId = threadId;
    }
}
