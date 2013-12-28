package org.smartreaction.boardgamegeek.db.dao;

import org.smartreaction.boardgamegeek.db.entities.*;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Stateless
public class GameDao
{
    @PersistenceContext
    EntityManager em;

    public Game getGame(long gameId)
    {
        Game game = em.find(Game.class, gameId);
        if (game != null) {
            game.setExpansions(getExpansions(game.getId()));
            game.setParentGames(getParentGames(game.getId()));
        }
        return game;
    }

    public boolean hasGame(long gameId)
    {
        return getGame(gameId) != null;
    }

    public void createGame(Game game)
    {
        em.persist(game);
    }

    public void updateGame(Game game)
    {
        em.merge(game);
    }

    public List<Game> getGames(long userId)
    {
        TypedQuery<Game> query = em.createQuery("select g from Game g, UserGame ug where g.id = ug.gameId and ug.userId = :userId order by g.name", Game.class);
        query.setParameter("userId", userId);
        List<Game> games = query.getResultList();
        for (Game game : games) {
            game.setExpansions(getExpansions(game.getId()));
            game.setParentGames(getParentGames(game.getId()));
        }
        return games;
    }

    public void createUserGame(UserGame userGame)
    {
        em.persist(userGame);
    }

    public void updateUserGame(UserGame userGame)
    {
        em.merge(userGame);
    }

    public void flush()
    {
        em.flush();
    }

    public Map<Long, UserGame> getUserGamesMap(long userId)
    {
        TypedQuery<UserGame> query = em.createQuery("select ug from UserGame ug where ug.userId = :userId", UserGame.class);
        query.setParameter("userId", userId);
        List<UserGame> userGames = query.getResultList();

        Map<Long, UserGame> userGamesMap = new HashMap<>(userGames.size());
        for (UserGame userGame : userGames) {
            userGamesMap.put(userGame.getGameId(), userGame);
        }

        return userGamesMap;
    }

    public List<UserGame> getTopUserGames(long userId)
    {
        TypedQuery<UserGame> query = em.createQuery("select distinct ug from UserGame ug, Game g where ug.userId = :userId and ug.gameId = g.id and g.rank > 0 and ug.rating >= 8 order by ug.rating desc", UserGame.class);
        query.setParameter("userId", userId);
        query.setMaxResults(20);
        return query.getResultList();
    }

    public void createExpansion(Expansion expansion)
    {
        em.persist(expansion);
    }

    public void createParentGame(ParentGame parentGame)
    {
        em.persist(parentGame);
    }

    public List<Game> getExpansions(long gameId)
    {
        TypedQuery<Game> query = em.createQuery("select g from Game g, Expansion e where e.gameId = :gameId and g.id = e.expansionId order by g.id", Game.class);
        query.setParameter("gameId", gameId);
        return query.getResultList();
    }

    public List<Game> getParentGames(long gameId)
    {
        TypedQuery<Game> query = em.createQuery("select g from Game g, ParentGame pg where pg.gameId = :gameId and g.id = pg.parentGameId", Game.class);
        query.setParameter("gameId", gameId);
        return query.getResultList();
    }

    public List<GameRating> getGameRatings(long gameId)
    {
        TypedQuery<GameRating> query = em.createQuery("select gr from GameRating gr where gr.gameId = :gameId", GameRating.class);
        query.setParameter("gameId", gameId);
        return query.getResultList();
    }

    public List<GameComment> getGameComments(long gameId)
    {
        TypedQuery<GameComment> query = em.createQuery("select gc from GameComment gc where gc.gameId = :gameId", GameComment.class);
        query.setParameter("gameId", gameId);
        return query.getResultList();
    }

    public void createGameRating(GameRating gameRating)
    {
        em.persist(gameRating);
    }

    public void createGameComment(GameComment gameComment)
    {
        em.persist(gameComment);
    }

    public List<Game> getUserGameRecommendations(long userId)
    {
        TypedQuery<UserGameRecommendation> query = em.createQuery("select ugr from UserGameRecommendation ugr where ugr.userId = :userId order by ugr.score desc", UserGameRecommendation.class);
        query.setParameter("userId", userId);
        List<UserGameRecommendation> recommendations = query.getResultList();

        List<Game> games = new ArrayList<>(recommendations.size());
        for (UserGameRecommendation recommendation : recommendations) {
            Game game = getGame(recommendation.getGameId());
            game.setRecommendationScore(recommendation.getScore());
            game.setYourRatingScore(recommendation.getYourRatingScore());
            game.setOtherRatingScore(recommendation.getOtherRatingScore());
            game.setCommonGamesScore(recommendation.getCommonGamesScore());
            games.add(game);
        }
        return games;
    }

    public void deleteUserGameRecommendations(long userId)
    {
        Query query = em.createQuery("delete from UserGameRecommendation ugr where ugr.userId = :userId");
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    public void createUserGameRecommendation(UserGameRecommendation recommendation)
    {
        em.persist(recommendation);
    }

    public void saveUserGameRecommendationsInfo(UserGameRecommendationsInfo info)
    {
        em.merge(info);
    }

    public UserGameRecommendationsInfo getUserGameRecommendationsInfo(long userId)
    {
        return em.find(UserGameRecommendationsInfo.class, userId);
    }

    public void deleteUserGames(long userId)
    {
        Query query = em.createQuery("delete from UserGame ug where ug.userId = :userId");
        query.setParameter("userId", userId);
        query.executeUpdate();
    }

    public void deleteGameComments(long gameId)
    {
        Query query = em.createQuery("delete from GameComment gc where gc.gameId = :gameId");
        query.setParameter("gameId", gameId);
        query.executeUpdate();
        em.flush();
    }

    public List<UserPlay> getUserPlays(long userId)
    {
        TypedQuery<UserPlay> query = em.createQuery("select up from UserPlay up where up.userId = :userId order by up.playId desc", UserPlay.class);
        query.setParameter("userId", userId);

        return query.getResultList();
    }

    public List<UserPlay> getUserPlaysForGame(long userId, long gameId)
    {
        TypedQuery<UserPlay> query = em.createQuery("select up from UserPlay up where up.userId = :userId and up.gameId = :gameId order by up.playId desc", UserPlay.class);
        query.setParameter("userId", userId);
        query.setParameter("gameId", gameId);

        return query.getResultList();
    }

    public List<UserPlay> getUserPlaysForGameByDate(long userId, long gameId, Date startDate, Date endDate)
    {
        TypedQuery<UserPlay> query = em.createQuery("select up from UserPlay up where up.userId = :userId and up.gameId = :gameId and up.playDate >= :startDate and up.playDate <= :endDate order by up.playId desc", UserPlay.class);
        query.setParameter("userId", userId);
        query.setParameter("gameId", gameId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        return query.getResultList();
    }

    public void createUserPlay(UserPlay userPlay)
    {
        em.persist(userPlay);
    }

    public List<Game> getTopGames() {
        TypedQuery<Game> query = em.createQuery("select g from Game g where g.rank > 0 order by g.rank", Game.class);
        query.setMaxResults(100);

        return query.getResultList();
    }

    public List<Game> searchGames(String searchString) {
        TypedQuery<Game> query = em.createQuery("select g from Game g where g.name like :searchString", Game.class);
        query.setParameter("searchString", "%" + searchString + "%");

        return query.getResultList();
    }

    public void deleteGameRatings(long gameId)
    {
        TypedQuery<GameRating> query = em.createQuery("delete from GameRating gr where gr.gameId = :gameId", GameRating.class);
        query.setParameter("gameId", gameId);

        query.executeUpdate();
    }
}
