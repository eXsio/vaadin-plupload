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
package pl.exsio.plupload.manager;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ProgressBar;
import com.vaadin.ui.VerticalLayout;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import pl.exsio.plupload.Plupload;
import pl.exsio.plupload.PluploadFile;
import static pl.exsio.plupload.util.PluploadUtil.trimTextInTheMiddle;

/**
 *
 * @author exsio
 */
public class PluploadManager extends VerticalLayout {

    protected final HorizontalLayout controls;

    protected final VerticalLayout itemsContainer;

    protected final Map<String, Item> itemsMap;

    protected String removeLabel = "";

    protected final Plupload uploader = new Plupload("Browse", FontAwesome.FILES_O);

    protected final Button startButton = new Button("Start", FontAwesome.PLAY);

    protected final Button stopButton = new Button("Stop", FontAwesome.STOP);

    protected final Set<ItemCreationListener> itemCreationListeners = new LinkedHashSet<>();

    public PluploadManager() {
        this.controls = new HorizontalLayout();
        this.itemsContainer = new VerticalLayout();
        this.itemsMap = new LinkedHashMap();
        this.postConstruct();
    }

    private void postConstruct() {

        this.initManager();
        this.initControls();
        this.initItems();
        this.handleFilesAdded();
        this.handleFilesRemoved();
        this.handleUploadStart();
        this.handleUploadStop();
        this.handleUploadProgress();
        this.handleUploadComplete();
        this.handleStartButtonClick();
        this.handleStopButtonClick();
    }

    private void initManager() {
        this.setSpacing(true);
        this.setMargin(true);
        this.setStyleName("plupload-mgr");
    }

    private void initControls() {
        this.controls.setSpacing(true);
        this.controls.setStyleName("plupload-mgr-controls");
        this.startButton.setEnabled(false);
        this.stopButton.setEnabled(false);
        this.controls.addComponent(uploader);
        this.controls.addComponent(startButton);
        this.controls.addComponent(stopButton);
        String id = "plupload-manager-" + this.uploader.getUploaderKey();
        this.setId(id);
        this.uploader.addDropZone(id);
        this.addComponent(this.controls);
    }

    private void initItems() {
        this.itemsContainer.setSpacing(true);
        this.itemsContainer.setStyleName("plupload-mgr-items");
        this.addComponent(this.itemsContainer);

    }

    public Plupload getUploader() {
        return this.uploader;
    }

    public PluploadManager setStartButtonCaption(String caption) {
        this.startButton.setCaption(caption);
        return this;
    }

    public PluploadManager setBrowseButtonCaption(String caption) {
        this.uploader.setCaption(caption);
        return this;
    }

    public PluploadManager setStopButtonCaption(String caption) {
        this.stopButton.setCaption(caption);
        return this;
    }

    public PluploadManager setRemoveButtonCaption(String caption) {
        this.removeLabel = caption;
        for (String fileId : this.itemsMap.keySet()) {
            this.itemsMap.get(fileId).getRemoveButton().setCaption(caption);
        }
        return this;
    }

    public PluploadFile[] getUploadedFiles() {
        return this.uploader.getUploadedFiles();
    }

    public HorizontalLayout getControls() {
        return this.controls;
    }

    public VerticalLayout getItemsContainer() {
        return this.itemsContainer;
    }

    public Map<String, Item> getItemsMap() {
        return this.itemsMap;
    }

    public Button getStartButton() {
        return this.startButton;
    }

    public Button getStopButton() {
        return this.stopButton;
    }

    public PluploadManager addItemCreationListener(ItemCreationListener listener) {
        this.itemCreationListeners.add(listener);
        return this;
    }

    public PluploadManager removeItemCreationListener(ItemCreationListener listener) {
        this.itemCreationListeners.remove(listener);
        return this;
    }

