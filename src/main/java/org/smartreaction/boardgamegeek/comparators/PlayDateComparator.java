package org.smartreaction.boardgamegeek.comparators;

import org.smartreaction.boardgamegeek.xml.plays.Play;

import java.util.Comparator;

public class PlayDateComparator implements Comparator<Play>
{
    @Override
    public int compare(Play p1, Play p2)
    {
        return p1.getDate().compareTo(p2.getDate());
    }
}
