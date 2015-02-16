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
package dev;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import java.io.File;
import javax.servlet.annotation.WebServlet;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadError;
import pl.exsio.plupload.PluploadFile;
import pl.exsio.plupload.field.PluploadField;
import pl.exsio.plupload.helper.filter.PluploadFilter;
import pl.exsio.plupload.helper.resize.PluploadImageResize;
import pl.exsio.plupload.manager.PluploadManager;

@SuppressWarnings("serial")
@Theme("valo")
public class DevUI extends UI {

    @WebServlet(value = "/*", asyncSupported = false)
    @VaadinServletConfiguration(productionMode = false, ui = DevUI.class, widgetset = "pl.exsio.plupload.PluploadWidgetSet")
    public static class DevServlet extends VaadinServlet {
    }

    private class Counter {

        private int i = 0;

        void increment() {
            i++;
        }

        int get() {
            return i;
        }

        void reset() {
            i = 0;
        }
    }

    @Override
    protected void init(VaadinRequest request) {

        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);

        PluploadManager mgr = createUploadManager("Manager 1");
        PluploadManager mgr2 = createUploadManager("Manager 2");

        mgr.getUploader().addFilter(new PluploadFilter("music", "mp3,flac"));

        VerticalLayout dropZone = new VerticalLayout() {
            {
                addComponent(new Label("Additional drop zone for music files"));
                setId("music-drop-zone");
            }
        };

        mgr.getUploader().addDropZone(dropZone);

        mgr2.getUploader().addFilter(new PluploadFilter("images", "jpg, jpeg, png"));
        mgr2.getUploader().setImageResize(new PluploadImageResize().setEnabled(true).setCrop(true).setHeight(200).setWidth(400));

        mainLayout.addComponent(mgr);
        mainLayout.addComponent(dropZone);
        mainLayout.addComponent(mgr2);

        PluploadField<File> field = createUploadField();
        final Form form = new Form();
        form.addField("file", field);
        field.addValidator(new NullValidator("file must not be null", false));
        Button submit = new Button("commit form");
        submit.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                form.commit();
            }
        });
        mainLayout.addComponent(form);
        mainLayout.addComponent(submit);

        final Plupload uploader = createSimpleUploader();
        uploader.setEnabled(false);
        Button b = new Button("toggle Enabled!", new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                uploader.setEnabled(!uploader.isEnabled());
            }
        });
        mainLayout.addComponent(b);
        mainLayout.addComponent(uploader);
        final Counter c = new Counter();
        final Button.ClickListener l = new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window w = new Window("win");

                if (c.get() < 5) {
                    w.setContent(new Button("win", this));
                    c.increment();
                } else {
                    Field f = createUploadField();
                    w.setContent(f);
                    c.reset();
                }
                w.setWidth("400px");
                w.setHeight("200px");
                getUI().addWindow(w);
            }
        };

        Button win = new Button("Win");
        win.addClickListener(l);
        mainLayout.addComponent(win);

        Button modal = new Button("modal");
        modal.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window w = new Window("modal");

                final PluploadField f = createUploadField();
                w.addCloseListener(new Window.CloseListener() {

                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        Notification.show("closed modal");
                        f.getUploader().destroy();
                    }
                });
                VerticalLayout lay = new VerticalLayout();
                lay.addComponent(f);
                lay.addComponent(new Button("destroy", new Button.ClickListener() {

                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        f.getUploader().destroy();
                    }
                }));

                w.setContent(lay);
                w.setModal(true);
                getUI().addWindow(w);
            }
        });

        Accordion acc = new Accordion();
        acc.addTab(this.createUploadManager("mgr3"), "uploader");
        acc.addTab(new HorizontalLayout(), "Stub");

        mainLayout.addComponent(acc);

        mainLayout.addComponent(modal);
        this.setContent(mainLayout);

    }

    private PluploadField<File> createUploadField() {
        final PluploadField<File> field = new PluploadField(File.class);
        field.getUploader().addUploadCompleteListener(new Plupload.UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                File file = field.getValue();

                System.out.println(file != null ? file.getAbsolutePath() : "null");
            }
        });
        return field;
    }

    private Plupload createSimpleUploader() {
        //instantiate the uploader just as it was a norman Vaadin Button
        final Plupload uploader = new Plupload("Browse", FontAwesome.FILES_O);
        //set the maximum size of uploaded file
        uploader.setMaxFileSize("5mb");
        //prevent duplicate files
        uploader.setPreventDuplicates(true);

        uploader.setMultiSelection(false);
        //add filter
        uploader.addFilter(new PluploadFilter("music", "mp3,flac"));
        //add file uploaded handler
        uploader.addFileUploadedListener(new Plupload.FileUploadedListener() {

            @Override
            public void onFileUploaded(PluploadFile file) {
                File uploadedFile = file.getUploadedFile();
                System.out.println("This file was just uploaded: " + uploadedFile.getAbsolutePath());
            }
        });
        //add upload completed handler
        uploader.addUploadCompleteListener(new Plupload.UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                System.out.println("Upload was completed");
                for (PluploadFile file : uploader.getUploadedFiles()) {
                    System.out.println("Uploaded file " + file.getName() + " is located: " + file.getUploadedFile().getAbsolutePath());
                }
            }
        });
        //add upload pgoress handler
        uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {

            @Override
            public void onUploadProgress(PluploadFile file) {
                System.out.println("I'm uploading file " + file.getName() + " and I'm at " + file.getPercent() + "%");
            }
        });
        //add files added handler - autostart the uploader after files addition
        uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {

            @Override
            public void onFilesAdded(PluploadFile[] files) {
                uploader.start();
            }
        });
        return uploader;
    }

    private PluploadManager createUploadManager(final String name) {
        final PluploadManager mgr = new PluploadManager();
        //mgr.getUploader().setUploadPath("/home/exsio");
        System.out.println("upload path for " + name + ": " + mgr.getUploader().getUploadPath());
        mgr.getUploader().addUploadCompleteListener(new Plupload.UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                System.out.println("Files uploaded by " + name + ": ");
                for (PluploadFile file : mgr.getUploadedFiles()) {
                    System.out.println(file.getUploadedFile().getAbsolutePath());
                }
            }
        });
        mgr.getUploader().addErrorListener(new Plupload.ErrorListener() {

            @Override
            public void onError(PluploadError error) {
                Notification.show("Upload error: " + error.getMessage() + " - " + error.getFile().getType() + " (" + error.getType() + ")", Notification.Type.ERROR_MESSAGE);
            }
        });
        mgr.getUploader().addFileFilteredListener(new Plupload.FileFilteredListener() {

            @Override
            public void onFileFiltered(PluploadFile file) {
                System.out.println("This file was filtered: " + file.getName());
            }
        });
        mgr.getUploader().setChunkSize("2mb");
        mgr.getUploader().setMaxFileSize("5mb");
        mgr.getUploader().setMultiSelection(true);
        mgr.getUploader().setPreventDuplicates(true);
        return mgr;
    }

}
