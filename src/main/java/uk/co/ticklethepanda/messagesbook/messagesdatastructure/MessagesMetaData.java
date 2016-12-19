package uk.co.ticklethepanda.messagesbook.messagesdatastructure;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class MessagesMetaData {

    private long pandaTotalLength;
    private long jessTotalLength;

    private long pandaTotalMessages;
    private long jessTotalMessages;

    private final int[] days;

    private final int[] months;

    private final int[] hours;

    // private final HashMap<Integer, Integer> monthYear;

    public MessagesMetaData() {
        days = new int[7];
        months = new int[12];
        hours = new int[24];

        // monthYear = new HashMap<Integer, Integer>(+ "\n");

        pandaTotalLength = 0;
        jessTotalLength = 0;

        pandaTotalMessages = 0;
        jessTotalMessages = 0;

    }

    public void analyseMessageList(MessageList ml) {
        for (int i = 0; i < ml.length(); i++) {
            analyseMessage(ml.getMessage(i));
        }
    }

    public void analyseMessage(MessageData md) {
        switch (md.getPerson()) {
        case PANDA:
            pandaTotalMessages++;
            pandaTotalLength += md.getMessage().length();
            break;
        case JESS:
            jessTotalMessages++;
            jessTotalLength += md.getMessage().length();
            break;
        default:
            break;
        }

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(md.getDateAndTime().getTimestamp());

        days[c.get(Calendar.DAY_OF_WEEK) - 1]++;
        months[c.get(Calendar.MONTH)]++;
        hours[c.get(Calendar.HOUR_OF_DAY)]++;
    }

    public void printOutput() throws IOException {

        File f = new File("output\\metadata.csv");
        f.delete();
        f.createNewFile();
        FileWriter fw = new FileWriter(f);

        // DAYS
        fw.write("Mon\t" + days[Calendar.MONDAY - 1] + "\n");
        fw.write("Tue\t" + days[Calendar.TUESDAY - 1] + "\n");
        fw.write("Wed\t" + days[Calendar.WEDNESDAY - 1] + "\n");
        fw.write("Thu\t" + days[Calendar.THURSDAY - 1] + "\n");
        fw.write("Fri\t" + days[Calendar.FRIDAY - 1] + "\n");
        fw.write("Sat\t" + days[Calendar.SATURDAY - 1] + "\n");
        fw.write("Sun\t" + days[Calendar.SUNDAY - 1] + "\n");

        fw.write("" + "\n");

        // MONTHS
        fw.write("Jan\t" + months[Calendar.JANUARY] + "\n");
        fw.write("Feb\t" + months[Calendar.FEBRUARY] + "\n");
        fw.write("Mar\t" + months[Calendar.MARCH] + "\n");
        fw.write("Apr\t" + months[Calendar.APRIL] + "\n");
        fw.write("May\t" + months[Calendar.MAY] + "\n");
        fw.write("Jun\t" + months[Calendar.JUNE] + "\n");
        fw.write("Jul\t" + months[Calendar.JULY] + "\n");
        fw.write("Aug\t" + months[Calendar.AUGUST] + "\n");
        fw.write("Sep\t" + months[Calendar.SEPTEMBER] + "\n");
        fw.write("Oct\t" + months[Calendar.OCTOBER] + "\n");
        fw.write("Nov\t" + months[Calendar.NOVEMBER] + "\n");
        fw.write("Dec\t" + months[Calendar.DECEMBER] + "\n");

        fw.write("" + "\n");

        // HOURS
        for (int i = 0; i < 24; i++) {
            fw.write(String.format("%02d:00", i) + "\t" + hours[i] + "\n");
        }

        fw.write("" + "\n");

        // COUNTS

        long totalCount = pandaTotalMessages + jessTotalMessages;

        fw.write("messages\t" + String.valueOf(totalCount) + "\n");
        fw.write("Jess Messages\t" + String.valueOf(jessTotalMessages) + "\n");
        fw.write("Panda Messages\t" + String.valueOf(pandaTotalMessages) + "\n");

        fw.write("" + "\n");

        // SIZES

        long totalSize = pandaTotalLength + jessTotalLength;

        fw.write("size\t"
                + String.valueOf((double) totalSize / (double) totalCount)
                + "\n");
        fw.write("Panda mean length\t"
                + String.valueOf((double) pandaTotalLength
                        / (double) pandaTotalMessages) + "\n");
        fw.write("Jess mean length\t"
                + String.valueOf((double) jessTotalLength
                        / (double) jessTotalMessages) + "\n");
        fw.write("Total Size\t" + String.valueOf(totalSize) + "\n");
        fw.write("Total Panda Size\t" + String.valueOf(pandaTotalLength) + "\n");
        fw.write("Total Jess Size\t" + String.valueOf(jessTotalLength) + "\n");

        fw.close();
    }
}
