package co.pixpie.api.android;

import android.util.Log;

import com.android.volley.VolleyError;

import co.pixpie.api.android.exception.PixpieApiException;

public abstract class AuthenticationListener {

    private static final String TAG = AuthenticationListener.class.getSimpleName();

    public void onComplete() {
        Log.d(TAG, "PixpieApi: authenticated!");
    }

    public void onNetworkError(VolleyError error) {
        Log.d(TAG, "PixpieApi: network error", error);
    }

    public void onPixpieError(PixpieApiException exception) {
        Log.d(TAG, "PixpieApi: API level error", exception);
    }
}
