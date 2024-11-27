package com.project.AuctionPlatform.controller;

import com.project.AuctionPlatform.dto.BidDTO;
import com.project.AuctionPlatform.model.Bid;
import com.project.AuctionPlatform.service.BidService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bids")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @GetMapping
    public List<Bid> getAllBids() {
        return null;
    }

    @PostMapping
    public BidDTO placeBid(@RequestBody BidDTO bidDTO) {
        return bidService.saveBid(bidDTO);
    }
}
