package org.fwoxford.service.impl;

import liquibase.util.CollectionUtil;
import liquibase.util.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.FrozenTube;
import org.fwoxford.domain.FrozenTubeType;
import org.fwoxford.repository.*;
import org.fwoxford.service.*;
import org.fwoxford.service.dto.response.*;
import org.fwoxford.web.rest.util.BankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * Service Implementation for managing StockList.
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
    private FrozenTubeHistoryRepositories frozenTubeHistoryRepositories;

    @Autowired
    private StockFrozenBoxListRepositries stockFrozenBoxListRepositries;

    @Autowired
    private ShelvesListRepositries shelvesListRepositries;

    @Autowired
    private ShelvesListByProjectRepositries shelvesListByProjectRepositries;

    @Autowired
    private AreasListRepositories areasListRepositories;
    @Autowired
    private AreasListByProjectRepositories areasListByProjectRepositories;
    @Autowired
    private FrozenTubeRepository frozenTubeRepository;
    /**
     * 冻存位置清单
     * @param input
     * @param searchForm
     * @return
     */
    @Override
    public DataTablesOutput<FrozenPositionListAllDataTableEntity> getPageStockFrozenPositionList(DataTablesInput input, FrozenPositionListSearchForm searchForm) {
        DataTablesOutput<FrozenPositionListAllDataTableEntity> stockInDataTablesOutput = new DataTablesOutput<FrozenPositionListAllDataTableEntity>();

        if(searchForm != null && searchForm.getProjectCodeStr()!=null && searchForm.getProjectCodeStr().length>0){
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
                    String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),null,null,null,null);
                    return new FrozenPositionListAllDataTableEntity(e.getId(), e.getEquipmentType(),
                        e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getShelvesType(),
                        e.getCountOfUsed(),e.getCountOfRest(),
                        e.getStatus(),"","",e.getEquipmentTypeId(),e.getEquipmentId(),e.getAreaId(),e.getShelvesId(),e.getShelvesTypeId(),position,e.getMemo());
                }
            };
            stockInDataTablesOutput =  stockListRepositries.findAll(input,null,specification,userConverter);
        }

        return stockInDataTablesOutput;
    }
    /**
     * 冻存盒清单
     * @param input
     * @param searchForm
     * @return
     */
    @Override
    public DataTablesOutput<FrozenBoxListAllDataTableEntity> getPageStockFrozenBoxList(DataTablesInput input, FrozenBoxListSearchForm searchForm) {

        DataTablesOutput<FrozenBoxListAllDataTableEntity> output = new DataTablesOutput<FrozenBoxListAllDataTableEntity>();
        Converter<FrozenBoxListAllDataTableEntity, FrozenBoxListAllDataTableEntity> convert = new Converter<FrozenBoxListAllDataTableEntity, FrozenBoxListAllDataTableEntity>() {
            @Override
            public FrozenBoxListAllDataTableEntity convert(FrozenBoxListAllDataTableEntity e) {
                String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getColumnsInShelf(),e.getRowsInShelf(),null,null);
                return new FrozenBoxListAllDataTableEntity(e.getId(),e.getFrozenBoxCode(),e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getRowsInShelf(),e.getColumnsInShelf(),
                    position,e.getSampleType(),e.getSampleClassification(),e.getFrozenBoxType(),e.getCountOfUsed(),e.getCountOfRest(),e.getStatus(),e.getProjectName(),e.getProjectCode(),
                    e.getEquipmentId(),e.getAreaId(),e.getShelvesId(),e.getSampleTypeId(),e.getSampleClassificationId(),e.getFrozenBoxTypeId(),e.getMemo(),e.getProjectId());
            }
        };
        Specification<FrozenBoxListAllDataTableEntity> specification =  new Specification<FrozenBoxListAllDataTableEntity>() {
            @Override
            public Predicate toPredicate(Root<FrozenBoxListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query = getSearchQueryForBox(root,query,cb,searchForm);
                return query.getRestriction();
            }
        };
        output = stockFrozenBoxListRepositries.findAll(input,specification,null,convert);
        return output;
    }
    /**
     * 样本清单
     * @param input
     * @param search
     * @return
     */
    @Override
    public DataTablesOutput<FrozenTubeListAllDataTableEntity> getPageStockFrozenTubeList(DataTablesInput input, FrozenTubeListSearchForm search) {
        DataTablesOutput<FrozenTubeListAllDataTableEntity> output = new DataTablesOutput<FrozenTubeListAllDataTableEntity>();
        Converter<FrozenTubeListAllDataTableEntity, FrozenTubeListAllDataTableEntity> convert = new Converter<FrozenTubeListAllDataTableEntity, FrozenTubeListAllDataTableEntity>() {
            @Override
            public FrozenTubeListAllDataTableEntity convert(FrozenTubeListAllDataTableEntity e) {
                FrozenTube frozenTube = frozenTubeRepository.findOne(e.getId());
                String sampleCode = StringUtils.isEmpty(frozenTube.getSampleCode())?frozenTube.getSampleTempCode():frozenTube.getSampleCode();
                String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getColumnsInShelf(),e.getRowsInShelf(),null,null);
                String positionInBox = e.getTubeRows()+e.getTubeColumns();
                return new FrozenTubeListAllDataTableEntity(
                    e.getId(),position,e.getFrozenBoxCode(),sampleCode,e.getProjectCode(),e.getProjectName(),e.getSampleType(),
                    e.getSampleClassification(),e.getSex(),e.getAge(),e.getDiseaseType(),e.getHemolysis(),e.getBloodLipid(),e.getSampleUsedTimes(),
                    e.getStatus(),e.getFrozenTubeState(),e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getRowsInShelf(),e.getColumnsInShelf(),
                    e.getTubeRows(),e.getTubeColumns(),e.getEquipmentId(),e.getAreaId(),e.getShelvesId(),e.getSampleTypeId(),e.getSampleClassificationId(),positionInBox
                );
            }
        };
        Specification<FrozenTubeListAllDataTableEntity> specification = new Specification<FrozenTubeListAllDataTableEntity>() {
            @Override
            public Predicate toPredicate(Root<FrozenTubeListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query = getSearchQueryForTube(root,query,cb,search);
                return query.getRestriction();
            }
        };
        output = stockFrozenTubeListRepositries.findAll(input,specification,null,convert);
        return output;
    }
    /**
     * 样本历史清单
     * @param frozenTubeId
     * @return
     */
    @Override
    public List<FrozenTubeHistory> findFrozenTubeHistoryDetail(Long frozenTubeId) {
        List<FrozenTubeHistory> frozenTubeHistories = frozenTubeHistoryRepositories.findByFrozenTubeIdAndStatusNot(frozenTubeId,Constants.INVALID);
        List<FrozenTubeHistory> frozenTubeHistoryList = new ArrayList<FrozenTubeHistory>();
        for (FrozenTubeHistory e :frozenTubeHistories){
            String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getColumnsInShelf(),e.getRowsInShelf(),null,null);
            FrozenTubeHistory frozenTubeHistory = new FrozenTubeHistory(e.getId(),e.getTranshipId(),e.getTranshipCode(),e.getStockInId(),e.getStockInCode(),e.getStockOutTaskId(),
                e.getStockOutTaskCode(),e.getHandoverId(),e.getHandoverCode(),e.getProjectCode(),e.getSampleCode(),e.getType(),e.getStatus(),e.getFrozenBoxCode(),
                position,e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getRowsInShelf(),e.getColumnsInShelf(),e.getPositionInBox(),e.getEquipmentId(),
                e.getAreaId(),e.getShelvesId(),e.getTubeRows(),e.getTubeColumns(),e.getOperateTime(),e.getFrozenTubeId(),e.getMemo(),e.getOperator()
              ,e.getSampleTypeId(),e.getSampleTypeCode(),e.getSampleTypeName(),e.getFrozenTubeTypeId(),e.getFrozenTubeTypeCode(),e.getFrozenTubeTypeName(),e.getFrozenTubeVolumns(),
                e.getFrozenTubeVolumnsUnit(),e.getSampleUsedTimesMost(),e.getSampleUsedTimes(),e.getSampleVolumns(),e.getProjectId(),e.getProjectSiteId(),e.getFrozenSiteCode(),
                e.getFrozenTubeState(),e.getSampleClassificationId(),e.getFrozenBoxId()
            );
            frozenTubeHistoryList.add(frozenTubeHistory);
        }
        return frozenTubeHistoryList;
    }

    @Override
    public DataTablesOutput<ShelvesListAllDataTableEntity> getPageShelvesList(DataTablesInput input, FrozenPositionListSearchForm searchForm) {
        DataTablesOutput<ShelvesListAllDataTableEntity> output = new DataTablesOutput<ShelvesListAllDataTableEntity>();
        if(searchForm != null && searchForm.getProjectCodeStr()!=null && searchForm.getProjectCodeStr().length>0){
            Specification<ShelvesListByProjectDataTableEntity> specification = new Specification<ShelvesListByProjectDataTableEntity>() {
                @Override
                public Predicate toPredicate(Root<ShelvesListByProjectDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query = getSearchShelvesListByProject(root,query,cb,searchForm);
                    return query.getRestriction();
                }
            };
            Converter<ShelvesListByProjectDataTableEntity, ShelvesListAllDataTableEntity> userConverter = new Converter<ShelvesListByProjectDataTableEntity, ShelvesListAllDataTableEntity>() {
                @Override
                public ShelvesListAllDataTableEntity convert(ShelvesListByProjectDataTableEntity e) {
                    String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),null,null,null,null);
                    return new ShelvesListAllDataTableEntity(e.getId(),e.getEquipmentType(),e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getShelvesType(),
                        e.getCountOfUsed(),e.getCountOfRest(),e.getStatus(),e.getEquipmentTypeId(),e.getEquipmentId(),e.getAreaId(),e.getShelvesId(),e.getShelvesTypeId(),
                        position,e.getMemo());
                }
            };
            output = shelvesListByProjectRepositries.findAll(input,null,specification,userConverter);
        }else{
            Specification<ShelvesListAllDataTableEntity> specification = new Specification<ShelvesListAllDataTableEntity>() {
                @Override
                public Predicate toPredicate(Root<ShelvesListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query = getSearchShelvesList(root,query,cb,searchForm);
                    return query.getRestriction();
                }
            };
            Converter<ShelvesListAllDataTableEntity, ShelvesListAllDataTableEntity> userConverter = new Converter<ShelvesListAllDataTableEntity, ShelvesListAllDataTableEntity>() {
                @Override
                public ShelvesListAllDataTableEntity convert(ShelvesListAllDataTableEntity e) {
                    String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),null,null,null,null);
                    return new ShelvesListAllDataTableEntity(e.getId(),e.getEquipmentType(),e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getShelvesType(),
                        e.getCountOfUsed(),e.getCountOfRest(),e.getStatus(),e.getEquipmentTypeId(),e.getEquipmentId(),e.getAreaId(),e.getShelvesId(),e.getShelvesTypeId(),
                        position,e.getMemo());
                }
            };
            output =  shelvesListRepositries.findAll(input,null,specification,userConverter);
        }
        return output;
    }

    @Override
    public DataTablesOutput<AreasListAllDataTableEntity> getPageAreaList(DataTablesInput input, FrozenPositionListSearchForm searchForm) {
        DataTablesOutput<AreasListAllDataTableEntity> output = new DataTablesOutput<AreasListAllDataTableEntity>();
        if(searchForm != null && searchForm.getProjectCodeStr()!=null && searchForm.getProjectCodeStr().length>0){
            Specification<AreasListByProjectDataTableEntity> specification = new Specification<AreasListByProjectDataTableEntity>() {
                @Override
                public Predicate toPredicate(Root<AreasListByProjectDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query = getSearchAreasListByProject(root,query,cb,searchForm);
                    return query.getRestriction();
                }
            };
            Converter<AreasListByProjectDataTableEntity, AreasListAllDataTableEntity> userConverter = new Converter<AreasListByProjectDataTableEntity, AreasListAllDataTableEntity>() {
                @Override
                public AreasListAllDataTableEntity convert(AreasListByProjectDataTableEntity e) {
                    String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),null,null,null,null,null);
                    return new AreasListAllDataTableEntity(e.getId(),e.getEquipmentType(),e.getEquipmentCode(),e.getAreaCode(),e.getStatus(),
                        e.getEquipmentTypeId(),e.getEquipmentId(),position,e.getFreezeFrameNumber(),e.getCountOfRest(),e.getCountOfUsed(),e.getCountOfShelves());
                }
            };
            output = areasListByProjectRepositories.findAll(input,null,specification,userConverter);
        }else{
            Specification<AreasListAllDataTableEntity> specification = new Specification<AreasListAllDataTableEntity>() {
                @Override
                public Predicate toPredicate(Root<AreasListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                    query = getSearchAreasList(root,query,cb,searchForm);
                    return query.getRestriction();
                }
            };
            Converter<AreasListAllDataTableEntity, AreasListAllDataTableEntity> userConverter = new Converter<AreasListAllDataTableEntity, AreasListAllDataTableEntity>() {
                @Override
                public AreasListAllDataTableEntity convert(AreasListAllDataTableEntity e) {
                    String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),null,null,null,null,null);
                    return new AreasListAllDataTableEntity(e.getId(),e.getEquipmentType(),e.getEquipmentCode(),e.getAreaCode(),e.getStatus(),
                        e.getEquipmentTypeId(),e.getEquipmentId(),position,e.getFreezeFrameNumber(),e.getCountOfRest(),e.getCountOfUsed(),e.getCountOfShelves());
                }
            };
            output =  areasListRepositories.findAll(input,null,specification,userConverter);
        }
        return output;
    }

    @Override
    public Map<Long,FrozenTubeHistory> findFrozenTubeHistoryDetailByIds(List<Long> ids) {
        List<FrozenTubeHistory> frozenTubeHistories = frozenTubeHistoryRepositories.findByFrozenTubeIdInAndOperateTimeNotNullAndStatusNot(ids,Constants.INVALID);
        List<FrozenTubeHistory> frozenTubeHistoryList = new ArrayList<FrozenTubeHistory>();
        Map<Long,List<FrozenTubeHistory>> map = new HashMap<Long,List<FrozenTubeHistory>>();
        Map<Long,FrozenTubeHistory> tubeMap = new HashMap<Long,FrozenTubeHistory>();
        for (FrozenTubeHistory e :frozenTubeHistories){
            String position = BankUtil.getPositionString(e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getColumnsInShelf(),e.getRowsInShelf(),null,null);
            FrozenTubeHistory frozenTubeHistory = new FrozenTubeHistory(e.getId(),e.getTranshipId(),e.getTranshipCode(),e.getStockInId(),e.getStockInCode(),e.getStockOutTaskId(),
                e.getStockOutTaskCode(),e.getHandoverId(),e.getHandoverCode(),e.getProjectCode(),e.getSampleCode(),e.getType(),e.getStatus(),e.getFrozenBoxCode(),
                position,e.getEquipmentCode(),e.getAreaCode(),e.getShelvesCode(),e.getRowsInShelf(),e.getColumnsInShelf(),e.getPositionInBox(),e.getEquipmentId(),
                e.getAreaId(),e.getShelvesId(),e.getTubeRows(),e.getTubeColumns(),e.getOperateTime(),e.getFrozenTubeId(),e.getMemo(),e.getOperator()
                ,e.getSampleTypeId(),
                e.getSampleTypeCode(),e.getSampleTypeName(),e.getFrozenTubeTypeId(),e.getFrozenTubeTypeCode(),e.getFrozenTubeTypeName(),e.getFrozenTubeVolumns(),
                e.getFrozenTubeVolumnsUnit(),e.getSampleUsedTimesMost(),e.getSampleUsedTimes(),e.getSampleVolumns(),e.getProjectId(),e.getProjectSiteId(),e.getFrozenSiteCode(),
                e.getFrozenTubeState(),e.getSampleClassificationId(),e.getFrozenBoxId()
            );
            frozenTubeHistoryList.add(frozenTubeHistory);
        }
        for (FrozenTubeHistory e :frozenTubeHistories){
            List<FrozenTubeHistory> list = new ArrayList<FrozenTubeHistory>();
            list = map.get(e.getFrozenTubeId());
            if(list != null && list.size()>0){
                list.add(e);
            }else{
                list = new ArrayList<FrozenTubeHistory>();
            }
            list.add(e);
            map.put(e.getFrozenTubeId(),list);
        }
        for(Long key:map.keySet()){
            List<FrozenTubeHistory> frozenTubeList = map.get(key);
//            Collections.sort(frozenTubeList,new Comparator<FrozenTubeHistory>(){
//                public int compare(FrozenTubeHistory arg1, FrozenTubeHistory arg0) {
//                    return arg0.getOperateTime().compareTo(arg1.getOperateTime());
//                }
//            });
            tubeMap.put(key,frozenTubeList.get(0));
        }
        return tubeMap;
    }

    private CriteriaQuery<?> getSearchAreasList(Root<AreasListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenPositionListSearchForm searchForm) {
        if (searchForm != null) {
            List<Predicate> predicate = new ArrayList<>();
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
                    default:break;
                }
                Predicate p11 = null;
                Expression exp = root.get(searchValue).as(Long.class);
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            p11 = cb.ge(exp, 1);
                        }else{
                            p11 = cb.gt(exp, searchForm.getNumber());
                        }
                        break;
                    case 2:
                        p11 = cb.ge(exp, searchForm.getNumber());
                        break;
                    case 3:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            Predicate p11_1 = cb.and(cb.gt(exp, -1));
                            Predicate p11_2 = cb.and(cb.lt(exp, 1));
                            p11 = cb.and(p11_1,p11_2);
                        }else{
                            p11 = cb.equal(exp, searchForm.getNumber());
                        }
                        break;
                    case 4:
                        p11 = cb.lt(exp, searchForm.getNumber());
                        break;
                    case 5:
                        p11 = cb.le(exp, searchForm.getNumber());
                        break;
                    default:break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p11);
                }
            }
            Predicate[] pre = new Predicate[predicate.size()];
            query.where(predicate.toArray(pre));
        }
        return query;
    }

    private CriteriaQuery<?> getSearchAreasListByProject(Root<AreasListByProjectDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenPositionListSearchForm searchForm) {
        if (searchForm != null) {
            List<Predicate> predicate = new ArrayList<>();
            if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                query.distinct(true);
                if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                    CriteriaBuilder.In<String> in = cb.in(root.get("projectCode"));
                    for (String id : searchForm.getProjectCodeStr()) {
                        in.value(id);
                    }
                    predicate.add(in);
                }
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
                    default:break;
                }
                Predicate p11 = null;
                Expression exp = root.get(searchValue).as(Long.class);
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            p11 = cb.ge(exp, 1);
                        }else{
                            p11 = cb.gt(exp, searchForm.getNumber());
                        }
                        break;
                    case 2:
                        p11 = cb.ge(exp, searchForm.getNumber());
                        break;
                    case 3:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            Predicate p11_1 = cb.and(cb.gt(exp, -1));
                            Predicate p11_2 = cb.and(cb.lt(exp, 1));
                            p11 = cb.and(p11_1,p11_2);
                        }else{
                            p11 = cb.equal(exp, searchForm.getNumber());
                        }
                        break;
                    case 4:
                        p11 = cb.lt(exp, searchForm.getNumber());
                        break;
                    case 5:
                        p11 = cb.le(exp, searchForm.getNumber());
                        break;
                    default:break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p11);
                }
            }
            Predicate[] pre = new Predicate[predicate.size()];
            query.where(predicate.toArray(pre));
        }
        return query;
    }

    private CriteriaQuery<?> getSearchShelvesListByProject(Root<ShelvesListByProjectDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenPositionListSearchForm searchForm) {
        if (searchForm != null) {
            List<Predicate> predicate = new ArrayList<>();
            if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                query.distinct(true);
                if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                    CriteriaBuilder.In<String> in = cb.in(root.get("projectCode"));
                    for (String id : searchForm.getProjectCodeStr()) {
                        in.value(id);
                    }
                    predicate.add(in);
                }
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
                    default:break;
                }
                Predicate p11 = null;
                Expression exp = root.get(searchValue).as(Long.class);
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            p11 = cb.ge(exp, 1);
                        }else{
                            p11 = cb.gt(exp, searchForm.getNumber());
                        }
                        break;
                    case 2:
                        p11 = cb.ge(exp, searchForm.getNumber());
                        break;
                    case 3:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            Predicate p11_1 = cb.and(cb.gt(exp, -1));
                            Predicate p11_2 = cb.and(cb.lt(exp, 1));
                            p11 = cb.and(p11_1,p11_2);
                        }else{
                            p11 = cb.equal(exp, searchForm.getNumber());
                        }
                        break;
                    case 4:
                        p11 = cb.lt(exp, searchForm.getNumber());
                        break;
                    case 5:
                        p11 = cb.le(exp, searchForm.getNumber());
                        break;
                    default:break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p11);
                }
            }
            Predicate[] pre = new Predicate[predicate.size()];
            query.where(predicate.toArray(pre));
        }
        return query;
    }

    private CriteriaQuery<?> getSearchShelvesList(Root<ShelvesListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenPositionListSearchForm searchForm) {
        if (searchForm != null) {
            List<Predicate> predicate = new ArrayList<>();
            if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                query.distinct(true);
                if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                    CriteriaBuilder.In<String> in = cb.in(root.get("projectCode"));
                    for (String id : searchForm.getProjectCodeStr()) {
                        in.value(id);
                    }
                    predicate.add(in);
                }
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
                Predicate p6 = cb.equal(root.get("shelvesId").as(Long.class), searchForm.getShelvesId());
                predicate.add(p6);
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
                    default:break;
                }
                Predicate p11 = null;
                Expression exp = root.get(searchValue).as(Long.class);
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            p11 = cb.ge(exp, 1);
                        }else{
                            p11 = cb.gt(exp, searchForm.getNumber());
                        }
                        break;
                    case 2:
                        p11 = cb.ge(exp, searchForm.getNumber());
                        break;
                    case 3:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            Predicate p11_1 = cb.and(cb.gt(exp, -1));
                            Predicate p11_2 = cb.and(cb.lt(exp, 1));
                            p11 = cb.and(p11_1,p11_2);
                        }else{
                            p11 = cb.equal(exp, searchForm.getNumber());
                        }
                        break;
                    case 4:
                        p11 = cb.lt(exp, searchForm.getNumber());
                        break;
                    case 5:
                        p11 = cb.le(exp, searchForm.getNumber());
                        break;
                    default:break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p11);
                }
            }
            Predicate[] pre = new Predicate[predicate.size()];
            query.where(predicate.toArray(pre));
        }

        return query;
    }

    private CriteriaQuery<?> getSearchQueryForBox(Root<FrozenBoxListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenBoxListSearchForm searchForm) {
        List<Predicate> predicate = new ArrayList<>();
        if (searchForm != null) {
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
                Predicate p6 = cb.equal(root.get("rowsInShelf").as(String.class), searchForm.getRowsInShelf());
                predicate.add(p6);
            }
            if (searchForm.getColumnsInShelf() != null) {
                Predicate p7 = cb.equal(root.get("columnsInShelf").as(String.class), searchForm.getColumnsInShelf());
                predicate.add(p7);
            }
            if (searchForm.getSampleTypeId() != null) {
                Predicate p8 = cb.equal(root.get("sampleTypeId").as(Long.class), searchForm.getSampleTypeId());
                predicate.add(p8);
            }
            if (searchForm.getSampleClassificationId() != null) {
                Predicate p9 = cb.equal(root.get("sampleClassificationId").as(Long.class), searchForm.getSampleClassificationId());
                predicate.add(p9);
            }
            if (searchForm.getFrozenBoxTypeId() != null) {
                Predicate p10 = cb.equal(root.get("frozenBoxTypeId").as(Long.class), searchForm.getFrozenBoxTypeId());
                predicate.add(p10);
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
                    default:break;
                }
                Predicate p11 = null;
                Expression exp = root.get(searchValue).as(Long.class);
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            p11 = cb.ge(exp, 1);
                        }else{
                            p11 = cb.gt(exp, searchForm.getNumber());
                        }
                        break;
                    case 2:
                        p11 = cb.ge(exp, searchForm.getNumber());
                        break;
                    case 3:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            Predicate p11_1 = cb.and(cb.gt(exp, -1));
                            Predicate p11_2 = cb.and(cb.lt(exp, 1));
                            p11 = cb.and(p11_1,p11_2);
                        }else{
                            p11 = cb.equal(exp, searchForm.getNumber());
                        }
                        break;
                    case 4:
                        p11 = cb.lt(exp, searchForm.getNumber());
                        break;
                    case 5:
                        p11 = cb.le(exp, searchForm.getNumber());
                        break;
                    default:break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p11);
                }
            }
        }
        Predicate p1 = cb.notEqual(root.get("status").as(String.class), Constants.INVALID);
        predicate.add(p1);
        Predicate[] pre = new Predicate[predicate.size()];
        query.where(predicate.toArray(pre));
        return query;
    }

    private CriteriaQuery<?> getSearchQueryForTube(Root<FrozenTubeListAllDataTableEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb, FrozenTubeListSearchForm searchForm) {
        List<Predicate> predicate = new ArrayList<>();
        query.distinct(true);
        Predicate p = cb.notEqual(root.get("status").as(String.class), Constants.INVALID);
        predicate.add(p);
//        Predicate pred = cb.equal(root.get("frozenTubeState").as(String.class), Constants.FROZEN_BOX_STOCKED);
//        predicate.add(pred);
        if (searchForm != null) {
            if (searchForm.getProjectCodeStr() != null && searchForm.getProjectCodeStr().length > 0) {
                CriteriaBuilder.In<String> in = cb.in(root.get("projectCode"));
                for (String id : searchForm.getProjectCodeStr()) {
                    in.value(id);
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
            if (searchForm.getSampleCodeStr() != null && searchForm.getSampleCodeStr().length > 0) {
                CriteriaBuilder.In<String> in = cb.in(root.get("sampleCode"));
                for (String sampleCode : searchForm.getSampleCodeStr()) {
                    in.value(sampleCode);
                }
                predicate.add(in);
            }
            if (searchForm.getFrozenTubeState() != null) {
                Predicate p3 = cb.equal(root.get("frozenTubeState").as(String.class), searchForm.getFrozenTubeState());
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
            if (searchForm.getTubeColumns() != null) {
                Predicate p5 = cb.equal(root.get("tubeColumns").as(String.class), searchForm.getTubeColumns());
                predicate.add(p5);
            }
            if (searchForm.getTubeRows() != null) {
                Predicate p5 = cb.equal(root.get("tubeRows").as(String.class), searchForm.getTubeRows());
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
            if (searchForm.getSex() != null) {
                Predicate p5 = cb.equal(root.get("sex").as(Long.class), searchForm.getSex());
                predicate.add(p5);
            }
            if (searchForm.getAge() != null) {
                Predicate p5 = cb.equal(root.get("age").as(Long.class), searchForm.getAge());
                predicate.add(p5);
            }
            if (searchForm.getDiseaseType() != null) {
                Predicate p5 = cb.equal(root.get("diseaseType").as(Long.class), searchForm.getDiseaseType());
                predicate.add(p5);
            }
            if (searchForm.getSampleUsedTimes() != null) {
                Predicate p5 = cb.equal(root.get("sampleUsedTimes").as(Long.class), searchForm.getSampleUsedTimes());
                predicate.add(p5);
            }
            if (searchForm.getFrozenTubeState() != null) {
                Predicate p5 = cb.equal(root.get("frozenTubeState").as(String.class), searchForm.getFrozenTubeState());
                predicate.add(p5);
            }
        }
        Predicate[] pre = new Predicate[predicate.size()];
        query.where(predicate.toArray(pre));
        return query;
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
                    default:break;
                }
                Predicate p11 = null;
                Expression exp = root.get(searchValue).as(Long.class);
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            p11 = cb.ge(exp, 1);
                        }else{
                            p11 = cb.gt(exp, searchForm.getNumber());
                        }
                        break;
                    case 2:
                        p11 = cb.ge(exp, searchForm.getNumber());
                        break;
                    case 3:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            Predicate p11_1 = cb.and(cb.gt(exp, -1));
                            Predicate p11_2 = cb.and(cb.lt(exp, 1));
                            p11 = cb.and(p11_1,p11_2);
                        }else{
                            p11 = cb.equal(exp, searchForm.getNumber());
                        }
                        break;
                    case 4:
                        p11 = cb.lt(exp, searchForm.getNumber());
                        break;
                    case 5:
                        p11 = cb.le(exp, searchForm.getNumber());
                        break;
                    default:break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p11);
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
                Predicate p7 = cb.equal(root.get("status").as(String.class), searchForm.getStatus());
                predicate.add(p7);
            }
            if (searchForm.getEquipmentId() != null) {
                Predicate p8 = cb.equal(root.get("equipmentId").as(Long.class), searchForm.getEquipmentId());
                predicate.add(p8);
            }
            if (searchForm.getAreaId() != null) {
                Predicate p9 = cb.equal(root.get("areaId").as(Long.class), searchForm.getAreaId());
                predicate.add(p9);
            }
            if (searchForm.getShelvesId() != null) {
                Predicate p10 = cb.equal(root.get("shelvesId").as(Long.class), searchForm.getShelvesId());
                predicate.add(p10);
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
                    default:break;
                }
                Predicate p11 = null;
                Expression exp = root.get(searchValue).as(Long.class);
                //1：大于，2：大于等于，3：等于，4：小于，5：小于等于
                switch (searchForm.getCompareType()) {
                    case 1:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            p11 = cb.ge(exp, 1);
                        }else{
                            p11 = cb.gt(exp, searchForm.getNumber());
                        }
                        break;
                    case 2:
                        p11 = cb.ge(exp, searchForm.getNumber());
                        break;
                    case 3:
                        if(searchForm.getNumber()!=null && searchForm.getNumber()==0){
                            Predicate p11_1 = cb.and(cb.gt(exp, -1));
                            Predicate p11_2 = cb.and(cb.lt(exp, 1));
                            p11 = cb.and(p11_1,p11_2);
                        }else{
                            p11 = cb.equal(exp, searchForm.getNumber());
                        }
                        break;
                    case 4:
                        p11 = cb.lt(exp, searchForm.getNumber());
                        break;
                    case 5:
                        p11 = cb.le(exp, searchForm.getNumber());
                        break;
                    default:break;
                }
                if (searchForm.getSpaceType() != null && searchForm.getCompareType() != null && searchForm.getNumber() != null) {
                    predicate.add(p11);
                }
            }
            Predicate[] pre = new Predicate[predicate.size()];
            query.where(predicate.toArray(pre));
        }
        return query;
    }
}
