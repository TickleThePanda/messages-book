package uk.co.ticklethepanda.messagesbook.messagesdatastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MessageList {

    public static MessageList combineMessageLists(final MessageList listA,
            final MessageList listB) {
        final MessageList newList = new MessageList();
        int i = 0, j = 0;
        while (i < listA.length() && j < listB.length()) {
            if (listA.getMessage(i).equals(listB.getMessage(j))) {
                newList.addMessage(listA.getMessage(i));
                i++;
                j++;
            } else {
                if (listA.getMessage(i).getDateAndTime().getTimestamp() < listB
                        .getMessage(j).getDateAndTime().getTimestamp()) {
                    newList.addMessage(listA.getMessage(i));
                    i++;
                } else {
                    newList.addMessage(listB.getMessage(j));
                    j++;
                }
            }
        }

        while (i < listA.length()) {
            newList.addMessage(listA.getMessage(i));
            i++;
        }

        while (j < listB.length()) {
            newList.addMessage(listB.getMessage(j));
            j++;
        }

        return newList;
    }

    private final ArrayList<MessageData> al;

    public MessageList() {
        this.al = new ArrayList<MessageData>();
    }

    public final void sort() {
        Collections.sort(al, new Comparator<MessageData>() {
            @Override
            public int compare(MessageData arg0, MessageData arg1) {
                long arg0Time = arg0.getDateAndTime().getTimestamp();
                long arg1Time = arg1.getDateAndTime().getTimestamp();
                if (arg0Time > arg1Time)
                    return 1;
                else if (arg0Time < arg1Time)
                    return -1;
                return 0;
            }
        });
    }

    public final void addMessage(final MessageData message) {
        this.al.add(message);
    }

    public final MessageData getMessage(final int n) {
        return this.al.get(n);
    }

    public final int length() {
        return this.al.size();
    }

}
