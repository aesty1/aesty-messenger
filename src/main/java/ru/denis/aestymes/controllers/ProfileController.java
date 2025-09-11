package ru.denis.aestymes.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.denis.aestymes.dtos.ChangePasswordDto;
import ru.denis.aestymes.dtos.UserDTO;
import ru.denis.aestymes.models.MyUser;
import ru.denis.aestymes.services.MyUserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private MyUserService myUserService;

    @GetMapping("/edit")
    public String editProfilePage(Model model, HttpServletRequest request) {
        MyUser user = myUserService.getUserById(myUserService.getCurrentUserId(request));

        model.addAttribute("user", user);
        model.addAttribute("userDTO", new UserDTO(user.getId(), user.getUsername(), user.getName(), user.getAvatarUrl()));

        return "/profile/edit";
    }

    @PostMapping("/edit/save")
    public String saveProfile(@ModelAttribute("userDTO") UserDTO userDTO, BindingResult result, HttpServletRequest request) {
        if(result.hasErrors()) {
            return "/profile/edit";
        }

        try {
            myUserService.updateProfile(myUserService.getCurrentUserId(request), userDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/chats";
    }

    @GetMapping("/password/edit")
    public String passwordEditPage(Model model) {
        model.addAttribute("passwordDto", new ChangePasswordDto());

        return "/profile/editPassword";
    }

    @PostMapping("/password/edit/save")
    public RedirectView passwordEdit(@ModelAttribute ChangePasswordDto passwordDto, HttpServletRequest request) {
        if(passwordDto != null) {
            myUserService.changePassword(passwordDto, myUserService.getUserById(myUserService.getCurrentUserId(request)));

            return new RedirectView("/chats");
        } else {
            throw new BadCredentialsException("Password exception");
        }
    }
}
