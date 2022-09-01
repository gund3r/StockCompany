package services;

import documents.dto.GoodsBalance;
import entities.Stock;
import entities.StockGood;
import exception.NoSuchStockException;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import repositories.entityRepositories.GoodRepository;
import repositories.entityRepositories.StockGoodRepository;
import repositories.entityRepositories.StockRepository;

import javax.transaction.Transactional;
import java.util.*;

@Singleton
public class ReportService {

    final Logger log = org.slf4j.LoggerFactory.getLogger(ReportService.class);
    protected final GoodRepository goodRepository;
    protected final StockGoodRepository stockGoodRepository;

    protected final StockRepository stockRepository;

    @Inject
    public ReportService(GoodRepository goodRepository, StockGoodRepository stockGoodRepository,
                         StockRepository stockRepository) {
        this.goodRepository = goodRepository;
        this.stockGoodRepository = stockGoodRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public List<GoodsBalance> getGoodsBalance(@Nullable String stockName) {
        List<GoodsBalance> result = new ArrayList<>();
        try {
            goodRepository.findAll()
                    .forEach(good -> result.add(new GoodsBalance(good.getId(), good.getVendorCode(), good.getName())));
            if (stockName == null) {
                for (GoodsBalance item : result) {
                    Iterator<StockGood> iterator = stockGoodRepository.findAllByGoodId(item.getGoodId()).iterator();
                    if (iterator.hasNext()) {
                        while (iterator.hasNext()) {
                            StockGood stockGood = iterator.next();
                            String stockNameFromRepo = stockRepository.findById(stockGood.getStockId()).get().getName();
                            item.getBalance().put(stockNameFromRepo, stockGood.getAmount());
                        }
                    }
                }
            } else {
                for (GoodsBalance item : result) {
                    Optional<Stock> stock = stockRepository.findByName(stockName);
                    if (stock.isPresent()) {
                        Optional<StockGood> stockGood = stockGoodRepository
                                .findByGoodIdAndStockId(item.getGoodId(), stock.get().getId());
                        stockGood.ifPresent(good -> item.getBalance().put(stockName, good.getAmount()));
                    } else {
                        throw new NoSuchStockException("Склада с таким названием не существует");
                    }
                }
            }
        } catch (Exception e) {
            log.error("Не удалось собрать отчёт по остаткам продуктов", e);
        }
        return result;
    }
}
