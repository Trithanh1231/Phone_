package com.edu.serviceimpl;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.edu.service.UploadService;

@Service
public class UploadServiceimpl implements UploadService{
    @Autowired
    private ServletContext app;

    @Override
    public File save(MultipartFile file, String folder) {
        File dir = new File(app.getRealPath("/assets/" + folder));
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String s = System.currentTimeMillis() + file.getOriginalFilename();
        String name = Integer.toHexString(s.hashCode()) + s.substring(s.lastIndexOf("."));
        try {
            File savedFile = new File(dir, name);
            file.transferTo(savedFile);
            System.out.println(savedFile.getAbsolutePath());
            return savedFile;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
	
	
}