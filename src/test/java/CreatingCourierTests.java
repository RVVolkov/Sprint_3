import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreatingCourierTests extends BaseClass {
    private CreatingCourier courier;
    private CreatingCourier courierWithoutLogin;

    @Before
    public void setUp() {
        courier = CreatingCourier.getRandomCourier();
        courierWithoutLogin = CreatingCourier.getCourierWithoutLogin();
    }
    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверка, что возвращается код 201 при создании нового курьера и код 409 при попытке использовать такой же логин")
    public void creatingCourierTest() {
        //Создаю курьера и получаю тело ответа
        boolean actual = given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.CREATING_COURIER.endpoint)
                .then().statusCode(201)
                .extract().body().path("ok");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
        //Попытка создать курьера с таким же логином
        String act = given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.CREATING_COURIER.endpoint)
                .then().statusCode(409)
                .extract().body().path("message");
        String exp = "Этот логин уже используется. Попробуйте другой.";
        Assert.assertEquals(exp, act);
        //Получаю id курьера
        int id = given()
                .spec(getBaseSpec())
                .body(courier)
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
    public void creatingCourierWithoutLoginTest() {
        //Попытка создать курьера без логина
        String actual = given()
                .spec(getBaseSpec())
                .body(courierWithoutLogin)
                .when()
                .post(Endpoints.CREATING_COURIER.endpoint)
                .then().statusCode(400)
                .extract().body().path("message");
        String expected = "Недостаточно данных для создания учетной записи";
        Assert.assertEquals(expected, actual);
    }
}
