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
import java.io.Serializable;

/**
 *
 * @author exsio
 */
public class PluploadChunk implements Serializable {

    protected String fileId;

    protected String name;

    protected int chunk;

    protected int chunks;

    protected File file;

    public String getFileId() {
        return fileId;
    }

    public PluploadChunk setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    public String getName() {
        return name;
    }

    public PluploadChunk setName(String name) {
        this.name = name;
        return this;
    }

    public int getChunk() {
        return chunk;
    }

    public PluploadChunk setChunk(int chunk) {
        this.chunk = chunk;
        return this;
    }

    public int getChunks() {
        return chunks;
    }

    public PluploadChunk setChunks(int chunks) {
        this.chunks = chunks;
        return this;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public boolean isLast() {
        return this.chunk + 1 == this.chunks;
    }

}
