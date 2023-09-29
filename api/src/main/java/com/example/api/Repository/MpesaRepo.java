package com.example.api.Repository;

import com.example.api.Model.Mpesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MpesaRepo extends JpaRepository<Mpesa, Long> {

    Optional<Mpesa> findByMerchantIdAndCheckoutId(String merchant_id,String Checkout_id);
}
