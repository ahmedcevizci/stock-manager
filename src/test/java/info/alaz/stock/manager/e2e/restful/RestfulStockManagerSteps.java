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
import static info.alaz.stock.manager.web.restless.StockManagerRestlessController.*;

public class RestfulStockManagerSteps {

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
    public void updateStockOfProduct(String productId, UUID stockId, StockUpdateRequestDto stockUpdateRequestDto) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .pathParam(PARAM_PRODUCT_ID, productId)
                .pathParam(PARAM_STOCK_ID, stockId.toString())
                .body(stockUpdateRequestDto)
                .when()
                .put("/restful/products/{" + PARAM_PRODUCT_ID + "}/stocks/{" + PARAM_STOCK_ID + "}");
    }

    @Step("Get stock of a given product")
    public void getStockOfAProduct(String productId) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .pathParam(PARAM_PRODUCT_ID, productId)
                .when()
                .get("/restful/products/{" + PARAM_PRODUCT_ID + "}");
        response.prettyPrint();
    }

    @Step("Get statistics about products in stock")
    public void getStatisticsAboutProductsInStock(TimeSpan timeSpan) {
        response = SerenityRest.given().log().all()
                .header(HttpHeaders.ACCEPT, STOCK_MANAGER_V1_RESPONSE_MEDIA_TYPE)
                .queryParam(PARAM_TIME, timeSpan.name())
                .when()
                .get("/restful/statistics");
    }

    @Step("Validating Http status")
    public void validateHttpStatus(HttpStatus httpStatus) {
        response.then().statusCode(httpStatus.value());
        response.prettyPrint();
    }



}
