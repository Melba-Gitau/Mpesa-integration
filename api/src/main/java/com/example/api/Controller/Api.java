package com.example.api.Controller;

import com.example.api.DTO.Payment;
import com.example.api.Model.Mpesa;
import com.example.api.Service.MpesaService;
import com.example.api.Util.AccessToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("vi")
public class Api {
    @Autowired
    MpesaService mpesaService;
    @PostMapping("create")

    public HashMap<String, Object>map(@RequestBody Payment payment){
        HashMap<String,Object> map = new HashMap<>();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String accessToken=getAccessToken();
        MediaType mediaType = MediaType.parse("application/json");
        okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\n" +
                "                \"BusinessShortCode\": 174379,\n" +
                "                \"Password\": \"MTc0Mzc5YmZiMjc5ZjlhYTliZGJjZjE1OGU5N2RkNzFhNDY3Y2QyZTBjODkzMDU5YjEwZjc4ZTZiNzJhZGExZWQyYzkxOTIwMjMwNDI4MTc0NzM3\",\n" +
                "                \"Timestamp\": \"20230428174737\",\n" +
                "                \"TransactionType\": \"CustomerPayBillOnline\",\n" +
                "                \"Amount\":  "+payment.getAmount()+",\n" +
                "                \"PartyA\": "+payment.getPhone()+",\n" +
                "                \"PartyB\": 174379,\n" +
                "                \"PhoneNumber\": "+payment.getPhone()+",\n" +
                "                \"CallBackURL\": \"https://salty-buckets-join.loca.lt/vi/skdsowakdm/test\",\n" +
                "                \"AccountReference\": \"CompanyXLTD\",\n" +
                "                \"TransactionDesc\": \"Payment of X\" \n"+
                "  }");
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/mpesa/stkpush/v1/processrequest")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer "+accessToken)
                .build();


        try {
            Response response = client.newCall(request).execute();
            String responseBody=response.body().string();
            Gson gson=new Gson();
            JsonObject stk_response=gson.fromJson(responseBody,JsonObject.class);
            Mpesa mpesa=new Mpesa();
            mpesa.setPhone(payment.getPhone());
            mpesa.setUsername(payment.getUsername());
            mpesa.setAmount(payment.getAmount());
            mpesa.setCheckoutId(stk_response.get("CheckoutRequestID").getAsString());
            mpesa.setMerchantId(stk_response.get("MerchantRequestID").getAsString());
            mpesaService.create(mpesa);

            map.put("success",true);
            map.put("message","successful");
            return (HashMap<String, Object>) map;
        } catch (IOException e) {
            map.put("success",false);
            map.put("message","error");

            throw new RuntimeException(e);

        }

    }
    public String getAccessToken(){
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://sandbox.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials")
                .method("GET", null)
                .addHeader("Authorization", "Basic cFJZcjZ6anEwaThMMXp6d1FETUxwWkIzeVBDa2hNc2M6UmYyMkJmWm9nMHFRR2xWOQ==")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string_body=response.body().string();
            Gson gson=new Gson();

           Map accessToken= gson.fromJson(string_body,Map.class);

           return  accessToken.get("access_token").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
//    @PostMapping("/response")
//    public String getResponse(@RequestBody String response ){
//       // System.out.println(response);
//        //process
//        Gson gson=new Gson();
//        //decoding the request body to json object
//        JsonObject jsonObject=gson.fromJson(response,JsonObject.class);
//        JsonObject subjects= jsonObject.get("subjects").getAsJsonObject();
//        JsonObject technical = subjects.get("technical").getAsJsonObject();
//        double math=technical.get("math").getAsDouble();
//        double descret=technical.get("descret").getAsDouble();
//        double sum=descret+math;
//        return  String.valueOf(sum);
//    }
//    @PostMapping("/array")
//    public   Map<String,Object> getResponseWithArray(@RequestBody String response ){
//        // System.out.println(response);
//        //process
//        Gson gson=new Gson();
//        //decoding the request body to json object
//        JsonObject jsonObject=gson.fromJson(response,JsonObject.class);
//        JsonArray marks=jsonObject.get("marks").getAsJsonArray();
//        JsonObject student=marks.get(0).getAsJsonObject();
//        String name=student.get("name").getAsString();
//        JsonObject score=student.get("score").getAsJsonObject();
//        double math=score.get("math").getAsDouble();
//        double eng=score.get("eng").getAsDouble();
//        double kis=score.get("kis").getAsDouble();
//        double sum=math+eng+kis;
//        Map<String,Object>  map=new HashMap<>();
//        map.put("success",true);
//        map.put("name",name);
//        map.put("total mark",sum);
//        return map;
//    }

    @PostMapping("/skdsowakdm/test")
    public void getMpesa(@RequestBody String response){
        Gson gson = new Gson();
        System.out.print(response);
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        JsonObject Body = jsonObject.get("Body").getAsJsonObject();
        JsonObject stkCallback = Body.get("stkCallback").getAsJsonObject();
        String MerchantRequestID = stkCallback.get("MerchantRequestID").getAsString();
        String CheckoutRequestID = stkCallback.get("CheckoutRequestID").getAsString();
        double ResultCode = stkCallback.get("ResultCode").getAsDouble();
        if(ResultCode==0){
            JsonObject CallbackMetadata = stkCallback.get("CallbackMetadata").getAsJsonObject();
            JsonArray Item = CallbackMetadata.get("Item").getAsJsonArray();
            JsonObject client = Item.get(0).getAsJsonObject();
            double amount = client.get("Value").getAsDouble();
            JsonObject client2 = Item.get(1).getAsJsonObject();
            String receipt = client2.get("Value").getAsString();
           Optional<Mpesa> mpesa_transaction= mpesaService.findByMerchantIdAndCheckoutId(MerchantRequestID,CheckoutRequestID);
           if(mpesa_transaction.isPresent()){
               Mpesa mpesa=mpesa_transaction.get();
               mpesa.setTransactionCode(receipt);
               OkHttpClient client_req = new OkHttpClient().newBuilder()
                       .build();
               MediaType mediaType = MediaType.parse("application/json");
               okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType, "{\r\n  " +
                       "  \"phone\":\""+mpesa.getPhone()+"\",\r\n  " +
                       "  \"message\":\"Thank you "+mpesa.getUsername()+", Confirmed we have received the payment of kSH "+
                       mpesa.getAmount()+" \"\r\n}");
               Request request = new Request.Builder()
                       .url("http://localhost:8080/message/sms")
                       .method("POST", body)
                       .addHeader("Content-Type", "application/json")
                       .build();
               try {
                   Response response_SMS = client_req.newCall(request).execute();
               } catch (IOException e) {
                   throw new RuntimeException(e);
               }
               mpesa.setStatus(1);
               mpesaService.update(mpesa);
           }
        }
        else{
            Optional<Mpesa> mpesa_transaction= mpesaService.findByMerchantIdAndCheckoutId(MerchantRequestID,CheckoutRequestID);
            if(mpesa_transaction.isPresent()) {
                Mpesa mpesa = mpesa_transaction.get();
                mpesa.setStatus(2);
                mpesaService.update(mpesa);
            }
        }



    }
}