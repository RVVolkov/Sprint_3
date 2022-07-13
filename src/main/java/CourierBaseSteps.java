import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CourierBaseSteps extends BaseClass {
    @Step("Создание курьера")
    public ValidatableResponse creatingCourier(CreatingCourier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.CREATING_COURIER.endpoint)
                .then();
    }

    @Step("Логин курьера")
    public ValidatableResponse loginCourier(LoginCourier credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(Endpoints.LOGIN_COURIER.endpoint)
                .then();
    }

    @Step("Получение id курьера")
    public int getCourierId(CreatingCourier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.LOGIN_COURIER.endpoint)
                .then().assertThat().body("id", notNullValue())
                .and().statusCode(200)
                .extract().body().path("id");
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int courierId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(Endpoints.DELETE_COURIER.endpoint, courierId)
                .then().statusCode(200);
    }
}
