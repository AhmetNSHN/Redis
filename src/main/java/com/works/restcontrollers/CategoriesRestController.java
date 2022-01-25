package com.works.restcontrollers;


import com.works.entities.Categories;
import com.works.enums.REnum;
import com.works.repositories.CategoriesRepository;
import com.works.userredis.RCategories;
import com.works.userredis.RCategoriesRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/categories")
public class CategoriesRestController {

    final CategoriesRepository cRepo;
    final RCategoriesRepository rRepo;
    public CategoriesRestController(CategoriesRepository cRepo, RCategoriesRepository rRepo) {
        this.cRepo = cRepo;
        this.rRepo = rRepo;
    }

    // insert
    @PostMapping("/add")
    public Map<REnum, Object> add(@RequestBody Categories categories ) {
        Map<REnum, Object> hm = new LinkedHashMap<>();

        Categories c = cRepo.save(categories);
        hm.put( REnum.status, true);
        hm.put( REnum.result, c );

        // Redis Insert
        RCategories r = new RCategories();
        r.setCid(c.getCid());
        r.setTitle(c.getTitle());
        r.setId(UUID.randomUUID().toString());
        rRepo.save(r);

        return hm;
    }


    @GetMapping("/list")
    public Map<REnum, Object> list() {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.status, true);
        hm.put(REnum.result, rRepo.findAll() );
        return hm;
    }

    @DeleteMapping("delete/{cid}")
    public Map<REnum, Object> delete( @PathVariable String cid ) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            int cCid = Integer.parseInt(cid);

            // db item delete
            cRepo.deleteById(cCid);

            // redis item delete
            Optional<RCategories> oct = rRepo.findByCid(cCid);
            if (oct.isPresent() ) {
                RCategories rc = oct.get();
                rRepo.deleteById(rc.getId());
                hm.put(REnum.status, true);
                hm.put(REnum.result, rc);
            }else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "Delete Redis Fail :" + cid);
            }

        }catch (Exception ex) {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "Delete Action Fail :" + cid);
        }
        return hm;
    }


    // Database and Redis item Update
    @PutMapping("/update")
    public Map<REnum, Object> update(  @RequestBody Categories categories ) {
        Map<REnum, Object> hm = new LinkedHashMap<>();

        // db update
        Optional<Categories> optC = cRepo.findById( categories.getCid() );
        if (optC.isPresent() ) {
            Categories c = optC.get();
            c.setTitle(categories.getTitle());
            cRepo.saveAndFlush(c);

            // Redis item update
            Optional<RCategories> optR = rRepo.findByCid(categories.getCid());
            if ( optR.isPresent() ) {
                RCategories rc = optR.get();
                rc.setTitle( categories.getTitle() );

                // delete item
                rRepo.deleteById( rc.getId() );
                rRepo.save( rc );

                hm.put(REnum.status, false);
                hm.put(REnum.message, "Update Redis Action Success");
                hm.put(REnum.result, rc);

            }else {
                hm.put(REnum.status, false);
                hm.put(REnum.message, "Update Redis Action Fail");
            }

        }else {
            hm.put(REnum.status, false);
            hm.put(REnum.message, "Update DB Action Fail");
        }
        return hm;
    }
}