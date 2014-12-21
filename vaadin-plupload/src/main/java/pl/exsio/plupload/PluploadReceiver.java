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
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author exsio
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
