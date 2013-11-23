package org.smartreaction.boardgamegeek.db.entities;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.smartreaction.boardgamegeek.BoardGameGeekConstants;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "games")
public class Game
{             
    @Id
    private long id;

    private String name;

    private String description;

    @Column(name = "year_published")
    private int yearPublished;

    private String image;

    @Column(name = "thumbnail_image")
    private String thumbnailImage;

    @Column(name = "min_players")
    private int minPlayers;

    @Column(name = "max_players")
    private int maxPlayers;

    @Column(name = "playing_time")
    private int playingTime;

    @Column(name = "num_owned")
    private int numOwned;

    @Column(name = "users_rated")
    private int usersRated;

    @Column(name = "average_rating")
    private float averageRating;

    @Column(name = "bayes_average_rating")
    private float bayesAverageRating;

    @Column(name = "last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Column(name = "last_played")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastPlayed;

    @Column(name = "comments_last_updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commentsLastUpdated;

    @Column
    private int rank;

    @Column
    private int ratings;

    @Column
    private String categories;

    @Column
    private String mechanics;

    @Column
    private String designers;

    @Column
    private String publishers;

    @Transient
    private List<Game> expansions;

    @Transient
    private List<Game> parentGames;

    @Transient
    private double recommendationScore;

    @Transient
    private double yourRatingScore;

    @Transient
    private double otherRatingScore;

    @Transient
    private double commonGamesScore;

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getEscapedDescription()
    {
        return StringEscapeUtils.unescapeHtml(description);
    }

    public int getYearPublished()
    {
        return yearPublished;
    }

    public void setYearPublished(int yearPublished)
    {
        this.yearPublished = yearPublished;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getThumbnailImage()
    {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage)
    {
        this.thumbnailImage = thumbnailImage;
    }

    public String getSmallThumbnailImage()
    {
        if (!StringUtils.isEmpty(thumbnailImage)) {
            return thumbnailImage.replace("_t.", "_mt.");
        }
        return thumbnailImage;
    }

    public int getMinPlayers()
    {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers)
    {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers)
    {
        this.maxPlayers = maxPlayers;
    }

    public String getNumberOfPlayersString()
    {
        if (minPlayers > 0 && maxPlayers > 0 && maxPlayers > minPlayers) {
            return minPlayers + " - " + maxPlayers;
        }
        else if (minPlayers > 0) {
            return String.valueOf(minPlayers);
        }
        else if (maxPlayers > 0) {
            return String.valueOf(maxPlayers);
        }
        else {
            return "N/A";
        }
    }

    public int getPlayingTime()
    {
        return playingTime;
    }

    public void setPlayingTime(int playingTime)
    {
        this.playingTime = playingTime;
    }

    public String getPlayingTimeString()
    {
        if (playingTime > 0) {
            return playingTime + " minutes";
        }
        else {
            return "N/A";
        }
    }

    public int getNumOwned()
    {
        return numOwned;
    }

    public void setNumOwned(int numOwned)
    {
        this.numOwned = numOwned;
    }

    public int getUsersRated()
    {
        return usersRated;
    }

    public void setUsersRated(int usersRated)
    {
        this.usersRated = usersRated;
    }

    public float getAverageRating()
    {
        return averageRating;
    }

    public void setAverageRating(float averageRating)
    {
        this.averageRating = averageRating;
    }

    public float getBayesAverageRating()
    {
        return bayesAverageRating;
    }

    public void setBayesAverageRating(float bayesAverageRating)
    {
        this.bayesAverageRating = bayesAverageRating;
    }

    public Date getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    public boolean hasExpansions()
    {
        return expansions != null && !expansions.isEmpty();
    }

    public List<Game> getExpansions()
    {
        return expansions;
    }

    public void setExpansions(List<Game> expansions)
    {
        this.expansions = expansions;
    }

    public String getGameLink()
    {
        return BoardGameGeekConstants.BBG_WEBSITE + "/boardgame/"+id;
    }

    public String getForumsLink()
    {
        return BoardGameGeekConstants.BBG_WEBSITE + "/forums/thing/"+id;
    }

    public int getRank()
    {
        return rank;
    }

    public int getSortRank()
    {
        if (rank > 0) {
            return rank;
        }
        else {
            return Integer.MAX_VALUE;
        }
    }

    public void setRank(int rank)
    {
        this.rank = rank;
    }

    public int getRatings()
    {
        return ratings;
    }

    public void setRatings(int ratings)
    {
        this.ratings = ratings;
    }

    public List<Game> getParentGames()
    {
        return parentGames;
    }

    public void setParentGames(List<Game> parentGames)
    {
        this.parentGames = parentGames;
    }

    public boolean isExpansion()
    {
        return parentGames != null && !parentGames.isEmpty();
    }

    public String getCategories()
    {
        return categories;
    }

    public void setCategories(String categories)
    {
        this.categories = categories;
    }

    public String getMechanics()
    {
        return mechanics;
    }

    public void setMechanics(String mechanics)
    {
        this.mechanics = mechanics;
    }

    public String getDesigners() {
        return designers;
    }

    public void setDesigners(String designers) {
        this.designers = designers;
    }

    public String getPublishers() {
        return publishers;
    }

    public void setPublishers(String publishers) {
        this.publishers = publishers;
    }

    public double getRecommendationScore()
    {
        return recommendationScore;
    }

    public void setRecommendationScore(double recommendationScore)
    {
        this.recommendationScore = recommendationScore;
    }

    public double getYourRatingScore()
    {
        return yourRatingScore;
    }

    public int getYourRatingPercentage()
    {
        return (int) Math.floor(yourRatingScore / recommendationScore * 100);
    }

    public void setYourRatingScore(double yourRatingScore)
    {
        this.yourRatingScore = yourRatingScore;
    }

    public double getOtherRatingScore()
    {
        return otherRatingScore;
    }

    public int getOtherRatingPercentage()
    {
        return (int) Math.floor(otherRatingScore / recommendationScore * 100);
    }

    public void setOtherRatingScore(double otherRatingScore)
    {
        this.otherRatingScore = otherRatingScore;
    }

    public double getCommonGamesScore()
    {
        return commonGamesScore;
    }

    public int getCommonGamesRatingPercentage()
    {
        return (int) Math.floor(commonGamesScore / recommendationScore * 100);
    }

    public void setCommonGamesScore(double commonGamesScore)
    {
        this.commonGamesScore = commonGamesScore;
    }

    public Date getLastPlayed()
    {
        return lastPlayed;
    }

    public void setLastPlayed(Date lastPlayed)
    {
        this.lastPlayed = lastPlayed;
    }

    public Date getCommentsLastUpdated()
    {
        return commentsLastUpdated;
    }

    public void setCommentsLastUpdated(Date commentsLastUpdated)
    {
        this.commentsLastUpdated = commentsLastUpdated;
    }
}
