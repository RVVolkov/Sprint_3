import com.github.javafaker.Faker;

public class CreatingCourier {
    private String login;
    private String password;
    private String firstName;
    public static Faker faker = new Faker();

    public CreatingCourier() {
    }

    public CreatingCourier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }


    public static CreatingCourier getRandomCourier() {
        final String login = faker.internet().domainName();
        ;
        final String password = faker.internet().password();
        final String firstName = faker.name().firstName();
        return new CreatingCourier(login, password, firstName);
    }

    public static CreatingCourier getCourierWithoutLogin() {
        final String login = faker.internet().domainName();
        final String password = faker.internet().password();
        final String firstName = faker.name().firstName();
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
