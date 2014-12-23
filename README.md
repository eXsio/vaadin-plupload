# Plupload library integration add-on for Vaadin Framework 7.x
===============

Pluploader is the most powerful client-side upload library out there and it would be a shame, if the Vaadin community couldn't use it. The original Plupload website can be found here: http://www.plupload.com/

### Attention!
The MIT license concernes olny the addon. If You want to use Plupload, please familiarize youself with it's licensing terms, which can be found here: http://www.plupload.com/license/gplv2 and here: http://www.plupload.com/license/oem

---

### Installation

In order to install, for now You have to download and compile the project, and then add it to the dependencies manually. Maven artifact will be availabie soon.

---

### Why is this add-on so damn cool?

Well, there is a number of reasons, but the main ones are:
- Live upload progress updates without any needs of Push dependencies or enabled asyncSupported feature.
- Chunked uploads with direct streaming to the desired destination - meaning that You can upload gigabyte-sized files without worrying about memory (RAM) usage on the server part, and if something goes wrong with the network the uploader will try to send the chunk again. You can also pause the upload and resume it, when network problems are resolved.
- Tons of events and event handlers give You the ability to respond dynamically to almost every situation
- 3 different compontents with 3 different purposes and complexity levels - choose the right one and customize it to Your needs

### How is all this possible? 

It's very simple - the Plupload library (on the client side) controls the upload process and sends appropriate event, which are propagated to the server side through RPC. The server side can respond accordingly and control the upload process this way, or update the UI components. All the chunking and resizing (in case of images) is done on the client side. All server side has to do is implement some event handlers.

---

### Usage

