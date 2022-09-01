package controllers;

import documents.dto.*;
import entities.Good;
import io.micronaut.data.model.Page;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class DocumentReportControllerTest {
    @Inject
    @Client("/")
    HttpClient client;


    @Test
    public void testFindNonExistingGoodReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/report/99"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }
    @Test
    public void testGoodCrudOperations() {

        List<Long> goodIds = new ArrayList<>();

        List<Long> stockIds = new ArrayList<>();

        //подготавливаем товары и склады
        HttpRequest<?> request = HttpRequest.POST("/goods", Collections.singletonMap("name", "Laptop"));
        HttpResponse<?> response = client.toBlocking().exchange(request);
        goodIds.add(getCreatedGoodId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/goods", Collections.singletonMap("name", "DVD"));
        response = client.toBlocking().exchange(request);
        goodIds.add(getCreatedGoodId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/goods", Collections.singletonMap("name", "VHS"));
        response = client.toBlocking().exchange(request);
        goodIds.add(getCreatedGoodId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/goods", Collections.singletonMap("name", "PC Computer"));
        response = client.toBlocking().exchange(request);
        goodIds.add(getCreatedGoodId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/goods", Collections.singletonMap("name", "Macintosh One"));
        response = client.toBlocking().exchange(request);
        goodIds.add(getCreatedGoodId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/goods", Collections.singletonMap("name", "Nokia 3310"));
        response = client.toBlocking().exchange(request);
        goodIds.add(getCreatedGoodId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/goods", Collections.singletonMap("name", "Electronica IM-02"));
        response = client.toBlocking().exchange(request);
        goodIds.add(getCreatedGoodId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/goods", Collections.singletonMap("name", "Tetris"));
        response = client.toBlocking().exchange(request);
        goodIds.add(getCreatedGoodId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/stocks", Collections.singletonMap("name", "EastStock"));
        response = client.toBlocking().exchange(request);
        stockIds.add(getCreatedStockId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/stocks", Collections.singletonMap("name", "WestStock"));
        response = client.toBlocking().exchange(request);
        stockIds.add(getCreatedStockId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/stocks", Collections.singletonMap("name", "NorthStock"));
        response = client.toBlocking().exchange(request);
        stockIds.add(getCreatedStockId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/stocks", Collections.singletonMap("name", "SouthStock"));
        response = client.toBlocking().exchange(request);
        stockIds.add(getCreatedStockId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        //подготавливаем дто для информации по продуктам в документах, делаем по-простому
        List<GoodAmount> newGoods = new ArrayList<>(goodIds.size());
        for (Long goodId : goodIds) {
            int amount = 10;
            int price = 100;
            newGoods.add(new GoodAmount(goodId, amount, price));
        }
        //проверяем работоспособность документов по добавлению товаров на склады
        int docNum = 10;
        for (Long stockId : stockIds) {
            docNum = docNum + 10;
            request = HttpRequest.POST("/documents/addNewGoods", new NewGoodsDTO(docNum, stockId, newGoods));
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.OK, response.getStatus());

            request = HttpRequest.GET("/documents/newgoods?documentNumber=" + docNum);
            NewGoodsDTO newGoodsDTO = client.toBlocking().retrieve(request, NewGoodsDTO.class);
            assertEquals(stockId, newGoodsDTO.getStockId());
            assertEquals(docNum, newGoodsDTO.getDocumentNumber());
        }
        //проверяем работоспособность отчета по товарам после проведения документов по добавлению товаров
        request = HttpRequest.GET("/report/goods");
        Page<Good> page = client.toBlocking().retrieve(request, Page.class);
        assertEquals(page.getContent().size(), goodIds.size());
        //проверяем работоспособность отчета по остаткам товаров на складах после проведения документов по добавлению товаров
        request = HttpRequest.GET("/report/balance");
        GoodsBalance[] balancePage = client.toBlocking().retrieve(request, GoodsBalance[].class);
        assertEquals(balancePage.length, goodIds.size());
        assertEquals(balancePage[0].getBalance().size(), stockIds.size());
        assertEquals(10, balancePage[0].getBalance().get("EastStock"));
        assertEquals(10, balancePage[0].getBalance().get("WestStock"));
        assertEquals(10, balancePage[0].getBalance().get("NorthStock"));
        assertEquals(10, balancePage[0].getBalance().get("SouthStock"));

        //проверяем работоспособность документов по продаже товаров со складов
        List<GoodAmount> saleGoods = List.copyOf(newGoods);
        saleGoods.forEach(goodAmount -> goodAmount.setPrice(150));
        saleGoods.forEach(goodAmount -> goodAmount.setAmount(5));
        for (Long stockIdFrom : stockIds) {
            docNum = docNum + 10;
            request = HttpRequest.POST("/documents/saleGoods", new SaleGoodsDTO(docNum, stockIdFrom, saleGoods));
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.OK, response.getStatus());

            request = HttpRequest.GET("/documents/sales?documentNumber=" + docNum);
            SaleGoodsDTO saleGoodsDTO = client.toBlocking().retrieve(request, SaleGoodsDTO.class);
            assertEquals(stockIdFrom, saleGoodsDTO.getStockIdFrom());
            assertEquals(docNum, saleGoodsDTO.getDocumentNumber());

            long goodId = saleGoodsDTO.getSaleGoods().get(0).getGoodId();
            request = HttpRequest.GET("/goods/" + goodId);
            Good good = client.toBlocking().retrieve(request, Good.class);
            //проверяем работоспособность изменения цен покупки/продажи после проведения документов добавления/продажи
            assertEquals(100, good.getLastPurchasePrice());
            assertEquals(150, good.getLastSalePrice());
        }
        //проверяем работоспособность отчета по остаткам товаров на складах после проведения документов по перемещению товаров
        request = HttpRequest.GET("/report/balance");
        balancePage = client.toBlocking().retrieve(request, GoodsBalance[].class);
        assertEquals(balancePage.length, goodIds.size());
        assertEquals(balancePage[0].getBalance().size(), stockIds.size());
        assertEquals(5, balancePage[0].getBalance().get("EastStock"));
        assertEquals(5, balancePage[0].getBalance().get("WestStock"));
        assertEquals(5, balancePage[0].getBalance().get("NorthStock"));
        assertEquals(5, balancePage[0].getBalance().get("SouthStock"));

        //проверяем работоспособность документов по перемещению товаров с одних складов на другие
        //для этого делим склады на два списка, немного коряво, но пока как есть
        List<GoodAmount> moveGoods = List.copyOf(saleGoods);
        moveGoods.forEach(goodAmount -> goodAmount.setAmount(1));
        List<Long> stockIdFrom = stockIds.subList(0, stockIds.size() / 2);
        List<Long> stockIdTo = stockIds.subList(stockIds.size() / 2, stockIds.size());
        for (int i = 0; i < stockIdFrom.size(); i++) {
            docNum = docNum + 10;
            request = HttpRequest.POST("/documents/moveGoods", new MovingGoodsDTO(docNum, stockIdFrom.get(i), stockIdTo.get(i), moveGoods));
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.OK, response.getStatus());

            request = HttpRequest.GET("/documents/moves?documentNumber=" + docNum);
            MovingGoodsDTO movingGoodsDTO = client.toBlocking().retrieve(request, MovingGoodsDTO.class);
            assertEquals(stockIdFrom.get(i), movingGoodsDTO.getStockIdFrom());
            assertEquals(stockIdTo.get(i), movingGoodsDTO.getStockIdTo());
            assertEquals(docNum, movingGoodsDTO.getDocumentNumber());

            long goodId = movingGoodsDTO.getMovingGoods().get(0).getGoodId();
            request = HttpRequest.GET("/goods/" + goodId);
            Good good = client.toBlocking().retrieve(request, Good.class);
            //проверяем работоспособность изменения цен покупки/продажи после проведения документов добавления/продажи/перемещения
            assertEquals(100, good.getLastPurchasePrice());
            assertEquals(150, good.getLastSalePrice());
        }

        request = HttpRequest.GET("/report/balance");
        balancePage = client.toBlocking().retrieve(request, GoodsBalance[].class);
        assertEquals(balancePage.length, goodIds.size());
        assertEquals(balancePage[0].getBalance().size(), stockIds.size());
        assertEquals(4, balancePage[0].getBalance().get("EastStock"));
        assertEquals(4, balancePage[0].getBalance().get("WestStock"));
        assertEquals(6, balancePage[0].getBalance().get("NorthStock"));
        assertEquals(6, balancePage[0].getBalance().get("SouthStock"));

        request = HttpRequest.GET("/report/balance?stockName=" + "SouthStock");
        balancePage = client.toBlocking().retrieve(request, GoodsBalance[].class);
        assertEquals(1, balancePage[0].getBalance().size());
        assertEquals(6, balancePage[balancePage.length - 1].getBalance().get("SouthStock"));
        assertEquals(1, balancePage[balancePage.length - 1].getBalance().size());
    }

    protected Long getCreatedStockId(HttpResponse<?> response) {
        String path = "/stocks/";
        String value = response.header(HttpHeaders.LOCATION);
        if (value == null) {
            return null;
        }
        int index = value.indexOf(path);
        if (index != -1) {
            return Long.valueOf(value.substring(index + path.length()));
        }
        return null;
    }

    protected Long getCreatedGoodId(HttpResponse<?> response) {
        String path = "/goods/";
        String value = response.header(HttpHeaders.LOCATION);
        if (value == null) {
            return null;
        }
        int index = value.indexOf(path);
        if (index != -1) {
            return Long.valueOf(value.substring(index + path.length()));
        }
        return null;
    }
}
