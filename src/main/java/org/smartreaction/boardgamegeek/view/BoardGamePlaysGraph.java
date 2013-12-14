package org.smartreaction.boardgamegeek.view;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.smartreaction.boardgamegeek.business.BoardGameCache;
import org.smartreaction.boardgamegeek.business.BoardGameUtil;
import org.smartreaction.boardgamegeek.comparators.GameRatingComparator;
import org.smartreaction.boardgamegeek.db.entities.Game;
import org.smartreaction.boardgamegeek.db.entities.UserGame;
import org.smartreaction.boardgamegeek.db.entities.UserPlay;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@ManagedBean
@ViewScoped
public class BoardGamePlaysGraph
{
    @ManagedProperty(value="#{userSession}")
    UserSession userSession;

    @EJB
    BoardGameUtil boardGameUtil;

    @EJB
    BoardGameCache boardGameCache;

    public static final String SCOPE_MONTHS = "months";
    public static final String SCOPE_QUARTERS = "quarters";
    public static final String SCOPE_YEARS = "years";

    private String scope;

    private boolean chartLoaded;

    private CartesianChartModel chartPlays;
    private Map<Long, List<UserPlay>> playsByGameMap;
    private int maxQuantity;
    private int minPlaysForGame = 3;
    private float minGameRating = 7;
    private float maxGameRating = 10;
    private int maxGamesToShow = 10;

    private Date startPlayDate;
    private Date endPlayDate;

    DateTime firstPlayDate;
    DateTime lastPlayDate;

    private List<String> labels;

    private boolean includeExpansions;

    @PostConstruct
    public void setup()
    {
        startPlayDate = new DateTime().minusYears(1).toDate();
        endPlayDate = new Date();
        if (userSession.getGames() == null) {
            userSession.syncGames();
        }
        boardGameUtil.updateUserPlays(userSession.getUser());
    }

    public void loadChart() throws MalformedURLException, JAXBException, ParseException
    {
        firstPlayDate = null;
        lastPlayDate = null;

        loadPlays();
        if (!playsByGameMap.isEmpty()) {
            setScope();
            loadLabels();
            addGamePlaysToChart();
        }
        chartLoaded = true;
    }

    public void refreshChart(){
        chartLoaded = false;
    }

    private void loadPlays() throws JAXBException, MalformedURLException, ParseException {

        playsByGameMap = new LinkedHashMap<>();

        List<UserGame> gamesByRating = new ArrayList<>();
        for (UserGame userGame : userSession.getUserGamesMap().values()) {
            gamesByRating.add(userGame);
        }
        Collections.sort(gamesByRating, new GameRatingComparator());
        Collections.reverse(gamesByRating);

        for (UserGame game : gamesByRating) {
            if (includeGame(game)) {
                List<UserPlay> playsForGame = getPlaysForGame(game.getGameId());
                if (!playsForGame.isEmpty()) {
                    Collections.reverse(playsForGame);

                    for (UserPlay play : playsForGame) {
                        play.setGame(boardGameCache.getGame(play.getGameId()));
                    }

                    playsByGameMap.put(game.getGameId(), playsForGame);
                    checkFirstAndLastPlayDate(playsForGame);
                }
            }
            if (playsByGameMap.size() == maxGamesToShow) {
                return;
            }
        }
    }

    private boolean includeGame(UserGame userGame)
    {
        boolean hasEnoughPlays = userGame.getNumPlays() >= minPlaysForGame;
        boolean insideGameRatingRange = userGame.getRating() >= minGameRating && userGame.getRating() <= maxGameRating;
        return hasEnoughPlays && insideGameRatingRange && (includeExpansions || !isExpansion(userGame.getGameId()));
    }

    private boolean isExpansion(long gameId)
    {
        for (Game game : userSession.getGames()) {
            if (game.getId() == gameId) {
                return game.isExpansion();
            }
        }
        return false;
    }

    private void checkFirstAndLastPlayDate(List<UserPlay> playsForGame) throws ParseException {
        DateTime gameFirstPlayDate = new DateTime(playsForGame.get(0).getPlayDate());
        if (firstPlayDate == null || gameFirstPlayDate.isBefore(firstPlayDate)) {
            firstPlayDate = gameFirstPlayDate;
        }
        DateTime gameLastPlayDate = new DateTime(playsForGame.get(playsForGame.size() - 1).getPlayDate());
        if (lastPlayDate == null || gameLastPlayDate.isAfter(lastPlayDate)) {
            lastPlayDate = gameLastPlayDate;
        }
    }

    private List<UserPlay> getPlaysForGame(long gameId) throws JAXBException, MalformedURLException
    {
        return boardGameUtil.getUserPlaysForGameByDate(userSession.getUser(), gameId, startPlayDate, endPlayDate);
    }

    private void setScope()
    {
        int numGames = playsByGameMap.size();
        int monthsBetween = Months.monthsBetween(firstPlayDate, lastPlayDate).getMonths();

        if (monthsBetween < 4 || (monthsBetween < 6 && numGames < 10)) {
            scope = SCOPE_MONTHS;
        }
        else if (monthsBetween < 12 || (monthsBetween < 18 && numGames < 10)) {
            scope = SCOPE_QUARTERS;
        }
        else {
            scope = SCOPE_YEARS;
        }
    }

