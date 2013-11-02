package org.smartreaction.boardgamegeek.view;

import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.business.GeekListsCache;
import org.smartreaction.boardgamegeek.model.GeekListDetail;
import org.smartreaction.boardgamegeek.model.GeekListEntry;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Date;

@ManagedBean
@ViewScoped
public class GeekListView
{
    @ManagedProperty(value="#{boardGameGeek}")
    BoardGameGeek boardGameGeek;

    @EJB
    GeekListsCache geekListsCache;

    private GeekListDetail geekListDetail;

    private boolean loaded;

    private boolean errorLoadingGeekListDetail;

    private long geekListId;

    private Date lastUpdated;

    private long firstSubscriptionEntryId;

    private boolean showRecommend = true;

    private boolean showUndoRecommend = false;

    @PostConstruct
    public void load()
    {
        geekListId = Long.parseLong(Faces.getRequestParameter("id"));
        String lastUpdatedParam = Faces.getRequestParameter("lastUpdated");
        if (lastUpdatedParam != null) {
            lastUpdated = new Date(Long.parseLong(lastUpdatedParam));
        }
        String markSubscriptionReadParam = Faces.getRequestParameter("markSubscriptionRead");
        if ("true".equals(markSubscriptionReadParam)) {
            boardGameGeek.markSubscriptionAsReadAsynchronous(geekListId, false);
        }
    }

    public void loadGeekList()
    {
        try {
            geekListDetail = geekListsCache.getGeekListDetail(geekListId);
            if (lastUpdated != null) {
                for (GeekListEntry entry : geekListDetail.getEntries()) {
                    if (entry.getPostDate().after(lastUpdated)) {
                        firstSubscriptionEntryId = entry.getEntryId();
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
            errorLoadingGeekListDetail = true;
        }
    }

    public void recommendGeeklist(boolean recommend)
    {
        boardGameGeek.recommendGeeklist(geekListId, recommend);
        if (recommend) {
            geekListDetail.setThumbs(getGeekListDetail().getThumbs()+1);
            showRecommend = false;
            showUndoRecommend = true;
        }
        else {
            geekListDetail.setThumbs(getGeekListDetail().getThumbs()-1);
            showRecommend = true;
            showUndoRecommend = false;
        }
    }

    public GeekListDetail getGeekListDetail()
    {
        return geekListDetail;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public boolean isErrorLoadingGeekListDetail()
    {
        return errorLoadingGeekListDetail;
    }

    public boolean highlight(Date postDate)
    {
        return lastUpdated != null && postDate != null && lastUpdated.before(postDate);
    }

    public String getBoardGameGeekLink()
    {
        return BoardGameGeekConstants.BBG_WEBSITE + "/geeklist/" + geekListId;
    }

    public boolean isShowRecommend()
    {
        return showRecommend;
    }

    public boolean isShowUndoRecommend()
    {
        return showUndoRecommend;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGameGeek(BoardGameGeek boardGameGeek)
    {
        this.boardGameGeek = boardGameGeek;
    }
}
