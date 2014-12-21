/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.exsio.plupload;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sdymi_000
 */
public class PluploadQueue {

    protected final Map<String, PluploadFile> queue;

    public PluploadQueue() {
        this.queue = new LinkedHashMap<>();
    }

    public void addFile(PluploadFile file) {
        this.queue.put(file.getId(), file);
    }

    public void addFiles(PluploadFile[] files) {
        for (PluploadFile file : files) {
            this.addFile(file);
        }
    }

    public void removeFile(String fileId) {
        if (this.queue.containsKey(fileId)) {
            this.queue.remove(fileId);
        }
    }

    public void removeFiles(PluploadFile[] files) {
        for (PluploadFile file : files) {
            this.removeFile(file.getId());
        }
    }

    public void setUploadedFile(String fileId, File uploadedFile) {
        if (this.queue.containsKey(fileId)) {
            this.queue.get(fileId).setUploadedFile(uploadedFile);
        }
    }

    public Set<String> getFileIds(boolean uploadedOnly) {
        if (!uploadedOnly) {
            return this.queue.keySet();
        } else {
            Set<String> ids = new LinkedHashSet();
            for (String fileId : this.queue.keySet()) {
                if (this.queue.get(fileId).isUploaded()) {
                    ids.add(fileId);
                }
            }
            return ids;
        }
    }

    public Set<PluploadFile> getPluploadFiles(boolean uploadedOnly) {
        Set<PluploadFile> files = new LinkedHashSet();
        for (String fileId : this.queue.keySet()) {
            PluploadFile file = this.queue.get(fileId);
            if (file.isUploaded()) {
                files.add(file);
            }
        }
        return files;
    }
}
