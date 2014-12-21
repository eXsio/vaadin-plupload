/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.plupload;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sdymi_000
 */
public class PluploadReceiver implements RequestHandler {

    protected static final String UPLOAD_ACTION_PATH = "//pluploader-upload-action/";

    protected final String uploaderId;

    protected final String uploadActionPath;

    protected final Map<String, File> uploadedFiles;

    public PluploadReceiver(String uploaderId) {
        this.uploaderId = uploaderId;
        this.uploadActionPath = UPLOAD_ACTION_PATH + uploaderId;
        this.uploadedFiles = new HashMap<>();
    }

    public File getUploadedFile(String fileId) {
        if (this.uploadedFiles.containsKey(fileId)) {
            return this.uploadedFiles.get(fileId);
        } else {
            return null;
        }
    }

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {
        System.out.println("request handler: " + request.getPathInfo());
        if (this.uploadActionPath.equals(request.getPathInfo())) {
            response.setContentType("text/plain");
            response.getWriter().append(
                    "upload stub");
            return true;
        } else {
            return false;
        }
    }

}
