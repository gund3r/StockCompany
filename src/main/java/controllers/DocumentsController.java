package controllers;

import documents.dto.MovingGoodsDTO;
import documents.dto.NewGoodsDTO;
import documents.dto.SaleGoodsDTO;
import exception.NoSuchDocumentException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import services.DocumentsService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ExecuteOn(TaskExecutors.IO)
@Controller("/documents")
public class DocumentsController {

    final Logger log = org.slf4j.LoggerFactory.getLogger(GoodController.class);

    final protected DocumentsService documentsService;

    @Inject
    public DocumentsController(DocumentsService documentsService) {
        this.documentsService = documentsService;
    }

    @Post("/addNewGoods")
    public HttpResponse addNewGoods(@Body @Valid NewGoodsDTO document){
        long stockId = document.getStockId();
        String success = String.format("Goods by document have been added to the stock with id:%s", stockId);
        String fail = String.format("Goods by document haven't been added to the stock with id:%s", stockId);
        return documentsService.addNewGoods(document) ? HttpResponse.status(HttpStatus.OK, success)
                : HttpResponse.status(HttpStatus.BAD_REQUEST, fail);
    }

    @Post("/saleGoods")
    public HttpResponse saleGoods(@Body @Valid SaleGoodsDTO document){
        long stockId = document.getStockIdFrom();
        String success = String.format("Goods by document have been saled from the stock with id:%s", stockId);
        String fail = String.format("Goods by document haven't been saled from the stock with id:%s", stockId);
        return documentsService.saleGoods(document) ? HttpResponse.status(HttpStatus.OK, success)
                : HttpResponse.status(HttpStatus.BAD_REQUEST, fail);
    }

    @Post("/moveGoods")
    public HttpResponse moveGoods(@Body @Valid MovingGoodsDTO document){
        long stockIdFrom = document.getStockIdFrom();
        long stockIdTo = document.getStockIdTo();
        String success = String.format("Goods by document have been transferred from the stock with id:%s" +
                " to stock with id:%s", stockIdFrom, stockIdTo);
        String fail = String.format("Goods by document haven't been transferred from the stock with id:%s" +
                " to stock with id:%s", stockIdFrom, stockIdTo);
        return documentsService.moveGoods(document) ? HttpResponse.status(HttpStatus.OK, success)
                : HttpResponse.status(HttpStatus.BAD_REQUEST, fail);
    }

    @Get("/newgoods")
    public NewGoodsDTO getNewGoodsDocByNumber(@NotNull Integer documentNumber) throws NoSuchDocumentException {
        return documentsService.getNewGoodsDocument(documentNumber);
    }

    @Get("/sales")
    public SaleGoodsDTO getSaleGoodsDocByNumber(@NotNull Integer documentNumber) throws NoSuchDocumentException {
        return documentsService.getSaleGoodsDocument(documentNumber);
    }

    @Get("/moves")
    public MovingGoodsDTO getMovingGoodsDocByNumber(@NotNull Integer documentNumber) throws NoSuchDocumentException {
        return documentsService.getMoveGoodsDocument(documentNumber);
    }


}
