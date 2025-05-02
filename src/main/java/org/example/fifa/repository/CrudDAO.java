package org.example.fifa.repository;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<E> {
    List<E> findAll() throws SQLException;
    E findById(String id) throws SQLException;
    E findByName(String name);
    List<E> save(List<E> entity);
    List<E> update(List<E> entities);
    void delete(E entity);
}
