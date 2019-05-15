package info.alaz.stock.manager.service.impl;

import info.alaz.stock.manager.config.StockManagerProperties;
import info.alaz.stock.manager.repository.ProductRepository;
import info.alaz.stock.manager.repository.StockEventRepository;
import info.alaz.stock.manager.repository.StockRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = {StockManagerServiceImpl.class})
public class StockManagerServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockEventRepository stockEventRepository;

    @Spy
    private StockManagerProperties stockManagerProperties = new StockManagerProperties(3, 3);

    /*@Spy
    private StockMapper stockMapper = new StockMapperImpl();

    @Captor
    private ArgumentCaptor<StockEntity> stockEntityArgumentCaptor;*/

    @InjectMocks
    private StockManagerServiceImpl stockManagerService;


    @Test
    public void test1() {
/*        //Given
        ZonedDateTime dateStatusChanged = ZonedDateTime.now();
        ZonedDateTime dateCreated = dateStatusChanged.minusDays(2);
        StockEntity stockEntity = new StockEntity();
        when(stockRepository.findOne(EXISTING_STOCK_ID2)).thenReturn(sensorEntity);

        //When
        stockManagerService.updateStock(UUID stockUuid, String productId, Integer newQuantity, ZonedDateTime updateTimestamp);

        //Then
        verify(stockRepository, times(1)).findOne(EXISTING_STOCK_ID2);

        verify(stockRepository, times(1)).save(stockEntityArgumentCaptor.capture());
        assertEquals(stockUuid, measurementEntityArgumentCaptor.getValue().getDateMeasured());*/
    }

}