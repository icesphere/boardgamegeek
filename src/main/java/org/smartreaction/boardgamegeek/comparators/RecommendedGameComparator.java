package org.smartreaction.boardgamegeek.comparators;

import org.smartreaction.boardgamegeek.model.RecommendedGameScore;

import java.util.Comparator;

public class RecommendedGameComparator implements Comparator<RecommendedGameScore>
{
    @Override
    public int compare(RecommendedGameScore rg1, RecommendedGameScore rg2)
    {
        return ((Double)rg2.getScore()).compareTo(rg1.getScore());
    }
}
