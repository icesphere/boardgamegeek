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

    private double ratingModifier;

    private double ratingMultiplier;

    private double ratingExponent;

    private double lastPlayedModifier;

    private double lastPlayedMultiplier;

    private double lastPlayedExponent;

    private double noLastPlayScore;

    private double minRating = 7;

    private Integer minPlayers;

    private Integer maxPlayers;

    private Integer minPlayingTime;

    private Integer maxPlayingTime;

    private String formulaType = "default";

    public void loadGamesToPlay() throws MalformedURLException, JAXBException
    {
        if (userSession.getGames() == null) {
            userSession.syncGames();
        }

        userSession.loadLastPlayed();

        formulaTypeChanged();

        recommendationsLoaded = true;
    }

    public void formulaTypeChanged()
    {
        ratingModifier = 0;
        ratingMultiplier = 2;
        ratingExponent = 2;

        lastPlayedModifier = 0;
        lastPlayedMultiplier = 1;
        lastPlayedExponent = 1;

        noLastPlayScore = 400;

        minRating = 7;
        minPlayers = null;
        maxPlayers = null;
        minPlayingTime = null;
        maxPlayingTime = null;

        if (formulaType.equals("rating")) {
            ratingModifier = 2;
            ratingMultiplier = 3;

            noLastPlayScore = 200;
        }
        else if (formulaType.equals("lastPlayed")) {
            lastPlayedMultiplier = 2;

            noLastPlayScore = 500;
        }

        if (!formulaType.equals("custom")) {
            createRecommendations();
        }
    }

    public void createRecommendations()
    {
        recommendations = new ArrayList<>();

        for (Game game : userSession.getGamesWithoutExpansions()) {
            try {
                if (userSession.getUserGamesMap().get(game.getId()).isOwned()) {
                    WhatToPlayRecommendation recommendation = getRecommendation(game);
                    if (recommendation != null) {
                        recommendations.add(recommendation);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        Collections.sort(recommendations, new WhatToPlayRecommendationComparator());

        recommendations = recommendations.subList(0, MAX_GAMES_TO_RECOMMEND);
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

        if (rating < minRating) {
            return null;
        }

        if (minPlayers != null && game.getMaxPlayers() > 0 && game.getMaxPlayers() < minPlayers) {
            return null;
        }

        if (maxPlayers != null && game.getMaxPlayers() > 0 && game.getMaxPlayers() > maxPlayers) {
            return null;
        }

        if (minPlayingTime != null && game.getPlayingTime() > 0 && game.getPlayingTime() < minPlayingTime) {
            return null;
        }

        if (maxPlayingTime != null && game.getPlayingTime() > 0 && game.getPlayingTime() > maxPlayingTime) {
            return null;
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

    public double getMinRating()
    {
        return minRating;
    }

    public void setMinRating(double minRating)
    {
        this.minRating = minRating;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }

    public String getFormulaType()
    {
        return formulaType;
    }

    public void setFormulaType(String formulaType)
    {
        this.formulaType = formulaType;
    }

    public Integer getMinPlayers()
    {
        return minPlayers;
    }

    public void setMinPlayers(Integer minPlayers)
    {
        this.minPlayers = minPlayers;
    }

    public Integer getMaxPlayers()
    {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers)
    {
        this.maxPlayers = maxPlayers;
    }

    public Integer getMinPlayingTime()
    {
        return minPlayingTime;
    }

    public void setMinPlayingTime(Integer minPlayingTime)
    {
        this.minPlayingTime = minPlayingTime;
    }

    public Integer getMaxPlayingTime()
    {
        return maxPlayingTime;
    }

    public void setMaxPlayingTime(Integer maxPlayingTime)
    {
        this.maxPlayingTime = maxPlayingTime;
    }
}
