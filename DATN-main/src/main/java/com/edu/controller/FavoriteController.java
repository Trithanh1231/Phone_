package com.edu.controller;

import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.edu.model.Favorite;
import com.edu.model.Product;
import com.edu.model.User;
import com.edu.reponsitory.FavoriteRepository;
import com.edu.service.ProductService;
import com.edu.service.UserService;

@Controller
public class FavoriteController {
    @Autowired
    FavoriteRepository favoriteRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;

    @RequestMapping(value = "/favorite/add/{id}", method = RequestMethod.GET)
    public String add(Model model, @PathVariable("id") Long id) {
        try {

            if (request.getRemoteUser() == null) {
                return "security/login";

            } else {
                Optional<Favorite> opt = favoriteRepository.findstatus1(request.getRemoteUser(), id);
                Optional<Favorite> optt = favoriteRepository.findstatus0(request.getRemoteUser(), id);
                if (opt.isPresent()) {
                    Favorite fa = favoriteRepository.add(request.getRemoteUser(), id);
                    fa.setStatus(false);
                    favoriteRepository.save(fa);

                } else if (optt.isPresent()) {
                    Favorite fa = favoriteRepository.add(request.getRemoteUser(), id);
                    fa.setStatus(true);
                    favoriteRepository.save(fa);
                } else {
                    User user = userService.findID(request.getRemoteUser());
                    Product product = productService.finID(id);
                    Favorite fa = new Favorite();
                    fa.setCreateDate(new Date());
                    fa.setUser(user);
                    fa.setProduct(product);
                    fa.setStatus(true);
                    favoriteRepository.save(fa);
                }

            }
            model.addAttribute("p", productService.finID(id));
            Optional<Favorite> fa = favoriteRepository.find(request.getRemoteUser(), id);
            if (fa.isPresent()) {
                model.addAttribute("favorite", favoriteRepository.findproduct(request.getRemoteUser(), id));
            } else {
                model.addAttribute("favorite", "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* return "redirect:/product/listall"; */
        return "product/detail :: #btnHeart";

    }
    
    @RequestMapping(value = "/favorite/create/{id}", method = RequestMethod.GET)
    public String create(Model model, @PathVariable("id") Long id) {
        try {

            
                Optional<Favorite> opt = favoriteRepository.findstatus1(request.getRemoteUser(), id);
                Optional<Favorite> optt = favoriteRepository.findstatus0(request.getRemoteUser(), id);
                if (opt.isPresent()) {
                    Favorite fa = favoriteRepository.add(request.getRemoteUser(), id);
                    fa.setStatus(false);
                    favoriteRepository.save(fa);

                } else if (optt.isPresent()) {
                    Favorite fa = favoriteRepository.add(request.getRemoteUser(), id);
                    fa.setStatus(true);
                    favoriteRepository.save(fa);
                } else {
                    User user = userService.findID(request.getRemoteUser());
                    Product product = productService.finID(id);
                    Favorite fa = new Favorite();
                    fa.setCreateDate(new Date());
                    fa.setUser(user);
                    fa.setProduct(product);
                    fa.setStatus(true);
                    favoriteRepository.save(fa);
                

            }
            model.addAttribute("p", productService.finID(id));
            Optional<Favorite> fa = favoriteRepository.find(request.getRemoteUser(), id);
            if (fa.isPresent()) {
                model.addAttribute("favorite", favoriteRepository.findproduct(request.getRemoteUser(), id));
            } else {
                model.addAttribute("favorite", new Favorite());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* return "redirect:/product/listall"; */
        return "redirect:/product/list/" +id;

    }
    
    @RequestMapping(value = "/favorite/delete/{id}", method = RequestMethod.GET)
    public String delete(Model model, @PathVariable("id") Long id) {
        try {

            Favorite fa = favoriteRepository.add(request.getRemoteUser(), id);
            fa.setStatus(false);
            favoriteRepository.save(fa);

        } catch (Exception e) {
            e.printStackTrace();
        }
        /* return "redirect:/product/listall"; */
        return "redirect:/wish/list";

    }
}
