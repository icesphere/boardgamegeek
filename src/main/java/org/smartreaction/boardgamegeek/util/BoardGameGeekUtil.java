package org.smartreaction.boardgamegeek.util;

import javax.ejb.Stateless;

@Stateless
public class BoardGameGeekUtil
{
    public String parsePlayResponse(String playResponse)
    {
        try {
            int start = playResponse.lastIndexOf("\">") + 2;
            int end = playResponse.lastIndexOf("<");

            return playResponse.substring(start, end);
        }
        catch (Exception e) {
            return "";
        }
    }
}
