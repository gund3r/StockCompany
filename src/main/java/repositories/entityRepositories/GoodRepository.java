package repositories.entityRepositories;

import entities.Good;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;

@JdbcRepository(dialect = Dialect.H2)
public interface GoodRepository extends PageableRepository<Good, Long> {

    Good save(String vendorCode, @NonNull @NotBlank String name, int lastPurchasePrice, int lastSalePrice);

    @Transactional
    default Good saveWithException(String vendorCode, @NonNull @NotBlank String name, int lastPurchasePrice, int lastSalePrice) {
        save(vendorCode, name, lastPurchasePrice, lastSalePrice);
        throw new DataAccessException("test exception");
    }

    long update(@Id Long id, String vendorCode, String name, int lastPurchasePrice, int lastSalePrice);

    long update(@Id Long id, String name);

    Page<Good> findById(@Id Long id, Pageable pageable);
}
