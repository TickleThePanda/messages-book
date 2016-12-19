package uk.co.ticklethepanda.messagesbook.messagestopdf;

import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessagesMetaData;
import uk.co.ticklethepanda.messagesbook.pdfwriter.CreateDocument;

public final class Driver {

    private Driver() {
    }

    public static void main(final String[] args) throws Throwable {

        System.out.println("Getting inputs");

        final MessageList messages = GetInputs.getInputs();

        final MessagesMetaData mmd = new MessagesMetaData();

        mmd.analyseMessageList(messages);

        mmd.printOutput();

        System.out.println("Finished getting inputs");

        System.out.println("Creating Output");

        CreateDocument.createDocumentsFromMessageList(messages);

        System.out.println("Finished creating output");

    }

}
