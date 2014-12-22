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
