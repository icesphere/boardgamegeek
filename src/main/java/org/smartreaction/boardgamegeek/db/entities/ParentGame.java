package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "parent_games")
public class ParentGame
{
    @Id
    @Column(name = "game_id")
    public long gameId;

    @Id
    @Column(name = "parent_game_id")
    public long parentGameId;

    public long getGameId()
    {
        return gameId;
    }

    public void setGameId(long gameId)
    {
        this.gameId = gameId;
    }

    public long getParentGameId()
    {
        return parentGameId;
    }

    public void setParentGameId(long parentGameId)
    {
        this.parentGameId = parentGameId;
    }
}
