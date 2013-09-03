package org.smartreaction.boardgamegeek.business;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.services.BoardGameGeekService;
import org.smartreaction.boardgamegeek.xml.hotgames.Item;
import org.smartreaction.boardgamegeek.xml.hotgames.Items;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Lock(LockType.READ)
public class BoardGameCache
{
    @EJB
    BoardGameUtil boardGameUtil;

    @EJB
    BoardGameGeekService boardGameGeekService;

    LoadingCache<Long, Game> games;

    List<Game> hotGames;
    private DateTime hotGamesLastUpdated;

    List<Game> topGames;
    private DateTime topGamesLastUpdated;

    @PostConstruct
    public void  setup()
    {
        games = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(
                    new CacheLoader<Long, Game>()
                    {
                        public Game load(Long gameId)
                        {
                            return boardGameUtil.getGame(gameId);
                        }
                    }
            );

        loadHotGames();
    }

    private void loadHotGames()
    {
        hotGames = new ArrayList<Game>();
        Items hotGameItems = boardGameGeekService.getHotGames();
        for (Item hotGameItem : hotGameItems.getItem()) {
            Game game = getGame(hotGameItem.getId().longValue());
            if (game != null) {
                hotGames.add(game);
            }
        }
        hotGamesLastUpdated = DateTime.now();
    }

    public Game getGame(long gameId)
    {
        try {
            Game game = games.getUnchecked(gameId);
            if (shouldRefreshGame(game)) {
                refreshGame(game);
            }
            return game;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Asynchronous
    public void refreshGame(Game game)
    {
        try {
            boardGameUtil.syncGame(game);
            games.put(game.getId(), game);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean shouldRefreshGame(Game game)
    {
        DateTime expiredTime = new DateTime(game.getLastUpdated());
        expiredTime = expiredTime.plusHours(48);
        expiredTime.plusHours(RandomUtils.nextInt(48));
        return DateTime.now().isAfter(expiredTime);
    }

    public void syncGame(Game game)
    {
        boardGameUtil.syncGame(game);
        games.put(game.getId(), game);
    }

    public List<Game> getHotGames()
    {
        if (shouldRefreshHotGames()) {
            loadHotGames();
        }
        return hotGames;
    }

    public boolean shouldRefreshHotGames()
    {
        DateTime refreshPeriod = DateTime.now().minusHours(1);
        return hotGamesLastUpdated == null || hotGamesLastUpdated.isBefore(refreshPeriod);
    }

    public List<Game> getTopGames()
    {
        if (shouldRefreshTopGames()) {
            loadTopGames();
        }
        return topGames;
    }

    private void loadTopGames()
    {
        topGames = new ArrayList<Game>(100);
        for (Long gameId : boardGameGeekService.getTopGameIds()) {
            Game game = getGame(gameId);
            if (game != null) {
                topGames.add(game);
            }
        }
        topGamesLastUpdated = DateTime.now();
    }

    public boolean shouldRefreshTopGames()
    {
        DateTime refreshPeriod = DateTime.now().minusHours(12);
        return topGamesLastUpdated == null || topGamesLastUpdated.isBefore(refreshPeriod);
    }

    public BoardGameUtil getBoardGameUtil()
    {
        return boardGameUtil;
    }
}
