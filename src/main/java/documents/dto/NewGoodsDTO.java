package documents.dto;

import io.micronaut.core.annotation.Introspected;

import java.util.List;

@Introspected
public class NewGoodsDTO {
    private int documentNumber;
    private long stockId;
    private List<GoodAmount> newGoods;

    public NewGoodsDTO() {}

    public NewGoodsDTO(int documentNumber, long stockId, List<GoodAmount> newGoods) {
        this.documentNumber = documentNumber;
        this.stockId = stockId;
        this.newGoods = newGoods;
    }

    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public List<GoodAmount> getNewGoods() {
        return newGoods;
    }

    public void setNewGoods(List<GoodAmount> newGoods) {
        this.newGoods = newGoods;
    }

    @Override
    public String toString() {
        return "NewGoodsDTO{" +
                "documentNumber=" + documentNumber +
                ", stockId=" + stockId +
                ", newGoods=" + newGoods +
                '}';
    }
}
