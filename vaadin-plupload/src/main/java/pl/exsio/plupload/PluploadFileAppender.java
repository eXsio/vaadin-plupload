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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import org.apache.commons.fileupload.FileUploadException;
import pl.exsio.plupload.util.PluploadUtil;

/**
 *
 * @author exsio
 */
public abstract class PluploadFileAppender implements Serializable {

    public static File append(PluploadChunk chunk) throws IOException, FileUploadException {
        String uploadedFileName = getFileName(chunk);
        String filePath = getFilePath(chunk, uploadedFileName);
        if (chunk.getInputStream() != null) {
            try (FileOutputStream output = new FileOutputStream(filePath, true)) {
                PluploadUtil.copyInputStreamToOutputStream(chunk.getInputStream(), output);
                output.close();
            }

        }
        return new File(filePath);
    }

    private static String getFilePath(PluploadChunk chunk, String uploadedFileName) {
        return getReceiver().getExpectedFilePath(chunk.getFileId()) + File.separator + uploadedFileName;
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
