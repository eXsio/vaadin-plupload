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
package pl.exsio.plupload;

import java.io.File;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author exsio
 */
public class PluploadFile implements Serializable {

    protected int percent;

    protected String id;

    protected String lastModifiedDate;

    protected long origSize;

    protected long size;

    protected int status;

    protected String type;

    protected long loaded;

    protected String name;

    protected boolean uploaded = false;

    protected transient Object uploadedFile;

    /**
     * Get percentage of upload progress
     *
     * @return
     */
    public int getPercent() {
        return percent;
    }

    void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     * Get unique Id of the file
     *
     * @return
     */
    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    /**
     * Gget the last modified date of the file
     *
     * @return
     */
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    /**
     * Get original size of the file (in bytes)
     *
     * @return
     */
    public long getOrigSize() {
        return origSize;
    }

    void setOrigSize(long origSize) {
        this.origSize = origSize;
    }

    /**
     * Get current size of the file (in bytes)
     *
     * @return
     */
    public long getSize() {
        return size;
    }

    void setSize(long size) {
        this.size = size;
    }

    /**
     * Get current status of the file according to client-side Plupload library
     *
     * @return
     */
    public int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }

    /**
     * Get mime-type of the file
     *
     * @return
     */
    public String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    /**
     * Get uploaded bytes
     *
     * @return
     */
    public long getLoaded() {
        return loaded;
    }

    void setLoaded(long loaded) {
        this.loaded = loaded;
    }

    /**
     * Get file name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    /**
     * Get the assicoated UploadedFile object
     *
     * @return
     */
    public Object getUploadedFile() {
        return uploadedFile;
    }

    void setUploadedFile(Object uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    /**
     * 
     * Get the associated UploadedFile object as desired Type
     * 
     * @param <T>
     * @param fileClass
     * @return 
     */
    public <T extends Object> T getUploadedFileAs(Class<T> fileClass) {
        return (T) this.uploadedFile;
    }

    /**
     * Check, if the file was uploaded
     *
     * @return
     */
    public boolean isUploaded() {
        return this.uploaded;
    }

    void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    /**
     * Check, if the file contains associated java.io.File saved on the server
     *
     * @return
     */
    public boolean hasUploadedFile() {
        return this.uploadedFile != null;
    }

    @Override
    public String toString() {
        return this.name + " (type: " + this.type + ", size: " + this.size + ")";
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final PluploadFile other = (PluploadFile) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
