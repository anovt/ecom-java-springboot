package com.ecommerce.project.service;

import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ApiResponse;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    public ProductDTO saveProduct(Product product,Long categoryId);

    ProductResponse getAllProducts();

    ProductResponse getAllProductByCategory(@Valid Long categoryId);

    ProductResponse getProductsByKeyword(String keyword);

    ProductDTO updateProduct(Long productId, Product product);

    ProductResponse deleteProduct(Long productId);

    ProductResponse productImageUpload(Long productId, MultipartFile image) throws IOException;
}
