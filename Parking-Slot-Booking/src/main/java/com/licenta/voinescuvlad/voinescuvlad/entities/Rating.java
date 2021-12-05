package com.licenta.voinescuvlad.voinescuvlad.entities;


import javax.persistence.*;

@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "stars")
    private int stars;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_booking_id")
    private Booking booking;

    public Rating() {
    }

    public Rating(int stars, User user, Booking booking) {
        this.stars = stars;
        this.booking = booking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }
}
