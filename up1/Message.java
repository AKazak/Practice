package up1;

import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Message {
    private String id;
    private String message;
    private String author;
    private long timestamp;

    Message(String author, String message, long timestamp){
        this.message = message;
        this.author = author;
        id = UUID.randomUUID().toString();
        this.timestamp = timestamp;
    }

    Message(String id, String author, String message, long timestamp){
        this.message = message;
        this.author = author;
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId(){
        return id;
    }

    public String getMessage(){
        return message;
    }

    public String getAuthor(){
        return author;
    }

    public long getTimestamp(){
        return timestamp;
    }

    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        obj.put("id:", id);
        obj.put("message:", message);
        obj.put("author:", author);
        obj.put("timestamp:", timestamp);
        return obj;
    }

    @Override
    public String toString() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(this.getTimestamp() + 3600000);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy ',' HH:mm:ss");
        String strDate = sdf.format(date);
        return "id: " + id + ", author: \"" +
                author + "\", message: \"" + message + "\", timestamp: " + strDate;
    }
}