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
import java.io.Serializable;

/**
 *
 * @author exsio
 */
public class PluploadJSNIDelegate implements Serializable {

    public static native void click(Element button) 
    /*-{    
            button.click();
    }-*/;
    
    public static native void createUploader(Element button, PluploadServerRpc rpc, String uploaderKey) 
    /*-{
        $wnd.uploaders = $wnd.uploaders || {};
        if(typeof $wnd.uploaders[uploaderKey] === 'undefined') {
            var uploader = new $wnd.plupload.Uploader({
                browse_button: button,
                url: 'pluploader-upload-action',
                max_file_size : '1000mb',
                chunk_size: '1mb',
                max_retries: 3,
                runtimes: "html5",
                multi_selection: true,
                multipart: true
            });
            

            var forceRPCCall = function() {
                @pl.exsio.plupload.client.PluploadConnector::forceRPCCall()();
            };

            uploader.bind('FilesAdded', function(up, files) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::filesAdded(Ljava/lang/String;)(JSON.stringify(files));
                    forceRPCCall();
            });

            uploader.bind('FilesRemoved', function(up, files) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::filesRemoved(Ljava/lang/String;)(JSON.stringify(files));
                    forceRPCCall();
            });

            uploader.bind('FileFiltered', function(up, file) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::fileFiltered(Ljava/lang/String;)(JSON.stringify(file));
                    forceRPCCall();
            });

            uploader.bind('FileUploaded', function(up, file) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::fileUploaded(Ljava/lang/String;)(JSON.stringify(file));
                    forceRPCCall();
            });

            uploader.bind('UploadProgress', function(up, file) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::uploadProgress(Ljava/lang/String;)(JSON.stringify(file));
                    forceRPCCall();
            });

            uploader.bind('UploadComplete', function(up, files) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::uploadComplete()();
                    forceRPCCall();
            });

            uploader.bind('Error', function(up, files) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::error()();
                    forceRPCCall();
            });
            
            uploader.bind('Destroy', function(up, files) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::destroy()();
                    forceRPCCall();
            });
            
            uploader.bind('Init', function(up, files) {
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::init()();
                    forceRPCCall();
            });

            uploader.bind('BeforeUpload', function (up, file) {
                up.settings.multipart_params = {fileId: file.id}
            });      

            uploader.init();    
            $wnd.uploaders[uploaderKey] = uploader;    
        }
     }-*/;
    
    public static native void startUploader(String uploaderKey)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            $wnd.uploaders[uploaderKey].start();
       }
    }-*/;
    
    public static native void stopUploader(String uploaderKey)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            $wnd.uploaders[uploaderKey].stop();
       }
            
    }-*/;
    
    public static native void refreshUploader(String uploaderKey)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            $wnd.uploaders[uploaderKey].refresh();
       }
            
    }-*/;
    
    public static native void disableBrowse(String uploaderKey, boolean disable)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {}; 
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            $wnd.uploaders[uploaderKey].disableBrowse(disable);
       }    
    }-*/;
    
    public static native void setOption(String uploaderKey, String name, String value)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            var tryParseToJSON = function(jsonString) {
                 try {
                     var o = JSON.parse(jsonString);
                     if (o && typeof o === "object" && o !== null) {
                         return o;
                     }
                 }
                 catch (e) { }

                 return false;
             };     
            
            var isNumber = function(n) {
                return !isNaN(parseFloat(n)) && isFinite(n);
            }
            
            var optionValue = tryParseToJSON(value);
            if(typeof optionValue !== "object") {
                 if(value.toLowerCase() == "true") {
                    optionValue = true;
                 } else if(value.toLowerCase() == "false") {
                    optionValue = false;
                 } else if(isNumber(value)) {
                    optionValue = value * 1;
                 } else {
                    optionValue = value;
                 }   
            }
            console.log('Setting uploader "'+uploaderKey+'" option "'+name+'":');
            console.log(optionValue);
            $wnd.uploaders[uploaderKey].setOption(name, optionValue);
       }
    }-*/;
    
    public static native void removeFile(String uploaderKey, String fileId)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {    
            $wnd.uploaders[uploaderKey].removeFile($wnd.uploaders[uploaderKey].getFile(fileId));
       }   
    }-*/;
    
    
    public static native void destroyUploader(String uploaderKey)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            $wnd.uploaders[uploaderKey].destroy();
            delete $wnd.uploaders[uploaderKey];
       }   
    }-*/;
}
