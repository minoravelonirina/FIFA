package org.example.fifa.repository;

import java.util.List;

public interface CrudDAO<E> {
    List<E> findAll();
    E findById(Long id);
    E findByName(String name);
    E save(E entity);
    E update(E entity);
    void delete(E entity);
}
