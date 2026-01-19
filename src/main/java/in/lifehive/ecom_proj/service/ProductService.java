package in.lifehive.ecom_proj.service;

import in.lifehive.ecom_proj.model.Product;
import in.lifehive.ecom_proj.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Product getProductById(Long prodId) {
        return repo.findById(prodId).orElse(new Product());
    }
}
