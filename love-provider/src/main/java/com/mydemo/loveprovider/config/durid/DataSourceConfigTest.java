// package com.mydemo.loveprovider.config.durid;
//
// import org.apache.ibatis.session.SqlSessionFactory;
// import org.mybatis.spring.SqlSessionFactoryBean;
// import org.mybatis.spring.SqlSessionTemplate;
// import org.mybatis.spring.annotation.MapperScan;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.boot.jdbc.DataSourceBuilder;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
// import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
// import javax.sql.DataSource;
//
//
// /**
//  * @author kun.han
//  */
// @Configuration
// @MapperScan(basePackages = "com.test.demo.mapper.test", sqlSessionTemplateRef  = "testSqlSessionTemplate")
// public class DataSourceConfigTest {
//
//     @Bean(name = "testConfiguration")
//     @ConfigurationProperties(prefix = "mybatis.configuration.test")
//     public org.apache.ibatis.session.Configuration configuration(){
//         return new org.apache.ibatis.session.Configuration();
//     }
//
//     @Bean(name = "testDataSource")
//     @ConfigurationProperties(prefix = "spring.datasource.test")
//     public DataSource testDataSource() {
//         return DataSourceBuilder.create().build();
//     }
//
//     @Bean(name = "testSqlSessionFactory")
//     public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource,@Qualifier("testConfiguration") org.apache.ibatis.session.Configuration configuration) throws Exception {
//         SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//         bean.setDataSource(dataSource);
//         bean.setConfiguration(configuration);
//         bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/test/*.xml"));
//         return bean.getObject();
//     }
//
//     @Bean(name = "testTransactionManager")
//     public DataSourceTransactionManager testTransactionManager(@Qualifier("testDataSource") DataSource dataSource) {
//         return new DataSourceTransactionManager(dataSource);
//     }
//
//     @Bean(name = "testSqlSessionTemplate")
//     public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//         return new SqlSessionTemplate(sqlSessionFactory);
//     }
// }