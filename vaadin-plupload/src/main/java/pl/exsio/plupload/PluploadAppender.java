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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.util.Streams;

/**
 *
 * @author exsio
 */
public abstract class PluploadAppender implements Serializable {

    public static PluploadChunk appendData(FileItemIterator items) throws IOException, FileUploadException {
        PluploadChunk chunk = new PluploadChunk();
        while (items.hasNext()) {
            FileItemStream item = (FileItemStream) items.next();

            if (item.isFormField()) {
                setChunkField(chunk, item);
            } else {
                saveChunkData(chunk, item);
            }
        }
        return chunk;
    }

    private static void setChunkField(PluploadChunk chunk, FileItemStream item) throws NumberFormatException, IOException {
        String value = Streams.asString(item.openStream());
        switch (item.getFieldName()) {
            case "fileId":
                chunk.setFileId(value);
                break;
            case "name":
                chunk.setName(value);
                break;
            case "chunk":
                chunk.setChunk(Integer.parseInt(value));
                break;
            case "chunks":
                chunk.setChunks(Integer.parseInt(value));
        }
    }

    private static void saveChunkData(PluploadChunk chunk, FileItemStream item) throws IOException {
        if (getReceiver().isFileExpected(chunk.getFileId())) {
            String uploadedFileName = getFileName(chunk);
            String filePath = getFilePath(chunk, uploadedFileName);
            try (InputStream input = item.openStream();
                    FileOutputStream output = new FileOutputStream(filePath, true)) {
                copyInputStreamToOutputStream(input, output);
                output.close();
            }
            chunk.setFile(new File(filePath));
        }
    }

    private static String getFilePath(PluploadChunk chunk, String uploadedFileName) {
        return getReceiver().getExpectedFilePath(chunk.getFileId()) + File.separator + uploadedFileName;
    }

    private static void copyInputStreamToOutputStream(InputStream input, final FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    protected static PluploadReceiver getReceiver() {
        return PluploadReceiver.getInstance();
    }

    protected static String getFileName(PluploadChunk chunk) {
        String[] arr = chunk.getName().split("\\.");
        StringBuilder name = new StringBuilder(chunk.getFileId());
        if (arr.length > 1) {
            name.append(".").append(arr[arr.length - 1]);
        }
        return name.toString();
    }

}
