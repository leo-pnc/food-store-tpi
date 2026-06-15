package integrado.prog2.exception;

public class EntidadNoEncontradaException extends RuntimeException{

    public EntidadNoEncontradaException(){
        super();
    }
    public EntidadNoEncontradaException(String mensaje){
        super(mensaje);
    }
}
