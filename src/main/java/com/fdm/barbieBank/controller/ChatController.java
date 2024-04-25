package com.fdm.barbieBank.controller;

import com.fdm.barbieBank.utils.ChatGPTRequest;
import com.fdm.barbieBank.utils.ChatGPTResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class ChatController {

    @Value("${openai.model}")
    private String openAIModel;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @PostMapping("/chat")
    public ChatGPTResponse chat(@RequestParam("message") String message) {
        String prompt = message;
        ChatGPTRequest request = new ChatGPTRequest(openAIModel, prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChatGPTRequest> entity = new HttpEntity<>(request, headers);

        ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, entity, ChatGPTResponse.class);
        return chatGPTResponse;
    }
}

