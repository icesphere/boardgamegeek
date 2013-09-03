package org.smartreaction.boardgamegeek.services;

import org.joda.time.DateTime;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BoardGameGeekServiceTest
{
    @Test
    public void testDateFormat() throws ParseException, UnsupportedEncodingException
    {
        String dateString = "Tue Nov 13, 2012 11:15 pm";
        String fixedString = fixString(dateString);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM d, yyyy h:mm a");
        Date parse = sdf.parse(fixedString);
        assertNotNull(parse);
    }

    private String fixString(String dateString)
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

    @Test
    public void testGeekListDate() throws ParseException
    {
        String testDate = "Fri, 7 Aug 2010 02:22:04 +0000";
        testDate = testDate.substring(0, testDate.indexOf(" +"));
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        Date parse = sdf.parse(testDate);
        assertNotNull(parse);
    }

    @Test
    public void testThreadDate() throws ParseException
    {
        String testDate = "2012-11-08T20:56:40-06:00";
        testDate = testDate.substring(0, testDate.lastIndexOf("-"));
        testDate = testDate.replace("T", " ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse = sdf.parse(testDate);
        assertNotNull(parse);
    }

    @Test
    public void testTimeOnlyDate() throws ParseException
    {
        String testDate = "4:59 pm";
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        Date parse = sdf.parse(testDate);
        assertNotNull(parse);

        DateTime parsedDate = new DateTime(parse);

        DateTime now = DateTime.now();

        DateTime todayWithParsedTime = new DateTime(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth(), parsedDate.getHourOfDay(), parsedDate.getMinuteOfHour());

        assertEquals(now.getYear(), todayWithParsedTime.getYear());
        assertEquals(now.getMonthOfYear(), todayWithParsedTime.getMonthOfYear());
        assertEquals(now.getDayOfMonth(), todayWithParsedTime.getDayOfMonth());
    }
}
