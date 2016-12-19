package uk.co.ticklethepanda.messagesbook.messagesdatastructure.skype;

import java.util.ArrayList;

import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageData;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageList;
import uk.co.ticklethepanda.messagesbook.messagesdatastructure.MessageType;

public class SkypeMessageList {

    ArrayList<SkypeMessageData> list;

    public static SkypeMessageList combineSkypeMessageLists(
            SkypeMessageList listA, SkypeMessageList listB) {
        // remove duplicates
        for (int aPos = 0; aPos < listA.size(); aPos++) {
            for (int bPos = 0; bPos < listB.size(); bPos++) {
                if (listA.get(aPos).getRemoteId() == listB.get(bPos)
                        .getRemoteId()
                        && listA.get(aPos).getMessage()
                                .equals(listB.get(bPos).getMessage())) {
                    if (listA.get(aPos).getTimestamp() < listB.get(bPos)
                            .getTimestamp()) {
                        listB.remove(bPos);
                        bPos--;
                    } else {
                        listA.remove(aPos);
                    }
                }
            }
        }

        // mergeListA and ListB

        SkypeMessageList newList = new SkypeMessageList();

        int aPos = 0, bPos = 0;
        while (aPos < listA.size() && bPos < listB.size()) {
            if (listA.get(aPos).getTimestamp() < listB.get(bPos).getTimestamp()) {
                newList.add(listA.get(aPos));
                aPos++;
            } else {
                newList.add(listB.get(bPos));
                bPos++;
            }
        }

        while (aPos < listA.size()) {
            newList.add(listA.get(aPos));
            aPos++;
        }

        while (bPos < listB.size()) {
            newList.add(listB.get(bPos));
            bPos++;
        }

        return newList;
    }

    public int size() {
        return list.size();
    }

    public SkypeMessageList() {
        list = new ArrayList<SkypeMessageData>();
    }

    public void add(SkypeMessageData smd) {
        list.add(smd);
    }

    public SkypeMessageData get(int i) {
        return list.get(i);
    }

    public void remove(int i) {
        list.remove(i);
    }

    public MessageList toMessageList() {
        MessageList ml = new MessageList();
        for (SkypeMessageData smd : list) {
            MessageData md =
                    new MessageData(smd.getPerson(), smd.getTimestamp(),
                            smd.getMessage(), MessageType.SKYPE);
            ml.addMessage(md);
        }
        return ml;
    }
}
