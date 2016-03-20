package up1;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public static Logger instance = new Logger("logfile.txt");
    private FileWriter logfile;
    Logger(String file){
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
