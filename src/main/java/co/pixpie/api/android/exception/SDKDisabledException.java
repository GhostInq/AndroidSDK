package co.pixpie.api.android.exception;

public class SDKDisabledException extends IllegalStateException {

    public SDKDisabledException() {
    }

    public SDKDisabledException(String detailMessage) {
        super(detailMessage);
    }

    public SDKDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public SDKDisabledException(Throwable cause) {
        super((cause == null ? null : cause.toString()), cause);
    }
}
