package com.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.Utils.SessionUtil;

@Controller
public class ShopingCartController {
    @Autowired
    SessionUtil session;
	@RequestMapping("/cart/view")
	public String view(Model model) {
	   
	    session.setAttribute("giamgia", 0);
		return "cart/view";
	}
	
}
