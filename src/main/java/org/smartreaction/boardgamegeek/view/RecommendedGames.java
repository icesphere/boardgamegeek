package org.smartreaction.boardgamegeek.view;

import org.apache.commons.lang.math.RandomUtils;
import org.joda.time.DateTime;
import org.primefaces.context.RequestContext;
import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.comparators.RecommendedGameComparator;
import org.smartreaction.boardgamegeek.db.entities.*;
import org.smartreaction.boardgamegeek.model.GameRecommendations;
import org.smartreaction.boardgamegeek.model.RecommendedGame;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.*;

@ManagedBean
@ViewScoped
public class RecommendedGames implements Serializable
{
    @ManagedProperty(value = "#{userSession}")
    UserSession userSession;

    @EJB
    BoardGameCache boardGameCache;

    public static final int MAX_GAMES_TO_RECOMMEND = 100;

    List<Game> allRecommendedGames;

    private boolean userGamesLoaded;

    private boolean settingsLoaded;

    private List<UserGame> topUserGames;

    private Map<Long, Boolean> selectedGames = new HashMap<>();

    private List<Game> topGames = new ArrayList<>();

    private List<Long> topUserGameIds = new ArrayList<>();

    private Set<Long> processedUsers = new HashSet<>();

    private List<GameRecommendations> gameRecommendationList = new ArrayList<>();

    private boolean allRecommendationsLoaded;

    private Map<Long, RecommendedGame> recommendedGameMap = new HashMap<>();

    private List<GameRating> gameRatings = new ArrayList<>();

    private long currentGameId;

    private int numRatingsProcessed;

    private UserGameRecommendationsInfo recommendationsInfo;

    private String mostGamesInCommonUser;

    private int mostGamesInCommon;

    private Map<Long,UserGame> userGamesMap;

    private boolean showRecommendationsLoading;

    public List<Game> getAllRecommendedGames()
    {
        return allRecommendedGames;
    }

    public void setupRecommendGames() throws MalformedURLException, JAXBException
    {
        recommendationsInfo = boardGameCache.getBoardGameUtil().getUserGameRecommendationsInfo(userSession.getUser().getId());
        if (recommendationsInfo == null) {
            recommendationsInfo = new UserGameRecommendationsInfo();
            recommendationsInfo.setUserId(userSession.getUser().getId());
            recommendationsInfo.setYourGameRatingMultiplier(3);
            recommendationsInfo.setYourGameRatingExponent(2);
            recommendationsInfo.setUserGameRatingMultiplier(1);
            recommendationsInfo.setUserGameRatingExponent(2);
            recommendationsInfo.setGamesInCommonMultiplier(3);
            recommendationsInfo.setGamesInCommonExponent(3);
            recommendationsInfo.setCommonDivider(10);
            recommendationsInfo.setNumRatingsPerGame(20);
            recommendationsInfo.setMinGamesInCommon(2);
            recommendationsInfo.setTopGamesExcluded(0);
        }

        List<Game> recommendations = boardGameCache.getBoardGameUtil().getUserGameRecommendations(userSession.getUser().getId());
        if (!recommendations.isEmpty()) {
            allRecommendedGames = recommendations;
            allRecommendationsLoaded = true;
        }
        else {
            loadUserGames();
        }
    }

    public void loadUserGames() throws MalformedURLException, JAXBException
    {
        if (!userSession.getUser().isCollectionLoaded()) {
            if (userSession.getUser().isTopGamesLoaded()) {
                boardGameCache.getBoardGameUtil().deleteUserGames(userSession.getUser().getId());
            }
            boardGameCache.getBoardGameUtil().loadBriefCollection(userSession.getUser());
        }
        userGamesMap = userSession.getUserGamesMap();
        if (userGamesMap == null) {
            userGamesMap = boardGameCache.getBoardGameUtil().getUserGamesMap(userSession.getUser().getId());
        }

        if (topUserGames == null) {
            topUserGames = boardGameCache.getBoardGameUtil().getTopUserGames(userSession.getUser().getId());

            for (UserGame topUserGame : topUserGames) {
                topUserGameIds.add(topUserGame.getGameId());
                topGames.add(boardGameCache.getGame(topUserGame.getGameId()));
                selectedGames.put(topUserGame.getGameId(), topGames.size() < 5);
            }
        }

        processedUsers.add(userSession.getUser().getId());

        userGamesLoaded = true;
    }

