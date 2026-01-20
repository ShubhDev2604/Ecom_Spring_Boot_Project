package in.lifehive.ecom_proj.controller;

import in.lifehive.ecom_proj.model.Product;
import in.lifehive.ecom_proj.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = service.getAllProducts();
        if(products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(products, HttpStatus.OK);
        }
    }

    // Will be using custom ApiResponse wrapper at end.
    @GetMapping("/product/{prodId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long prodId) {
        Product product = service.getProductById(prodId);
        if(product == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } else {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile) {
        System.out.println(product);
        try {
            Product product1 = service.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{prodId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable Long prodId){
        Product product = service.getProductById(prodId);
        byte[] imageFile = product.getImageData();
        if(
                imageFile == null
        ) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        } else {
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
        try {
            Product prod = service.updateProduct(prodId, product, imageFile);
            if(prod == null) {
                return new ResponseEntity("Something went Wrong...Failed to update", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity("Updated", HttpStatus.OK);
            }
        } catch(Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/product/{prodId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long prodId){
        Product product = service.getProductById(prodId);
        if(product == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Product Not Found");
        } else {
            try {
                service.deleteProductById(prodId);
                return ResponseEntity
                        .ok()
                        .body("Product Deleted!");
            } catch(Exception e) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(e.getMessage());
            }
        }
    }
}
