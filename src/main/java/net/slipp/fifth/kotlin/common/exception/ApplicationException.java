package net.slipp.fifth.kotlin.common.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private static final long serialVersionUID = 6378732031128293317L;
    private final Object[] messageParams;

    public ApplicationException(Throwable cause) {
        super(cause);
        this.messageParams = null;
    }

    public ApplicationException(String message) {
        super(message);
        this.messageParams = null;
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.messageParams = null;
    }

    public ApplicationException(String message, Throwable cause, Object... messageParams) {
        super(message, cause);
        this.messageParams = messageParams;
    }
}
