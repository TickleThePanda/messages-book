package messageWriter;

import java.io.FileNotFoundException;
import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import uk.co.ticklethepanda.messagesbook.datareader.WhatsappMessageReader;

public class TestWhatMessageReader {
    WhatsappMessageReader wmr;

    @Before
    public void setUp() throws Exception {
        //wmr = new WhatsappMessageReader();
    }

    @Test
    public void constructor_normalCircumstances_createSuccessful()
            throws ClassNotFoundException {
        //wmr = new WhatsappMessageReader();
    }

    @Test
    public void getMessages_normalCircumstances_noError()
            throws FileNotFoundException, ParseException {
        wmr.getMessages();
    }

}