    public void saveSettings()
    {
        for (UserGame topUserGame : topUserGames) {
            if (selectedGames.get(topUserGame.getGameId())) {
                GameRecommendations gameRecommendations = new GameRecommendations();
                gameRecommendations.setUserGame(topUserGame);
                gameRecommendations.setGame(boardGameCache.getGame(topUserGame.getGameId()));
                gameRecommendationList.add(gameRecommendations);
            }
        }
        setLoadingForNextGame();
        settingsLoaded = true;
        userGamesLoaded = false;
    }

    public void createNewRecommendations()
    {
        allRecommendationsLoaded = false;
        allRecommendedGames.clear();
        settingsLoaded = false;
        processedUsers.clear();
        gameRecommendationList.clear();
        recommendedGameMap.clear();
        gameRatings.clear();
        currentGameId = 0;
        numRatingsProcessed = 0;
        mostGamesInCommon = 0;
        mostGamesInCommonUser = null;
        showRecommendationsLoading = false;
    }

    private void setLoadingForNextGame()
    {
        GameRecommendations nextGameRecommendations = getNextGameRecommendations();
        if (nextGameRecommendations != null) {
            nextGameRecommendations.setLoading(true);
        }
    }

    private void setLoadingForNextRating()
    {
        GameRating nextGameRating = getNextGameRating();
        if (nextGameRating != null) {
            nextGameRating.setLoading(true);
        }
    }

    public void loadRecommendations()
    {
        GameRecommendations gameRecommendations = getNextGameRecommendations();
        if (gameRecommendations != null) {
            if (currentGameId != gameRecommendations.getGame().getId()) {
                try {
                    numRatingsProcessed = 0;
                    currentGameId = gameRecommendations.getGame().getId();
                    gameRatings = boardGameCache.getBoardGameUtil().getGameRatings(gameRecommendations.getGame(), shouldRefreshRecommendations(gameRecommendations.getGame()));
                    gameRatings.get(0).setLoading(true);
                }
                catch (Throwable t) {
                    gameRecommendations.setError(true);
                    t.printStackTrace();
                }
            }
            else {
                GameRating nextGameRating = getNextGameRating();
                if (nextGameRating != null && numRatingsProcessed <= recommendationsInfo.getNumRatingsPerGame()) {
                    addGameRecommendationsForUser(gameRecommendations, nextGameRating, recommendationsInfo);
                    nextGameRating.setLoaded(true);
                    numRatingsProcessed++;
                    setLoadingForNextRating();
                    if (numRatingsProcessed == recommendationsInfo.getNumRatingsPerGame()) {
                        setMostGamesInCommonUser();
                        gameRecommendations.setLoaded(true);
                        if (getNextGameRecommendations() == null) {
                            showRecommendationsLoading = true;
                        }
                        else {
                            setLoadingForNextGame();
                        }
                    }
                }
            }
        }
        else {
            List<RecommendedGame> allGamesRecommended = new ArrayList<>(recommendedGameMap.values());
            Collections.sort(allGamesRecommended, new RecommendedGameComparator());
            allRecommendedGames = new ArrayList<>();
            for (RecommendedGame recommendedGame : allGamesRecommended) {
                if (allRecommendedGames.size() >= MAX_GAMES_TO_RECOMMEND) {
                    break;
                }
                Game game = boardGameCache.getGame(recommendedGame.getGameId());
                if (game.getRank() > recommendationsInfo.getTopGamesExcluded()) {
                    game.setRecommendationScore(recommendedGame.getScore());
                    game.setYourRatingScore(recommendedGame.getYourRatingScore());
                    game.setOtherRatingScore(recommendedGame.getOtherRatingScore());
                    game.setCommonGamesScore(recommendedGame.getCommonGamesScore());
                    allRecommendedGames.add(game);
                }
            }

            recommendationsInfo.setMostGamesInCommon(mostGamesInCommon);
            recommendationsInfo.setUserWithMostGamesInCommon(mostGamesInCommonUser);

            allRecommendationsLoaded = true;
            boardGameCache.getBoardGameUtil().saveUserGameRecommendations(userSession.getUser().getId(), allRecommendedGames);
            boardGameCache.getBoardGameUtil().saveUserGameRecommendationsInfo(recommendationsInfo);
        }

        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("allRecommendationsLoaded", allRecommendationsLoaded);
    }

