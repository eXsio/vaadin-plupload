package pl.exsio.plupload;

import com.google.gson.Gson;
import com.vaadin.annotations.JavaScript;
import com.vaadin.ui.Button;
import java.util.LinkedHashSet;
import java.util.Set;
import pl.exsio.plupload.client.PluploadCilentRpc;
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

    public void start() {
        for (UploadStartListener listener : uploadStartListeners) {
            listener.onUploadStart();
        }
        this.getRpcProxy(PluploadCilentRpc.class).start();
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
