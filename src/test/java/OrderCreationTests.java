import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTests extends BaseClass {
    private Order order;
    private OrderBaseStep orderBaseStep;
    private final List<String> color;

    @Before
    public void setUp() {
        order = Order.getRandomOrder(color);
        orderBaseStep = new OrderBaseStep();
    }

    public OrderCreationTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Object[][] createOrder() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("BLACK", "GREY")},
                {List.of()},
        };
    }

    @Test
    @DisplayName("Создание заказов с разными цветами самокатов")
    @Description("Проверка, что возвращается код 201 и трек номер")
    public void orderTests() {
        ValidatableResponse response = orderBaseStep.createOrder(order)
                .assertThat().body("track", notNullValue())
                .and().statusCode(201);
        int trackId = response.extract().body().path("track");
        orderBaseStep.cancelOrder(trackId);
    }
}
