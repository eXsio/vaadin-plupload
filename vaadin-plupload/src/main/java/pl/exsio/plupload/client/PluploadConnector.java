package pl.exsio.plupload.client;

import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.ui.button.ButtonConnector;
import com.vaadin.shared.ui.Connect;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.server.PluploadServerRpc;

/**
 *
 * @author exsio
 */
@Connect(Plupload.class)
public class PluploadConnector extends ButtonConnector {

    protected PluploadCilentRpc clientRpc = new PluploadCilentRpc() {

        @Override
        public void start() {
            PluploadJSNIDelegate.startUploader();
        }

    };

    protected PluploadServerRpc serverRpc = RpcProxy.create(PluploadServerRpc.class, this);

    public PluploadConnector() {
        super();
        registerRpc(PluploadCilentRpc.class, clientRpc);
        PluploadJSNIDelegate.initUploader(getWidget().getElement(), serverRpc);
    }

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
