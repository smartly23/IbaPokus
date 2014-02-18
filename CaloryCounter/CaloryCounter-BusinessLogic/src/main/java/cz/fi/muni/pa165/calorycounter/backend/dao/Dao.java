package cz.fi.muni.pa165.calorycounter.backend.dao;

/**
 * DAO super-interface Extending interfaces will inherit basic CRUD operations
 * on their entities.
 *
 * @author Martin Pasko (smartly23)
 *
 * @param <T> Generic type of entity
 */
public interface Dao<T, U> {

    /*  Create the enatity
     * @throws IllegalArgumentException if parameter is null or invalid
     */
    U create(T entity);

    /* Return the entity
     * @throws IllegalArgumentException if parameter is null or invalid
     */
    T get(U pk);

    /* Update the entity
     * @throws IllegalArgumentException if parameter is null, invalid or non-existent in the DB
     */
    void update(T entity);

    /* Remove the entity
     * @throws IllegalArgumentException if parameter is null or invalid. Does not throw this exception if
     * parameter is valid but given entity is nonexistent.
     */
    void remove(U pk);
}
