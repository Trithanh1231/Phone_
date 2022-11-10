package com.edu.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.model.MultiImage;
import com.edu.service.MultimageService;

@CrossOrigin("*")
@RestController
@RequestMapping("/rest/multiimages")
public class MultiImageRestController {
    @Autowired
    MultimageService multimageService;
    
    @GetMapping("{id}")
    public List<MultiImage> getimage(@PathVariable("id") Long productId) {
        return multimageService.findimage(productId);
    }
}
