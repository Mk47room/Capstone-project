package com.repository;

import com.Mapper.ProductCountMapperFromRs;
import com.Mapper.ProductMapperFromRs;
import com.dto.ProductDto;
import com.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProductMapperFromRs productMapperFromRs;
    private final ProductCountMapperFromRs productCountMapperFromRs;

    public ProductRepository(JdbcTemplate jdbcTemplate, ProductMapperFromRs productMapperFromRs, ProductCountMapperFromRs productCountMapperFromRs) {
        this.jdbcTemplate = jdbcTemplate;
        this.productMapperFromRs = productMapperFromRs;
        this.productCountMapperFromRs = productCountMapperFromRs;
    }

    public void insertProduct(Product product) {
        String  categorySql = "insert into category values(?,?,?)";
        Object[] categoryValues = new Object[]{product.getCategory().getId(),
                                product.getCategory().getName(),
                                product.getCategory().getDescription()};
        jdbcTemplate.update(categorySql,categoryValues);

        String vendorSql = "insert into vendor values (?,?,?)";
        Object[] vendorValues = new Object[]{product.getVendor().getId(),
                                product.getVendor().getName(),
                                product.getVendor().getEmail()};
        jdbcTemplate.update(vendorSql,vendorValues);


        String sql = "insert into product values (?,?,?,?,?,?)";
        Object[] values = new Object[]{product.getId(),product.getName(),product.getPrice(),product.getStockQuantity(),product.getCategory().getId(),product.getVendor().getId()};
        jdbcTemplate.update(sql,values);
    }

    public List<ProductDto> getProductById(int i) {
        String sql = """
                select p.id,p.name,p.price,p.stock_quantity,c.name as category_name,v.name as vendor_name
                from product p
                JOIN category c ON c.id = p.category_id
                JOIN vendor v ON v.id = p.vendor_id
                where p.id = ?
                """;
         return jdbcTemplate.query(sql, productMapperFromRs, i);
    }


    public void updateStockById(int id, int stock) {

        String sql = "update product set stock_quantity = ? where id = ?";
        Object[] stockObject = new Object[]{stock,id};
        jdbcTemplate.update(sql,stockObject);

    }

    public List<Map<String, Integer>> countProductsByVendor() {
        String sql = """
                        SELECT v.name AS vendor_name, COUNT(p.id) AS product_count
                        FROM vendor v
                        LEFT JOIN product p ON v.id = p.vendor_id
                        GROUP BY v.id, v.name
                        """;
        return jdbcTemplate.query(sql,productCountMapperFromRs);

    }
}
