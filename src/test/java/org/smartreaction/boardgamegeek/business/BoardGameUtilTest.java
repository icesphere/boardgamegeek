package org.smartreaction.boardgamegeek.business;

import org.junit.Before;
import org.junit.Test;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.db.dao.GameDao;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.UserGame;
import org.smartreaction.boardgamegeek.services.BoardGameGeekService;
import org.smartreaction.boardgamegeek.xml.game.*;
import org.smartreaction.boardgamegeek.xml.game.Average;
import org.smartreaction.boardgamegeek.xml.game.Bayesaverage;
import org.smartreaction.boardgamegeek.xml.game.Item;
import org.smartreaction.boardgamegeek.xml.game.Median;
import org.smartreaction.boardgamegeek.xml.game.Rank;
import org.smartreaction.boardgamegeek.xml.game.Ranks;
import org.smartreaction.boardgamegeek.xml.game.Stddev;
import org.smartreaction.boardgamegeek.xml.game.Usersrated;
import org.smartreaction.boardgamegeek.xml.collection.*;
import org.smartreaction.boardgamegeek.xml.collection.Items;
import org.smartreaction.boardgamegeek.xml.collection.Name;

import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class BoardGameUtilTest
{
    private BoardGameUtil boardGameUtil;
    private GameDao gameDao;
    private BoardGameGeekService boardGameGeekService;

    private static final long TEST_GAME_ID = 1;
    private static final long TEST_USER_ID = 2;
    private static final long TEST_EXPANSION_ID = 3;
    private static final String TEST_USERNAME = "testusername";

    @Before
    public void setup() throws MalformedURLException, JAXBException
    {
        boardGameUtil = spy(new BoardGameUtil());

        gameDao = mock(GameDao.class);
        when(gameDao.getExpansions(TEST_GAME_ID)).thenReturn(getTestExpansions());
        boardGameUtil.gameDao = gameDao;

        boardGameGeekService = mock(BoardGameGeekService.class);
        when(boardGameGeekService.getGame(TEST_GAME_ID)).thenReturn(getTestGameItem(TEST_GAME_ID, true));
        when(boardGameGeekService.getGame(TEST_EXPANSION_ID)).thenReturn(getTestGameItem(TEST_EXPANSION_ID, false));
        boardGameUtil.boardGameGeekService = boardGameGeekService;
    }

    @Test
    public void getGame_returns_existing_game_if_game_exists() throws MalformedURLException, JAXBException
    {
        Game testGame = getTestGame();
        when(gameDao.getGame(TEST_GAME_ID)).thenReturn(testGame);
        Game game = boardGameUtil.getGame(TEST_GAME_ID);
        assertNotNull(game);
        verify(boardGameUtil, never()).createGame(TEST_GAME_ID);
    }

    private Game getTestGame()
    {
        Game game = new Game();
        game.setId(TEST_GAME_ID);
        game.setExpansions(getTestExpansions());
        return game;
    }

    @Test
    public void getGame_creates_game_if_it_does_not_exist() throws MalformedURLException, JAXBException
    {
        doNothing().when(boardGameUtil).syncGame(any(Game.class), any(Item.class), anyBoolean());
        when(gameDao.getGame(TEST_GAME_ID)).thenReturn(null);
        Game game = boardGameUtil.getGame(TEST_GAME_ID);
        assertNotNull(game);
        verify(boardGameUtil).createGame(TEST_GAME_ID);
    }

    private Item getTestGameItem(long id, boolean addExpansions)
    {
        Item gameItem = new Item();

        org.smartreaction.boardgamegeek.xml.game.Name name = new org.smartreaction.boardgamegeek.xml.game.Name();
        name.setValue("test game name");
        name.setType(BoardGameGeekConstants.PRIMARY_TYPE);
        gameItem.getName().add(name);

        gameItem.setId(String.valueOf(id));
        gameItem.setDescription("test description");
        gameItem.setImage("testimage.png");
        gameItem.setThumbnail("testthumbnail.png");
        gameItem.setType("boardgame");

        Maxplayers maxplayers = new Maxplayers();
        maxplayers.setValue("4");
        gameItem.setMaxplayers(maxplayers);

        Minplayers minplayers = new Minplayers();
        minplayers.setValue("2");
        gameItem.setMinplayers(minplayers);

        gameItem.setStatistics(getTestStatistics());

        Yearpublished yearpublished = new Yearpublished();
        yearpublished.setValue("2010");
        gameItem.setYearpublished(yearpublished);

        if (addExpansions) {
            Link link = getTestLink();
            gameItem.getLink().add(link);
        }

        return gameItem;
    }

    private Link getTestLink()
    {
        Link link = new Link();
        link.setType(BoardGameGeekConstants.EXPANSION_TYPE);
        link.setId(String.valueOf(TEST_EXPANSION_ID));
        link.setValue("Test Expansion");
        return link;
    }

    private Statistics getTestStatistics()
    {
        Statistics statistics = new Statistics();
        statistics.setPage("1");
        statistics.setRatings(getTestRatings());
        return statistics;
    }

    private Ratings getTestRatings()
    {
        Ratings ratings = new Ratings();

        Average average = new Average();
        average.setValue("7.55838");
        ratings.setAverage(average);

        Owned owned = new Owned();
        owned.setValue("15005");
        ratings.setOwned(owned);

        Averageweight averageweight = new Averageweight();
        averageweight.setValue("2.25");
        ratings.setAverageweight(averageweight);

        Bayesaverage bayesaverage = new Bayesaverage();
        bayesaverage.setValue("6.98123");
        ratings.setBayesaverage(bayesaverage);

        Median median = new Median();
        median.setValue("0");
        ratings.setMedian(median);

        Numcomments numcomments = new Numcomments();
        numcomments.setValue("259");
        ratings.setNumcomments(numcomments);

        Numweights numweights = new Numweights();
        numweights.setValue("210");
        ratings.setNumweights(numweights);

        Ranks ranks = new Ranks();
        Rank rank = new Rank();
        rank.setId(String.valueOf(1));
        rank.setType("subtype");
        rank.setName("boardgame");
        rank.setFriendlyname("Board Game Rank");
        rank.setValue("289");
        rank.setBayesaverage("6.98123");
        ranks.getRank().add(rank);
        ratings.setRanks(ranks);

        Stddev stddev = new Stddev();
        stddev.setValue("1.35677");
        ratings.setStddev(stddev);

        Trading trading = new Trading();
        trading.setValue("347");
        ratings.setTrading(trading);

        Usersrated usersrated = new Usersrated();
        usersrated.setValue("345");
        ratings.setUsersrated(usersrated);

        Wanting wanting = new Wanting();
        wanting.setValue("234");
        ratings.setWanting(wanting);

        Wishing wishing = new Wishing();
        wishing.setValue("123");
        ratings.setWishing(wishing);

        return ratings;
    }

    @Test
    public void syncGame() throws MalformedURLException, JAXBException
    {
        Game testGame = getTestGame();
        boardGameUtil.syncGame(testGame);
        verify(gameDao).updateGame(testGame);
        assertEquals(1, testGame.getExpansions().size());
    }

    @Test
    public void initUserGames() throws MalformedURLException, JAXBException
    {
        when(boardGameGeekService.getCollection(TEST_USERNAME)).thenReturn(getTestCollection());
        boardGameUtil.initUserGames(TEST_USER_ID, TEST_USERNAME);
    }

    public Items getTestCollection()
    {
        Items items = new Items();

        org.smartreaction.boardgamegeek.xml.collection.Item testItem = getTestItem();
        items.getItem().add(testItem);

        return items;
    }

    private org.smartreaction.boardgamegeek.xml.collection.Item getTestItem()
    {
        org.smartreaction.boardgamegeek.xml.collection.Item item = new org.smartreaction.boardgamegeek.xml.collection.Item();

        item.setImage("test image");
        item.setThumbnail("test thumbnail");
        Name name = new Name();
        name.setContent("test name");
        item.setName(name);
        item.setCollid("1234");
        item.setObjectid(String.valueOf(TEST_GAME_ID));
        item.setNumplays("324");
        item.setObjecttype("thing");
        item.setSubtype("boardgame");
        item.setYearpublished("2010");

        item.setStats(getTestStats());
        item.setStatus(getTestStatus());

        return item;
    }

    private Stats getTestStats()
    {
        Stats stats = new Stats();

        stats.setMaxplayers("5");
        stats.setMinplayers("2");
        stats.setNumowned("2345");
        stats.setPlayingtime("60");

        Rating rating = getTestRating();
        stats.setRating(rating);

        return stats;
    }

    private Rating getTestRating()
    {
        Rating rating = new Rating();
        org.smartreaction.boardgamegeek.xml.collection.Average average = new org.smartreaction.boardgamegeek.xml.collection.Average();
        average.setValue("7.67873");
        rating.setAverage(average);
        return rating;
    }

    private Status getTestStatus()
    {
        Status status = new Status();
        status.setLastmodified("2012-07-09 21:32:53");
        status.setFortrade("0");
        status.setOwn("1");
        status.setPreordered("0");
        status.setPrevowned("0");
        status.setWant("0");
        status.setWanttobuy("0");
        status.setWanttoplay("0");
        status.setWishlist("0");
        return status;
    }

    @Test
    public void initUserGames_with_empty_collection() throws MalformedURLException, JAXBException
    {
        when(boardGameGeekService.getCollection(TEST_USERNAME)).thenReturn(new Items());
        boardGameUtil.initUserGames(TEST_USER_ID, TEST_USERNAME);
    }

    @Test
    public void syncUserGames() throws MalformedURLException, JAXBException
    {
        when(boardGameGeekService.getCollection(TEST_USERNAME)).thenReturn(getTestCollection());

        Map<Long, UserGame> userGamesMap = new HashMap<Long, UserGame>();
        userGamesMap.put(TEST_GAME_ID, getTestUserGame());

        boardGameUtil.syncUserGames(TEST_USER_ID, TEST_USERNAME, userGamesMap);
    }

    private UserGame getTestUserGame()
    {
        UserGame userGame = new UserGame();
        userGame.setGameId(TEST_GAME_ID);
        userGame.setUserId(TEST_USER_ID);
        userGame.setLastModified(new Date());
        userGame.setLastUpdated(new Date());
        userGame.setNumPlays(345);
        userGame.setOwned(true);
        userGame.setRating(7.64534f);
        return userGame;
    }

    @Test
    public void getGameName()
    {
        String gameName = boardGameUtil.getGameName(getTestGameItem(TEST_GAME_ID, false));
        assertEquals("test game name", gameName);
    }

    public List<Game> getTestExpansions()
    {
        List<Game> expansions = new ArrayList<Game>();

        Game expansion = new Game();
        expansion.setId(TEST_EXPANSION_ID);
        expansions.add(expansion);

        return expansions;
    }
}
