package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@MappedEntity
public class Good {

    @Id
    @GeneratedValue(GeneratedValue.Type.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    private String vendorCode;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private int lastPurchasePrice;

    private int lastSalePrice;

    public Good(String name, int lastPurchasePrice) {
        this.vendorCode = UUID.randomUUID().toString();
        this.name = name;
        this.lastPurchasePrice = lastPurchasePrice;
        this.lastSalePrice = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getLastPurchasePrice() {
        return lastPurchasePrice;
    }

    public void setLastPurchasePrice(int lastPurchasePrice) {
        this.lastPurchasePrice = lastPurchasePrice;
    }

    public int getLastSalePrice() {
        return lastSalePrice;
    }

    public void setLastSalePrice(int lastSalePrice) {
        this.lastSalePrice = lastSalePrice;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", vendorCode='" + vendorCode + '\'' +
                ", name='" + name + '\'' +
                ", lastPurchasePrice=" + lastPurchasePrice +
                ", lastSalePrice=" + lastSalePrice +
                '}';
    }
}
