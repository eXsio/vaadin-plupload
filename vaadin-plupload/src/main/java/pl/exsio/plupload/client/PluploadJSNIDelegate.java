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
