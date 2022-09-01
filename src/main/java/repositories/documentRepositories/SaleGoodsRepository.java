package repositories.documentRepositories;

import documents.SaleGoodsDocument;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2)
public interface SaleGoodsRepository extends PageableRepository<SaleGoodsDocument, Long> {

    SaleGoodsDocument save(int documentNumber, long stockIdFrom, long saleGoodsId, int saleGoodsAmount, int price);

    @Transactional
    default SaleGoodsDocument saveWithException(int documentNumber, long stockIdFrom, long saleGoodsId, int saleGoodsAmount, int price) {
        save(documentNumber, stockIdFrom, saleGoodsId, saleGoodsAmount, price);
        throw new DataAccessException("test exception");
    }

    long update(@Id Long id, int documentNumber, long stockIdFrom, long saleGoodsId, int saleGoodsAmount, int price);

    Iterable<SaleGoodsDocument> findAllByDocumentNumber(@NotNull int documentNumber);

    Optional<SaleGoodsDocument> findByDocumentNumber(@NotNull int documentNumber);

    @Override
    Iterable<SaleGoodsDocument> findAll();
}
