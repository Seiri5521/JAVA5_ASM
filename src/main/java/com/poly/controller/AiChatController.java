package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poly.services.AiChatService;

@Controller
public class AiChatController {
	private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    @GetMapping("/chat")
    public String chatWithAI(@RequestParam(required = false) String question, Model model) {
        if (question != null && !question.isEmpty()) {
            String response = aiChatService.askQuestion(question);
            model.addAttribute("response", response);
        }
        return "/user/chatAI";
    }

    @GetMapping("/chat-ui")
    public String showChatPage() {
        return "/user/chatAI";
    }
}
