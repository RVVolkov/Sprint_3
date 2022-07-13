public class LoginCourier {
    private String login;
    private String password;

    public LoginCourier() {
    }

    public LoginCourier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public LoginCourier(CreatingCourier courier) {
        this.login = courier.getLogin();
        this.password = courier.getPassword();
    }

    public static LoginCourier from(CreatingCourier courier) {
        return new LoginCourier(courier);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
