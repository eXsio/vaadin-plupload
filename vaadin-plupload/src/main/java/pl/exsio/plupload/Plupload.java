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

import pl.exsio.plupload.helper.filter.PluploadFilter;
import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.server.Resource;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Button;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import pl.exsio.plupload.client.PluploadCilentRpc;
import pl.exsio.plupload.shared.PluploadState;
import pl.exsio.plupload.client.PluploadServerRpc;
import pl.exsio.plupload.ex.InvalidDropZoneIdException;
import pl.exsio.plupload.handler.PluploadChunkHandlerFactory;
import pl.exsio.plupload.handler.file.FileAppendingChunkHandlerFactory;
import pl.exsio.plupload.helper.filter.PluploadFilters;
import pl.exsio.plupload.helper.resize.PluploadImageResize;

/**
 *
 * @author exsio
 */
@JavaScript({
    "client/js/plupload.js"
})
public class Plupload extends Button {

    protected boolean uploadStarted = false;

    protected final PluploadQueue queue = new PluploadQueue();

    protected final PluploadReceiver receiver = PluploadReceiver.getInstance();

    protected final PluploadFilters filters = new PluploadFilters();

    protected PluploadImageResize resize = new PluploadImageResize();

    protected PluploadChunkHandlerFactory chunkHandlerFactory = new FileAppendingChunkHandlerFactory(System.getProperty("java.io.tmpdir"));

    protected String chunkSize = "1mb";

    protected String maxFileSize = "1000mb";

    protected boolean multiSelection = true;

    protected boolean preventDuplicates = false;

    protected int maxRetries = 3;

