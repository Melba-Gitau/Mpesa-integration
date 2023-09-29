package com.example.api.Controller;

import com.example.api.Model.Mpesa;
import com.example.api.Service.MpesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("v2")
public class MpesaController {
    @Autowired
    MpesaService mpesaService;



    @GetMapping ("/read")
    public HashMap<String, Object> list(){
        HashMap<String, Object> map = new HashMap<>();
        List<Mpesa> listMpesa = mpesaService.list();
        if(!listMpesa.isEmpty()){
            map.put("success",true);
            map.put("Message", "created successfully");
        }else{
            map.put("Success", false);
            map.put("Message","Failed to add");
        }
        return map;
    }
}
