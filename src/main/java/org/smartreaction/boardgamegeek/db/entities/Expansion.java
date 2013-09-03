package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "game_expansions")
public class Expansion
{
    @Id
    @Column(name = "game_id")
    public long gameId;

    @Id
    @Column(name = "expansion_id")
    public long expansionId;

    public long getGameId()
    {
        return gameId;
    }

    public void setGameId(long gameId)
    {
        this.gameId = gameId;
    }

    public long getExpansionId()
    {
        return expansionId;
    }

    public void setExpansionId(long expansionId)
    {
        this.expansionId = expansionId;
    }
}
