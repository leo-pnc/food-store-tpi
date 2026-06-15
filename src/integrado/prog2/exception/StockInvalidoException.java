package integrado.prog2.exception;

public class StockInvalidoException extends RuntimeException{

    public StockInvalidoException(){
        super();
    }

    public StockInvalidoException(String mensaje){
        super(mensaje);
    }
}
