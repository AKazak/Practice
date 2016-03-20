package up1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Chat {
    public final int MAX_MESSAGE_LENGTH = 140;
    private List<Message> messages;
    private Logger log = Logger.instance;
    private int countAdd;
    private int countDelete;
    private int countFind;

    Chat(){
        messages = new ArrayList<>();
        countAdd = 0;
        countDelete = 0;
        countFind = 0;
    }

    public void addMessage(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter author:");
            String author = br.readLine();
            System.out.println("Enter message:");
            String message = br.readLine();
            long timestamp = System.currentTimeMillis();
            Message mess = new Message(author, message, timestamp);
            messages.add(mess);
            countAdd++;
            log.add("Info", "Message from " + mess.getAuthor() + " added");
            if(mess.getMessage().length() > MAX_MESSAGE_LENGTH ){
                log.add("Warning", "Length of this message is more than 140 characters");
            }
        } catch(IOException e){
            System.err.println("Error " + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void addFromFile(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("log.txt"));
            String infFromFile = br.readLine();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject;
            Object obj = parser.parse(infFromFile);
            int j;
            JSONArray jsonArray = (JSONArray) obj;
            for (Object aJsonArray : jsonArray) {
                j = 0;
                jsonObject = (JSONObject) aJsonArray;
                String id = jsonObject.get("id").toString();
                String author = jsonObject.get("author").toString();
                String message = jsonObject.get("message").toString();
                long timestamp = (long)jsonObject.get("timestamp");
                Message mess = new Message(id, author, message, timestamp);
                for (Message m : messages) {
                    if (m.getId().equals(mess.getId())) {
                        j = 1;
                    }
                }
                if (j == 0) {
                    messages.add(mess);
                    countAdd++;
                    log.add("Info", "Add 1 message from file log.txt");
                    if (mess.getMessage().length() > MAX_MESSAGE_LENGTH) {
                        log.add("Warning", "Length of this message is more than 140 characters");
                    }
                }
            }
        } catch (org.json.simple.parser.ParseException | IOException pe) {
            System.err.println("Error " + pe.getMessage());
            log.add("Error", "Error " + pe.getMessage());
        }
    }

    public void deleteMessage(){
        try {
            System.out.println("Enter id:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String id = br.readLine();
            messages.stream().filter(m -> m.getId().equals(id)).forEach(m -> {
                messages.remove(m);
                log.add("Info", "Message with id " + id + " has been deleted");
                countDelete++;
            });
        } catch(IOException e){
            System.err.println("Error " + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void addToFile(StringBuilder sb){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter author:");
            String author = br.readLine();
            System.out.println("Enter message:");
            String message = br.readLine();
            long timestamp = System.currentTimeMillis();
            Message mess = new Message(author, message, timestamp);
            messages.add(mess);
            sb.append(mess.toJSON());
            sb.append(",");
            countAdd++;
            log.add("Info", "Message to file output.txt from " + mess.getAuthor() + " added");
            if(mess.getMessage().length() > MAX_MESSAGE_LENGTH ){
                log.add("Warning", "Length of this message is more than 140 characters");
            }
        } catch (IOException e){
            System.err.println("Error " + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void sort(){
        Collections.sort(messages, (msg1, msg2) -> Long.compare(msg1.getTimestamp(),msg2.getTimestamp()));
    }

    public boolean isEmpty(){
        return messages.isEmpty();
    }

    public void showHistoryPeriod(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            boolean flag = true;
            while(flag){
                System.out.println("Enter period(Example: 21.08.1997-27.08.1997):");
                String str = br.readLine();
                if(str.length() != 21){
                    System.out.println("Invalid input. Please try again");
                }
                else{
                    StringTokenizer tok = new StringTokenizer(str, ". -");
                    int day1 = Integer.parseInt(tok.nextToken());
                    int month1 = Integer.parseInt(tok.nextToken());
                    int year1 = Integer.parseInt(tok.nextToken());
                    int day2 = Integer.parseInt(tok.nextToken());
                    int month2 = Integer.parseInt(tok.nextToken());
                    int year2 = Integer.parseInt(tok.nextToken());
                    Calendar calendar1 = new GregorianCalendar(year1, month1 - 1, day1);
                    Calendar calendar2 = new GregorianCalendar(year2, month2 - 1, day2);
                    Calendar calendar = new GregorianCalendar();
                    boolean flag1 = false;
                    for(Message m: messages){
                        calendar.setTimeInMillis(m.getTimestamp());
                        if(calendar.after(calendar1)){
                            if(calendar.before(calendar2) || calendar.equals(calendar2)){
                                System.out.println(m);
                                countFind++;
                                log.add("Info", "Message was found in period " + str);
                                flag1 = true;
                            }
                        }
                    }
                    if(!flag1){
                        System.out.println("There is no such message");
                        log.add("Info", "There is no such message in period " + str);
                    }
                    flag = false;
                }
            }
        } catch (IOException e){
            System.err.println("Error" + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void searchAuthor(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter author:");
            String author = br.readLine();
            boolean flag = false;
            for(Message m: messages){
                if(m.getAuthor().equals(author)){
                    System.out.println(m);
                    countFind++;
                    log.add("Info", "Message with author " + author + " was found");
                    flag = true;
                }
            }
            if(!flag){
                System.out.println("There is no such message");
                log.add("Info", "There is no such message with author " + author);
            }
        } catch(IOException e){
            System.err.println("Error" + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void searchWord(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter word:");
            String word = br.readLine();
            boolean flag = false;
            for(Message m: messages){
                if(m.getMessage().contains(word)){
                    System.out.println(m);
                    countFind++;
                    flag = true;
                }
            }
            if(!flag){
                System.out.println("There is no such message");
                log.add("Info", "There is no message with word " + word);
            }
        } catch(IOException e){
            System.err.println("Error" + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void searchRegular(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter regular expression(Example: ^[0-9]{6}$ ):");
            String regular = br.readLine();
            Pattern pattern = Pattern.compile(regular);
            Matcher matcher;
            boolean flag = false;
            for(Message m: messages){
                matcher = pattern.matcher(m.getMessage());
                if(matcher.matches()){
                    System.out.println(m);
                    countFind++;
                    log.add("Info", "Message was found in the regular expression " + regular);
                    flag = true;
                }
            }
            if(!flag){
                System.out.println("There is no such message");
                log.add("Info", "There is no such message in the regular expression " + regular);
            }
        } catch(IOException e){
            System.err.println("Error" + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void logAdd(){
        log.add("Info", "The number of messages added " + countAdd);
        log.add("Info", "The number of messages deleted " + countDelete);
        log.add("Info", "The number of messages found " + countFind);
    }

    public void print(){
        for(Message m: messages){
            System.out.println(m.toString());
        }
    }
}