package info.alaz.stock.manager.e2e.restful;

import info.alaz.stock.manager.dto.TimeSpan;
import info.alaz.stock.manager.dto.restful.StockUpdateRequestDto;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static info.alaz.stock.manager.web.ParentStockManagerController.STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE;
import static info.alaz.stock.manager.web.restful.StockManagerRestfulController.BASE_RESTFUL_API_PATH;
import static info.alaz.stock.manager.web.restless.StockManagerRestlessController.*;

public class RestfulStockManagerSteps {

    private Response response;

    public void setup(int port) {
        RestAssured.reset();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBasePath(BASE_RESTFUL_API_PATH)
                .setPort(port)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Step("Update stock of a given product")
    public void updateStockOfProduct(String productId, UUID stockId, StockUpdateRequestDto stockUpdateRequestDto) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .pathParam(PARAM_PRODUCT_ID, productId)
                .pathParam(PARAM_STOCK_ID, stockId.toString())
                .body(stockUpdateRequestDto)
                .when()
                .put("/products/{" + PARAM_PRODUCT_ID + "}/stocks/{" + PARAM_STOCK_ID + "}");
    }

    @Step("Get stock of a given product")
    public void getStockOfAProduct(String productId) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .pathParam(PARAM_PRODUCT_ID, productId)
                .when()
                .get("/products/{" + PARAM_PRODUCT_ID + "}");
        response.prettyPrint();
    }

    @Step("Get statistics about products in stock")
    public void getStatisticsAboutProductsInStock(TimeSpan timeSpan) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .queryParam(PARAM_TIME, timeSpan.name())
                .when()
                .get("/statistics");
    }

    @Step("Validating Http status")
    public void validateHttpStatus(HttpStatus httpStatus) {
        response.then().statusCode(httpStatus.value());
        response.prettyPrint();
    }


/*    @Step("Validating sensor status")
    public void validateSensorStatus(SensorStatus sensorStatus) {
        response.then()
                .body(equalTo("\"" + sensorStatus.name() + "\""));
    }*/


/*    @Step("Validating alert list")
    public void validateAlertList() {
        response.then()
                .body("[0].startTime", equalTo("2019-04-21T20:05:15+0200"))
                .body("[0].endTime", equalTo("2019-04-21T22:05:15+0200"))
                .body("[0].measurement1", equalTo(2200))

                .body("[1].startTime", equalTo("2019-04-21T19:05:15+0200"))
                .body("[1].endTime", equalTo("2019-04-21T21:05:15+0200"))
        response.prettyPrint();
    }*/
}
