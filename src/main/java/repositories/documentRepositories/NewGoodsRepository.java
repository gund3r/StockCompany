package repositories.documentRepositories;

import documents.NewGoodsDocument;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface NewGoodsRepository extends PageableRepository<NewGoodsDocument, Long> {
    NewGoodsDocument save(int documentNumber, long stockId, long newGoodsId, int newGoodsAmount, int price);

    default NewGoodsDocument saveWithException(int documentNumber, long stockId, long newGoodsId, int newGoodsAmount, int price) {
        save(documentNumber, stockId, newGoodsId, newGoodsAmount, price);
        throw new DataAccessException("test exception");
    }

    long update(@Id Long id, int documentNumber, long stockId, long newGoodsId, int newGoodsAmount, int price);

    @Override
    Optional<NewGoodsDocument> findById(@NotNull @Id Long id);

    Iterable<NewGoodsDocument> findAllByDocumentNumber(@NotNull int documentNumber);

    Optional<NewGoodsDocument> findByDocumentNumber(@NotNull int documentNumber);

    @Override
    Iterable<NewGoodsDocument> findAll();

}
