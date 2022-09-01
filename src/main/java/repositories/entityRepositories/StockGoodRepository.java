package repositories.entityRepositories;

import entities.StockGood;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface StockGoodRepository extends PageableRepository<StockGood, Long> {

    StockGood save(long stockId, long goodId, int amount);

    @Transactional
    default StockGood saveWithException(long stockId, long goodId, int amount) {
        save(stockId, goodId, amount);
        throw new DataAccessException("test exception");
    }

    long update(@Id Long id, long stockId, long goodId, int amount);

    Optional<StockGood> findByGoodIdAndStockId(@NonNull @NotNull Long goodId, @NonNull @NotNull Long stockId);

    Iterable<StockGood> findAll();

    Iterable<StockGood> findAllByStockId(@NonNull @NotNull Long stockId);

    Iterable<StockGood> findAllByGoodId(@NonNull @NotNull Long goodId);

}
