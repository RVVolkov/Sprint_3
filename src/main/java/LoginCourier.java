import org.apache.commons.lang3.RandomStringUtils;

public class LoginCourier {
    private String login;
    private String password;

    public LoginCourier() {
    }

    public LoginCourier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public static LoginCourier getRandomCourierCredentials() {
        final String login = RandomStringUtils.randomAlphabetic(5);
        final String password = RandomStringUtils.randomNumeric(5);
        return new LoginCourier(login, password);
    }

    public static LoginCourier getRandomCourierWithoutLogin() {
        final String login = RandomStringUtils.randomAlphabetic(5);
        final String password = RandomStringUtils.randomNumeric(5);
        return new LoginCourier("", password);
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
