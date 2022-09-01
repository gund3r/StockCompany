package repositories.documentRepositories;

import documents.MovingGoodsDocument;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface MovingGoodsRepository extends PageableRepository<MovingGoodsDocument, Long> {
    MovingGoodsDocument save(int documentNumber, long stockIdFrom, long stockIdTo, long movingGoodsId, int movingGoodsAmount);

    @Transactional
    default MovingGoodsDocument saveWithException(int documentNumber, long stockIdFrom, long stockIdTo, long movingGoodsId, int movingGoodsAmount) {
        save(documentNumber, stockIdFrom, stockIdTo, movingGoodsId, movingGoodsAmount);
        throw new DataAccessException("test exception");
    }

    long update(@Id Long id, int documentNumber, long stockIdFrom, long stockIdTo, long movingGoodsId, int movingGoodsAmount);

    Iterable<MovingGoodsDocument> findAllByDocumentNumber(@NotNull int documentNumber);

    Optional<MovingGoodsDocument> findByDocumentNumber(@NotNull int documentNumber);

    @Override
    Iterable<MovingGoodsDocument> findAll();
}
