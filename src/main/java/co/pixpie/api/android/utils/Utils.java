package co.pixpie.api.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.math.BigInteger;

public class Utils {

    public static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + 'x', new BigInteger(1, data));
    }

    public static int calculateImageQuality(Context context) {
        int quality = 75;
        NetworkInfo ni = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (ni != null) {
            int type = ni.getType();
            int subType = ni.getSubtype();


            if (type == ConnectivityManager.TYPE_WIFI) {
                quality = 80;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                switch (subType) {
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                        quality = 75;
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                        quality = 80; // ~ 5 Mbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        quality = 80; // ~ 600-1400 kbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        quality = 80; // ~ 400-1000 kbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        quality = 70; // ~ 14-64 kbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        quality = 80; // ~ 14-64 kbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        quality = 80; // ~ 1-14,4 Mbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        quality = 80; // ~ 1-21 Mbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        quality = 80; // ~ 1-23 Mbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        quality = 80; // ~ 2-14 Mbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                        quality = 80; // ~ 1-2 Mbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        quality = 30; // ~ 100 kbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        quality = 50; // ~ 50-100 kbps
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                        quality = 80; // ~ 10+ Mbps
                        break;
                    default:
                        quality = 75;
                        break;
                }
            }
        }

        return quality;
    }
}
