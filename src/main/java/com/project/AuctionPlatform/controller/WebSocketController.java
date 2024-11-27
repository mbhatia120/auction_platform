package com.project.AuctionPlatform.controller;

import com.project.AuctionPlatform.dto.BidDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketController {

    @GetMapping("/web-bids")
    public String index() {
        return "client";
    }
}