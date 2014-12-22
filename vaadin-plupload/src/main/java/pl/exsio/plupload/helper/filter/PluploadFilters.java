/* 
 * The MIT License
 *
 * Copyright 2014 exsio.
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
package pl.exsio.plupload.helper.filter;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author exsio
 */
public class PluploadFilters {

    @SerializedName("mime_types")
    protected PluploadFilter[] mimeTypes = new PluploadFilter[0];

    public PluploadFilter[] getMimeTypes() {
        return mimeTypes;
    }

    public void setMimeTypes(PluploadFilter[] mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    public void addMimeType(PluploadFilter mimeType) {
        List<PluploadFilter> list = new ArrayList(Arrays.asList(mimeTypes));
        list.add(mimeType);
        this.mimeTypes = list.toArray(new PluploadFilter[list.size()]);
    }

    public void removeMimeType(PluploadFilter mimeType) {
        List<PluploadFilter> list = new ArrayList(Arrays.asList(mimeTypes));
        list.remove(mimeType);
        this.mimeTypes = list.toArray(new PluploadFilter[list.size()]);
    }

}
