package co.pixpie.api.android.networking;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.SSLCertificateSocketFactory;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

/**
 * Created by accord on 8/5/16.
 */
public class PixpieRequestQueueWrapper {

    /**
     * Creates a default instance of the worker pool.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack An {@link HttpStack} to use for the network, or null for default.
     */

    private static RequestQueue queue;

    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {

        if (queue == null) {

            // File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

            String userAgent = "volley/0";
            try {
                String packageName = context.getPackageName();
                PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
                userAgent = packageName + "/" + info.versionCode;
            } catch (PackageManager.NameNotFoundException e) {
            }

            if (stack == null) {
                if (Build.VERSION.SDK_INT >= 9) {
                    stack = new HurlStack(null, SSLCertificateSocketFactory.getDefault(30000, null));
                } else {
                    // Prior to Gingerbread, HttpUrlConnection was unreliable.
                    // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                    stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
                }
            }

            Network network = new BasicNetwork(stack);

            queue = new RequestQueue(new NoCache(), network);
            queue.start();

        }

        return queue;
    }

    /**
     * Creates a default instance of the worker pool.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     */
    public static RequestQueue newRequestQueue(Context context) {
        return newRequestQueue(context, null);
    }
}
