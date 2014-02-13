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

    private boolean errorLoadingSubscriptions;

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
        try {
            subscriptions = subscriptionUtil.getSubscriptions(userSession.getCookies());
            errorLoadingSubscriptions = false;
        } catch (Exception e) {
            errorLoadingSubscriptions = true;
        }
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
        String threadLink = "thread.xhtml?id=" + subscription.getThreadId() + "&markSubscriptionRead=true&faces-redirect=true";

        if (subscription.getLastUpdated() != null) {
            threadLink += "&lastUpdated=" + subscription.getLastUpdated().getTime();
        }

        return threadLink;
    }

    public String showGeekListSubscription(GeekListSubscription subscription)
    {
        String geekListLink = "geeklist.xhtml?id=" + subscription.getGeekListId() + "&markSubscriptionRead=true&faces-redirect=true";

        if (subscription.getLastUpdated() != null) {
            geekListLink += "&lastUpdated=" + subscription.getLastUpdated().getTime();
        }

        return geekListLink;
    }

    public void markAllForumSubscriptionsAsRead()
    {
        for (ForumSubscription forumSubscription : subscriptions.getForumSubscriptions()) {
            boardGameGeek.markSubscriptionAsRead(forumSubscription.getThreadId(), true);
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

    public boolean isErrorLoadingSubscriptions() {
        return errorLoadingSubscriptions;
    }
}
