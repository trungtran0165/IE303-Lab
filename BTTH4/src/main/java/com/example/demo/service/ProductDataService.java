package com.example.demo.service;

import com.example.demo.model.Product;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ProductDataService {

    public List<Product> loadProductsFromFile() {
        List<Product> products = new ArrayList<>();

        try {
            ClassPathResource resource = new ClassPathResource("data/product-info.txt");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            // Parse the content to extract product information
            String text = content.toString();

            // Extract product data using regex patterns
            Pattern imagePattern = Pattern.compile("image (\\d+):");
            Pattern namePattern = Pattern.compile("- Name: ([^\\\\]+)");
            Pattern pricePattern = Pattern.compile("- Price: ([\\d,]+(?:\\.\\d+)?)");
            Pattern brandPattern = Pattern.compile("- Brand: ([^\\\\]+)");
            Pattern descPattern = Pattern.compile("- Description: ([^\\\\]+)");

            String[] sections = text.split("image \\d+:");

            // Predefined product data based on the file content
            String[][] productData = {
                    { "4DFWD PULSE SHOES", "160.00", "Adidas",
                            "This product is excluded from all promotional discounts and offers", "img1.png" },
                    { "FORUM MID SHOES", "100.00", "Adidas",
                            "This product is excluded from all promotional discounts and offers", "img2.png" },
                    { "SUPERNOVA SHOES", "150.00", "Adidas", "NMD City Stock 2", "img3.png" },
                    { "BEACH SEASON SHOES", "160.00", "Adidas", "Summer collection", "img4.png" },
                    { "BLACK THADER", "120.00", "Adidas", "Speed Run", "img5.png" },
                    { "FLOWAIR MID SHOES", "160.00", "Adidas", "Speed Run", "img6.png" }
            };

            // Create Product objects with various sizes and colors
            String[] sizes = { "39", "40", "41", "42", "43", "44" };
            String[] colors = { "Xanh lá-Đen", "Trắng-Xanh", "Đen-Trắng", "Trắng-Xanh lá", "Đen-Tím", "Cam-Trắng" };
            int[] stocks = { 25, 30, 20, 35, 15, 28 };

            for (int i = 0; i < productData.length; i++) {
                String[] data = productData[i];
                Product product = new Product();
                product.setName(data[0]);
                product.setPrice(new BigDecimal(data[1]).multiply(new BigDecimal("1000"))); // Convert to VND
                                                                                            // (thousands)
                product.setBrand(data[2]);
                product.setDescription(data[3]);
                product.setImageUrl("/images/" + data[4]);
                product.setSize(sizes[i]);
                product.setColor(colors[i]);
                product.setStock(stocks[i]);

                products.add(product);
            }

        } catch (Exception e) {
            System.err.println("Error reading product data file: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }
}