package org.smartreaction.boardgamegeek.view;

import org.omnifaces.util.Faces;
import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.GameComment;
import org.smartreaction.boardgamegeek.xml.forumlist.Forum;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean
@ViewScoped
public class BoardGame {
    @ManagedProperty(value = "#{userSession}")
    UserSession userSession;

    @EJB
    BoardGameCache boardGameCache;

    private Game game;

    private boolean gameLoaded;

    private List<GameComment> gameComments;

    private List<Forum> forums;

    private boolean showGameComments;

    private boolean showForums;

    private boolean showDescription;

    private boolean showExpansions;

    @PostConstruct
    public void setup() {
        long gameId = Long.parseLong(Faces.getRequestParameter("id"));
        loadGame(gameId);
        gameLoaded = true;
    }

    private void loadGame(long gameId) {
        if (game == null) {
            game = boardGameCache.getGame(gameId);
        }
        if (game == null) {
            throw new RuntimeException("Game not found");
        }
    }

    public void syncGame() {
        boardGameCache.syncGame(game);
        gameLoaded = true;
    }

    public void refreshGame() {
        gameLoaded = false;
    }

    public void loadGameComments() {
        if (gameComments == null) {
            gameComments = boardGameCache.getGameComments(game);
        }
        showGameComments = true;
    }

    public void hideGameComments() {
        showGameComments = false;
    }

    public void loadForums() {
        if (forums == null) {
            forums = boardGameCache.getForums(game.getId());
        }
        showForums = true;
    }

    public void hideForums() {
        showForums = false;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public boolean isGameLoaded() {
        return gameLoaded;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public List<GameComment> getGameComments() {
        return gameComments;
    }

    public boolean isShowGameComments() {
        return showGameComments;
    }

    public boolean isShowForums() {
        return showForums;
    }

    public void setShowForums(boolean showForums) {
        this.showForums = showForums;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public boolean isShowDescription() {
        return showDescription;
    }

    public void setShowDescription(boolean showDescription) {
        this.showDescription = showDescription;
    }

    public boolean isShowExpansions() {
        return showExpansions;
    }

    public void setShowExpansions(boolean showExpansions) {
        this.showExpansions = showExpansions;
    }
}
