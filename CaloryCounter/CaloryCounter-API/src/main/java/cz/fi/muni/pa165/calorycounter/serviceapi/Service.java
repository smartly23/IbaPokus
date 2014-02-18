package cz.fi.muni.pa165.calorycounter.serviceapi;

/**
 * Service super-interface Extending interfaces will inherit basic CRUD
 * operations on their DTOs.
 *
 * @author Martin Pasko (smartly23)
 *
 * @param <T> Generic type of DTO
 */
public interface Service<T> {

    public Long create(T dto);

    public T get(Long id);

    public void update(T dto);

    public void remove(Long id);
}
