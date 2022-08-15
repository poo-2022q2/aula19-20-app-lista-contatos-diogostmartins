package model;

import java.util.List;

public interface IRepository<T> {
    long insert(T element) throws DataUpdateException;
    void remove(T element) throws DataUpdateException;
    void update(T element)  throws DataUpdateException, DataStorageException ;
    T getById(long id) throws ElementNotFoundException;
    List<T> getAll();
}
