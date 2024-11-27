package com.project.AuctionPlatform.service;


import com.project.AuctionPlatform.dto.BidDTO;
import com.project.AuctionPlatform.handlers.SocketConnectionHandler;
import com.project.AuctionPlatform.model.Bid;
import com.project.AuctionPlatform.model.Product;
import com.project.AuctionPlatform.model.User;
import com.project.AuctionPlatform.repository.BidRepository;
import com.project.AuctionPlatform.repository.ProductRepository;
import com.project.AuctionPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SocketConnectionHandler socketConnectionHandler;



    @Autowired
    public BidService(BidRepository bidRepository,
                      ProductRepository productRepository,
                      UserRepository userRepository, SocketConnectionHandler socketConnectionHandler) {
        this.bidRepository = bidRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.socketConnectionHandler = socketConnectionHandler;
    }

    public BidDTO saveBid(BidDTO bidDTO){

        Product product = productRepository.findByName(bidDTO.getProduct())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        User user = userRepository.findByUsername(bidDTO.getName())
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setUsername(bidDTO.getName());
                    // Set any other default values for the new user as needed
                    return userRepository.save(newUser);
                });

        Bid bid = new Bid();
        bid.setProduct(product);
        bid.setUser(user);
        bid.setAmount(bidDTO.getAmount());
        bidRepository.save(bid);


        product.setCurrentHighestBid(bidDTO.getAmount());
        product.setHighestBidder(user);

        productRepository.save(product);

        String broadcastMessage = String.format(
                "New Bid: Product: %s, User: %s, Amount: %.2f",
                product.getName(),
                user.getUsername(),
                bidDTO.getAmount()
        );
        System.out.println("Current WebSocket sessions size: " + socketConnectionHandler.productSessionsMap.get(product.getName()));
        socketConnectionHandler.productSessionsMap.get(product.getName()).forEach(session -> {
            try {
                System.out.println("Attempting to send message to session: " + session.getId());
                session.sendMessage(new TextMessage(broadcastMessage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        System.out.println("Broadcasting bid update: " + broadcastMessage);

        return bidDTO;
    }
}