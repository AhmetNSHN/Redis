package com.works.restcontrollers;

import com.works.entities.Note;
import com.works.enums.REnum;
import com.works.repositories.NoteRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/note")
public class NoteRestController {

    final NoteRepository nRepo;
    final CacheManager cacheManager;
    public NoteRestController(NoteRepository nRepo, CacheManager cacheManager) {
        this.nRepo = nRepo;
        this.cacheManager = cacheManager;
    }


    // add
    @PostMapping("/add")
    public Map<REnum, Object> add(@RequestBody Note note) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.status, true);
        hm.put(REnum.result, nRepo.save(note));
        cacheManager.getCache("noteList").clear();
        return hm;
    }


    @GetMapping("/list")
    @Cacheable("noteList")
    public Map<REnum, Object> list() {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.status, true);
        hm.put(REnum.result, nRepo.findAll());
        return hm;
    }
}