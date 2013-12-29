package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "recommended_game")
public class RecommendedGame
{
    @Id
    @Column
    private long id;

    @Column(name = "game_id")
    private long gameId;

    @Column(name = "recommended_game_id")
    private long recommendedGameId;

    @Column
    private double score;

    public long getGameId()
    {
        return gameId;
    }

    public void setGameId(long gameId)
    {
        this.gameId = gameId;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getRecommendedGameId()
    {
        return recommendedGameId;
    }

    public void setRecommendedGameId(long recommendedGameId)
    {
        this.recommendedGameId = recommendedGameId;
    }

    public double getScore()
    {
        return score;
    }

    public void setScore(double score)
    {
        this.score = score;
    }
}
