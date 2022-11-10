package com.edu.service;

import java.sql.Timestamp;
import java.util.Date;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.edu.model.Product;

public interface ProductService {

	void deleteById(Long id);

	Optional<Product> findById(Long id);
	
	public Product finid(int i);
	
	List<Product> findAll();

	<S extends Product> S save(S entity);

    Product finID(Long id);

    Product update(Product product);

    <S extends Product> Page<S> findAll(Example<S> example, Pageable pageable);
	
    Page<Product> findByPrice(int pageSize, int pageNumber,double min, double max) throws Exception;
    
    Page<Product> findBycolor(int pageSize, int pageNumber,String color) throws Exception;
    
    Page<Product> findByBrand(int pageSize, int pageNumber,String name) throws Exception;
    
    Page<Product> findBySize(int pageSize, int pageNumber,Integer size) throws Exception;

    long count();
    
    Page<Product> searchByKeyword(int pageSize, int pageNumber, String keyword) throws Exception;
    
    // sort
    Page<Product> sortAtoZ(int pageSize, int pageNumber) throws Exception;
    Page<Product> sortZtoA(int pageSize, int pageNumber) throws Exception;
    Page<Product> sortPriceLow(int pageSize, int pageNumber) throws Exception;
    Page<Product> sortPriceHeight(int pageSize, int pageNumber) throws Exception;
    Page<Product> sortNew(int pageSize, int pageNumber) throws Exception;
    Page<Product> sortOld(int pageSize, int pageNumber) throws Exception;
    
    Product create(Product product);
}
