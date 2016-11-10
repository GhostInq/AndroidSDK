package co.pixpie.api.android.image;

/**
 * Created by accord on 11/2/16.
 */

public enum CropAlignType {

    TOP("top"),
    BOTTOM("bottom"),
    RIGHT("right"),
    LEFT("left"),
    TOP_RIGHT("top_right"),
    TOP_LEFT("top_left"),
    BOTTOM_RIGHT("bottom_right"),
    BOTTOM_LEFT("bottom_left");

    private String urlValue;

    CropAlignType(String urlValue) {
        this.urlValue = urlValue;
    }

    public String getUrlValue() {
        return urlValue;
    }
}
