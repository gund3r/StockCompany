package services;

import documents.MovingGoodsDocument;
import documents.NewGoodsDocument;
import documents.SaleGoodsDocument;
import documents.dto.GoodAmount;
import documents.dto.MovingGoodsDTO;
import documents.dto.NewGoodsDTO;
import documents.dto.SaleGoodsDTO;
import entities.Stock;
import entities.StockGood;
import exception.NoSuchDocumentException;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import repositories.documentRepositories.MovingGoodsRepository;
import repositories.documentRepositories.NewGoodsRepository;
import repositories.documentRepositories.SaleGoodsRepository;
import repositories.entityRepositories.GoodRepository;
import repositories.entityRepositories.StockGoodRepository;
import repositories.entityRepositories.StockRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Singleton
public class DocumentsService {

    final Logger log = org.slf4j.LoggerFactory.getLogger(DocumentsService.class);
    protected final StockGoodRepository stockGoodRepository;
    protected final NewGoodsRepository newGoodsRepository;
    protected final SaleGoodsRepository saleGoodsRepository;
    protected final MovingGoodsRepository movingGoodsRepository;
    protected final GoodRepository goodRepository;
    protected final StockRepository stockRepository;

    @Inject
    public DocumentsService(StockGoodRepository stockGoodRepository, NewGoodsRepository newGoodsRepository,
                            SaleGoodsRepository saleGoodsRepository, MovingGoodsRepository movingGoodsRepository,
                            GoodRepository goodRepository, StockRepository stockRepository) {
        this.stockGoodRepository = stockGoodRepository;
        this.newGoodsRepository = newGoodsRepository;
        this.saleGoodsRepository = saleGoodsRepository;
        this.movingGoodsRepository = movingGoodsRepository;
        this.goodRepository = goodRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public boolean addNewGoods(NewGoodsDTO document) {
        int documentNumber = document.getDocumentNumber();
        if (newGoodsRepository.findAllByDocumentNumber(documentNumber).iterator().hasNext()) {
            log.error("Документ на добавление с номером {} уже существует", documentNumber);
            return false;
        }
        long stockId = document.getStockId();
        Optional<Stock> stock = stockRepository.findById(stockId);
        List<GoodAmount> newGoods = document.getNewGoods();
        for (GoodAmount good : newGoods) {
            long goodId = good.getGoodId();
            int goodPrice = good.getPrice();
            try {
                Optional<StockGood> stockGood = stockGoodRepository.findByGoodIdAndStockId(goodId, stockId);
                if (stockGood.isPresent()) {
                    StockGood stockGoodObj = stockGood.get();
                    int oldAmount = stockGoodObj.getAmount();
                    int newAmount = good.getAmount();
                    stockGoodRepository.update(stockGoodObj.getId(), stockId, goodId, Integer.sum(oldAmount, newAmount));
                } else {
                    if (stock.isPresent()) {
                        stockGoodRepository.save(stockId, good.getGoodId(), good.getAmount());
                    } else {
                        return false;
                    }
                }
                newGoodsRepository.save(documentNumber, stockId, goodId, good.getAmount(), good.getPrice());

                goodRepository.findById(goodId).ifPresent(goodToUpdate ->
                        goodRepository.update(goodId, goodToUpdate.getVendorCode(), goodToUpdate.getName(),
                                goodPrice, goodToUpdate.getLastSalePrice()));

            } catch (Exception e) {
                log.error("Не удалось произвести операции с базой данных", e);
                return false;
            }
        }
        return true;
    }

    @Transactional
    public boolean saleGoods(SaleGoodsDTO document) {
        int documentNumber = document.getDocumentNumber();
        if (saleGoodsRepository.findAllByDocumentNumber(documentNumber).iterator().hasNext()) {
            log.error("Документ на продажу с номером {} уже существует", documentNumber);
            return false;
        }
        long stockId = document.getStockIdFrom();
        List<GoodAmount> saleGoods = document.getSaleGoods();
        for (GoodAmount good : saleGoods) {
            long goodId = good.getGoodId();
            int goodPrice = good.getPrice();
            try {
                Optional<StockGood> stockGood = stockGoodRepository.findByGoodIdAndStockId(goodId, stockId);
                if (stockGood.isPresent()) {
                    StockGood stockGoodObj = stockGood.get();
                    int oldAmount = stockGoodObj.getAmount();
                    int newAmount = oldAmount - good.getAmount();
                    if (newAmount >= 0) {
                        stockGoodRepository.update(stockGoodObj.getId(), stockId, goodId, newAmount);
                        saleGoodsRepository.save(documentNumber, stockId, goodId, good.getAmount(), good.getPrice());
                    } else {
                        log.error("Не удалось найти на складе {} товар {} в нужном количестве {}", stockId, goodId, newAmount);
                        return false;
                    }
                } else {
                    log.error("Не удалось найти на складе {} товар {}", stockId, goodId);
                    return false;
                }
                goodRepository.findById(goodId).ifPresent(goodToUpdate ->
                        goodRepository.update(goodId, goodToUpdate.getVendorCode(), goodToUpdate.getName(),
                                goodToUpdate.getLastPurchasePrice(), goodPrice));
            } catch (Exception e) {
                log.error("Не удалось произвести операции с базой данных", e);
                return false;
            }
        }
        return true;
    }

    @Transactional
    public boolean moveGoods(MovingGoodsDTO document) {
        int documentNumber = document.getDocumentNumber();
        if (movingGoodsRepository.findAllByDocumentNumber(documentNumber).iterator().hasNext()) {
            log.error("Документ на перемещение с номером {} уже существует", documentNumber);
            return false;
        }
        long stockIdFrom = document.getStockIdFrom();
        long stockIdTo = document.getStockIdTo();
        List<GoodAmount> movingGoods = document.getMovingGoods();
        for (GoodAmount good : movingGoods) {
            long goodId = good.getGoodId();
            int goodAmount = good.getAmount();
            try {
                Optional<StockGood> stockGoodFrom = stockGoodRepository.findByGoodIdAndStockId(goodId, stockIdFrom);
                if (stockGoodFrom.isPresent()) {
                    StockGood stockGoodFromObj = stockGoodFrom.get();
                    int goodAmountFrom = stockGoodFromObj.getAmount();
                    Optional<StockGood> stockGoodTo = stockGoodRepository.findByGoodIdAndStockId(goodId, stockIdTo);
                    if (stockGoodTo.isPresent()) {
                        StockGood stockGoodToObj = stockGoodTo.get();
                        int oldAmount = stockGoodToObj.getAmount();
                        if (goodAmount <= goodAmountFrom) {
                            stockGoodRepository.update(stockGoodToObj.getId(), stockIdTo, goodId, Integer.sum(oldAmount, goodAmount));
                        } else {
                            log.error("Не удалось найти на складе {} товар {} в нужном количестве {}", stockIdFrom, goodId, goodAmount);
                        }
                    } else {
                        if (goodAmount <= goodAmountFrom) {
                            stockGoodRepository.save(stockIdTo, goodId, goodAmount);
                        }
                    }
                    stockGoodRepository.update(stockGoodFromObj.getId(), stockIdFrom, goodId, goodAmountFrom - goodAmount);
                    movingGoodsRepository.save(documentNumber, stockIdFrom, stockIdTo, goodId, goodAmount);
                } else {
                    log.error("Не удалось найти на складе {} товар {}", stockIdFrom, goodId);
                    return false;
                }
            } catch (Exception e) {
                log.error("Не удалось произвести операции с базой данных", e);
                return false;
            }
        }
        return true;
    }

    public NewGoodsDTO getNewGoodsDocument(int documentNumber) throws NoSuchDocumentException {
        NewGoodsDTO result = null;
        try {
            Iterator<NewGoodsDocument> documents = newGoodsRepository.findAllByDocumentNumber(documentNumber).iterator();
            if (documents.hasNext()) {
                NewGoodsDocument documentDTO = newGoodsRepository.findByDocumentNumber(documentNumber).get();
                result = new NewGoodsDTO(documentNumber, documentDTO.getStockId(), new ArrayList<>());
                while (documents.hasNext()) {
                    NewGoodsDocument document = documents.next();
                    result.getNewGoods().add(new GoodAmount(document.getNewGoodsId(),
                            document.getNewGoodsAmount(), document.getPrice()));
                }
            }
        } catch (Exception e) {
            log.error("Документа с таким номером {} не существует", documentNumber, e);
            throw new NoSuchDocumentException("Документа с таким номером не существует");
        }
        return result;
    }

    public SaleGoodsDTO getSaleGoodsDocument(int documentNumber) throws NoSuchDocumentException {
        SaleGoodsDTO result = null;
        try {
            Iterator<SaleGoodsDocument> documents = saleGoodsRepository.findAllByDocumentNumber(documentNumber).iterator();
            if (documents.hasNext()) {
                SaleGoodsDocument documentDTO = saleGoodsRepository.findByDocumentNumber(documentNumber).get();
                result = new SaleGoodsDTO(documentNumber, documentDTO.getStockIdFrom(), new ArrayList<>());
                while (documents.hasNext()) {
                    SaleGoodsDocument document = documents.next();
                    result.getSaleGoods().add(new GoodAmount(document.getSaleGoodsId(),
                            document.getSaleGoodsAmount(), document.getPrice()));
                }
            }

        } catch (Exception e) {
            log.error("Документа с таким номером {} не существует", documentNumber, e);
            throw new NoSuchDocumentException("Документа с таким номером не существует");
        }
        return result;
    }

    public MovingGoodsDTO getMoveGoodsDocument(int documentNumber) throws NoSuchDocumentException {
        MovingGoodsDTO result = null;
        try {
            Iterator<MovingGoodsDocument> documents = movingGoodsRepository.findAllByDocumentNumber(documentNumber).iterator();
            if (documents.hasNext()) {
                MovingGoodsDocument documentDTO = movingGoodsRepository.findByDocumentNumber(documentNumber).get();
                result = new MovingGoodsDTO(documentNumber, documentDTO.getStockIdFrom(), documentDTO.getStockIdTo(), new ArrayList<>());
                while (documents.hasNext()) {
                    MovingGoodsDocument document = documents.next();
                    result.getMovingGoods().add(new GoodAmount(document.getMovingGoodsId(),
                            document.getMovingGoodsAmount(), 0));
                }
            }
        } catch (Exception e) {
            log.error("Документа с таким номером {} не существует", documentNumber, e);
            throw new NoSuchDocumentException("Документа с таким номером не существует");
        }
        return result;
    }

}
