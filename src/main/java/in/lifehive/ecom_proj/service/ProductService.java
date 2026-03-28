package in.lifehive.ecom_proj.service;

import in.lifehive.ecom_proj.model.Product;
import in.lifehive.ecom_proj.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    public List<Product> getAllProducts() {
        logger.debug("Fetching all products from repository");
        return repo.findAll();
    }

    public Product getProductById(Long prodId) {
        logger.debug("Fetching product with id={} from repository", prodId);
        return repo.findById(prodId).orElse(null);
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
        logger.debug("Preparing product name={} for save", product.getName());
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        Product savedProduct = repo.save(product);
        logger.debug("Saved product with id={}", savedProduct.getId());
        return savedProduct;
    }

    public Product updateProduct(Long prodId, Product product, MultipartFile imageFile) throws IOException {
        logger.debug("Preparing product id={} for update", prodId);
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());
        Product prod1 = repo.findById(prodId).orElse(null);
        if(prod1 == null) {
            logger.debug("Product id={} not found during update", prodId);
            return null;
        } else {
            product.setId(prodId);
            Product updatedProduct = repo.save(product);
            logger.debug("Updated product id={}", updatedProduct.getId());
            return updatedProduct;
        }
    }

    public void deleteProductById(Long prodId) {
        logger.debug("Deleting product id={} from repository", prodId);
        repo.deleteById(prodId);
    }

    public List<Product> searchProducts(String keyword) {
        logger.debug("Searching products in repository with keyword={}", keyword);
        return repo.searchProducts(keyword);
    }
}
