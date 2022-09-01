package controllers;

import documents.dto.GoodsBalance;
import entities.Good;
import entities.StockGood;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import repositories.entityRepositories.GoodRepository;
import repositories.entityRepositories.StockGoodRepository;
import services.ReportService;

import java.net.URI;
import java.util.List;

@ExecuteOn(TaskExecutors.IO)
@Controller("/report")
public class ReportController {

    final Logger log = org.slf4j.LoggerFactory.getLogger(ReportController.class);
    protected final StockGoodRepository stockGoodRepository;
    protected final GoodRepository goodRepository;
    protected final ReportService reportService;

    @Inject
    public ReportController(StockGoodRepository stockGoodRepository, GoodRepository goodRepository,
                            ReportService reportService) {
        this.stockGoodRepository = stockGoodRepository;
        this.goodRepository = goodRepository;
        this.reportService = reportService;
    }

    @Get("/goods")
    public Page<Good> getGoods(@Nullable Long goodId, @Nullable Pageable pageable) {
        if (goodId != null) {
            return goodRepository.findById(goodId, pageable);
        } else {
            return goodRepository.findAll(pageable);
        }
    }

    @Get("/balance")
    public List<GoodsBalance> getGoodsBalance(@Nullable String stockName) {
        return reportService.getGoodsBalance(stockName);
    }

    protected URI location(Long id) {
        return URI.create("/report/" + id);
    }

    protected URI location(StockGood stockGood) {
        return location(stockGood.getId());
    }


}
