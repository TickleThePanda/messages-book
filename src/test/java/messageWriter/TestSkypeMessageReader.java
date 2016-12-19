package messageWriter;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import uk.co.ticklethepanda.messagesbook.datareader.SkypeMessageReader;

public class TestSkypeMessageReader {
    SkypeMessageReader smr;

    @Before
    public void setUp() throws Exception {
        //smr = new SkypeMessageReader();
    }

    @Test
    public void constructor_normalCircumstances_createSuccessful()
            throws ClassNotFoundException {
        //smr = new SkypeMessageReader();
    }

    @Test
    public void getMessages_normalCircumstances_noError() throws SQLException {
        smr.getMessages();
    }

}
