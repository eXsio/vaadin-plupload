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

import pl.exsio.plupload.client.shared.PluploadState;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;
import java.util.Random;
import pl.exsio.plupload.Plupload;

/**
 *
 * @author exsio
 */
@Connect(Plupload.class)
public class PluploadConnector extends ButtonConnector {

    protected PluploadServerRpc serverRpc = RpcProxy.create(PluploadServerRpc.class, this);

    protected final String uploaderKey = "" + (new Random().nextInt(Integer.MAX_VALUE))
            + (new Random().nextInt(Integer.MAX_VALUE))
            + (new Random().nextInt(Integer.MAX_VALUE));

    public PluploadConnector() {
        super();
        registerRpc(PluploadCilentRpc.class, clientRpc);
        PluploadJSNIDelegate.createUploader(this.getWidget().getElement(), serverRpc, this.uploaderKey);
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
        public void setOption(String name, String value) {
            PluploadJSNIDelegate.setOption(uploaderKey, name, value);
        }

        @Override
        public void removeFile(String fileId) {
            PluploadJSNIDelegate.removeFile(uploaderKey, fileId);
        }

    };

    @Override
    public void init() {
        super.init();
    }

    @Override
    public PluploadWidget getWidget() {
        return (PluploadWidget) super.getWidget();
    }

    @Override
    public PluploadState getState() {
        return (PluploadState) super.getState();
    }

}
