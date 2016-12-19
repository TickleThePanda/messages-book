package uk.co.ticklethepanda.messagesbook.messagesdatastructure;

public class MessageData {
    private final Person person;
    private final String message;
    private final MessageType messageType;
    private final DateAndTime dt;

    public MessageData(final Person pPerson, final long timestamp,
            final String pMessage, final MessageType pMessageType) {
        // set time
        this.dt = new DateAndTime(timestamp);

        // set person
        this.person = pPerson;

        String modifiedMessage = pMessage;

        modifiedMessage = modifiedMessage.replaceAll("&gt;", ">");
        modifiedMessage = modifiedMessage.replaceAll("&lt;", "<");

        // fix quotes
        modifiedMessage = modifiedMessage.replaceAll("&quot;", "\"");
        modifiedMessage = modifiedMessage.replaceAll("&ldquo;", "\"");
        modifiedMessage = modifiedMessage.replaceAll("&rdquo;", "\"");
        modifiedMessage = modifiedMessage.replaceAll("&apos;", "'");
        modifiedMessage = modifiedMessage.replaceAll("&rsquo;", "'");
        modifiedMessage = modifiedMessage.replaceAll("&lsquo;", "'");

        // fix symbols
        modifiedMessage = modifiedMessage.replaceAll("&amp;", "&");
        modifiedMessage = modifiedMessage.replaceAll("&not;", "�");
        modifiedMessage = modifiedMessage.replaceAll("&pound;", "�");
        modifiedMessage = modifiedMessage.replaceAll("&gt;", ">");
        modifiedMessage = modifiedMessage.replaceAll("&lt;", "<");
        modifiedMessage = modifiedMessage.replaceAll("&mdash;", "-");
        modifiedMessage = modifiedMessage.replaceAll("&ndash;", "-");
        modifiedMessage = modifiedMessage.replaceAll("&hellip;", "...");
        modifiedMessage = modifiedMessage.replaceAll("&sup3;", "\u00B3");
        modifiedMessage = modifiedMessage.replaceAll("&radic;", "\u221A");

        // fix accents
        modifiedMessage = modifiedMessage.replaceAll("&aacute;", "\u00e2");
        modifiedMessage = modifiedMessage.replaceAll("&eacute;", "\u00e9");
        modifiedMessage = modifiedMessage.replaceAll("&iacute;", "\u00ed");
        modifiedMessage = modifiedMessage.replaceAll("&oacute;", "\u00f3");
        modifiedMessage = modifiedMessage.replaceAll("&uacute;", "\u00fa");

        // get rid of tags
        modifiedMessage =
                modifiedMessage.replaceAll(
                        "<.?ss.*?>|<.?a.*?>|<.?videomessage.*?>", "");

        // set message
        this.message = modifiedMessage;

        // set message type
        this.messageType = pMessageType;
    }

    public final DateAndTime getDateAndTime() {
        return this.dt;
    }

    public final String getMessage() {

        return this.message;
    }

    public final MessageType getMessageType() {
        return this.messageType;
    }

    public final String getMessageTypeAsString() {

        switch (this.messageType) {
        case FACEBOOK:
            return "Facebook - ";
        case SKYPE:
            return "Skype - ";
        case WHATSAPP:
            return "WhatsApp - ";
        case TEXT:
            return "Text - ";
        case HANGOUTS:
            return "Hangouts - ";
        default:
            return "";
        }
    }

    public final Person getPerson() {
        return this.person;
    }

    public final String toString() {
        return this.person.name() + " " + this.dt.getDateAsString()
                + " " + this.dt.getTimeAsString() + " " + this.message + " "
                + this.messageType;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (object instanceof MessageData) {
            MessageData md = (MessageData) object;
            if (this.getPerson() == md.getPerson()
                    && this.getMessage() == md.getMessage()
                    && this.messageType == md.getMessageType()
                    && this.dt.equals(md.getDateAndTime()))
                return true;
        }
        return false;
    }
}
