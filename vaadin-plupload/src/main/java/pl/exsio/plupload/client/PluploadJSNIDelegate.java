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
package pl.exsio.plupload.client;

import com.google.gwt.dom.client.Element;
import java.io.Serializable;

/**
 *
 * @author exsio
 */
public abstract class PluploadJSNIDelegate implements Serializable {

    public static native void click(Element button) 
    /*-{    
            button.click();
    }-*/;
    
    public static native void attachUploader(Element uploadTrigger, PluploadServerRpc rpc, String uploaderKey, Element dropZone) 
    /*-{
        $wnd.uploaders = $wnd.uploaders || {};
        if(typeof $wnd.uploaders[uploaderKey] === 'undefined') {
            @com.vaadin.client.VConsole::log(Ljava/lang/String;)('creating uploader "'+uploaderKey+'"');
            var uploader = new $wnd.plupload.Uploader({
                browse_button: uploadTrigger,
                drop_element: dropZone,
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
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('files have been added to uploader "'+uploaderKey+'"');
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::filesAdded(Ljava/lang/String;)(JSON.stringify(files));
                    forceRPCCall();
            });

            uploader.bind('FilesRemoved', function(up, files) {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('files have been removed from uploader "'+uploaderKey+'"');
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::filesRemoved(Ljava/lang/String;)(JSON.stringify(files));
                    forceRPCCall();
            });

            uploader.bind('FileFiltered', function(up, file) {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('files have been filtered in uploader "'+uploaderKey+'"');
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::fileFiltered(Ljava/lang/String;)(JSON.stringify(file));
                    forceRPCCall();
            });

            uploader.bind('FileUploaded', function(up, file) {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('file "'+file.id+'" has been uploaded in uploader "'+uploaderKey+'"');
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::fileUploaded(Ljava/lang/String;)(JSON.stringify(file));
                    forceRPCCall();
            });

            uploader.bind('UploadProgress', function(up, file) {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('there was an upload progress in uploader "'+uploaderKey+'", fileId: "'+file.id+'", progress: '+file.percent+'%');
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::uploadProgress(Ljava/lang/String;)(JSON.stringify(file));
                    forceRPCCall();
            });

            uploader.bind('UploadComplete', function() {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('upload was completed in uploader "'+uploaderKey+'"');
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::uploadComplete()();
                    forceRPCCall();
            });

            uploader.bind('Error', function(up, error) {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('there was an error in uploader "'+uploaderKey+'": '+error.message);
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::error(Ljava/lang/String;)(JSON.stringify(error));
                    forceRPCCall();
            });
            
            uploader.bind('Destroy', function() {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('uploader "'+uploaderKey+'" was destroyed');
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::destroy()();
                    forceRPCCall();
            });
            
            uploader.bind('Init', function() {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('uploader "'+uploaderKey+'" was initialized');
                    rpc.@pl.exsio.plupload.client.PluploadServerRpc::init()();
                    forceRPCCall();
            });

            uploader.bind('BeforeUpload', function (up, file) {
                up.settings.multipart_params = {fileId: file.id}
            });      

            uploader.init();    
            $wnd.uploaders[uploaderKey] = uploader;    
        } else {
            @com.vaadin.client.VConsole::log(Ljava/lang/String;)('attaching previously initialized uploader "'+uploaderKey+'"');
        }
     }-*/;
    
    public static native void startUploader(String uploaderKey)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {
            @com.vaadin.client.VConsole::log(Ljava/lang/String;)('starting uploader "'+uploaderKey+'"');
            $wnd.uploaders[uploaderKey].start();
       }
    }-*/;
    
    public static native void stopUploader(String uploaderKey)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            @com.vaadin.client.VConsole::log(Ljava/lang/String;)('stopping uploader "'+uploaderKey+'"');
            $wnd.uploaders[uploaderKey].stop();
       }
            
    }-*/;
    
    public static native void refreshUploader(String uploaderKey)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            @com.vaadin.client.VConsole::log(Ljava/lang/String;)('refreshing uploader "'+uploaderKey+'"');
            $wnd.uploaders[uploaderKey].refresh();
       }
            
    }-*/;
    
    public static native void disableBrowse(String uploaderKey, boolean disable)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {}; 
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            @com.vaadin.client.VConsole::log(Ljava/lang/String;)('Toggling uploader "'+uploaderKey+'"');
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
            @com.vaadin.client.VConsole::log(Ljava/lang/String;)('Setting uploader "'+uploaderKey+'" option "'+name+'": '+value);
            $wnd.uploaders[uploaderKey].setOption(name, optionValue);
       }
    }-*/;
    
    public static native void removeFile(String uploaderKey, String fileId)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {
            var file = $wnd.uploaders[uploaderKey].getFile(fileId);
            if(typeof file !=='undefined') {
                @com.vaadin.client.VConsole::log(Ljava/lang/String;)('removing uploader "'+uploaderKey+'" file "'+fileId+'"');
                $wnd.uploaders[uploaderKey].removeFile(file);
            } else {
                @com.vaadin.client.VConsole::error(Ljava/lang/String;)('Couldn\'t find file "'+fileId+'" in uploader "'+uploaderKey+'"');
            }
       }   
    }-*/;
    
    
    public static native void destroyUploader(String uploaderKey)
    /*-{
       $wnd.uploaders = $wnd.uploaders || {};
       if(typeof $wnd.uploaders[uploaderKey] === 'object') {     
            @com.vaadin.client.VConsole::log(Ljava/lang/String;)('destroying uploader "'+uploaderKey+'"');
            $wnd.uploaders[uploaderKey].destroy();
            delete $wnd.uploaders[uploaderKey];
       }   
    }-*/;
    
    
    public static native void addDropZone(String uploaderKey, String dropZoneId)
    /*-{
            $wnd.uploaders = $wnd.uploaders || {};
            if(typeof $wnd.uploaders[uploaderKey] === 'object') {    
                var uploader = $wnd.uploaders[uploaderKey];
                var element = $doc.getElementById(dropZoneId);
                if(typeof element !== 'undefined') {
                    @com.vaadin.client.VConsole::log(Ljava/lang/String;)('adding new drop zone with id "'+dropZoneId+'" to uploader "'+uploaderKey+'"');
                    var dropzone = new $wnd.mOxie.FileDrop({
                        drop_zone: element
                    }); 
                    dropzone.ondrop = function( event ) {
                        uploader.addFile( dropzone.files );
                    };
                    dropzone.init(); 
                    uploader.dropZones = uploader.dropZones || [];
                    uploader.dropZones.push(dropzone);
                } else {
                    @com.vaadin.client.VConsole::error(Ljava/lang/String;)('cannot add new drop zone to uploader "'+uploaderKey+'", because there is no element with id "'+dropZoneId+'"');
                }
                
            }
    }-*/;
            
}
