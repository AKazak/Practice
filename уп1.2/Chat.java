package up1;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Chat {
    private ArrayList<Message> alMessage;
    private Singleton log = Singleton.instance;
    private int countAddMessage;
    private int countDeleteMessage;
    private int countFindMessage;

    Chat(){
        alMessage = new ArrayList<Message>();
        countAddMessage = 0;
        countDeleteMessage = 0;
        countFindMessage = 0;
    }

    public void addMessage(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter author:");
            String author = br.readLine();
            System.out.println("Enter message:");
            String message = br.readLine();
            Date date = new Date();
            String timestamp = ((Long)date.getTime()).toString();
            Message mess = new Message(author, message, timestamp);
            alMessage.add(mess);
            countAddMessage++;
            log.add("Info", "Message from " + mess.getAuthor() + " added");
        } catch(IOException e){
            System.err.println("Error" + e.getMessage());
            log.add("Error", "Erro r" + e.getMessage());
        }
    }

    public void addFromFile(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("log.txt"));
            String infFromFile = br.readLine();
            JSONParser parser = new JSONParser();
            JSONObject jsonObject;
            Object obj = parser.parse(infFromFile);
            int j = 0;
            JSONArray jsonArray = (JSONArray) obj;
            for(int i = 0; i < jsonArray.size(); i++){
                j = 0;
                jsonObject = (JSONObject) jsonArray.get(i);
                String id = jsonObject.get("id").toString();
                String author = jsonObject.get("author").toString();
                String message = jsonObject.get("message").toString();
                String timestamp = jsonObject.get("timestamp").toString();
                Message mess = new Message(id, author, message, timestamp);
                for(Message m: alMessage){
                    if(m.getId().equals(mess.getId())){
                        j = 1;
                    }
                }
                if(j == 0){
                    alMessage.add(mess);
                    countAddMessage++;
                    log.add("Info", "Add 1 message from file log.txt");
                }
            }
        } catch (org.json.simple.parser.ParseException pe) {
            System.err.println("Error" + pe.getMessage());
            log.add("Error", "Error " + pe.getMessage());
        } catch (IOException e){
            System.err.println("Error" + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void deleteMessage(){
        try {
            System.out.println("Enter id:");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String id = br.readLine();
            for (Message m : alMessage) {
                if (m.getId().equals(id)) {
                    alMessage.remove(m);
                    log.add("Info", "Message with id " + id + " has been deleted");
                    countDeleteMessage++;
                }
            }
        } catch(IOException e){
            System.err.println("Error " + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void addToFile(){
        try{
            PrintStream ps = new PrintStream(new FileOutputStream("output.txt", true));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter author:");
            String author = br.readLine();
            System.out.println("Enter message:");
            String message = br.readLine();
            Date date = new Date();
            String timestamp = ((Long)date.getTime()).toString();
            Message mess = new Message(author, message, timestamp);
            alMessage.add(mess);
            ps.println(mess.toJSON());
            countAddMessage++;
            log.add("Info", "Message to file output.txt from " + mess.getAuthor() + " added");
        } catch (IOException e){
            System.err.println("Error" + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void sort(){
        Collections.sort(alMessage, new MyComparator());
    }

    public boolean isEmpty(){
        if(alMessage.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

    public void historyPeriod(){
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
                    for(Message m: alMessage){
                        calendar.setTimeInMillis(Long.parseLong(m.getTimestamp()));
                        if(calendar.after(calendar1)){
                            if(calendar.before(calendar2) || calendar.equals(calendar2)){
                                System.out.println(m);
                                countFindMessage++;
                                log.add("Info", "Message was found in period " + str);
                                flag1 = true;
                            }
                        }
                    }
                    if(!flag1){
                        System.out.println("There is no such messages");
                        log.add("Info", "There is no such messages in period " + str);
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
            for(Message m: alMessage){
                if(m.getAuthor().equals(author)){
                    System.out.println(m);
                    countFindMessage++;
                    log.add("Info", "Message with author " + author + " was found");
                    flag = true;
                }
            }
            if(!flag){
                System.out.println("There is no such messages");
                log.add("Info", "There is no such messages with author " + author);
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
            for(Message m: alMessage){
                if(m.getMessage().contains(word)){
                    System.out.println(m);
                    countFindMessage++;
                    flag = true;
                }
            }
            if(!flag){
                System.out.println("There is no such messages");
                log.add("Info", "There is no messages with word " + word);
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
            for(Message m: alMessage){
                matcher = pattern.matcher(m.getMessage());
                if(matcher.matches()){
                    System.out.println(m);
                    countFindMessage++;
                    log.add("Info", "Message was found in the regular expression " + regular);
                    flag = true;
                }
            }
            if(!flag){
                System.out.println("There is no such messages");
                log.add("Info", "There is no such message in the regular expression " + regular);
            }
        } catch(IOException e){
            System.err.println("Error" + e.getMessage());
            log.add("Error", "Error " + e.getMessage());
        }
    }

    public void logAdd(){
        log.add("Info", "The number of messages added " + countAddMessage);
        log.add("Info", "The number of messages deleted " + countDeleteMessage);
        log.add("Info", "The number of messages found " + countFindMessage);
    }

    public void print(){
        for(Message m: alMessage){
            System.out.println(m.toString());
        }
    }
}
