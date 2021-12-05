package com.licenta.voinescuvlad.voinescuvlad.controllers;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.UserRegistrationDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import com.licenta.voinescuvlad.voinescuvlad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.validation.Valid;

import java.util.Random;
class Generator {
    public static String generateRandomPassword(int len) {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijk"
                +"lmnopqrstuvwxyz!@#$%&";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}

@Controller
@RequestMapping("/registration")
public class UserRegisterController {

    @Autowired
    private EmailController emailService;

    @Autowired
    private UserService userService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {

        return new UserRegistrationDto();
    }
boolean Otpbool =false;
//    public String Otp="87GkhwYkj";

    Generator obj=new Generator();
    public String Otp= obj.generateRandomPassword(6);

    @GetMapping
    public String showRegistrationForm(Model theModel) {

        UserRegistrationDto usertDto = new UserRegistrationDto();
        if(!theModel.containsAttribute("user")){
            theModel.addAttribute("user", usertDto);
        }
        return "registration";
    }

//    @PostMapping
//    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto user,
//                                      BindingResult result, RedirectAttributes attr) {
//
//        User existing = userService.findByUsername(user.getUserName());
//        if (existing != null) {
//
//            return "errorRegistrationPage";
//        }
//        if (result.hasErrors()) {
//
//            attr.addFlashAttribute("org.springframework.validation.BindingResult.user",
//                    result);
//            attr.addFlashAttribute("user", user);
//            return "redirect:/registration";
//        }
//
//        try {
//            emailService.sendHelloEmail(user.getEmail(), user.getUserName());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        userService.save(user);
//        AdminController.u = AdminController.u + 2;
//
//        return "redirect:/login";
//    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto user,@RequestParam(value="otp" , defaultValue = "")String otp,
                                      BindingResult result, RedirectAttributes attr,Model model) {
//         String Otp = RandomString.make(8);

        System.out.println(otp);
        System.out.println(Otp);
        User existing = userService.findByUsername(user.getUserName());
        if (existing != null) {

            return "errorRegistrationPage";
        }
        if (result.hasErrors()) {

            attr.addFlashAttribute("org.springframework.validation.BindingResult.user",
                    result);
            attr.addFlashAttribute("user", user);
            return "redirect:/registration";
        }
        if (!Otp.equalsIgnoreCase(otp)) {
            System.out.println(otp);
            System.out.println(Otp);
            emailService.sendOtp(user.getEmail(), user.getUserName(), Otp);
            Otpbool=true;
            model.addAttribute("otpbool", true);

            return "registration";
        }
        System.out.println(otp);
        System.out.println(Otp);

        if (otp.equals(Otp)) {
            System.out.println(otp);
            System.out.println(Otp);
            try {

                emailService.sendHelloEmail(user.getEmail(), user.getUserName());

            } catch (Exception e) {
                e.printStackTrace();
            }
            userService.save(user);

            return "redirect:/login";



        }
        else {
            return "redirect:/registration";
        }



    }

}


