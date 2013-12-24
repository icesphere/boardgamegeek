package org.smartreaction.boardgamegeek.view;

import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.services.BoardGameGeekService;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class SearchGames
{
    @EJB
    BoardGameGeekService boardGameGeekService;

    @EJB
    BoardGameCache boardGameCache;

    private List<Game> games;

    private String searchString;

    private boolean searched;

    public String getSearchString()
    {
        return searchString;
    }

    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }

    public void searchGames()
    {
        List<Long> gameIds = boardGameGeekService.searchGames(searchString);
        if (gameIds != null) {
            games = new ArrayList<>(gameIds.size());
            for (Long gameId : gameIds) {
                Game game = boardGameCache.getGame(gameId);
                if (game != null) {
                    games.add(game);
                }
            }
        }
        else {
            games = boardGameCache.getBoardGameUtil().searchGames(searchString);
        }
        searched = true;
    }

    public List<Game> getGames()
    {
        return games;
    }

    public boolean isSearched()
    {
        return searched;
    }
}
