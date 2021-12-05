package com.licenta.voinescuvlad.voinescuvlad.controllers;

import com.licenta.voinescuvlad.voinescuvlad.entities.Parking;
import com.licenta.voinescuvlad.voinescuvlad.services.ParkingService;
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
    private ParkingService parkingService;

    @Autowired
    private UserService userService;

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }


    @GetMapping("/")
    public String homePage(Model model) {
        if (parkingService.findAllAccepted().size() > 3) {
            List<Parking> accepted = parkingService.findAllAccepted();
            List<Parking> sortedByRating = accepted.stream()
                    .sorted(Comparator.comparing(Parking::getRatting).reversed())
                    .collect(Collectors.toList());


            int count = 0;
            for (Parking a : sortedByRating) {
                if (a.getRatting() >= 1)
                    count++;
            }

            if (count > 2) {
                Parking first = sortedByRating.get(0);
                Parking second = sortedByRating.get(1);
                Parking third = sortedByRating.get(2);
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

    @RequestMapping(path = "/viewParking/{id}")
    public String viewParkingById(Model model, @PathVariable("id") int id) {
        Parking parking = parkingService.findById(id);
        model.addAttribute(parking);

        return "/HOME/viewParking";
    }


    @GetMapping(value = "/parkings")
    public ModelAndView searchGET(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        List<Parking> parkings = parkingService.findAllAccepted();
        model.addAttribute("search", parkings);
        modelAndView.setViewName("/HOME/search");

        return modelAndView;
    }

    @GetMapping(value = "/searchParkings")
    public String searchPOST(@RequestParam("search") String search, @RequestParam("criteria") String criteria, Model model) {
        List<Parking> parkings = null;
        switch (criteria) {
            case "parkingPrice": {
                parkings = parkingService.findByMaxPrice(search);
                model.addAttribute("search", parkings);
                break;
            }
            case "parkingCity": {
                parkings = parkingService.findAllByTheCity(search);
                model.addAttribute("search", parkings);
                break;
            }
            case "none": {
                parkings = parkingService.findAllAccepted();
                model.addAttribute("search", parkings);
                break;
            }
            case "parkingCountry": {
                parkings = parkingService.findAllByTheCountry(search);
                model.addAttribute("search", parkings);
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
        List<Parking> parkings = null;
        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Parking> ownedParkings = parkingService.findByUser(userService.findByUsername(auth));
        parkings = parkingService.findAllAccepted();
        parkings.removeAll(ownedParkings);
        model.addAttribute("search", parkings);
        modelAndView.setViewName("/HOME/loggedSearch");

        return modelAndView;
    }

    @GetMapping(value = "/searchStays")
    public String searchLUPOST(@RequestParam("search") String search, @RequestParam("criteria") String criteria, Model model) {

        List<Parking> parkings = null;
        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Parking> ownedParkings = parkingService.findByUser(userService.findByUsername(auth));
        switch (criteria) {
            case "parkingPrice": {

                parkings = parkingService.findByMaxPrice(search);
                parkings.removeAll(ownedParkings);
                model.addAttribute("search", parkings);
                break;
            }
            case "parkingCity": {
                parkings = parkingService.findAllByTheCity(search);
                parkings.removeAll(ownedParkings);
                model.addAttribute("search", parkings);
                break;
            }
            case "none": {
                parkings = parkingService.findAllAccepted();
                parkings.removeAll(ownedParkings);
                model.addAttribute("search", parkings);
                break;
            }
            case "parkingCountry": {
                parkings = parkingService.findAllByTheCountry(search);
                parkings.removeAll(ownedParkings);
                model.addAttribute("search", parkings);
                break;
            }

            default:
                break;

        }
        return "/HOME/loggedSearch";

    }


}
