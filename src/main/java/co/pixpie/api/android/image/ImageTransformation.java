package co.pixpie.api.android.image;

public class ImageTransformation {

    public static final ImageTransformation ORIGINAL = new ImageTransformation(true);

    private Integer width;
    private Integer height;
    private Integer quality;
    private ImageFormat format = ImageFormat.WEBP;
    private boolean original = false;
    private CropAlignType cropAlignType;


    public ImageTransformation() {}

    private ImageTransformation(boolean original) {
        this.original = original;
    }

    public ImageTransformation withWidth(int width) {
        this.width = width;
        return this;
    }

    public ImageTransformation withHeight(int height) {
        this.height = height;
        return this;
    }

    public ImageTransformation withQuality(int quality) {
        this.quality = quality;
        return this;
    }

    public ImageTransformation withFormat(ImageFormat format) {
        this.format = format;
        return this;
    }

    public ImageTransformation withCropAlignType(CropAlignType cropAlignType) {
        this.cropAlignType = cropAlignType;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getQuality() {
        return quality;
    }

    public ImageFormat getFormat() {
        return format;
    }

    public boolean isOriginal() {
        return original;
    }

    public CropAlignType getCropAlignType() {
        return cropAlignType;
    }
}
