package com.edu.serviceimpl;

import java.util.List;

import javax.security.auth.login.AccountNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.model.Product;
import com.edu.model.User;
import com.edu.reponsitory.UserReponsitory;
import com.edu.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserReponsitory userReponsitory;

    @Override
    public <S extends User> S save(S entity) {
        return userReponsitory.save(entity);
    }

    @Override
    public List<User> findAll() {
        return userReponsitory.findAll();
    }

    @Override
    public void deleteById(String id) {
        userReponsitory.deleteById(id);
    }

    @Override
    public User getById(String id) {
        return userReponsitory.getById(id);
    }

    @Override
    public User findById(String username) {
        return userReponsitory.findById(username).get();
    }
    
    public User findByUsername(String username) {
        return userReponsitory.findByUsername(username);
    }

    @Override
    public User findID(String parameter) {
        return userReponsitory.findById(parameter).get();
    }

    @Override
    public void changePassword(String password, String username) {
       userReponsitory.changePassword(password, username);
        
    }

    @Transactional(rollbackOn = { Exception.class, Throwable.class })
    @Override
    public void update(User user) {
       if(user.getPhoto() ==null && user.getPhoto().isEmpty()) {
           userReponsitory.updateNonImage(user.getEmail(), user.getFullname(), user.getUsername());
       }else {
           userReponsitory.updateNonPass(user.getEmail(), user.getFullname(),user.getPhoto(), user.getUsername());
       }
    }

    @Override
    public void updateResetPasswordToken(String token, String email) {
        User customer = userReponsitory.findByEmail(email);
        if (customer != null) {
            customer.setPassword(token);
            userReponsitory.save(customer);
        }
        
    }

    @Override
    public User getByResetPasswordToken(String token) {
        return userReponsitory.findByPassword(token);
    }

    @Override
    public void updateNonPass(String email, String fullname, String photo, String username) {
       userReponsitory.updateNonPass(email, fullname, photo, username);
    }

    @Override
    public List<User> getAdministrators() {
        return userReponsitory.getAdministrators();
    };

}
