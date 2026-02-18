package gestio.moneders.moneders.repository;

import java.util.List;
import java.util.Optional;

public interface FileRepository<T, ID> {

    List<T> findAll();

    Optional<T> findById(ID id);

    T updateById(ID id, T entity);

    T save(T entity);

    void deleteById(ID id);
}