package cz.fi.muni.pa165.calorycounter.backend.service.common;

import org.springframework.dao.RecoverableDataAccessException;

/**
 * Exception template for wrapping try-catch block around any set of
 * persistence operations, throwing DataAccessExceptions when Exception in the
 * wrapped code itself is thrown. 
 * All tested code has to be put into implementation of the abstract doMethod().
 * This class is used when code is expected to include no return statement.
 * 
 * @author Martin Pasko (smartly23)
 */
public abstract class DataAccessExceptionVoidTemplate<U> {
        private final U u;

    public DataAccessExceptionVoidTemplate(U u) {
        this.u = u;
    }
    
    public void tryMethod() {
        try {
            doMethod();
        } catch (Exception ex) {
            throw new RecoverableDataAccessException("Operation 'create' failed." + ex.getMessage(), ex);
        }
    }
    
    public abstract void doMethod();
    
    public U getU() {
        return u;
    }
        

}
