package org.smartreaction.boardgamegeek.business;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;
import org.smartreaction.boardgamegeek.db.dao.GameDao;
import org.smartreaction.boardgamegeek.db.dao.UserDao;
import org.smartreaction.boardgamegeek.db.entities.*;
import org.smartreaction.boardgamegeek.model.RecommendedGame;
import org.smartreaction.boardgamegeek.services.BoardGameGeekService;
import org.smartreaction.boardgamegeek.xml.game.*;
import org.smartreaction.boardgamegeek.xml.gamewithrating.Comment;
import org.smartreaction.boardgamegeek.xml.collection.Item;
import org.smartreaction.boardgamegeek.xml.collection.Items;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Stateless
public class BoardGameUtil
{
    @EJB
    GameDao gameDao;

    @EJB
    UserDao userDao;

    @EJB
    BoardGameGeekService boardGameGeekService;

    public Game getGame(long gameId)
    {
        try {
            Game game = gameDao.getGame(gameId);
            if (game != null) {
                return game;
            }
            else {
                return createGame(gameId);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Game createGame(long gameId) throws JAXBException, MalformedURLException
    {
        org.smartreaction.boardgamegeek.xml.game.Item gameItem = boardGameGeekService.getGame(gameId);
        Game game = new Game();
        game.setId(gameId);
        if (gameItem == null) {
            return null;
        }
        syncGame(game, gameItem, false);
        return game;
    }

    public void syncGame(Game game)
    {
        try {
            org.smartreaction.boardgamegeek.xml.game.Item gameItem = boardGameGeekService.getGame(game.getId());
            syncGame(game, gameItem, true);
        }
        catch (Exception e) {
            System.out.println("Error syncing game " + game.getName() + game.getId());
            throw new RuntimeException(e);
        }
    }

    public void initUserGames(long userId, String username) throws MalformedURLException, JAXBException
    {
        Items collection = boardGameGeekService.getCollection(username);
        syncUserGames(userId, collection, new HashMap<Long, UserGame>(0));
    }

    public void loadUserTopGames(User user) throws MalformedURLException, JAXBException
    {
        Items collection = boardGameGeekService.getCollectionTopGames(user.getUsername());
        syncUserGames(user.getId(), collection, new HashMap<Long, UserGame>(0));
        user.setTopGamesLoaded(true);
        userDao.saveUser(user);
    }

    public void loadBriefCollection(User user) throws MalformedURLException, JAXBException
    {
        Items collection = boardGameGeekService.getBriefCollection(user.getUsername());
        syncUserGames(user.getId(), collection, new HashMap<Long, UserGame>(0));
        user.setTopGamesLoaded(true);
        userDao.saveUser(user);
    }

    public void syncUserGames(long userId, String username, Map<Long, UserGame> userGamesMap) throws MalformedURLException, JAXBException
    {
        Items collection = boardGameGeekService.getCollection(username);
        syncUserGames(userId, collection, userGamesMap);
    }

    @Asynchronous
    public void syncUserGamesAsynchronous(long userId, String username, Map<Long, UserGame> userGamesMap)
    {
        try {
            syncUserGames(userId, username, userGamesMap);
        }
        catch (Exception e) {
            System.out.println("Error syncing user games");
            e.printStackTrace();
        }
    }

    private void syncUserGames(long userId, Items collection, Map<Long, UserGame> userGamesMap) throws MalformedURLException, JAXBException
    {
        for (Item item : collection.getItem()) {
            UserGame userGame;
            long gameId = NumberUtils.toLong(item.getObjectid());
            if (userGamesMap.containsKey(gameId)) {
                userGame = userGamesMap.get(gameId);
                syncUserGame(item, userGame);
                gameDao.updateUserGame(userGame);
            }
            else {
                if (createGameIfNotExists(gameId)) {
                    createUserGame(userId, item, gameId);
                }
            }
        }
    }

    private void createUserGame(long userId, Item item, long gameId)
    {
        UserGame userGame;
        userGame = new UserGame();
        userGame.setUserId(userId);
        userGame.setGameId(gameId);
        syncUserGame(item, userGame);
        gameDao.createUserGame(userGame);
    }

    //returns false if game does not exist and could not be created
    private boolean createGameIfNotExists(long gameId) throws MalformedURLException, JAXBException
    {
        return gameDao.hasGame(gameId) || createGame(gameId) != null;
    }

    void syncGame(Game game, org.smartreaction.boardgamegeek.xml.game.Item gameItem, boolean exists) throws MalformedURLException, JAXBException
    {
        try {
            game.setLastUpdated(new Date());
            game.setName(getGameName(gameItem));
            game.setDescription(gameItem.getDescription());
            if (gameItem.getYearpublished() != null) {
                game.setYearPublished(NumberUtils.toInt(gameItem.getYearpublished().getValue()));
            }
            game.setImage(gameItem.getImage());
            game.setThumbnailImage(gameItem.getThumbnail());
            game.setMinPlayers(NumberUtils.toInt(gameItem.getMinplayers().getValue()));
            game.setMaxPlayers(NumberUtils.toInt(gameItem.getMaxplayers().getValue()));
            game.setPlayingTime(getPlayingTime(gameItem));
            game.setNumOwned(NumberUtils.toInt(gameItem.getStatistics().getRatings().getOwned().getValue()));
            game.setUsersRated(NumberUtils.toInt(gameItem.getStatistics().getRatings().getUsersrated().getValue()));
            game.setAverageRating(NumberUtils.toFloat(gameItem.getStatistics().getRatings().getAverage().getValue()));
            game.setBayesAverageRating(NumberUtils.toFloat(gameItem.getStatistics().getRatings().getBayesaverage().getValue()));
            game.setRank(getGameRank(gameItem.getStatistics().getRatings().getRanks()));
            game.setRatings(NumberUtils.toInt(gameItem.getStatistics().getRatings().getUsersrated().getValue()));
            game.setCategories(getCategories(gameItem));
            game.setMechanics(getMechanics(gameItem));
        }
        catch (Exception e) {
            System.out.println("Error updating game from xml: " + game.getName() + game.getId() + " error: "+e.getMessage());
            e.printStackTrace();
        }

        if (exists) {
            gameDao.updateGame(game);
        }
        else {
            gameDao.createGame(game);
        }
        gameDao.flush();

        syncExpansions(game, gameItem, exists);
        syncParentGames(game, gameItem, exists);
    }

    private int getGameRank(Ranks ranks)
    {
        for (Rank rank : ranks.getRank()) {
            if (rank.getName().equals("boardgame")) {
                return NumberUtils.toInt(rank.getValue());
            }
        }
        return 0;
    }

    private int getPlayingTime(org.smartreaction.boardgamegeek.xml.game.Item gameItem)
    {
        List<Object> minageOrPlayingtimeOrPoll = gameItem.getMinageOrPlayingtimeOrPoll();
        for (Object object : minageOrPlayingtimeOrPoll) {
            if (object instanceof Playingtime) {
                return NumberUtils.toInt(((Playingtime)object).getValue());
            }
        }
        return 0;
    }

    private void syncExpansions(Game game, org.smartreaction.boardgamegeek.xml.game.Item gameItem, boolean exists) throws MalformedURLException, JAXBException
    {
        List<Long> expansionIds = getExpansionIds(gameItem);
        if (!exists) {
            for (Long expansionId : expansionIds) {
                addExpansionToGame(game, expansionId);
            }
            game.setExpansions(gameDao.getExpansions(game.getId()));
        }
        else {
            List<Game> existingExpansions = gameDao.getExpansions(game.getId());
            List<Long> existingExpansionIds = new ArrayList<Long>();
            for (Game expansion : existingExpansions) {
                existingExpansionIds.add(expansion.getId());
            }
            boolean addedExpansions = false;
            for (Long expansionId : expansionIds) {
                if (!existingExpansionIds.contains(expansionId)) {
                    addExpansionToGame(game, expansionId);
                    addedExpansions = true;
                }
            }
            if (addedExpansions) {
                game.setExpansions(gameDao.getExpansions(game.getId()));
            }
        }
    }

    private List<Long> getExpansionIds(org.smartreaction.boardgamegeek.xml.game.Item gameItem)
    {
        List<Long> expansionIds = new ArrayList<Long>();
        for (Link link : gameItem.getLink()) {
            if (link.getType().equals(BoardGameGeekConstants.EXPANSION_TYPE) && !link.isInbound()) {
                expansionIds.add(NumberUtils.toLong(link.getId()));
            }
        }
        return expansionIds;
    }

    private void addExpansionToGame(Game game, long expansionId) throws MalformedURLException, JAXBException
    {
        if(createGameIfNotExists(expansionId)) {
            Expansion expansion = new Expansion();
            expansion.setGameId(game.getId());
            expansion.setExpansionId(expansionId);
            gameDao.createExpansion(expansion);
        }
    }

    private void syncParentGames(Game game, org.smartreaction.boardgamegeek.xml.game.Item gameItem, boolean exists) throws MalformedURLException, JAXBException
    {
        List<Long> parentGameIds = getParentGameIds(gameItem);
        if (!exists) {
            for (Long parentGameId : parentGameIds) {
                addParentGameToGame(game, parentGameId);
            }
            game.setParentGames(gameDao.getParentGames(game.getId()));
        }
        else {
            List<Game> existingParentGames = gameDao.getParentGames(game.getId());
            List<Long> existingParentGameIds = new ArrayList<Long>();
            for (Game parentGame : existingParentGames) {
                existingParentGameIds.add(parentGame.getId());
            }
            boolean addedParentGames = false;
            for (Long parentGameId : parentGameIds) {
                if (!existingParentGameIds.contains(parentGameId)) {
                    addParentGameToGame(game, parentGameId);
                    addedParentGames = true;
                }
            }
            if (addedParentGames) {
                game.setParentGames(gameDao.getParentGames(game.getId()));
            }
        }
    }

    private List<Long> getParentGameIds(org.smartreaction.boardgamegeek.xml.game.Item gameItem)
    {
        List<Long> parentGameIds = new ArrayList<Long>();
        for (Link link : gameItem.getLink()) {
            if (link.getType().equals(BoardGameGeekConstants.EXPANSION_TYPE) && link.isInbound()) {
                parentGameIds.add(NumberUtils.toLong(link.getId()));
            }
        }
        return parentGameIds;
    }

    private String getCategories(org.smartreaction.boardgamegeek.xml.game.Item gameItem)
    {
        List<String> categories = new ArrayList<String>(0);
        for (Link link : gameItem.getLink()) {
            if (link.getType().equals(BoardGameGeekConstants.CATEGORY_TYPE)) {
                categories.add(link.getValue());
            }
        }
        if (!categories.isEmpty()) {
            return StringUtils.join(categories, ", ");
        }
        else {
            return null;
        }
    }

    private String getMechanics(org.smartreaction.boardgamegeek.xml.game.Item gameItem)
    {
        List<String> mechanics = new ArrayList<String>(0);
        for (Link link : gameItem.getLink()) {
            if (link.getType().equals(BoardGameGeekConstants.MECHANIC_TYPE)) {
                mechanics.add(link.getValue());
            }
        }
        if (!mechanics.isEmpty()) {
            return StringUtils.join(mechanics, ", ");
        }
        else {
            return null;
        }
    }

    private void addParentGameToGame(Game game, long parentGameId) throws MalformedURLException, JAXBException
    {
        if(createGameIfNotExists(parentGameId)) {
            ParentGame parentGame = new ParentGame();
            parentGame.setGameId(game.getId());
            parentGame.setParentGameId(parentGameId);
            gameDao.createParentGame(parentGame);
        }
    }

    public String getGameName(org.smartreaction.boardgamegeek.xml.game.Item gameItem)
    {
        for (Name name : gameItem.getName()) {
            if (name.getType().equals(BoardGameGeekConstants.PRIMARY_TYPE)) {
                return name.getValue();
            }
        }
        return "";
    }

    private void syncUserGame(Item item, UserGame userGame)
    {
        userGame.setCollectionId(NumberUtils.toLong(item.getCollid()));
        setLastModifiedDate(item, userGame);
        userGame.setLastUpdated(new Date());
        userGame.setRating(NumberUtils.toFloat(item.getStats().getRating().getValue()));
        userGame.setOwned(getBooleanFromStatusString(item.getStatus().getOwn()));
        userGame.setPreviouslyOwned(getBooleanFromStatusString(item.getStatus().getPrevowned()));
        userGame.setForTrade(getBooleanFromStatusString(item.getStatus().getFortrade()));
        userGame.setWant(getBooleanFromStatusString(item.getStatus().getWant()));
        userGame.setWantToPlay(getBooleanFromStatusString(item.getStatus().getWanttoplay()));
        userGame.setWantToBuy(getBooleanFromStatusString(item.getStatus().getWanttobuy()));
        userGame.setWishList(getBooleanFromStatusString(item.getStatus().getWishlist()));
        userGame.setPreOrdered(getBooleanFromStatusString(item.getStatus().getPreordered()));
        userGame.setNumPlays(NumberUtils.toInt(item.getNumplays()));
    }

    private void setLastModifiedDate(Item item, UserGame userGame)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String lastModifiedString = item.getStatus().getLastmodified();
        if (!StringUtils.isEmpty(lastModifiedString)) {
            try {
                userGame.setLastModified(simpleDateFormat.parse(lastModifiedString));
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean getBooleanFromStatusString(String status)
    {
        return "1".equals(status);
    }

    public List<GameRating> getGameRatings(long gameId) throws MalformedURLException, JAXBException
    {
        List<GameRating> gameRatings = gameDao.getGameRatings(gameId);
        if (gameRatings.isEmpty()) {
            gameRatings = loadGameRatings(gameId);
        }
        return gameRatings;
    }

    public void addGameRecommendationsForUser(List<Long> topUserGameIds, Set<Long> processedUsers, double userGameRating,
                                              GameRating gameRating, Map<Long, UserGame> userGamesMap,
                                              Map<Long, RecommendedGame> recommendedGameMap, UserGameRecommendationsInfo recommendationsInfo) throws MalformedURLException, JAXBException
    {
        User user = userDao.getUser(gameRating.getUsername());
        if (user == null) {
            user = new User();
            user.setUsername(gameRating.getUsername());
            user.setCreatedDate(new Date());
            userDao.createUser(user);
            userDao.flush();
        }

        if (user.isCollectionError()) {
            gameRating.setError(true);
        }
        else if (!processedUsers.contains(user.getId())) {
            if (!user.isCollectionLoaded() && !user.isTopGamesLoaded()) {
                loadUserTopGames(user);
            }
            List<UserGame> topUserGamesForRatingUser = getTopUserGames(user.getId());
            int numGamesInCommon = getNumGamesInCommon(topUserGameIds, topUserGamesForRatingUser);
            gameRating.setGamesInCommon(numGamesInCommon);

            if (numGamesInCommon >= recommendationsInfo.getMinGamesInCommon()) {
                for (UserGame userGame : topUserGamesForRatingUser) {
                    if (!topUserGameIds.contains(userGame.getGameId()) && userGame.getRating() >= 8 && !userGamesMap.containsKey(userGame.getGameId())) {
                        float commonDivider = recommendationsInfo.getCommonDivider();
                        double yourRatingScore = (recommendationsInfo.getYourGameRatingMultiplier()*Math.pow(userGameRating, recommendationsInfo.getYourGameRatingExponent()))/commonDivider;
                        double otherRatingScore = (recommendationsInfo.getUserGameRatingMultiplier()*Math.pow(userGame.getRating(), recommendationsInfo.getUserGameRatingExponent()))/commonDivider;
                        double commonGamesScore = (recommendationsInfo.getGamesInCommonMultiplier()*Math.pow(numGamesInCommon, recommendationsInfo.getGamesInCommonExponent()))/commonDivider;
                        double totalScore = yourRatingScore + otherRatingScore + commonGamesScore;

                        RecommendedGame recommendedGame = recommendedGameMap.get(userGame.getGameId());
                        if (recommendedGame == null) {
                            recommendedGame = new RecommendedGame();
                            recommendedGame.setGameId(userGame.getGameId());
                        }
                        recommendedGame.setScore(recommendedGame.getScore() + totalScore);
                        recommendedGame.setYourRatingScore(recommendedGame.getYourRatingScore() + yourRatingScore);
                        recommendedGame.setOtherRatingScore(recommendedGame.getOtherRatingScore() + otherRatingScore);
                        recommendedGame.setCommonGamesScore(recommendedGame.getCommonGamesScore() + commonGamesScore);

                        recommendedGameMap.put(userGame.getGameId(), recommendedGame);
                    }
                }
            }

            processedUsers.add(user.getId());
        }
    }

    private int getNumGamesInCommon(List<Long> topUserGameIds, List<UserGame> topUserGamesForRatingUser)
    {
        int numInCommon = 0;
        for (UserGame userGame : topUserGamesForRatingUser) {
            if (topUserGameIds.contains(userGame.getGameId())) {
                numInCommon++;
            }
        }
        return numInCommon;
    }

    private List<GameRating> loadGameRatings(long gameId) throws MalformedURLException, JAXBException
    {
        List<GameRating> ratings = new ArrayList<GameRating>();

        org.smartreaction.boardgamegeek.xml.gamewithrating.Item gameItem = boardGameGeekService.getGameWithRatings(gameId);

        for (Comment comment : gameItem.getComments().getComment()) {
            GameRating gameRating = new GameRating();
            gameRating.setGameId(gameId);
            gameRating.setUsername(comment.getUsername());
            gameRating.setRating(NumberUtils.toFloat(comment.getRating()));
            gameDao.createGameRating(gameRating);
            ratings.add(gameRating);
        }

        return ratings;
    }

    public List<UserGame> getTopUserGames(long userId)
    {
        return gameDao.getTopUserGames(userId);
    }

    public UserDao getUserDao()
    {
        return userDao;
    }

    public List<Game> getUserGameRecommendations(long userId)
    {
        return gameDao.getUserGameRecommendations(userId);
    }

    public void saveUserGameRecommendations(long userId, List<Game> games)
    {
        gameDao.deleteUserGameRecommendations(userId);
        gameDao.flush();

        for (Game game : games) {
            UserGameRecommendation recommendation = new UserGameRecommendation();
            recommendation.setUserId(userId);
            recommendation.setGameId(game.getId());
            recommendation.setScore(game.getRecommendationScore());
            recommendation.setYourRatingScore(game.getYourRatingScore());
            recommendation.setOtherRatingScore(game.getOtherRatingScore());
            recommendation.setCommonGamesScore(game.getCommonGamesScore());
            gameDao.createUserGameRecommendation(recommendation);
        }
    }

    public void saveUserGameRecommendationsInfo(UserGameRecommendationsInfo info)
    {
        gameDao.saveUserGameRecommendationsInfo(info);
    }

    public UserGameRecommendationsInfo getUserGameRecommendationsInfo(long userId)
    {
        return gameDao.getUserGameRecommendationsInfo(userId);
    }

    public Map<Long, UserGame> getUserGamesMap(long userId)
    {
        return gameDao.getUserGamesMap(userId);
    }

    public void deleteUserGames(long userId)
    {
        gameDao.deleteUserGames(userId);
    }
}