package org.smartreaction.boardgamegeek.comparators;

import org.smartreaction.boardgamegeek.model.RecommendedGame;

import java.util.Comparator;

public class RecommendedGameComparator implements Comparator<RecommendedGame>
{
    @Override
    public int compare(RecommendedGame rg1, RecommendedGame rg2)
    {
        return ((Double)rg2.getScore()).compareTo(rg1.getScore());
    }
}
