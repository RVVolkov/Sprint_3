import org.apache.commons.lang3.RandomStringUtils;

public class CreatingCourier {
    private String login;
    private String password;
    private String firstName;

    public CreatingCourier() {
    }
    public CreatingCourier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }


    public static CreatingCourier getRandomCourier() {
        final String login = RandomStringUtils.randomAlphabetic(5);
        final String password = RandomStringUtils.randomNumeric(5);
        final String firstName = RandomStringUtils.randomAlphabetic(8);
        return new CreatingCourier(login, password, firstName);
    }

    public static CreatingCourier getCourierWithoutLogin() {
        final String login;
        final String password = RandomStringUtils.randomNumeric(5);
        final String firstName = RandomStringUtils.randomAlphabetic(8);
        return new CreatingCourier("", password, firstName);
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
