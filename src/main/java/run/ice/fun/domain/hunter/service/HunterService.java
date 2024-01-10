package run.ice.fun.domain.hunter.service;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import run.ice.fun.domain.hunter.config.AppConfig;
import run.ice.fun.domain.hunter.constant.DomainConstant;
import run.ice.fun.domain.hunter.entity.Domain;
import run.ice.fun.domain.hunter.error.HunterError;
import run.ice.fun.domain.hunter.message.MessageProducer;
import run.ice.fun.domain.hunter.model.DomainData;
import run.ice.fun.domain.hunter.model.DomainSniper;
import run.ice.fun.domain.hunter.model.DomainTarget;
import run.ice.fun.domain.hunter.repository.DomainRepository;
import run.ice.lib.core.error.AppException;

import java.net.URI;
import java.time.Duration;
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

    @Resource
    private MessageProducer messageProducer;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void domainHunter(DomainTarget param) {
        Integer bit = param.getBit();
        String tld = param.getTld();
        String[] tlds = DomainConstant.TLDS;
        if (null != tld && !List.of(tlds).contains(tld)) {
            throw new AppException(HunterError.TLD_ERROR, tld);
        }
        switch (bit) {
            case 1:
                Thread.ofVirtual().start(() -> bit1(tld));
                break;
            case 2:
                Thread.ofVirtual().start(() -> bit2(tld));
                break;
            case 3:
                Thread.ofVirtual().start(() -> bit3(tld));
                break;
            case 4:
                Thread.ofVirtual().start(() -> bit4(tld));
                break;
            default:
                throw new AppException(HunterError.BIT_ERROR, String.valueOf(bit));
        }
    }

    public DomainData domainSniper(DomainSniper param) {
        String sld = param.getSld();
        String tld = param.getTld();
        String[] tlds = DomainConstant.TLDS;
        if (null != tld && !List.of(tlds).contains(tld)) {
            throw new AppException(HunterError.TLD_ERROR, tld);
        }
        doCheck(sld, tld);
        Domain domain = new Domain();
        domain.setSld(sld);
        domain.setTld(tld);
        domain = domainRepository.findOne(Example.of(domain))
                .orElseThrow(() -> new AppException(HunterError.DOMAIN_NOT_EXIST, sld + "." + tld));
        DomainData data = new DomainData();
        BeanUtils.copyProperties(domain, data);
        return data;
    }

    public void bit1(String tld) {
        char[] chars = DomainConstant.LETTERS;
        for (char c : chars) {
            String sld = String.valueOf(c);
            toCheck(sld, tld);
        }
    }

    public void bit2(String tld) {
        char[] chars = DomainConstant.LETTERS;
        for (char c1 : chars) {
            for (char c2 : chars) {
                String sld = String.format("%s%s", c1, c2);
                toCheck(sld, tld);
            }
        }
    }

    public void bit3(String tld) {
        char[] chars = DomainConstant.LETTERS;
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
        char[] chars = DomainConstant.LETTERS;
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
        String key = cacheKey(sld, tld);
        Object o = redisTemplate.opsForValue().get(key);
        if (null != o) {
            Domain domain = (Domain) o;
            Boolean valid = domain.getValid();
            if (valid) {
                return;
            }
        }
        messageProducer.domainHunter(sld + "." + tld);
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
        if (avail == 1) { // 可用
            domain.setAvail(Boolean.TRUE);
            ArrayList<LinkedHashMap<String, ?>> priceList = (ArrayList<LinkedHashMap<String, ?>>) module.get("priceList");
            Optional<LinkedHashMap<String, ?>> o = priceList.stream()
                    .filter(price -> price.get("period").equals(12) && price.get("action").equals("activate"))
                    .findFirst();
            if (o.isPresent()) {
                LinkedHashMap<String, ?> price = o.get();
                Long money = (Long) price.get("money");
                domain.setPrice(money);
            } else {
                log.error("{}.{} check error : {}", sld, tld, resultMap);
            }
        } else { // 不可用
            domain.setAvail(Boolean.FALSE);
            LinkedHashMap<String, ?> saleDetail = (LinkedHashMap<String, ?>) module.get("saleDetail");
            if (null != saleDetail) {
                Integer productType = (Integer) saleDetail.get("productType");
                String price = (String) saleDetail.get("price");
                if (null != productType && productType == 2 && null != price && !price.isEmpty()) { // 2 一口价
                    Long money = Long.valueOf(price.replaceAll(",", ""));
                    domain.setPrice(money);
                }
            }
        }
        domain.setUpdateTime(LocalDateTime.now());
        domain.setValid(Boolean.TRUE);
        domain = domainRepository.save(domain);

        String key = cacheKey(sld, tld);
        redisTemplate.opsForValue().set(key, domain, Duration.ofDays(30L));

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

    private void doCount(String sld, String tld) {
        int length = sld.length();
        int total = (int) Math.pow(DomainConstant.LETTERS.length, length);
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

    private String cacheKey(String sld, String tld) {
        return String.format("domain:%s.%s", sld, tld);
    }

}
