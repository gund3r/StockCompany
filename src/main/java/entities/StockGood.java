package entities;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotNull;

@MappedEntity
public class StockGood {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private Long id;
    @NotNull
    private long goodId;
    @NotNull
    private long stockId;
    @NotNull
    private int amount;

    public StockGood(long goodId, long stockId, int amount) {
        this.goodId = goodId;
        this.stockId = stockId;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "StockGoods{" +
                "goodId=" + goodId +
                ", stockId=" + stockId +
                ", amount=" + amount +
                '}';
    }
}
