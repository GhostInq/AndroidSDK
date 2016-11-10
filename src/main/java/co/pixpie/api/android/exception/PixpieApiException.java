package co.pixpie.api.android.exception;

public class PixpieApiException extends Exception {

    public PixpieApiException() {
    }

    public PixpieApiException(String detailMessage) {
        super(detailMessage);
    }

    public PixpieApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public PixpieApiException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }
}
