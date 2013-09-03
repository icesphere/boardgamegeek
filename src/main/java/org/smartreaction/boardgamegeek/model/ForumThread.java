package org.smartreaction.boardgamegeek.model;

import java.util.List;

public class ForumThread
{
    private long threadId;
    private String subject;
    private List<ThreadArticle> articles;

    public long getThreadId()
    {
        return threadId;
    }

    public void setThreadId(long threadId)
    {
        this.threadId = threadId;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public List<ThreadArticle> getArticles()
    {
        return articles;
    }

    public void setArticles(List<ThreadArticle> articles)
    {
        this.articles = articles;
    }
}
