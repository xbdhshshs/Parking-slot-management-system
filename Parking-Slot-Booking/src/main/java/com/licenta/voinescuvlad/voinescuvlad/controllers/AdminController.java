package com.licenta.voinescuvlad.voinescuvlad.controllers;

import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.ApartmentDto;
import com.licenta.voinescuvlad.voinescuvlad.controllers.dto.UserRegistrationDto;
import com.licenta.voinescuvlad.voinescuvlad.entities.Apartment;
import com.licenta.voinescuvlad.voinescuvlad.entities.Booking;
import com.licenta.voinescuvlad.voinescuvlad.entities.Role;
import com.licenta.voinescuvlad.voinescuvlad.entities.User;
import com.licenta.voinescuvlad.voinescuvlad.services.ApartmentService;
import com.licenta.voinescuvlad.voinescuvlad.services.BookingService;
import com.licenta.voinescuvlad.voinescuvlad.services.DtoMapping;
import com.licenta.voinescuvlad.voinescuvlad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller()
@RequestMapping("/admin")
public class AdminController {

    public static int i = 4;
    public static int u = 0;
    @Autowired
    private ApartmentService apartmentService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailController emailService;


    @Autowired
    DtoMapping converter;

    @Autowired
    private BookingService bookingService;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {

        return new UserRegistrationDto();
    }

