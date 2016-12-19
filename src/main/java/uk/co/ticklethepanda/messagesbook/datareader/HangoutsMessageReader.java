package uk.co.ticklethepanda.messagesbook.datareader;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageData;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageType;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.Person;

public class HangoutsMessageReader {
    private final String location;

    public HangoutsMessageReader(String location) {
        this.location = location;
    }

    public MessageList getMessages() throws IOException {

        MessageList ml = new MessageList();

        JsonReader reader =
                new JsonReader(new FileReader(location + "/hangouts/Hangouts.json"));
        reader.setLenient(true);
        reader.beginObject();
        reader.nextName();
        reader.beginArray();

        // for every conversation
        while (reader.hasNext()) {
            reader.beginObject();

            // if it's the right conversation

            if (isCorrectMessageThread(reader)) {
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("response_header")) {
                        reader.skipValue();
                        continue;
                    }
                    if (name.equals("conversation_state")) {
                        reader.beginObject();
                        while (reader.hasNext()) {
                            String name2 = reader.nextName();

                            // finally got to the messages

                            if (name2.equals("event")) {
                                reader.beginArray();

                                while (reader.hasNext()) {
                                    // reader.skipValue();
                                    reader.beginObject();
                                    MessageData temp = nextMessage(reader);
                                    if (temp != null)
                                        ml.addMessage(temp);
                                    reader.endObject();
                                }

                                reader.endArray();
                            } else {
                                reader.skipValue();
                            }
                        }
                        reader.endObject();
                    }
                }
            } else {
                while (reader.hasNext()) {
                    reader.skipValue();
                }
            }
            reader.endObject();
        }

        reader.endArray();
        reader.endObject();
        return ml;
    }

    private boolean isCorrectMessageThread(JsonReader reader)
            throws IOException {
        reader.nextName();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id")) {
                String idText = reader.nextString();
                if (idText.equals("UgxLhYJ8anCHxF-eNsV4AaABAQ")) {
                    reader.endObject();
                    return true;
                }
            }
        }
        reader.endObject();
        return false;
    }

    private MessageData nextMessage(JsonReader reader) throws IOException {
        Person person = null;
        String message = "";
        long timestamp = 0;

        while (reader.hasNext()) {
            if (reader.peek().equals(JsonToken.NAME)) {

                String nextName = reader.nextName();

                if (nextName.equals("sender_id")) {
                    reader.beginObject();
                    reader.skipValue();
                    if (reader.nextString().equals("117783212067293338653")) {
                        person = Person.PANDA;
                    } else {
                        person = Person.JESS;
                    }
                    while (reader.hasNext()) {
                        reader.skipValue();
                    }
                    reader.endObject();
                } else if (nextName.equals("timestamp")) {
                    timestamp = reader.nextLong() / 1000;
                } else if (nextName.equals("hangout_event")) {
                    while (reader.hasNext())
                        reader.skipValue();
                    return null;
                } else if (nextName.equals("chat_message")) {
                    reader.beginObject();
                    reader.skipValue();
                    reader.beginObject();
                    reader.skipValue();
                    reader.beginArray();
                    while (reader.hasNext()) {
                        if (reader.peek().equals(JsonToken.BEGIN_OBJECT)) {
                            reader.beginObject();
                            while (reader.hasNext()) {
                                String name2 = reader.nextName();
                                if (name2.equals("text")) {
                                    message += reader.nextString();
                                } else {
                                    reader.skipValue();
                                }
                            }
                            reader.endObject();
                        } else {
                            reader.skipValue();
                        }

                    }
                    reader.endArray();
                    while (reader.hasNext())
                        reader.skipValue();
                    reader.endObject();
                    reader.endObject();
                } else {
                    reader.skipValue();
                }
            } else {
                reader.skipValue();
                break;
            }
        }
        MessageData md =
                new MessageData(person, timestamp, message,
                        MessageType.HANGOUTS);
        return md;
    }
}
