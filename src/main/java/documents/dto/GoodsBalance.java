package documents.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.HashMap;
import java.util.Map;

@Introspected
public class GoodsBalance {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    long goodId;

    String vendorCode;

    String name;

    Map<String, Integer> balance;

    public GoodsBalance() {}

    public GoodsBalance(long goodId, String vendorCode, String name) {
        this.goodId = goodId;
        this.vendorCode = vendorCode;
        this.name = name;
        this.balance = new HashMap<>();
    }

    public long getGoodId() {
        return goodId;
    }

    public void setGoodId(long goodId) {
        this.goodId = goodId;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public void setVendorCode(String vendorCode) {
        this.vendorCode = vendorCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getBalance() {
        return balance;
    }

    public void setBalance(Map<String, Integer> balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "GoodsBalance{" +
                "vendorCode='" + vendorCode + '\'' +
                ", name='" + name + '\'' +
                ", balance=" + balance.toString() +
                '}';
    }
}
