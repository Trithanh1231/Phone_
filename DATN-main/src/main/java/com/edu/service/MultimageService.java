package com.edu.service;

import java.util.List;

import com.edu.model.MultiImage;

public interface MultimageService {

    void deleteById(Long id);

    List<MultiImage> findAll();

    <S extends MultiImage> S save(S entity);
    List<MultiImage> findimage(Long productId);
}
