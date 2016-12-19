package uk.co.ticklethepanda.messagesbook.messagestopdf;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import uk.co.ticklethepanda.messagesbook.ConfigLoader;
import uk.co.ticklethepanda.messagesbook.datareader.FacebookMessageReader;
import uk.co.ticklethepanda.messagesbook.datareader.HangoutsMessageReader;
import uk.co.ticklethepanda.messagesbook.datareader.SkypeMessageReader;
import uk.co.ticklethepanda.messagesbook.datareader.TextMessageReader;
import uk.co.ticklethepanda.messagesbook.datareader.WhatsappMessageReader;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;

public final class GetInputs {

    private GetInputs() {
    }

    public static MessageList getInputs() throws IOException,
            ParserConfigurationException, ParseException, SAXException,
            ClassNotFoundException, SQLException {

        Properties properties = ConfigLoader.getProperties();

        MessageList ml = new MessageList();

        System.out.println("\tGetting Facebook messages");
        FacebookMessageReader fmr =
                new FacebookMessageReader(properties.getProperty("input.location")
                        + "/facebook/facebook.htm");
        ml = MessageList.combineMessageLists(ml, fmr.getMessages());

        System.out.println("\tGot Facebook messages");

        System.out.println("\tGetting Text messages");

        TextMessageReader tmr = new TextMessageReader(
                properties.getProperty("input.location"));
        ml = MessageList.combineMessageLists(ml, tmr.getMessages());

        System.out.println("\tGot Text messages");

        System.out.println("\tGetting Skype messages");

        SkypeMessageReader smr = new SkypeMessageReader(
                properties.getProperty("input.location")
        );
        ml = MessageList.combineMessageLists(ml, smr.getMessages());

        System.out.println("\tGot Skype messages");

        System.out.println("\tGetting Whatsapp messages");

        WhatsappMessageReader wmr = new WhatsappMessageReader(
                properties.getProperty("input.location")
        );
        ml = MessageList.combineMessageLists(ml, wmr.getMessages());

        System.out.println("\tGot Whatsapp messages");

        System.out.println("\tGetting Hangouts messages");

        HangoutsMessageReader hmr = new HangoutsMessageReader(
                properties.getProperty("input.location")
        );
        MessageList tempml = hmr.getMessages();
        ml = MessageList.combineMessageLists(ml, tempml);

        System.out.println("\tGot Hangouts messages");

        System.out.println("\tSorting messageList");
        ml.sort();
        System.out.println("\finished sorting messageList");

        return ml;
    }

}
