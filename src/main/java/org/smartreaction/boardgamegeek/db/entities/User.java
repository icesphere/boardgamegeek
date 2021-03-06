package org.smartreaction.boardgamegeek.db.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String username;

    @Column(name = "collection_loaded")
    private boolean collectionLoaded;

    @Column
    private int logins;

    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "collection_error")
    private boolean collectionError;

    @Column
    private boolean admin;

    @Column(name = "top_games_loaded")
    private boolean topGamesLoaded;

    @Column(name = "plays_last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date playsLastUpdated;

    @Column(name = "collection_last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date collectionLastUpdated;

    @Column(name = "top_games_last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date topGamesLastUpdated;

    @Column(name = "hide_collection_expansions")
    private boolean hideCollectionExpansions;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public boolean isCollectionLoaded()
    {
        return collectionLoaded;
    }

    public void setCollectionLoaded(boolean collectionLoaded)
    {
        this.collectionLoaded = collectionLoaded;
    }

    public int getLogins()
    {
        return logins;
    }

    public void setLogins(int logins)
    {
        this.logins = logins;
    }

    public Date getLastLogin()
    {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin)
    {
        this.lastLogin = lastLogin;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public boolean isCollectionError()
    {
        return collectionError;
    }

    public void setCollectionError(boolean collectionError)
    {
        this.collectionError = collectionError;
    }

    public boolean isAdmin()
    {
        return admin;
    }

    public void setAdmin(boolean admin)
    {
        this.admin = admin;
    }

    public boolean isTopGamesLoaded()
    {
        return topGamesLoaded;
    }

    public void setTopGamesLoaded(boolean topGamesLoaded)
    {
        this.topGamesLoaded = topGamesLoaded;
    }

    public Date getPlaysLastUpdated()
    {
        return playsLastUpdated;
    }

    public void setPlaysLastUpdated(Date playsLastUpdated)
    {
        this.playsLastUpdated = playsLastUpdated;
    }

    public Date getCollectionLastUpdated()
    {
        return collectionLastUpdated;
    }

    public void setCollectionLastUpdated(Date collectionLastUpdated)
    {
        this.collectionLastUpdated = collectionLastUpdated;
    }

    public Date getTopGamesLastUpdated() {
        return topGamesLastUpdated;
    }

    public void setTopGamesLastUpdated(Date topGamesLastUpdated) {
        this.topGamesLastUpdated = topGamesLastUpdated;
    }

    public boolean isHideCollectionExpansions()
    {
        return hideCollectionExpansions;
    }

    public void setHideCollectionExpansions(boolean hideCollectionExpansions)
    {
        this.hideCollectionExpansions = hideCollectionExpansions;
    }
}
