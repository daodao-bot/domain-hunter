package run.ice.fun.domain.hunter.service;

import jakarta.annotation.Resource;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import run.ice.fun.domain.hunter.constant.AppConstant;
import run.ice.fun.domain.hunter.entity.Domain;
import run.ice.fun.domain.hunter.error.AppError;
import run.ice.fun.domain.hunter.model.DomainData;
import run.ice.fun.domain.hunter.model.DomainSearch;
import run.ice.fun.domain.hunter.model.DomainSelect;
import run.ice.fun.domain.hunter.model.DomainUpsert;
import run.ice.fun.domain.hunter.repository.DomainRepository;
import run.ice.lib.core.error.CoreException;
import run.ice.lib.core.model.PageData;
import run.ice.lib.core.model.PageParam;
import run.ice.lib.util.bean.BeanUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author DaoDao
 */
@Slf4j
@Service
@Transactional
public class DomainService {

    @Resource
    private DomainRepository domainRepository;

    public DomainData domainSelect(DomainSelect param) {
        Long id = param.getId();
        Domain entity = domainRepository.findById(id)
                .orElseThrow(() -> new CoreException(AppError.DOMAIN_NOT_EXIST, String.valueOf(id)));
        DomainData data = new DomainData();
        BeanUtils.copyProperties(entity, data);
        return data;
    }

    public DomainData domainUpsert(DomainUpsert param) {
        Long id = param.getId();
        String sld = param.getSld();
        String tld = param.getTld();
        String[] tlds = AppConstant.TLDS;
        if (null != tld && !List.of(tlds).contains(tld)) {
            throw new CoreException(AppError.TLD_ERROR, tld);
        }
        Domain entity = new Domain();
        if (id == null) {
            entity.setSld(sld);
            entity.setTld(tld);
            Optional<Domain> optional = domainRepository.findOne(Example.of(entity));
            if (optional.isPresent()) {
                entity = optional.get();
                throw new CoreException(AppError.DOMAIN_ALREADY_EXIST, entity.toJson());
            }
            entity.setCreateTime(LocalDateTime.now());
        } else {
            Optional<Domain> optional = domainRepository.findById(id);
            if (optional.isEmpty()) {
                throw new CoreException(AppError.DOMAIN_NOT_EXIST, String.valueOf(id));
            }
            Domain model = new Domain();
            model.setSld(sld);
            model.setTld(tld);
            Optional<Domain> o = domainRepository.findOne(Example.of(model));
            if (o.isPresent()) {
                model = o.get();
                if (!model.getId().equals(id)) {
                    throw new CoreException(AppError.DOMAIN_ALREADY_EXIST, model.toJson());
                }
            }
        }
        BeanUtils.copyProperties(param, entity, BeanUtil.nullProperties(param));
        entity.setUpdateTime(LocalDateTime.now());
        entity = domainRepository.save(entity);
        DomainData data = new DomainData();
        BeanUtils.copyProperties(entity, data);
        return data;
    }

    public PageData<DomainData> domainSearch(PageParam<DomainSearch> pageParam) {
        Integer page = pageParam.getPage();
        Integer size = pageParam.getSize();
        DomainSearch param = pageParam.getParam();
        String sld = param.getSld();
        String tld = param.getTld();
        String[] tlds = AppConstant.TLDS;
        if (null != tld && !List.of(tlds).contains(tld)) {
            throw new CoreException(AppError.TLD_ERROR, tld);
        }
        Boolean avail = param.getAvail();
        Long priceLower = param.getPriceLower();
        Long priceUpper = param.getPriceUpper();
        Boolean valid = param.getValid();
        Specification<Domain> specification = (root, query, builder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (sld != null) {
                predicateList.add(builder.equal(root.get("sld"), sld));
            }
            if (tld != null) {
                predicateList.add(builder.equal(root.get("tld"), tld));
            }
            if (avail != null) {
                predicateList.add(builder.equal(root.get("avail"), avail));
            }
            if (valid != null) {
                predicateList.add(builder.equal(root.get("valid"), valid));
            }
            if (priceLower != null) {
                predicateList.add(builder.greaterThanOrEqualTo(root.get("price"), priceLower));
            }
            if (priceUpper != null) {
                predicateList.add(builder.lessThanOrEqualTo(root.get("price"), priceUpper));
            }
            Predicate[] predicates = new Predicate[predicateList.size()];
            predicates = predicateList.toArray(predicates);
            return query.where(predicates).getRestriction();
        };
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Domain> all = domainRepository.findAll(specification, pageable);
        Long total = all.getTotalElements();
        List<DomainData> list = all.getContent().stream().map(source -> {
            DomainData target = new DomainData();
            BeanUtils.copyProperties(source, target);
            return target;
        }).toList();
        return new PageData<>(page, size, total, list);
    }

}
