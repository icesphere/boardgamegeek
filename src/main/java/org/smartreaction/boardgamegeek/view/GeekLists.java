package org.smartreaction.boardgamegeek.view;

import org.smartreaction.boardgamegeek.business.GeekListsCache;
import org.smartreaction.boardgamegeek.model.GeekList;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class GeekLists
{
    @EJB
    GeekListsCache geekListsCache;

    private List<GeekList> hotGeekLists;
    private List<GeekList> recentGeekLists;

    private boolean loaded;

    private String sort = "hot";

    public void load()
    {
        hotGeekLists = new ArrayList<>(geekListsCache.getHotGeekLists());
        recentGeekLists = new ArrayList<>(geekListsCache.getRecentGeekLists());
        loaded = true;
    }

    public void refreshGeekLists()
    {
        geekListsCache.refreshGeekLists();
        load();
    }

    public List<GeekList> getGeekLists()
    {
        if (sort.equals("recent")) {
            return recentGeekLists;
        }
        else {
            return hotGeekLists;
        }
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public String getSort()
    {
        return sort;
    }

    public void setSort(String sort)
    {
        this.sort = sort;
    }
}
