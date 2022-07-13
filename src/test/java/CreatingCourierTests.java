import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CreatingCourierTests extends BaseClass {
    private CreatingCourier courier;
    private CourierBaseSteps courierBaseSteps;

    @Before
    public void setUp() {
        courierBaseSteps = new CourierBaseSteps();
    }

    @Test
    @DisplayName("Создание нового курьера")
    @Description("Проверка, что возвращается код 201 при создании нового курьера и код 409 при попытке использовать такой же логин")
    public void creatingCourierTest() {
        courier = CreatingCourier.getRandomCourier();
        ValidatableResponse response = courierBaseSteps.creatingCourier(courier);
        boolean actual = response.statusCode(201)
                .extract().body().path("ok");
        boolean expected = true;
        Assert.assertEquals(expected, actual);
        //Попытка создать курьера с таким же логином
        response = courierBaseSteps.creatingCourier(courier);
        String act = response.statusCode(409)
                .extract().body().path("message");
        String exp = "Этот логин уже используется. Попробуйте другой.";
        Assert.assertEquals(exp, act);
        int courierId = courierBaseSteps.getCourierId(courier);
        courierBaseSteps.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Запрос без логина")
    @Description("Проверка, что возвращается код 400 и текст ошибки")
    public void creatingCourierWithoutLoginTest() {
        //Попытка создать курьера без логина
        courier = CreatingCourier.getCourierWithoutLogin();
        ValidatableResponse response = courierBaseSteps.creatingCourier(courier);
        String actual = response.statusCode(400)
                .extract().body().path("message");
        String expected = "Недостаточно данных для создания учетной записи";
        Assert.assertEquals(expected, actual);
    }
}
