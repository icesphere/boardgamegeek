package org.smartreaction.boardgamegeek.model;

public class RecommendedGameScore
{
    private long gameId;

    private double score;

    private double yourRatingScore;

    private double otherRatingScore;

    private double commonGamesScore;

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
