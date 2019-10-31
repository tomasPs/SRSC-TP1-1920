package SMCP;

import java.util.*;

public class MessageSequenceHandler {
    private Map<String, Integer> lastReceived;
//    private Map<String, List<Payload>> messageList;
    private int mySequence;

    public MessageSequenceHandler() {
        lastReceived = new HashMap<>();
//        messageList = new HashMap<>();
        mySequence = 1;
    }

    public boolean validateNewMsg(String user, int seqNumber) {
        if (!lastReceived.containsKey(user)) {
            lastReceived.put(user, seqNumber);
            return true;
        }

        int last = lastReceived.get(user);

        if (seqNumber == last + 1) {
            lastReceived.put(user, last + 1);
            return true;
        } else {
            return false;
        }
    }

//    public void addMessageQueue(Payload msg) {
//        String user = msg.getFromPeerID();
//        List<Payload> list = messageList.get(user);
//        if (list == null)
//            list = new ArrayList<>();
//        list.add(msg);
//        Collections.sort(list);
//
//        messageList.put(user, list);
//    }

//    public Payload getValidMsg(String user) {
//        int last = 0;
//        if (lastReceived.containsKey(user))
//            last = lastReceived.get(user);
//        else
//            return null;
//        Payload msg = messageList.get(user).get(0);
//        if (msg == null || msg.getSeqNumber() != last + 1)
//            return null;
//        else {
//            lastReceived.put(user, last + 1);
//            return msg;
//        }
//    }

    public int useSequence() {
        return this.mySequence++;
    }
}
