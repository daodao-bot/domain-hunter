package run.ice.fun.domain.hunter.controller;

import org.springframework.web.bind.annotation.RestController;
import run.ice.fun.domain.hunter.api.IndexApi;

@RestController
public class IndexController implements IndexApi {

    @Override
    public String index() {
        return null;
    }

}
