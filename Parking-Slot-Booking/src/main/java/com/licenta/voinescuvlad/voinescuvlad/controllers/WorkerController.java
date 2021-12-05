package com.licenta.voinescuvlad.voinescuvlad.controllers;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.ApartmentDto;
import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.BookingDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Apartment;
import com.licenta.voinescuvlad.voinescuvlad.entities.Booking;
import com.licenta.voinescuvlad.voinescuvlad.entities.Rating;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import com.licenta.voinescuvlad.voinescuvlad.services.ApartmentService;
import com.licenta.voinescuvlad.voinescuvlad.services.BookingService;
import com.licenta.voinescuvlad.voinescuvlad.services.DtoMapping;
import com.licenta.voinescuvlad.voinescuvlad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/worker")
public class WorkerController {

    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private DtoMapping converter;

    @Autowired
    private EmailController emailService;

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }


    //worker

    @GetMapping()
    public String authentificatedUserHomePage(Model theModel, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());
        theModel.addAttribute("currentUser", user);

        return "/Worker/optionsPage";
    }

    @GetMapping(value = "/viewProfile")
    public String viewProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        model.addAttribute("user", user);


        //Tourist
        List<Booking> myBookings = bookingService.findAllByUser(user.getId());
        double totalIncome = 0;
        for (Booking b : myBookings) {
            long diff = b.getCheckOut().getTime() - b.getCheckIn().getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
            int SumDays = (int) diffDays;
            totalIncome += b.getApartment().getPpn() * SumDays;
        }
        int size = myBookings.size();
        model.addAttribute("size", size);
        model.addAttribute("sum", totalIncome);
        List<Apartment> activeApartments = apartmentService.findAllAcceptedByUser(user);
        int guestIncome = 0;
        int bookingsSize = 0;
        for (Apartment a : activeApartments) {
            for (Booking b : a.getBookings()) {
                long diff = b.getCheckOut().getTime() - b.getCheckIn().getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
                int SumDays = (int) diffDays;
                guestIncome += b.getApartment().getPpn() * SumDays;
                bookingsSize++;
            }

        }


        model.addAttribute("activeApartments", activeApartments.size());
        model.addAttribute("guestIncome", guestIncome);
        model.addAttribute("reservations", bookingsSize);

        return "/Worker/viewProfile";
    }

    //APARTMENT

    @GetMapping("/stays")
    public String ownedApartments(Model theModel) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        List<Apartment> theApartments = apartmentService.findByUser(user);
        if (theApartments.size() == 0)
            return "/Worker/empty";
        else {
            theModel.addAttribute("apartments", theApartments);
            theModel.addAttribute("theUser", user);

            return "/Worker/list-apartments";
        }
    }

    @RequestMapping(path = "/viewApartment/{id}")
    public String viewApartmentById(Model model, @PathVariable("id") int id) {
        Apartment apartment = apartmentService.findById(id);
        model.addAttribute(apartment);
        List<Booking> activeBookings = bookingService.findBookingByTheApartment(id);
        Date actual = new Date();
        List<Booking> valid = new ArrayList<>();
        for (Booking b : activeBookings) {
            if (b.getCheckIn().after(actual))
                valid.add(b);
        }
        model.addAttribute("valid", valid);

        return "/Worker/viewApartment";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteApartmentById(Model model, @PathVariable("id") int id) {
        Apartment apartment = apartmentService.findById(id);

        apartmentService.delete(apartment);


        return "redirect:/worker/stays";
    }

    @GetMapping(path = "/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        ApartmentDto apartmentDto = new ApartmentDto();


        if (!theModel.containsAttribute("apartment")) {
            theModel.addAttribute("apartment", apartmentDto);
        }

        return "/Worker/apartment-form";
    }

    @PostMapping("/save")
    public String addApartment(@ModelAttribute("apartment") @Valid ApartmentDto apartment, BindingResult result, RedirectAttributes attr) {


        if (result.hasErrors()) {

            attr.addFlashAttribute("org.springframework.validation.BindingResult.apartment",
                    result);
            attr.addFlashAttribute("apartment", apartment);
            return "redirect:/worker/showFormForAdd";
        }


        apartmentService.save(apartment);
        return "redirect:/worker/stays";
    }


    @GetMapping(value = "/updateApartment/{id}")
    public String showEditApartmentForm(Model model, @PathVariable("id") int id) {
        Apartment apartment = apartmentService.findById(id);
        ApartmentDto dto = converter.getApartmentDtoFromApartment(apartment);
        model.addAttribute("apartment", dto);

        return "/Worker/updateApartment";
    }

    @PostMapping("edit-apartment")
    public String saveEditForApartment(@ModelAttribute ApartmentDto dto) {
        apartmentService.update(dto);

        return "redirect:/worker/stays";
    }

//    @RequestMapping(value = "/favorites/{id}")
//    public String addToFavorites(@PathVariable("id") int id) {
//        Apartment fav = apartmentService.findById(id);
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName();
//        User currentUser = userService.findByUsername(name);
//        for (int i : currentUser.getFavorites()) {
//            if (fav.getId() == i) {
//                return "/Worker/duplicatedSave";
//            }
//        }
//        currentUser.getFavorites().add(fav.getId());
//        userService.update(currentUser);
//        return "redirect:/stays";
//
//    }

//    @GetMapping(value = "/savedApartments")
//    public String myFavorites(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName();
//        User currentUser = userService.findByUsername(name);
//        if (currentUser.getFavorites().size() == 0)
//            return "/Worker/empty";
//        else {
//            List<Apartment> favorites = new ArrayList<>();
//            for (int i : currentUser.getFavorites()) {
//                favorites.add(apartmentService.findById(i));
//            }
//            model.addAttribute("favorites", favorites);
//            return "/Worker/favoriteList";
//        }
//    }

    @GetMapping(value = "/savedApartments/remove/{id}")
    public String deleteFavorites(@PathVariable("id") int id) {
        Apartment fav = apartmentService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User currentUser = userService.findByUsername(name);
        int identity = fav.getId();
        for (Iterator i = currentUser.getFavorites().iterator(); i.hasNext(); ) {
            int favorite = (int) i.next();
            if (favorite == identity) {
                i.remove();
            }
        }
        userService.update(currentUser);


        return "redirect:/worker/savedApartments";
    }


    //BOOKING

    @GetMapping("/myBookings")
    public String ownedBookings(Model theModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        List<Booking> bookings = bookingService.findAllByUser(user.getId());
        Date currentDate = new Date();
        List<Booking> viableBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getCheckIn().after(currentDate))
                viableBookings.add(b);
        }
        if (viableBookings.size() == 0)
            return "/Worker/empty";

        theModel.addAttribute("bookings", viableBookings);
        return "/Worker/list-myBookings";

    }

    @RequestMapping("/deleteBooking/{id}")
    public String cancelBooking(Model model, @PathVariable("id") int id) {
        bookingService.delete(id);
        return "redirect:/worker/myBookings";
    }

    @RequestMapping(path = "/viewApartment/{id}/calendar")
    public String viewApartmentBookingCalendar(Model model, @PathVariable("id") int id) {
        List<Booking> bookings = bookingService.findBookingByTheApartment(id);
        Apartment theApartment = apartmentService.findById(id);
        int theId = theApartment.getId();
        if (bookings.size() == 0) {
            model.addAttribute("id", theId);

            return "/Worker/emptyCalendar";

        } else {
            model.addAttribute("bookings", bookings);

            return "/Worker/bookingCalendar";

        }
    }

    @GetMapping(path = "/viewApartment/{id}/calendar/showFormForAdd")
    public String showFormForAddBooking(Model theModel, @PathVariable("id") int id) {
        BookingDto bookingDto = new BookingDto();
        Apartment apartment = apartmentService.findById(id);
        theModel.addAttribute("apartment", apartment);
        if (!theModel.containsAttribute("booking")) {
            theModel.addAttribute("booking", bookingDto);
        }

        return "/Worker/bookingForm";
    }


    @PostMapping("/saveBooking/{id}")
    public String addBooking(Model model, @PathVariable("id") int id, @ModelAttribute("booking") @Valid BookingDto booking, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.booking",
                    result);
            attr.addFlashAttribute("booking", booking);
            return "redirect:/worker/viewApartment/" + id + "/calendar/showFormForAdd";
        }

        if (bookingService.isOverlapping(booking, id) == false) {
            model.addAttribute("id", id);
            return "/Worker/errorBookingForm";

        } else {
            Apartment apartment = apartmentService.findById(id);
            ApartmentDto dto = converter.getApartmentDtoFromApartment(apartment);
            booking.setApartmentDto(dto);
            bookingService.save(booking);


            //TODO:review this tomorrow!
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            User user = userService.findByUsername(name);


            emailService.sendBookingInfoForCustomer(user.getUserName(), user.getEmail(), booking.getCheckIn().toString().substring(0, 10),
                    booking.getCheckOut().toString().substring(0, 10), apartment.getApartmentName(), apartment.
                            getCity(), apartment.getCountrey(), apartment.getAdress());

//            emailService.sendBookingAlertToGuest(apartment.getUser().getUserName(), apartment.getUser().getEmail(),
//                    apartment.getApartmentName(), booking.getCheckIn().toString().substring(0, 10), booking.getCheckOut().toString().substring(0, 10));
//

            return "redirect:/worker/viewApartment/" + id + "/calendar";

        }
    }

//    @GetMapping(value = "/pastVisits")
//    public String viewPastExperiences(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName();
//        User user = userService.findByUsername(name);
//        List<Booking> bookings = bookingService.findAllByUser(user.getId());
//        List<Booking> pastBookings = new ArrayList<>();
//        Date currentDate = new Date();
//        for (Booking b : bookings) {
//            if (b.getCheckOut().before(currentDate))
//                pastBookings.add(b);
//        }
//
//
//        if (pastBookings.size() == 0)
//            return "/Worker/empty";
//
//        model.addAttribute("bookings", pastBookings);
//        model.addAttribute("rating", new Rating());
//
//        return "/Worker/pastBookings";
//    }

    //RATING

    @RequestMapping(value = "/saveRating/{id}")
    public String submitRating(Model model, @ModelAttribute("rating") Rating rating, @PathVariable("id") int id) {
        Booking booking = bookingService.findById(id);
        booking.setRating(rating);
        bookingService.update(booking);

        return "redirect:/worker/pastVisits";
    }

}
