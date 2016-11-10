package co.pixpie.api.android;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.android.volley.RequestQueue;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import co.pixpie.api.android.auth.AuthManager;
import co.pixpie.api.android.beans.ApplicationInfo;
import co.pixpie.api.android.networking.PixpieRequestQueueWrapper;
import co.pixpie.api.android.utils.NetworkSingleton;

public class PixpieApi {

    private final String TAG = PixpieApi.class.getName();

    private static String BACKEND_SCHEME = "https";
    private static String BACKEND_URL = "api.pixpie.co:9443";
    private final static String SDK_VERSION = "1.2.1";
    private final static int DEFAULT_CACHE_ENTRIES_NUMBER = 200;

    private static AuthManager authManager = new AuthManager();
    private static ApplicationInfo applicationInfo;

    private static RequestQueue requestQueue;

    private static boolean sdkDisabled = false;

    private PixpieApi() {}

    public static void authenticate(Context context, PixpieConfiguration configuration, String scheme, String host, Integer port) {
        if (StringUtils.isNotBlank(scheme)) {
            BACKEND_SCHEME = scheme;
        }
        if (StringUtils.isNotBlank(host) && port != null && port > 0) {
            BACKEND_URL = host + ":" + port;
        }
        authenticate(context, configuration, null);
    }

    public static void authenticate(Context context, PixpieConfiguration configuration) {
        authenticate(context, configuration, null);
    }

    public static void authenticate(Context context, PixpieConfiguration configuration,
                                    final AuthenticationListener listener) {
        // TODO: consider cancelling previous request and starting new
        if (authManager.isAuthenticationInProgress()) {
            return;
        }

        fillMissingConfigurationParameters(configuration);
        validateConfiguration(configuration);

        applicationInfo = createApplicationInfo(configuration);
        requestQueue = PixpieRequestQueueWrapper.newRequestQueue(context);

        authManager.authenticate(requestQueue, configuration.userId, configuration.deviceId, applicationInfo, listener);
    }

    public static void disableSDK() {
        sdkDisabled = true;
        clearCache();
    }

    public static void enableSDK() {
        sdkDisabled = false;
        clearCache();
    }

    public static PixpieRequestManager with(Context context) {
        return new PixpieRequestManager(applicationInfo, authManager, context, requestQueue, sdkDisabled);
    }

    private static void fillMissingConfigurationParameters(PixpieConfiguration configuration) {
        if (configuration.userId == null) {
            configuration.userId = Settings.Secure.ANDROID_ID;
        }
        if (configuration.deviceId == null) {
            configuration.deviceId = Settings.Secure.ANDROID_ID;
        }
        if (configuration.deviceDescription == null) {
            configuration.deviceDescription = String.format(
                    "%s/%s/%s", Build.MANUFACTURER, Build.MODEL, Build.VERSION.RELEASE);
        }
        if (configuration.cacheEntriesNumber == 0) {
            configuration.cacheEntriesNumber = DEFAULT_CACHE_ENTRIES_NUMBER;
        }
    }

    private static void validateConfiguration(PixpieConfiguration configuration) {
        if (StringUtils.isEmpty(configuration.reverseUrlId)) {
            throw new IllegalArgumentException("Reverse url id shouldn't empty");
        }
        if (StringUtils.isEmpty(configuration.secretKey)) {
            throw new IllegalArgumentException("Secret key shouldn't be empty");
        }
        if (StringUtils.isEmpty(configuration.salt)) {
            throw new IllegalArgumentException("Salt shouldn't be empty");
        }
        Validate.isTrue(configuration.cacheEntriesNumber > 0, "Cache entries number should be greater than zero");
    }

    private static ApplicationInfo createApplicationInfo(PixpieConfiguration configuration) {
        return new ApplicationInfo(
                configuration.reverseUrlId,
                configuration.secretKey,
                configuration.deviceDescription,
                SDK_VERSION,
                BACKEND_SCHEME,
                BACKEND_URL,
                configuration.salt,
                configuration.cacheEntriesNumber);
    }

    private static void clearCache() {
        NetworkSingleton.getInstance(applicationInfo.getCacheEntriesNumber()).clearCache();
        if (requestQueue != null) {
            requestQueue.getCache().clear();
        }
    }
}
