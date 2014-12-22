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
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 *
 * @author exsio
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

        if (this.uploadActionPath.equals(request.getPathInfo())) {
            if (request instanceof VaadinServletRequest) {
                VaadinServletRequest vsr = (VaadinServletRequest) request;
                HttpServletRequest req = vsr.getHttpServletRequest();
                if (ServletFileUpload.isMultipartContent(req)) {
                    try {

                        ServletFileUpload upload = new ServletFileUpload();
                        FileItemIterator items = upload.getItemIterator(req);
                        PluploadChunk chunk = PluploadAppender.appendData(items);

                        if (chunk.isLast()) {
                            this.uploadedFiles.put(chunk.getFileId(), chunk.getFile());
                            response.getWriter().append("file " + chunk.getName() + " uploaded successfuly");
                        } else {
                            response.getWriter().append("file chunk " + (chunk.getChunk() + 1) + " of " + chunk.getChunks() + " uploaded successfuly");
                        }
                        response.setContentType("text/plain");
                    } catch (FileUploadException ex) {
                        response.getWriter().append("file upload unsuccessful");
                        throw new IOException(ex);
                    }
                    return true;
                }
            }
        }
        return false;

    }

}
