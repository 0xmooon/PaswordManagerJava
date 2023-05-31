import java.util.List;
import java.util.Scanner;

class UI {
    private Scanner scanner;
    private Database database;

    private Encryption encryption;

    private String MansterPassword;
    private User presentUser;

    public UI() {
        scanner = new Scanner(System.in);
        database = new Database("data.txt");

        encryption = new Encryption();
        database.loadDatabase();

    }

    public void mainMenu() {
        while (true) {
            System.out.println("1. Log in");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline from input
            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    register();
                    break;
                case 3:
                    database.saveDatabase();
                    System.exit(0);
                }
            }


        }
    private void login(){
        while (true) {
            System.out.print("User name: ");
            String username = scanner.nextLine();

            for (int i = 0; i < 3; i++) {

                System.out.print("Password: ");
                String masterPassword = scanner.nextLine();
                this.MansterPassword = masterPassword;

                if (this.database.getUser(encryption.encrypt(username,masterPassword)) != null) {
                 if (this.database.getUser(encryption.encrypt(username,masterPassword)).checkLogin(encryption.encrypt(username,masterPassword), encryption.encrypt(masterPassword,masterPassword))) {
                     this.presentUser = this.database.getUser(encryption.encrypt(username,masterPassword));
                     mainMenuLogined();
                 }
             }
            }
            System.out.println("No account found");

            }

        }

    private void register(){
        while (true) {
            System.out.print("User name: ");
            String username = scanner.nextLine();

            System.out.print("Password: ");
            String masterPassword = scanner.nextLine();


            this.presentUser = new User(username,masterPassword);
            this.database.addUser(this.presentUser );
            mainMenuLogined();
        }

    }

    private void mainMenuLogined(){
        while (true) {
            System.out.println("1. Add account");
            System.out.println("2. View accounts");
            System.out.println("3. Delete account");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline from input

            switch (choice) {
                case 1:
                    addAccount();
                    break;
                case 2:
                    viewAccounts();
                    break;
                case 3:
                    deleteAccount();
                    break;
                case 4:
                    database.saveDatabase();
                    mainMenu();
            }
        }
    }
    public void addAccount() {
        System.out.print("Enter service name: ");
        String service = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        Account account = new Account(service, username, password);
        database.addAccount(presentUser, account);
    }

    public void viewAccounts() {

        List<Account> accounts = this.database.getAccounts(presentUser);
        System.out.println("+-----------------------------------+");
        for (Account account : accounts) {

            if (account != null) {

                System.out.println("Service: " + encryption.decrypt(account.getService(), this.MansterPassword));
                System.out.println("Username: " + encryption.decrypt(account.getUsername(), this.MansterPassword));
                System.out.println("Password: " + encryption.decrypt(account.getPassword(), this.MansterPassword));
                System.out.println("+-----------------------------------+");
            }

        }
    }

        public void viewAccountByService() {
            System.out.print("Enter service name: ");
            String service = scanner.nextLine();
        }


    public void deleteAccount() {
        System.out.print("Enter service name: ");
        String service = scanner.nextLine();

        if (database.deleteAccount(presentUser, encryption.encrypt(service,this.MansterPassword))) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("No account found for the specified service.");
        }
    }
}
