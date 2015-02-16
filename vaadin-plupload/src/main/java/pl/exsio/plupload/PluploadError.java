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

import java.io.Serializable;

/**
 *
 * @author exsio
 */
public class PluploadError implements Serializable {

    protected int code;

    protected PluploadFile file;

    protected String message;

    protected Type type;

    public enum Type {

        GENERIC_ERROR(-100),
        HTTP_ERROR(-200),
        IO_ERROR(-300),
        SECURITY_ERROR(-400),
        INIT_ERROR(-500),
        FILE_SIZE_ERROR(-600),
        FILE_EXTENSION_ERROR(-601),
        FILE_DUPLICATE_ERROR(-602),
        IMAGE_FORMAT_ERROR(-700),
        MEMORY_ERROR(-701),
        UNKNOWN_ERROR(0);

        private int code;

        private Type(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }

        public static Type byCode(int code) {
            for (Type type : Type.values()) {
                if (type.getCode() == code) {
                    return type;
                }
            }
            return UNKNOWN_ERROR;
        }
    }

    public int getCode() {
        return code;
    }

    public PluploadFile getFile() {
        return file;
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    void setType() {
        this.type = Type.byCode(code);
    }

    void setCode(int code) {
        this.code = code;
    }

    void setFile(PluploadFile file) {
        this.file = file;
    }

    void setMessage(String message) {
        this.message = message;
    }

    void setType(Type type) {
        this.type = type;
    }

    public boolean is(Type type) {
        return this.type != null && this.type.equals(type);
    }

}
