package org.smartreaction.boardgamegeek.view;

import org.joda.time.DateTime;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.business.BoardGameUtil;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.UserPlay;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean
@ViewScoped
public class SingleGamePlaysGraph {
    @ManagedProperty(value = "#{userSession}")
    UserSession userSession;

    @EJB
    BoardGameUtil boardGameUtil;

    @EJB
    BoardGameCache boardGameCache;

    public static final String SCOPE_MONTHS = "months";
    public static final String SCOPE_QUARTERS = "quarters";
    public static final String SCOPE_YEARS = "years";

    private String scope;

    private CartesianChartModel chartPlays;

    private List<String> labels;

    private List<UserPlay> plays;

    private int playTime = 3;
    private DateTime startPlayDate;
    private DateTime endPlayDate;

    private Game game;

    private boolean playsUpdated = false;

    public void loadChart(Game game) throws MalformedURLException, JAXBException, ParseException
    {
        if (!playsUpdated) {
            boardGameUtil.updateUserPlays(userSession.getUser());
            playsUpdated = true;
        }

        this.game = game;
        loadPlays();
        setScope();
        loadLabels();
        addGamePlaysToChart();
    }

    private void loadPlays() throws JAXBException, MalformedURLException, ParseException {
        plays = getPlaysForGame(game.getId());
        if (!plays.isEmpty()) {
            Collections.reverse(plays);
        }
    }

    private List<UserPlay> getPlaysForGame(long gameId) throws JAXBException, MalformedURLException
    {
        if (playTime > 0) {
            if (playTime == 6) {
                startPlayDate = new DateTime().minusMonths(6);
            }
            else {
                startPlayDate = new DateTime().minusYears(playTime);
            }
            endPlayDate = new DateTime();
        }

        return boardGameUtil.getUserPlaysForGameByDate(userSession.getUser(), gameId, startPlayDate.toDate(), endPlayDate.toDate());
    }

    private void setScope()
    {
        if (playTime == 6) {
            scope = SCOPE_MONTHS;
        }
        else if (playTime == 1) {
            scope = SCOPE_QUARTERS;
        }
        else {
            scope = SCOPE_YEARS;
        }
    }

    private void loadLabels() {
        labels = new ArrayList<>();

        String firstLabel = getLabel(startPlayDate);
        String lastLabel = getLabel(endPlayDate);

        labels.add(firstLabel);
        if (!firstLabel.equals(lastLabel)) {
            String nextLabel = getNextLabel(startPlayDate);
            DateTime currentLabelDate = startPlayDate;
            while (!nextLabel.equals(lastLabel)) {
                labels.add(nextLabel);
                nextLabel = getNextLabel(currentLabelDate);
                currentLabelDate = getNextLabelDate(currentLabelDate);
            }
            labels.add(lastLabel);
        }
    }

    private DateTime getNextLabelDate(DateTime date) {
        if (scope.equals(SCOPE_MONTHS)) {
            return date.plusMonths(1);
        } else if (scope.equals(SCOPE_QUARTERS)) {
            return date.plusMonths(3);
        } else {
            return date.plusYears(1);
        }
    }

    private String getNextLabel(DateTime date) {
        if (scope.equals(SCOPE_MONTHS)) {
            DateTime nextMonth = date.plusMonths(1);
            return getLabel(nextMonth);
        } else if (scope.equals(SCOPE_QUARTERS)) {
            DateTime nextMonth = date.plusMonths(3);
            return getLabel(nextMonth);
        } else {
            return String.valueOf(date.getYear() + 1);
        }
    }

    private String getLabel(DateTime date) {
        if (scope.equals(SCOPE_MONTHS)) {
            SimpleDateFormat dateLabelFormat = new SimpleDateFormat("MMM yyyy");
            return dateLabelFormat.format(date.toDate());
        } else if (scope.equals(SCOPE_QUARTERS)) {
            String quarter = getQuarter(date);
            return quarter + " " + date.getYear();
        } else {
            return String.valueOf(date.getYear());
        }
    }

    private String getQuarter(DateTime date) {
        String quarter;
        int month = date.getMonthOfYear();
        if (month < 4) {
            quarter = "Q1";
        } else if (month < 7) {
            quarter = "Q2";
        } else if (month < 10) {
            quarter = "Q3";
        } else {
            quarter = "Q4";
        }
        return quarter;
    }

    private void addGamePlaysToChart() throws ParseException {
        chartPlays = new CartesianChartModel();

        ChartSeries gameSeries = new ChartSeries();

        gameSeries.setLabel(game.getName());

        int playQuantity = 0;
        String currentLabel = labels.get(0);
        String previousLabel = "";
        int labelIndex = 0;

        for (UserPlay play : plays) {
            String label = getLabel(new DateTime(play.getPlayDate()));
            if (labelChanged(previousLabel, label)) {
                addValues(gameSeries, playQuantity, previousLabel);
            }
            while (!label.equals(currentLabel)) {
                if (!currentLabel.equals(previousLabel)) {
                    gameSeries.set(currentLabel, 0);
                }
                labelIndex++;
                currentLabel = labels.get(labelIndex);
            }
            if (!currentLabel.equals(previousLabel)) {
                previousLabel = label;
                playQuantity = play.getQuantity();
            } else {
                playQuantity += play.getQuantity();
            }
        }

        addValues(gameSeries, playQuantity, currentLabel);

        String lastLabel = labels.get(labels.size() - 1);
        while (!currentLabel.equals(lastLabel)) {
            labelIndex++;
            currentLabel = labels.get(labelIndex);
            gameSeries.set(currentLabel, 0);
        }

        chartPlays.addSeries(gameSeries);
    }

    private boolean labelChanged(String previousLabel, String currentLabel) {
        return !previousLabel.equals("") && !previousLabel.equals(currentLabel);
    }

    private void addValues(ChartSeries series, int quantity, String label) {
        if (!label.equals("")) {
            series.set(label, quantity);
        }
    }

    public CartesianChartModel getChartPlays() {
        return chartPlays;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }

    public int getPlayTime()
    {
        return playTime;
    }

    public void setPlayTime(int playTime)
    {
        this.playTime = playTime;
    }
}
