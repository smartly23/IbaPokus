package cz.fi.muni.pa165.calorycounter.rest;

/**
 *
 * @author smartly23
 */
public class RestException extends RuntimeException {
    
    public RestException() {
        super();
    }
    
    public RestException(String msg) {
        super(msg);
    }

    public RestException(Throwable cause) {
        super(cause);
    }

    public RestException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
