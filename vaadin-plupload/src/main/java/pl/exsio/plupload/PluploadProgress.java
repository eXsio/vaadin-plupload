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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author exsio
 */
public class PluploadProgress {

    protected int appendedChunks = 0;

    protected final String path = System.getProperty("java.io.tmpdir");

    protected String uploadedFileName;

    protected boolean uploaded = false;

    public boolean appendChunk(PluploadChunk chunk) throws IOException {
        if (this.uploadedFileName == null) {
            this.uploadedFileName = this.getFileName(chunk);
        }
        try (FileOutputStream output = new FileOutputStream(this.getFilePath(), true)) {
            output.write(chunk.getData().get());
            output.close();
        }
        this.appendedChunks++;
        if (this.appendedChunks == chunk.getChunks()) {
            this.uploaded = true;
        }
        return this.uploaded;
    }

    public boolean isUploaded() {
        return this.uploaded;
    }

    public File getUploadedFile() throws FileNotFoundException {
        if (this.uploaded) {
            File f = new File(this.getFilePath());
            if (f.exists()) {
                return f;
            } else {
                throw new FileNotFoundException(this.getFilePath());
            }
        } else {
            return null;
        }
    }

    protected String getFileName(PluploadChunk chunk) {
        String[] arr = chunk.getName().split("\\.");
        StringBuilder name = new StringBuilder(chunk.getFileId());
        if (arr.length > 1) {
            name.append(".").append(arr[arr.length - 1]);
        }
        return name.toString();
    }

    protected String getFilePath() {
        return this.path + File.separator + this.uploadedFileName;
    }
}
