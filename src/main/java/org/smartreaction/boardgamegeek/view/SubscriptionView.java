package org.smartreaction.boardgamegeek.view;

import org.smartreaction.boardgamegeek.business.SubscriptionUtil;
import org.smartreaction.boardgamegeek.model.ForumSubscription;
import org.smartreaction.boardgamegeek.model.GeekListSubscription;
import org.smartreaction.boardgamegeek.model.Subscriptions;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class SubscriptionView
{
    @ManagedProperty(value="#{userSession}")
    UserSession userSession;

    @ManagedProperty(value="#{boardGameGeek}")
    BoardGameGeek boardGameGeek;

    @EJB
    SubscriptionUtil subscriptionUtil;

    private Subscriptions subscriptions;

    private boolean loaded;

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGameGeek(BoardGameGeek boardGameGeek)
    {
        this.boardGameGeek = boardGameGeek;
    }

    public void loadSubscriptions()
    {
        subscriptions = subscriptionUtil.getSubscriptions(userSession.getCookies());
        loaded = true;
    }

    public Subscriptions getSubscriptions()
    {
        return subscriptions;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public String showForumSubscription(ForumSubscription subscription)
    {
        return "thread.xhtml?id="+subscription.getForumId()+"&markSubscriptionRead=true&lastUpdated="+subscription.getLastUpdated().getTime()+"&faces-redirect=true";
    }

    public String showGeekListSubscription(GeekListSubscription subscription)
    {
        return "geeklist.xhtml?id="+subscription.getGeekListId()+"&markSubscriptionRead=true&lastUpdated="+subscription.getLastUpdated().getTime()+"&faces-redirect=true";
    }

    public void markAllForumSubscriptionsAsRead()
    {
        for (ForumSubscription forumSubscription : subscriptions.getForumSubscriptions()) {
            boardGameGeek.markSubscriptionAsRead(forumSubscription.getForumId(), true);
        }
        loadSubscriptions();
    }

    public void markAllGeekListSubscriptionsAsRead()
    {
        for (GeekListSubscription geekListSubscription : subscriptions.getGeekListSubscriptions()) {
            boardGameGeek.markSubscriptionAsRead(geekListSubscription.getGeekListId(), false);
        }
        loadSubscriptions();
    }
}
