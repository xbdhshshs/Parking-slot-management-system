package com.licenta.voinescuvlad.voinescuvlad.controllers;

import com.licenta.voinescuvlad.voinescuvlad.entities.Apartment;
import com.licenta.voinescuvlad.voinescuvlad.services.ApartmentService;
import com.licenta.voinescuvlad.voinescuvlad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GuestController {


    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private UserService userService;

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }


    @GetMapping("/")
    public String homePage(Model model) {
        if (apartmentService.findAllAccepted().size() > 3) {
            List<Apartment> accepted = apartmentService.findAllAccepted();
            List<Apartment> sortedByRating = accepted.stream()
                    .sorted(Comparator.comparing(Apartment::getRatting).reversed())
                    .collect(Collectors.toList());


            int count = 0;
            for (Apartment a : sortedByRating) {
                if (a.getRatting() >= 1)
                    count++;
            }

            if (count > 2) {
                Apartment first = sortedByRating.get(0);
                Apartment second = sortedByRating.get(1);
                Apartment third = sortedByRating.get(2);
                model.addAttribute("first", first);
                model.addAttribute("second", second);
                model.addAttribute("third", third);
                return "/HOME/homePage";
            }
            else return "/HOME/default";
        }

        return "/HOME/default";

    }


    @GetMapping("/contact")
    public String contact() {

        return "/HOME/contactPage";
    }

    @GetMapping("/aboutUs")
    public String about() {

        return "/HOME/aboutPage";
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @RequestMapping(path = "/viewApartment/{id}")
    public String viewApartmentById(Model model, @PathVariable("id") int id) {
        Apartment apartment = apartmentService.findById(id);
        model.addAttribute(apartment);

        return "/HOME/viewApartment";
    }


    @GetMapping(value = "/apartments")
    public ModelAndView searchGET(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        List<Apartment> apartments = apartmentService.findAllAccepted();
        model.addAttribute("search", apartments);
        modelAndView.setViewName("/HOME/search");

        return modelAndView;
    }

    @GetMapping(value = "/searchApartments")
    public String searchPOST(@RequestParam("search") String search, @RequestParam("criteria") String criteria, Model model) {
        List<Apartment> apartments = null;
        switch (criteria) {
            case "apartmentPrice": {
                apartments = apartmentService.findByMaxPrice(search);
                model.addAttribute("search", apartments);
                break;
            }
            case "apartmentCity": {
                apartments = apartmentService.findAllByTheCity(search);
                model.addAttribute("search", apartments);
                break;
            }
            case "none": {
                apartments = apartmentService.findAllAccepted();
                model.addAttribute("search", apartments);
                break;
            }
            case "apartmentCountry": {
                apartments = apartmentService.findAllByTheCountry(search);
                model.addAttribute("search", apartments);
                break;
            }

            default:
                break;

        }


        return "/HOME/search";

    }


    @GetMapping(value = "/stays")
    public ModelAndView searchLUGET(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        List<Apartment> apartments = null;
        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Apartment> ownedApartments = apartmentService.findByUser(userService.findByUsername(auth));
        apartments = apartmentService.findAllAccepted();
        apartments.removeAll(ownedApartments);
        model.addAttribute("search", apartments);
        modelAndView.setViewName("/HOME/loggedSearch");

        return modelAndView;
    }

    @GetMapping(value = "/searchStays")
    public String searchLUPOST(@RequestParam("search") String search, @RequestParam("criteria") String criteria, Model model) {

        List<Apartment> apartments = null;
        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Apartment> ownedApartments = apartmentService.findByUser(userService.findByUsername(auth));
        switch (criteria) {
            case "apartmentPrice": {

                apartments = apartmentService.findByMaxPrice(search);
                apartments.removeAll(ownedApartments);
                model.addAttribute("search", apartments);
                break;
            }
            case "apartmentCity": {
                apartments = apartmentService.findAllByTheCity(search);
                apartments.removeAll(ownedApartments);
                model.addAttribute("search", apartments);
                break;
            }
            case "none": {
                apartments = apartmentService.findAllAccepted();
                apartments.removeAll(ownedApartments);
                model.addAttribute("search", apartments);
                break;
            }
            case "apartmentCountry": {
                apartments = apartmentService.findAllByTheCountry(search);
                apartments.removeAll(ownedApartments);
                model.addAttribute("search", apartments);
                break;
            }

            default:
                break;

        }
        return "/HOME/loggedSearch";

    }


}
