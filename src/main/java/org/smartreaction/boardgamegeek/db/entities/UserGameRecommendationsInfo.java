package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_game_recommendations_info")
public class UserGameRecommendationsInfo
{
    @Id
    @Column(name = "user_id")
    private long userId;

    @Column(name = "your_game_rating_multiplier")
    private float yourGameRatingMultiplier;

    @Column(name = "user_game_rating_multiplier")
    private float userGameRatingMultiplier;

    @Column(name = "your_game_rating_exponent")
    private float yourGameRatingExponent;

    @Column(name = "user_game_rating_exponent")
    private float userGameRatingExponent;

    @Column(name = "games_in_common_multiplier")
    private float gamesInCommonMultiplier;

    @Column(name = "games_in_common_exponent")
    private float gamesInCommonExponent;

    @Column(name = "common_divider")
    private float commonDivider;

    @Column(name = "user_with_most_games_in_common")
    private String userWithMostGamesInCommon;

    @Column(name = "most_games_in_common")
    private int mostGamesInCommon;

    @Column(name = "num_ratings_per_game")
    private int numRatingsPerGame;

    @Column(name = "min_games_in_common")
    private int minGamesInCommon;

    @Column(name = "top_games_excluded")
    private int topGamesExcluded;

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public float getYourGameRatingMultiplier()
    {
        return yourGameRatingMultiplier;
    }

    public void setYourGameRatingMultiplier(float yourGameRatingMultiplier)
    {
        this.yourGameRatingMultiplier = yourGameRatingMultiplier;
    }

    public float getUserGameRatingMultiplier()
    {
        return userGameRatingMultiplier;
    }

    public void setUserGameRatingMultiplier(float userGameRatingMultiplier)
    {
        this.userGameRatingMultiplier = userGameRatingMultiplier;
    }

    public float getYourGameRatingExponent()
    {
        return yourGameRatingExponent;
    }

    public void setYourGameRatingExponent(float yourGameRatingExponent)
    {
        this.yourGameRatingExponent = yourGameRatingExponent;
    }

    public float getUserGameRatingExponent()
    {
        return userGameRatingExponent;
    }

    public void setUserGameRatingExponent(float userGameRatingExponent)
    {
        this.userGameRatingExponent = userGameRatingExponent;
    }

    public float getGamesInCommonMultiplier()
    {
        return gamesInCommonMultiplier;
    }

    public void setGamesInCommonMultiplier(float gamesInCommonMultiplier)
    {
        this.gamesInCommonMultiplier = gamesInCommonMultiplier;
    }

    public float getGamesInCommonExponent()
    {
        return gamesInCommonExponent;
    }

    public void setGamesInCommonExponent(float gamesInCommonExponent)
    {
        this.gamesInCommonExponent = gamesInCommonExponent;
    }

    public float getCommonDivider()
    {
        return commonDivider;
    }

    public void setCommonDivider(float commonDivider)
    {
        this.commonDivider = commonDivider;
    }

    public String getUserWithMostGamesInCommon()
    {
        return userWithMostGamesInCommon;
    }

    public void setUserWithMostGamesInCommon(String userWithMostGamesInCommon)
    {
        this.userWithMostGamesInCommon = userWithMostGamesInCommon;
    }

    public int getMostGamesInCommon()
    {
        return mostGamesInCommon;
    }

    public void setMostGamesInCommon(int mostGamesInCommon)
    {
        this.mostGamesInCommon = mostGamesInCommon;
    }

    public int getNumRatingsPerGame()
    {
        return numRatingsPerGame;
    }

    public void setNumRatingsPerGame(int numRatingsPerGame)
    {
        this.numRatingsPerGame = numRatingsPerGame;
    }

    public int getMinGamesInCommon()
    {
        return minGamesInCommon;
    }

    public void setMinGamesInCommon(int minGamesInCommon)
    {
        this.minGamesInCommon = minGamesInCommon;
    }

    public int getTopGamesExcluded()
    {
        return topGamesExcluded;
    }

    public void setTopGamesExcluded(int topGamesExcluded)
    {
        this.topGamesExcluded = topGamesExcluded;
    }
}
