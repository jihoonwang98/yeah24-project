package com.prac.controller;

import com.prac.dto.UserDTO;
import com.prac.dto.UserDTO.UserRegisterDTO;
import com.prac.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/join")
    public String joinPage(@ModelAttribute("registerDTO") UserRegisterDTO registerDTO) {
        log.info("GET /auth/join");
        log.info("registerDTO: " + registerDTO);
        return "auth/join";
    }

    @PostMapping("/join")
    public String joinProcessing(@ModelAttribute("registerDTO") @Valid UserRegisterDTO registerDTO, BindingResult bindingResult) {
        log.info("POST /auth/join");
        log.info("registerDTO: " + registerDTO);
        log.info("profileImg: " + registerDTO.getProfileImg());

        MultipartFile profileImg = registerDTO.getProfileImg();
        log.info("isempty: " + profileImg.isEmpty());

        if(bindingResult.hasErrors()) {
            return "auth/join";
        }

        userService.register(registerDTO);
        return "redirect:/";
    }

}
