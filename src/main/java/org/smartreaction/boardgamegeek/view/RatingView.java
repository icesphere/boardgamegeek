package org.smartreaction.boardgamegeek.view;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;

@ManagedBean
@ViewScoped
public class RatingView implements Serializable
{
    @ManagedProperty(value="#{boardGameGeek}")
    BoardGameGeek boardGameGeek;

    private long gameId;

    private float rating;

    private String saveRatingMessage;

    private boolean success;
    private boolean failure;

    public void showGameRating(long gameId, float rating)
    {
        this.gameId = gameId;
        this.rating = rating;
        success = false;
        failure = false;
    }

    public void saveRating()
    {
        String message = boardGameGeek.changeGameRating(gameId, rating);
        if (message.equals("success")) {
            saveRatingMessage = "Successfully saved rating";
        }
        else {
            saveRatingMessage = "Error saving rating";
        }
        this.gameId = 0;
        this.rating = 0;
    }

    public void cancelChangeRating()
    {
        success = false;
        failure = false;
        this.gameId = 0;
        this.rating = 0;
    }

    public float getRating()
    {
        return rating;
    }

    public void setRating(float rating)
    {
        this.rating = rating;
    }

    public String getSaveRatingMessage()
    {
        return saveRatingMessage;
    }

    public long getGameId()
    {
        return gameId;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public boolean isFailure()
    {
        return failure;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGameGeek(BoardGameGeek boardGameGeek)
    {
        this.boardGameGeek = boardGameGeek;
    }
}
