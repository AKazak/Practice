package up1;

import java.io.FileWriter;
import java.io.IOException;

public class Singleton {
    public static Singleton instance = new Singleton("logfile.txt");
    private FileWriter logfile;
    Singleton(String file){
        try{
            logfile = new FileWriter(file);
        }
        catch(IOException e){
            System.err.println("Error" + e.getMessage());
        }
    }
    public void add(String type, String message){
        try{
            logfile.write(type + ": " + message + "\r\n");
            logfile.flush();
        } catch(IOException e){
            System.err.println("Error" + e.getMessage());
        }
    }
}
