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

import com.vaadin.annotations.Push;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import javax.servlet.annotation.WebServlet;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadOption;
import pl.exsio.plupload.model.PluploadFile;

@SuppressWarnings("serial")
public class DevUI extends UI {

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DevUI.class, widgetset = "pl.exsio.plupload.PluploadWidgetSet")
    public static class DevServlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {

        final Plupload uploader = new Plupload();
        uploader.setImmediate(true);
        uploader.setCaption("Browse");
        
        final ProgressBar progress = new ProgressBar();
        progress.setIndeterminate(false);
        progress.setValue(0f);
        progress.setWidth("200px");
        
        
        final Label label = new Label("0%");
        

        uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {

            @Override
            public void onFilesAdded(PluploadFile[] files) {
                progress.setValue(0f);
                label.setValue("0%");
                for (PluploadFile f : files) {
                    System.out.println(f);
                }
            }
        });

        uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {

            @Override
            public void onUploadProgress(PluploadFile file) {
                label.setValue(file.getPercent() + "%");
                System.out.println(file.getPercent() + "%");
                progress.setValue(new Long(file.getPercent()).floatValue()/100);
            }
        });

        uploader.addUploadCompleteListener(new Plupload.UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                System.out.println("upload complete");
            }
        });

        Button start = new Button("Start");
        start.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                uploader.start();
            }
        });

        FormLayout layout = new FormLayout();
        layout.addComponent(uploader);
        layout.addComponent(start);
        layout.addComponent(progress);
        layout.addComponent(label);
        this.setContent(layout);

        uploader.setOption(PluploadOption.MAX_FILE_SIZE, "500mb");
        uploader.setOption(PluploadOption.MULTI_SELECTION, "false");
        uploader.setOption(PluploadOption.FILTERS, "[{\"title\" : \"Image files\", \"extensions\" : \"jpg,jpeg,gif,png\"}]");
        uploader.init();
    }

}
