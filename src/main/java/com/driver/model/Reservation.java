package com.driver.model;

import javax.persistence.*;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int noOfHours;

    @ManyToOne
    @JoinColumn
    private Spot spot;

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToOne
    @JoinColumn
    private Payment payment;

    public Reservation() {
    }

    public Spot getSpot() {
        return spot;
    }

    public void setSpot(Spot spot) {
        this.spot = spot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNoOfHours() {
        return noOfHours;
    }

    public void setNoOfHours(int noOfHours) {
        this.noOfHours = noOfHours;
    }
}
