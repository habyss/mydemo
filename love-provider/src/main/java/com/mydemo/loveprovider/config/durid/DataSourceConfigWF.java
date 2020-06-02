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
// import org.springframework.context.annotation.Primary;
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
// @MapperScan(basePackages = "com.test.demo.mapper.wf", sqlSessionTemplateRef  = "wfSqlSessionTemplate")
// public class DataSourceConfigWF {
//
//     @Bean(name = "wfConfiguration")
//     @ConfigurationProperties(prefix = "mybatis.configuration.wf")
//     @Primary
//     public org.apache.ibatis.session.Configuration configuration(){
//         return new org.apache.ibatis.session.Configuration();
//     }
//
//     @Bean(name = "wfDataSource")
//     @ConfigurationProperties(prefix = "spring.datasource.wf")
//     @Primary
//     public DataSource testDataSource() {
//         return DataSourceBuilder.create().build();
//     }
//
//     @Bean(name = "wfSqlSessionFactory")
//     @Primary
//     public SqlSessionFactory testSqlSessionFactory(@Qualifier("wfDataSource") DataSource dataSource,@Qualifier("wfConfiguration") org.apache.ibatis.session.Configuration configuration) throws Exception {
//         SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
//         bean.setDataSource(dataSource);
//         bean.setConfiguration(configuration);
//         bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/wf/*.xml"));
//         return bean.getObject();
//     }
//
//     @Bean(name = "wfTransactionManager")
//     @Primary
//     public DataSourceTransactionManager testTransactionManager(@Qualifier("wfDataSource") DataSource dataSource) {
//         return new DataSourceTransactionManager(dataSource);
//     }
//
//     @Bean(name = "wfSqlSessionTemplate")
//     @Primary
//     public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("wfSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
//         return new SqlSessionTemplate(sqlSessionFactory);
//     }
// }
