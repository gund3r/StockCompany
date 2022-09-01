package documents.dto;

public class GoodAmount {
    long goodId;
    int amount;
    int price;

    public GoodAmount() {}

    public GoodAmount(long goodId, int amount, int price) {
        this.goodId = goodId;
        this.amount = amount;
        this.price = price;
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "GoodAmount{" +
                "goodId=" + goodId +
                ", amount=" + amount +
                ", price=" + price +
                '}';
    }

}
