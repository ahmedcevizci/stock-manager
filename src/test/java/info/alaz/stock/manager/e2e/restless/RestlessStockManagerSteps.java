package info.alaz.stock.manager.e2e.restless;

import info.alaz.stock.manager.dto.ProductStockDto;
import info.alaz.stock.manager.dto.TimeSpan;
import info.alaz.stock.manager.matcher.IsZonedDateTime;
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
import static org.hamcrest.core.IsEqual.equalTo;

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

    @Step("Validating product")
    public void validateProduct(String productId, UUID stockId, String stockUpdateDateTimeStr, int stockQuantity) {
        response.then()
                .body("productId", equalTo(productId))
                .body("requestTimestamp", new IsZonedDateTime("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
                .body("stock.id", equalTo(stockId.toString()))
                .body("stock.timestamp", equalTo(stockUpdateDateTimeStr))
                .body("stock.quantity", equalTo(stockQuantity));
        response.prettyPrint();
    }

    @Step("Validating statistics of today")
    public void validateStatisticsOfToday() {
        response.then()
                .body("requestTimestamp", new IsZonedDateTime("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
                .body("timeSpan", equalTo("" + TimeSpan.TODAY.name() + ""))
                .body("topAvailableProducts.size", equalTo(2))
                .body("topAvailableProducts[0].id", equalTo("d081fc48-332b-46d8-8b22-45eb8e02cc04"))
                .body("topAvailableProducts[0].timestamp", equalTo("2019-04-24T18:05:24.000+0200"))
                .body("topAvailableProducts[0].productId", equalTo("vegetable-124"))
                .body("topAvailableProducts[0].quantity", equalTo(800))
                .body("topSellingProducts.size", equalTo(0));
        response.prettyPrint();
    }

    @Step("Validating statistics of last month")
    public void validateStatisticsOfLastMonth() {
        response.then()
                .body("requestTimestamp", new IsZonedDateTime("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
                .body("timeSpan", equalTo("" + TimeSpan.LAST_MONTH.name() + ""))
                .body("topAvailableProducts.size", equalTo(2))
                .body("topAvailableProducts[0].id", equalTo("d081fc48-332b-46d8-8b22-45eb8e02cc04"))
                .body("topAvailableProducts[0].timestamp", equalTo("2019-04-24T18:05:24.000+0200"))
                .body("topAvailableProducts[0].productId", equalTo("vegetable-124"))
                .body("topAvailableProducts[0].quantity", equalTo(800))
                .body("topSellingProducts.size", equalTo(2))
                .body("topSellingProducts[0].productId", equalTo("vegetable-123"))
                .body("topSellingProducts[0].itemsSold", equalTo(205))
                .body("topSellingProducts[1].productId", equalTo("vegetable-122"))
                .body("topSellingProducts[1].itemsSold", equalTo(105));
        response.prettyPrint();
    }
}
