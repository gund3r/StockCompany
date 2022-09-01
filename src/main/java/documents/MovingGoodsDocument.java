package documents;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotNull;

@MappedEntity
public class MovingGoodsDocument {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private Long id;
    @NotNull
    private int documentNumber;
    @NotNull
    private long stockIdFrom;
    @NotNull
    private long stockIdTo;
    @NotNull
    private long movingGoodsId;
    @NotNull
    private int movingGoodsAmount;

    public MovingGoodsDocument(int documentNumber, long stockIdFrom, long stockIdTo, long movingGoodsId, @NotNull int movingGoodsAmount) {
        this.documentNumber = documentNumber;
        this.stockIdFrom = stockIdFrom;
        this.stockIdTo = stockIdTo;
        this.movingGoodsId = movingGoodsId;
        this.movingGoodsAmount = movingGoodsAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public long getMovingGoodsId() {
        return movingGoodsId;
    }

    public void setMovingGoodsId(long movingGoodsId) {
        this.movingGoodsId = movingGoodsId;
    }

    public int getMovingGoodsAmount() {
        return movingGoodsAmount;
    }

    public void setMovingGoodsAmount(int movingGoodsAmount) {
        this.movingGoodsAmount = movingGoodsAmount;
    }

    @Override
    public String toString() {
        return "MovingGoodsDocument{" +
                "id=" + id +
                ", documentNumber=" + documentNumber +
                ", stockIdFrom=" + stockIdFrom +
                ", stockIdTo=" + stockIdTo +
                ", movingGoodsId=" + movingGoodsId +
                ", movingGoodsAmount=" + movingGoodsAmount +
                '}';
    }
}
