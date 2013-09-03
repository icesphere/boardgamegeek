package org.smartreaction.boardgamegeek.comparators;

import org.smartreaction.boardgamegeek.db.entities.UserGame;

import java.util.Comparator;

public class GameRatingComparator implements Comparator<UserGame>
{
    @Override
    public int compare(UserGame ug1, UserGame ug2)
    {
        return ((Float) ug1.getRating()).compareTo(ug2.getRating());
    }
}
