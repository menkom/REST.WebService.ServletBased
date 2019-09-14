package info.mastera.api.dao;

import java.util.List;

public interface IGenericDao<T> {

    /**
     * Create
     *
     * @param entity Save object to DB
     * @return T
     */
    T create(T entity);

    /**
     * Read
     *
     * @param id Get T object with id
     * @return T
     */
    T getById(int id);

    /**
     * Update
     *
     * @param entity Entity to update
     */
    void update(T entity);

    /**
     * Delete
     *
     * @param entity Entity to delete
     */
    void delete(T entity);

    /**
     * getAll
     *
     * @return List<T> all T objects
     */
    List<T> getAll();

    /**
     * count()
     *
     * @return Long
     */
    Long count();
}