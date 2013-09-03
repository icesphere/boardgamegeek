package org.smartreaction.boardgamegeek.db.dao;

import org.junit.Before;
import org.junit.Test;
import org.smartreaction.boardgamegeek.db.entities.Expansion;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.UserGame;
import org.smartreaction.test.BaseDatabaseTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class GameDaoIT extends BaseDatabaseTest
{
    private GameDao gameDao;

    private static final long TEST_GAME_1_ID = 1;
    private static final long TEST_GAME_2_ID = 4;

    private static final long TEST_USER_1_ID = 1;
    private static final long TEST_USER_2_ID = 2;

    @Before
    public void setup() {
        super.setup();
        gameDao = new GameDao();
        gameDao.em = entityManager;
    }

    @Test
    public void getGame() {
        Game game = gameDao.getGame(TEST_GAME_1_ID);
        assertNotNull(game);
    }

    @Test
    public void hasGame()
    {
        assertTrue(gameDao.hasGame(TEST_GAME_2_ID));
        assertFalse(gameDao.hasGame(-1));
    }

    @Test
    public void createGame()
    {
        long newGameId = 99;
        Game game = new Game();
        game.setId(newGameId);
        game.setName("test game");
        game.setDescription("test game description");
        gameDao.createGame(game);
        assertNotNull(gameDao.getGame(newGameId));
    }

    @Test
    public void updateGame()
    {
        Game game = new Game();
        game.setId(TEST_GAME_1_ID);
        game.setDescription("updated description");
        gameDao.updateGame(game);
        Game gameFromDB = gameDao.getGame(TEST_GAME_1_ID);
        assertEquals("updated description", gameFromDB.getDescription());
    }

    @Test
    public void getGames()
    {
        List<Game> games = gameDao.getGames(TEST_USER_2_ID);
        assertEquals(2, games.size());
        List<Game> expansions = games.get(0).getExpansions();
        assertEquals(2, expansions.size());
        assertEquals(0, games.get(1).getExpansions().size());
    }

    @Test
    public void createUserGame()
    {
        UserGame userGame = new UserGame();
        userGame.setUserId(TEST_USER_1_ID);
        userGame.setGameId(TEST_GAME_1_ID);
        gameDao.createUserGame(userGame);
    }

    @Test
    public void getUserGamesMap()
    {
        Map<Long, UserGame> userGamesMap = gameDao.getUserGamesMap(TEST_USER_2_ID);
        assertEquals(2, userGamesMap.size());
    }

    @Test
    public void updateUserGame()
    {
        UserGame userGame = new UserGame();
        userGame.setGameId(TEST_GAME_1_ID);
        userGame.setUserId(TEST_USER_2_ID);
        userGame.setNumPlays(99);
        gameDao.updateUserGame(userGame);
        Map<Long, UserGame> userGamesMap = gameDao.getUserGamesMap(TEST_USER_2_ID);
        assertEquals(99, userGamesMap.get(TEST_GAME_1_ID).getNumPlays());
    }

    @Test
    public void createExpansion() {
        Expansion expansion = new Expansion();
        expansion.setGameId(TEST_GAME_1_ID);
        expansion.setExpansionId(99);
        gameDao.createExpansion(expansion);
    }
}
