package org.fwoxford.service.impl;

import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Implementation for managing StockIn.
 */
@Service
@Transactional
public class StockListServiceImpl implements StockListService {

    private final Logger log = LoggerFactory.getLogger(StockListServiceImpl.class);


    @Autowired
    private StockListRepositries stockListRepositries;

    @Autowired
    private StockListByProjectRepositries stockListByProjectRepositries;

    @Autowired
    private StockFrozenTubeListRepositries stockFrozenTubeListRepositries;

    @Autowired
    private StockFrozenBoxListRepositries stockFrozenBoxListRepositries;

    @Override
    public DataTablesOutput<FrozenPositionListAllDataTableEntity> getPageStockFrozenPositionList(DataTablesInput input, FrozenPositionListSearchForm searchForm) {
        DataTablesOutput<FrozenPositionListAllDataTableEntity> stockInDataTablesOutput = new DataTablesOutput<FrozenPositionListAllDataTableEntity>();

        if(searchForm != null && searchForm.getProjectCodeStr()!=null){
            Specification<FrozenPositionListAllDataTableEntity> specification = new Specification<FrozenPositionListAllDataTableEntity>() {
                @Override
                public Predicate toPredicate(Root<FrozenPositionListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query = getSearchQuery(root,query,cb,searchForm);
                    return query.getRestriction();
                }
            };
            stockInDataTablesOutput = stockListByProjectRepositries.findAll(input,specification);
        }else{
            Specification<FrozenPositionListDataTableEntity> specification = new Specification<FrozenPositionListDataTableEntity>() {
                @Override
                public Predicate toPredicate(Root<FrozenPositionListDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query.distinct(true);
                    query = getSearchQueryExceptProject(root,query,cb,searchForm);
                    return query.getRestriction();
                }
            };
            Converter<FrozenPositionListDataTableEntity, FrozenPositionListAllDataTableEntity> userConverter = new Converter<FrozenPositionListDataTableEntity, FrozenPositionListAllDataTableEntity>() {
                @Override
                public FrozenPositionListAllDataTableEntity convert(FrozenPositionListDataTableEntity e) {
                    return new FrozenPositionListAllDataTableEntity(e.getId(), e.getEquipmentType(),
                        e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getShelvesType(),
                        e.getCountOfUsed(),e.getCountOfRest(),
                        e.getStatus(),"","",e.getEquipmentTypeId(),e.getEquipmentId(),e.getAreaId(),e.getShelvesId(),e.getShelvesTypeId(),e.getPosition());
                }
            };
            stockInDataTablesOutput =  stockListRepositries.findAll(input,null,specification,userConverter);
        }

        return stockInDataTablesOutput;
    }

