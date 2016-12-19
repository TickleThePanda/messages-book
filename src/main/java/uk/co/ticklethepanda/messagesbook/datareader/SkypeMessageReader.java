package uk.co.ticklethepanda.messagesbook.datareader;

import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.Person;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.skype.SkypeMessageData;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.skype.SkypeMessageList;

public class SkypeMessageReader {

    private final String location;

    public SkypeMessageReader(String location) throws ClassNotFoundException {
        this.location = location;
        Class.forName("org.sqlite.JDBC");
    }

    public static File[] getFileList(String location) {
        File dir = new File(location + "/skype");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".db"));
        return files;
    }

    public MessageList getMessages() throws SQLException {

        File[] files = getFileList(location);

        SkypeMessageList sml = new SkypeMessageList();

        for (File dbfile : files) {
            Connection connection =
                    DriverManager.getConnection("jdbc:sqlite:"
                            + dbfile.getPath());
            Statement statement = connection.createStatement();
            String sql =
                    "SELECT author, timestamp, body_xml, remote_id "
                            + "FROM messages "
                            + "WHERE dialog_partner = 'jess.rushworth' AND chatmsg_type = 3";
            statement.execute(sql);
            ResultSet rs = statement.getResultSet();
            SkypeMessageList tempSml = new SkypeMessageList();
            while (rs.next()) {
                String author = rs.getString("author");
                Person person = Person.NONE;
                if (author.contains("jess.rushworth"))
                    person = Person.JESS;
                else if (author.contains("killerpandarage"))
                    person = Person.PANDA;
                long timestamp = rs.getLong("timestamp") * 1000;
                String body_xml = rs.getString("body_xml");
                long remote_id = rs.getLong("remote_id");

                SkypeMessageData smd =
                        new SkypeMessageData(timestamp, body_xml, remote_id,
                                person);

                tempSml.add(smd);
            }
            sml = SkypeMessageList.combineSkypeMessageLists(sml, tempSml);
        }

        MessageList ml = sml.toMessageList();

        return ml;

    }
}
