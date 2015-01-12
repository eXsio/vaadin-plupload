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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.vaadin.client.annotations.OnStateChange;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.shared.PluploadState;

/**
 *
 * @author exsio
 */
@Connect(Plupload.class)
public class PluploadConnector extends ButtonConnector {

    protected final PluploadServerRpc serverRpc = RpcProxy.create(PluploadServerRpc.class, this);

    private Element uploadTrigger;

    protected String uploaderKey;

    public PluploadConnector() {
        this.registerRpc(PluploadCilentRpc.class, this.clientRpc);
        if (this.getState().uploaderKey != null) {
            this.attachUploader();
        }
    }

    @OnStateChange("uploaderKey")
    private void attachUploader() {
        this.uploaderKey = this.getState().uploaderKey;
        this.fetchUploadTrigger();
        getWidget().getElement().getOwnerDocument().getBody().appendChild(this.uploadTrigger);
        PluploadJSNIDelegate.createUploader(this.uploadTrigger, this.serverRpc, this.uploaderKey);
        getWidget().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                PluploadJSNIDelegate.click(uploadTrigger);
            }
        });
        this.getWidget().getElement().setAttribute("data-uploader-id", this.uploaderKey);
    }

    @OnStateChange("filters")
    void setFilters() {
        PluploadJSNIDelegate.setOption(uploaderKey, "filters", this.getState().filters);
    }

    @OnStateChange("resize")
    void setResize() {
        PluploadJSNIDelegate.setOption(uploaderKey, "resize", this.getState().resize);
    }

    @OnStateChange("chunkSize")
    void setChunkSize() {
        PluploadJSNIDelegate.setOption(uploaderKey, "chunk_size", this.getState().chunkSize);
    }

    @OnStateChange("maxFileSize")
    void setMaxFileSize() {
        PluploadJSNIDelegate.setOption(uploaderKey, "max_file_size", this.getState().maxFileSize);
    }

    @OnStateChange("maxRetries")
    void setMaxRetries() {
        PluploadJSNIDelegate.setOption(uploaderKey, "max_retries", Integer.toString(this.getState().maxRetries));
    }

    @OnStateChange("multiSelection")
    void setMultiSelection() {
        PluploadJSNIDelegate.setOption(uploaderKey, "multi_selection", Boolean.toString(this.getState().multiSelection));
    }

    @OnStateChange("preventDuplicates")
    void setPreventDuplicates() {
        PluploadJSNIDelegate.setOption(uploaderKey, "prevent_duplicates", Boolean.toString(this.getState().preventDuplicates));
    }

    private void fetchUploadTrigger() {
        String triggerId = this.getUploadTriggerId();
        Element trigger = DOM.getElementById(triggerId);
        if (trigger == null) {
            trigger = this.createUploadTrigger(trigger, triggerId);
        }
        this.uploadTrigger = trigger;
    }

    private String getUploadTriggerId() {
        String triggerId = "upload-trigger-" + this.uploaderKey;
        return triggerId;
    }

    private Element createUploadTrigger(Element trigger, String triggerId) {
        trigger = DOM.createButton();
        trigger.setAttribute("id", triggerId);
        trigger.setAttribute("style", "display:none;");
        return trigger;
    }

    /**
     * This is a hack to force the RPC call. This is done, because if the upload
     * progress isn't immediate, the client side of Vaadin sends RPC updates
     * only after user input (mouse move or keypress). If I'll find a better way
     * to handle this, I'll change it, but for now it has to be this way
     */
    public static void forceRPCCall() {
        Document doc = Document.get();
        doc.getBody().dispatchEvent(doc.createMouseMoveEvent(0, 0, 0, 0, 0, false, false, false, false, 0));
    }

    protected PluploadCilentRpc clientRpc = new PluploadCilentRpc() {

        @Override
        public void start() {
            PluploadJSNIDelegate.startUploader(uploaderKey);
        }

        @Override
        public void stop() {
            PluploadJSNIDelegate.stopUploader(uploaderKey);
        }

        @Override
        public void disableBrowse(boolean disable) {
            PluploadJSNIDelegate.disableBrowse(uploaderKey, disable);
        }

        @Override
        public void removeFile(String fileId) {
            PluploadJSNIDelegate.removeFile(uploaderKey, fileId);
        }

        @Override
        public void refresh() {
            PluploadJSNIDelegate.refreshUploader(uploaderKey);
        }

        @Override
        public void destroy() {
            PluploadJSNIDelegate.destroyUploader(uploaderKey);
            getWidget().getElement().getOwnerDocument().getBody().removeChild(uploadTrigger);
        }

    };

    @Override
    public PluploadWidget getWidget() {
        return (PluploadWidget) super.getWidget();
    }

    @Override
    public PluploadState getState() {
        return (PluploadState) super.getState();
    }

}
