package org.smartreaction.boardgamegeek.comparators;

import junit.framework.TestCase;
import org.smartreaction.boardgamegeek.db.entities.UserGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameRatingComparatorTest extends TestCase
{
    public void test()
    {
        List<UserGame> userGames = new ArrayList<UserGame>(4);
        userGames.add(getTestUserGame(5.0f));
        userGames.add(getTestUserGame(4.0f));
        userGames.add(getTestUserGame(7.0f));
        userGames.add(getTestUserGame(4.5f));

        Collections.sort(userGames, new GameRatingComparator());

        assertEquals(4.0f, userGames.get(0).getRating());
        assertEquals(4.5f, userGames.get(1).getRating());
        assertEquals(5.0f, userGames.get(2).getRating());
        assertEquals(7.0f, userGames.get(3).getRating());
    }

    private UserGame getTestUserGame(float rating)
    {
        UserGame game = new UserGame();
        game.setRating(rating);
        return game;
    }
}
