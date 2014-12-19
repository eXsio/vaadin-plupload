package pl.exsio.plupload.server;

import com.vaadin.shared.communication.ServerRpc;

/**
 *
 * @author exsio
 */
public interface PluploadServerRpc extends ServerRpc {

    void filesAdded(String json);

    void uploadProgress(String json);

    void uploadComplete();

    void filesRemoved(String json);

    void fileUploaded(String json);

    void error();

    void fileFiltered(String json);
}
