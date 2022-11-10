package com.edu.service;

import java.util.List;

import com.edu.model.User;

public interface UserService {

    User getById(String id);

    void deleteById(String id);

    List<User> findAll();

    <S extends User> S save(S entity);

    User findById(String username);

    User findByUsername(String username);

    User findID(String username);

    void changePassword(String password, String username);

    void update(User user);

    void updateResetPasswordToken(String token, String email);

    User getByResetPasswordToken(String token);
    
    void updateNonPass (String email, String fullname, String photo, String username);
    
    public List<User> getAdministrators();
  
}
