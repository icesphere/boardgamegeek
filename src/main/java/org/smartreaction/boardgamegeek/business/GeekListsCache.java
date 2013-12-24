package org.smartreaction.boardgamegeek.business;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.joda.time.DateTime;
import org.smartreaction.boardgamegeek.model.GeekList;
import org.smartreaction.boardgamegeek.model.GeekListDetail;
import org.smartreaction.boardgamegeek.util.DateUtil;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Singleton
@Lock(LockType.READ)
public class GeekListsCache
{
    @EJB
    GeekListUtil geekListUtil;

    private List<GeekList> hotGeekLists = new ArrayList<>();
    private List<GeekList> recentGeekLists = new ArrayList<>();

    private DateTime lastUpdated;

    private LoadingCache<Long, GeekListDetail> geekListDetails;

    @PostConstruct
    public void load()
    {
        loadGeekLists();

        geekListDetails = CacheBuilder.newBuilder()
                .maximumSize(300)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .build(
                        new CacheLoader<Long, GeekListDetail>()
                        {
                            public GeekListDetail load(Long geekListId) throws JAXBException, IOException
                            {
                                return geekListUtil.getGeekListDetail(geekListId);
                            }
                        }
                );
    }

    @Lock(LockType.WRITE)
    private void loadGeekLists()
    {
        try {
            hotGeekLists = geekListUtil.getGeekLists(1, "hot");
            hotGeekLists.addAll(geekListUtil.getGeekLists(2, "hot"));
            hotGeekLists.addAll(geekListUtil.getGeekLists(3, "hot"));

            recentGeekLists = geekListUtil.getGeekLists(1, "recent");
            recentGeekLists.addAll(geekListUtil.getGeekLists(2, "recent"));
            recentGeekLists.addAll(geekListUtil.getGeekLists(3, "recent"));

            lastUpdated = DateTime.now();
        }
        catch (Exception e) {
            e.printStackTrace();
            //todo display error loading geek lists
        }
    }

    public void refreshGeekLists()
    {
        loadGeekLists();
    }

    public List<GeekList> getHotGeekLists()
    {
        if (isShouldRefreshGeeklists()) {
            refreshGeekLists();
        }
        return hotGeekLists;
    }

    public List<GeekList> getRecentGeekLists()
    {
        if (isShouldRefreshGeeklists()) {
            refreshGeekLists();
        }
        return recentGeekLists;
    }


    public String getLastUpdatedPeriod()
    {
        return DateUtil.getTimePeriodSinceNow(lastUpdated);
    }

    public boolean isShouldRefreshGeeklists()
    {
        DateTime refreshPeriod = DateTime.now().minusMinutes(10);
        return lastUpdated == null || lastUpdated.isBefore(refreshPeriod);
    }

    public GeekListDetail getGeekListDetail(long geekListId) throws ExecutionException
    {
        return geekListDetails.get(geekListId);
    }
}
