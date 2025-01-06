//package com.poly.controller;
//
//import org.springframework.web.bind.annotation.*;
//import org.springframework.ui.Model;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.http.*;
//import org.springframework.stereotype.Controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Controller
//@RequestMapping("/chat")
//public class GeminiChatController {
//
//    private final RestTemplate restTemplate;
//
//    public GeminiChatController(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//    
//    @GetMapping("/ask")
//    public String showChatForm() {
//        return "user/chat";  // Trả về trang chat.html
//    }
//    
//    @PostMapping("/ask")
//    public String askQuestion(@RequestParam("question") String question, Model model) {
//        // Gọi API Gemini AI để nhận câu trả lời
//        String response = callGeminiApi(question);
//
//        // Thêm câu trả lời vào model để hiển thị trên giao diện
//        model.addAttribute("response", response);
//
//        return "user/chat"; // Trả về trang chat.html
//    }
//
//    private String callGeminiApi(String question) {
//        String apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyBhVZhXnHaE4UpT6kTwKrU4r2jYvgHChQ4";  // Thay thế URL thật của Gemini AI
//        String apiKey = System.getenv("gemini.api.key");; // Thay thế bằng API Key của bạn
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + apiKey);
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> request = new HashMap<>();
//        request.put("question", question);
//
//        HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, entity, String.class);
//
//        return response.getBody(); // Trả về nội dung phản hồi từ Gemini AI
//    }
//}