package com.example.demo.config;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDataService productDataService;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra nếu database đã có dữ liệu thì không thêm nữa
        if (productRepository.count() == 0) {
            // Load dữ liệu từ file product-info.txt
            List<Product> products = productDataService.loadProductsFromFile();

            if (!products.isEmpty()) {
                // Lưu tất cả sản phẩm vào database
                for (Product product : products) {
                    productRepository.save(product);
                }

                System.out.println("Đã khởi tạo " + products.size() + " sản phẩm từ file dữ liệu vào cơ sở dữ liệu!");

                // In ra thông tin các sản phẩm đã load
                products.forEach(product -> {
                    System.out.println("- " + product.getName() + " (" + product.getBrand() + ") - " +
                            product.getPrice() + " VND - Size: " + product.getSize() +
                            " - Color: " + product.getColor());
                });
            } else {
                System.out.println("Không thể load dữ liệu từ file, sử dụng dữ liệu mặc định!");
                loadDefaultData();
            }
        }
    }

    private void loadDefaultData() {
        // Fallback data nếu không đọc được file
        Product defaultProduct = new Product(
                "Default Shoe", "Unknown",
                new java.math.BigDecimal("1000000"),
                "42", "Đen",
                "Sản phẩm mặc định",
                "/images/default.jpg", 10);
        productRepository.save(defaultProduct);
    }
}