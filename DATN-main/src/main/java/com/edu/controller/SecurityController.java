package com.edu.controller;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.edu.model.Authority;
import com.edu.model.User;
import com.edu.reponsitory.UserReponsitory;
import com.edu.service.UserService;
import com.edu.service.UserServices;

@Controller
@EnableWebSecurity
public class SecurityController {
	@Autowired
	UserService accountService;
	
	private final String folder_image = "src/main/resources/static/assets/images/";
	@Autowired
	private ServletContext application; 
	
	@Autowired
	HttpSession session;

	@Autowired
	private UserService userService;
	
	@Autowired
    private UserServices userServices;
	
	@Autowired
	private UserReponsitory userReponsitory;
	
	@RequestMapping("/security/login/form")
	public String loginform(Model model) {
		model.addAttribute("message", "Vui long dang nhap");

		return "security/login";
	}

	@RequestMapping("/security/login/success")
	public String loginSuccess(Model model) {
		return "security/login";
	}

	@RequestMapping("/security/login/error")
	public String loginError(Model model) {
		model.addAttribute("message", "Sai thong tin dang nhap");
		return "security/login";
	}

	@RequestMapping("/security/unauthoried")
	public String unauthoried(Model model) {
		model.addAttribute("message", "Khong co quyen truy xuat");
		return "security/login";
	}

	@RequestMapping("/security/logoff/success")
	public String logoffSuccess(Model model) {
		model.addAttribute("message", "Dang xuat thanh cong");
		return "security/login";
	}

	@RequestMapping("/login/register")
	public String register(HttpServletRequest request, ModelMap model) {
	    User acc = new User();
        Authority au = new Authority();
        acc.setUsername(request.getParameter("username"));
        acc.setPassword(request.getParameter("password"));
        acc.setPhoto("");
        acc.setEmail("");
        acc.setFullname("");
        acc.setOrders(null);
        acc.setAuthorites(null);
        accountService.save(acc);

        model.addAttribute("message", "Them Thanh Cong");
        return "security/login";
	}
	@RequestMapping("/oauth2/login/success")
    public String success(OAuth2AuthenticationToken oauth2) {
        userServices.loginFromOAuth2(oauth2);
        return "redirect:/shop";
    }

	
	@RequestMapping("/security/login/chang")
    public String chang(Model model) {
        return "security/chang";
    }

    @PostMapping("/security/login/chang")
    public String chang(HttpServletRequest request, ModelMap model, @RequestParam("username") String username) {
        User acc = userService.findID(request.getParameter("username"));
        if (acc.getPassword().equals(request.getParameter("password"))) {
            if (request.getParameter("password1").equals(request.getParameter("password2"))) {
                String password1 = request.getParameter("password1");
                userService.changePassword(password1, username);
                model.addAttribute("message", "Doi mat khau Thanh Cong");
                return "security/login";
            } else {
                model.addAttribute("message", "2 Mật khẩu không khớp");
            }
        } else {
            model.addAttribute("message", "Mật khẩu không đúng");
        }

        return "security/chang";
    
    }
    @GetMapping("/security/login/editprofile")
    public String editprofile(Model model) {
        return "security/editprofile";
    }

    @PostMapping("/security/login/editprofile")
    public String updateeditprofile(@ModelAttribute("accountRequest") User userRequest,
            RedirectAttributes redirectAttributes, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam("username") String username, Model model) {
        try {
               if(imageFile.getSize() ==0) {
            
                   userReponsitory.updateNonImage(userRequest.getEmail(), userRequest.getFullname(), userRequest.getUsername());
               }else {
                   String path = application.getRealPath("/");
                   userRequest.setPhoto(imageFile.getOriginalFilename());
                   String filepath = "D:/eclipse/DATN/" + folder_image + userRequest.getPhoto();
                   System.out.println(filepath);
                   File file = new File(filepath);
                   imageFile.transferTo(file);
                   accountService.update(userRequest);
               }
               redirectAttributes.addFlashAttribute("mess",
                    "Account " + userRequest.getUsername() + "was updated");
            
            session.setAttribute("image", accountService.findID(username).getPhoto());
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mess",
                    "cannot update account" + userRequest.getUsername());
        }
        return "redirect:/security/login/editprofile";
    }
    
//    @PostMapping("/security/login/editprofile")
//    public String updateeditprofile2(@RequestParam("email") String email,@RequestParam("fullname") String fullname,
//            RedirectAttributes redirectAttributes, @RequestParam(value = "imageFile") MultipartFile imageFile,
//            @RequestParam("username") String username, Model model) {
//        try {
//           
//            String path = application.getRealPath("/");
//            User u = userService.findID(username);
//            u.setPhoto(imageFile.getOriginalFilename());
//            String filepath = "D:/eclipse/DATN/" + folder_image + u.getPhoto();
//            u.setEmail(email);
//            u.setFullname(fullname);
//            System.out.println(filepath);
//            File file = new File(filepath);
//            imageFile.transferTo(file);
//            accountService.updateNonPass(email, fullname, imageFile.getOriginalFilename(), username);
//            redirectAttributes.addFlashAttribute("mess",
//                    "Account " + u.getUsername() + "was updated");
//            
//            session.setAttribute("image", accountService.findID(username).getPhoto());
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//            redirectAttributes.addFlashAttribute("mess",
//                    "cannot update account");
//        }
//        return "redirect:/security/login/editprofile";
//    }
	
}
