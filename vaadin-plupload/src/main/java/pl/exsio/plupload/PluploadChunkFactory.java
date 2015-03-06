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

import java.io.IOException;
import java.io.Serializable;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.util.Streams;
import static pl.exsio.plupload.PluploadQueue.getReceiver;

/**
 *
 * @author exsio
 */
public abstract class PluploadChunkFactory implements Serializable {

    public static PluploadChunk create(FileItemIterator items) throws IOException, FileUploadException {
        PluploadChunk chunk = new PluploadChunk();
        while (items.hasNext()) {
            FileItemStream item = (FileItemStream) items.next();

            if (item.isFormField()) {
                setChunkField(chunk, item);
            } else {
                saveChunkData(chunk, item);
                break;
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
            chunk.setInputStream(item.openStream());
        }
    }

}
