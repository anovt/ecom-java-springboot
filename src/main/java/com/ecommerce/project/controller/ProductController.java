package com.ecommerce.project.controller;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ApiResponse;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import com.ecommerce.project.service.ProductServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> post(@Valid @RequestBody Product product, @PathVariable Long categoryId) {

        ProductDTO productDTO = productService.saveProduct(product,categoryId);

        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @GetMapping("/public/products")
    public  ResponseEntity<ProductResponse> listProduct(){

     ProductResponse productResponse = productService.getAllProducts();

     return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/categories/{categoryId}/product")
    public ResponseEntity<ProductResponse> listProductByCategory(@Valid @PathVariable Long categoryId){

        ProductResponse productResponse = productService.getAllProductByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @GetMapping("/public/products/keywords/{keyword}")
    public  ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword){

        ProductResponse productResponse = productService.getProductsByKeyword(keyword);

        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @PutMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody Product product, @PathVariable Long productId){

        ProductDTO updatedProduct = productService.updateProduct(productId,product);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }

    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long productId){

        ProductResponse deletedProduct = productService.deleteProduct(productId);
        return new ResponseEntity<>(deletedProduct, HttpStatus.OK);

    }

    @PutMapping("/admin/products/{productId}/image")
    public ResponseEntity<ProductResponse> productImageUpload(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException {

        ProductResponse updatedProduct = productService.productImageUpload(productId,image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);

    }

}
