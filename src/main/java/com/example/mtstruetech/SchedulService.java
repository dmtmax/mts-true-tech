package com.example.mtstruetech;

import com.example.mtstruetech.config.ApplicationProperties;
import com.example.mtstruetech.model.GetResource;
import com.example.mtstruetech.model.ResourceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulService {

    private final MTSFeignClient mtsClient;
    private final MTSService mtsService;

    private final ApplicationProperties appProps;
    @Value("${application.token}")
    public String token;


    @Scheduled(fixedRateString = "${application.schedule-rate}", timeUnit = TimeUnit.SECONDS, initialDelay = 0)
    public void task() {
        log.info("calculating");
        calculate();
    }

    public void calculate() {
        List<GetResource> currentResources = mtsClient.getResources(token);
        if (currentResources.isEmpty()) {
            setBasicResources();
        } else {
            updateResources(currentResources);
        }
    }

    public void updateResources(List<GetResource> currentResources) {
        Map<ResourceType, List<GetResource>> resources = currentResources
                .stream()
                .collect(Collectors.groupingBy(GetResource::type));

        applyBasicManagement(ResourceType.VM, resources);
        applyBasicManagement(ResourceType.DB, resources);
    }

    public void applyBasicManagement(ResourceType type, Map<ResourceType, List<GetResource>> resources) {
        List<GetResource> typedResources = resources.get(type);
        double cpuLoad = typedResources
                .stream()
                .mapToDouble(GetResource::cpuLoad)
                .sum();

        double ramLoad = typedResources
                .stream()
                .mapToDouble(GetResource::ramLoad)
                .sum();
        int podCount = typedResources.size();
        log.info("type = [{}], pod count = [{}], cpu load = [{}], ram load = [{}]",
                type, podCount, cpuLoad, ramLoad);
        if (cpuLoad / podCount > appProps.getCpuLoadMax() ||
                ramLoad / podCount > appProps.getMemoryLoadMax()) {
            log.info("add [{}]", type);
            mtsService.addMin(type);
        }
        if (podCount > 1 &&
                (cpuLoad / (podCount - 1) < appProps.getMemoryLoadMax() - appProps.getDelta() &&
                        ramLoad / (podCount - 1) < appProps.getMemoryLoadMax() - appProps.getDelta())) {
            log.info("delete [{}]", type);
            mtsService.deleteMin(type);
        }
    }

    public void setBasicResources() {
        mtsService.addMin(ResourceType.VM);
        mtsService.addMin(ResourceType.DB);
    }

}

