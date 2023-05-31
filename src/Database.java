import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Database {
    private String filename;
    private Encryption encryption;
    private Map<User, List<Account>> userAccounts;

    public Database(String filename) {
        this.encryption = new Encryption();
        this.filename = filename;
        this.userAccounts = new HashMap<>();
    }

    public void loadDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            User currentUser = null;
            List<Account> accounts = null;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String[] userS = parts[0].split(",");
                String[] accsS = parts[1].split(",");
                User pUser = new User(userS[0],userS[1]);
                addUser(pUser);

                String service = "", username = "", pass = "";

                for (int i = 0; i < accsS.length; i++) {
                    switch (i % 3) {
                        case 0:
                            service = accsS[i];
                            break;
                        case 1:
                            username = accsS[i];
                            break;
                        case 2:
                            pass = accsS[i];
                            addAccount(pUser, new Account(service, username, pass));
                            service = "";
                            username = "";
                            pass = "";
                            break;
                    }
                }
                }

        } catch (IOException e) {
            System.err.println("Error while loading the database from the text file");
            e.printStackTrace();
        }
    }

    public void saveDatabase() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (User user : getUsers()) {
                // Write user data
                String userStr = this.encryption.encrypt(user.getUsername(), user.getMasterPassword()) + "," + this.encryption.encrypt(user.getMasterPassword(), user.getMasterPassword()) + ";";
                writer.write(userStr);

                // Write account data
                List<Account> accounts = getAccounts(user);
                int accountCount = accounts.size();
                for (int i = 0; i < accountCount; i++) {
                    Account account = accounts.get(i);
                    String accountStr = this.encryption.encrypt(account.getService(), user.getMasterPassword()) + "," + this.encryption.encrypt(account.getUsername(), user.getMasterPassword()) + "," + this.encryption.encrypt(account.getPassword(), user.getMasterPassword());
                    if (i < accountCount - 1) {
                        accountStr += ",";
                    }
                    writer.write(accountStr);
                }

                // Write a newline character
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error while saving the database to the text file");
            e.printStackTrace();
        }
    }

    public void addAccount(User user, Account account) {
        List<Account> accounts = userAccounts.get(user);
        if (accounts == null) {
            accounts = new ArrayList<>();
            userAccounts.put(user, accounts);
        }
        accounts.add(account);
    }

    public void addUser(User user) {
        if (!userAccounts.containsKey(user)) {
            userAccounts.put(user, new ArrayList<>());
        }
    }

    public List<Account> getAccounts(User user){
        return this.userAccounts.get(user);
    }

    public Account getAccount(User user, String service) {
        List<Account> accounts = userAccounts.get(user);
        if (accounts != null) {
            for (Account account : accounts) {
                if (account.getService().equalsIgnoreCase(service)) {
                    return account;
                }
            }
        }
        return null;
    }

    public User getUser(String username) {
        for (User user : userAccounts.keySet()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public Set<User> getUsers(){
        return this.userAccounts.keySet();

    }

    public boolean deleteAccount(User user, String service) {
        List<Account> accounts = userAccounts.get(user);
        if (accounts != null) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getService().equalsIgnoreCase(service)) {
                    accounts.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public Map<User, List<Account>> getAllUsersAndAccounts() {
        return new HashMap<>(userAccounts);
    }
}