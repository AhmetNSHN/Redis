package com.works.userredis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;

@RedisHash("categories")
@Data
public class RCategories implements Serializable {

    @Id
    private String id;
    @Indexed
    private Integer cid;
    @Indexed
    private String title;

}

