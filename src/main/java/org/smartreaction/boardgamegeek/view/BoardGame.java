package org.smartreaction.boardgamegeek.view;

import org.omnifaces.util.Faces;
import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.db.dao.GameDao;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.GameComment;
import org.smartreaction.boardgamegeek.db.entities.RecentlyViewedGame;
import org.smartreaction.boardgamegeek.xml.forumlist.Forum;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class BoardGame {
    @ManagedProperty(value = "#{userSession}")
    UserSession userSession;

    @ManagedProperty(value = "#{singleGamePlaysGraph}")
    SingleGamePlaysGraph singleGamePlaysGraph;

    @ManagedProperty(value="#{boardGameGeek}")
    BoardGameGeek boardGameGeek;

    @EJB
    BoardGameCache boardGameCache;

    @EJB
    GameDao gameDao;

    private Game game;

    private boolean gameLoaded;

    private List<GameComment> gameComments;

    private List<Forum> forums;

    private boolean showGameComments;

    private boolean showForums;

    private boolean showDescription;

    private boolean showExpansions;

    private boolean showGamePlays;

    private boolean gamePlaysLoaded;

    private List<Game> recommendedGames;

    private boolean showRecommendedGames;

    @PostConstruct
    public void setup() {
        long gameId = Long.parseLong(Faces.getRequestParameter("id"));
        loadGame(gameId);
        if (userSession.isUsernameSet() && userSession.getGames() == null) {
            userSession.syncGames();
        }

        if (userSession.isLoggedIn()) {
            RecentlyViewedGame recentlyViewedGame = gameDao.getRecentlyViewedGame(userSession.getUser().getId(), gameId);
            if (recentlyViewedGame == null) {
                recentlyViewedGame = new RecentlyViewedGame();
                recentlyViewedGame.setGameId(gameId);
                recentlyViewedGame.setUserId(userSession.getUser().getId());
                recentlyViewedGame.setViewDate(new Date());
                gameDao.createRecentlyViewedGame(recentlyViewedGame);
            }
            else {
                recentlyViewedGame.setViewDate(new Date());
                gameDao.updateRecentlyViewedGame(recentlyViewedGame);
            }
        }

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

    public void toggleForumSubscription(Forum forum)
    {
        boardGameGeek.subscribeToForum(forum.getId().longValue(), !forum.isSubscribed());
        forum.setSubscribed(!forum.isSubscribed());
    }

    public void loadGamePlays() throws ParseException, JAXBException, MalformedURLException {
        if (!gamePlaysLoaded) {
            singleGamePlaysGraph.loadChart(game);
            gamePlaysLoaded = true;
        }
        showGamePlays = true;
    }

    public void hideGamePlays() {
        showGamePlays = false;
    }

    public void addGameToCollection() throws MalformedURLException, JAXBException
    {
        if (boardGameGeek.addGameToCollection(game)) {
            userSession.forceIncrementalSync();
        }
    }

    public void loadRecommendedGames() throws MalformedURLException, JAXBException
    {
        if (recommendedGames == null) {
            recommendedGames = boardGameCache.getBoardGameUtil().getRecommendedGames(game);
        }
        showRecommendedGames = true;
    }

    public void hideRecommendedGames() {
        showRecommendedGames = false;
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

    public boolean isShowGamePlays() {
        return showGamePlays;
    }

    public void setShowGamePlays(boolean showGamePlays) {
        this.showGamePlays = showGamePlays;
    }

    public List<Game> getRecommendedGames()
    {
        return recommendedGames;
    }

    public boolean isShowRecommendedGames()
    {
        return showRecommendedGames;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setSingleGamePlaysGraph(SingleGamePlaysGraph singleGamePlaysGraph) {
        this.singleGamePlaysGraph = singleGamePlaysGraph;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGameGeek(BoardGameGeek boardGameGeek)
    {
        this.boardGameGeek = boardGameGeek;
    }
}
