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
import java.io.InputStream;
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

    protected InputStream inputStream;

    /**
     * Get unique file id
     *
     * @return
     */
    public String getFileId() {
        return fileId;
    }

    PluploadChunk setFileId(String fileId) {
        this.fileId = fileId;
        return this;
    }

    /**
     * Get the file name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    PluploadChunk setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get current chunk number
     *
     * @return
     */
    public int getChunk() {
        return chunk;
    }

    PluploadChunk setChunk(int chunk) {
        this.chunk = chunk;
        return this;
    }

    /**
     * Get overall number of chunks
     *
     * @return
     */
    public int getChunks() {
        return chunks;
    }

    PluploadChunk setChunks(int chunks) {
        this.chunks = chunks;
        return this;
    }

    /**
     * Get the InputStream with chunk data. This stream will only be valid in
     * the current request scope. Do not trey to serialize it/save it for use in
     * the future.
     *
     * @return
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * Check if this is a last chunk
     *
     * @return
     */
    public boolean isLast() {
        return this.chunk + 1 == this.chunks;
    }

    /**
     * Check if this is a first chunk
     *
     * @return
     */
    public boolean isFirst() {
        return this.chunk == 0;
    }

}
