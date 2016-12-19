package messageWriter;

import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import uk.co.ticklethepanda.messagesbook.datareader.TextMessageReader;

public class TextTextMessageReader {
    TextMessageReader tmr;

    @Before
    public void setUp() throws Exception {
        //tmr = new TextMessageReader();
    }

    @Test
    public void getFileList_normalCircumstances_noError() {
        //assertFalse(TextMessageReader.getFileList() == null);
    }

    @Test
    public void getMessageList_normalCurumstances_noError()
            throws ParseException, ParserConfigurationException, SAXException,
            IOException {
        tmr.getMessages();
    }

}
