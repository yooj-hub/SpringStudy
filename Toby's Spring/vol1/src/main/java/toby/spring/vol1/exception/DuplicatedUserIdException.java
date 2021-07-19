package toby.spring.vol1.exception;

public class DuplicatedUserIdException extends RuntimeException{
    public DuplicatedUserIdException(Throwable cause){
        super(cause);
    }

}
