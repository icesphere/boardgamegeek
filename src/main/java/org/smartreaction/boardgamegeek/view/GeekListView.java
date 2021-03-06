package org.smartreaction.boardgamegeek.view;

import org.omnifaces.util.Faces;
import org.primefaces.context.RequestContext;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.business.GeekListUtil;
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
    @ManagedProperty(value="#{userSession}")
    UserSession userSession;

    @ManagedProperty(value="#{boardGameGeek}")
    BoardGameGeek boardGameGeek;

    @EJB
    GeekListsCache geekListsCache;

    @EJB
    GeekListUtil geekListUtil;

    private GeekListDetail geekListDetail;

    private boolean loaded;

    private boolean errorLoadingGeekListDetail;

    private long geekListId;

    private Date lastUpdated;

    private long firstSubscriptionEntryId;

    private boolean showRecommend = true;

    private boolean showUndoRecommend = false;

    private boolean showSubscribe = true;

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
            boardGameGeek.markSubscriptionAsRead(geekListId, false);
        }
    }

    public void loadGeekList()
    {
        try {
            geekListDetail = geekListsCache.getGeekListDetail(geekListId);
            addFirstSubscriptionId();
            loaded = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            errorLoadingGeekListDetail = true;
        }
    }

    private void addFirstSubscriptionId()
    {
        if (lastUpdated != null) {
            for (GeekListEntry entry : geekListDetail.getEntries()) {
                if (entry.getPostDate() != null && entry.getPostDate().after(lastUpdated)) {
                    firstSubscriptionEntryId = entry.getEntryId();
                    break;
                }
            }
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("firstSubscriptionEntryId", firstSubscriptionEntryId);
    }

    public void loadGeekListNew()
    {
        try {
            geekListDetail = geekListUtil.getGeekListDetailNew(geekListId);
            addFirstSubscriptionId();
            loaded = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            errorLoadingGeekListDetail = true;
        }
    }

    public void recommendGeekList()
    {
        recommendGeekList(true);
    }

    public void unRecommendGeekList()
    {
        recommendGeekList(false);
    }

    public void recommendGeekList(boolean recommend)
    {
        boardGameGeek.recommendGeekList(geekListId, recommend);
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

    public void subscribeToGeekList()
    {
        subscribeToGeekList(true);
    }

    public void unSubscribeFromGeekList()
    {
        subscribeToGeekList(false);
    }

    public void subscribeToGeekList(boolean subscribe)
    {
        boardGameGeek.subscribeToGeekList(geekListId, subscribe);
        showSubscribe = !subscribe;
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
        return BoardGameGeekConstants.BGG_WEBSITE + "/geeklist/" + geekListId;
    }

    public long getGeekListId()
    {
        return geekListId;
    }

    public boolean isShowRecommend()
    {
        return showRecommend;
    }

    public boolean isShowUndoRecommend()
    {
        return showUndoRecommend;
    }

    public boolean isShowSubscribe()
    {
        return showSubscribe;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGameGeek(BoardGameGeek boardGameGeek)
    {
        this.boardGameGeek = boardGameGeek;
    }
}
