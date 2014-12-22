package pl.exsio.plupload.helper.resize;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author exsio
 */
public class PluploadImageResize {

    @SerializedName("enabled")
    protected boolean enabled = false;

    @SerializedName("crop")
    protected boolean crop = false;

    @SerializedName("preserve_headers")
    protected boolean preserveHeaders = true;

    @SerializedName("width")
    protected int width = 0;

    @SerializedName("height")
    protected int height = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public PluploadImageResize setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public boolean isCrop() {
        return crop;
    }

    public PluploadImageResize setCrop(boolean crop) {
        this.crop = crop;
        return this;
    }

    public boolean isPreserveHeaders() {
        return preserveHeaders;
    }

    public PluploadImageResize setPreserveHeaders(boolean preserveHeaders) {
        this.preserveHeaders = preserveHeaders;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public PluploadImageResize setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;

    }

    public PluploadImageResize setHeight(int height) {
        this.height = height;
        return this;
    }

}
