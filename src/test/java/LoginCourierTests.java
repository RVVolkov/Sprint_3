import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTests extends BaseClass {
    private LoginCourier credentials;
    private LoginCourier credentialsWithoutLogin;

    @Before
    public void setUp() {
        credentials = LoginCourier.getRandomCourierCredentials();
        credentialsWithoutLogin = LoginCourier.getRandomCourierWithoutLogin();
    }

    @Test
    @DisplayName("Успешный логин")
    @Description("Проверка, что возвращается код 200")
    public void successfulLoginTest() {
        //Создаю курьера
        RestAssured.given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(Endpoints.CREATING_COURIER.endpoint)
                .then().statusCode(201);
        //Выполгяю логин и получаю id курьера
        int id = given()
                .spec(getBaseSpec())
                .body(credentials)
                .post(Endpoints.LOGIN_COURIER.endpoint)
                .then().assertThat().body("id", notNullValue())
                .and().statusCode(200)
                .extract().body().path("id");
        //Удаляю созданного курьера
        RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .delete(Endpoints.DELETE_COURIER.endpoint, id)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Запрос без логина")
    @Description("Проверка, что возвращается код 400 и текст ошибки")
    public void loginWithoutRequiredFieldsTest() {
        //Попытка отправить запрос без указания логина
        RestAssured.given()
                .spec(getBaseSpec())
                .body(credentialsWithoutLogin)
                .when()
                .post(Endpoints.LOGIN_COURIER.endpoint)
                .then().statusCode(400)
                .and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("Запрос с несуществующим логином")
    @Description("Проверка, что возвращается код 404 и текст ошибки")
    public void incorrectDataLoginTest() {
        //Попытка отправить запрос с несуществующим логином
        RestAssured.given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(Endpoints.LOGIN_COURIER.endpoint)
                .then().statusCode(404)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }
}
