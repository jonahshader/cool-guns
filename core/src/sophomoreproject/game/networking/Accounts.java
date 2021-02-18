package sophomoreproject.game.networking;

import java.io.*;
import java.util.HashMap;

public class Accounts {
    private static final String PATH_TO_ACCOUNTS = "accounts.data";
    private HashMap<String, Account> accounts;

    public Accounts() {
        accounts = new HashMap<>();
    }

    // constructor for loading from file
    private Accounts(HashMap<String, Account> accounts) {
        this.accounts = accounts;
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
            saveAccountsToFile();
            return true;
        }
    }

    /**
     * @param username account username
     * @param password account password
     * @return -1: failed. any other non negative number is a successful id being returned
     */
    public int tryGetAccountID(String username, String password) {
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
     * @return null: failed, else success
     */
    public static Accounts loadFromFile() {
        try {
            FileInputStream fIn = new FileInputStream(PATH_TO_ACCOUNTS);
            ObjectInputStream objIn = new ObjectInputStream(fIn);
            Object obj = objIn.readObject();


            return new Accounts((HashMap<String, Account>) obj);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null; // failed at some point
    }

    /**
     * @return true: success, false: fail
     */
    public boolean saveAccountsToFile() {
        try {
            FileOutputStream fOut = new FileOutputStream(PATH_TO_ACCOUNTS);
            ObjectOutputStream objOut = new ObjectOutputStream(fOut);
            objOut.writeObject(accounts);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

         return false;
    }

    public Account getAccountByUsername(String username) {
        return accounts.get(username);
    }
}
