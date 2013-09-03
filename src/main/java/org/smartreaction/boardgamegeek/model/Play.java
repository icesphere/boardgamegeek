package org.smartreaction.boardgamegeek.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Play {
    private long playId;
    private long gameId;
    private Date playDate = new Date();
    private Date dateInput = new Date();
    private int length;
    private String location = "";
    private int quantity = 1;
    private boolean incomplete;
    private boolean noWinStats;
    private String comments;
    private boolean expansion;

    private Map<Long, String> selectedExpansions = new HashMap<Long, String>();

    public Play()
    {
    }

    public Play(Play play, long expansionGameId)
    {
        gameId = expansionGameId;
        playDate = play.getPlayDate();
        dateInput = play.getDateInput();
        length = play.getLength();
        location = play.getLocation();
        quantity = play.getQuantity();
        incomplete = play.isIncomplete();
        noWinStats = play.isNoWinStats();
        comments = play.getComments();
        expansion = true;
    }

    public long getPlayId() {
        return playId;
    }

    public void setPlayId(long playId) {
        this.playId = playId;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public Date getPlayDate() {
        return playDate;
    }

    public void setPlayDate(Date playDate) {
        this.playDate = playDate;
    }

    public Date getDateInput() {
        return dateInput;
    }

    public void setDateInput(Date dateInput) {
        this.dateInput = dateInput;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isIncomplete() {
        return incomplete;
    }

    public void setIncomplete(boolean incomplete) {
        this.incomplete = incomplete;
    }

    public boolean isNoWinStats() {
        return noWinStats;
    }

    public void setNoWinStats(boolean noWinStats) {
        this.noWinStats = noWinStats;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Map<Long, String> getSelectedExpansions()
    {
        return selectedExpansions;
    }

    public void setSelectedExpansions(Map<Long, String> selectedExpansions)
    {
        this.selectedExpansions = selectedExpansions;
    }

    public boolean isExpansion()
    {
        return expansion;
    }

    public void setExpansion(boolean expansion)
    {
        this.expansion = expansion;
    }
}
