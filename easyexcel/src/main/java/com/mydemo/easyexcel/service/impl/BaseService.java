package com.mydemo.easyexcel.service.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * @author kun.han on 2020/6/28 15:38
 */
@Configuration
public class BaseService {

    @Resource
    @PersistenceContext
    protected EntityManager entityManager;

    protected JPAQueryFactory queryFactory;

    @PostConstruct
    private void init(){
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
}
