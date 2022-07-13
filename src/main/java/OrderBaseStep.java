import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderBaseStep extends BaseClass {
    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(Endpoints.ORDER_CREATION.endpoint).then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int trackId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .queryParam("track", trackId)
                .put(Endpoints.CANCEL_ORDER.endpoint)
                .then().statusCode(200);
    }

    @Step("Получение списка заказов")
    public ValidatableResponse listOfOrder(int courierId) {
        return given()
                .spec(getBaseSpec())
                .when()
                .queryParam("courierId", courierId)
                .get(Endpoints.ORDER_LIST.endpoint)
                .then();
    }

}
