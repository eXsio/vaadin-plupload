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

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.Button;
import java.util.LinkedHashSet;
import java.util.Set;
import pl.exsio.plupload.client.PluploadCilentRpc;
import pl.exsio.plupload.ex.PluploadNotInitializedException;
import pl.exsio.plupload.model.PluploadFile;
import pl.exsio.plupload.server.PluploadServerRpc;

/**
 *
 * @author exsio
 */
@JavaScript({
    "client/js/plupload.js"
})
public class Plupload extends Button {

    protected final Set<FilesAddedListener> filesAddedListeners = new LinkedHashSet<>();

    protected final Set<UploadProgressListener> uploadProgressListeners = new LinkedHashSet<>();

    protected final Set<FilesRemovedListener> filesRemovedListeners = new LinkedHashSet<>();

    protected final Set<FileFilteredListener> fileFilteredListeners = new LinkedHashSet<>();

    protected final Set<FileUploadedListener> fileUploadedListeners = new LinkedHashSet<>();

    protected final Set<ErrorListener> errorListeners = new LinkedHashSet<>();

    protected final Set<UploadStartListener> uploadStartListeners = new LinkedHashSet<>();

    protected final Set<UploadCompleteListener> uploadCompleteListeners = new LinkedHashSet<>();

    protected boolean initialized = false;

    public Plupload() {
        super();
        registerRpc(serverRpc);

    }

    public Plupload(String caption) {
        super(caption);
        registerRpc(serverRpc);
    }

    public Plupload(String caption, ClickListener clickListener) {
        super(caption, clickListener);
        registerRpc(serverRpc);
    }

    protected PluploadServerRpc serverRpc = new PluploadServerRpc() {

        @Override
        public void filesAdded(String json) {
            PluploadFile[] files = new Gson().fromJson(json, PluploadFile[].class);
            for (FilesAddedListener listener : filesAddedListeners) {
                listener.onFilesAdded(files);
            }
        }

        @Override
        public void uploadProgress(String json) {
            PluploadFile file = new Gson().fromJson(json, PluploadFile.class);
            for (UploadProgressListener listener : uploadProgressListeners) {
                listener.onUploadProgress(file);
            }
        }

        @Override
        public void uploadComplete() {
            for (UploadCompleteListener listener : uploadCompleteListeners) {
                listener.onUploadComplete();
            }
        }

        @Override
        public void filesRemoved(String json) {
            PluploadFile[] files = new Gson().fromJson(json, PluploadFile[].class);
            for (FilesRemovedListener listener : filesRemovedListeners) {
                listener.onFilesRemoved(files);
            }
        }

        @Override
        public void fileUploaded(String json) {
            PluploadFile file = new Gson().fromJson(json, PluploadFile.class);
            for (FileUploadedListener listener : fileUploadedListeners) {
                listener.onFileUploaded(file);
            }
        }

        @Override
        public void error() {
            for (ErrorListener listener : errorListeners) {
                listener.onError();
            }
        }

        @Override
        public void fileFiltered(String json) {
            PluploadFile file = new Gson().fromJson(json, PluploadFile.class);
            for (FileFilteredListener listener : fileFilteredListeners) {
                listener.onFileFiltered(file);
            }
        }

    };

    public void init() {
        if (!this.initialized) {
            this.getClient().init();
            this.initialized = true;
        }
    }

    public void disableBrowse(boolean disable) {
        this.getClient().disableBrowse(disable);
    }

    public void setOption(PluploadOption name, String value) {
        this.getClient().setOption(name.toString(), value);
    }

    public void start() {
        if (this.initialized) {
            for (UploadStartListener listener : uploadStartListeners) {
                listener.onUploadStart();
            }
            this.getClient().start();
        } else {
            throw new PluploadNotInitializedException("You can't start the upload, because the uploader hasn't been initialized");
        }
    }

    public void stop() {
        this.getClient().stop();
    }

    protected PluploadCilentRpc getClient() {
        return this.getRpcProxy(PluploadCilentRpc.class);
    }

    public void addFilesAddedListener(FilesAddedListener listener) {
        this.filesAddedListeners.add(listener);
    }

    public void addFilesRemovedListener(FilesRemovedListener listener) {
        this.filesRemovedListeners.add(listener);
    }

    public void addFileFilteredListener(FileFilteredListener listener) {
        this.fileFilteredListeners.add(listener);
    }

    public void addFileUploadedListener(FileUploadedListener listener) {
        this.fileUploadedListeners.add(listener);
    }

    public void addUploadProgressListener(UploadProgressListener listener) {
        this.uploadProgressListeners.add(listener);
    }

    public void addUploadStartListener(UploadStartListener listener) {
        this.uploadStartListeners.add(listener);
    }

    public void addUploadCompleteListener(UploadCompleteListener listener) {
        this.uploadCompleteListeners.add(listener);
    }

    public void addErrorListener(ErrorListener listener) {
        this.errorListeners.add(listener);
    }

    public void removeFilesAddedListener(FilesAddedListener listener) {
        this.filesAddedListeners.remove(listener);
    }

    public void removeFilesRemovedListener(FilesRemovedListener listener) {
        this.filesRemovedListeners.remove(listener);
    }

    public void removeFileFilteredListener(FileFilteredListener listener) {
        this.fileFilteredListeners.remove(listener);
    }

    public void removeFileUploadedListener(FileUploadedListener listener) {
        this.fileUploadedListeners.remove(listener);
    }

    public void removeUploadProgressListener(UploadProgressListener listener) {
        this.uploadProgressListeners.remove(listener);
    }

    public void removeUploadStartListener(UploadStartListener listener) {
        this.uploadStartListeners.remove(listener);
    }

    public void removeUploadCompleteListener(UploadCompleteListener listener) {
        this.uploadCompleteListeners.remove(listener);
    }

    public void removeErrorListener(ErrorListener listener) {
        this.errorListeners.remove(listener);
    }

    public interface FilesAddedListener {

        void onFilesAdded(PluploadFile[] files);
    }

    public interface FilesRemovedListener {

        void onFilesRemoved(PluploadFile[] files);
    }

    public interface FileFilteredListener {

        void onFileFiltered(PluploadFile file);
    }

    public interface FileUploadedListener {

        void onFileUploaded(PluploadFile file);
    }

    public interface UploadStartListener {

        void onUploadStart();
    }

    public interface UploadProgressListener {

        void onUploadProgress(PluploadFile file);
    }

    public interface UploadCompleteListener {

        void onUploadComplete();
    }

    public interface ErrorListener {

        void onError();
    }
}
