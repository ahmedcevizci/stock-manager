package info.alaz.stock.manager.service.impl;

import info.alaz.stock.manager.config.StockManagerProperties;
import info.alaz.stock.manager.dto.ProductResponseDto;
import info.alaz.stock.manager.dto.StatisticsResponseDto;
import info.alaz.stock.manager.dto.TimeSpan;
import info.alaz.stock.manager.entity.ProductEntity;
import info.alaz.stock.manager.entity.StockEntity;
import info.alaz.stock.manager.entity.StockEventEntity;
import info.alaz.stock.manager.entity.TopSellingProductEntity;
import info.alaz.stock.manager.exception.*;
import info.alaz.stock.manager.mapper.ProductMapper;
import info.alaz.stock.manager.mapper.ProductMapperImpl;
import info.alaz.stock.manager.repository.ProductRepository;
import info.alaz.stock.manager.repository.StockEventRepository;
import info.alaz.stock.manager.repository.StockRepository;
import info.alaz.stock.manager.repository.TopSellingEntityRepository;
import info.alaz.stock.manager.util.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static info.alaz.stock.manager.TestObjectCreator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {StockManagerServiceImpl.class})
public class StockManagerServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockEventRepository stockEventRepository;

    @Mock
    TopSellingEntityRepository topSellingEntityRepository;

    @Spy
    private StockManagerProperties stockManagerProperties = new StockManagerProperties(3, 3);

    @Spy
    private ProductMapper productMapper = new ProductMapperImpl();

    @Captor
    private ArgumentCaptor<StockEventEntity> stockEventEntityArgumentCaptor;

    @InjectMocks
    private StockManagerServiceImpl stockManagerService;


    /*void updateStock(UUID stockUuid, String productId, Integer newQuantity, ZonedDateTime updateTimestamp)
            throws StockNotFoundException, ProductNotFoundException, StockNotBelongToProductException,
            StockUpdateTimestampCannotBeInFutureException, StockHasBeenUpdatedBeforeException, OptimisticLockException;

    ProductResponseDto getStockOfProduct(String productId, ZonedDateTime requestTimestamp) throws ProductNotFoundException;

    StatisticsResponseDto calculateStatistics(TimeSpan timeSpan, ZonedDateTime requestTimestamp)*/

    @Test
    public void updateStock_GivenValidStockId_And_GivenValidProductId_And_GivenValidQuantity_And_GivenValidUpdateDate_ShouldPass() throws StockHasBeenUpdatedBeforeException {
        //Given
        ProductEntity productEntity = new ProductEntity(EXISTING_PRODUCT_ID1, ZonedDateTime.now().minusMonths(1), null);
        ZonedDateTime oldUpdateTimestamp = ZonedDateTime.now().minusMinutes(60);
        StockEntity oldStockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 500, oldUpdateTimestamp, null);
        ZonedDateTime newUpdateTimestamp = ZonedDateTime.now().minusMinutes(30);
        StockEntity newStockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 100, oldUpdateTimestamp, null);

        when(productRepository.findOne(EXISTING_PRODUCT_ID1)).thenReturn(productEntity);
        when(stockRepository.findOne(EXISTING_STOCK_ID1)).thenReturn(oldStockEntity);
        when(stockRepository.findByIdAndProduct_Name(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1)).thenReturn(oldStockEntity);
        when(stockRepository.save(newStockEntity)).thenReturn(newStockEntity);

        //When
        stockManagerService.updateStock(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1, 100, newUpdateTimestamp);

        //Then
        verify(productRepository, times(1)).findOne(EXISTING_PRODUCT_ID1);
        verify(stockRepository, times(1)).findOne(EXISTING_STOCK_ID1);
        verify(stockRepository, times(1)).save(newStockEntity);
        verify(stockEventRepository, times(1)).save(stockEventEntityArgumentCaptor.capture());

        assertEquals(new Integer(-400), stockEventEntityArgumentCaptor.getValue().getQuantityChange());
        assertEquals(newStockEntity, stockEventEntityArgumentCaptor.getValue().getStock());
        assertNotNull(stockEventEntityArgumentCaptor.getValue().getDateChanged());
    }

    @Test
    public void updateStock_GivenValidStockId_And_GivenValidProductId_And_GivenQuantityZero_And_GivenValidUpdateDate_ShouldPass() throws StockHasBeenUpdatedBeforeException {
        //Given
        ProductEntity productEntity = new ProductEntity(EXISTING_PRODUCT_ID1, ZonedDateTime.now().minusMonths(1), null);
        ZonedDateTime oldUpdateTimestamp = ZonedDateTime.now().minusMinutes(60);
        StockEntity oldStockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 500, oldUpdateTimestamp, null);
        ZonedDateTime newUpdateTimestamp = ZonedDateTime.now().minusMinutes(30);
        StockEntity newStockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 0, oldUpdateTimestamp, null);

        when(productRepository.findOne(EXISTING_PRODUCT_ID1)).thenReturn(productEntity);
        when(stockRepository.findOne(EXISTING_STOCK_ID1)).thenReturn(oldStockEntity);
        when(stockRepository.findByIdAndProduct_Name(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1)).thenReturn(oldStockEntity);
        when(stockRepository.save(newStockEntity)).thenReturn(newStockEntity);

        //When
        stockManagerService.updateStock(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1, 0, newUpdateTimestamp);

        //Then
        verify(productRepository, times(1)).findOne(EXISTING_PRODUCT_ID1);
        verify(stockRepository, times(1)).findOne(EXISTING_STOCK_ID1);
        verify(stockRepository, times(1)).save(newStockEntity);
        verify(stockEventRepository, times(1)).save(stockEventEntityArgumentCaptor.capture());

        assertEquals(new Integer(-500), stockEventEntityArgumentCaptor.getValue().getQuantityChange());
        assertEquals(newStockEntity, stockEventEntityArgumentCaptor.getValue().getStock());
        assertNotNull(stockEventEntityArgumentCaptor.getValue().getDateChanged());
    }

    @Test(expected = StockUpdateTimestampCannotBeInFutureException.class)
    public void updateStock_GivenValidStockId_And_GivenValidProductId_And_GivenValidQuantity_And_GivenInvalidUpdateDate_ShouldThrow_StockUpdateTimestampCannotBeInFutureException() throws StockHasBeenUpdatedBeforeException {
        //Given
        ZonedDateTime newUpdateTimestampInFuture = ZonedDateTime.now().plusMinutes(30);

        //When
        stockManagerService.updateStock(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1, 100, newUpdateTimestampInFuture);

        //Then
        //Should throw exception
    }

    @Test(expected = InvalidQuantityException.class)
    public void updateStock_GivenValidStockId_And_GivenValidProductId_And_GivenInvalidQuantity_And_GivenValidUpdateDate_ShouldThrow_InvalidQuantityException() throws StockHasBeenUpdatedBeforeException {
        //Given
        ZonedDateTime newUpdateTimestamp = ZonedDateTime.now().minusMinutes(30);

        //When
        stockManagerService.updateStock(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1, -100, newUpdateTimestamp);

        //Then
        //Should throw exception
    }

    @Test(expected = ProductNotFoundException.class)
    public void updateStock_GivenValidStockId_And_GivenInvalidProductId_And_GivenValidQuantity_And_GivenValidUpdateDate_ShouldThrow_ProductNotFoundException() throws StockHasBeenUpdatedBeforeException {
        //Given
        ZonedDateTime newUpdateTimestamp = ZonedDateTime.now().minusMinutes(30);

        when(productRepository.findOne(NOT_EXISTING_PRODUCT_ID)).thenReturn(null);

        //When
        stockManagerService.updateStock(EXISTING_STOCK_ID1, NOT_EXISTING_PRODUCT_ID, 100, newUpdateTimestamp);

        //Then
        //Should throw exception
    }

    @Test(expected = StockNotFoundException.class)
    public void updateStock_GivenInvalidStockId_And_GivenValidProductId_And_GivenValidQuantity_And_GivenValidUpdateDate_ShouldThrow_StockNotFoundException() throws StockHasBeenUpdatedBeforeException {
        //Given
        ProductEntity productEntity = new ProductEntity(EXISTING_PRODUCT_ID1, ZonedDateTime.now().minusMonths(1), null);
        ZonedDateTime newUpdateTimestamp = ZonedDateTime.now().minusMinutes(30);

        when(productRepository.findOne(EXISTING_PRODUCT_ID1)).thenReturn(productEntity);
        when(stockRepository.findOne(NOT_EXISTING_STOCK_ID)).thenReturn(null);

        //When
        stockManagerService.updateStock(NOT_EXISTING_STOCK_ID, EXISTING_PRODUCT_ID1, 100, newUpdateTimestamp);

        //Then
        //Should throw exception
    }

    @Test(expected = StockNotBelongToProductException.class)
    public void updateStock_GivenValidParameters_And_GivenProductStockMismatch_ShouldThrow_StockNotBelongToProductException() throws StockHasBeenUpdatedBeforeException {
        //Given
        ProductEntity productEntity1 = new ProductEntity(EXISTING_PRODUCT_ID1, ZonedDateTime.now().minusMonths(1), null);
        ProductEntity productEntity2 = new ProductEntity(EXISTING_PRODUCT_ID2, ZonedDateTime.now().minusMonths(1), null);
        ZonedDateTime oldUpdateTimestamp = ZonedDateTime.now().minusMinutes(60);
        StockEntity oldStockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity2, 500, oldUpdateTimestamp, null);
        ZonedDateTime newUpdateTimestamp = ZonedDateTime.now().minusMinutes(30);

        when(productRepository.findOne(EXISTING_PRODUCT_ID1)).thenReturn(productEntity1);
        when(stockRepository.findOne(EXISTING_STOCK_ID1)).thenReturn(oldStockEntity);

        //When
        stockManagerService.updateStock(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1, 100, newUpdateTimestamp);

        //Then
        //Should throw exception
    }

    @Test(expected = StockHasBeenUpdatedBeforeException.class)
    public void updateStock_GivenValidParameters_And_GivenNewUpdateDateBeforeOldUpdateDate_ShouldThrow_StockHasBeenUpdatedBeforeException() throws StockHasBeenUpdatedBeforeException {
        //Given
        ProductEntity productEntity = new ProductEntity(EXISTING_PRODUCT_ID1, ZonedDateTime.now().minusMonths(1), null);
        ZonedDateTime oldUpdateTimestamp = ZonedDateTime.now().minusMinutes(1);
        StockEntity oldStockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 500, oldUpdateTimestamp, null);
        ZonedDateTime newUpdateTimestamp = ZonedDateTime.now().minusMinutes(30);
        StockEntity newStockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 100, oldUpdateTimestamp, null);

        when(productRepository.findOne(EXISTING_PRODUCT_ID1)).thenReturn(productEntity);
        when(stockRepository.findOne(EXISTING_STOCK_ID1)).thenReturn(oldStockEntity);
        when(stockRepository.findByIdAndProduct_Name(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1)).thenReturn(oldStockEntity);
        when(stockRepository.save(newStockEntity)).thenReturn(newStockEntity);

        //When
        stockManagerService.updateStock(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1, 100, newUpdateTimestamp);

        //Then
        //Should throw exception
    }

    @Test
    public void getStockOfProduct_GivenValidProductId_ShouldPass() {
        //Given
        Set<StockEntity> stockEntitySet = new HashSet<>();
        ProductEntity productEntity = new ProductEntity(EXISTING_PRODUCT_ID1, ZonedDateTime.now().minusMonths(1), stockEntitySet);
        ZonedDateTime stockUpdateTimestamp = ZonedDateTime.now().minusMinutes(60);
        StockEntity stockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 500, stockUpdateTimestamp, null);
        stockEntitySet.add(stockEntity);
        ZonedDateTime requestTimestamp = ZonedDateTime.now();

        when(productRepository.findOne(EXISTING_PRODUCT_ID1)).thenReturn(productEntity);

        //When
        ProductResponseDto productResponseDto = stockManagerService.getStockOfProduct(EXISTING_PRODUCT_ID1, requestTimestamp);

        //Then
        verify(productRepository, times(1)).findOne(EXISTING_PRODUCT_ID1);
        verify(productMapper, times(1)).toDto(productEntity, stockEntity, requestTimestamp);

        assertEquals(EXISTING_PRODUCT_ID1, productResponseDto.getProductId());
        assertEquals(requestTimestamp, productResponseDto.getRequestTimestamp());
        assertEquals(EXISTING_STOCK_ID1, productResponseDto.getStock().getId());
        assertEquals(new Integer(500), productResponseDto.getStock().getQuantity());
        assertEquals(stockUpdateTimestamp, productResponseDto.getStock().getTimestamp());
    }

    @Test(expected = ProductNotFoundException.class)
    public void getStockOfProduct_GivenInvalidProductId_ShouldThrow_ProductNotFoundException() {
        //Given
        when(productRepository.findOne(NOT_EXISTING_PRODUCT_ID)).thenReturn(null);

        //When
        stockManagerService.getStockOfProduct(NOT_EXISTING_PRODUCT_ID, ZonedDateTime.now());

        //Then
    }

    @Test
    public void calculateStatistics_GivenTodayTimeSpan_ShouldPass() {
        //Given
        ProductEntity productEntity = new ProductEntity(EXISTING_PRODUCT_ID1, ZonedDateTime.now().minusMonths(1), null);
        StockEntity stockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 500, ZonedDateTime.now().minusMinutes(1), null);

        ZonedDateTime requestTimestamp = ZonedDateTime.now();

        TopSellingProductEntity topSellingProductEntity = new TopSellingProductEntity(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1, 200);
        Set<TopSellingProductEntity> topSellingProductEntitySet = new HashSet<>(1);
        topSellingProductEntitySet.add(topSellingProductEntity);
        ZonedDateTime midnight = DateUtil.getStartDateOfTimeSpanFrom(TimeSpan.TODAY, requestTimestamp);
        when(topSellingEntityRepository.findTopSellingProducts(midnight, 3)).thenReturn(topSellingProductEntitySet);

        List<StockEntity> stockEntityList = new ArrayList<>(1);
        stockEntityList.add(stockEntity);
        Page<StockEntity> stockEntityPage = mock(Page.class);
        when(stockRepository.findAll(any(PageRequest.class))).thenReturn(stockEntityPage);
        when(stockEntityPage.getContent()).thenReturn(stockEntityList);


        //When
        StatisticsResponseDto statisticsResponseDto = stockManagerService.calculateStatistics(TimeSpan.TODAY, requestTimestamp);

        //Then
        verify(topSellingEntityRepository, times(1)).findTopSellingProducts(midnight, 3);
        verify(stockRepository, times(1)).findAll(any(PageRequest.class));
        verify(stockManagerProperties, times(1)).getTopAvailableProductCountToShow();
        verify(stockManagerProperties, times(1)).getTopSellingProductCountToShow();

        assertEquals(TimeSpan.TODAY, statisticsResponseDto.getTimeSpan());
        assertEquals(requestTimestamp, statisticsResponseDto.getRequestTimestamp());
        assertEquals(1, statisticsResponseDto.getTopAvailableProducts().size());
        assertEquals(EXISTING_STOCK_ID1, statisticsResponseDto.getTopAvailableProducts().iterator().next().getId());
        assertEquals(1, statisticsResponseDto.getTopSellingProducts().size());
        assertEquals(EXISTING_PRODUCT_ID1, statisticsResponseDto.getTopSellingProducts().iterator().next().getProductId());
    }

    @Test
    public void calculateStatistics_GivenLastMonthTimeSpan_ShouldPass() {
        //Given
        ProductEntity productEntity = new ProductEntity(EXISTING_PRODUCT_ID1, ZonedDateTime.now().minusMonths(1), null);
        StockEntity stockEntity = new StockEntity(EXISTING_STOCK_ID1, productEntity, 500, ZonedDateTime.now().minusMinutes(1), null);

        ZonedDateTime requestTimestamp = ZonedDateTime.now();

        TopSellingProductEntity topSellingProductEntity = new TopSellingProductEntity(EXISTING_STOCK_ID1, EXISTING_PRODUCT_ID1, 200);
        Set<TopSellingProductEntity> topSellingProductEntitySet = new HashSet<>(1);
        topSellingProductEntitySet.add(topSellingProductEntity);
        ZonedDateTime midnight = DateUtil.getStartDateOfTimeSpanFrom(TimeSpan.LAST_MONTH, requestTimestamp);
        when(topSellingEntityRepository.findTopSellingProducts(midnight, 3)).thenReturn(topSellingProductEntitySet);

        List<StockEntity> stockEntityList = new ArrayList<>(1);
        stockEntityList.add(stockEntity);
        Page<StockEntity> stockEntityPage = mock(Page.class);
        when(stockRepository.findAll(any(PageRequest.class))).thenReturn(stockEntityPage);
        when(stockEntityPage.getContent()).thenReturn(stockEntityList);


        //When
        StatisticsResponseDto statisticsResponseDto = stockManagerService.calculateStatistics(TimeSpan.LAST_MONTH, requestTimestamp);

        //Then
        verify(topSellingEntityRepository, times(1)).findTopSellingProducts(midnight, 3);
        verify(stockRepository, times(1)).findAll(any(PageRequest.class));
        verify(stockManagerProperties, times(1)).getTopAvailableProductCountToShow();
        verify(stockManagerProperties, times(1)).getTopSellingProductCountToShow();

        assertEquals(TimeSpan.LAST_MONTH, statisticsResponseDto.getTimeSpan());
        assertEquals(requestTimestamp, statisticsResponseDto.getRequestTimestamp());
        assertEquals(1, statisticsResponseDto.getTopAvailableProducts().size());
        assertEquals(EXISTING_STOCK_ID1, statisticsResponseDto.getTopAvailableProducts().iterator().next().getId());
        assertEquals(1, statisticsResponseDto.getTopSellingProducts().size());
        assertEquals(EXISTING_PRODUCT_ID1, statisticsResponseDto.getTopSellingProducts().iterator().next().getProductId());
    }
}