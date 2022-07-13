import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

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
        DELETE_COURIER("/api/v1/courier/{courierId}"),
        ORDER_CREATION("/api/v1/orders"),
        CANCEL_ORDER("/api/v1/orders/cancel"),
        ORDER_TRACK("/api/v1/orders/track"),
        ORDER_ACCEPT("/api/v1/orders/accept/{id}"),
        ORDER_LIST("/api/v1/orders");
        public String endpoint;

        Endpoints(String endpoint) {
            this.endpoint = endpoint;
        }
    }
}
