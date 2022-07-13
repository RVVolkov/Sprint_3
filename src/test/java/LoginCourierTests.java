import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTests extends BaseClass {

    private CreatingCourier courier;
    private CourierBaseSteps courierBaseSteps;

    @Before
    public void setUp() {
        courier = CreatingCourier.getRandomCourier();
        courierBaseSteps = new CourierBaseSteps();
        courierBaseSteps.creatingCourier(courier);
    }

    @Test
    @DisplayName("Успешный логин")
    @Description("Проверка, что возвращается код 200")
    public void successfulLoginTest() {
        LoginCourier credentials = LoginCourier.from(courier);
        ValidatableResponse response = courierBaseSteps.loginCourier(credentials);
        int courierId = response.assertThat().body("id", notNullValue())
                .and().statusCode(200)
                .extract().body().path("id");
        courierBaseSteps.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Запрос без логина")
    @Description("Проверка, что возвращается код 400 и текст ошибки")
    public void loginWithoutRequiredFieldsTest() {
        LoginCourier credentials = LoginCourier.from(courier);
        credentials.setLogin("");
        ValidatableResponse response = courierBaseSteps.loginCourier(credentials)
                .statusCode(400)
                .and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Запрос с несуществующим логином")
    @Description("Проверка, что возвращается код 404 и текст ошибки")
    public void incorrectDataLoginTest() {
        LoginCourier credentials = LoginCourier.from(courier);
        credentials.setPassword(RandomStringUtils.randomAlphanumeric(8));
        ValidatableResponse response = courierBaseSteps.loginCourier(credentials)
                .statusCode(404)
                .and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Запрос без тела запроса")
    @Description("Проверка, что возвращается код 504 и сервер не доступен Service unavailable")
    public void requestWithoutBody() {
        RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .post(Endpoints.LOGIN_COURIER.endpoint)
                .then().statusCode(504)
                .and().extract().response().equals("Service unavailable");
    }

}
