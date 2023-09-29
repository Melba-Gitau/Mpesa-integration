package com.example.api.Service;

import com.example.api.Model.Mpesa;
import com.example.api.Repository.MpesaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MpesaService {
    @Autowired
    MpesaRepo mpesaRepo;
    public boolean create(Mpesa mpesa) {
        try {
            mpesaRepo.save(mpesa);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean update(Mpesa mpesa) {
        try {
            mpesaRepo.save(mpesa);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public   Optional<Mpesa> findByMerchantIdAndCheckoutId(String merchant_id,String Checkout_id){
      return mpesaRepo.findByMerchantIdAndCheckoutId(merchant_id,Checkout_id);
    }
    public List<Mpesa> list(){
           return  mpesaRepo.findAll();
    }

    public boolean updateMpesa(Long id, Mpesa mpesa){
        Optional<Mpesa> scene = mpesaRepo.findById(id);
        if(scene.isPresent()){
            Mpesa updated  = scene.get();
            updated.setUsername(mpesa.getUsername());
            updated.setPhone(mpesa.getPhone());
            updated.setMerchantId(mpesa.getMerchantId());
            updated.setCheckoutId(mpesa.getCheckoutId());
            updated.setAmount(mpesa.getAmount());
            updated.setTransactionCode(mpesa.getTransactionCode());
            updated.setStatus(mpesa.getStatus());
            mpesaRepo.save(mpesa);
            return true;
        }else {
            return false;
        }
    }
    public boolean deleteMpesa(Long id){
        Optional<Mpesa> scene = mpesaRepo.findById(id);
        if(scene.isPresent()){
            mpesaRepo.delete(scene.get());
            return true;
        }else{
            return false;
        }
    }





}
