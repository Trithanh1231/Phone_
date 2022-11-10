package com.edu.service;

import java.util.List;

import com.edu.model.Authority;

public interface AuthorityService {

	public List<Authority> fillAll();
	public Authority create(Authority auth);

	public List<Authority> findAuthoritiesOfAdministrators();
    void delete(Long id);

}
