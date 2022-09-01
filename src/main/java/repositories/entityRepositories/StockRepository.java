package repositories.entityRepositories;

import entities.Stock;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface StockRepository extends PageableRepository<Stock, Long> {

    Stock save(@NonNull @NotBlank String name);

    @Transactional
    default Stock saveWithException(@NonNull @NotBlank String name) {
        save(name);
        throw new DataAccessException("test exception");
    }

    void update(@NonNull @NotNull @Id Long id, @NonNull @NotBlank String name);

    void deleteById(@NonNull @NotNull @Id Long id);

    Optional<Stock> findById(@NonNull @NotNull @Id Long id);

    Optional<Stock> findByName(@NonNull @NotBlank String name);
}