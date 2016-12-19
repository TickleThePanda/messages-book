package uk.co.ticklethepanda.messagesbook.messagesdatastructure;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.TimeZone;

public class DateAndTime {
    public static String addLeadingZeros(final int i) {
        final int smallestTwoDigitNumber = 10;
        if (i < smallestTwoDigitNumber) {
            return "0" + i;
        }
        return String.valueOf(i);
    }

    public static boolean dayIsAfterLast(final long thisTimeInMillis,
            final long lastTimeInMillis) {

        final Calendar thisDate =
                Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        thisDate.setTimeInMillis(thisTimeInMillis);

        final Calendar lastDate =
                Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        lastDate.setTimeInMillis(lastTimeInMillis);

        thisDate.setTimeZone(TimeZone.getTimeZone("GMT"));
        lastDate.setTimeZone(TimeZone.getTimeZone("GMT"));

        if (thisDate.get(Calendar.YEAR) > lastDate.get(Calendar.YEAR)) {
            return true;
        }
        if (thisDate.get(Calendar.MONTH) > lastDate.get(Calendar.MONTH)) {
            return true;
        }
        if (thisDate.get(Calendar.DAY_OF_MONTH) > lastDate
                .get(Calendar.DAY_OF_MONTH)) {
            return true;
        }

        return false;
    }

    private static String dayOfMonthTitle(final int n) {
        final int teenthLower = 11;
        final int teenthUpper = 13;
        if (n >= teenthLower && n <= teenthUpper) {
            return "th";
        }
        final int modDivisor = 10;
        final int caseST = 1;
        final int caseND = 2;
        final int caseRD = 3;
        switch (n % modDivisor) {
        case caseST:
            return "st";
        case caseND:
            return "nd";
        case caseRD:
            return "rd";
        default:
            return "th";
        }
    }

    private static String monthName(final int n) {
        final DateFormatSymbols dfs = new DateFormatSymbols();
        final String[] months = dfs.getMonths();
        return months[n];
    }

    private static String timeSuffix(final int i) {
        final int firstHourOfAfternoon = 12;
        if (i < firstHourOfAfternoon) {
            return "am";
        } else {
            return "pm";
        }
    }

    private final long timeInMillis;

    public DateAndTime(final long pTimeInMillis) {
        this.timeInMillis = pTimeInMillis;
    }

    public final String getDateAsString() {
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(this.timeInMillis);
        final Integer dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        final Integer month = cal.get(Calendar.MONTH);
        return dayOfMonth.toString() + dayOfMonthTitle(dayOfMonth) + " "
                + monthName(month) + " " + cal.get(Calendar.YEAR);
    }

    private int getHourIn12(final int hour) {
        final int firstHourOfAfternoon = 12;
        int hourInTwelve = hour;
        if (hourInTwelve > firstHourOfAfternoon) {
            hourInTwelve -= firstHourOfAfternoon;
        }
        return hourInTwelve;
    }

    public final String getTimeAsString() {
        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(this.timeInMillis);
        Integer hour = cal.get(Calendar.HOUR_OF_DAY);
        final String suffix = timeSuffix(hour);
        hour = this.getHourIn12(hour);
        final Integer minute = cal.get(Calendar.MINUTE);
        return hour.toString() + ":" + addLeadingZeros(minute) + suffix;
    }

    public final long getTimestamp() {
        return this.timeInMillis;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (object instanceof DateAndTime) {
            DateAndTime dt = (DateAndTime) object;
            if (this.getTimestamp() == dt.getTimestamp())
                return true;
        }
        return false;
    }
}
