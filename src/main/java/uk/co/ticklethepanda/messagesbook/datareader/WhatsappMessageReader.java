package uk.co.ticklethepanda.messagesbook.datareader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageData;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageType;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.Person;

public class WhatsappMessageReader {

    private final String location;

    public WhatsappMessageReader(String location) {
        this.location = location;
    }

    public static File[] getFileList(String location) {
        File dir = new File(location + "/whatsapp");
        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return name.endsWith(".txt");
            }
        });
        return files;
    }

    public MessageList getMessages() throws FileNotFoundException,
            ParseException {
        File[] files = getFileList(location);

        MessageList ml = new MessageList();

        for (File txtFile : files) {

            MessageList mlTemp = new MessageList();

            Scanner sr = new Scanner(txtFile);

            while (sr.hasNextLine()) {
                String line = sr.nextLine();

                String dateString =
                        line.substring(0, line.indexOf("-")) + "2013";
                line = line.substring(line.indexOf("-") + 2);
                String personString = line.substring(0, line.indexOf(":"));
                line = line.substring(line.indexOf(":") + 2);

                long timestamp =
                        new SimpleDateFormat("HH:mm',' dd MMM yyyy").parse(
                                dateString).getTime();
                Person person = Person.NONE;
                if (personString.equals("Panda")) {
                    person = Person.PANDA;
                } else if (personString.equals("Jess Rushworth")) {
                    person = Person.JESS;
                }
                String message = line;

                MessageData md =
                        new MessageData(person, timestamp, message,
                                MessageType.WHATSAPP);

                mlTemp.addMessage(md);

            }

            sr.close();
            ml = MessageList.combineMessageLists(ml, mlTemp);
        }
        return ml;
    }
}
