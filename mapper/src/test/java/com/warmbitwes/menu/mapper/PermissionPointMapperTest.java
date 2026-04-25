package com.warmbitwes.menu.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.warmbitwes.menu.entity.PermissionPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@MybatisTest(properties = "mybatis.mapper-locations=classpath*:mapper/*.xml")
class PermissionPointMapperTest {
    @Autowired
    private PermissionPointMapper permissionPointMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void initSchema() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS permission_point");
        jdbcTemplate.execute("""
                CREATE TABLE permission_point (
                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                  code VARCHAR(64) NOT NULL UNIQUE,
                  name VARCHAR(128) NOT NULL,
                  remark VARCHAR(512) DEFAULT NULL,
                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP()
                )
                """);
    }

    @Test
    void should_insert_and_query_permission_code() {
        PermissionPoint point = new PermissionPoint();
        point.setCode("dish:create");
        point.setName("新增菜品");
        permissionPointMapper.insert(point);

        PermissionPoint loaded = permissionPointMapper.selectByCode("dish:create");
        assertEquals("新增菜品", loaded.getName());
    }

    @SpringBootApplication
    @EnableAutoConfiguration
    @MapperScan("com.warmbitwes.menu.mapper")
    static class TestApp {
    }
}
