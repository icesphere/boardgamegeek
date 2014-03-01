package org.smartreaction.boardgamegeek.model;

import org.smartreaction.boardgamegeek.db.entities.Game;

public class WhatToPlayRecommendation
{
    private Game game;

    private double score;

    private double ratingScore;

    private double lastPlayedScore;

    public Game getGame()
    {
        return game;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
    }

    public double getRatingScore()
    {
        return ratingScore;
    }

    public void setRatingScore(double ratingScore)
    {
        this.ratingScore = ratingScore;
    }

    public double getLastPlayedScore()
    {
        return lastPlayedScore;
    }

    public void setLastPlayedScore(double lastPlayedScore)
    {
        this.lastPlayedScore = lastPlayedScore;
    }

    public int getRatingScorePercentage()
    {
        return (int) Math.floor(ratingScore / score * 100);
    }

    public int getLastPlayedScorePercentage()
    {
        return (int) Math.floor(lastPlayedScore / score * 100);
    }
}
