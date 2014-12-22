package pl.exsio.plupload.helper.filter;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 *
 * @author exsio
 */
public class PluploadFilter {

    @SerializedName("title")
    protected String title;

    @SerializedName("extensions")
    protected String extensions;

    public PluploadFilter(String title, String extensions) {
        this.title = title;
        this.extensions = extensions;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExtensions(String extensions) {
        this.extensions = extensions;
    }

    public String getTitle() {
        return title;
    }

    public String getExtensions() {
        return extensions;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.title);
        hash = 67 * hash + Objects.hashCode(this.extensions);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PluploadFilter other = (PluploadFilter) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.extensions, other.extensions)) {
            return false;
        }
        return true;
    }

}
