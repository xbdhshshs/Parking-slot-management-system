package com.licenta.voinescuvlad.voinescuvlad.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "carReg")
    private String carReg;

    @Column(name = "password")
    private String password;

    @Column(name = "user_image")
    private byte[] userImage;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Parking> parkings = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new ArrayList<>();

    @ElementCollection
    private List<Integer> favorites = new ArrayList<>();

    public User() {
    }

    public double getRatting() {
        List<Parking> allParkings = this.getParkings();
        List<Booking> ratedBooking = new ArrayList<>();
        for (Parking a : allParkings) {
            for (Booking b : a.getBookings()) {
                if (b.getRating() != null)
                    ratedBooking.add(b);
            }
        }
        double sum = 0;
        for (Booking b : ratedBooking) {
            sum += b.getRating().getStars();
        }
        String result;
        if(sum>0) {
            result = Double.toString(sum / ratedBooking.size());
            String string = result.substring(0, 3);
            Double end = Double.parseDouble(string);
            return end;
        }

        else return 0;
    }

    public List<Integer> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<Integer> favorites) {
        this.favorites = favorites;
    }

    public User(String userName, String email, String password, byte[] userImage) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.userImage = userImage;
    }

    public User(String userName, String email, String password, Collection<Role> roles, byte[] userImage) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.userImage = userImage;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCarReg() {
        return carReg;
    }

    public void setCarReg(String carReg) {
        this.carReg = carReg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public List<Parking> getParkings() {
        return parkings;
    }

    public void setParkings(List<Parking> parkings) {
        this.parkings = parkings;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + "*********" + '\'' +
                ", roles=" + roles +
                '}';
    }


}
