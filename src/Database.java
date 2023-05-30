import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Database {
    private String filename;
    private List<Account> accounts;

    public Database(String filename) {
        this.filename = filename;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Account getAccount(String service) {
        for (Account account : accounts) {
            if (account.getService().equalsIgnoreCase(service)) {
                return account;
            }
        }
        return null;
    }

    public boolean deleteAccount(String service) {
        Iterator<Account> iterator = accounts.iterator();

        while (iterator.hasNext()) {
            Account account = iterator.next();
            if (account.getService().equalsIgnoreCase(service)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public void loadDatabase() {
        String fileContent = FileIO.readFile(filename);
        if (fileContent != null) {
            List<Account> parsedAccounts = parseCsv(fileContent);
            accounts.clear();
            accounts.addAll(parsedAccounts);
        }
    }

    public void saveDatabase() {
        String csv = createCsv(accounts);
        FileIO.writeFile(filename, csv);
    }

    private List<Account> parseCsv(String csv) {
        List<Account> parsedAccounts = new ArrayList<>();
        String[] lines = csv.split("\n");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] fields = line.split(",");
                if (fields.length >= 3) {
                    String service = fields[0].trim();
                    String username = fields[1].trim();
                    String password = fields[2].trim();
                    parsedAccounts.add(new Account(service, username, password));
                }
            }
        }

        return parsedAccounts;
    }

    private String createCsv(List<Account> accounts) {
        StringBuilder csvBuilder = new StringBuilder();

        for (Account account : accounts) {
            csvBuilder.append(account.getService()).append(",");
            csvBuilder.append(account.getUsername()).append(",");
            csvBuilder.append(account.getPassword()).append("\n");
        }

        return csvBuilder.toString();
    }
}