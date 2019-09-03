package com.lj.scmusteritemdemo.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.lj.scmusteritemdemo.common.DBTypeEnum;
import com.lj.scmusteritemdemo.common.DynamicDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 *  持久化配置
 */
@Configuration
@MapperScan({"com.lj.scmusteritemdemo.mapper.*"})
public class MybatisPlusConfig {

    /**
     * mapper-plus分页插件<br>
     * 文档：http://mp.baomidou.com<br>
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        //paginationInterceptor.setLocalPage(true);// 开启 PageHelper 的支持
        return paginationInterceptor;
    }

    /**
     * mapper-plus SQL执行效率插件【生产环境可以关闭】
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    @Bean(name = "cs1db")
    @ConfigurationProperties(prefix = "spring.datasource.druid.cs1-db" )
    public DataSource cs1db () {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "cs2db")
    @ConfigurationProperties(prefix = "spring.datasource.druid.cs2-db" )
    public DataSource cs2db () {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 动态数据源配置
     * @return
     */
    @Bean
    @Primary
    public DataSource multipleDataSource (@Qualifier("cs1db") DataSource cs1db,
                                          @Qualifier("cs2db") DataSource cs2db) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map< Object, Object > targetDataSources = new HashMap<>();
        targetDataSources.put(DBTypeEnum.cs1db.getValue(), cs1db );
        targetDataSources.put(DBTypeEnum.cs2db.getValue(), cs2db);

        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(cs1db);//指定默认
        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(multipleDataSource(cs1db(),cs2db()));

        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*/*Mapper.xml"));

        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.setCacheEnabled(false);
        sqlSessionFactory.setConfiguration(configuration);
        sqlSessionFactory.setPlugins(new Interceptor[]{
                paginationInterceptor()
        });
        return sqlSessionFactory.getObject();
    }


}
