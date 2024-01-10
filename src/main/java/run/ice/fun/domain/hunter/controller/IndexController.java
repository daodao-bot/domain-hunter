package run.ice.fun.domain.hunter.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RestController;
import run.ice.fun.domain.hunter.api.IndexApi;
import run.ice.fun.domain.hunter.config.AppConfig;

@RestController
public class IndexController implements IndexApi {

    @Resource
    private AppConfig appConfig;

    @Override
    public String index() {
        return appConfig.getApplication();
    }

}
