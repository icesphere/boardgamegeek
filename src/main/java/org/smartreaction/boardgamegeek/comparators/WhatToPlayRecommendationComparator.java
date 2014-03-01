package org.smartreaction.boardgamegeek.comparators;

import org.smartreaction.boardgamegeek.model.WhatToPlayRecommendation;

import java.util.Comparator;

public class WhatToPlayRecommendationComparator implements Comparator<WhatToPlayRecommendation>
{
    @Override
    public int compare(WhatToPlayRecommendation r1, WhatToPlayRecommendation r2)
    {
        return ((Double)r2.getScore()).compareTo(r1.getScore());
    }
}
