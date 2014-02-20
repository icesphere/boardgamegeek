package org.smartreaction.boardgamegeek.view;

import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.db.dao.GameDao;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.RecentlyViewedGame;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@ViewScoped
public class RecentlyViewedGames
{
    @ManagedProperty(value="#{userSession}")
    UserSession userSession;

    @EJB
    BoardGameCache boardGameCache;

    @EJB
    GameDao gameDao;

    List<Game> recentlyViewedGames;

    private boolean loaded;

    public void loadGames()
    {
        List<RecentlyViewedGame> recentlyViewedGamesForUser = gameDao.getRecentlyViewedGamesForUser(userSession.getUser().getId());

        recentlyViewedGames = new ArrayList<>(recentlyViewedGamesForUser.size());

        for (RecentlyViewedGame recentlyViewedGame : recentlyViewedGamesForUser) {
            recentlyViewedGames.add(boardGameCache.getGame(recentlyViewedGame.getGameId()));
        }

        loaded = true;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public List<Game> getRecentlyViewedGames()
    {
        return recentlyViewedGames;
    }

    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }
}
