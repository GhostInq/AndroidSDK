package co.pixpie.api.android.beans;

import com.android.volley.VolleyError;

public class PixpieResult {

    private final boolean success;
    private final VolleyError volleyError;
    private final String errorString;

    public PixpieResult(boolean success, VolleyError volleyError, String errorString) {
        this.success = success;
        this.volleyError = volleyError;
        this.errorString = errorString;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorString() {
        return errorString;
    }

    public VolleyError getVolleyError() {
        return volleyError;
    }
}
