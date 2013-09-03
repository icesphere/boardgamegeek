package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_game_recommendations")
public class UserGameRecommendation
{
    @Id
    @Column(name = "user_id")
    private long userId;

    @Id
    @Column(name = "game_id")
    private long gameId;

    @Column
    private double score;

    @Column(name = "your_rating_score")
    private double yourRatingScore;

    @Column(name = "other_rating_score")
    private double otherRatingScore;

    @Column(name = "common_games_score")
    private double commonGamesScore;

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getGameId()
    {
        return gameId;
    }

    public void setGameId(long gameId)
    {
        this.gameId = gameId;
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public double getYourRatingScore()
    {
        return yourRatingScore;
    }

    public void setYourRatingScore(double yourRatingScore)
    {
        this.yourRatingScore = yourRatingScore;
    }

    public double getOtherRatingScore()
    {
        return otherRatingScore;
    }

    public void setOtherRatingScore(double otherRatingScore)
    {
        this.otherRatingScore = otherRatingScore;
    }

    public double getCommonGamesScore()
    {
        return commonGamesScore;
    }

    public void setCommonGamesScore(double commonGamesScore)
    {
        this.commonGamesScore = commonGamesScore;
    }
}
