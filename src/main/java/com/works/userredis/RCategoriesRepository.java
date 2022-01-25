package com.works.userredis;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RCategoriesRepository extends JpaRepository<RCategories, String> {

    Optional<RCategories> findByCid(Integer cid);


    // void deleteByCid( Integer cid );

}
