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

    private static final String CACHE_PATH = System.getProperty("java.io.tmpdir") + File.separator;

    private static final int CACHE_SIZE = 1000 * (int) Math.pow(10, 6);

    private static final int MAX_REQUEST_SIZE = 1000 * (int) Math.pow(10, 6);

    private static final int MAX_FILE_SIZE = 1000 * (int) Math.pow(10, 6);

    protected final String uploaderId;

    protected final String uploadActionPath;

    protected final Map<String, File> uploadedFiles;

    protected final Map<String, PluploadProgress> progressMap;

    public PluploadReceiver(String uploaderId) {
        this.uploaderId = uploaderId;
        this.uploadActionPath = UPLOAD_ACTION_PATH + uploaderId;
        this.uploadedFiles = new HashMap<>();
        this.progressMap = new HashMap<>();
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

                        ServletFileUpload upload = this.createServletFileUpload();
                        List<FileItem> items = upload.parseRequest(req);
                        PluploadChunk chunk = this.createChunk(items);
                        PluploadProgress progress = this.getProgress(chunk);

                        if (progress.appendChunk(chunk)) {
                            this.uploadedFiles.put(chunk.getFileId(), progress.getUploadedFile());
                            this.progressMap.remove(chunk.getFileId());
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

    private PluploadProgress getProgress(PluploadChunk chunk) {
        PluploadProgress progress = null;
        if (!this.progressMap.containsKey(chunk.getFileId())) {
            progress = new PluploadProgress();
            this.progressMap.put(chunk.getFileId(), progress);
        } else {
            progress = this.progressMap.get(chunk.getFileId());
        }
        return progress;
    }

    private PluploadChunk createChunk(List<FileItem> items) throws NumberFormatException {
        PluploadChunk chunk = new PluploadChunk();
        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();

            if (item.isFormField()) {
                switch (item.getFieldName()) {
                    case "fileId":
                        chunk.setFileId(item.getString());
                        break;
                    case "name":
                        chunk.setName(item.getString());
                        break;
                    case "chunk":
                        chunk.setChunk(Integer.parseInt(item.getString()));
                        break;
                    case "chunks":
                        chunk.setChunks(Integer.parseInt(item.getString()));
                }

            } else {
                chunk.setData(item);
            }
        }
        return chunk;
    }

    private ServletFileUpload createServletFileUpload() {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(CACHE_PATH));
        factory.setSizeThreshold(CACHE_SIZE);
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setSizeMax(MAX_REQUEST_SIZE);
        upload.setFileSizeMax(MAX_FILE_SIZE);
        return upload;
    }

}
