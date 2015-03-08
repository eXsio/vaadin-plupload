/*
 * The MIT License
 *
 * Copyright 2015 sdymi_000.
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
package pl.exsio.plupload.handler.file;

import java.io.File;
import java.io.FileOutputStream;
import pl.exsio.plupload.PluploadChunk;
import pl.exsio.plupload.handler.PluploadChunkHandler;
import pl.exsio.plupload.util.PluploadUtil;

/**
 *
 * @author eXsio
 */
public class FileAppendingChunkHandler implements PluploadChunkHandler<File> {

    protected String uploadPath;

    protected File uploadedFile;

    public FileAppendingChunkHandler(String uploadPath) {
        this.uploadPath = uploadPath;
    }

    @Override
    public void handleUploadedChunk(PluploadChunk chunk) throws Exception {
        String uploadedFileName = this.getFileName(chunk);
        String filePath = this.getFilePath(uploadedFileName);
        if (chunk.getInputStream() != null) {
            try (FileOutputStream output = new FileOutputStream(filePath, true)) {
                PluploadUtil.copyInputStreamToOutputStream(chunk.getInputStream(), output);
                output.close();
            }
        }
        if (chunk.isLast()) {
            this.uploadedFile = new File(filePath);
        }
    }

    @Override
    public File getUploadedFile() {
        return this.uploadedFile;
    }

    protected String getFilePath(String uploadedFileName) {
        return this.uploadPath + File.separator + uploadedFileName;
    }

    protected String getFileName(PluploadChunk chunk) {
        String[] arr = chunk.getName().split("\\.");
        StringBuilder name = new StringBuilder(chunk.getFileId());
        if (arr.length > 1) {
            name.append(".").append(arr[arr.length - 1]);
        }
        return name.toString();
    }

}