    private boolean shouldRefreshRecommendations(Game game)
    {
        if (game.getRecommendationsLastUpdated() == null) {
            return true;
        }

        DateTime lastUpdated = new DateTime(game.getRecommendationsLastUpdated());
        lastUpdated = lastUpdated.plusDays(10);
        lastUpdated = lastUpdated.plusDays(RandomUtils.nextInt(10));
        return DateTime.now().isAfter(lastUpdated);
    }

    private void setMostGamesInCommonUser()
    {
        for (GameRating gameRating : gameRatings) {
            if (gameRating.getGamesInCommon() > mostGamesInCommon) {
                mostGamesInCommon = gameRating.getGamesInCommon();
                mostGamesInCommonUser = gameRating.getUsername();
            }
        }
    }

    private void addGameRecommendationsForUser(GameRecommendations gameRecommendations, GameRating gameRating, UserGameRecommendationsInfo recommendationsInfo)
    {
        try {
            boardGameCache.getBoardGameUtil().addGameRecommendationsForUser(topUserGameIds, processedUsers, gameRecommendations.getUserGame().getRating(), gameRating, userGamesMap, recommendedGameMap, recommendationsInfo);
        }
        catch (Throwable t) {
            gameRating.setError(true);
            t.printStackTrace();
            User user = boardGameCache.getBoardGameUtil().getUserDao().getUser(gameRating.getUsername());
            if (user == null) {
                user = new User();
                user.setUsername(gameRating.getUsername());
                user.setCreatedDate(new Date());
            }
            user.setCollectionError(true);
            boardGameCache.getBoardGameUtil().getUserDao().saveUser(user);
        }
    }

    private GameRecommendations getNextGameRecommendations()
    {
        for (GameRecommendations gameRecommendations : gameRecommendationList) {
            if (!gameRecommendations.isLoaded() && !gameRecommendations.isError()) {
                return gameRecommendations;
            }
        }
        return null;
    }

    private GameRating getNextGameRating()
    {
        for (GameRating gameRating : gameRatings) {
            if (!gameRating.isLoaded() && !gameRating.isError()) {
                return gameRating;
            }
        }
        return null;
    }

    public boolean isUserGamesLoaded()
    {
        return userGamesLoaded;
    }

    public List<GameRecommendations> getGameRecommendationList()
    {
        return gameRecommendationList;
    }

    public boolean isAllRecommendationsLoaded()
    {
        return allRecommendationsLoaded;
    }

    public boolean isSettingsLoaded()
    {
        return settingsLoaded;
    }

    public void setSettingsLoaded(boolean settingsLoaded)
    {
        this.settingsLoaded = settingsLoaded;
    }

    public List<Game> getTopGames()
    {
        return topGames;
    }

    public Map<Long, Boolean> getSelectedGames()
    {
        return selectedGames;
    }

    public List<GameRating> getGameRatings()
    {
        if (gameRatings.size() >= recommendationsInfo.getNumRatingsPerGame()) {
            return gameRatings.subList(0, recommendationsInfo.getNumRatingsPerGame());
        }
        return gameRatings;
    }

    public long getCurrentGameId()
    {
        return currentGameId;
    }

    public UserGameRecommendationsInfo getRecommendationsInfo()
    {
        return recommendationsInfo;
    }

    public void setRecommendationsInfo(UserGameRecommendationsInfo recommendationsInfo)
    {
        this.recommendationsInfo = recommendationsInfo;
    }

    public boolean isShowRecommendationsLoading()
    {
        return showRecommendationsLoading;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }
}