    protected final String uploaderKey
            = (new Random().nextInt(Integer.MAX_VALUE)
            + new Random().nextInt(Integer.MAX_VALUE)
            + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())
            + new Random().nextInt(Integer.MAX_VALUE)
            + new Random().nextInt(Integer.MAX_VALUE)
            + new Random().nextInt(Integer.MAX_VALUE)).substring(2, 42);

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

    protected enum Option {

        RESIZE("resize"),
        FILTERS("filters"),
        MAX_RETRIES("max_retries"),
        MULTI_SELECTION("multi_selection"),
        PREVENT_DUPLICATES("prevent_duplicates"),
        CHUNK_SIZE("chunk_size"),
        MAX_FILE_SIZE("max_file_size");

        private final String name;

        private Option(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    private void postConstruct() {

        this.setImmediate(true);
        this.getState().uploaderKey = this.uploaderKey;
        this.handleFilesAdded();
        this.handleFilesRemoved();
        this.handleFileUploaded();
        this.handleUploadComplete();
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
                Object uploadedFile = queue.getFileHandler(file.getId()).getUploadedFile();
                queue.setUploadedFile(file.getId(), uploadedFile);
                file.setUploadedFile(uploadedFile);
                file.setUploaded(true);
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
        final Plupload self = this;
        this.addFilesAddedListener(new FilesAddedListener() {

            @Override
            public void onFilesAdded(PluploadFile[] files) {
                for (PluploadFile file : files) {
                    queue.addFile(file, chunkHandlerFactory.create(file, self));
                }
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
        public void error(String json) {
            PluploadError error = new Gson().fromJson(json, PluploadError.class);
            error.setType();
            for (ErrorListener listener : errorListeners) {
                listener.onError(error);
            }
        }

        @Override
        public void fileFiltered(String json) {
            PluploadFile file = new Gson().fromJson(json, PluploadFile.class);
            for (FileFilteredListener listener : fileFilteredListeners) {
                listener.onFileFiltered(file);
            }
        }

        @Override
        public void destroy() {
            for (DestroyListener listener : destroyListeners) {
                listener.onDestroy();
            }
        }

        @Override
        public void init() {
            for (InitListener listener : initListeners) {
                listener.onInit();
            }
        }
    };

    /**
     * Disable the browse button on the client side
     *
     * @param disable
     * @return
     */
    public Plupload disableBrowse(boolean disable) {
        this.getClient().disableBrowse(disable);
        return this;
    }

    /**
     * Remove a file from the upload queue
     *
     * @param fileId
     * @return
     */
    public Plupload removeFile(String fileId) {
        this.getClient().removeFile(fileId);
        return this;
    }

    /**
     * Add new Filter to the Uploader
     *
     * @param filter
     * @return
     */
    public Plupload addFilter(PluploadFilter filter) {
        this.filters.add(filter);
        this.getClient().setOption(Option.FILTERS.toString(), new Gson().toJson(this.filters));
        return this;
    }

    /**
     * Remove a Filter from the Uploader
     *
     * @param filter
     * @return
     */
    public Plupload removeFilter(PluploadFilter filter) {
        this.filters.remove(filter);
        this.getClient().setOption(Option.FILTERS.toString(), new Gson().toJson(this.filters));
        return this;
    }

    /**
     * Return an array of currently active Uploader Filters
     *
     * @return
     */
    public PluploadFilter[] getFilters() {
        return this.filters.get();
    }

    /**
     * Return current configuration of client-side Image resizing
     *
     * @return
     */
    public PluploadImageResize getImageResize() {
        return this.resize;
    }

    /**
     * Set the new configuration of client-size Image resizing
     *
     * @param resize
     * @return
     */
    public Plupload setImageResize(PluploadImageResize resize) {
        this.resize = resize;
        this.getClient().setOption(Option.RESIZE.toString(), new Gson().toJson(resize));
        return this;
    }

    /**
     * Set the desired Chunk size. Defaults to "1mb"
     *
     * @param chunkSize
     * @return
     */
    public Plupload setChunkSize(String chunkSize) {
        this.chunkSize = chunkSize;
        this.getClient().setOption(Option.CHUNK_SIZE.toString(), chunkSize);
        return this;
    }

    /**
     * Get the currently configured Chunk size
     *
     * @return
     */
    public String getChunkSize() {
        return this.chunkSize;
    }

    /**
     * Add additional drop zone by it's Id. The Id cannot be empty and must be
     * unique.
     *
     * @param dropZoneId
     * @return
     */
    public Plupload addDropZone(String dropZoneId) {
        if (dropZoneId != null && !"".equals(dropZoneId)) {
            this.getClient().addDropZone(dropZoneId);
            return this;
        } else {
            throw new InvalidDropZoneIdException("Drop zone must have an non-empty id");
        }
    }

    /**
     * Add additional drop zone by component. The component must have an
     * non-empty and unique Id.
     *
     * @param component
     * @return
     */
    public Plupload addDropZone(AbstractComponent component) {
        return this.addDropZone(component.getId());
    }

    /**
     * Set maximum file size accepted by this Uploader. Defaults to "1000mb"
     *
     * @param maxFileSize
     * @return
     */
    public Plupload setMaxFileSize(String maxFileSize) {
        this.maxFileSize = maxFileSize;
        this.getClient().setOption(Option.MAX_FILE_SIZE.toString(), maxFileSize);
        return this;
    }

    /**
     * Get currently configured maximum file size accepred by this Uploader
     *
     * @return
     */
    public String getMaxFileSize() {
        return this.maxFileSize;
    }

    /**
     * Get currently configured duplicates prevention setting
     *
     * @return
     */
    public boolean isPreventDuplicates() {
        return this.preventDuplicates;
    }

    /**
     * Decide, whether the Upoader should prevend adding duplicate copies of the
     * same file to the queue. Defaults to false
     *
     * @param prevent
     * @return
     */
    public Plupload setPreventDuplicates(boolean prevent) {
        this.preventDuplicates = prevent;
        this.getClient().setOption(Option.PREVENT_DUPLICATES.toString(), Boolean.toString(prevent));
        return this;
    }

    /**
     * Decide how many times the Uploader shoud retry sending of the Chunk.
     * Defaults to 5
     *
     * @param maxRetries
     * @return
     */
    public Plupload setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        this.getClient().setOption(Option.MAX_RETRIES.toString(), Integer.toString(maxRetries));
        return this;
    }

    /**
     * Return currently configured max retries option
     *
     * @return
     */
    public int getMaxRetries() {
        return this.maxRetries;
    }

    /**
     * Decide, if user should be able to select multiple files in the Browse
     * window. Defaults to true
     *
     * @param multiSelection
     * @return
     */
    public Plupload setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection;
        this.getClient().setOption(Option.MULTI_SELECTION.toString(), Boolean.toString(multiSelection));
        return this;
    }

    /**
     * Check, if user can select multiple files in the Browse window
     *
     * @return
     */
    public boolean isMultiSelection() {
        return this.multiSelection;
    }

    /**
     * Get an uniqie uploader key
     *
     * @return
     */
    public String getUploaderKey() {
        return uploaderKey;
    }

    /**
     * Start the upload process
     *
     * @return
     */
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

    /**
     * Refresh the client-side uploader
     *
     * @return
     */
    public Plupload refresh() {
        this.getClient().refresh();
        return this;
    }

    /**
     * Destroy the client side uploder. After calling this method the uploader
     * is no longer usable
     *
     * @return
     */
    public Plupload destroy() {
        this.getClient().destroy();
        return this;
    }

    /**
     * Get files uploaded by this Uploader
     *
     * @return
     */
    public PluploadFile[] getUploadedFiles() {
        Set<PluploadFile> files = this.queue.getPluploadFiles(PluploadQueue.Mode.UPLOADED);
        return files.toArray(new PluploadFile[files.size()]);
    }

    /**
     * Get files, that were added to the queue but not yet uploaded
     *
     * @return
     */
    public PluploadFile[] getQueuedFiles() {
        Set<PluploadFile> files = this.queue.getPluploadFiles(PluploadQueue.Mode.NOT_UPLOADED);
        return files.toArray(new PluploadFile[files.size()]);
    }

    /**
     * Stops the current upload process
     *
     * @return
     */
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

    /**
     * Get currently configured chunk handler factory.
     *
     * @return
     */
    public PluploadChunkHandlerFactory getChunkHandlerFactory() {
        return chunkHandlerFactory;
    }

    /**
     * Set desired chunk handler factory. Defaults to
     * FileAppendingChunkHandlerFactory
     *
     * @param chunkHandlerFactory
     */
    public void setChunkHandlerFactory(PluploadChunkHandlerFactory chunkHandlerFactory) {
        this.chunkHandlerFactory = chunkHandlerFactory;
    }

    protected PluploadCilentRpc getClient() {
        return this.getRpcProxy(PluploadCilentRpc.class);
    }

    private final Set<FilesAddedListener> filesAddedListeners = new LinkedHashSet<>();

    private final Set<UploadProgressListener> uploadProgressListeners = new LinkedHashSet<>();

    private final Set<FilesRemovedListener> filesRemovedListeners = new LinkedHashSet<>();

    private final Set<FileFilteredListener> fileFilteredListeners = new LinkedHashSet<>();

    private final Set<FileUploadedListener> fileUploadedListeners = new LinkedHashSet<>();

    private final Set<ErrorListener> errorListeners = new LinkedHashSet<>();

    private final Set<DestroyListener> destroyListeners = new LinkedHashSet<>();

    private final Set<InitListener> initListeners = new LinkedHashSet<>();

    private final Set<UploadStartListener> uploadStartListeners = new LinkedHashSet<>();

    private final Set<UploadStopListener> uploadStopListeners = new LinkedHashSet<>();

    private final Set<UploadCompleteListener> uploadCompleteListeners = new LinkedHashSet<>();

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

    public Plupload addDestroyListener(DestroyListener listener) {
        this.destroyListeners.add(listener);
        return this;
    }

    public Plupload addInitListener(InitListener listener) {
        this.initListeners.add(listener);
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

    public Plupload removeInitListener(InitListener listener) {
        this.initListeners.remove(listener);
        return this;
    }

    public Plupload removeDestroyListener(DestroyListener listener) {
        this.destroyListeners.remove(listener);
        return this;
    }

    public interface FilesAddedListener extends Serializable {

        void onFilesAdded(PluploadFile[] files);
    }

    public interface FilesRemovedListener extends Serializable {

        void onFilesRemoved(PluploadFile[] files);
    }

    public interface FileFilteredListener extends Serializable {

        void onFileFiltered(PluploadFile file);
    }

    public interface FileUploadedListener extends Serializable {

        void onFileUploaded(PluploadFile file);
    }

    public interface UploadStartListener extends Serializable {

        void onUploadStart();
    }

    public interface UploadProgressListener extends Serializable {

        void onUploadProgress(PluploadFile file);
    }

    public interface UploadStopListener extends Serializable {

        void onUploadStop();
    }

    public interface UploadCompleteListener extends Serializable {

        void onUploadComplete();
    }

    public interface ErrorListener extends Serializable {

        void onError(PluploadError error);
    }

    public interface DestroyListener extends Serializable {

        void onDestroy();
    }

    public interface InitListener extends Serializable {

        void onInit();
    }
}
