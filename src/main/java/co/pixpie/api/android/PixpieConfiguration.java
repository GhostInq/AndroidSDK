package co.pixpie.api.android;

public class PixpieConfiguration {

    String reverseUrlId;
    String secretKey;
    String salt;
    String userId;
    String deviceId;
    String deviceDescription;

    int cacheEntriesNumber;

    public PixpieConfiguration withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public PixpieConfiguration withDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public PixpieConfiguration withDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
        return this;
    }

    public PixpieConfiguration withReverseUrlId(String reverseUrlId) {
        this.reverseUrlId = reverseUrlId;
        return this;
    }

    public PixpieConfiguration withSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    public PixpieConfiguration withSalt(String salt) {
        this.salt = salt;
        return this;
    }

    public PixpieConfiguration withCacheEntriesNumber(int cacheEntriesNumber) {
        this.cacheEntriesNumber = cacheEntriesNumber;
        return this;
    }
}
