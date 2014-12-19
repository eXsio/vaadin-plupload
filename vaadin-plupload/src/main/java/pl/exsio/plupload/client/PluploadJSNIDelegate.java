package pl.exsio.plupload.client;

import com.google.gwt.dom.client.Element;
import pl.exsio.plupload.server.PluploadServerRpc;

/**
 *
 * @author exsio
 */
public class PluploadJSNIDelegate {

    public static native void initUploader(Element button, PluploadServerRpc rpc) 
    /*-{

        this.uploader = new $wnd.plupload.Uploader({
            browse_button: button,
            url: 'dummypath',
            max_file_size : '1000mb',
            chunk_size: '1mb',
            max_retries: 3
        });

        this.uploader.bind('FilesAdded', function(up, files) {
                console.info(arguments);
                rpc.@pl.exsio.plupload.server.PluploadServerRpc::filesAdded(Ljava/lang/String;)(JSON.stringify(files));
        });

        this.uploader.bind('FilesRemoved', function(up, files) {
                console.info(arguments);
                rpc.@pl.exsio.plupload.server.PluploadServerRpc::filesRemoved(Ljava/lang/String;)(JSON.stringify(files));
        });

        this.uploader.bind('FileFiltered', function(up, file) {
                console.info(arguments);
                rpc.@pl.exsio.plupload.server.PluploadServerRpc::fileFiltered(Ljava/lang/String;)(JSON.stringify(file));
        });

        this.uploader.bind('FileUploaded', function(up, file) {
                console.info(arguments);
                rpc.@pl.exsio.plupload.server.PluploadServerRpc::fileUploaded(Ljava/lang/String;)(JSON.stringify(file));
        });

        this.uploader.bind('UploadProgress', function(up, file) {
                console.info(arguments);
                rpc.@pl.exsio.plupload.server.PluploadServerRpc::uploadProgress(Ljava/lang/String;)(JSON.stringify(file));
        });

        this.uploader.bind('UploadComplete', function(up, files) {
                console.info(arguments);
                rpc.@pl.exsio.plupload.server.PluploadServerRpc::uploadComplete()();
        });

        this.uploader.bind('Error', function(up, files) {
                console.info(arguments);
                rpc.@pl.exsio.plupload.server.PluploadServerRpc::error()();
        });

        this.uploader.init();
            
     }-*/;
    
    public static native void startUploader()
    /*-{
            
       this.uploader.start();
            
    }-*/;
}
