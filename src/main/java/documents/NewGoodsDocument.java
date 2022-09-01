package documents;

import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@MappedEntity
public class NewGoodsDocument {
    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    private Long id;
    @NotNull
    private int documentNumber;
    @NotNull
    private long stockId;
    @NotNull
    private long newGoodsId;
    @NotNull
    private int newGoodsAmount;
    @NotNull
    private int price;

    public NewGoodsDocument(int documentNumber, long stockId, long newGoodsId, int newGoodsAmount, int price) {
        this.documentNumber = documentNumber;
        this.stockId = stockId;
        this.newGoodsId = newGoodsId;
        this.newGoodsAmount = newGoodsAmount;
        this.price = price;
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

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
    }

    public long getNewGoodsId() {
        return newGoodsId;
    }

    public void setNewGoodsId(long newGoodsId) {
        this.newGoodsId = newGoodsId;
    }

    public int getNewGoodsAmount() {
        return newGoodsAmount;
    }

    public void setNewGoodsAmount(int newGoodsAmount) {
        this.newGoodsAmount = newGoodsAmount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "NewGoodsDocument{" +
                "id=" + id +
                ", documentNumber=" + documentNumber +
                ", stockId=" + stockId +
                ", newGoodsId=" + newGoodsId +
                ", newGoodsAmount=" + newGoodsAmount +
                ", price=" + price +
                '}';
    }
}