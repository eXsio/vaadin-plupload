/* 
 * The MIT License
 *
 * Copyright 2015 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.exsio.plupload.helper.resize;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

/**
 *
 * @author exsio
 */
public class PluploadImageResize implements Serializable {

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
