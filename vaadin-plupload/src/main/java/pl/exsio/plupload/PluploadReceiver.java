/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.plupload;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author sdymi_000
 */
public class PluploadReceiver implements RequestHandler {

    protected static final String UPLOAD_ACTION_PATH = "//pluploader-upload-action/";

    private static final String CACHE_PATH = "./temp/";
    private static final int CACHE_SIZE = 1000 * (int) Math.pow(10, 6);
    private static final int MAX_REQUEST_SIZE = 100 * (int) Math.pow(10, 6);
    private static final int MAX_FILE_SIZE = 100 * (int) Math.pow(10, 6);

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

        if (this.uploadActionPath.equals(request.getPathInfo())) {
            if (request instanceof VaadinServletRequest) {
                VaadinServletRequest vsr = (VaadinServletRequest) request;
                HttpServletRequest req = vsr.getHttpServletRequest();
                if (ServletFileUpload.isMultipartContent(req)) {
                    try {
                        DiskFileItemFactory factory = new DiskFileItemFactory();
                        // Set factory constraints
                        factory.setRepository(new File(CACHE_PATH));
                        factory.setSizeThreshold(CACHE_SIZE);
                        
                        // Create a new file upload handler
                        ServletFileUpload upload = new ServletFileUpload(factory);
                        // Set overall request size constraint
                        upload.setSizeMax(MAX_REQUEST_SIZE);
                        upload.setFileSizeMax(MAX_FILE_SIZE);
                        
                        // Parse the request
                        @SuppressWarnings("unchecked")
                                List<FileItem> items = upload.parseRequest(req);
                        
                        // Process the uploaded items
                        Iterator iter = items.iterator();
                        while (iter.hasNext()) {
                            FileItem item = (FileItem) iter.next();
                            
                            if (item.isFormField()) {
                                System.out.println(item.getFieldName()+": "+item.getString());
                            } else {
                                System.out.println("Data: "+ item.getSize());
                            }
                        }
                    } catch (FileUploadException ex) {
                        throw new IOException(ex);
                    }
                }
            }

            response.setContentType("text/plain");
            response.getWriter().append(
                    "upload stub");
            return true;
        } else {
            return false;
        }
    }

}
