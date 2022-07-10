import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class OrderListTests extends BaseClass {
    private CreatingCourier courier;
    File json = new File("src/test/resources/anyColorTest.json");


    @Before
    public void setUp() {
        courier = CreatingCourier.getRandomCourier();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что возвращается код 200 и не пустое тело ответа")
    public void getOrderListTest() {
        //Создаю курьера
        RestAssured.given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.CREATING_COURIER.endpoint)
                .then().statusCode(201);
        //Получаю его id
        int courierId = given()
                .spec(getBaseSpec())
                .body(courier)
                .post(Endpoints.LOGIN_COURIER.endpoint)
                .then().statusCode(200)
                .extract().body().path("id");
        //Создаю заказ и получаю его trackid
        int trackId = given()
                .spec(getBaseSpec())
                .body(json)
                .when()
                .post(Endpoints.ORDER_CREATION.endpoint)
                .then().extract().body().path("track");
        //Получаю id заказа по трек номеру
        String response = given()
                .spec(getBaseSpec())
                .when()
                .get(Endpoints.ORDER_TRACK.endpoint, trackId)
                .then().extract().body().asString().substring(15, 20);
        int id = Integer.parseInt(response);
        //Принимаю заказ
        RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .put(Endpoints.ORDER_ACCEPT.endpoint, id, courierId)
                .then().statusCode(200);
        //Получаю список заказов
        RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .get(Endpoints.ORDER_LIST.endpoint, courierId)
                .then().statusCode(200)
                .and().body(notNullValue());
        //Удаляю курьера
        RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .delete(Endpoints.DELETE_COURIER.endpoint, courierId)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Запрос с несуществующим courierId")
    @Description("Проверка, что возвращается код 404 и текст ошибки")
    public void getOrderListWithNonexistentCourierId() {
        //Создаю курьера
        RestAssured.given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(Endpoints.CREATING_COURIER.endpoint)
                .then().statusCode(201);
        //Получаю его id
        int courierId = given()
                .spec(getBaseSpec())
                .body(courier)
                .post(Endpoints.LOGIN_COURIER.endpoint)
                .then().statusCode(200)
                .extract().body().path("id");
        //Удаляю курьера
        RestAssured.given()
                .spec(getBaseSpec())
                .when()
                .delete(Endpoints.DELETE_COURIER.endpoint, courierId)
                .then().statusCode(200);
        //Попытка получить список заказов
        String actual = given()
                .spec(getBaseSpec())
                .when()
                .get(Endpoints.ORDER_LIST.endpoint, courierId)
                .then().statusCode(404)
                .extract().body().path("message");
        String expected = "Курьер с идентификатором " + courierId + " не найден";
        Assert.assertEquals(expected, actual);
    }
}
