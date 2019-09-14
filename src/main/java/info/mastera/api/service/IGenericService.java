package info.mastera.api.service;

import java.util.List;

public interface IGenericService<T> {

    T create(T entity);

    void delete(T entity);

    void update(T entity);

    T getById(int id);

    List<T> getAll();

    Long count();
}