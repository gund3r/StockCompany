package controllers;

import controllers.updateCommand.GoodUpdateCommand;
import entities.Good;
import io.micronaut.core.type.Argument;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class GoodControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testFindNonExistingGoodReturns404() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/goods/99"));
        });

        assertNotNull(thrown.getResponse());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }

    @Test
    public void testGoodCrudOperations() {

        List<Long> GoodIds = new ArrayList<>();

        HttpRequest<?> request = HttpRequest.POST("/goods", Collections.singletonMap("name", "Laptop"));
        HttpResponse<?> response = client.toBlocking().exchange(request);
        GoodIds.add(entityId(response));
        assertEquals(HttpStatus.CREATED, response.getStatus());

        request = HttpRequest.POST("/goods", Collections.singletonMap("name", "DVD"));
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.CREATED, response.getStatus());

        Long id = entityId(response);
        GoodIds.add(id);
        request = HttpRequest.GET("/goods/" + id);
        Good good = client.toBlocking().retrieve(request, Good.class);
        assertEquals("DVD", good.getName());

        request = HttpRequest.PUT("/goods", new GoodUpdateCommand(id, "VHS"));
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/goods/" + id);
        good = client.toBlocking().retrieve(request, Good.class);
        assertEquals("VHS", good.getName());

        request = HttpRequest.GET("/goods/list");
        List<Good> goodList = client.toBlocking().retrieve(request, Argument.of(List.class, Good.class));
        assertEquals(2, goodList.size());

        request = HttpRequest.POST("/goods/ex", Collections.singletonMap("name", "Laptop"));
        response = client.toBlocking().exchange(request);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatus());

        request = HttpRequest.GET("/goods/list");
        goodList = client.toBlocking().retrieve(request, Argument.of(List.class, Good.class));
        assertEquals(2, goodList.size());

        request = HttpRequest.GET("/goods/list?size=1");
        goodList = client.toBlocking().retrieve(request, Argument.of(List.class, Good.class));
        assertEquals(1, goodList.size());
        assertEquals("Laptop", goodList.get(0).getName());

        request = HttpRequest.GET("/goods/list?size=1&sort=name,desc");
        goodList = client.toBlocking().retrieve(request, Argument.of(List.class, Good.class));
        assertEquals(1, goodList.size());
        assertEquals("VHS", goodList.get(0).getName());

        request = HttpRequest.GET("/goods/list?size=1&page=2");
        goodList = client.toBlocking().retrieve(request, Argument.of(List.class, Good.class));
        assertEquals(0, goodList.size());

        // cleanup:
        for (Long goodId : GoodIds) {
            request = HttpRequest.DELETE("/goods/" + goodId);
            response = client.toBlocking().exchange(request);
            assertEquals(HttpStatus.NO_CONTENT, response.getStatus());
        }
    }

    protected Long entityId(HttpResponse<?> response) {
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
