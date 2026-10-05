package com.Mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ProductCountMapperFromRs implements RowMapper<Map<String,Integer>> {

    @Nullable
    @Override
    public Map<String, Integer> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Integer> vendorProductCountMap = new HashMap<>();
        while (rs.next()) {
            vendorProductCountMap.put(
                    rs.getString("vendor_name"),
                    rs.getInt("product_count")
            );
        }
        return vendorProductCountMap;
    }
}
