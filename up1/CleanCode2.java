package up1;

import java.io.*;
import java.util.Scanner;

public class CleanCode2 {
    public static void main(String[] args) {
        try {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                StringBuilder addTofile = new StringBuilder();
                addTofile.append("[");
                try (PrintStream ps = new PrintStream(new FileOutputStream("output.txt"))) {
                    Chat chat = new Chat();
                    boolean flag = true;
                    int menuCount;
                    try (Scanner menuScanner = new Scanner(System.in)) {
                        do {
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
                            switch (menuCount) {
                                case 1: {
                                    chat.addMessage(br);
                                    break;
                                }
                                case 2: {
                                    chat.addFromFile(br);
                                    break;
                                }
                                case 3: {
                                    chat.deleteMessage(br);
                                    break;
                                }
                                case 4: {
                                    if (chat.isEmpty()) {
                                        System.out.println("Your history is empty");
                                    } else {
                                        chat.sort();
                                        chat.print();
                                    }
                                    break;
                                }
                                case 5: {
                                    chat.showHistoryPeriod(br);
                                    break;
                                }
                                case 6: {
                                    chat.addToFile(addTofile, br);
                                    break;
                                }
                                case 7: {
                                    chat.searchAuthor(br);
                                    break;
                                }
                                case 8: {
                                    chat.searchWord(br);
                                    break;
                                }
                                case 9: {
                                    chat.searchRegular(br);
                                    break;
                                }
                                case 0: {
                                    flag = false;
                                    chat.logAdd();
                                    if (addTofile.length() > 1) {
                                        addTofile.deleteCharAt(addTofile.lastIndexOf(","));
                                        addTofile.append("]");
                                        ps.print(addTofile);
                                    }
                                    ps.close();
                                    break;
                                }
                                default: {
                                    System.out.println("Invalid input. Please try again");
                                    break;
                                }
                            }
                        }
                        while (flag);
                    }
                }
            }
        } catch (NumberFormatException nfe) {
            System.err.println("Invalid input");
        } catch (FileNotFoundException fnfe) {
            System.err.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}