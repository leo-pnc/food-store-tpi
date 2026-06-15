package integrado.prog2.exception;

public class EmailDuplicadoException extends RuntimeException{

    public EmailDuplicadoException(){
        super();
    }

    public EmailDuplicadoException(String mensaje){
        super(mensaje);
    }
}
