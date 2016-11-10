package co.pixpie.api.android.beans;

public class ApplicationInfo {

    private final String reverseUrlId;
    private final String secretKey;
    private final String deviceDescription;
    private final String sdkVersion;
    private final String serverScheme;
    private final String serverAddress;
    private final String salt;
    private final int cacheEntriesNumber;

    public ApplicationInfo(String reverseUrlId, String secretKey, String deviceDescription, String sdkVersion,
                           String serverScheme, String serverAddress, String salt, int cacheEntriesNumber) {
        this.reverseUrlId = reverseUrlId;
        this.secretKey = secretKey;
        this.deviceDescription = deviceDescription;
        this.sdkVersion = sdkVersion;
        this.serverScheme = serverScheme;
        this.serverAddress = serverAddress;
        this.salt = salt;
        this.cacheEntriesNumber = cacheEntriesNumber;
    }

    public String getReverseUrlId() {
        return reverseUrlId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public String getServerScheme() {
        return serverScheme;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getSalt() {
        return salt;
    }

    public int getCacheEntriesNumber() {
        return cacheEntriesNumber;
    }
}
