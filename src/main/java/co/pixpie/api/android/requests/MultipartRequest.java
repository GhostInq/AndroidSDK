package co.pixpie.api.android.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.util.Map;

class MultipartRequest extends Request<NetworkResponse> {

    private final Response.Listener<NetworkResponse> responseListener;
    private final Response.ErrorListener errorListener;
    private final Map<String, String> headers;
    private final String mimeType;
    private final byte[] multipartBody;

    MultipartRequest(String url, Map<String, String> headers, String mimeType, byte[] multipartBody,
                     Response.Listener<NetworkResponse> responseListener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);

        this.responseListener = responseListener;
        this.errorListener = errorListener;
        this.headers = headers;
        this.mimeType = mimeType;
        this.multipartBody = multipartBody;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return (headers != null) ? headers : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return multipartBody;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        if (responseListener != null) {
            responseListener.onResponse(response);
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        if (errorListener != null) {
            errorListener.onErrorResponse(error);
        }
    }
}
