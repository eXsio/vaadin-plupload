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
package dev;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import java.io.File;
import javax.servlet.annotation.WebServlet;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadFile;
import pl.exsio.plupload.field.PluploadField;
import pl.exsio.plupload.helper.filter.PluploadFilter;
import pl.exsio.plupload.helper.resize.PluploadImageResize;
import pl.exsio.plupload.manager.PluploadManager;

@SuppressWarnings("serial")
public class DevUI extends UI {

    @WebServlet(value = "/*", asyncSupported = false)
    @VaadinServletConfiguration(productionMode = false, ui = DevUI.class, widgetset = "pl.exsio.plupload.PluploadWidgetSet")
    public static class DevServlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);

        PluploadManager mgr = createUploadManager("Manager 1");
        PluploadManager mgr2 = createUploadManager("Manager 2");

        mgr.getUploader().addFilter(new PluploadFilter("music", "mp3, flac"));
        mgr2.getUploader().addFilter(new PluploadFilter("images", "jpg, jpeg, png"));
        mgr2.getUploader().setImageResize(new PluploadImageResize().setEnabled(true).setCrop(true).setHeight(200).setWidth(400));

        mainLayout.addComponent(mgr);
        mainLayout.addComponent(mgr2);

        final PluploadField<File> field = new PluploadField(File.class);
        field.getUploader().addUploadCompleteListener(new Plupload.UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                File file = field.getValue();

                System.out.println(file != null ? file.getAbsolutePath() : "null");
            }
        });

        mainLayout.addComponent(field);

        this.setContent(mainLayout);

    }

    private PluploadManager createUploadManager(final String name) {
        final PluploadManager mgr = new PluploadManager();
        mgr.getUploader().addUploadCompleteListener(new Plupload.UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                System.out.println("Files uploaded by " + name + ": ");
                for (PluploadFile file : mgr.getUploadedFiles()) {
                    System.out.println(file.getUploadedFile().getAbsolutePath());
                }
            }
        });
        mgr.getUploader().addFileFilteredListener(new Plupload.FileFilteredListener() {

            @Override
            public void onFileFiltered(PluploadFile file) {
                System.out.println("This file was filtered: " + file.getName());
            }
        });
        return mgr;
    }

}