    private void handleStopButtonClick() {
        this.stopButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                uploader.stop();
            }
        });
    }

    private void handleStartButtonClick() {
        this.startButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                uploader.start();
            }
        });
    }

    private void handleUploadComplete() {
        this.uploader.addUploadCompleteListener(new Plupload.UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                if (uploader.getQueuedFiles().length == 0) {
                    startButton.setEnabled(false);
                }
                stopButton.setEnabled(false);
            }
        });
    }

    private void handleUploadProgress() {
        this.uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {

            @Override
            public void onUploadProgress(PluploadFile file) {
                if (itemsMap.containsKey(file.getId())) {
                    itemsMap.get(file.getId()).setProgress(file.getPercent());
                }
            }
        });
    }

    private void handleUploadStop() {
        this.uploader.addUploadStopListener(new Plupload.UploadStopListener() {

            @Override
            public void onUploadStop() {
                startButton.setEnabled(true);
                stopButton.setEnabled(false);
            }
        });
    }

    private void handleUploadStart() {
        this.uploader.addUploadStartListener(new Plupload.UploadStartListener() {

            @Override
            public void onUploadStart() {
                startButton.setEnabled(false);
                stopButton.setEnabled(true);
            }
        });
    }

    private void handleFilesRemoved() {
        this.uploader.addFilesRemovedListener(new Plupload.FilesRemovedListener() {

            @Override
            public void onFilesRemoved(PluploadFile[] files) {
                for (PluploadFile file : files) {
                    removeItem(file.getId());
                }
                toggleStartButton();
            }
        });
    }

    private void handleFilesAdded() {
        this.uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {

            @Override
            public void onFilesAdded(PluploadFile[] files) {

                for (PluploadFile file : files) {
                    Item item = new Item(file);
                    for (ItemCreationListener listener : itemCreationListeners) {
                        listener.onCreateItem(item, file);
                    }
                    addItem(file.getId(), item);
                }
                toggleStartButton();
            }
        });
    }

    private void handleUploaderDestroy() {
        this.uploader.addDestroyListener(new Plupload.DestroyListener() {

            @Override
            public void onDestroy() {
                itemsContainer.removeAllComponents();
                itemsMap.clear();
            }
        });
    }

    private void toggleStartButton() {
        startButton.setEnabled(uploader.getQueuedFiles().length > 0);
    }

    protected void addItem(String fileId, Item item) {
        this.itemsMap.put(fileId, item);
        this.itemsContainer.addComponent(item);
    }

    protected void removeItem(String fileId) {
        if (this.itemsMap.containsKey(fileId)) {
            this.itemsContainer.removeComponent(this.itemsMap.get(fileId));
            this.itemsMap.remove(fileId);
        }
    }

    public interface ItemCreationListener {

        void onCreateItem(Item item, PluploadFile file);

    }

    public class Item extends HorizontalLayout {

        protected ProgressBar progressBar;

        protected Label nameLabel;

        protected Label percentLabel;

        protected Button removeButton;

        public Item(final PluploadFile file) {

            this.setSpacing(true);
            this.progressBar = new ProgressBar();
            this.progressBar.setIndeterminate(false);
            this.progressBar.setValue(0f);
            this.progressBar.setWidth("270px");

            this.setStyleName("plupload-mgr-item plupload-mgr-item-" + file.getId());

            this.nameLabel = new Label(trimTextInTheMiddle(file.getName(), 33));
            this.nameLabel.setWidth("270px");
            this.nameLabel.setDescription(file.getName());

            this.percentLabel = new Label("0%");

            this.removeButton = new Button(removeLabel, FontAwesome.TIMES);
            this.removeButton.addClickListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    uploader.removeFile(file.getId());
                }
            });

            VerticalLayout vlayout = new VerticalLayout();
            vlayout.setSpacing(false);
            vlayout.addComponent(this.nameLabel);
            vlayout.addComponent(this.progressBar);

            this.addComponent(vlayout);
            this.addComponent(this.percentLabel);
            this.addComponent(this.removeButton);
            this.setComponentAlignment(this.percentLabel, Alignment.MIDDLE_CENTER);
            this.setComponentAlignment(this.removeButton, Alignment.MIDDLE_RIGHT);

        }

        public void setProgress(long percent) {
            this.progressBar.setValue(new Long(percent).floatValue() / 100);
            this.percentLabel.setValue(percent + "%");
        }

        public ProgressBar getProgressBar() {
            return progressBar;
        }

        public Label getNameLabel() {
            return nameLabel;
        }

        public Label getPercentLabel() {
            return percentLabel;
        }

        public Button getRemoveButton() {
            return removeButton;
        }

    }
}
