package exception;

public class VendedorException extends TiendaException {
    private static final long serialVersionUID = 1L;

	public VendedorException(String mensaje) {
        super(mensaje);
    }
}
