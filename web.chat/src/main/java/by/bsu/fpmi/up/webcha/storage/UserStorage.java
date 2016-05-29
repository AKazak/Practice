package by.bsu.fpmi.up.webcha.storage;

import by.bsu.fpmi.up.webcha.utils.Hashcode;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class UserStorage {
    protected static Map<String, String> users;
    private static UserStorage ourInstance = new UserStorage();

    private UserStorage() {
        users = new HashMap<>();
        try(InputStream usersStream = getClass().getClassLoader().getResourceAsStream("users.txt")){
            Scanner usersFile = new Scanner(usersStream);
            while (usersFile.hasNext()) {
                String login = usersFile.next();
                String password = usersFile.next();
                users.put(login, password);
            }
            usersFile.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static UserStorage getInstance() {
        return ourInstance;
    }

    public boolean checkUser(String login, String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (!users.containsKey(login)) {
            return false;
        }
        String userPassword = users.get(login);
        return !userPassword.equals(Hashcode.encryptPassword(password));
    }
}
