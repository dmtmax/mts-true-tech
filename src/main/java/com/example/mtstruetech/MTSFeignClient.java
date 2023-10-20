package com.example.mtstruetech;

import com.example.mtstruetech.model.GetResource;
import com.example.mtstruetech.model.PostResource;
import com.example.mtstruetech.model.Price;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
@FeignClient(name = "client")
public interface MTSFeignClient {

    String QUERY_TOKEN = "token";
    String PRICE_PATH = "/price";
    String RESOURCE_PATH = "/resource";


    @GetMapping(PRICE_PATH)
    List<Price> getPrices();

    @GetMapping(value = RESOURCE_PATH, consumes = {"application/json"})
    List<GetResource> getResources(@RequestParam(QUERY_TOKEN) String token);

    @PostMapping(RESOURCE_PATH)
    void postResource(@RequestParam(QUERY_TOKEN) String token, PostResource resource);

    @DeleteMapping(RESOURCE_PATH + "/{id}")
    void deleteResource(@RequestParam(QUERY_TOKEN) String token, @PathVariable Integer id);

}