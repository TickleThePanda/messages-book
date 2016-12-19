package uk.co.ticklethepanda.messagesbook.datareader;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageData;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageType;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.Person;

public class TextMessageReader {

    private String location;

    public TextMessageReader(String location) {
        this.location = location;
    }

    private static File[] getFileList(String location) {
        File dir = new File(location + "/sms");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".xml"));
        return files;
    }

    public MessageList getMessages() throws ParseException,
            ParserConfigurationException, IOException, SAXException {

        File[] files = getFileList(this.location);

        MessageList ml = new MessageList();

        for (File xmlfile : files) {
            System.out.println("\t\tGetting " + xmlfile.getName());
            MessageList tempMl = new MessageList();
            DocumentBuilderFactory dbFactory =
                    DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlfile);

            doc.getDocumentElement().normalize();

            NodeList nl = doc.getDocumentElement().getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                Node nNode = nl.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    if (element.getAttribute("contact_name").equals(
                            "Jess Rushworth")) {
                        Person person = null;
                        if (element.getAttribute("type").equals("1")) {
                            person = Person.JESS;
                        } else {
                            person = Person.PANDA;
                        }
                        String message = element.getAttribute("body");
                        Long timestamp =
                                Long.valueOf(element.getAttribute("date"));

                        if (timestamp < 1000000000000L)
                            continue;

                        tempMl.addMessage(new MessageData(person, timestamp,
                                message, MessageType.TEXT));
                    }
                }
            }
            System.out.println("\t\tGot " + xmlfile.getName());
            System.out.println("\t\tCombining messages");
            ml = MessageList.combineMessageLists(ml, tempMl);
            System.out.println("\t\tFinish combining messagess");

        }

        return ml;

    }
}
