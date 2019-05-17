package info.alaz.stock.manager.e2e.restful;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import info.alaz.stock.manager.dto.TimeSpan;
import info.alaz.stock.manager.dto.restful.StockUpdateRequestDto;
import info.alaz.stock.manager.util.DateUtil;
import net.serenitybdd.junit.spring.integration.SpringIntegrationSerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;

import static info.alaz.stock.manager.TestObjectCreator.*;

@RunWith(SpringIntegrationSerenityRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.yml")
@ActiveProfiles("test")
public class RestfulStockManagerE2e {

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("src/test/resources/docker-compose.yml"))
                    .withExposedService("postgresql-db", 5432);

    private static ObjectMapper objectMapper;

    @Steps
    private RestfulStockManagerSteps restfulStockManagerSteps;

    @LocalServerPort
    private int port;

    @BeforeClass
    public static void setupClass() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, true);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat(DateUtil.DATE_FORMAT_PATTERN));
    }

    @Before
    public void setup() {
        restfulStockManagerSteps.setup(port);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_update_stock_of_a_product() throws Exception {
        //Given
        StockUpdateRequestDto stockUpdateRequestDto = new StockUpdateRequestDto(ZonedDateTime.now().minusMinutes(30), 120);

        //When
        restfulStockManagerSteps.updateStockOfProduct(EXISTING_PRODUCT_ID1, EXISTING_STOCK_ID1, stockUpdateRequestDto);

        //Then
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.NO_CONTENT);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_update_stock_of_a_product_with_old_update_timestamp_and_get_409() throws Exception {
        //Given
        StockUpdateRequestDto stockUpdateRequestDto = new StockUpdateRequestDto(ZonedDateTime.now().minusMonths(1), 120);

        //When
        restfulStockManagerSteps.updateStockOfProduct(EXISTING_PRODUCT_ID1, EXISTING_STOCK_ID1, stockUpdateRequestDto);

        //Then
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.CONFLICT);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_update_stock_of_a_product_with_unknown_product_id_and_get_404() throws Exception {
        //Given
        StockUpdateRequestDto stockUpdateRequestDto = new StockUpdateRequestDto(ZonedDateTime.now().minusMonths(1), 120);

        //When
        restfulStockManagerSteps.updateStockOfProduct(NOT_EXISTING_PRODUCT_ID, EXISTING_STOCK_ID1, stockUpdateRequestDto);

        //Then
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_update_stock_of_a_product_with_unknown_stock_id_and_get_404() throws Exception {
        //Given
        StockUpdateRequestDto stockUpdateRequestDto = new StockUpdateRequestDto(ZonedDateTime.now().minusMonths(1), 120);

        //When
        restfulStockManagerSteps.updateStockOfProduct(EXISTING_PRODUCT_ID1, NOT_EXISTING_STOCK_ID, stockUpdateRequestDto);

        //Then
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_update_stock_of_a_product_with_mismatched_product_id_stock_id_and_get_404() throws Exception {
        //Given
        StockUpdateRequestDto stockUpdateRequestDto = new StockUpdateRequestDto(ZonedDateTime.now().minusMonths(1), 120);

        //When
        restfulStockManagerSteps.updateStockOfProduct(EXISTING_PRODUCT_ID1, EXISTING_STOCK_ID2, stockUpdateRequestDto);

        //Then
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_get_current_stock_of_a_product() throws Exception {
        //Given

        //When
        restfulStockManagerSteps.getStockOfAProduct(EXISTING_PRODUCT_ID1);

        //Then
        restfulStockManagerSteps.validateProduct(EXISTING_PRODUCT_ID1, EXISTING_STOCK_ID1, "2019-04-21T15:05:21.000+0200", 500);
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.OK);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_get_current_stock_of_a_product_with_unknown_product_id_and_get_404() throws Exception {
        //Given

        //When
        restfulStockManagerSteps.getStockOfAProduct(NOT_EXISTING_PRODUCT_ID);

        //Then
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.NOT_FOUND);
    }


    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_get_statistics_of_today_about_products_in_stock() throws Exception {
        //Given

        //When
        restfulStockManagerSteps.getStatisticsAboutProductsInStock(TimeSpan.TODAY);

        //Then
        restfulStockManagerSteps.validateStatisticsOfToday();
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.OK);
    }

    @Test
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:testscripts/fillDB.sql")
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:testscripts/clearDB.sql")
    public void as_user_I_want_to_get_statistics_of_last_month_about_products_in_stock() throws Exception {
        //Given

        //When
        restfulStockManagerSteps.getStatisticsAboutProductsInStock(TimeSpan.LAST_MONTH);

        //Then
        restfulStockManagerSteps.validateHttpStatus(HttpStatus.OK);
        restfulStockManagerSteps.validateStatisticsOfLastMonth();
    }

}
