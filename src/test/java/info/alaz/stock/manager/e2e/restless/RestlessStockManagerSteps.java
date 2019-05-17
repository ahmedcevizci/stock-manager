package info.alaz.stock.manager.e2e.restless;

import info.alaz.stock.manager.dto.ProductStockDto;
import info.alaz.stock.manager.dto.TimeSpan;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static info.alaz.stock.manager.web.ParentStockManagerController.STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE;
import static info.alaz.stock.manager.web.restless.StockManagerRestlessController.*;

public class RestlessStockManagerSteps {

    private Response response;

    public void setup(int port) {
        RestAssured.reset();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBasePath(BASE_API_PATH)
                .setPort(port)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Step("Update stock of a given product")
    public void updateStockOfProduct(ProductStockDto productStockDto) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .body(productStockDto)
                .when()
                .post("/restless/updateStock");
    }

    @Step("Get stock of a given product")
    public void getStockOfAProduct(String productId) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .queryParam(PARAM_PRODUCT_ID, productId)
                .when()
                .get("/restless/stock");
    }

    @Step("Get statistics about products in stock")
    public void getStatisticsAboutProductsInStock(TimeSpan timeSpan) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .queryParam(PARAM_TIME, timeSpan.name())
                .when()
                .get("/restless/statistics");
    }

    @Step("Validating Http status")
    public void validateHttpStatus(HttpStatus httpStatus) {
        response.then().statusCode(httpStatus.value());
        response.prettyPrint();
    }

    @Step("Validating statistics response dto")
    public void validateStatisticsResponseDto(TimeSpan lastMonth) {
    }

    @Step("Validating product response dto")
    public void validateProductResponseDto(String existingProductId1) {
        /*        response.then()
         .body(equalTo("\"" + timeSpan.name() + "\""));
                .body("[0].startTime", equalTo("2019-04-21T20:05:15+0200"))
                .body("[0].endTime", equalTo("2019-04-21T22:05:15+0200"))
                .body("[1].startTime", equalTo("2019-04-21T19:05:15+0200"))
                .body("[1].endTime", equalTo("2019-04-21T21:05:15+0200"))
        response.prettyPrint();*/
    }
}
