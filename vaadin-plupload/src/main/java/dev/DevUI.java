package dev;

import com.google.gwt.core.client.JavaScriptObject;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import javax.servlet.annotation.WebServlet;
import pl.exsio.plupload.Plupload;
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
        uploader.setCaption("Browse");

        uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {

            @Override
            public void onFilesAdded(PluploadFile[] files) {
                for (PluploadFile f : files) {
                    System.out.println(f);
                }
            }
        });
        
        uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {

            @Override
            public void onUploadProgress(PluploadFile file) {
                System.out.println(file.getPercent()+"%");
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
        this.setContent(layout);
    }

}
