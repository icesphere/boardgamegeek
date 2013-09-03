package org.smartreaction.boardgamegeek.view;

import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.model.Play;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import java.util.Date;

@ManagedBean
@ViewScoped
public class LogPlay
{
    @ManagedProperty(value = "#{boardGame}")
    BoardGame boardGame;

    @ManagedProperty(value="#{boardGameGeek}")
    BoardGameGeek boardGameGeek;

    private boolean playMode;

    @PostConstruct
    public void setup()
    {
        play = new Play();
    }

    private Play play;

    private String playMessage;
    private boolean showPlayMessage;
    private boolean showPlayError;

    public void loadPlay()
    {
        playMode = true;
        showPlayError = false;
        showPlayMessage = false;
        play = new Play();
        play.setGameId(boardGame.getGame().getId());
        play.setDateInput(new Date());
        setSelectedExpansions();
    }

    private void setSelectedExpansions()
    {
        for (Game expansion : boardGame.getGame().getExpansions()) {
            play.getSelectedExpansions().put(expansion.getId(), "false");
        }
    }

    public void submitPlay()
    {
        playMode = false;
        String response = boardGameGeek.logPlay(play);
        if (response.equals("failed")) {
            showPlayError = true;
        }
        else {
            showPlayMessage = true;
            StringBuilder sb = new StringBuilder("You have logged ");
            sb.append(response);
            if (response.equals("1")) {
                sb.append(" play");
            }
            else {
                sb.append(" plays");
            }
            sb.append(" of ");
            sb.append(boardGame.getGame().getName());
            playMessage = sb.toString();
        }
    }

    public void cancelLogPlay()
    {
        playMode = false;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGameGeek(BoardGameGeek boardGameGeek)
    {
        this.boardGameGeek = boardGameGeek;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setBoardGame(BoardGame boardGame)
    {
        this.boardGame = boardGame;
    }

    public Play getPlay()
    {
        return play;
    }

    public void setPlay(Play play)
    {
        this.play = play;
    }

    public String getPlayMessage()
    {
        return playMessage;
    }

    public void setPlayMessage(String playMessage)
    {
        this.playMessage = playMessage;
    }

    public boolean isShowPlayMessage()
    {
        return showPlayMessage;
    }

    public boolean isShowPlayError()
    {
        return showPlayError;
    }

    public boolean isPlayMode()
    {
        return playMode;
    }
}
