package com.labs.ad_board.http.controller;

import com.labs.ad_board.dto.UserCreateEditDto;
import com.labs.ad_board.dto.UserLoginDto;
import com.labs.ad_board.dto.UserReadDto;
import com.labs.ad_board.exception.EmailException;
import com.labs.ad_board.exception.UsernameException;
import com.labs.ad_board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/register")
    public String getRegisterPage() {
        log.info("trying to register");
        return "register";
    }

    @PostMapping("/register")
    public String registerHandler(
            @ModelAttribute("user")
            UserCreateEditDto user,
            Model model,
            HttpServletRequest request
    ) {
        log.info(user.toString());

        List<String> errors = new ArrayList<>();

        if (userService.usernameExist(user.getUsername())) {
            errors.add("Username exist!");
        }
        if (userService.emailExist(user.getEmail())) {
            errors.add("Email exist!");
        }
        if (!user.getPassword().equals(user.getRePassword())) {
            errors.add("Passwords should match!");
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "register";
        }

        UserReadDto newUser = userService.registerNewUserAccount(user);

        model.addAttribute("user", newUser);

        return "redirect:/login";
    }

    @GetMapping("/login")
    public String getLoginPage(
            @RequestParam(value = "error", required = false)
            String error,
            Model model
    ) {
        log.info("Returning login page");
        if (error != null) {
            model.addAttribute(
                    "error",
                    "Incorrect email or username"
            );
        }
        return "login";
    }

//    @PostMapping("/login")
//    public String loginHandler(
//            @RequestBody
//            UserLoginDto userLoginDto,
//            Model model
//    ) {
//        log.info("Trying to login");
//
//        log.info(userLoginDto.toString());
//
//        log.info("checking password");
//        boolean isRightPassword = userService.areThePasswordsSame(
//                userLoginDto.getUsername(),
//                passwordEncoder.encode(userLoginDto.getPassword())
//        );
//
//        if (!userService.usernameExist(userLoginDto.getUsername()) ||
//            !isRightPassword) {
//            log.info("sending error info");
//            model.addAttribute(
//                    "error",
//                    "incorrect email or username"
//            );
//            return "login";
//        }
//
//
//
//        return "home";
//    }
}