    @Override
    public DataTablesOutput<FrozenBoxListAllDataTableEntity> getPageStockFrozenBoxList(DataTablesInput input, FrozenBoxListSearchForm searchForm) {

        DataTablesOutput<FrozenBoxListAllDataTableEntity> output = new DataTablesOutput<FrozenBoxListAllDataTableEntity>();
        Converter<FrozenBoxListAllDataTableEntity, FrozenBoxListAllDataTableEntity> convert = new Converter<FrozenBoxListAllDataTableEntity, FrozenBoxListAllDataTableEntity>() {
            @Override
            public FrozenBoxListAllDataTableEntity convert(FrozenBoxListAllDataTableEntity e) {
                String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getColumnsInShelf(),e.getRowsInShelf());
                return new FrozenBoxListAllDataTableEntity(e.getId(),e.getFrozenBoxCode(),e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getRowsInShelf(),e.getColumnsInShelf(),
                    position,e.getSampleType(),e.getSampleClassification(),e.getFrozenBoxType(),e.getCountOfUsed(),e.getCountOfRest(),e.getStatus(),e.getProjectName(),e.getProjectCode(),
                    e.getEquipmentId(),e.getAreaId(),e.getShelvesId(),e.getSampleTypeId(),e.getSampleClassificationId(),e.getFrozenBoxTypeId());
            }
        };
        Specification<FrozenBoxListAllDataTableEntity> specification = new Specification<FrozenBoxListAllDataTableEntity>() {
            @Override
            public Predicate toPredicate(Root<FrozenBoxListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query = getSearchQueryForBox(root,query,cb,searchForm);
                return query.getRestriction();
            }
        };
            output = stockFrozenBoxListRepositries.findAll(input,specification,null,convert);
        return output;
    }

    private CriteriaQuery<?> getSearchQueryForBox(Root<FrozenBoxListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenBoxListSearchForm searchForm) {
        if (searchForm != null) {
            List<Predicate> predicate = new ArrayList<>();
            if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                CriteriaBuilder.In<String> in = cb.in(root.get("projectCode"));
                for (String projectCode : searchForm.getProjectCodeStr()) {
                    in.value(projectCode);
                }
                predicate.add(in);
            }
            if (searchForm.getFrozenBoxCodeStr() != null && searchForm.getFrozenBoxCodeStr().length > 0) {
                CriteriaBuilder.In<String> in = cb.in(root.get("frozenBoxCode"));
                for (String frozenBoxCode : searchForm.getFrozenBoxCodeStr()) {
                    in.value(frozenBoxCode);
                }
                predicate.add(in);
            }
            if (searchForm.getStatus() != null) {
                Predicate p3 = cb.equal(root.get("status").as(String.class), searchForm.getStatus());
                predicate.add(p3);
            }
            if (searchForm.getEquipmentId() != null) {
                Predicate p3 = cb.equal(root.get("equipmentId").as(Long.class), searchForm.getEquipmentId());
                predicate.add(p3);
            }
            if (searchForm.getAreaId() != null) {
                Predicate p4 = cb.equal(root.get("areaId").as(Long.class), searchForm.getAreaId());
                predicate.add(p4);
            }
            if (searchForm.getShelvesId() != null) {
                Predicate p5 = cb.equal(root.get("shelvesId").as(Long.class), searchForm.getShelvesId());
                predicate.add(p5);
            }
            if (searchForm.getRowsInShelf() != null) {
                Predicate p5 = cb.equal(root.get("rowsInShelf").as(String.class), searchForm.getRowsInShelf());
                predicate.add(p5);
            }
            if (searchForm.getColumnsInShelf() != null) {
                Predicate p5 = cb.equal(root.get("columnsInShelf").as(String.class), searchForm.getColumnsInShelf());
                predicate.add(p5);
            }
            if (searchForm.getSampleTypeId() != null) {
                Predicate p5 = cb.equal(root.get("sampleTypeId").as(Long.class), searchForm.getSampleTypeId());
                predicate.add(p5);
            }
            if (searchForm.getSampleClassificationId() != null) {
                Predicate p5 = cb.equal(root.get("sampleClassificationId").as(Long.class), searchForm.getSampleClassificationId());
                predicate.add(p5);
            }
            if (searchForm.getFrozenBoxTypeId() != null) {
                Predicate p5 = cb.equal(root.get("frozenBoxTypeId").as(Long.class), searchForm.getFrozenBoxTypeId());
                predicate.add(p5);
            }
            if (searchForm.getSpaceType() != null) {
                String searchValue = "";
                switch (searchForm.getSpaceType()) {
                    case 1:
                        searchValue = "countOfUsed";
                        break;
                    case 2:
                        searchValue = "countOfRest";
                        break;
                }
                Predicate p5 = null;
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        p5 = cb.gt(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 2:
                        p5 = cb.ge(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 3:
                        p5 = cb.equal(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 4:
                        p5 = cb.lt(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 5:
                        p5 = cb.le(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p5);
                }
            }
            Predicate[] pre = new Predicate[predicate.size()];
            query.where(predicate.toArray(pre));
        }
        return query;
    }

    @Override
    public DataTablesOutput<FrozenTubeListAllDataTableEntity> getPageStockFrozenTubeList(DataTablesInput input, FrozenTubeListAllDataTableEntity search) {
        return null;
    }

    private CriteriaQuery<?> getSearchQuery(Root<FrozenPositionListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenPositionListSearchForm searchForm) {
        if (searchForm != null) {
            List<Predicate> predicate = new ArrayList<>();
            query.distinct(true);
            if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                CriteriaBuilder.In<String> in = cb.in(root.get("projectCode"));
                for (String id : searchForm.getProjectCodeStr()) {
                    in.value(id);
                }
                predicate.add(in);
            }
            if (searchForm.getEquipmentTypeId() != null) {
                Predicate p1 = cb.equal(root.get("equipmentTypeId").as(Long.class), searchForm.getEquipmentTypeId());
                predicate.add(p1);
            }
            if (searchForm.getShelvesTypeId() != null) {
                Predicate p2 = cb.equal(root.get("shelvesTypeId").as(Long.class), searchForm.getShelvesTypeId());
                predicate.add(p2);
            }
            if (searchForm.getStatus() != null) {
                Predicate p3 = cb.equal(root.get("status").as(String.class), searchForm.getStatus());
                predicate.add(p3);
            }
            if (searchForm.getEquipmentId() != null) {
                Predicate p3 = cb.equal(root.get("equipmentId").as(Long.class), searchForm.getEquipmentId());
                predicate.add(p3);
            }
            if (searchForm.getAreaId() != null) {
                Predicate p4 = cb.equal(root.get("areaId").as(Long.class), searchForm.getAreaId());
                predicate.add(p4);
            }
            if (searchForm.getShelvesId() != null) {
                Predicate p5 = cb.equal(root.get("shelvesId").as(Long.class), searchForm.getShelvesId());
                predicate.add(p5);
            }
            if (searchForm.getSpaceType() != null) {
                String searchValue = "";
                switch (searchForm.getSpaceType()) {
                    case 1:
                        searchValue = "countOfUsed";
                        break;
                    case 2:
                        searchValue = "countOfRest";
                        break;
                }
                Predicate p5 = null;
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        p5 = cb.gt(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 2:
                        p5 = cb.ge(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 3:
                        p5 = cb.equal(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 4:
                        p5 = cb.lt(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 5:
                        p5 = cb.le(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p5);
                }
            }
            Predicate[] pre = new Predicate[predicate.size()];
            query.where(predicate.toArray(pre));
        }
        return query;
    }

    private CriteriaQuery<?> getSearchQueryExceptProject(Root<FrozenPositionListDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenPositionListSearchForm searchForm) {
        if (searchForm != null) {
            List<Predicate> predicate = new ArrayList<>();
            query.distinct(true);
            if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                CriteriaBuilder.In<String> in = cb.in(root.get("projectCode"));
                for (String id : searchForm.getProjectCodeStr()) {
                    in.value(id);
                }
                predicate.add(in);
            }
            if (searchForm.getEquipmentTypeId() != null) {
                Predicate p1 = cb.equal(root.get("equipmentTypeId").as(Long.class), searchForm.getEquipmentTypeId());
                predicate.add(p1);
            }
            if (searchForm.getShelvesTypeId() != null) {
                Predicate p2 = cb.equal(root.get("shelvesTypeId").as(Long.class), searchForm.getShelvesTypeId());
                predicate.add(p2);
            }
            if (searchForm.getStatus() != null) {
                Predicate p3 = cb.equal(root.get("status").as(String.class), searchForm.getStatus());
                predicate.add(p3);
            }
            if (searchForm.getEquipmentId() != null) {
                Predicate p3 = cb.equal(root.get("equipmentId").as(Long.class), searchForm.getEquipmentId());
                predicate.add(p3);
            }
            if (searchForm.getAreaId() != null) {
                Predicate p4 = cb.equal(root.get("areaId").as(Long.class), searchForm.getAreaId());
                predicate.add(p4);
            }
            if (searchForm.getShelvesId() != null) {
                Predicate p5 = cb.equal(root.get("shelvesId").as(Long.class), searchForm.getShelvesId());
                predicate.add(p5);
            }
            if (searchForm.getSpaceType() != null) {
                String searchValue = "";
                switch (searchForm.getSpaceType()) {
                    case 1:
                        searchValue = "countOfUsed";
                        break;
                    case 2:
                        searchValue = "countOfRest";
                        break;
                }
                Predicate p5 = null;
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        p5 = cb.gt(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 2:
                        p5 = cb.ge(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 3:
                        p5 = cb.equal(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 4:
                        p5 = cb.lt(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                    case 5:
                        p5 = cb.le(root.get(searchValue).as(Long.class), searchForm.getNumber());
                        break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p5);
                }
            }
            Predicate[] pre = new Predicate[predicate.size()];
            query.where(predicate.toArray(pre));
        }
        return query;
    }
}
