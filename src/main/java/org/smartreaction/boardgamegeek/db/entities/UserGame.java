package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_games")
public class UserGame
{
    @Id
    @Column(name = "user_id")
    public long userId;

    @Id
    @Column(name = "game_id")
    public long gameId;

    @Column(name = "collection_id")
    public long collectionId;

    private float rating;

    private boolean owned;

    @Column(name = "previously_owned")
    private boolean previouslyOwned;

    @Column(name = "for_trade")
    private boolean forTrade;

    private boolean want;

    @Column(name = "want_to_play")
    private boolean wantToPlay;

    @Column(name = "want_to_buy")
    private boolean wantToBuy;

    @Column(name = "wish_list")
    private boolean wishList;

    @Column(name = "pre_ordered")
    private boolean preOrdered;

    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;

    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Column(name = "num_plays")
    private int numPlays;

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

    public long getCollectionId()
    {
        return collectionId;
    }

    public void setCollectionId(long collectionId)
    {
        this.collectionId = collectionId;
    }

    public float getRating()
    {
        return rating;
    }

    public void setRating(float rating)
    {
        this.rating = rating;
    }

    public boolean isOwned()
    {
        return owned;
    }

    public void setOwned(boolean owned)
    {
        this.owned = owned;
    }

    public boolean isPreviouslyOwned()
    {
        return previouslyOwned;
    }

    public void setPreviouslyOwned(boolean previouslyOwned)
    {
        this.previouslyOwned = previouslyOwned;
    }

    public boolean isForTrade()
    {
        return forTrade;
    }

    public void setForTrade(boolean forTrade)
    {
        this.forTrade = forTrade;
    }

    public boolean isWant()
    {
        return want;
    }

    public void setWant(boolean want)
    {
        this.want = want;
    }

    public boolean isWantToPlay()
    {
        return wantToPlay;
    }

    public void setWantToPlay(boolean wantToPlay)
    {
        this.wantToPlay = wantToPlay;
    }

    public boolean isWantToBuy()
    {
        return wantToBuy;
    }

    public void setWantToBuy(boolean wantToBuy)
    {
        this.wantToBuy = wantToBuy;
    }

    public boolean isWishList()
    {
        return wishList;
    }

    public void setWishList(boolean wishList)
    {
        this.wishList = wishList;
    }

    public boolean isPreOrdered()
    {
        return preOrdered;
    }

    public void setPreOrdered(boolean preOrdered)
    {
        this.preOrdered = preOrdered;
    }

    public Date getLastModified()
    {
        return lastModified;
    }

    public void setLastModified(Date lastModified)
    {
        this.lastModified = lastModified;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public int getNumPlays()
    {
        return numPlays;
    }

    public void setNumPlays(int numPlays)
    {
        this.numPlays = numPlays;
    }
}
