package org.smartreaction.boardgamegeek.view;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.smartreaction.boardgamegeek.xml.plays.Play;
import org.smartreaction.boardgamegeek.xml.plays.Plays;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.net.MalformedURLException;
import java.net.URL;
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

    public static final String SCOPE_MONTHS = "months";
    public static final String SCOPE_QUARTERS = "quarters";
    public static final String SCOPE_YEARS = "years";

    private String scope;

    private boolean chartLoaded;

    private CartesianChartModel chartPlays;

    DateTime firstPlayDate;
    DateTime lastPlayDate;

    private List<String> labels;

    private List<Play> plays;

    public void loadChart(long gameId) throws MalformedURLException, JAXBException, ParseException {
        firstPlayDate = null;
        lastPlayDate = null;

        loadPlays(gameId);
        if (!plays.isEmpty()) {
            setScope();
            loadLabels();
            addGamePlaysToChart();
        }
        chartLoaded = true;
    }

    private void loadPlays(long gameId) throws JAXBException, MalformedURLException, ParseException {
        plays = getPlaysForGame(gameId);
        if (!plays.isEmpty()) {
            Collections.reverse(plays);
            checkFirstAndLastPlayDate();
        }
    }

    private void checkFirstAndLastPlayDate() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateTime gameFirstPlayDate = new DateTime(sdf.parse(plays.get(0).getDate()));
        if (firstPlayDate == null || gameFirstPlayDate.isBefore(firstPlayDate)) {
            firstPlayDate = gameFirstPlayDate;
        }
        DateTime gameLastPlayDate = new DateTime(sdf.parse(plays.get(plays.size() - 1).getDate()));
        if (lastPlayDate == null || gameLastPlayDate.isAfter(lastPlayDate)) {
            lastPlayDate = gameLastPlayDate;
        }
    }

    private List<Play> getPlaysForGame(long gameId) throws JAXBException, MalformedURLException {
        JAXBContext jc = JAXBContext.newInstance("org.smartreaction.boardgamegeek.xml.plays");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        StringBuilder sb = new StringBuilder("http://boardgamegeek.com/xmlapi2/plays?username=");
        sb.append(userSession.getUsername());
        sb.append("&id=").append(gameId);
        URL url = new URL(sb.toString());
        return ((Plays) unmarshaller.unmarshal(url)).getPlay();
    }

    private void setScope() {
        int numGames = plays.size();
        int monthsBetween = Months.monthsBetween(firstPlayDate, lastPlayDate).getMonths();

        if (monthsBetween < 4 || (monthsBetween < 6 && numGames < 10)) {
            scope = SCOPE_MONTHS;
        } else if (monthsBetween < 12 || (monthsBetween < 18 && numGames < 10)) {
            scope = SCOPE_QUARTERS;
        } else {
            scope = SCOPE_YEARS;
        }
    }

    private void loadLabels() {
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

        String gameName = StringEscapeUtils.escapeJavaScript(plays.get(0).getItem().getName());
        gameSeries.setLabel(gameName);

        int playQuantity = 0;
        String currentLabel = labels.get(0);
        String previousLabel = "";
        int labelIndex = 0;

        for (Play play : plays) {
            String label = getLabel(new DateTime(play.getDate()));
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
                playQuantity = NumberUtils.toInt(play.getQuantity());
            } else {
                playQuantity += NumberUtils.toInt(play.getQuantity());
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

    public boolean hasPlays() {
        return !plays.isEmpty();
    }

    public boolean isChartLoaded() {
        return chartLoaded;
    }

    @SuppressWarnings("UnusedDeclaration")
    public void setUserSession(UserSession userSession) {
        this.userSession = userSession;
    }
}
