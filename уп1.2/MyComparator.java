package up1;

import java.util.Comparator;

public class MyComparator implements Comparator<Message> {
    @Override
    public int compare(Message o1, Message o2){
        return o1.getTimestamp().compareTo(o2.getTimestamp());
    }
}