package com.main;

import com.config.Appconfig;
import com.dto.ProductDto;
import com.model.Category;
import com.model.Product;
import com.model.Vendor;
import com.service.ProductService;
import com.service.VendorService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.security.spec.ECField;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Appconfig.class);
        VendorService vendorService = context.getBean(VendorService.class);
        ProductService productService = context.getBean(ProductService.class);

        Scanner sc = new Scanner(System.in);

        while(true) {
            System.out.println("---------Product Management---------");
            System.out.println("1. Insert Product");
            System.out.println("2. Find Product by ID ");
            System.out.println("3. Update stock Quantity ");
            System.out.println("4. Show products by vendor");
            System.out.println("0. Exit");
            System.out.println("------------------------------------");
            int input = sc.nextInt();
            if (input == 0) {
                System.out.println("Exiting...");
                break;
            }
            switch (input) {
                case 1 -> {

                    sc.nextLine();

                    Product product = new Product();
                    Category category = new Category();
                    Vendor vendor = new Vendor();

                    System.out.println("Enter the name");
                    product.setName(sc.nextLine());
                    System.out.println("Enter the price");
                    product.setPrice(sc.nextDouble());
                    System.out.println("Enter the quantity");
                    product.setStockQuantity(sc.nextInt());
                    sc.nextLine();
                    System.out.println("Enter the category name");
                    category.setName(sc.nextLine());
                    System.out.println("Enter the description of category");
                    category.setDescription(sc.nextLine());
                    System.out.println("------Enter the vendor details-----");
                    System.out.println("Enter vendor name");
                    vendor.setName(sc.nextLine());
                    System.out.println("Enter vendor email");
                    vendor.setEmail(sc.nextLine());

                    product.setCategory(category);
                    product.setVendor(vendor);

                    try {
                        productService.insertProduct(product);
                        System.out.println("Product inserted successfully.");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }
                case 2 ->{
                    System.out.println("Enter the product Id :");
                     try{
                         ProductDto prductDto = productService.getProductById(sc.nextInt());
                         System.out.println("Product name : " + prductDto.name());
                         System.out.println("Product price : "+ prductDto.price());
                         System.out.println("Product stock Quantity: " + prductDto.stockQuantity());
                         System.out.println("Product Category name: " + prductDto.categoryName());
                         System.out.println("Product vendor name: " + prductDto.vendorName());

                     }catch (Exception e){
                         System.out.println(e.getMessage());
                     }

                    break;
                }
                case 3 ->{
                    System.out.println("Enter the product id to update:");
                    try{
                        int id = sc.nextInt();
                        ProductDto prductDto = productService.getProductById(id);
                        System.out.println("Enter new stock: ");
                        int stock = sc.nextInt();
                        productService.updateStockById(id,stock);
                        System.out.println("Product Updated....");
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }

                    break;
                }
                case 4 ->{
                    // count products by vendor.(no input)
                    List<Map<String, Integer>> vendorCounts = productService.countProductsByVendor();
                    System.out.println("Product Counts by Vendor:");
                    vendorCounts.stream()
                            .flatMap(map -> map.entrySet().stream())
                            .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
                }

                default -> {
                    System.out.println("Invalid option. Returning...");
                    return;
                }
                }
            }

        sc.close();
        }

    }

