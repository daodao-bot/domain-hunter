package run.ice.fun.domain.hunter.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import run.ice.fun.domain.hunter.config.AppConfig;
import run.ice.fun.domain.hunter.constant.AppConstant;
import run.ice.fun.domain.hunter.entity.Domain;
import run.ice.fun.domain.hunter.error.AppError;
import run.ice.fun.domain.hunter.model.DomainSniper;
import run.ice.fun.domain.hunter.model.DomainTarget;
import run.ice.fun.domain.hunter.repository.DomainRepository;
import run.ice.lib.core.error.CoreException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author DaoDao
 */
@Slf4j
@Service
@Transactional
public class HunterService {

    @Resource
    private AppConfig appConfig;

    @Resource
    private DomainRepository domainRepository;

    @Resource
    private JdbcClient jdbcClient;

    public void domainHunter(DomainTarget param) {
        Integer bit = param.getBit();
        String tld = param.getTld();
        String[] tlds = AppConstant.TLDS;
        if (null != tld && !List.of(tlds).contains(tld)) {
            throw new CoreException(AppError.TLD_ERROR, tld);
        }
        switch (bit) {
            case 1:
                bit1(tld);
                break;
            case 2:
                bit2(tld);
                break;
            case 3:
                bit3(tld);
                break;
            case 4:
                bit4(tld);
                break;
            default:
                throw new CoreException(AppError.BIT_ERROR, String.valueOf(bit));
        }
    }

    public void domainSniper(DomainSniper param) {
        String sld = param.getSld();
        String tld = param.getTld();
        String[] tlds = AppConstant.TLDS;
        if (null != tld && !List.of(tlds).contains(tld)) {
            throw new CoreException(AppError.TLD_ERROR, tld);
        }
        toCheck(sld, tld);
    }

    public void bit1(String tld) {
        char[] chars = AppConstant.LETTERS;
        for (char c : chars) {
            String sld = String.valueOf(c);
            toCheck(sld, tld);
        }
    }

    public void bit2(String tld) {
        char[] chars = AppConstant.LETTERS;
        for (char c1 : chars) {
            for (char c2 : chars) {
                String sld = String.format("%s%s", c1, c2);
                toCheck(sld, tld);
            }
        }
    }

    public void bit3(String tld) {
        char[] chars = AppConstant.LETTERS;
        for (char c1 : chars) {
            for (char c2 : chars) {
                for (char c3 : chars) {
                    String sld = String.format("%s%s%s", c1, c2, c3);
                    toCheck(sld, tld);
                }
            }
        }
    }

    public void bit4(String tld) {
        char[] chars = AppConstant.LETTERS;
        for (char c1 : chars) {
            for (char c2 : chars) {
                for (char c3 : chars) {
                    for (char c4 : chars) {
                        String sld = String.format("%s%s%s%s", c1, c2, c3, c4);
                        toCheck(sld, tld);
                    }
                }
            }
        }
    }

    public void toCheck(String sld, String tld) {
        Thread.ofVirtual().start(() -> doCheck(sld, tld));
    }

    @SuppressWarnings("unchecked")
    public void doCheck(String sld, String tld) {
        Domain domain = new Domain();
        domain.setSld(sld);
        domain.setTld(tld);
        Optional<Domain> optional = domainRepository.findOne(Example.of(domain));
        if (optional.isPresent()) {
            domain = optional.get();
            Boolean valid = domain.getValid();
            if (valid) {
                return;
            }
        } else {
            domain.setValid(Boolean.FALSE);
            domain.setCreateTime(LocalDateTime.now());
            domain.setUpdateTime(LocalDateTime.now());
            domain = domainRepository.save(domain);
        }
        LinkedHashMap<String, ?> resultMap;
        try {
            resultMap = check(sld, tld);
        } catch (Exception e) {
            log.error("{}.{} check error : {}", sld, tld, e.getMessage());
            return;
        }
        Boolean success = (Boolean) resultMap.get("success");
        if (!success) {
            log.error("{}.{} check error : {}", sld, tld, resultMap);
            return;
        }
        LinkedHashMap<String, ?> module = (LinkedHashMap<String, ?>) resultMap.get("module");
        if (null == module) {
            log.error("{}.{} check error : {}", sld, tld, resultMap);
            return;
        }
        LinkedHashMap<String, ?> domainDetail = (LinkedHashMap<String, ?>) module.get("domainDetail");
        if (null == domainDetail) {
            log.error("{}.{} check error : {}", sld, tld, resultMap);
            return;
        }
        Integer avail = (Integer) domainDetail.get("avail");
        if (avail == 1) {
            ArrayList<LinkedHashMap<String, ?>> priceList = (ArrayList<LinkedHashMap<String, ?>>) module.get("priceList");
            Optional<LinkedHashMap<String, ?>> o = priceList.stream()
                    .filter(price -> price.get("period").equals(12) && price.get("action").equals("activate"))
                    .findFirst();
            if (o.isPresent()) {
                LinkedHashMap<String, ?> price = o.get();
                Long money = (Long) price.get("money");
                domain.setAvail(Boolean.TRUE);
                domain.setPrice(money);
                domain.setValid(Boolean.TRUE);
                domain.setUpdateTime(LocalDateTime.now());
                domainRepository.save(domain);
            } else {
                log.error("{}.{} check error : {}", sld, tld, resultMap);
            }
        } else {
            LinkedHashMap<String, ?> saleDetail = (LinkedHashMap<String, ?>) module.get("saleDetail");
            if (null == saleDetail) {
                domain.setAvail(Boolean.FALSE);
            } else {
                String price = (String) saleDetail.get("price");
                if (null == price) {
                    domain.setAvail(Boolean.FALSE);
                } else {
                    Long money = Long.valueOf(price.replaceAll(",", ""));
                    domain.setAvail(Boolean.TRUE);
                    domain.setPrice(money);
                }
            }
            domain.setValid(Boolean.TRUE);
            domain.setUpdateTime(LocalDateTime.now());
            domainRepository.save(domain);
        }
        doCount(sld, tld);
    }

    public LinkedHashMap<String, ?> check(String sld, String tld) {
        String domain = sld + "." + tld;
        String checkApi = appConfig.getCheckApi();
        URI uri = URI.create(checkApi + "?domain=" + domain + "&productID=210701&token=" + UUID.randomUUID());
        ParameterizedTypeReference<LinkedHashMap<String, ?>> typeReference = new ParameterizedTypeReference<>() {
        };
        LinkedHashMap<String, ?> result;
        RestClient restClient = RestClient.create();
        try {
            result = restClient.get()
                    .uri(uri)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(typeReference);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public void doCount(String sld, String tld) {
        int length = sld.length();
        int total = (int) Math.pow(AppConstant.LETTERS.length, length);
        Object recordCount = jdbcClient.sql("SELECT COUNT(*) FROM `domain_hunter`.`domain` WHERE `tld` = :tld AND LENGTH(`sld`) = :length")
                .param("tld", tld)
                .param("length", length)
                .query()
                .singleValue();
        Long record = (Long) recordCount;
        Object doneCount = jdbcClient.sql("SELECT COUNT(*) FROM `domain_hunter`.`domain` WHERE `tld` = :tld AND LENGTH(`sld`) = :length AND `valid` = TRUE")
                .param("tld", tld)
                .param("length", length)
                .query()
                .singleValue();
        Long done = (Long) doneCount;
        log.info("{}.{} : {} / {} / {}", sld, tld, done, record, total);
    }

}
