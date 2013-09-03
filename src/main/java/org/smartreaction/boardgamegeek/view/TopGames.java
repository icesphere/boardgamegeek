package org.smartreaction.boardgamegeek.view;

import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.db.entities.Game;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean
@ViewScoped
public class TopGames
{
    @EJB
    BoardGameCache boardGameCache;

    List<Game> topGames;

    List<Game> filteredTopGames;

    private boolean loaded;

    public List<Game> getTopGames()
    {
        return topGames;
    }

    public void loadTopGames()
    {
        topGames = boardGameCache.getTopGames();
        loaded = true;
    }

    public void refreshHotGames()
    {
        loaded = false;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public List<Game> getFilteredTopGames()
    {
        return filteredTopGames;
    }

    public void setFilteredTopGames(List<Game> filteredTopGames)
    {
        this.filteredTopGames = filteredTopGames;
    }
}
