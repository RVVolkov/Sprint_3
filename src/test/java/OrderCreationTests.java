import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import java.io.File;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTests extends BaseClass {
    private static File json;

    public OrderCreationTests(File json) {
        this.json = json;
    }

    @Parameterized.Parameters
    public static Object[][] getJsonFile() {
        return new Object[][]{
                {json = new File("src/test/resources/anyColorTest.json")},
                {json = new File("src/test/resources/twoColorsTest.json")},
                {json = new File("src/test/resources/withoutColorTest.json")},
        };
    }

    @Test
    @DisplayName("Создание заказов с разными цветами самокатов")
    @Description("Проверка, что возвращается код 201 и трек номер")
    public void orderTests() {
        //Создаю заказ и получаю его трек номер
        int trackId = given()
                .spec(getBaseSpec())
                .body(json)
                .when()
                .post(Endpoints.ORDER_CREATION.endpoint)
                .then().assertThat().body("track", notNullValue())
                .and().statusCode(201)
                .extract().body().path("track");
        //Удаляю созданные заказы
        boolean actual = given()
                .spec(getBaseSpec())
                .when()
                .put(Endpoints.CANCEL_ORDER.endpoint, trackId)
                .then().statusCode(200)
                .extract().body().path("ok");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
    }
}
