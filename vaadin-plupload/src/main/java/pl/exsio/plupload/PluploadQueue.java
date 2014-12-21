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
package pl.exsio.plupload;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author exsio
 */
public class PluploadQueue {

    protected final Map<String, PluploadFile> queue;

    public PluploadQueue() {
        this.queue = new LinkedHashMap<>();
    }

    public void addFile(PluploadFile file) {
        this.queue.put(file.getId(), file);
    }

    public void addFiles(PluploadFile[] files) {
        for (PluploadFile file : files) {
            this.addFile(file);
        }
    }

    public void removeFile(String fileId) {
        if (this.queue.containsKey(fileId)) {
            this.queue.remove(fileId);
        }
    }

    public void removeFiles(PluploadFile[] files) {
        for (PluploadFile file : files) {
            this.removeFile(file.getId());
        }
    }

    public void setUploadedFile(String fileId, File uploadedFile) {
        if (this.queue.containsKey(fileId)) {
            this.queue.get(fileId).setUploadedFile(uploadedFile);
        }
    }

    public Set<String> getFileIds(boolean uploadedOnly) {
        if (!uploadedOnly) {
            return this.queue.keySet();
        } else {
            Set<String> ids = new LinkedHashSet();
            for (String fileId : this.queue.keySet()) {
                if (this.queue.get(fileId).isUploaded()) {
                    ids.add(fileId);
                }
            }
            return ids;
        }
    }

    public Set<PluploadFile> getPluploadFiles(boolean uploadedOnly) {
        Set<PluploadFile> files = new LinkedHashSet();
        for (String fileId : this.queue.keySet()) {
            PluploadFile file = this.queue.get(fileId);
            if (file.isUploaded()) {
                files.add(file);
            }
        }
        return files;
    }
}
