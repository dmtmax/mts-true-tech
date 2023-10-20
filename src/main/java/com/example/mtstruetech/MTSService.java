package com.example.mtstruetech;

import com.example.mtstruetech.model.PostResource;
import com.example.mtstruetech.model.Price;
import com.example.mtstruetech.model.ResourceType;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Slf4j
public class MTSService {

    private final MTSFeignClient mtsClient;

    @Value("${application.token}")
    private String token;

    private Price minDB;
    private Price minVM;



    @PostConstruct
    private void init() {
        mtsClient.getResources(token).forEach(
                res -> mtsClient.deleteResource(token, res.id())
        );
        mtsClient.getPrices().forEach(System.out::println);
        try {
            minDB = mtsClient.getPrices()
                    .stream()
                    .filter(res -> res.type().equals(ResourceType.DB))
                    .min(Comparator.comparingInt(Price::cost))
                    .orElseThrow();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            minVM = mtsClient.getPrices()
                    .stream()
                    .filter(res -> res.type().equals(ResourceType.VM))
                    .min(Comparator.comparingInt(Price::cost))
                    .orElseThrow();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        log.info("Initialization complete.");
        log.info("minDB: " + minDB);
        log.info("minVM: " + minVM);
    }


    public void addMin(ResourceType type) {
        switch (type) {
            case VM -> mtsClient.postResource(token, new PostResource(minVM.cpu(), minVM.ram(), ResourceType.VM));
            case DB -> mtsClient.postResource(token, new PostResource(minVM.cpu(), minVM.ram(), ResourceType.DB));
        }

    }

    public void deleteMin(ResourceType type) {
        mtsClient.getResources(token)
                .stream()
                .filter(res -> res.type().equals(type))
                .findFirst()
                .ifPresent(res -> mtsClient.deleteResource(token,res.id()));
    }
}
