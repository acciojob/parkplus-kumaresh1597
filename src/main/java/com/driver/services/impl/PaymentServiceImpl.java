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

        if(amountSent < bill) throw new Exception("Insufficient Amount");

        boolean isCard = PaymentMode.CARD.name().equalsIgnoreCase(mode);
        boolean isUPI = PaymentMode.UPI.name().equalsIgnoreCase(mode);
        boolean isCash = PaymentMode.CASH.name().equalsIgnoreCase(mode);

        if(!isCard && !isCash && !isUPI){
            throw new Exception("Payment mode not detected");
        }


        Payment newPayment = new Payment();

        if(isCard) {
            newPayment.setPaymentMode(PaymentMode.CARD);
        }
        else if (isUPI){
            newPayment.setPaymentMode(PaymentMode.UPI);
        }
        else if (isCash) {
            newPayment.setPaymentMode(PaymentMode.CASH);
        }

        newPayment.setPaymentCompleted(true);
        newPayment.setReservation(reservation);

        paymentRepository2.save(newPayment);

        reservation.setPayment(newPayment);
        reservationRepository2.save(reservation);

        return newPayment;
    }
}
