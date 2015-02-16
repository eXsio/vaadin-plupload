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
package pl.exsio.plupload.field;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadFile;
import pl.exsio.plupload.ex.UnsupportedFieldTypeException;
import static pl.exsio.plupload.util.PluploadUtil.trimTextInTheMiddle;

/**
 *
 * @author exsio
 */
public class PluploadField<T extends Object> extends CustomField<T> {

    protected final Class<T> returnTypeClass;

    protected String browseLabel = "Browse";

    protected String removeLabel = "";

    protected final Plupload uploader = new Plupload(this.browseLabel, FontAwesome.FILES_O);

    protected ProgressBar progressBar;

    protected Label nameLabel;

    protected Button removeButton;

    protected HorizontalLayout layout;

    protected PluploadFile currentFile;

    protected PluploadFile newFile;

    public PluploadField(Class<T> returnTypeClass) {
        if (!byte[].class.equals(returnTypeClass) && !File.class.equals(returnTypeClass)) {
            throw new UnsupportedFieldTypeException("The types supported by this field are byte[] and java.io.File.");
        }
        this.returnTypeClass = returnTypeClass;
        this.initHandlers();
    }

    @Override
    protected Component initContent() {

        this.layout = new HorizontalLayout();
        this.layout.setStyleName("plupload-field");
        this.layout.setSpacing(true);

        this.uploader.setMultiSelection(false);
        this.uploader.setStyleName("plupload-field-uploader");

        this.progressBar = new ProgressBar();
        this.progressBar.setIndeterminate(false);
        this.progressBar.setValue(0f);
        this.progressBar.setWidth("128px");
        this.progressBar.setStyleName("plupload-field-progressbar");
        this.progressBar.setVisible(false);

        this.nameLabel = new Label();
        this.nameLabel.setStyleName("plupload-field-name");
        this.nameLabel.setWidth("128px");

        this.removeButton = new Button(this.removeLabel, FontAwesome.TIMES);
        this.removeButton.setVisible(false);
        this.removeButton.setStyleName("plupload-field-remove");

        VerticalLayout vlayout = new VerticalLayout();

        vlayout.addComponent(this.nameLabel);
        vlayout.addComponent(this.progressBar);

        this.layout.addComponent(this.uploader);
        this.layout.addComponent(vlayout);
        this.layout.addComponent(this.removeButton);
        this.layout.setComponentAlignment(this.removeButton, Alignment.TOP_RIGHT);

        return layout;
    }

    public Plupload getUploader() {
        return this.uploader;
    }

    private void initHandlers() {

        this.handleFilesAdded();
        this.handleFilesRemoved();
        this.handleUploadProgress();
        this.handleFileUploaded();

    }

    private void handleFileUploaded() {
        this.uploader.addFileUploadedListener(new Plupload.FileUploadedListener() {

            @Override
            public void onFileUploaded(PluploadFile file) {
                if (File.class.equals(returnTypeClass)) {
                    setValue((T) file.getUploadedFile());
                } else {
                    try {
                        setValue((T) FileUtils.readFileToByteArray(file.getUploadedFile()));
                    } catch (IOException ex) {
                        setValue(null);
                        Logger.getLogger(PluploadField.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
    }

    private void handleUploadProgress() {
        this.uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {

            @Override
            public void onUploadProgress(PluploadFile file) {
                progressBar.setValue(new Long(file.getPercent()).floatValue() / 100);
                progressBar.setDescription(file.getPercent() + "%");
            }
        });
    }

    private void handleFilesRemoved() {
        this.uploader.addFilesRemovedListener(new Plupload.FilesRemovedListener() {

            @Override
            public void onFilesRemoved(PluploadFile[] files) {
                if (files[0].equals(currentFile)) {
                    resetField();
                }
            }

        });
    }

    private void handleFilesAdded() {
        this.uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {

            @Override
            public void onFilesAdded(final PluploadFile[] files) {
                if (files.length == 1) {
                    if (currentFile == null) {
                        currentFile = files[0];
                        nameLabel.setValue(trimTextInTheMiddle(files[0].getName(), 12));
                        nameLabel.setDescription(files[0].getName());
                        removeButton.setVisible(true);
                        progressBar.setVisible(true);
                        removeButton.addClickListener(new Button.ClickListener() {

                            @Override
                            public void buttonClick(Button.ClickEvent event) {
                                uploader.removeFile(files[0].getId());
                            }
                        });
                        uploader.start();
                    }
                } else {
                    for (PluploadFile file : files) {
                        uploader.removeFile(file.getId());
                    }
                }
            }
        });
    }

    @Override
    public Class<? extends T> getType() {
        return this.returnTypeClass;
    }

    private void resetField() throws Converter.ConversionException, ReadOnlyException {
        this.nameLabel.setValue("");
        this.nameLabel.setDescription("");
        this.progressBar.setValue(0f);
        this.progressBar.setVisible(false);
        this.removeButton.setVisible(false);
        for (Object listener : this.removeButton.getListeners(Button.ClickListener.class)) {
            this.removeButton.removeClickListener((Button.ClickListener) listener);
        }
        this.currentFile = null;
        setValue(null);
    }

    /**
     * Set the browse button's label. Defaults to "Browse".
     */
    public void setBrowseLabel(String browseLabel) {
        this.browseLabel = browseLabel;
        if (this.uploader != null) {
            this.uploader.setCaption(browseLabel);
        }
    }

    /**
     * Set the remove button's label. Defaults to a empty String.
     */
    public void setRemoveLabel(String removeLabel) {
        this.removeLabel = removeLabel;
        if (this.removeButton != null) {
            this.removeButton.setCaption(removeLabel);
        }
    }

}
