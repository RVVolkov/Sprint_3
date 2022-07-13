import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTests extends BaseClass {
    private CreatingCourier courier;
    private CourierBaseSteps courierBaseSteps;
    private Order order;
    private OrderBaseStep orderBaseStep;
    private List<String> color;

    @Before
    public void setUp() {
        courier = CreatingCourier.getRandomCourier();
        courierBaseSteps = new CourierBaseSteps();
        courierBaseSteps.creatingCourier(courier);
        order = Order.getRandomOrder(color);
        orderBaseStep = new OrderBaseStep();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что возвращается код 200 и не пустое тело ответа")
    public void getOrderListTest() {
        LoginCourier credentials = LoginCourier.from(courier);
        ValidatableResponse loginCourier = courierBaseSteps.loginCourier(credentials);
        int courierId = loginCourier.extract().body().path("id");
        ValidatableResponse createOrder = orderBaseStep.createOrder(order);
        int trackId = createOrder.extract().body().path("track");
        int orderId = given()
                .spec(getBaseSpec())
                .when()
                .queryParam("t", trackId)
                .get(Endpoints.ORDER_TRACK.endpoint)
                .then().extract().body().path("order.id");
        ValidatableResponse acceptOrder = given()
                .spec(getBaseSpec())
                .when()
                .pathParam("id", orderId)
                .queryParam("courierId", courierId)
                .put(Endpoints.ORDER_ACCEPT.endpoint)
                .then().statusCode(200);
        ValidatableResponse orderList = orderBaseStep.listOfOrder(courierId)
                .statusCode(200).and().body(notNullValue());
        courierBaseSteps.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Запрос с несуществующим courierId")
    @Description("Проверка, что возвращается код 404 и текст ошибки")
    public void getOrderListWithNonexistentCourierId() {
        LoginCourier credentials = LoginCourier.from(courier);
        ValidatableResponse loginCourier = courierBaseSteps.loginCourier(credentials);
        int courierId = loginCourier.extract().body().path("id");
        courierBaseSteps.deleteCourier(courierId);
        ValidatableResponse orderList = orderBaseStep.listOfOrder(courierId);
        String actual = orderList.statusCode(404)
                .extract().body().path("message");
        String expected = "Курьер с идентификатором " + courierId + " не найден";
        Assert.assertEquals(expected, actual);
    }

}
