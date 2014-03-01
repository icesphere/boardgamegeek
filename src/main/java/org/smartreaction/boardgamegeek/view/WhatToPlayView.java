package org.smartreaction.boardgamegeek.view;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.comparators.WhatToPlayRecommendationComparator;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.UserGame;
import org.smartreaction.boardgamegeek.model.WhatToPlayRecommendation;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@ManagedBean
@ViewScoped
public class WhatToPlayView implements Serializable
{
    @ManagedProperty(value = "#{userSession}")
    UserSession userSession;

    @EJB
    BoardGameCache boardGameCache;

    public static final int MAX_GAMES_TO_RECOMMEND = 50;

    private List<WhatToPlayRecommendation> recommendations;

    private boolean recommendationsLoaded;

    private boolean showRecommendationSettings;

    private double ratingModifier = 0;

    private double ratingMultiplier = 3;

    private double ratingExponent = 2;

    private double lastPlayedModifier = 0;

    private double lastPlayedMultiplier = 1;

    private double lastPlayedExponent = 1;

    private double noLastPlayScore = 500;

    public void loadGamesToPlay() throws MalformedURLException, JAXBException
    {
        if (userSession.getGames() == null) {
            userSession.syncGames();
        }

        userSession.loadLastPlayed();

        createRecommendations();

        recommendationsLoaded = true;
    }

    public void createRecommendations()
    {
        recommendations = new ArrayList<>();

        for (Game game : userSession.getGamesWithoutExpansions()) {
            if (userSession.getUserGamesMap().get(game.getId()).isOwned()) {
                recommendations.add(getRecommendation(game));
                if (recommendations.size() >= MAX_GAMES_TO_RECOMMEND) {
                    break;
                }
            }
        }

        Collections.sort(recommendations, new WhatToPlayRecommendationComparator());
    }

    private WhatToPlayRecommendation getRecommendation(Game game)
    {
        WhatToPlayRecommendation recommendation = new WhatToPlayRecommendation();
        recommendation.setGame(game);

        UserGame userGame = userSession.getUserGamesMap().get(game.getId());

        double rating;
        float yourRating = userGame.getRating();
        if (yourRating > 0) {
            rating = yourRating;
        }
        else {
            rating = game.getAverageRating();
        }
        double ratingScore = Math.pow((ratingMultiplier * (rating + ratingModifier)), ratingExponent);
        recommendation.setRatingScore(ratingScore);

        double lastPlayedScore;
        Date lastPlayed = game.getLastPlayed();
        if (lastPlayed != null) {
            int daysSinceLastPlayed = Days.daysBetween(new DateTime(lastPlayed), new DateTime()).getDays();
            lastPlayedScore =  Math.pow((lastPlayedMultiplier * (daysSinceLastPlayed + lastPlayedModifier)), lastPlayedExponent);
        }
        else {
            lastPlayedScore = noLastPlayScore;
        }
        recommendation.setLastPlayedScore(lastPlayedScore);

        recommendation.setScore(ratingScore + lastPlayedScore);

        return recommendation;
    }

    public List<WhatToPlayRecommendation> getRecommendations()
    {
        return recommendations;
    }

    public double getRatingMultiplier()
    {
        return ratingMultiplier;
    }

    public void setRatingMultiplier(double ratingMultiplier)
    {
        this.ratingMultiplier = ratingMultiplier;
    }

    public double getRatingExponent()
    {
        return ratingExponent;
    }

    public void setRatingExponent(double ratingExponent)
    {
        this.ratingExponent = ratingExponent;
    }

    public double getLastPlayedMultiplier()
    {
        return lastPlayedMultiplier;
    }

    public void setLastPlayedMultiplier(double lastPlayedMultiplier)
    {
        this.lastPlayedMultiplier = lastPlayedMultiplier;
    }

    public double getLastPlayedExponent()
    {
        return lastPlayedExponent;
    }

    public void setLastPlayedExponent(double lastPlayedExponent)
    {
        this.lastPlayedExponent = lastPlayedExponent;
    }

    public double getNoLastPlayScore()
    {
        return noLastPlayScore;
    }

    public void setNoLastPlayScore(double noLastPlayScore)
    {
        this.noLastPlayScore = noLastPlayScore;
    }

    public double getRatingModifier()
    {
        return ratingModifier;
    }

    public void setRatingModifier(double ratingModifier)
    {
        this.ratingModifier = ratingModifier;
    }

    public double getLastPlayedModifier()
    {
        return lastPlayedModifier;
    }

    public void setLastPlayedModifier(double lastPlayedModifier)
    {
        this.lastPlayedModifier = lastPlayedModifier;
    }

    public boolean isRecommendationsLoaded()
    {
        return recommendationsLoaded;
    }

    public boolean isShowRecommendationSettings()
    {
        return showRecommendationSettings;
    }

    public void setShowRecommendationSettings(boolean showRecommendationSettings)
    {
        this.showRecommendationSettings = showRecommendationSettings;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }
}
