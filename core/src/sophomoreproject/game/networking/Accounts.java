package sophomoreproject.game.networking;

import java.io.*;
import java.util.HashMap;

public class Accounts implements Serializable {
    private HashMap<String, Account> accounts;

    public Accounts() {
        accounts = new HashMap<>();
    }

    /**
     * @param username new account username
     * @param password new account password
     * @return true: success, false: fail (already exists)
     */
    public boolean tryAddAccount(String username, String password) {
        if (accounts.containsKey(username)) {
            // account already exist!
            return false;
        } else {
            // create new account object with unique id
            Account newAccount = new Account(username, password, accounts.size());
            // add account object to hash map
            accounts.put(username, newAccount);
            return true;
        }
    }

    /**
     * @param username account username
     * @param password account password
     * @return -1: failed. any other non negative number is a successful id being returned
     */
    public long tryGetAccountID(String username, String password) {
        if (accounts.containsKey(username)) {
            Account account = accounts.get(username);
            if (account.password.equals(password)) {
                // password matched, success
                return account.accountID;
            }
        }
        // failed to return accountID
        return -1;
    }

    /**
     * @param filePathAndName path and name of serialized Accounts object to load
     * @return null: failed, else success
     */
    public static Accounts loadFromFile(String filePathAndName) {
        try {
            FileInputStream fIn = new FileInputStream(filePathAndName);
            ObjectInputStream objIn = new ObjectInputStream(fIn);
            Object obj = objIn.readObject();

            if (obj instanceof Accounts) {
                return (Accounts) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null; // failed at some point
    }

    /**
     * @param filePathAndName
     * @return true: success, false: fail
     */
    public boolean saveAccountsToFile(String filePathAndName) {
        try {
            FileOutputStream fOut = new FileOutputStream(filePathAndName);
            ObjectOutputStream objOut = new ObjectOutputStream(fOut);
            objOut.writeObject(this);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

         return false;
    }
}
