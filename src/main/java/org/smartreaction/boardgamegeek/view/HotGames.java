package org.smartreaction.boardgamegeek.view;

import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.db.entities.Game;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean
@ViewScoped
public class HotGames
{
    @EJB
    BoardGameCache boardGameCache;

    List<Game> hotGames;

    private boolean loaded;

    public List<Game> getHotGames()
    {
        return hotGames;
    }

    public void loadHotGames()
    {
        hotGames = boardGameCache.getHotGames();
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
}
