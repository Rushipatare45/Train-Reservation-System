package com.example.Reservation_System.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "http://localhost:3000") // allow React
public class PaymentController {

  private static final String KEY = "rzp_test_RGJCrSCstDxkaH";
  private static final String SECRET = "P6xK6SylUWwzOdyCfYbTr3Jw";

    @PostMapping("/createOrder")
    public String createOrder(@RequestParam int amount) {
        try {
            RazorpayClient client = new RazorpayClient(KEY, SECRET);

            JSONObject options = new JSONObject();
            options.put("amount", amount * 100); // amount in paise
            options.put("currency", "INR");
            options.put("payment_capture", 1); // auto capture

            Order order = client.orders.create(options);

            return order.toString(); // send order JSON to frontend
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }
}
