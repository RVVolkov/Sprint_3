import io.qameta.allure.Step;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseClass {
    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("http://qa-scooter.praktikum-services.ru")
                .setContentType(ContentType.JSON)
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();
    }
    public enum Endpoints {
        CREATING_COURIER("/api/v1/courier"),
        LOGIN_COURIER("/api/v1/courier/login"),
        DELETE_COURIER("/api/v1/courier/{id}"),
        ORDER_CREATION("/api/v1/orders"),
        CANCEL_ORDER("/api/v1/orders/cancel?track={trackId}"),
        ORDER_TRACK("/api/v1/orders/track?t={trackId}"),
        ORDER_ACCEPT("/api/v1/orders/accept/{id}?courierId={courierId}"),
        ORDER_LIST("/api/v1/orders?courierId={courierId}");
        public String endpoint;
        Endpoints(String endpoint){
            this.endpoint = endpoint;
        }
    }
}