There are 3 main components in this add-on:
- Plupload
- PluploadManager
- PluploadField
 
  ### Plupload

  This is the most basic component. It's actually a Vaadin Button with the ability to pick files, You want to upload. But it's got all the goodies, You'd want from an uploader:
  
  - custom upload path
  - all the relevant Plupload events
  - chunked uploads
  - multi / single file upload
  - file extension filters
  - client-side image resize configuration
  - max file upload setting
  - chunk size setting
  - no. of retries setting
  
  Events suported by Plupload add-on:
  ```
 - FilesAdded
 - FilesRemoved
 - FileFiltered
 - FileUploaded
 - UploadStarted
 - UploadStopped
 - UploadProgress
 - UploadComplete
 - Error
 ```
  
  Example usage of the Plupload component:
  
  ```
        //instantiate the uploader just as it was a norman Vaadin Button
        final Plupload uploader = new Plupload("Browse", FontAwesome.FILES_O);

        //set the maximum size of uploaded file
        uploader.setOption(PluploadOption.MAX_FILE_SIZE, "50mb");
        
        //prevent duplicate files
        uploader.setOption(PluploadOption.PREVENT_DUPLICATES, "true");
        
        //add filter
        uploader.addFilter(new PluploadFilter("music", "mp3, flac"));

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
                    System.out.println("Uploaded file " + file.getName() + " is located at: " + file.getUploadedFile().getAbsolutePath());
                }
            }
        });
        
        //add upload pgoress handler
        uploader.addUploadProgressListener(new Plupload.UploadProgressListener() {

            @Override
            public void onUploadProgress(PluploadFile file) {
                System.out.println("I'm uploading file "+file.getName()+" and I'm at "+file.getPercent()+"%");
            }
        });

        //add files added handler - autostart the uploader after files addition
        uploader.addFilesAddedListener(new Plupload.FilesAddedListener() {

            @Override
            public void onFilesAdded(PluploadFile[] files) {
                uploader.start();
            }
        });
        
        layout.addComponent(uploader);
  ```
  
  and the result, after uploading two music files should be similar to this:
  ```
  I'm uploading file test.mp3 and I'm at 0%
I'm uploading file test.mp3 and I'm at 16%
I'm uploading file test.mp3 and I'm at 32%
I'm uploading file test.mp3 and I'm at 48%
I'm uploading file test.mp3 and I'm at 64%
I'm uploading file test.mp3 and I'm at 80%
I'm uploading file test.mp3 and I'm at 96%
I'm uploading file test.mp3 and I'm at 100%
This file was just uploaded: /tmp/o_199r9ll9e16aicav1hg2qi31jipk.mp3
I'm uploading file test.flac and I'm at 0%
I'm uploading file test.flac and I'm at 5%
I'm uploading file test.flac and I'm at 9%
I'm uploading file test.flac and I'm at 14%
I'm uploading file test.flac and I'm at 18%
I'm uploading file test.flac and I'm at 22%
I'm uploading file test.flac and I'm at 27%
I'm uploading file test.flac and I'm at 31%
I'm uploading file test.flac and I'm at 35%
I'm uploading file test.flac and I'm at 40%
I'm uploading file test.flac and I'm at 44%
I'm uploading file test.flac and I'm at 48%
I'm uploading file test.flac and I'm at 53%
I'm uploading file test.flac and I'm at 57%
I'm uploading file test.flac and I'm at 62%
I'm uploading file test.flac and I'm at 66%
I'm uploading file test.flac and I'm at 70%
I'm uploading file test.flac and I'm at 75%
I'm uploading file test.flac and I'm at 79%
I'm uploading file test.flac and I'm at 83%
I'm uploading file test.flac and I'm at 88%
I'm uploading file test.flac and I'm at 92%
I'm uploading file test.flac and I'm at 96%
I'm uploading file test.flac and I'm at 100%
This file was just uploaded: /tmp/o_199r9ll9e1g6q15vmrdj13l51rdbl.flac
Upload was completed
Uploaded file test.mp3 is located at: /tmp/o_199r9ll9e16aicav1hg2qi31jipk.mp3
Uploaded file test.flac is located at: /tmp/o_199r9ll9e1g6q15vmrdj13l51rdbl.flac
  ```
  
  Although in this case, the callbacks affected the server side (wrote something into the server console), You can update client components (Labels, ProgressBars etc) this way as well. Which brings us to the second component...
  
  ### PluploadManager
  
  This is a complete upload manager for those of You, who want to type couple of lines of code and have working and fully equipped component.
  
  Manager instantiation and usage are trivial:
  
  ```
  final PluploadManager mgr = new PluploadManager();
  
  //set the desired upload path (java.io.tmpdir by default)
  mgr.getUploader().setUploadPath("/some/upload/path");
  
  //add file filter
  mgr.getUploader().addFilter(new PluploadFilter("images", "jpg, jpeg, png"));
  
  //set the client-side resizer
  mgr.getUploader().setImageResize(new PluploadImageResize().setEnabled(true).setCrop(true).setHeight(200).setWidth(400));
  
  //add the component to layout
  layout.addComponent(mgr);
  ```
  
  And thats it! This manager will show you a nice progressbar for every added and uploaded file, will handle upload pause/resume and will enable You to remove the files from queue using UI.
  
  ### PluploadField
  
  And what if You want to use this in a Form? Don't worry, we have that covered too :) Just use the PluploadField:
  
  ```
  final PluploadField<File> field = new PluploadField(File.class);
        field.getUploader().addUploadCompleteListener(new Plupload.UploadCompleteListener() {

            @Override
            public void onUploadComplete() {
                File file = field.getValue();
                System.out.println("Uploaded file is at: "+file.getAbsolutePath());
            }
        });
  ```
  
  As You see, the field is parametrized. Thanks to this, You can use it to get ```java.io.File``` or ```byte[]``` as a value/type of the field.
  
---  
  
### Troubleshooting

  This is a fairly new stuff and it's not properly polished yet, so there is a serious possibility that something can go wrong somewhere. Don't get angry at the code (or at me for that matter), just submit an issue or work on the problem and submit a pull request here: https://github.com/eXsio/vaadin-plupload. Cheers!
