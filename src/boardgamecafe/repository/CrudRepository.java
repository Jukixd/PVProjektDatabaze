package boardgamecafe.repository;

import java.util.List;

public interface CrudRepository<T> {
    Integer save(T entity);
    T findById(int id);
    List<T> findAll();
    boolean update(T entity);
    boolean delete(int id);
}