package exception;

public class ProductoException extends TiendaException {
    private static final long serialVersionUID = 1L;

	public ProductoException(String mensaje) {
        super(mensaje);
    }
}
