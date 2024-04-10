package dev.tjulioh.erpsenior.exception;

import java.io.Serial;

public class NotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6116161976205789537L;

    public NotFoundException(String message) {
        super(message);
    }
}
