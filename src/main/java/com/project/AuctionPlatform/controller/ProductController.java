package com.project.AuctionPlatform.controller;
import com.project.AuctionPlatform.model.Product;
import com.project.AuctionPlatform.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{name}")
    public Product getProductByName(@PathVariable String name) {
        return productService.getProductByName(name);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        if (product.getCurrentHighestBid() == null) {
            System.out.println("world");
            product.setCurrentHighestBid(product.getStartingPrice());
        }
        return productService.saveProduct(product);
    }
}
