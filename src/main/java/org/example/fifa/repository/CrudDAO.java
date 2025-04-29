package org.example.fifa.repository;

import java.util.List;

public interface CrudDAO<E> {
    List<E> findAll();
    E findById(Long id);
    E findByName(String name);
    List<E> save(List<E> entity);
    List<E> update(List<E> entities);
    void delete(E entity);
}
