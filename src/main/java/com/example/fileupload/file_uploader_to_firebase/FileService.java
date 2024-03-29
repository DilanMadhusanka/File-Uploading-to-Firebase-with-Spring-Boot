package com.example.fileupload.file_uploader_to_firebase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.fileupload.file_uploader_to_firebase.response.MessageResponse;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class FileService {
	
	String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/img-store-sample.appspot.com/o/%s?alt=media";
	

	public Object upload(MultipartFile multipartFile) {

        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name. 

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String TEMP_URL = this.uploadFile(file, fileName);                                   // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder
            return new MessageResponse("Successfully Uploaded !", TEMP_URL);                     // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return new MessageResponse("500", e.toString(), "Unsuccessfully Uploaded!");
        }

    }
    
    public Object download(String fileName) throws IOException {
        String destFileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));     // to set random strinh for destination file name
        String destFilePath = "/home/insharp/Music" + destFileName;                                    // to set destination file path
        
        ////////////////////////////////   Download  ////////////////////////////////////////////////////////////////////////
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("/home/insharp/Documents/Github/File-Uploading-to-Firebase-with-Spring-Boot/src/main/resources/img-store-sample-firebase-adminsdk-3g4fv-876b2f240c.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        Blob blob = storage.get(BlobId.of("img-store-sample.appspot.com", fileName));
        blob.downloadTo(Paths.get(destFilePath));
        return new MessageResponse("200", "Successfully Downloaded!");
    }
    
    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("img-store-sample.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("/home/insharp/Documents/Github/File-Uploading-to-Firebase-with-Spring-Boot/src/main/resources/img-store-sample-firebase-adminsdk-3g4fv-876b2f240c.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
