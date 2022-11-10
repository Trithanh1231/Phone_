package com.edu.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.edu.Utils.Utility;
import com.edu.model.User;
import com.edu.service.UserService;

import net.bytebuddy.utility.RandomString;


@Controller
public class ForgotPasswordController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JavaMailSender mailsender;

	@GetMapping("/forgot_password")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("pageTitle", "Fogot Password");
        return "security/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(45);

        try {
            userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            System.out.println(token);
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email. Please check.");
        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email.");
            e.printStackTrace();
        }
        model.addAttribute("pageTitle", "Fogot Password");
        return "security/forgot_password_form";
    }

    private void sendEmail(String email, String resetPasswordLink)
            throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailsender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("dangdhps16394@fpt.edu.vn", "Shopme Support");
        helper.setTo(email);

        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>" + "<p><b><a href=\"" + resetPasswordLink
                + "\">Change my password</a></b></p>" + "<br>" + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";
        helper.setSubject(subject);

        helper.setText(content, true);

        mailsender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value="token") String token, Model model) {
        User account = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
        model.addAttribute("pageTitle", "Reset your Password");
        if(account == null) {
            model.addAttribute("message", "Invalid Token");
            model.addAttribute("title", "Reset your password");
            return "security/message";
        }
        return "security/reset_password_form";

    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
         
        User account = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");
         
        if (account == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        } else {
            userService.changePassword(password, account.getUsername());
            model.addAttribute("message", "You have successfully changed your password.");
        }
         
        return "security/message";

    }
}
