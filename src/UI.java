import java.util.Scanner;

class UI {
    private Scanner scanner;
    private Database database;

    public UI() {
        scanner = new Scanner(System.in);
        database = new Database("passwords.json");
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


        }

    private void register(){

    }

    private void mainMenuLogined(){
        while (true) {
            System.out.println("1. Add account");
            System.out.println("2. View account");
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
                    viewAccount();
                    break;
                case 3:
                    deleteAccount();
                    break;
                case 4:
                    database.saveDatabase();
                    System.exit(0);
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
        database.addAccount(account);
    }

    public void viewAccount() {
        System.out.print("Enter service name: ");
        String service = scanner.nextLine();

        Account account = database.getAccount(service);

        if (account != null) {
            System.out.println("Username: " + account.getUsername());
            System.out.println("Password: " + account.getPassword());
        } else {
            System.out.println("No account found for the specified service.");
        }
    }

    public void deleteAccount() {
        System.out.print("Enter service name: ");
        String service = scanner.nextLine();

        if (database.deleteAccount(service)) {
            System.out.println("Account deleted successfully.");
        } else {
            System.out.println("No account found for the specified service.");
        }
    }
}
