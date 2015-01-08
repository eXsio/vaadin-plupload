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

import pl.exsio.plupload.helper.filter.PluploadFilter;
import pl.exsio.plupload.helper.filter.PluploadFilters;
import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;
import pl.exsio.plupload.client.PluploadCilentRpc;
import pl.exsio.plupload.client.shared.PluploadState;
import pl.exsio.plupload.client.PluploadServerRpc;
import pl.exsio.plupload.helper.resize.PluploadImageResize;

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

    protected final Set<UploadStopListener> uploadStopListeners = new LinkedHashSet<>();

    protected final Set<UploadCompleteListener> uploadCompleteListeners = new LinkedHashSet<>();

    protected boolean uploadStarted = false;

    protected final transient PluploadQueue queue = new PluploadQueue();

    protected final transient PluploadFilters filters = new PluploadFilters();

    protected PluploadImageResize imageResize = new PluploadImageResize();

    protected String uploadPath = System.getProperty("java.io.tmpdir");

    public Plupload() {
        super();
        this.registerRpc(serverRpc);
        this.postConstruct();
    }

    public Plupload(String caption) {
        super(caption);
        this.registerRpc(serverRpc);
        this.postConstruct();
    }

    public Plupload(String caption, ClickListener clickListener) {
        super(caption, clickListener);
        this.registerRpc(serverRpc);
        this.postConstruct();
    }

    public Plupload(String caption, Resource icon) {
        super(caption, icon);
        this.registerRpc(serverRpc);
        this.postConstruct();
    }

    private void postConstruct() {

        getReceiver().bind();
        this.handleFilesAdded();
        this.handleFilesRemoved();
        this.handleFileUploaded();
        this.handleUploadComplete();
    }

    protected static PluploadReceiver getReceiver() {
        return PluploadReceiver.getInstance();
    }

    private void handleUploadComplete() {
        this.addUploadCompleteListener(new UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                uploadStarted = false;
            }
        });
    }

    private void handleFileUploaded() {
        this.addFileUploadedListener(new FileUploadedListener() {

            @Override
            public void onFileUploaded(PluploadFile file) {
                File uploadedFile = getReceiver().retrieveUploadedFile(file.getId());
                if (uploadedFile != null) {
                    queue.setUploadedFile(file.getId(), uploadedFile);
                    file.setUploadedFile(uploadedFile);
                }
            }
        });
    }

    private void handleFilesRemoved() {
        this.addFilesRemovedListener(new FilesRemovedListener() {

            @Override
            public void onFilesRemoved(PluploadFile[] files) {
                queue.removeFiles(files);
            }
        });
    }

    private void handleFilesAdded() {
        this.addFilesAddedListener(new FilesAddedListener() {

            @Override
            public void onFilesAdded(PluploadFile[] files) {
                queue.addFiles(files, uploadPath);
            }
        });
    }

    @Override
    public PluploadState getState() {
        return (PluploadState) super.getState();
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

    public Plupload disableBrowse(boolean disable) {
        this.getClient().disableBrowse(disable);
        return this;
    }

    public Plupload setOption(PluploadOption name, String value) {
        this.getClient().setOption(name.toString(), value);
        return this;
    }

    public Plupload removeFile(String fileId) {
        this.getClient().removeFile(fileId);
        return this;
    }

    public Plupload addFilter(PluploadFilter filter) {
        this.filters.addMimeType(filter);
        this.setOption(PluploadOption.FILTERS, new Gson().toJson(this.filters));
        return this;
    }

    public Plupload removeFilter(PluploadFilter filter) {
        this.filters.removeMimeType(filter);
        this.setOption(PluploadOption.FILTERS, new Gson().toJson(this.filters));
        return this;
    }

    public PluploadImageResize getImageResize() {
        return this.imageResize;
    }

    public Plupload setImageResize(PluploadImageResize resize) {
        this.imageResize = resize;
        this.setOption(PluploadOption.RESIZE, new Gson().toJson(this.imageResize));
        return this;
    }

    public Plupload start() {

        if (!this.queue.isEmpty() && !this.uploadStarted) {
            for (UploadStartListener listener : uploadStartListeners) {
                listener.onUploadStart();
            }
            this.getClient().start();
            this.uploadStarted = true;
        }
        return this;

    }

    public Plupload refresh() {
        this.getClient().refresh();
        return this;
    }

    public PluploadFile[] getUploadedFiles() {
        Set<PluploadFile> files = this.queue.getPluploadFiles(PluploadQueue.Mode.UPLOADED);
        return files.toArray(new PluploadFile[files.size()]);
    }

    public PluploadFile[] getQueuedFiles() {
        Set<PluploadFile> files = this.queue.getPluploadFiles(PluploadQueue.Mode.NOT_UPLOADED);
        return files.toArray(new PluploadFile[files.size()]);
    }

    public String getUploadPath() {
        return uploadPath;
    }

    public Plupload setUploadPath(String uploadPath) {
        this.uploadPath = uploadPath;
        return this;
    }

    public Plupload stop() {

        if (this.uploadStarted) {
            this.getClient().stop();
            for (UploadStopListener listener : this.uploadStopListeners) {
                listener.onUploadStop();
            }
            this.uploadStarted = false;
        }
        return this;

    }

    protected PluploadCilentRpc getClient() {
        return this.getRpcProxy(PluploadCilentRpc.class);
    }

    public Plupload addFilesAddedListener(FilesAddedListener listener) {
        this.filesAddedListeners.add(listener);
        return this;
    }

    public Plupload addFilesRemovedListener(FilesRemovedListener listener) {
        this.filesRemovedListeners.add(listener);
        return this;
    }

    public Plupload addFileFilteredListener(FileFilteredListener listener) {
        this.fileFilteredListeners.add(listener);
        return this;
    }

    public Plupload addFileUploadedListener(FileUploadedListener listener) {
        this.fileUploadedListeners.add(listener);
        return this;
    }

    public Plupload addUploadProgressListener(UploadProgressListener listener) {
        this.uploadProgressListeners.add(listener);
        return this;
    }

    public Plupload addUploadStartListener(UploadStartListener listener) {
        this.uploadStartListeners.add(listener);
        return this;
    }

    public Plupload addUploadStopListener(UploadStopListener listener) {
        this.uploadStopListeners.add(listener);
        return this;
    }

    public Plupload addUploadCompleteListener(UploadCompleteListener listener) {
        this.uploadCompleteListeners.add(listener);
        return this;
    }

    public Plupload addErrorListener(ErrorListener listener) {
        this.errorListeners.add(listener);
        return this;
    }

    public Plupload removeFilesAddedListener(FilesAddedListener listener) {
        this.filesAddedListeners.remove(listener);
        return this;
    }

    public Plupload removeFilesRemovedListener(FilesRemovedListener listener) {
        this.filesRemovedListeners.remove(listener);
        return this;
    }

    public Plupload removeFileFilteredListener(FileFilteredListener listener) {
        this.fileFilteredListeners.remove(listener);
        return this;
    }

    public Plupload removeFileUploadedListener(FileUploadedListener listener) {
        this.fileUploadedListeners.remove(listener);
        return this;
    }

    public Plupload removeUploadProgressListener(UploadProgressListener listener) {
        this.uploadProgressListeners.remove(listener);
        return this;
    }

    public Plupload removeUploadStartListener(UploadStartListener listener) {
        this.uploadStartListeners.remove(listener);
        return this;
    }

    public Plupload removeUploadStopListener(UploadStopListener listener) {
        this.uploadStopListeners.remove(listener);
        return this;
    }

    public Plupload removeUploadCompleteListener(UploadCompleteListener listener) {
        this.uploadCompleteListeners.remove(listener);
        return this;
    }

    public Plupload removeErrorListener(ErrorListener listener) {
        this.errorListeners.remove(listener);
        return this;
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

    public interface UploadStopListener {

        void onUploadStop();
    }

    public interface UploadCompleteListener {

        void onUploadComplete();
    }

    public interface ErrorListener {

        void onError();
    }
}
