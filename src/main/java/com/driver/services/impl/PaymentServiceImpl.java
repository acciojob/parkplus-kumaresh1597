package com.driver.services.impl;

import com.driver.model.Payment;
import com.driver.model.PaymentMode;
import com.driver.model.Reservation;
import com.driver.repository.PaymentRepository;
import com.driver.repository.ReservationRepository;
import com.driver.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    ReservationRepository reservationRepository2;
    @Autowired
    PaymentRepository paymentRepository2;

    @Override
    public Payment pay(Integer reservationId, int amountSent, String mode) throws Exception {
        Reservation reservation = reservationRepository2.findById(reservationId).get();
        int hrs =  reservation.getNumberOfHours();
        int price = reservation.getSpot().getPricePerHour();
        int bill = hrs * price;

        if(mode.equalsIgnoreCase("cash") || mode.equalsIgnoreCase("card") || mode.equalsIgnoreCase("upi")) {
            if(amountSent!=bill) throw new Exception("Insufficient Amount");
            Payment payment = new Payment();
            PaymentMode paymentMode;
            if (mode.equalsIgnoreCase("cash")) paymentMode = PaymentMode.CASH;
            else if (mode.equalsIgnoreCase("card")) paymentMode = PaymentMode.CARD;
            else paymentMode = PaymentMode.UPI;
            payment.setPaymentMode(paymentMode);
            payment.setPaymentCompleted(Boolean.TRUE);
            reservation.getSpot().setOccupied(Boolean.FALSE);
            payment.setReservation(reservation);

            reservation.setPayment(payment);

            reservationRepository2.save(reservation);
            return payment;
        }
        else {
            throw new Exception("Payment mode not detected");
        }
    }
}
