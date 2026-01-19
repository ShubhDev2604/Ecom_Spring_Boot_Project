package in.lifehive.ecom_proj.controller;

import in.lifehive.ecom_proj.model.Product;
import in.lifehive.ecom_proj.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String hii(){
        return "Hello World!";
    }

    @GetMapping("/products")
    public List<Product> getAllProducts(){
        return service.getAllProducts();
    }

    @GetMapping("/product/{prodId}")
    public Product getProductById(@PathVariable Long prodId) {
        return service.getProductById(prodId);
    }
}
