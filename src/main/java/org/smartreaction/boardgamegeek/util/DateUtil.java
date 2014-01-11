package org.smartreaction.boardgamegeek.util;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil
{
    public static final PeriodType PERIOD_TO_SECONDS = PeriodType.standard().withMillisRemoved();

    public static String getTimePeriodSinceNow(DateTime dateTime)
    {
        Period p = new Period(dateTime, DateTime.now(), PERIOD_TO_SECONDS);
        return PeriodFormat.getDefault().print(p);
    }

    public static Date getDateFromBggString(String lastUpdatedString) throws ParseException
    {
        lastUpdatedString = DateUtil.fixDateString(lastUpdatedString);
        SimpleDateFormat sdf;
        Date lastUpdated;
        if (lastUpdatedString.startsWith("Today")) {
            lastUpdated = getTodayDate(lastUpdatedString);
        }
        else {
            sdf = new SimpleDateFormat("EEE MMM d, yyyy h:mm a");
            lastUpdated = sdf.parse(lastUpdatedString);
        }
        return lastUpdated;
    }

    private static Date getTodayDate(String dateString) throws ParseException
    {
        dateString = dateString.substring(6);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        DateTime parsedDate = new DateTime(sdf.parse(dateString));
        DateTime now = DateTime.now();
        DateTime todayWithParsedTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), parsedDate.getHourOfDay(), parsedDate.getMinuteOfHour());
        return todayWithParsedTime.toDate();
    }

    public static String fixDateString(String dateString)
    {
        String fixedString = "";
        for (char c : dateString.toCharArray()) {
            if (c < 194) {
                if (c == 160) {
                    fixedString += " ";
                }
                else {
                    fixedString += c;
                }
            }
        }
        return fixedString;
    }
}
