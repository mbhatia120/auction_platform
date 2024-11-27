package com.project.AuctionPlatform.dto;


import java.math.BigDecimal;

/**
 * Data Transfer Object for Bid entity.
 */
public class BidDTO {

    private String name;
    private String product;
    private BigDecimal amount;


    public BidDTO() {}

    public BidDTO(String name, String product, BigDecimal amount) {
        this.name = name;
        this.product = product;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}