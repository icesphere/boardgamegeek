package org.smartreaction.boardgamegeek.util;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormat;

public class DateUtil
{
    public static final PeriodType PERIOD_TO_SECONDS = PeriodType.standard().withMillisRemoved();

    public static String getTimePeriodSinceNow(DateTime dateTime)
    {
        Period p = new Period(dateTime, DateTime.now(), PERIOD_TO_SECONDS);
        return PeriodFormat.getDefault().print(p);
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
