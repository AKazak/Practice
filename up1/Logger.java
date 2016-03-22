package up1;

import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static Logger instance;
    private FileWriter logfile;
    Logger(String file){
        try{
            logfile = new FileWriter(file);
        }
        catch(IOException e){
            System.err.println("Error" + e.getMessage());
        }
    }

    public static synchronized Logger getInstance(){
        if(instance == null){
            instance = new Logger("logfile.txt");
        }
        return instance;
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
