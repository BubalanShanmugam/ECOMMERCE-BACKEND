package com.example.AmazonClone.Controller;

import com.example.AmazonClone.Model.Product;
import com.example.AmazonClone.Model.Userss;
import com.example.AmazonClone.Service.ProductService;
import com.example.AmazonClone.Service.UserPassService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//specific path
//@CrossOrigin(origins = "http://localhost:5173")

//general path
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService service;
    @Autowired
    private UserPassService userPassService;

    @GetMapping("/products")
    //ResponseEntity - By doing this we not only have data also respond entity. we have status
    public ResponseEntity<List<Product>> getAllProducts() {

        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);  //service get the product from DB
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
        Product product = service.getProductById(id);
        if(product != null)
            return new ResponseEntity<>(product, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/product")
//    ? - accept the response and exception(both)
//    @RequestPart - it will accept an part
    public ResponseEntity<?> addProduct(@RequestPart("product") String product, @RequestPart("imageFile")  MultipartFile imageFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Product produc = mapper.readValue(product, Product.class);
            System.out.println(product);
            Product product1 = service.addProduct(produc, imageFile);

            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
//    @PostMapping("/product")
//    public ResponseEntity<?> addPoduct(@RequestPart Product product,
//                                       @RequestPart MultipartFile imagefile){
//        try{
//            Product product1 = service.addProduct(product,imagefile);
//            return new ResponseEntity<>(product1, HttpStatus.OK);
//        }
//        catch (Exception e){
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {
        Product product = service.getProductById(productId);
        byte[] imageFile = product.getImageData();
        return ResponseEntity.ok().contentType(MediaType.valueOf(product.getImageType())).body(imageFile);
    }


    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id,
                                                @RequestPart("product") String productJson,
                                                @RequestPart("imageFile") MultipartFile imageFile) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Product product = mapper.readValue(productJson, Product.class); // ✅ manually convert
            Product updated = service.updateProduct(id, product, imageFile);

            if (updated != null) {
                return new ResponseEntity<>("Updated", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id){
        Product product=service.getProductById(id);
        if(product!=null){
            service.deleteMapping(id);
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Failed to update", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String key){
        List<Product> products = service.searchProducts(key);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/csrf-token")
    public ResponseEntity<?> getCsrfToken(HttpServletRequest request) {
        CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println("CSRF TOKEN: " + token); // Debug print

        if (token == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("CSRF token not found in request.");
        }

        return ResponseEntity.ok(token);
    }

    @PostMapping("/user")
    public void addstudent(@RequestBody Userss user){
        userPassService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Userss user){
        return userPassService.verify(user);
    }
}
