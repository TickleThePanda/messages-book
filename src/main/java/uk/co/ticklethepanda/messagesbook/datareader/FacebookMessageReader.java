package uk.co.ticklethepanda.messagesbook.datareader;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageData;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageType;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.Person;

public class FacebookMessageReader {
    private static Document fileToDOM(final File file) throws IOException,
            ParserConfigurationException {
        final CleanerProperties props = new CleanerProperties();

        // get rid of some useless header stuff
        props.setTranslateSpecialEntities(true);
        props.setOmitXmlDeclaration(false);

        // do parsing
        TagNode tagNode = null;
        tagNode = new HtmlCleaner(props).clean(file);

        // turn file into DOM
        final DomSerializer ds = new DomSerializer(props);
        Document document = null;
        document = ds.createDOM(tagNode);

        // return document;
        return document;
    }

    private NodeList nodeList;

    public FacebookMessageReader(final String fileName) throws IOException,
            ParserConfigurationException {
        final File file = new File(fileName);
        final Document doc = fileToDOM(file);
        this.nodeList = getMessageNodeList(doc);
    }

    public final MessageList getMessages() throws ParseException {
        final MessageList messages = new MessageList();
        // starting from 1 because first value is text rather than a node.
        for (int i = 1; i < this.nodeList.getLength(); i = i + 2) {
            final Node header = this.nodeList.item(i);
            final Node messageHeader = header.getFirstChild();
            final String sender =
                    messageHeader.getFirstChild().getFirstChild()
                            .getTextContent();
            String date =
                    messageHeader.getFirstChild().getNextSibling()
                            .getFirstChild().getTextContent();

            final Node m = this.nodeList.item(i + 1);

            String message = "";
            if (m.getFirstChild() != null) {
                message = m.getFirstChild().getTextContent();
            }

            Person person = null;
            if (sender.startsWith("Thomas")) {
                person = Person.PANDA;
            } else if (sender.startsWith("Jess")) {
                person = Person.JESS;
            }

            date = date.split(",")[1];

            long timestamp = 0L;
            final SimpleDateFormat sdf =
                    new SimpleDateFormat("dd MMM yyyy 'at' HH:mm zzzzzzzz",
                            Locale.ENGLISH);
            timestamp = sdf.parse(date).getTime();

            messages.addMessage(new MessageData(person, timestamp, message,
                    MessageType.FACEBOOK));
        }
        return messages;
    }

    private NodeList getMessageNodeList(final Document doc) {
        // navigateDOMtoThreadNode
        final Node body = doc.getFirstChild().getLastChild();
        final Node header =
                body.getFirstChild().getNextSibling().getFirstChild();
        Node thread = header.getNextSibling();

        while (true) {
            if (thread.getNextSibling() == null) {
                return null;
            }
            if (thread.getTextContent().startsWith(
                    "Thomas 'Panda' Attwood, Jess Namubilu Rushworth")) {
                break;
            }
            thread = thread.getNextSibling();
        }

        final NodeList nl = thread.getChildNodes();

        return nl;
    }

}
