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
package pl.exsio.plupload;

import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServletRequest;
import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
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
public class PluploadReceiver implements RequestHandler, Serializable {

    public static final String UPLOAD_ACTION_PATH = "pluploader-upload-action";

    private final Map<String, PluploadFileConfig> expectedFileIds = Collections.synchronizedMap(new HashMap());

    private final Map<String, File> uploadedFiles = Collections.synchronizedMap(new HashMap());

    private PluploadReceiver() {
    }

    public static PluploadReceiver getInstance() {
        VaadinSession session = VaadinSession.getCurrent();
        for (RequestHandler handler : session.getRequestHandlers()) {
            if (handler instanceof PluploadReceiver) {
                return (PluploadReceiver) handler;
            }
        }

        PluploadReceiver receiver = new PluploadReceiver();
        session.addRequestHandler(receiver);
        return receiver;
    }

    public synchronized File retrieveUploadedFile(String fileId) {
        if (this.uploadedFiles.containsKey(fileId)) {
            File file = this.uploadedFiles.get(fileId);
            this.uploadedFiles.remove(fileId);
            expectedFileIds.remove(fileId);
            return file;
        } else {
            return null;
        }
    }

    @Override
    public boolean handleRequest(VaadinSession session, VaadinRequest request, VaadinResponse response) throws IOException {
        if (request.getPathInfo() != null && request.getPathInfo().endsWith(UPLOAD_ACTION_PATH)) {
            if (request instanceof VaadinServletRequest) {
                VaadinServletRequest vsr = (VaadinServletRequest) request;
                HttpServletRequest req = vsr.getHttpServletRequest();
                if (ServletFileUpload.isMultipartContent(req)) {
                    try {
                        synchronized (this) {
                            ServletFileUpload upload = new ServletFileUpload();
                            FileItemIterator items = upload.getItemIterator(req);
                            PluploadChunk chunk = PluploadChunkFactory.create(items);
                            File targetFile = PluploadFileAppender.append(chunk);
                            if (chunk.isLast()) {
                                this.uploadedFiles.put(chunk.getFileId(), targetFile);
                                response.getWriter().append("file " + chunk.getName() + " uploaded successfuly");
                            } else {
                                response.getWriter().append("file chunk " + (chunk.getChunk() + 1) + " of " + chunk.getChunks() + " uploaded successfuly");
                            }
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

    public String getExpectedFilePath(String fileId) {
        if (this.isFileExpected(fileId)) {
            return this.expectedFileIds.get(fileId).uploadPath;
        } else {
            return null;
        }
    }

    public boolean isFileExpected(String fileId) {
        return this.expectedFileIds.containsKey(fileId);
    }

    public void addExpectedFile(String fileId, PluploadFileConfig config) {
        this.expectedFileIds.put(fileId, config);
    }

    public void removeExpectedFile(String fileId) {
        this.expectedFileIds.remove(fileId);
    }

    protected Map<String, File> getUploadedFiles() {
        return uploadedFiles;
    }

}
