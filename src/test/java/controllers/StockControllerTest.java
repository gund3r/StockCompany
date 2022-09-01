package controllers;

import controllers.updateCommand.StockUpdateCommand;
import entities.Stock;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class StockControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testHello() {
        HttpRequest<String> request = HttpRequest.GET("/stocks/hello");
        String body = client.toBlocking().retrieve(request);

        assertNotNull(body);
        assertEquals("Nice to meet you at the StockCompany console", body);
    }

    @Test
    public void testFindNonExistingGenreReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/stocks/99"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    public void testStockCrudOperations() {

        List<Long> stockIds = new ArrayList<>();

        HttpRequest<?> request = HttpRequest.POST("/stocks", Collections.singletonMap("name", "EastStock"));
        HttpResponse<?> response = client.toBlocking().exchange(request);
        stockIds.add(entityId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/stocks", Collections.singletonMap("name", "WestStock"));
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        Long id = entityId(response);
        stockIds.add(id);
        request = HttpRequest.GET("/stocks/" + id);
        Stock stock = client.toBlocking().retrieve(request, Stock.class);
        assertEquals("WestStock", stock.getName());

        request = HttpRequest.PUT("/stocks", new StockUpdateCommand(id, "NorthStock"));
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/stocks/" + id);
        stock = client.toBlocking().retrieve(request, Stock.class);
        assertEquals("NorthStock", stock.getName());

        request = HttpRequest.GET("/stocks/list");
        List<Stock> stocks = client.toBlocking().retrieve(request, Argument.of(List.class, Stock.class));
        assertEquals(2, stocks.size());

        request = HttpRequest.POST("/stocks/ex", Collections.singletonMap("name", "WestStock"));
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/stocks/list");
        stocks = client.toBlocking().retrieve(request, Argument.of(List.class, Stock.class));
        assertEquals(2, stocks.size());

        request = HttpRequest.GET("/stocks/list?size=1");
        stocks = client.toBlocking().retrieve(request, Argument.of(List.class, Stock.class));
        assertEquals(1, stocks.size());
        assertEquals("EastStock", stocks.get(0).getName());

        request = HttpRequest.GET("/stocks/list?size=1&sort=name,desc");
        stocks = client.toBlocking().retrieve(request, Argument.of(List.class, Stock.class));
        assertEquals(1, stocks.size());
        assertEquals("NorthStock", stocks.get(0).getName());

        request = HttpRequest.GET("/stocks/list?size=1&page=2");
        stocks = client.toBlocking().retrieve(request, Argument.of(List.class, Stock.class));
        assertEquals(0, stocks.size());

        // cleanup:
        for (Long genreId : stockIds) {
            request = HttpRequest.DELETE("/stocks/" + genreId);
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        }
    }

    protected Long entityId(HttpResponse<?> response) {
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

}
