package in.lifehive.ecom_proj.controller;

import in.lifehive.ecom_proj.model.Product;
import in.lifehive.ecom_proj.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        logger.info("Received request to fetch all products");
        List<Product> products = service.getAllProducts();
        if(products.isEmpty()) {
            logger.warn("No products found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.info("Returning {} products", products.size());
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
    }

    // Will be using custom ApiResponse wrapper at end.
    @GetMapping("/product/{prodId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long prodId) {
        logger.info("Received request to fetch product with id={}", prodId);
        Product product = service.getProductById(prodId);
        if(product == null) {
            logger.warn("Product with id={} was not found", prodId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } else {
            logger.info("Returning product with id={}", prodId);
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile) {
        logger.info("Received request to add product name={} with image={}", product.getName(), imageFile.getOriginalFilename());
        try {
            Product product1 = service.addProduct(product, imageFile);
            logger.info("Created product with id={}", product1.getId());
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        } catch(Exception e){
            logger.error("Failed to create product name={}", product.getName(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Long prodId){
        logger.info("Received request to fetch image for product id={}", prodId);
        Product product = service.getProductById(prodId);
        if (product == null) {
            logger.warn("Cannot fetch image because product id={} was not found", prodId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        byte[] imageFile = product.getImageData();
        if(
                imageFile == null
        ) {
            logger.warn("No image found for product id={}", prodId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } else {
            logger.info("Returning image for product id={} with {} bytes", prodId, imageFile.length);
            return ResponseEntity
                    .ok()
                    .body(imageFile);
        }
    }

    @PutMapping("/product/{prodId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long prodId,
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile
    ) {
        logger.info("Received request to update product id={}", prodId);
        try {
            Product prod = service.updateProduct(prodId, product, imageFile);
            if(prod == null) {
                logger.warn("Failed to update product id={} because it does not exist", prodId);
                return new ResponseEntity("Something went Wrong...Failed to update", HttpStatus.BAD_REQUEST);
            } else {
                logger.info("Updated product id={}", prodId);
                return new ResponseEntity("Updated", HttpStatus.OK);
            }
        } catch(Exception e) {
            logger.error("Failed to update product id={}", prodId, e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long prodId){
        logger.info("Received request to delete product id={}", prodId);
        Product product = service.getProductById(prodId);
        if(product == null) {
            logger.warn("Cannot delete product id={} because it does not exist", prodId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Product Not Found");
        } else {
            try {
                service.deleteProductById(prodId);
                logger.info("Deleted product id={}", prodId);
                return ResponseEntity
                        .ok()
                        .body("Product Deleted!");
            } catch(Exception e) {
                logger.error("Failed to delete product id={}", prodId, e);
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(e.getMessage());
            }
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword) {
        logger.info("Received request to search products with keyword={}", keyword);
        List<Product> products = service.searchProducts(keyword);
        logger.info("Search for keyword={} returned {} products", keyword, products.size());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
