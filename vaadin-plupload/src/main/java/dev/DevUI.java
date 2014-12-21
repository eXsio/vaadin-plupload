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
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadOption;
import pl.exsio.plupload.ex.PluploadNotInitializedException;
import pl.exsio.plupload.PluploadFile;

@SuppressWarnings("serial")
public class DevUI extends UI {
    
    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DevUI.class, widgetset = "pl.exsio.plupload.PluploadWidgetSet")
    public static class DevServlet extends VaadinServlet {
    }
    
    private Map<String, Button> removeButtons = new HashMap();
    
    private Map<String, ProgressBar> progressBars = new HashMap();
    
    private Map<String, Label> progressLabels = new HashMap();
    
    private Map<String, Layout> controls = new HashMap();
    
    @Override
    protected void init(VaadinRequest request) {
        
        final Plupload uploader = new Plupload();
        final VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        uploader.setImmediate(true);
        uploader.setCaption("Browse");
        
        uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {
            
            @Override
            public void onFilesAdded(PluploadFile[] files) {
                
                for (final PluploadFile file : files) {
                    System.out.println(file.toString());
                    HorizontalLayout layout = new HorizontalLayout();
                    final ProgressBar progress = new ProgressBar();
                    progress.setIndeterminate(false);
                    progress.setValue(0f);
                    progress.setWidth("200px");
                    Button remove = new Button("X");
                    Label nameLabel = new Label(file.getName());
                    Label progressLabel = new Label("0%");
                    remove.addClickListener(new Button.ClickListener() {
                        
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            uploader.removeFile(file.getId());
                        }
                    });
                    
                    removeButtons.put(file.getId(), remove);
                    progressBars.put(file.getId(), progress);
                    progressLabels.put(file.getId(), progressLabel);
                    layout.addComponent(nameLabel);
                    layout.addComponent(progress);
                    layout.addComponent(progressLabel);
                    layout.addComponent(remove);
                    mainLayout.addComponent(layout);
                    controls.put(file.getId(), layout);
                }
            }
        });
         final Button start = new Button("Start");
         start.setEnabled(false);
        uploader.addInitListener(new Plupload.InitListener() {

            @Override
            public void onInitialized(String uploaderId) {
                System.out.println("Initialized uploader: "+uploaderId);
                start.setEnabled(true);
            }
        });
        
        uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {
            
            @Override
            public void onUploadProgress(PluploadFile file) {
                progressLabels.get(file.getId()).setValue(file.getPercent() + "%");
                System.out.println(file.getPercent() + "%");
                progressBars.get(file.getId()).setValue(new Long(file.getPercent()).floatValue() / 100);
            }
        });
        
        uploader.addUploadCompleteListener(new Plupload.UploadCompleteListener() {
            
            @Override
            public void onUploadComplete() {
                System.out.println("upload complete");
            }
        });
        
        uploader.addFilesRemovedListener(new Plupload.FilesRemovedListener() {
            
            @Override
            public void onFilesRemoved(PluploadFile[] files) {
                for (PluploadFile file : files) {
                    mainLayout.removeComponent(controls.get(file.getId()));
                }
            }
        });
        
       
        start.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                
                try {
                    uploader.start();
                } catch (PluploadNotInitializedException ex) {
                    Notification.show("Nie udało się wystartować!");
                    ex.printStackTrace();
                }
            }
        });
        
        Button stop = new Button("Stop");
        stop.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                uploader.stop();
            }
        });
        
        mainLayout.addComponent(uploader);
        mainLayout.addComponent(start);
        mainLayout.addComponent(stop);
        this.setContent(mainLayout);
        
        uploader.setOption(PluploadOption.MAX_FILE_SIZE, "500mb");
        uploader.setOption(PluploadOption.MULTI_SELECTION, "false");
        uploader.setOption(PluploadOption.FILTERS, "[{\"title\" : \"Image files\", \"extensions\" : \"jpg,jpeg,gif,png\"}]");
        uploader.init();
        
    }
    
}
