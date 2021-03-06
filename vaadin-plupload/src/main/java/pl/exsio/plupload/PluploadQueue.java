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

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import pl.exsio.plupload.ex.InvalidFileHandlerException;
import pl.exsio.plupload.handler.PluploadChunkHandler;

/**
 *
 * @author exsio
 */
public class PluploadQueue implements Serializable {

    protected final Map<String, PluploadFile> queue;

    protected final Map<String, PluploadChunkHandler> handlers;

    protected enum Mode {

        ALL,
        UPLOADED,
        NOT_UPLOADED
    }

    public PluploadQueue() {
        this.queue = new LinkedHashMap<>();
        this.handlers = new LinkedHashMap<>();
    }

    public void addFile(PluploadFile file, PluploadChunkHandler fileHandler) {
        this.queue.put(file.getId(), file);
        this.handlers.put(file.getId(), fileHandler);
        getReceiver().addExpectedFile(file.getId(), fileHandler);
    }

    public void removeFile(String fileId) {
        if (this.queue.containsKey(fileId)) {
            this.queue.remove(fileId);
            this.handlers.remove(fileId);
            getReceiver().removeExpectedFile(fileId);
        }
    }

    public void removeFiles(PluploadFile[] files) {
        for (PluploadFile file : files) {
            this.removeFile(file.getId());
        }
    }

    public boolean isEmpty() {
        return this.getFileIds(Mode.NOT_UPLOADED).isEmpty();
    }

    public void setUploadedFile(String fileId, Object uploadedFile) {
        if (this.queue.containsKey(fileId)) {
            this.queue.get(fileId).setUploadedFile(uploadedFile);
            this.queue.get(fileId).setUploaded(true);
        }
    }

    public PluploadChunkHandler getFileHandler(String fileId) {
        if (this.handlers.containsKey(fileId)) {
            return this.handlers.get(fileId);
        } else {
            throw new InvalidFileHandlerException("there is no file handler for file " + fileId);
        }
    }

    public Set<String> getFileIds(Mode mode) {
        if (Mode.ALL.equals(mode)) {
            return this.queue.keySet();
        } else {
            Set<String> ids = new LinkedHashSet();
            for (String fileId : this.queue.keySet()) {
                if ((this.queue.get(fileId).isUploaded() && Mode.UPLOADED.equals(mode))
                        || (!this.queue.get(fileId).isUploaded() && Mode.NOT_UPLOADED.equals(mode))) {
                    ids.add(fileId);
                }
            }
            return ids;
        }
    }

    public Set<PluploadFile> getPluploadFiles(Mode mode) {
        Set<PluploadFile> files = new LinkedHashSet();
        for (String fileId : this.queue.keySet()) {
            PluploadFile file = this.queue.get(fileId);
            if (Mode.ALL.equals(mode)
                    || (file.isUploaded() && Mode.UPLOADED.equals(mode))
                    || (!file.isUploaded() && Mode.NOT_UPLOADED.equals(mode))) {
                files.add(file);
            }
        }
        return files;
    }

    protected static PluploadReceiver getReceiver() {
        return PluploadReceiver.getInstance();
    }

}
