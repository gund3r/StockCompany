package documents.dto;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Introspected
public class MovingGoodsDTO {
    @NotNull
    private int documentNumber;
    @NotNull
    private long stockIdFrom;
    @NotNull
    private long stockIdTo;
    @NotNull
    @NotEmpty
    private List<GoodAmount> movingGoods;

    public MovingGoodsDTO() {}

    public MovingGoodsDTO(int documentNumber, long stockIdFrom, long stockIdTo, List<GoodAmount> movingGoods) {
        this.documentNumber = documentNumber;
        this.stockIdFrom = stockIdFrom;
        this.stockIdTo = stockIdTo;
        this.movingGoods = movingGoods;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public long getStockIdFrom() {
        return stockIdFrom;
    }

    public void setStockIdFrom(long stockIdFrom) {
        this.stockIdFrom = stockIdFrom;
    }

    public long getStockIdTo() {
        return stockIdTo;
    }

    public void setStockIdTo(long stockIdTo) {
        this.stockIdTo = stockIdTo;
    }

    public List<GoodAmount> getMovingGoods() {
        return movingGoods;
    }

    public void setMovingGoods(List<GoodAmount> movingGoods) {
        this.movingGoods = movingGoods;
    }

    @Override
    public String toString() {
        return "MovingGoodsDTO{" +
                "documentNumber=" + documentNumber +
                ", stockIdFrom=" + stockIdFrom +
                ", stockIdTo=" + stockIdTo +
                ", movingGoods=" + movingGoods +
                '}';
    }
}
