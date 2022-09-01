package controllers;

import controllers.updateCommand.GoodUpdateCommand;
import entities.Good;
import entities.dao.GoodDAO;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import repositories.entityRepositories.GoodRepository;
import repositories.entityRepositories.StockGoodRepository;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@ExecuteOn(TaskExecutors.IO)
@Controller("/goods")
public class GoodController {

    final Logger log = org.slf4j.LoggerFactory.getLogger(GoodController.class);

    protected final GoodRepository goodRepository;
    protected final StockGoodRepository stockGoodRepository;

    @Inject
    public GoodController(GoodRepository goodRepository, StockGoodRepository stockGoodRepository) {
        this.goodRepository = goodRepository;
        this.stockGoodRepository = stockGoodRepository;
    }

    @Get("/{id}")
    public Optional<Good> getGoodById(Long id) {
        return goodRepository
                .findById(id);
    }

    @Put
    public HttpResponse update(@Body @Valid GoodUpdateCommand command) {
        goodRepository.update(command.getId(), command.getName());
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath());
    }

    @Get("/list")
    public List<Good> list(@Valid Pageable pageable) {
        return goodRepository.findAll(pageable).getContent();
    }

    @Post
    public HttpResponse<Good> save(@Body("name") @NotBlank String name) {
        GoodDAO goodDAO = new GoodDAO(name);

        Good good = goodRepository.save(goodDAO.getVendorCode(), goodDAO.getName(),
                goodDAO.getLastPurchasePrice(), goodDAO.getLastSalePrice());

        return HttpResponse
                .created(good)
                .headers(headers -> headers.location(location(good.getId())));
    }

    @Post("/ex")
    public HttpResponse<Good> saveExceptions(@Body("name") @NotBlank String name) {
        try {
            GoodDAO goodDAO = new GoodDAO(name);

            Good good = goodRepository.saveWithException(goodDAO.getVendorCode(), goodDAO.getName(),
                    goodDAO.getLastPurchasePrice(), goodDAO.getLastSalePrice());

            return HttpResponse
                    .created(good)
                    .headers(headers -> headers.location(location(good.getId())));
        } catch(DataAccessException e) {
            return HttpResponse.noContent();
        }
    }

    @Delete("/{id}")
    @Status(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        //если удаляем товар, то и удаляем его на всех складах
        stockGoodRepository.deleteAllByGoodId(id);
        goodRepository.deleteById(id);
    }

    protected URI location(Long id) {
        return URI.create("/goods/" + id);
    }

    protected URI location(Good good) {
        return location(good.getId());
    }

}
