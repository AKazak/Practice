package up1;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CleanCode2 {
    public static void main(String []args){
        try{
            StringBuilder addTofile = new StringBuilder();
            addTofile.append("[");
            PrintStream ps = new PrintStream(new FileOutputStream("output.txt"));
            Chat chat = new Chat();
            boolean flag = true;
            int menuCount;
            Scanner menuScanner = new Scanner(System.in);
            do{
                System.out.println("Menu:");
                System.out.println("1.Add message");
                System.out.println("2.Add message from file");
                System.out.println("3.Delete message");
                System.out.println("4.Show history");
                System.out.println("5.Show history(period)");
                System.out.println("6.Download message to file");
                System.out.println("7.Search(author)");
                System.out.println("8.Search(word)");
                System.out.println("9.Search(regular)");
                System.out.println("0.Exit");
                menuCount = Integer.parseInt(menuScanner.next());
                switch(menuCount){
                    case 1:{
                        chat.addMessage();
                        break;
                    }
                    case 2:{
                        chat.addFromFile();
                        break;
                    }
                    case 3:{
                        chat.deleteMessage();
                        break;
                    }
                    case 4:{
                        if(chat.isEmpty()){
                            System.out.println("Your history is empty");
                        }
                        else{
                            chat.sort();
                            chat.print();
                        }
                        break;
                    }
                    case 5:{
                        chat.showHistoryPeriod();
                        break;
                    }
                    case 6:{
                        chat.addToFile(addTofile);
                        break;
                    }
                    case 7:{
                        chat.searchAuthor();
                        break;
                    }
                    case 8:{
                        chat.searchWord();
                        break;
                    }
                    case 9:{
                        chat.searchRegular();
                        break;
                    }
                    case 0:{
                        flag = false;
                        chat.logAdd();
                        if(addTofile.length() > 1){
                            addTofile.deleteCharAt(addTofile.lastIndexOf(","));
                            addTofile.append("]");
                            ps.print(addTofile);
                        }
                        ps.close();
                        menuScanner.close();
                        break;
                    }
                    default:{
                        System.out.println("Invalid input. Please try again");
                        break;
                    }
                }
            }
            while(flag);
        } catch(NumberFormatException nfe){
            System.err.println("Invalid input");
        } catch (FileNotFoundException fnfe){
            System.err.println("File not found");
        }
    }
}