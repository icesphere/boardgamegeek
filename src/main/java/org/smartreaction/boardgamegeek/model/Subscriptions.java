package org.smartreaction.boardgamegeek.model;

import java.util.ArrayList;
import java.util.List;

public class Subscriptions
{
    List<GeekListSubscription> geekListSubscriptions = new ArrayList<GeekListSubscription>();
    List<ForumSubscription> forumSubscriptions = new ArrayList<ForumSubscription>();

    public List<GeekListSubscription> getGeekListSubscriptions()
    {
        return geekListSubscriptions;
    }

    public void setGeekListSubscriptions(List<GeekListSubscription> geekListSubscriptions)
    {
        this.geekListSubscriptions = geekListSubscriptions;
    }

    public List<ForumSubscription> getForumSubscriptions()
    {
        return forumSubscriptions;
    }

    public void setForumSubscriptions(List<ForumSubscription> forumSubscriptions)
    {
        this.forumSubscriptions = forumSubscriptions;
    }
}
