public class User {
    private String username;
    private String masterPassword;

    public User(String username, String masterPassword) {
        this.username = username;
        this.masterPassword = masterPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMasterPassword() {
        return masterPassword;
    }

    public void setMasterPassword(String masterPassword) {
        this.masterPassword = masterPassword;
    }

    public boolean checkLogin(String username, String masterPassword){
        if (this.username.equals(username)  && this.masterPassword.equals(masterPassword) ){
            return true;
        }
        return false;
    }

}