package org.smartreaction.boardgamegeek.view;

import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.smartreaction.boardgamegeek.model.ForumThread;
import org.smartreaction.boardgamegeek.model.ThreadArticle;
import org.smartreaction.boardgamegeek.services.BoardGameGeekService;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Date;

@ManagedBean
@ViewScoped
public class ThreadView
{
    @ManagedProperty(value="#{boardGameGeek}")
    BoardGameGeek boardGameGeek;

    @EJB
    BoardGameGeekService boardGameGeekService;

    private boolean loaded;

    private boolean errorLoadingThread;

    private long threadId;

    private ForumThread thread;

    private Date lastUpdated;

    private long firstSubscriptionEntryId;

    @PostConstruct
    public void load()
    {
        threadId = Long.parseLong(Faces.getRequestParameter("id"));
        String lastUpdatedParam = Faces.getRequestParameter("lastUpdated");
        if (lastUpdatedParam != null) {
            lastUpdated = new Date(Long.parseLong(lastUpdatedParam));
        }
        String markSubscriptionReadParam = Faces.getRequestParameter("markSubscriptionRead");
        if ("true".equals(markSubscriptionReadParam)) {
            boardGameGeek.markSubscriptionAsRead(threadId, true);
        }
    }

    public void loadThread()
    {
        try {
            thread = boardGameGeekService.getThread(threadId);
            if (lastUpdated != null) {
                for (ThreadArticle article : thread.getArticles()) {
                    if (article.getPostDate().after(lastUpdated)) {
                        firstSubscriptionEntryId = article.getId();
                        break;
                    }
                }
            }
            RequestContext context = RequestContext.getCurrentInstance();
            context.addCallbackParam("firstSubscriptionEntryId", firstSubscriptionEntryId);
            loaded = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            errorLoadingThread = true;
        }
    }

    public ForumThread getThread()
    {
        return thread;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public boolean isErrorLoadingThread()
    {
        return errorLoadingThread;
    }

    public boolean highlight(Date postDate)
    {
        return lastUpdated != null && postDate != null && lastUpdated.before(postDate);
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGameGeek(BoardGameGeek boardGameGeek)
    {
        this.boardGameGeek = boardGameGeek;
    }
}