    private void loadLabels()
    {
        labels = new ArrayList<>();

        String firstLabel = getLabel(firstPlayDate);
        String lastLabel = getLabel(lastPlayDate);

        labels.add(firstLabel);
        if (!firstLabel.equals(lastLabel)) {
            String nextLabel = getNextLabel(firstPlayDate);
            DateTime currentLabelDate = firstPlayDate;
            while (!nextLabel.equals(lastLabel)) {
                labels.add(nextLabel);
                nextLabel = getNextLabel(currentLabelDate);
                currentLabelDate = getNextLabelDate(currentLabelDate);
            }
            labels.add(lastLabel);
        }
    }

    private DateTime getNextLabelDate(DateTime date)
    {
        if (scope.equals(SCOPE_MONTHS)) {
            return date.plusMonths(1);
        }
        else if (scope.equals(SCOPE_QUARTERS)) {
            return date.plusMonths(3);
        }
        else {
            return date.plusYears(1);
        }
    }

    private String getNextLabel(DateTime date)
    {
        if (scope.equals(SCOPE_MONTHS)) {
            DateTime nextMonth = date.plusMonths(1);
            return getLabel(nextMonth);
        }
        else if (scope.equals(SCOPE_QUARTERS)) {
            DateTime nextMonth = date.plusMonths(3);
            return getLabel(nextMonth);
        }
        else {
            return String.valueOf(date.getYear() + 1);
        }
    }

    private String getLabel(DateTime date)
    {
        if (scope.equals(SCOPE_MONTHS)) {
            SimpleDateFormat dateLabelFormat = new SimpleDateFormat("MMM yyyy");
            return dateLabelFormat.format(date.toDate());
        }
        else if (scope.equals(SCOPE_QUARTERS)) {
            String quarter = getQuarter(date);
            return quarter + " " + date.getYear();
        }
        else {
            return String.valueOf(date.getYear());
        }
    }

    private String getQuarter(DateTime date)
    {
        String quarter;
        int month = date.getMonthOfYear();
        if (month < 4) {
            quarter = "Q1";
        }
        else if (month < 7) {
            quarter = "Q2";
        }
        else if (month < 10) {
            quarter = "Q3";
        }
        else {
            quarter = "Q4";
        }
        return quarter;
    }

    private void addGamePlaysToChart() throws ParseException
    {
        chartPlays = new CartesianChartModel();
        for (Long gameId : playsByGameMap.keySet()) {
            List<UserPlay> playsForGame = playsByGameMap.get(gameId);
            ChartSeries gameSeries = new ChartSeries();
            gameSeries.setLabel(playsForGame.get(0).getGame().getName());
            int playQuantity = 0;
            String currentLabel = labels.get(0);
            String previousLabel = "";
            int labelIndex = 0;
            for (UserPlay play : playsForGame) {
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
                }
                else {
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
    }

    private boolean labelChanged(String previousLabel, String currentLabel)
    {
        return !previousLabel.equals("") && !previousLabel.equals(currentLabel);
    }

    private void addValues(ChartSeries series, int quantity, String label)
    {
        if (!label.equals("")) {
            series.set(label, quantity);
            if (quantity > maxQuantity) {
                maxQuantity = quantity;
            }
        }
    }

    public CartesianChartModel getChartPlays()
    {
        return chartPlays;
    }

    public int getMinPlaysForGame()
    {
        return minPlaysForGame;
    }

    public void setMinPlaysForGame(int minPlaysForGame)
    {
        this.minPlaysForGame = minPlaysForGame;
    }

    public float getMinGameRating()
    {
        return minGameRating;
    }

    public void setMinGameRating(float minGameRating)
    {
        this.minGameRating = minGameRating;
    }

    public float getMaxGameRating()
    {
        return maxGameRating;
    }

    public void setMaxGameRating(float maxGameRating)
    {
        this.maxGameRating = maxGameRating;
    }

    public int getMaxGamesToShow()
    {
        return maxGamesToShow;
    }

    public void setMaxGamesToShow(int maxGamesToShow)
    {
        this.maxGamesToShow = maxGamesToShow;
    }

    public boolean hasPlays()
    {
        return !playsByGameMap.isEmpty();
    }

    public boolean isChartLoaded()
    {
        return chartLoaded;
    }

    public Date getStartPlayDate()
    {
        return startPlayDate;
    }

    public void setStartPlayDate(Date startPlayDate)
    {
        this.startPlayDate = startPlayDate;
    }

    public Date getEndPlayDate()
    {
        return endPlayDate;
    }

    public void setEndPlayDate(Date endPlayDate)
    {
        this.endPlayDate = endPlayDate;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession)
    {
        this.userSession = userSession;
    }

    public boolean isIncludeExpansions()
    {
        return includeExpansions;
    }

    public void setIncludeExpansions(boolean includeExpansions)
    {
        this.includeExpansions = includeExpansions;
    }
}
