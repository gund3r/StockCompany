package entities.dao;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

public class GoodDAO {
    private String vendorCode;
    @NotNull
    @NotBlank
    private String name;

    private int lastPurchasePrice;

    private int lastSalePrice;

    public GoodDAO(@NotNull @NotBlank String name) {
        this.vendorCode = UUID.randomUUID().toString();
        this.name = name;
        this.lastPurchasePrice = 0;
        this.lastSalePrice = 0;
    }

    public String getVendorCode() {
        return vendorCode;
    }

    public String getName() {
        return name;
    }

    public int getLastPurchasePrice() {
        return lastPurchasePrice;
    }

    public int getLastSalePrice() {
        return lastSalePrice;
    }
}
