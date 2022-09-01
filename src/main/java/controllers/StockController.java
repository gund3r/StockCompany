package controllers;

import controllers.updateCommand.StockUpdateCommand;
import entities.Stock;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.slf4j.Logger;
import repositories.entityRepositories.StockGoodRepository;
import repositories.entityRepositories.StockRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO)
@Controller("/stocks")
public class StockController {

    final Logger log = org.slf4j.LoggerFactory.getLogger(StockController.class);

    protected final StockRepository stockRepository;
    protected final StockGoodRepository stockGoodRepository;

    public StockController(StockRepository stockRepository, StockGoodRepository stockGoodRepository) {
        this.stockRepository = stockRepository;
        this.stockGoodRepository = stockGoodRepository;
    }

    @Get("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Nice to meet you at the StockCompany console";
    }


    @Get("/{id}")
    public Optional<Stock> getStockById(Long id) {
        return stockRepository
                .findById(id);
    }

    @Put
    public HttpResponse update(@Body @Valid StockUpdateCommand command) {
        stockRepository.update(command.getId(), command.getName());
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }

    @Get("/list")
    public List<Stock> list(@Valid Pageable pageable) {
        return stockRepository.findAll(pageable).getContent();
    }

    @Post
    public HttpResponse<Stock> save(@Body("name") @NotBlank String name) {
        Stock stock = stockRepository.save(name);

        return HttpResponse
                .created(stock)
                .headers(headers -> headers.location(location(stock.getId())));
    }

    @Post("/ex")
    public HttpResponse<Stock> saveExceptions(@Body @NotBlank String name) {
        try {
            Stock stock = stockRepository.saveWithException(name);
            return HttpResponse
                    .created(stock)
                    .headers(headers -> headers.location(location(stock.getId())));
        } catch(DataAccessException e) {
            return HttpResponse.noContent();
        }
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        //если удаляем склад, то и удаляем все товары из него
        stockGoodRepository.deleteAllByStockId(id);
        stockRepository.deleteById(id);
    }

    protected URI location(Long id) {
        return URI.create("/stocks/" + id);
    }

    protected URI location(Stock stock) {
        return location(stock.getId());
    }

}

