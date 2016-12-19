package uk.co.ticklethepanda.messagesbook.messagesdatastructure.skype;

import uk.co.ticklethepanda.messagesbook.messagesdatastructure.Person;

public class SkypeMessageData {
    private final long timestamp;
    private final String message;
    private final long remoteId;
    private final Person person;

    public SkypeMessageData(long timestamp, String message, long remoteId,
            Person person) {
        this.timestamp = timestamp;
        this.message = message;
        this.remoteId = remoteId;
        this.person = person;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public long getRemoteId() {
        return remoteId;
    }

    public Person getPerson() {
        return person;
    }

}
