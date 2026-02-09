package com.ecommerce.project.service;

import ch.qos.logback.core.model.Model;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ApiResponse;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;



    @Override
    public ProductDTO saveProduct(Product product, Long categoryId) {
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    ()->new ResourceNotFoundException("Category","categoryId",categoryId));

            boolean isProductNameExists = false;

            List<Product> allProducts = category.getProducts();

            for (Product item : allProducts) {
                if (product.getProductName().equalsIgnoreCase(item.getProductName())) {
                    isProductNameExists = true;
                    break;
                }
            }


            if(!isProductNameExists) {
                product.setCategory(category);
                double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) * product.getPrice());
                product.setSpecialPrice(specialPrice);
                Product savedProduct = productRepository.save(product);
                return modelMapper.map(savedProduct, ProductDTO.class);
            }else {
                throw new APIException("Product with name "+product.getProductName()+" already exists in category "+category.getCategoryName());
            }

    }

    @Override
    public ProductResponse getAllProducts() {

        List<Product> products = productRepository.findAll();

        List<ProductDTO> productDTOS = products.stream().
                map(product -> modelMapper.map(product,ProductDTO.class)).
                toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setData(productDTOS);
        productResponse.setStatus(true);
        return productResponse;
    }

    @Override
    public ProductResponse getAllProductByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                ()->new ResourceNotFoundException("Category","categoryId",categoryId));
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        List<ProductDTO> productDTOS = products.stream().
                map(product -> modelMapper.map(product,ProductDTO.class)).
                toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setData(productDTOS);
        productResponse.setStatus(true);
        return productResponse;

    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%");

        List<ProductDTO> productDTOS = products.stream().
                map(product -> modelMapper.map(product,ProductDTO.class)).
                toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setData(productDTOS);
        productResponse.setStatus(true);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, Product product) {
        Product productdB = productRepository.findById(productId).
                orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));


        productdB.setProductName(product.getProductName());
        productdB.setProductDescription(product.getProductDescription());
        productdB.setDiscount(product.getDiscount());
        productdB.setPrice(product.getPrice());
        productdB.setQuantity(product.getQuantity());
        double specialPrice = product.getPrice() - ((product.getDiscount() * 0.01) *  product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(productdB);

        return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @Override
    public ProductResponse deleteProduct(Long productId) {
        Product productdB = productRepository.findById(productId).
                orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));

        productRepository.delete(productdB);
        ProductDTO productDTO =  modelMapper.map(productdB,ProductDTO.class);

        ProductResponse productResponse = new ProductResponse();
        productResponse.setData(List.of(productDTO));
        productResponse.setStatus(true);
        productResponse.setMessage("Product deleted successfully");
        return productResponse;

    }

    @Override
    public ProductResponse productImageUpload(Long productId, MultipartFile image) throws IOException {
        Product productdB = productRepository.findById(productId).
                orElseThrow(()->new ResourceNotFoundException("Product","productId",productId));


        String fileName = fileService.uploadImage(image);

        productdB.setImage(fileName);

        Product savedProduct = productRepository.save(productdB);

        ProductDTO productDTO =  modelMapper.map(productdB,ProductDTO.class);
        ProductResponse productResponse = new ProductResponse();
        productResponse.setData(List.of(productDTO));
        productResponse.setStatus(true);
        productResponse.setMessage("Product image uploaded successfully");
        return productResponse;
    }


}
