package com.edu.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.edu.Utils.SessionUtil;
import com.edu.model.Order;
import com.edu.model.OrderDetail;
import com.edu.model.User;
import com.edu.reponsitory.OrderDetailReponsitory;
import com.edu.service.OrderService;
import com.edu.service.UserService;

@Controller
public class OrderController {
	@Autowired
	OrderService orderservice;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	UserService userService;
	
	@Autowired
	HttpServletRequest request;
	
	@Autowired
	OrderDetailReponsitory detailReponsitory;
	
	@Autowired
	SessionUtil sessionUtil;
	@RequestMapping("/order/checkout")
	public String checkout(Model model) {
	    String phone = userService.findID(request.getRemoteUser()).getPhone();
	  System.out.println("phone: "+phone);
	    session.setAttribute("phone", phone);
		return "order/checkout";
	}
	@RequestMapping("/order/list")
	public String list(Model model, HttpServletRequest request) {
		String username = request.getRemoteUser();
		model.addAttribute("orders", orderservice.findUsername(username));
		return "order/list";
	}
	@RequestMapping("/order/detail/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		model.addAttribute("order", orderservice.findId(id));
		OrderDetail detail = detailReponsitory.findByOrderId(id);
		Integer giamgia = sessionUtil.getAttribute("giamgia");
		detail.setTotal(detail.getQuantity()*detail.getPrice()+30000-giamgia);
		detailReponsitory.save(detail);
		return "order/detail";
	}
	@RequestMapping("/order/track/{id}")
    public String track(@PathVariable("id") Long id, Model model) {
        model.addAttribute("order", orderservice.findId(id));
        return "order/track";
    }
	@RequestMapping("/order/delete/{id}")
	public String delete(@PathVariable("id") Long id, Model model) {
		Order o =  orderservice.findById(id).get();
		o.setStatus(false);
		orderservice.save(o);
		return "forward:/order/list";
	}
}
