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
package pl.exsio.plupload.helper.filter;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author exsio
 */
public class PluploadFilters implements Serializable {

    @SerializedName("mime_types")
    protected PluploadFilter[] filters = new PluploadFilter[0];

    public PluploadFilter[] get() {
        return filters;
    }

    public void set(PluploadFilter[] filters) {
        this.filters = filters;
    }

    public void add(PluploadFilter filter) {
        List<PluploadFilter> list = new ArrayList(Arrays.asList(filters));
        list.add(filter);
        this.filters = list.toArray(new PluploadFilter[list.size()]);
    }

    public void remove(PluploadFilter filter) {
        List<PluploadFilter> list = new ArrayList(Arrays.asList(filters));
        list.remove(filter);
        this.filters = list.toArray(new PluploadFilter[list.size()]);
    }

}