    @GetMapping()
    public String adminHomePage() {

        return "/ADM/optionsPage";
    }

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }

    @PostMapping("/save")
    public String addApartment(@ModelAttribute("apartment") @Valid ApartmentDto apartment, BindingResult result, RedirectAttributes attr) {


        if (result.hasErrors()) {

            attr.addFlashAttribute("org.springframework.validation.BindingResult.apartment",
                    result);
            attr.addFlashAttribute("apartment", apartment);
            return "redirect:/ADM/showFormForAdd";
        }

        apartmentService.save(apartment);
        AdminController.i++;
        return "redirect:/admin/checkApartment/" + AdminController.i;
    }

    //USER

    @GetMapping("/users")
    public String userList(Model theModel) {
        List<User> theUsers = userService.findAllUsers();
        User admin = userService.findByUsername("admin");
        User w = userService.findByUsername("worker");
        User w1 = userService.findByUsername("worker A");
        User w2 = userService.findByUsername("worker B");
        User w3 = userService.findByUsername("worker C");
        theUsers.remove(admin);
        theUsers.remove(w);
        theUsers.remove(w1);
        theUsers.remove(w2);
        theUsers.remove(w3);

        if (theUsers.size() == 0)
            return "/ADM/empty";

        theModel.addAttribute("users", theUsers);

        return "/ADM/userList";
    }

    @GetMapping("/workers")
    public String workercontrol() {
        return "/ADM/workers";
    }

    @GetMapping("/workers/registerworker")
    public String showRegistrationFormforworker(Model theModel) {

        UserRegistrationDto usertDto = new UserRegistrationDto();
        if (!theModel.containsAttribute("user")) {
            theModel.addAttribute("user", usertDto);
        }
        return "/ADM/registerworker";
    }

    @PostMapping("/workers/registerworker")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto user,
                                      BindingResult result, RedirectAttributes attr) {

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

        try {
            emailService.sendHelloEmail(user.getEmail(), user.getUserName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        userService.save(user);
        AdminController.u = AdminController.u + 2;
        int r = AdminController.u + 1;

        return "/ADM/workerList";
    }

    @GetMapping(path = "/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        ApartmentDto apartmentDto = new ApartmentDto();


        if (!theModel.containsAttribute("apartment")) {
            theModel.addAttribute("apartment", apartmentDto);
        }

        return "/ADM/apartment-form";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);

        return "redirect:/admin";
    }

    @RequestMapping(path = "/viewUser/{id}")
    public String viewUserById(Model model, @PathVariable("id") int id) {
        User user = userService.findById(id);
        List<Apartment> apartments = apartmentService.findByUser(user);
        List<String> apartNames = new ArrayList<String>();
        for (int i = 0; i < apartments.size(); i++) {
            apartNames.add(apartments.get(i).getApartmentName());
        }
        model.addAttribute("user", user);
        model.addAttribute("apartNames", apartNames);

        return "/ADM/viewUser";
    }


    //APARTMENT

    @RequestMapping(path = "/checkApartment/{id}")
    public String reviewApartment(Model model, @PathVariable("id") int id) throws IOException {
        Apartment apartment = apartmentService.findById(id);
        String acceptedStatus = "accepted";
        String declinedStatus = "declined";
        model.addAttribute("apartment", apartment);
        model.addAttribute("accepted", acceptedStatus);
        model.addAttribute("declined", declinedStatus);

        return "/ADM/reviewApartment";
    }
    ///////////////
    @RequestMapping(path = "/deleteApartment/{id}")
    public String deleteApartment(Model model, @PathVariable("id") int id) throws IOException {
        Apartment apartment = apartmentService.findById(id);
        model.addAttribute("apartment", apartment);
        apartmentService.delete(apartment);
        return "redirect:/admin/deleteApartments";
    }


    @GetMapping("/pendingApartments")
    public String listPendingApartments(Model theModel) {
        List<Apartment> theApartments = apartmentService.findAllApartmentsByStatus("pending");

        if (theApartments.size() == 0)
            return "/ADM/empty";

        theModel.addAttribute("apartments", theApartments);

        return "/ADM/pendingApartments";


    }
    ////////
    @GetMapping("/deleteApartments")
    public String listApartmentstodelete(Model theModel) {
        List<Apartment> theApartments = apartmentService.findAllApartmentsByStatus("accepted");

        if (theApartments.size() == 0)
            return "/ADM/empty";

        theModel.addAttribute("apartments", theApartments);

        return "/ADM/deleteapartment";

    }

    @PostMapping("/saveStatus")
    public String updateApartmentStatus(@ModelAttribute("apartmentId") int apartmentId, @ModelAttribute("apartmentStatus") String status) {
        Apartment apartment = apartmentService.findById(apartmentId);
        ApartmentDto dto = converter.getApartmentDtoFromApartment(apartment);
        dto.setStatus(status);
        if (apartment.getUser().getUserName() == "admin") {
            apartment.setStatus("accepted");
        }
        apartmentService.updateStatus(dto);

        return "redirect:/admin/pendingApartments";
    }


    //STATISTICS

    @RequestMapping(path = "/statistics")
    public String statistics(Model model) {
        List<Booking> bookings = bookingService.findAll();
        int numberOfBookings = bookings.size();
        int totalIncome = 0;
        for (Booking b : bookings) {
            long diff = b.getCheckOut().getTime() - b.getCheckIn().getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
            int SumDays = (int) diffDays;
            totalIncome += b.getApartment().getPpn() * SumDays;
        }
        model.addAttribute("bookingsNumber", numberOfBookings);
        model.addAttribute("totalIncome", totalIncome);

        return "/ADM/statistics";
    }

    @RequestMapping("/chart")
    public String generateGraph(Model model) {
        Map<String, Integer> techMap = getTechnologyMap();
        model.addAttribute("techMap", techMap);

        return "/ADM/chart";
    }


    @RequestMapping("/pie")
    public String generatePieChart(Model model) {

        List<Booking> bookings = bookingService.findAll();

        int sumIan = 0, sumFeb = 0, sumMar = 0, sumApr = 0, sumMay = 0, sumIun = 0, sumIul = 0, sumAug = 0, sumSep = 0, sumOct = 0, sumNov = 0, sumDec = 0;
        for (Booking b : bookings) {
            switch (b.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue()) {
                case 1:
                    sumIan += getBookingPrice(b);
                    break;
                case 2:
                    sumFeb += getBookingPrice(b);
                    break;
                case 3:
                    sumMar += getBookingPrice(b);
                    break;
                case 4:
                    sumApr += getBookingPrice(b);
                    break;
                case 5:
                    sumMay += getBookingPrice(b);
                    break;
                case 6:
                    sumIun += getBookingPrice(b);
                    break;
                case 7:
                    sumIul += getBookingPrice(b);
                    break;
                case 8:
                    sumAug += getBookingPrice(b);
                    break;
                case 9:
                    sumSep += getBookingPrice(b);
                    break;
                case 10:
                    sumOct += getBookingPrice(b);
                    break;
                case 11:
                    sumNov += getBookingPrice(b);
                    break;
                case 12:
                    sumDec += getBookingPrice(b);
                    break;
                default:
                    break;
            }


        }


        model.addAttribute("January", sumIan);
        model.addAttribute("February", sumFeb);
        model.addAttribute("March", sumMar);
        model.addAttribute("April", sumApr);
        model.addAttribute("May", sumMay);
        model.addAttribute("June", sumIun);
        model.addAttribute("July", sumIul);
        model.addAttribute("August", sumAug);
        model.addAttribute("September", sumSep);
        model.addAttribute("October", sumOct);
        model.addAttribute("November", sumNov);
        model.addAttribute("December", sumDec);
        return "/ADM/pie";

    }

    private double getBookingPrice(Booking booking) {
        double sum = 0;
        long diff = booking.getCheckOut().getTime() - booking.getCheckIn().getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        int SumDays = (int) diffDays;
        sum = booking.getApartment().getPpn() * SumDays;
        return sum;
    }

    private Map<String, Integer> getTechnologyMap() {
        List<Booking> bookings = bookingService.findAll();
        List<Date> checkInDates = new ArrayList<>();
        for (Booking b : bookings) {
            checkInDates.add(b.getCheckIn());
        }

        List<Integer> months = new ArrayList<>();
        for (Date d : checkInDates) {
            months.add(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getMonthValue());
        }

        int januaryNr = 0, februaryNr = 0, marchNr = 0, aprilNr = 0, mayNr = 0, juneNr = 0, julyNr = 0, augustNr = 0, septemberNr = 0, octoberNr = 0, novemberNr = 0, decemberNr = 0;

        for (int i = 0; i < months.size(); i++) {
            switch (months.get(i)) {
                case 1:
                    januaryNr++;
                    break;
                case 2:
                    februaryNr++;
                    break;
                case 3:
                    marchNr++;
                    break;
                case 4:
                    aprilNr++;
                    break;
                case 5:
                    mayNr++;
                    break;
                case 6:
                    juneNr++;
                    break;
                case 7:
                    julyNr++;
                    break;
                case 8:
                    augustNr++;
                    break;
                case 9:
                    septemberNr++;
                    break;
                case 10:
                    octoberNr++;
                    break;
                case 11:
                    novemberNr++;
                    break;
                case 12:
                    decemberNr++;
                    break;

                default:
                    break;
            }
        }
        Map<String, Integer> techMap = new ConcurrentHashMap<>();
        techMap.put("January", januaryNr);
        techMap.put("February", februaryNr);
        techMap.put("March", marchNr);
        techMap.put("April", aprilNr);
        techMap.put("May", mayNr);
        techMap.put("June", juneNr);
        techMap.put("July", julyNr);
        techMap.put("August", augustNr);
        techMap.put("September", septemberNr);
        techMap.put("Octomber", octoberNr);
        techMap.put("November", novemberNr);
        techMap.put("December", decemberNr);
        return techMap;
    }

    //Worker

    @GetMapping("/worker")
    public String workerList(Model theModel) {
        List<User> theUsers = userService.findAllUsers();
        User admin = userService.findByUsername("admin");
        User u = userService.findByUsername("user");
        User u1 = userService.findByUsername("user A");
        User u2 = userService.findByUsername("user B");
        User u3 = userService.findByUsername("user C");
        theUsers.remove(admin);
        theUsers.remove(u);
        theUsers.remove(u1);
        theUsers.remove(u2);
        theUsers.remove(u3);

        if (theUsers.size() == 0)
            return "/ADM/empty";

        theModel.addAttribute("users", theUsers);

        return "/ADM/workerList";
    }

//    @RequestMapping(path = "/delete/{id}")
//    public String deleteWorkerById(@PathVariable("id") int id) {
//        userService.deleteUserById(id);
//
//        return "redirect:/admin/users";
//    }

    @RequestMapping(path = "/viewWorker/{id}")
    public String viewWorkerById(Model model, @PathVariable("id") int id) {
        User user = userService.findById(id);
        List<Apartment> apartments = apartmentService.findByUser(user);
        List<String> apartNames = new ArrayList<String>();
        for (int i = 0; i < apartments.size(); i++) {
            apartNames.add(apartments.get(i).getApartmentName());
        }
        model.addAttribute("user", user);
        model.addAttribute("apartNames", apartNames);

        return "/ADM/viewWorker";
    }
}
