package org.smartreaction.boardgamegeek.business;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.GameComment;
import org.smartreaction.boardgamegeek.services.BoardGameGeekService;
import org.smartreaction.boardgamegeek.xml.forumlist.Forum;
import org.smartreaction.boardgamegeek.xml.hotgames.Item;
import org.smartreaction.boardgamegeek.xml.hotgames.Items;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
@Lock(LockType.READ)
public class BoardGameCache
{
    @EJB
    BoardGameUtil boardGameUtil;

    @EJB
    BoardGameGeekService boardGameGeekService;

    @EJB
    BoardGameAsynchronous boardGameAsynchronous;

    LoadingCache<Long, Game> games;

    List<Game> hotGames;
    private DateTime hotGamesLastUpdated;

    List<Game> topGames;
    private DateTime topGamesLastUpdated;

    LoadingCache<Long, List<GameComment>> gameComments;

    private boolean errorLoadingHotGames;
    private boolean errorLoadingTopGames;

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

        loadTopGames();

        gameComments = CacheBuilder.newBuilder()
            .maximumSize(20)
            .build(
                    new CacheLoader<Long, List<GameComment>>()
                    {
                        public List<GameComment> load(Long gameId)
                        {
                            return boardGameUtil.getGameComments(getGame(gameId));
                        }
                    }
            );
    }

    private void loadHotGames()
    {
        hotGames = new ArrayList<>();

        Items hotGameItems = boardGameGeekService.getHotGames();
        if (hotGameItems != null) {
            for (Item hotGameItem : hotGameItems.getItem()) {
                Game game = getGame(hotGameItem.getId().longValue());
                if (game != null) {
                    hotGames.add(game);
                }
            }

            errorLoadingHotGames = false;
        }
        else {
            errorLoadingHotGames = true;
        }

        hotGamesLastUpdated = DateTime.now();
    }

    public Game getGame(long gameId)
    {
        try {
            Game game = games.get(gameId);
            if (shouldRefreshGame(game)) {
                boardGameAsynchronous.refreshGame(game, this);
            }
            return game;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean shouldRefreshGame(Game game)
    {
        DateTime expiredTime = new DateTime(game.getLastUpdated());
        expiredTime = expiredTime.plusHours(48);
        expiredTime = expiredTime.plusHours(RandomUtils.nextInt(48));
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
        List<Long> topGameIds = boardGameGeekService.getTopGameIds();

        if (topGameIds != null) {
            topGames = new ArrayList<>(100);

            for (Long gameId : topGameIds) {
                Game game = getGame(gameId);
                if (game != null) {
                    topGames.add(game);
                }
            }
            topGamesLastUpdated = DateTime.now();

            errorLoadingTopGames = false;
        }
        else {
            errorLoadingTopGames = true;
            topGames = boardGameUtil.getTopGames();
        }
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

    public List<GameComment> getGameComments(Game game)
    {
        try {
            List<GameComment> comments = gameComments.getUnchecked(game.getId());
            if (shouldRefreshGameComments(game)) {
                game.setCommentsLastUpdated(new Date());
                boardGameAsynchronous.refreshGameComments(game, this);
            }
            return comments;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean shouldRefreshGameComments(Game game)
    {
        if (game.getCommentsLastUpdated() == null) {
            return true;
        }
        DateTime lastUpdated = new DateTime(game.getCommentsLastUpdated());
        lastUpdated = lastUpdated.plusDays(1);
        return DateTime.now().isAfter(lastUpdated);
    }

    public List<Forum> getForums(long gameId) {
        try {
            return boardGameGeekService.getForumList(gameId);
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isErrorLoadingHotGames() {
        return errorLoadingHotGames;
    }

    public void setErrorLoadingHotGames(boolean errorLoadingHotGames) {
        this.errorLoadingHotGames = errorLoadingHotGames;
    }

    public boolean isErrorLoadingTopGames() {
        return errorLoadingTopGames;
    }

    public void setErrorLoadingTopGames(boolean errorLoadingTopGames) {
        this.errorLoadingTopGames = errorLoadingTopGames;
    }
}
