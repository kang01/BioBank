package org.fwoxford.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.fwoxford.BioBankApp;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.dto.response.GeocoderSearchAddressResponse;
import org.fwoxford.service.dto.response.GeocoderSearchResponse;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.StockOutFrozenBoxPoisition;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.fwoxford.web.rest.util.GeocoderReaderUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the ImportSampleTest REST controller.
 *
 * @see ImportSampleDataTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
//@Transactional
public class ImportSampleDataTest {
    @Autowired
    SupportRackTypeRepository supportRackTypeRepository;
    @Autowired
    FrozenTubeTypeRepository frozenTubeTypeRepository;
    @Autowired
    FrozenBoxTypeRepository frozenBoxTypeRepository;
    @Autowired
    EquipmentGroupRepository equipmentGroupRepository;
    @Autowired
    EquipmentModleRepository equipmentModleRepository;
    @Autowired
    EquipmentMapper equipmentMapper;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    AreaRepository areaRepository;
    @Autowired
    AreaMapper areaMapper;
    @Autowired
    SupportRackMapper supportRackMapper;
    @Autowired
    SupportRackRepository supportRackRepository;
    @Autowired
    SampleTypeRepository sampleTypeRepository;
    @Autowired
    SampleClassificationRepository sampleClassificationRepository;
    @Autowired
    ProjectSampleClassRepository projectSampleClassRepository;
    @Autowired
    ProjectSampleClassMapper projectSampleClassMapper;
    @Autowired
    FrozenBoxRepository frozenBoxRepository;
    @Autowired
    FrozenBoxMapper frozenBoxMapper;
    @Autowired
    FrozenTubeRepository frozenTubeRepository;
    @Autowired
    FrozenTubeMapper frozenTubeMapper;
    @Autowired
    StockInRepository stockInRepository;
    @Autowired
    StockInMapper stockInMapper;
    @Autowired
    StockInBoxRepository stockInBoxRepository;
    @Autowired
    StockInBoxPositionRepository stockInBoxPositionRepository;
    @Autowired
    StockInTubeRepository stockInTubeRepository;
    @Autowired
    StockInTubeMapper stockInTubeMapper;
    @Autowired
    StockInTubeService stockInTubeService;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ProjectSiteRepository projectSiteRepository;
    @Autowired
    ProjectRelateRepository projectRelateRepository;
    @Autowired
    DelegateRepository delegateRepository;
    @Autowired
    BankUtil bankUtil;
    @Autowired
    StockOutApplyRepository stockOutApplyRepository;
    @Autowired
    StockOutRequirementRepository stockOutRequirementRepository;
    @Autowired
    StockOutReqFrozenTubeRepository stockOutReqFrozenTubeRepository;
    @Autowired
    StockOutPlanRepository stockOutPlanRepository;
    @Autowired
    StockOutPlanFrozenTubeRepository stockOutPlanFrozenTubeRepository;
    @Autowired
    StockOutTaskRepository stockOutTaskRepository;
    @Autowired
    StockOutTaskFrozenTubeRepository stockOutTaskFrozenTubeRepository;
    @Autowired
    StockOutHandoverRepository stockOutHandoverRepository;
    @Autowired
    StockOutHandoverDetailsRepository stockOutHandoverDetailsRepository;
    @Autowired
    PositionMoveRepository positionMoveRepository;
    @Autowired
    PositionMoveRecordRepository positionMoveRecordRepository;
    @Autowired
    StockOutApplyProjectRepository stockOutApplyProjectRepository;
    @Autowired
    StockOutFrozenBoxRepository stockOutFrozenBoxRepository;
    @Autowired
    StockOutBoxTubeRepository stockOutBoxTubeRepository;
    @Autowired
    StockOutBoxPositionRepository stockOutBoxPositionRepository;
    @Autowired
    PositionDestroyRecordRepository positionDestroyRecordRepository;
    @Autowired
    PositionDestroyRepository positionDestroyRepository;
    @Autowired
    FrozenBoxImportService frozenBoxImportService;
    @Autowired
    ProvinceRepository provinceRepository;
    @Autowired
    TranshipRepository transhipRepository;
    @Autowired
    TranshipBoxRepository transhipBoxRepository;
    @Autowired
    TranshipBoxPositionRepository transhipBoxPositionRepository;
    @Autowired
    TranshipTubeRepository transhipTubeRepository;
    @Autowired
    TranshipStockInRepository transhipStockInRepository;
    @Autowired
    CoordinateRepository coordinateRepository;
    @Autowired
    StockOutHandoverBoxRepository stockOutHandoverBoxRepository;

    private final Logger log = LoggerFactory.getLogger(ImportSampleDataTest.class);
    //创建项目
    @Test
    public void createProject() throws Exception {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from PROJECT";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            String projectCode = list.get(i).get("PROJECT_CODE").toString().trim();
            String projectName = list.get(i).get("PROJECT_NAME").toString().trim();
            String delegateName = list.get(i).get("DELEGATE").toString().trim();
            Delegate delegate = delegateRepository.findByDelegateName(delegateName);
            if (delegate == null) {
                delegate = new Delegate().delegateCode("D_0000" + (i + 1)).delegateName(delegateName).status("0001");
                delegateRepository.save(delegate);
            }
            Project project = projectRepository.findByProjectCode(projectCode);
            if (project == null) {
                project = new Project().projectCode(projectCode).projectName(projectName).status("0001").delegate(delegate);
                projectRepository.save(project);
            }
        }
    }
    //创建项目点
    @Test
    public void createProjectSite() throws Exception {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Long projectId = null;
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from LCC";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Project project = projectRepository.findByProjectCode("0037");
        for (int i = 0; i < list.size(); i++) {
            String projectSiteCode = list.get(i).get("LCC_ID").toString();
            ProjectSite projectSite = projectSiteRepository.findByProjectSiteCode(projectSiteCode);
            if (projectSite == null) {
                projectSite = new ProjectSite()
                    .projectSiteCode(list.get(i).get("LCC_ID").toString())
                    .projectSiteName(list.get(i).get("NAME").toString())
                    .area(list.get(i).get("AREA").toString())
                    .status("0001").detailedLocation(list.get(i).get("LOCATION") != null ? list.get(i).get("LOCATION").toString() : null)
                    .department(list.get(i).get("DEPARTMENT").toString())
                    .detailedAddress(list.get(i).get("ADDRESS").toString())
                    .zipCode(list.get(i).get("ZIP_CODE") != null ? list.get(i).get("ZIP_CODE").toString() : null)
                    .username1(list.get(i).get("USER_1").toString()).username2(list.get(i).get("USER_2").toString())
                    .phoneNumber1(list.get(i).get("PHONE_1").toString()).phoneNumber2(list.get(i).get("PHONE_2").toString());
                projectSiteRepository.save(projectSite);
                ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(project.getId(), projectSite.getId());
                if (projectRelate == null) {
                    projectRelate = new ProjectRelate().project(project).projectSite(projectSite).status("0001");
                    projectRelateRepository.save(projectRelate);
                }
            }
        }
    }
    //创建0038项目点
    @Test
    public void createProjectSiteForPeace3() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listAll = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from LCC_2";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
            String sqlForSelectAllLcc = "select * from LCC_1";// 预编译语句
            PreparedStatement pres = con.prepareStatement(sqlForSelectAllLcc);// 实例化预编译语句
            ResultSet result2 = pres.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetas2 = result2.getMetaData();
            Map<String, Object> mapForAll = null;
            while (result2.next()) {
                mapForAll = this.Result2Map(result2, rsMetas2);
                listAll.add(mapForAll);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Project project = projectRepository.findByProjectCode("0038");
        for (int i = 0; i < list.size(); i++) {
            Object projectSiteCodeObj = list.get(i).get("LCC_ID");
            if (projectSiteCodeObj == null) {
                continue;
            }
            String projectSiteCode = list.get(i).get("LCC_ID").toString();
            Object names = list.get(i).get("NAME");
            if (names == null) {
                continue;
            }
            String name = list.get(i).get("NAME").toString();
            if (projectSiteCode.length() < 4) {
                throw new BankServiceException("项目点异常:" + projectSiteCode);
            }
            String projectSiteId = projectSiteCode.substring(0, 4);
            String area = null;
            for (int j = 0; j < listAll.size(); j++) {
                String lcc = listAll.get(j).get("LCC_ID").toString();
                if (projectSiteId.equals(lcc)) {
                    area = listAll.get(j).get("PROVINCE").toString();
                }
            }
            ProjectSite projectSite = projectSiteRepository.findByProjectSiteCode(projectSiteCode);
            if (projectSite == null) {
                projectSite = new ProjectSite()
                    .projectSiteCode(list.get(i).get("LCC_ID").toString())
                    .projectSiteName(name)
                    .area(area)
                    .status("0001");
//                    .detailedLocation(list.get(i).get("LOCATION") != null ? list.get(i).get("LOCATION").toString() : null)
//                    .department(list.get(i).get("DEPARTMENT").toString())
//                    .detailedAddress(list.get(i).get("ADDRESS").toString())
//                    .zipCode(list.get(i).get("ZIP_CODE") != null ? list.get(i).get("ZIP_CODE").toString() : null)
//                    .username1(list.get(i).get("USER_1").toString()).username2(list.get(i).get("USER_2").toString())
//                    .phoneNumber1(list.get(i).get("PHONE_1").toString()).phoneNumber2(list.get(i).get("PHONE_2").toString());
                projectSiteRepository.save(projectSite);
                ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(project.getId(), projectSite.getId());
                if (projectRelate == null) {
                    projectRelate = new ProjectRelate().project(project).projectSite(projectSite).status("0001");
                    projectRelateRepository.save(projectRelate);
                }
            }
        }
    }
    //冻存架类型，固定值
    @Test
    public void createSupportRackType() throws Exception {
        //冻存架类型
        SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("S5x5");
        if (supportRackType == null) {
            supportRackType = new SupportRackType();
            supportRackType.setSupportRackTypeCode("S5x5");
            supportRackType.setSupportRackTypeName("冻存架1(5x5)");
            supportRackType.setSupportRackRows("5");
            supportRackType.setSupportRackColumns("5");
            supportRackType.setStatus("0001");
            supportRackType.setMemo("宽");
            supportRackTypeRepository.save(supportRackType);
            assertThat(supportRackType).isNotNull();
        }
        SupportRackType supportRackType55 = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
        if (supportRackType55 == null) {
            supportRackType55 = new SupportRackType();
            supportRackType55.setSupportRackTypeCode("B5x5");
            supportRackType55.setSupportRackTypeName("冻存架2(5x5)");
            supportRackType55.setSupportRackRows("5");
            supportRackType55.setSupportRackColumns("5");
            supportRackType55.setStatus("0001");
            supportRackType55.setMemo("窄");
            supportRackTypeRepository.save(supportRackType55);
        }
        //冻存架类型
        SupportRackType supportRackType1 = supportRackTypeRepository.findBySupportRackTypeCode("B5x4");
        if (supportRackType1 == null) {
            supportRackType1 = new SupportRackType();
            supportRackType1.setSupportRackTypeCode("B5x4");
            supportRackType1.setSupportRackTypeName("冻存架3(5x4)");
            supportRackType1.setSupportRackRows("5");
            supportRackType1.setSupportRackColumns("4");
            supportRackType1.setStatus("0001");
            supportRackType1.setMemo("宽");
            supportRackTypeRepository.save(supportRackType1);
            assertThat(supportRackType1).isNotNull();
        }
        //冻存架类型
        SupportRackType supportRackType2 = supportRackTypeRepository.findBySupportRackTypeCode("S5x4");
        if (supportRackType2 == null) {
            supportRackType2 = new SupportRackType();
            supportRackType2.setSupportRackTypeCode("S5x4");
            supportRackType2.setSupportRackTypeName("冻存架4(5x4)");
            supportRackType2.setSupportRackRows("5");
            supportRackType2.setSupportRackColumns("4");
            supportRackType2.setStatus("0001");
            supportRackType2.setMemo("窄");
            supportRackTypeRepository.save(supportRackType2);
        }
    }
    //冻存管类型--固定值
    @Test
    public void createFrozenTubeType() throws Exception {
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        if (frozenTubeType == null) {
            frozenTubeType = new FrozenTubeType().frozenTubeTypeCode("DCG").frozenTubeTypeName("冻存管(1ml)").sampleUsedTimesMost(10).frozenTubeVolumn(2.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.save(frozenTubeType);
            assertThat(frozenTubeType).isNotNull();
        }
        FrozenTubeType frozenTubeType1 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("2DDCG");
        if (frozenTubeType1 == null) {
            frozenTubeType1 = new FrozenTubeType().frozenTubeTypeCode("2DDCG").frozenTubeTypeName("2D冻存管(0.5ml)").sampleUsedTimesMost(10).frozenTubeVolumn(0.5).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.save(frozenTubeType1);
            assertThat(frozenTubeType1).isNotNull();
        }
        FrozenTubeType frozenTubeType2 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("6CXG");
        if (frozenTubeType2 == null) {
            frozenTubeType2 = new FrozenTubeType().frozenTubeTypeCode("6CXG").frozenTubeTypeName("采血管(6ml)").sampleUsedTimesMost(10).frozenTubeVolumn(6.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.save(frozenTubeType2);
            assertThat(frozenTubeType2).isNotNull();
        }
        FrozenTubeType frozenTubeType3 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("RNA");
        if (frozenTubeType3 == null) {
            frozenTubeType3 = new FrozenTubeType().frozenTubeTypeCode("RNA").frozenTubeTypeName("RNA管(9ml)").sampleUsedTimesMost(10).frozenTubeVolumn(9.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.save(frozenTubeType3);
            assertThat(frozenTubeType3).isNotNull();
        }
        FrozenTubeType frozenTubeType4 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("9CXG");
        if (frozenTubeType4 == null) {
            frozenTubeType4 = new FrozenTubeType().frozenTubeTypeCode("9CXG").frozenTubeTypeName("采血管(9ml)").sampleUsedTimesMost(10).frozenTubeVolumn(9.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.save(frozenTubeType4);
            assertThat(frozenTubeType4).isNotNull();
        }
    }
    //创建设备组--固定值
    @Test
    public void createEquipmentGroup() {
        //创建设备组
        EquipmentGroup equipmentGroup = equipmentGroupRepository.findByEquipmentGroupName("样本库");
        if (equipmentGroup == null) {
            equipmentGroup = new EquipmentGroup()
                .equipmentGroupName("样本库")
                .equipmentGroupManagerId(5L)
                .equipmentManagerName("钟慧")
                .equipmentGroupAddress("样本中心D座")
                .status("0001");
            equipmentGroup.setCreatedBy("admin");
            equipmentGroupRepository.save(equipmentGroup);
            assertThat(equipmentGroup).isNotNull();
        }
    }
    //设备类型
    @Test
    public void createEquipmentModel() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Long projectId = null;
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select  model,name,COUNT(1) as COUNT_OF_AREA from equipment_model group by model,name";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (Map<String, Object> map : list) {
            String code = map.get("MODEL").toString();
            String type = map.get("NAME").toString();
            int area = Integer.valueOf(map.get("COUNT_OF_AREA").toString());
            EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode(code);
            if (equipmentModle == null) {
                equipmentModle = new EquipmentModle().equipmentModelCode(code)
                    .equipmentModelName(code)
                    .equipmentType(type)
                    .areaNumber(area)
                    .shelveNumberInArea(0)
                    .status("0001");
                equipmentModleRepository.save(equipmentModle);
            }
        }
    }
    //导入设备
    @Test
    public void createEquipment() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> modelList = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from equipment ";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }

            String sql = "select  * from equipment_model";// 预编译语句
            PreparedStatement pres2 = con.prepareStatement(sql);// 实例化预编译语句
            ResultSet results2 = pres2.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetas2 = results2.getMetaData();
            Map<String, Object> maps = null;
            while (results2.next()) {
                maps = this.Result2Map(results2, rsMetas2);
                modelList.add(maps);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, List<Map<String, Object>>> modelMap = new HashMap<>();
        for (Map<String, Object> map : modelList) {
            String model = map.get("MODEL").toString();
            List<Map<String, Object>> alist = new ArrayList();
            if (modelMap.get(model) == null || modelMap.get(model).size() == 0) {
                alist.add(map);
                modelMap.put(model, alist);
            } else {
                alist = modelMap.get(model);
                alist.add(map);
                modelMap.put(model, alist);
            }
        }
        for (Map<String, Object> map : list) {
            String code = map.get("CODE").toString();
            String model = map.get("MODEL").toString();
            String status = map.get("STATUS").toString();
            String name = map.get("NAME").toString();
            String brand = map.get("BRAND").toString();
            String belongs = map.get("BELONGS").toString();
            String users = map.get("USERS").toString();
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(status);
            arrayList.add(name);
            arrayList.add(brand);
            arrayList.add(belongs);
            arrayList.add(users);
            EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode(model);
            if (equipmentModle == null) {
                throw new BankServiceException("型号不存在！");
            }
            Equipment entity = equipmentRepository.findOneByEquipmentCode(code);
            if (entity == null) {
                entity = new Equipment().equipmentAddress(belongs).equipmentCode(code)
                    .equipmentGroup(equipmentMapper.equipmentGroupFromId(1L))
                    .equipmentModle(equipmentModle).temperature(0).ampoulesMax(0).ampoulesMin(0).status("0001").memo(String.join(",", arrayList));
            }
            //设备
            equipmentRepository.save(entity);
            //查询区域
            List<Map<String, Object>> areaList = modelMap.get(model);
            for (Map<String, Object> key : areaList) {
                String areaCode = key.get("AREA").toString();
                int count = Integer.valueOf(key.get("COUNT_OF_SHELF").toString());
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(count).equipmentCode(entity.getEquipmentCode())
                        .status("0001");
                    areaRepository.save(area);
                }
                List<String> supportCodeStr = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    String newString = String.format("%0" + 2 + "d", i + 1);
                    supportCodeStr.add("R" + newString);
                }
                SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("S5x5");
                for (String supportCode : supportCodeStr) {
                    //冻存架
                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                    if (supportRack == null) {
                        supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                            .area(supportRackMapper.areaFromId(area.getId()));
                        supportRackRepository.save(supportRack);
                    }
                }
            }
        }
    }
    //创建冻存盒类型--固定值
    @Test
    public void createFrozenBoxType() throws Exception {
        //冻存盒类型
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        if (frozenBoxType == null) {
            frozenBoxType = new FrozenBoxType().frozenBoxTypeCode("DCH")
                .frozenBoxTypeName("冻存盒(10*10)").frozenBoxTypeColumns("10").frozenBoxTypeRows("10").status("0001");
            frozenBoxType.setCreatedBy("admin");
            frozenBoxTypeRepository.save(frozenBoxType);
        }
        //冻存盒类型
        FrozenBoxType frozenBoxType1 = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DJH");
        if (frozenBoxType1 == null) {
            frozenBoxType1 = new FrozenBoxType().frozenBoxTypeCode("DJH")
                .frozenBoxTypeName("大橘盒(10*10)").frozenBoxTypeColumns("10").frozenBoxTypeRows("10").status("0001");
            frozenBoxTypeRepository.save(frozenBoxType1);
        }
        //冻存盒类型
        FrozenBoxType frozenBoxType2 = frozenBoxTypeRepository.findByFrozenBoxTypeCode("96KB");
        if (frozenBoxType2 == null) {
            frozenBoxType2 = new FrozenBoxType().frozenBoxTypeCode("96KB")
                .frozenBoxTypeName("96孔板(8*12)").frozenBoxTypeColumns("12").frozenBoxTypeRows("8").status("0001");
            frozenBoxTypeRepository.save(frozenBoxType2);
        }
    }
    //创建样本类型
    @Test
    public void createSampleType() throws Exception {

        SampleType sampleType1 = sampleTypeRepository.findBySampleTypeCode("A");
        if (sampleType1 == null) {
            sampleType1 = new SampleType().sampleTypeCode("A").sampleTypeName("血浆")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(240,224,255)");
            sampleTypeRepository.save(sampleType1);
            assertThat(sampleType1).isNotNull();
        }
        SampleType sampleType2 = sampleTypeRepository.findBySampleTypeCode("W");
        if (sampleType2 == null) {
            sampleType2 = new SampleType().sampleTypeCode("W").sampleTypeName("白细胞")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,255,255)");
            sampleTypeRepository.save(sampleType2);
            assertThat(sampleType2).isNotNull();
        }
        SampleType sampleType3 = sampleTypeRepository.findBySampleTypeCode("F");
        if (sampleType3 == null) {
            sampleType3 = new SampleType().sampleTypeCode("F").sampleTypeName("血清")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,179,179)");
            sampleTypeRepository.save(sampleType3);
            assertThat(sampleType3).isNotNull();
        }
        SampleType sampleType4 = sampleTypeRepository.findBySampleTypeCode("E");
        if (sampleType4 == null) {
            sampleType4 = new SampleType().sampleTypeCode("E").sampleTypeName("尿")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,255,179)");
            sampleTypeRepository.save(sampleType4);
            assertThat(sampleType4).isNotNull();
        }
        SampleType sampleType5 = sampleTypeRepository.findBySampleTypeCode("R");
        if (sampleType5 == null) {
            sampleType5 = new SampleType().sampleTypeCode("R").sampleTypeName("红细胞")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(236,236,236)");
            sampleTypeRepository.save(sampleType5);
            assertThat(sampleType5).isNotNull();
        }
        SampleType sampleType6 = sampleTypeRepository.findBySampleTypeCode("RNA");
        if (sampleType6 == null) {
            sampleType6 = new SampleType().sampleTypeCode("RNA").sampleTypeName("RNA")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,220,165)");
            sampleTypeRepository.save(sampleType6);
            assertThat(sampleType6).isNotNull();
        }
    }
    //0037的项目配置样本分类
    @Test
    public void createSampleTypeFor0037() throws Exception {
        Project project = projectRepository.findByProjectCode("0037");
        SampleType sampleType_A = sampleTypeRepository.findBySampleTypeCode("A");
        SampleType sampleType_R = sampleTypeRepository.findBySampleTypeCode("R");
        SampleType sampleType_E = sampleTypeRepository.findBySampleTypeCode("E");
        SampleType sampleType_F = sampleTypeRepository.findBySampleTypeCode("F");
        SampleType sampleType_W = sampleTypeRepository.findBySampleTypeCode("W");
        SampleType sampleType_RNA = sampleTypeRepository.findBySampleTypeCode("RNA");
        for (int i = 1; i <= 10; i++) {
            String frontColor = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get(String.format("%0" + 2 + "d", i)).toString();
            SampleClassification sampleClassification01A = new SampleClassification()
                .sampleClassificationCode(String.format("%0" + 2 + "d", i))
                .sampleClassificationName(Constants.SAMPLE_TYPE_MAP.get(String.format("%0" + 2 + "d", i)))
                .status("0001")
                .backColor(frontColor)
                .frontColor("black");
            sampleClassificationRepository.save(sampleClassification01A);
            SampleType sampleType = null;
            switch (i) {
                case 1:
                    sampleType = sampleType_A;
                    break;
                case 2:
                    sampleType = sampleType_A;
                    break;
                case 3:
                    sampleType = sampleType_W;
                    break;
                case 4:
                    sampleType = sampleType_R;
                    break;
                case 5:
                    sampleType = sampleType_A;
                    break;
                case 6:
                    sampleType = sampleType_A;
                    break;
                case 7:
                    sampleType = sampleType_F;
                    break;
                case 8:
                    sampleType = sampleType_F;
                    break;
                case 9:
                    sampleType = sampleType_E;
                    break;
                case 10:
                    sampleType = sampleType_E;
                    break;
                case 11:
                    sampleType = sampleType_RNA;
                    break;
                default:
                    break;
            }
            ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_W.getId(), sampleClassification01A.getId());
            if (projectSampleClass == null) {
                projectSampleClass = new ProjectSampleClass()
                    .project(project).projectCode(project.getProjectCode())
                    .sampleClassification(sampleClassification01A)
                    .sampleType(sampleType)
                    .columnsNumber(null).sampleClassificationCode(String.format("%0" + 2 + "d", i)).sampleClassificationName(Constants.SAMPLE_TYPE_MAP.get(String.format("%0" + 2 + "d", i)))
                    .status("0001");
                projectSampleClassRepository.save(projectSampleClass);
            }
        }


        SampleType sampleType7 = sampleTypeRepository.findBySampleTypeCode("98");
        List<SampleClassification> sampleClassifications = sampleClassificationRepository.findAll();
        if (sampleType7 == null) {
            sampleType7 = new SampleType().sampleTypeCode("98").sampleTypeName("98")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(169,241,253)");
            sampleTypeRepository.save(sampleType7);
            assertThat(sampleType7).isNotNull();
        }
        SampleType sampleType8 = sampleTypeRepository.findBySampleTypeCode("99");
        if (sampleType8 == null) {
            sampleType8 = new SampleType().sampleTypeCode("99").sampleTypeName("99")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(169,241,253)");
            sampleTypeRepository.save(sampleType8);
            assertThat(sampleType8).isNotNull();
        }
        for (SampleClassification s : sampleClassifications) {
            if (s.getSampleClassificationCode().equals("11")) {
                continue;
            }
            ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType8.getId(), s.getId());
            ;
            String columnNumber = Constants.COLUMNNUMBER_MAP.get(s.getSampleClassificationCode());
            if (projectSampleClass == null) {
                projectSampleClass = new ProjectSampleClass()
                    .project(project).projectCode(project.getProjectCode()).sampleType(sampleType8).sampleClassification(s)
                    .columnsNumber(columnNumber).sampleClassificationCode(s.getSampleClassificationCode()).sampleClassificationName(s.getSampleClassificationName())
                    .status("0001");
                projectSampleClassRepository.save(projectSampleClass);
                assertThat(projectSampleClass).isNotNull();
            }
        }

        SampleType sampleType97 = sampleTypeRepository.findBySampleTypeCode("97");
        if (sampleType97 == null) {
            sampleType97 = new SampleType().sampleTypeCode("97").sampleTypeName("97")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(255, 0, 0)");
            sampleTypeRepository.save(sampleType97);
            assertThat(sampleType97).isNotNull();
        }

    }
    //将返回结果转成List
    private Map<String, Object> Result2Map(ResultSet rs, ResultSetMetaData meta) throws SQLException {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            map.put(meta.getColumnName(i), rs.getObject(meta.getColumnName(i)));
        }
        return map;
    }
    //创建冻存盒位置
    @Test
    public void createPositionInShelf() {
        String[] b = new String[]{"A", "B", "C", "D", "E"};  //定义数组b
        boolean flag = false;
        for (int j = 0; j < b.length; j++) {
            for (int i = 1; i <= 5; i++) {
                String bb = b[j] + i;
                if (bb.equals("A1")) {//若库存里有，则继续取下一个，若没有，就取当前的这个
                    continue;
                } else {
                    flag = true;
                    System.out.print(bb);
                    break;
                }
            }
            if (flag) {
                break;
            }
        }
    }
    //百度API获取地址测试方式
    @Test
    public void getLocation() throws IOException, JSONException {
        //这里调用百度的ip定位api服务 详见 http://api.map.baidu.com/lbsapi/cloud/ip-location-api.htm
        JSONObject json = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?address=", "陕西省西安");
        ObjectMapper objectMapper = new ObjectMapper();
        GeocoderSearchResponse response = objectMapper.readValue(json.getString("GeocoderSearchResponse"), GeocoderSearchResponse.class);
        BigDecimal lat = response.getResult().getLocation().getLat();
        BigDecimal lng = response.getResult().getLocation().getLng();
        JSONObject jsonForAddress = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?location=", lat + "," + lng);
        ObjectMapper objectMapperForAddress = new ObjectMapper();
        GeocoderSearchAddressResponse res = objectMapperForAddress.readValue(jsonForAddress.getString("GeocoderSearchResponse"), GeocoderSearchAddressResponse.class);
        System.out.println(jsonForAddress.toString());
    }
    //为所有项目点获取位置信息
    @Test
    public void importLocationForProjectSite() throws IOException, JSONException {
        List<ProjectSite> projectSites = projectSiteRepository.findAll();
        for (ProjectSite p : projectSites) {
            String projectSiteCode = p.getProjectSiteCode();
            String projectSiteId = null;
            if (projectSiteCode.length() < 4) {
                projectSiteId = projectSiteCode;
            } else {
                projectSiteId = projectSiteCode.substring(0, 4);
            }
            String name = p.getDetailedAddress() == null ? p.getProjectSiteName() : p.getDetailedAddress();
            if (StringUtils.isEmpty(name)) {
                throw new BankServiceException("项目点名称异常" + p.toString());
            }
            JSONObject json = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?address=", name.replaceAll("\r|\n", "").replaceAll(" +", ""));
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.print(json.toString());
            if (json.length() == 0 || json == null || json.getString("GeocoderSearchResponse") == null) {

            } else {
                if (json.getJSONObject("GeocoderSearchResponse").get("status").equals("OK")) {
                    GeocoderSearchResponse response = objectMapper.readValue(json.getString("GeocoderSearchResponse"), GeocoderSearchResponse.class);
                    if (response != null && response.getStatus().equals("OK")) {
                        BigDecimal lat = response.getResult().getLocation().getLat();
                        BigDecimal lng = response.getResult().getLocation().getLng();
                        String address = lat + "," + lng;
                        JSONObject jsonForAddress = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?location=", address);
                        ObjectMapper objectMapperForAddress = new ObjectMapper();
                        if (jsonForAddress.length() > 0 && jsonForAddress.getJSONObject("GeocoderSearchResponse").get("status").equals("OK")) {
                            GeocoderSearchAddressResponse res = objectMapperForAddress.readValue(jsonForAddress.getString("GeocoderSearchResponse"), GeocoderSearchAddressResponse.class);
                            if (res != null && res.getStatus().equals("OK")) {
                                p.setProjectSiteId(projectSiteId);
                                p.setLatitude(lat);
                                p.setLongitude(lng);
                                p.setProvince(res.getResult().getAddressComponent().getProvince());
                                p.setCity(res.getResult().getAddressComponent().getCity());
                                p.setDistrict(res.getResult().getAddressComponent().getDistrict());
                                p.setStreet(res.getResult().getAddressComponent().getStreet());
                            }
                            projectSiteRepository.save(p);
                        }
                    }
                }
            }
            p.setProjectSiteId(projectSiteId);
            projectSiteRepository.save(p);
        }
    }
    //为0029项目配置样本类型和样本分类
    @Test
    public void createSampleTypeAndClassFor0029() {
        Project project = projectRepository.findByProjectCode("0029");
        if (project == null) {
            throw new BankServiceException("项目查询失败！");
        }
        SampleType sampleType_A = sampleTypeRepository.findBySampleTypeCode("A");
        SampleType sampleType_R = sampleTypeRepository.findBySampleTypeCode("R");
        SampleType sampleType_E = sampleTypeRepository.findBySampleTypeCode("E");
        SampleType sampleType_W = sampleTypeRepository.findBySampleTypeCode("W");
        String frontColor = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("03").toString();
        SampleClassification sampleClassification08W = new SampleClassification()
            .sampleClassificationCode("08")
            .sampleClassificationName("W-白细胞")
            .status("0001")
            .backColor(frontColor)
            .frontColor("black");
        sampleClassificationRepository.save(sampleClassification08W);
        ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_W.getId(), sampleClassification08W.getId());
        if (projectSampleClass == null) {
            projectSampleClass = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification08W)
                .sampleType(sampleType_W)
                .columnsNumber(null).sampleClassificationCode("08").sampleClassificationName("W-白细胞")
                .status("0001");
            projectSampleClassRepository.save(projectSampleClass);
        }
        String frontColorE = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("09").toString();
        SampleClassification sampleClassification05E = new SampleClassification()
            .sampleClassificationCode("05")
            .sampleClassificationName("E-尿")
            .status("0001")
            .backColor(frontColorE)
            .frontColor("black");
        sampleClassificationRepository.save(sampleClassification05E);
        ProjectSampleClass projectSampleClass_E = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_E.getId(), sampleClassification05E.getId());
        if (projectSampleClass_E == null) {
            projectSampleClass_E = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification05E)
                .sampleType(sampleType_E)
                .columnsNumber(null).sampleClassificationCode("05").sampleClassificationName("E-尿")
                .status("0001");
            projectSampleClassRepository.save(projectSampleClass_E);
        }
        String frontColorR = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("04").toString();
        SampleClassification sampleClassification09R = new SampleClassification()
            .sampleClassificationCode("09")
            .sampleClassificationName("R-红细胞")
            .status("0001")
            .backColor(frontColorR)
            .frontColor("black");
        sampleClassificationRepository.save(sampleClassification09R);
        ProjectSampleClass projectSampleClass_R = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_R.getId(), sampleClassification09R.getId());
        if (projectSampleClass_R == null) {
            projectSampleClass_R = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification09R)
                .sampleType(sampleType_R)
                .columnsNumber(null).sampleClassificationCode("09").sampleClassificationName("R-红细胞")
                .status("0001");
            projectSampleClassRepository.save(projectSampleClass_R);
        }
        String frontColorA = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("01").toString();
        SampleClassification sampleClassification01A = new SampleClassification()
            .sampleClassificationCode("01")
            .sampleClassificationName("A-血浆")
            .status("0001")
            .backColor(frontColorA)
            .frontColor("black");
        sampleClassificationRepository.save(sampleClassification01A);
        ProjectSampleClass projectSampleClass_A = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_A.getId(), sampleClassification01A.getId());
        if (projectSampleClass_A == null) {
            projectSampleClass_A = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification01A)
                .sampleType(sampleType_A)
                .columnsNumber(null).sampleClassificationCode("01").sampleClassificationName("A-血浆")
                .status("0001");
            projectSampleClassRepository.save(projectSampleClass_A);
        }
    }
    //为0038项目配置样本类型和样本分类
    @Test
    public void createSampleTypeAndClassFor0038() {
        Project project = projectRepository.findByProjectCode("0038");
        if (project == null) {
            throw new BankServiceException("项目查询失败！");
        }
        SampleType sampleType_A = sampleTypeRepository.findBySampleTypeCode("A");
        SampleType sampleType_R = sampleTypeRepository.findBySampleTypeCode("R");
        SampleType sampleType_E = sampleTypeRepository.findBySampleTypeCode("E");
        SampleType sampleType_W = sampleTypeRepository.findBySampleTypeCode("W");
        String frontColor = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("03").toString();
        SampleClassification sampleClassification08W = new SampleClassification()
            .sampleClassificationCode("08")
            .sampleClassificationName("EDTA抗凝白细胞")
            .status("0001")
            .backColor(frontColor)
            .frontColor("black");
        sampleClassificationRepository.save(sampleClassification08W);
        ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_W.getId(), sampleClassification08W.getId());
        if (projectSampleClass == null) {
            projectSampleClass = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification08W)
                .sampleType(sampleType_W)
                .columnsNumber(null).sampleClassificationCode("08").sampleClassificationName("EDTA抗凝白细胞")
                .status("0001");
            projectSampleClassRepository.save(projectSampleClass);
        }
        String frontColorE = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("09").toString();
        SampleClassification sampleClassification05E = new SampleClassification()
            .sampleClassificationCode("05")
            .sampleClassificationName("尿")
            .status("0001")
            .backColor(frontColorE)
            .frontColor("black");
        sampleClassificationRepository.save(sampleClassification05E);
        ProjectSampleClass projectSampleClass_E = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_E.getId(), sampleClassification05E.getId());
        if (projectSampleClass_E == null) {
            projectSampleClass_E = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification05E)
                .sampleType(sampleType_E)
                .columnsNumber(null).sampleClassificationCode("05").sampleClassificationName("尿")
                .status("0001");
            projectSampleClassRepository.save(projectSampleClass_E);
        }
        String frontColorR = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("04").toString();
        SampleClassification sampleClassification09R = new SampleClassification()
            .sampleClassificationCode("09")
            .sampleClassificationName("红细胞")
            .status("0001")
            .backColor(frontColorR)
            .frontColor("black");
        sampleClassificationRepository.save(sampleClassification09R);
        ProjectSampleClass projectSampleClass_R = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_R.getId(), sampleClassification09R.getId());
        if (projectSampleClass_R == null) {
            projectSampleClass_R = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification09R)
                .sampleType(sampleType_R)
                .columnsNumber(null).sampleClassificationCode("09").sampleClassificationName("红细胞")
                .status("0001");
            projectSampleClassRepository.save(projectSampleClass_R);
        }
        String frontColorA = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("01").toString();
        SampleClassification sampleClassification01A = new SampleClassification()
            .sampleClassificationCode("01")
            .sampleClassificationName("EDTA抗凝血浆")
            .status("0001")
            .backColor(frontColorA)
            .frontColor("black");
        sampleClassificationRepository.save(sampleClassification01A);
        ProjectSampleClass projectSampleClass_A = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_A.getId(), sampleClassification01A.getId());
        if (projectSampleClass_A == null) {
            projectSampleClass_A = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification01A)
                .sampleType(sampleType_A)
                .columnsNumber(null).sampleClassificationCode("01").sampleClassificationName("EDTA抗凝血浆")
                .status("0001");
            projectSampleClassRepository.save(projectSampleClass_A);
        }
    }
    //创建0029项目的项目点
    @Test
    public void createProjectSiteForPeace2() throws Exception {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Long projectId = null;
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from LCC_170823";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Project project = projectRepository.findByProjectCode("0029");
        for (int i = 0; i < list.size(); i++) {
            String projectSiteCode = list.get(i).get("LCC_ID").toString();
            ProjectSite projectSite = projectSiteRepository.findByProjectSiteCode(projectSiteCode);
            if (projectSite == null) {
                projectSite = new ProjectSite()
                    .projectSiteCode(list.get(i).get("LCC_ID").toString())
                    .projectSiteName(list.get(i).get("NAME").toString())
                    .area(list.get(i).get("AREA").toString())
                    .status("0001").detailedLocation(list.get(i).get("LOCATION") != null ? list.get(i).get("LOCATION").toString() : null)
//                    .department(list.get(i).get("DEPARTMENT").toString())
                    .detailedAddress(list.get(i).get("ADDRESS") != null ? list.get(i).get("ADDRESS").toString() : null)
                    .zipCode(list.get(i).get("ZIP_CODE") != null ? list.get(i).get("ZIP_CODE").toString() : null)
                    .username1(list.get(i).get("USER_1").toString()).username2(list.get(i).get("USER_2").toString())
                    .phoneNumber1(list.get(i).get("PHONE_1").toString()).phoneNumber2(list.get(i).get("PHONE_2").toString());
                projectSiteRepository.save(projectSite);
                ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(project.getId(), projectSite.getId());
                if (projectRelate == null) {
                    projectRelate = new ProjectRelate().project(project).projectSite(projectSite).status("0001");
                    projectRelateRepository.save(projectRelate);
                }
            }
        }
    }
    //创建0038项目的项目点
    @Test
    public void updateProjectSiteForPeace3() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listAll = new ArrayList<Map<String, Object>>();
        Long projectId = null;
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from LCC_0038";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
            String sqlForSelectAllLcc = "select * from LCC_0038";// 预编译语句
            PreparedStatement pres = con.prepareStatement(sqlForSelectAllLcc);// 实例化预编译语句
            ResultSet result2 = pres.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetas2 = result2.getMetaData();
            Map<String, Object> mapForAll = null;
            while (result2.next()) {
                mapForAll = this.Result2Map(result2, rsMetas2);
                listAll.add(mapForAll);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Project project = projectRepository.findByProjectCode("0038");
        List<ProjectSite> projectSiteList = projectSiteRepository.findAllProjectSitesByProjectId(project.getId());
        for (int i = 0; i < list.size(); i++) {
            Object projectSiteCodeObj = list.get(i).get("LCC_ID");
            if (projectSiteCodeObj == null) {
                continue;
            }
            String projectSiteCode = list.get(i).get("LCC_ID").toString();
            Object names = list.get(i).get("COMPANY");
            if (names == null) {
                continue;
            }
            String name = list.get(i).get("COMPANY").toString();
            if (projectSiteCode.length() < 4) {
                throw new BankServiceException("项目点异常:" + projectSiteCode);
            }
            String projectSiteId = projectSiteCode.substring(0, 4);
            String area = null;
            for (int j = 0; j < listAll.size(); j++) {
                String lcc = listAll.get(j).get("LCC_ID").toString();
                if (projectSiteId.equals(lcc)) {
                    area = listAll.get(j).get("PROVINCE").toString();
                }
            }
            ProjectSite projectSite = new ProjectSite();
            for (ProjectSite p : projectSiteList) {
                if (p.getProjectSiteCode().equals(projectSiteCode)) {
                    projectSite = p;
                }
            }

            projectSite = projectSite.projectSiteCode(list.get(i).get("LCC_ID").toString())
                .projectSiteName(name)
                .area(area)
                .status("0001").memo(list.get(i).get("MEMO") != null ? list.get(i).get("MEMO").toString() : null)
                .detailedLocation(list.get(i).get("POST_ADDRESS") != null ? list.get(i).get("POST_ADDRESS").toString() : null)
                .department(list.get(i).get("ADDRESS") != null ? list.get(i).get("ADDRESS").toString() : null)
                .detailedAddress(list.get(i).get("POST_ADDRESS") != null ? list.get(i).get("POST_ADDRESS").toString() : null)
                .zipCode(list.get(i).get("ZIP_CODE") != null ? list.get(i).get("ZIP_CODE").toString() : null)
                .username1(list.get(i).get("USER_1") != null ? list.get(i).get("USER_1").toString() : null)
                .username2(list.get(i).get("USER_2") != null ? list.get(i).get("USER_2").toString() : null)
                .phoneNumber1(list.get(i).get("PHONE_1") != null ? list.get(i).get("PHONE_1").toString() : null)
                .phoneNumber2(list.get(i).get("PHONE_2") != null ? list.get(i).get("PHONE_2").toString() : null);
            projectSiteRepository.save(projectSite);
            ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(project.getId(), projectSite.getId());
            if (projectRelate == null) {
                projectRelate = new ProjectRelate().project(project).projectSite(projectSite).status("0001");
                projectRelateRepository.save(projectRelate);
            }
        }
    }
    //导入项目点的坐标信息
    @Test
    public void createCoordinate() throws IOException, JSONException {
        List<Object[]> projectSites = projectSiteRepository.findAllGroupByProvinceAndCity();
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        for (Object[] projectSite : projectSites) {
            String province = projectSite[0] != null ? projectSite[0].toString() : null;
            String area = projectSite[1] != null ? projectSite[1].toString() : null;
            if (province == null) {
                continue;
            }
            Coordinate coordinate = coordinateRepository.findByProvinceAndCity(province, area);
            if (coordinate == null) {
                coordinate = new Coordinate();
            }

            coordinate.setProvince(province);
            coordinate.setCity(area);
            coordinate.setStatus(Constants.VALID);
            String name = province + " " + area;
            JSONObject json = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?address=", name.replaceAll("\r|\n", "").replaceAll(" +", ""));
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.print(json.toString());
            if (json.length() == 0 || json == null || json.getString("GeocoderSearchResponse") == null) {
                throw new BankServiceException("获取坐标失败！");
            } else {
                if (json.getJSONObject("GeocoderSearchResponse").get("status").equals("OK")) {
                    GeocoderSearchResponse response = objectMapper.readValue(json.getString("GeocoderSearchResponse"), GeocoderSearchResponse.class);
                    if (response != null && response.getStatus().equals("OK")) {
                        BigDecimal lat = response.getResult().getLocation().getLat();
                        BigDecimal lng = response.getResult().getLocation().getLng();
                        coordinate.setLatitude(lat);
                        coordinate.setLongitude(lng);
                    }
                }
            }
            coordinateRepository.save(coordinate);
        }

    }
    //导入省信息
    @Test
    public void createProvince() throws IOException, JSONException {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from PROVINCE";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        List<Province> alist = new ArrayList<Province>();
        for (int i = 0; i < list.size(); i++) {
            Province province = new Province();
            String name = list.get(i).get("NAME").toString();
            String code = list.get(i).get("CODE").toString();
            String city = list.get(i).get("CITY").toString();
            province.setName(name);
            province.setCode(code);
            province.setStatus(Constants.VALID);
            JSONObject json = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?address=", (name + city).replaceAll("\r|\n", "").replaceAll(" +", ""));
            ObjectMapper objectMapper = new ObjectMapper();
            System.out.print(json.toString());
            if (json.length() == 0 || json == null || json.getString("GeocoderSearchResponse") == null) {
                throw new BankServiceException("获取坐标失败！");
            } else {
                if (json.getJSONObject("GeocoderSearchResponse").get("status").equals("OK")) {
                    GeocoderSearchResponse response = objectMapper.readValue(json.getString("GeocoderSearchResponse"), GeocoderSearchResponse.class);
                    if (response != null && response.getStatus().equals("OK")) {
                        BigDecimal lat = response.getResult().getLocation().getLat();
                        BigDecimal lng = response.getResult().getLocation().getLng();
                        province.setLongitude(lng);
                        province.setLatitude(lat);
                        provinceRepository.save(province);
                    }
                }
            }
        }

    }
    //创建特殊位置,S99.R99
    @Test
    public void createEquipmentSpecial() {

        List<String> equipmentCode = new ArrayList<String>() {{
            add("F2-07");
            add("F2-08");
            add("F2-11");
            add("F2-12");
            add("F2-13");
            add("F2-14");
            add("F2-15");
            add("R4-01");
            add("R4-02");
            add("R4-03");
            add("F2-16");
            add("F2-17");
            add("F2-18");
            add("F2-19");
            add("F2-20");
            add("F1-10");
            add("F3-08");
            add("F1-08");
            add("F4-06");
            add("F2-06");
            add("F3-05");
            add("F2-03");
            add("F2-04");
            add("F2-02");
            add("F2-01");
            add("F1-03");
            add("F1-04");
        }};

        for (int i = 0; i < equipmentCode.size(); i++) {
            Equipment equipment = equipmentRepository.findOneByEquipmentCode(equipmentCode.get(i));
            if (equipment == null) {
                throw new BankServiceException("设备未配置", equipmentCode.get(i));
            }
            String[] areaCodeStr = new String[]{"S01", "S02", "S03", "S04", "S99"};
            for (String areaCode : areaCodeStr) {

                int freezeFrameNumber = 0;
                if (areaCode.equals("S01") || areaCode.equals("S02") || areaCode.equals("S03") || areaCode.equals("S04")) {
                    freezeFrameNumber = 24;
                } else if (areaCode.equals("S99")) {
                    freezeFrameNumber = 28;
                }
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, equipment.getId());
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(equipment.getId())).freezeFrameNumber(freezeFrameNumber).equipmentCode(equipment.getEquipmentCode())
                        .status("0001");
                    areaRepository.save(area);
                }
                String[] supportCodeStr = new String[]{};
                if (areaCode.equals("S01") || areaCode.equals("S02") || areaCode.equals("S03") || areaCode.equals("S04")) {
                    supportCodeStr = new String[]{"R01", "R02", "R03", "R04", "R05", "R06", "R07", "R08", "R09", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R99"};
                } else if (areaCode.equals("S99")) {
                    supportCodeStr = new String[]{"R01", "R02", "R03", "R04", "R05", "R06", "R07", "R08", "R09", "R10", "R11", "R12", "R13", "R14", "R15", "R16", "R17", "R18", "R19", "R20", "R21", "R22", "R23", "R24", "R25", "R26", "R27", "R28", "R99"};
                }

                for (String supportCode : supportCodeStr) {
                    //冻存架
                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    if (supportRack == null) {
                        supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                            .area(supportRackMapper.areaFromId(area.getId()));
                        supportRackRepository.save(supportRack);
                    }
                }
            }
        }
    }

    public Date strToDate(String strDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Date date = format.parse(strDate);
        return date;
    }

    public Date strToDate2(String strDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse(strDate);
        return date;
    }

    public Date strToDate3(String strDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = format.parse(strDate);
        return date;
    }

    ///////////////////////////////////////////////////0029导入开始///////////////////////////////////////

    //导入第一步，导入基础数据
    @Test
    public void importBaseData_first() throws Exception {
        this.createProject();
        this.createProjectSite();//peace5项目点
        this.createProjectSiteForPeace3();
        this.updateProjectSiteForPeace3();
        this.createProjectSiteForPeace2();
        this.createSupportRackType();
        this.createEquipmentGroup();
        this.createEquipmentModel();
        this.createEquipment();
        this.createFrozenTubeType();
        this.createFrozenBoxType();
        this.createSampleType();
        this.createSampleTypeFor0037();
        this.importLocationForProjectSite();
        this.createSampleTypeAndClassFor0038();
        this.createSampleTypeAndClassFor0029();
        this.createEquipmentSpecial();
        this.createCoordinate();
        this.createProvince();
    }
    //导入第二步，导入首次入库记录
    @Test
    public void importForPeace2() throws Exception {
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        if (frozenTubeType == null) {
            throw new BankServiceException("冻存管类型导入失败！");
        }

        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode("A");
        Project project = projectRepository.findByProjectCode("0029");

        importBoxForPeace2("HE_A_0908", "A", frozenTubeType, frozenBoxType, null, projectSiteList, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("R");
        importBoxForPeace2("HE_R_0908", "R", frozenTubeType, frozenBoxType, null, projectSiteList, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("E");
        importBoxForPeace2("HE_E_0908", "E", frozenTubeType, frozenBoxType, null, projectSiteList, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("W");
        importBoxForPeace2("HE_W_0908", "W", frozenTubeType, frozenBoxType, null, projectSiteList, sampleType, project);
    }
    //导入第三步，导入操作记录
    @Test
    public void importOptRecordForPeace2() {
        importBoxRecordForPeace2("A_RECORD", "A");
        importBoxRecordForPeace2("E_RECORD", "E");
        importBoxRecordForPeace2("W_RECORD", "W");
        importBoxRecordForPeace2("R_RECORD", "R");
    }

    private Integer allOperations = 0;
    final Integer[] executedOperations = {0};
    private List<net.sf.json.JSONObject> alist = new ArrayList<>();
    //导入操作记录的具体实现
    private void importBoxRecordForPeace2(String tableName, String type) {
        allOperations = 0;
        executedOperations[0] = 0;
        //从视图中查询所有的操作记录
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
//            System.out.println("连接成功！");
            log.info("链接成功！");
            String sqlForSelect = "select * from " + tableName + " a order by a.OPT_YEAR, a.OLD_DATE, a.OPT_PERSON_1, a.OPT_PERSON_2, a.BOX_CODE_1, a.BOX_CODE ";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                log.info("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, List<Map<String, Object>>> listGroupByDateAndOptionType =
            list.stream().collect(Collectors.groupingBy(w -> w.get("OLD_DATE").toString() + "&" + w.get("TABLENAME").toString()));

        TreeMap<String, List<Map<String, Object>>> mapGroupByDateAndOptionType = new TreeMap<>();
        mapGroupByDateAndOptionType.putAll(listGroupByDateAndOptionType);
        allOperations = list.size();
        for (String optionType : mapGroupByDateAndOptionType.keySet()) {
            List<Map<String, Object>> opts1 = mapGroupByDateAndOptionType.get(optionType);
            optionType = optionType.split("&")[1];

            switch (optionType) {
                case "移位":
                    // 不分组直接导入
                    opts1.forEach(o -> {
                        this.importMoveBoxForPeace2(o, type);
                        executedOperations[0]++;
                        log.info("Finished:(%d/%d)", executedOperations[0], allOperations);
                    });
                    break;
                case "销毁":
                    // 不分组直接导入
                    opts1.forEach(o -> {
                        this.importDestroyRecord(o);
                        executedOperations[0]++;
                        log.info("Finished:(%d/%d)", executedOperations[0], allOperations);
                    });
                    break;
                case "出库":
                case "出库2":
                    // 根据临时盒编码分组, 为空时任务编码分组, 为空时操作员分组, 为空时一维冻存盒编码进行的分组
//                    listGroupByTask =
//                        opts1.stream().collect(Collectors.groupingBy(w -> {
//                            Object oprator1 = Optional.ofNullable(w.get("OPT_PERSON_1")).orElse("");
//                            Object oprator2 = Optional.ofNullable(w.get("OPT_PERSON_2")).orElse("");
//                            String oprators = oprator1.toString() + oprator2.toString();
//                            Object taskKey = Optional.ofNullable(w.get("TEMP_BOX")).orElse(w.get("TASK_CODE"));
//                            if (oprators.length() > 0) {
//                                taskKey = Optional.ofNullable(taskKey).orElse(oprators);
//                            } else {
//                                taskKey = Optional.ofNullable(taskKey).orElse(w.get("BOX_CODE_1"));
//                            }
//                            return taskKey.toString();
//                        }));

//                    for (String task : listGroupByTask.keySet()) {
//                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
//                        this.importBoxOutForPeace2(opts2, type);
//                    }
                    this.importBoxOutForPeace2(opts1, type);
                    break;
                case "移位入库":
                    // 根据二维冻存盒编码进行的分组
//                    listGroupByTask =
//                        opts1.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));
//
//                    for (String task : listGroupByTask.keySet()) {
//                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
//                        this.importBoxForReInStock(opts2, type, Constants.STORANGE_IN_TYPE_MOVE);
//                    }
                    this.importBoxForReInStock(opts1, type, Constants.STORANGE_IN_TYPE_MOVE);
                    break;
                case "复位":
                    // 根据二维冻存盒编码进行的分组
//                    listGroupByTask =
//                        opts1.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));
//
//                    for (String task : listGroupByTask.keySet()) {
//                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
//                        this.importBoxForReInStock(opts2, type, Constants.STORANGE_IN_TYPE_REVERT);
//                    }
                    this.importBoxForReInStock(opts1, type, Constants.STORANGE_IN_TYPE_REVERT);
                    break;
                default:
                    break;
            }
        }
    }
    //保存销毁记录
    private void importDestroyRecord(Map<String, Object> box) {
        String boxCode1 = box.get("BOX_CODE_1").toString();
        String boxCode = box.get("BOX_CODE").toString();
        String optDate = box.get("OLD_DATE").toString();
        String sampleType = box.get("TYPE").toString();
        FrozenBox frozenBox = frozenBoxRepository.findByFrozenBoxCode1D(boxCode+"W");
//        if (frozenBox == null) {
//            return;
//        }
        FrozenTube frozenTube = null;
        String type = Constants.MOVE_TYPE_FOR_TUBE;
        //整盒销毁
        if (boxCode1.equals(boxCode)) {
            type = Constants.MOVE_TYPE_FOR_BOX;
        }else{
             frozenTube = frozenTubeRepository.findBySampleCodeAndSampleTypeCode(boxCode, sampleType);
            if(frozenTube == null){
                return;
            }
        }
        Date date = null;
        try {
            date = strToDate2(optDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> remark = new ArrayList<>();
        String optPerson1 = box.get("OPT_PERSON_1") != null ? box.get("OPT_PERSON_1").toString() : "NA";
        String optPerson2 = box.get("OPT_PERSON_2") != null ? box.get("OPT_PERSON_2").toString() : "NA";
        String memo = box.get("MEMO") != null ? box.get("MEMO").toString() : null;
        String special = box.get("SPECIAL") != null ? box.get("SPECIAL").toString() : null;
        if (!StringUtils.isEmpty(memo)) {
            remark.add(memo);
        }
        if (!StringUtils.isEmpty(memo)) {
            remark.add(special);
        }
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ZonedDateTime createDate = date.toInstant().atZone(ZoneId.systemDefault());
        Long opt_user_id = null;
        Long opt_user_id_2 = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            opt_user_id = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            opt_user_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }
        //移位入库
        PositionDestroy positionDestroy = new PositionDestroy();
        positionDestroy.destroyReason("无").positionDestroyDate(localDate).destroyType(type)
            .memo(String.join(",", remark))
            .status(Constants.VALID).operatorId1(opt_user_id).operatorId2(opt_user_id_2);
        positionDestroy.setCreatedDate(createDate);
        positionDestroyRepository.save(positionDestroy);
        //移位入库详情
        if (type.equals(Constants.MOVE_TYPE_FOR_BOX)) {
            //先查询到盒子的首次入库记录

            //
            if (frozenBox == null) {
                return;
            }
            String memoOld = frozenBox.getMemo();
            String memoLast = String.join(",", remark);
            if (!StringUtils.isEmpty(memoOld)) {
                memoLast = memoOld + "," + memoLast;
            }
            frozenBox.setMemo(memoLast);
            frozenBox.setStatus(Constants.FROZEN_BOX_DESTROY);
            frozenBoxRepository.save(frozenBox);
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
            for (FrozenTube f : frozenTubeList) {
                f.setStatus(Constants.FROZEN_TUBE_DESTROY);
                f.setFrozenTubeState(Constants.FROZEN_BOX_DESTROY);
                frozenTubeRepository.save(f);
            }
            saveDestroyDetail(positionDestroy, Constants.MOVE_TYPE_FOR_BOX, frozenTubeList);
        } else {

            frozenTube.setStatus(Constants.FROZEN_TUBE_DESTROY);
            frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_DESTROY);
            frozenTubeRepository.save(frozenTube);
            FrozenTube finalFrozenTube = frozenTube;
            List<FrozenTube> frozenTubeList = new ArrayList<FrozenTube>() {{
                add(finalFrozenTube);
            }};
            saveDestroyDetail(positionDestroy, Constants.MOVE_TYPE_FOR_BOX, frozenTubeList);
        }

    }
    //保存销毁记录详情
    public void saveDestroyDetail(PositionDestroy positionDestroy, String type, List<FrozenTube> frozenTubeList) {
        List<PositionDestroyRecord> positionDestroyRecordList = new ArrayList<PositionDestroyRecord>();
        for (FrozenTube frozenTube : frozenTubeList) {
            PositionDestroyRecord positionDestroyRecord = new PositionDestroyRecord()
                .sampleCode(StringUtils.isEmpty(frozenTube.getSampleCode()) ? frozenTube.getSampleTempCode() : frozenTube.getSampleCode())
                .positionDestroy(positionDestroy)
                .frozenTube(frozenTube)
                .destroyType(type)
                .equipment(frozenTube.getFrozenBox().getEquipment())
                .equipmentCode(frozenTube.getFrozenBox().getEquipmentCode())
                .area(frozenTube.getFrozenBox().getArea())
                .areaCode(frozenTube.getFrozenBox().getAreaCode())
                .supportRack(frozenTube.getFrozenBox().getSupportRack())
                .supportRackCode(frozenTube.getFrozenBox().getSupportRackCode())
                .columnsInShelf(frozenTube.getFrozenBox().getColumnsInShelf())
                .rowsInShelf(frozenTube.getFrozenBox().getRowsInShelf())
                .frozenBox(frozenTube.getFrozenBox())
                .frozenBoxCode(frozenTube.getFrozenBoxCode())
                .memo(frozenTube.getMemo())
                .project(frozenTube.getProject())
                .projectCode(frozenTube.getProjectCode())
                .projectSite(frozenTube.getProjectSite())
                .projectSiteCode(frozenTube.getProjectSiteCode())
                .status(frozenTube.getStatus())
                .tubeColumns(frozenTube.getTubeColumns())
                .tubeRows(frozenTube.getTubeRows())
                .frozenTubeType(frozenTube.getFrozenTubeType())
                .frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName())
                .sampleType(frozenTube.getSampleType())
                .sampleTypeCode(frozenTube.getSampleTypeCode())
                .sampleTypeName(frozenTube.getSampleTypeName())
                .sampleClassification(frozenTube.getSampleClassification())
                .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                .frozenTubeCode(frozenTube.getFrozenTubeCode())
                .frozenTubeState(frozenTube.getFrozenTubeState())
                .sampleTempCode(frozenTube.getSampleTempCode())
                .sampleUsedTimes(frozenTube.getSampleUsedTimes())
                .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost())
                .frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit())
                .sampleVolumns(frozenTube.getSampleVolumns())
                .errorType(frozenTube.getErrorType());
            positionDestroyRecordList.add(positionDestroyRecord);
            if (positionDestroyRecordList.size() >= 5000) {
                positionDestroyRecordRepository.save(positionDestroyRecordList);
                positionDestroyRecordList = new ArrayList<PositionDestroyRecord>();
            }
        }
        if (positionDestroyRecordList.size() > 0) {
            positionDestroyRecordRepository.save(positionDestroyRecordList);
        }
    }
    //保存移位记录
    public void importMoveBoxForPeace2(Map<String, Object> map, String sampleTypeCode) {
        String optName = map.get("OPT_NAME").toString();
        if(!optName.equals("移位入库")){
            return;
        }
        String boxCode = map.get("BOX_CODE").toString();
        log.info(boxCode);
        FrozenBox frozenBox = frozenBoxRepository.findByFrozenBoxCodeAndSampleTypeCode(boxCode, sampleTypeCode);

        if (frozenBox == null) {
            return;
        }
        Date stockInDate = null;
        try {
            stockInDate = strToDate2(map.get("OLD_DATE").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<String> remark = new ArrayList<>();
        String optPerson1 = map.get("OPT_PERSON_1") != null ? map.get("OPT_PERSON_1").toString() : "NA";
        String optPerson2 = map.get("OPT_PERSON_2") != null ? map.get("OPT_PERSON_2").toString() : "NA";
        String memo = map.get("MEMO") != null ? map.get("MEMO").toString() : null;
        String special = map.get("SPECIAL") != null ? map.get("SPECIAL").toString() : null;
        if (!StringUtils.isEmpty(memo)) {
            remark.add(memo);
        }
        if (!StringUtils.isEmpty(special)) {
            remark.add(special);
        }
        LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ZonedDateTime createDate = stockInDate.toInstant().atZone(ZoneId.systemDefault());
        Long opt_user_id = null;
        Long opt_user_id_2 = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            opt_user_id = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            opt_user_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }
        //移位
        PositionMove positionMove = new PositionMove();
        positionMove = positionMove.moveAffect("无").moveReason("无").moveType(Constants.MOVE_TYPE_FOR_BOX)
            .memo(String.join(",", remark))
            .whetherFreezingAndThawing(false).positionMoveDate(date)
            .status(Constants.VALID).operatorId1(opt_user_id).operatorId2(opt_user_id_2);
        positionMove.setCreatedDate(createDate);
        positionMoveRepository.saveAndFlush(positionMove);
        //移位入库详情
        //先查询到盒子的首次入库记录

        String equipmentCode = map.get("EQUIPMENT").toString().trim();
        String areaCode = map.get("AREA") != null && !map.get("AREA").toString().equals("NA") ? map.get("AREA").toString().trim() : "S99";

        String supportCode = map.get("SHELF") != null && !map.get("SHELF").toString().equals(null) && !map.get("SHELF").toString().equals("NA")
            ? map.get("SHELF").toString().trim() : "R99";

        //设备
        Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
        if (entity == null) {
            throw new BankServiceException("设备未导入:" + equipmentCode);
        }

        //区域
        Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
        if (area == null) {
            area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                .status("0001");
            areaRepository.saveAndFlush(area);
        }
        //冻存架
        SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
        if (supportRack == null) {
            SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
            supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                .area(supportRackMapper.areaFromId(area.getId()));
            supportRackRepository.saveAndFlush(supportRack);
        }
        String posInShelf = map.get("POS") != null ? map.get("POS").toString() : null;
        if (posInShelf == null || posInShelf.equals("NA")) {
            Long count = frozenBoxRepository.countByEquipmentCodeAndAreaCodeAndSupportRackCode(
                equipmentCode, areaCode, supportCode);
            posInShelf = "A" + (count.intValue() + 1);
        }

        String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
        String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
        frozenBox = frozenBox.equipment(entity).equipmentCode(equipmentCode)
            .area(area)
            .areaCode(areaCode)
            .supportRack(supportRack)
            .supportRackCode(supportCode)
            .columnsInShelf(columnsInShelf)
            .rowsInShelf(rowsInShelf);
        String memoOld = frozenBox.getMemo();
        String memoLast = String.join(",", remark);
        if (!StringUtils.isEmpty(memoOld)) {
            memoLast = memoOld + "," + memoLast;
        }
        frozenBox.setMemo(memoLast);
        frozenBoxRepository.saveAndFlush(frozenBox);
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
        saveMoveDetail(positionMove, Constants.MOVE_TYPE_FOR_BOX, frozenTubeList);
    }
    //保存移位记录详情
    public void saveMoveDetail(PositionMove positionMove, String moveType, List<FrozenTube> frozenTubeList) {
        List<PositionMoveRecord> positionMoveRecordList = new ArrayList<>();
        for (FrozenTube frozenTube : frozenTubeList) {
            PositionMoveRecord positionMoveRecord = new PositionMoveRecord()
                .sampleCode(StringUtils.isEmpty(frozenTube.getSampleCode()) ? frozenTube.getSampleTempCode() : frozenTube.getSampleCode())
                .positionMove(positionMove)
                .frozenTube(frozenTube)
                .moveType(moveType)
                .equipment(frozenTube.getFrozenBox().getEquipment())
                .equipmentCode(frozenTube.getFrozenBox().getEquipmentCode())
                .area(frozenTube.getFrozenBox().getArea())
                .areaCode(frozenTube.getFrozenBox().getAreaCode())
                .supportRack(frozenTube.getFrozenBox().getSupportRack())
                .supportRackCode(frozenTube.getFrozenBox().getSupportRackCode())
                .columnsInShelf(frozenTube.getFrozenBox().getColumnsInShelf())
                .rowsInShelf(frozenTube.getFrozenBox().getRowsInShelf())
                .frozenBox(frozenTube.getFrozenBox())
                .frozenBoxCode(frozenTube.getFrozenBoxCode())
                .memo(frozenTube.getMemo())
                .project(frozenTube.getProject())
                .projectCode(frozenTube.getProjectCode())
                .projectSite(frozenTube.getProjectSite())
                .projectSiteCode(frozenTube.getProjectSiteCode())
                .whetherFreezingAndThawing(positionMove.isWhetherFreezingAndThawing())
                .status(frozenTube.getStatus())
                .tubeColumns(frozenTube.getTubeColumns())
                .tubeRows(frozenTube.getTubeRows())
                .frozenTubeType(frozenTube.getFrozenTubeType())
                .frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName())
                .sampleType(frozenTube.getSampleType())
                .sampleTypeCode(frozenTube.getSampleTypeCode())
                .sampleTypeName(frozenTube.getSampleTypeName())
                .sampleClassification(frozenTube.getSampleClassification())
                .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                .frozenTubeCode(frozenTube.getFrozenTubeCode())
                .frozenTubeState(frozenTube.getFrozenTubeState())
                .sampleTempCode(frozenTube.getSampleTempCode())
                .sampleUsedTimes(frozenTube.getSampleUsedTimes())
                .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost())
                .frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit())
                .sampleVolumns(frozenTube.getSampleVolumns())
                .errorType(frozenTube.getErrorType());
            positionMoveRecordList.add(positionMoveRecord);
        }
        positionMoveRecordRepository.save(positionMoveRecordList);
    }
    //首次入库保存具体实现方法
    public void importBoxForPeace2(String tableName, String sampleTypeCode, FrozenTubeType frozenTubeType, FrozenBoxType frozenBoxType
        , String sqlAppend, List<ProjectSite> projectSiteList, SampleType sampleType, Project project) {

        List<ProjectSampleClass> projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeId(project.getId(), sampleType.getId());
        SampleClassification sampleClassification = projectSampleClass.get(0).getSampleClassification();

        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from " + tableName + " where opt_name = '首次入库' and box_code_2 in ('530120022A','530120022R','530120022W','530120022E') ";// 预编译语句
            if (sqlAppend != null) {
                sqlForSelect = sqlForSelect + sqlAppend;
            }
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<List, List<Map<String, Object>>> map = new HashMap<List, List<Map<String, Object>>>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get("OPT_DATE") == null) {
                throw new BankServiceException("操作日期为空！", list.get(i).toString());
            }
            if (list.get(i).get("OPT_PERSON") == null) {
                throw new BankServiceException("操作人为空！", list.get(i).toString());
            }
            if (list.get(i).get("LCC_ID") == null) {
                throw new BankServiceException("项目点为空！", list.get(i).toString());
            }

            String person = list.get(i).get("OPT_PERSON").toString();
            String optDate = list.get(i).get("OPT_DATE").toString();
            String lccId = list.get(i).get("LCC_ID").toString();
            if (lccId.equals("2206") || lccId.equals("3706")) {
                lccId = lccId + "00";
            }
            List<Map<String, Object>> alist = new ArrayList<Map<String, Object>>();
            List keyList = new ArrayList();
            keyList.add(optDate);
            keyList.add(person);
            keyList.add(lccId);
            if (map.get(keyList) == null || map.get(keyList).size() == 0) {
                alist.add(list.get(i));
                map.put(keyList, alist);
            } else {
                alist = map.get(keyList);
                alist.add(list.get(i));
                map.put(keyList, alist);
            }
        }
        for (List key : map.keySet()) {
            Date stockInDate = null;
            try {
                stockInDate = strToDate(key.get(0).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String optPerson = key.get(1).toString();
            String lcc = key.get(2).toString();
            ProjectSite projectSite = null;
            for (ProjectSite s : projectSiteList) {
                if (s.getProjectSiteCode().equals(lcc)) {
                    projectSite = s;
                }
            }
            if (projectSite == null) {
                throw new BankServiceException("项目点不存在！" + lcc);
            }
            String stockInCode = bankUtil.getUniqueIDByDate("B", stockInDate);

            Long receiverId = null;
            Long optPersonId2 = Constants.RECEIVER_MAP.get("NA");
            if (!StringUtils.isEmpty(optPerson)) {
                receiverId = Constants.RECEIVER_MAP.get(optPerson);
            }
            List<Map<String, Object>> boxList = map.get(key);
            int countOfSample = 0;
            Map<String, List<Map<String, Object>>> sampleMap = new HashMap<>();
            for (int j = 0; j < boxList.size(); j++) {
                String boxCode = boxList.get(j).get("BOX_CODE").toString().trim();
                String boxCode1 = boxList.get(j).get("BOX_CODE_2").toString().substring(0, boxList.get(j).get("BOX_CODE_2").toString().length() - 1);
                List<Map<String, Object>> tubeList = new ArrayList<>();
                Connection con1 = null;// 创建一个数据库连接
                PreparedStatement pre1 = null;// 创建预编译语句对象，一般都是用这个而不用Statement
                ResultSet result1 = null;// 创建一个结果集对象;
                try {
                    con1 = DBUtilForTemp.open();
                    System.out.println("连接成功！");
                    String sql = "select * from SAMPLE_0029 where box_code = " + boxCode1;// 预编译语句
                    pre1 = con1.prepareStatement(sql);// 实例化预编译语句
                    result1 = pre1.executeQuery();// 执行查询，注意括号中不需要再加参数
                    ResultSetMetaData rsMetas1 = result1.getMetaData();
                    Map<String, Object> sampleMap1 = null;
                    while (result1.next()) {
                        sampleMap1 = this.Result2Map(result1, rsMetas1);
                        tubeList.add(sampleMap1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        DBUtilForTemp.close(con);
                        System.out.println("数据库连接已关闭！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (tubeList == null || tubeList.size() == 0) {
                    tubeList = createFrozenTubeForEmptyFrozenTube(boxCode1);
                }
                sampleMap.put(boxCode1, tubeList);
                countOfSample += tubeList.size();
            }
            if (countOfSample == 0) {
                continue;
            }
            LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            ZonedDateTime createDate = stockInDate.toInstant().atZone(ZoneId.systemDefault());
            //保存入库记录
            StockIn stockInOld = stockInRepository.findStockInByStockInCode(stockInCode);
            if (stockInOld != null) {
                continue;
            }
            StockIn stockIn = new StockIn().projectCode("0029")
                .projectSiteCode(lcc)
                .receiveId(receiverId).receiveDate(date)
                .receiveName(optPerson)
                .stockInType("8001")
                .storeKeeperId1(receiverId)
                .storeKeeper1(optPerson)
                .storeKeeperId2(optPersonId2)
                .storeKeeper2("NA")
                .stockInDate(date)
                .countOfSample(countOfSample).stockInCode(stockInCode).project(project).projectSite(projectSite)
                .memo("PEACE2 项目" + LocalDate.now() + "数据导入")
                .status(Constants.STOCK_IN_COMPLETE);
            stockIn.setCreatedDate(createDate);
            stockInRepository.save(stockIn);

            //保存盒子以及盒内样本
            for (int j = 0; j < boxList.size(); j++) {

                if (boxList.get(j).get("BOX_CODE") == null) {
                    throw new BankServiceException("冻存盒编码为空！", boxList.get(j).toString());
                }
                String boxCode = boxList.get(j).get("BOX_CODE").toString().trim();
                FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
                if (frozenBox != null) {
                    continue;
                }

                String equipmentCode = boxList.get(j).get("EQUIPMENT").toString().trim();
                String areaCode = boxList.get(j).get("AREA") != null && !boxList.get(j).get("AREA").toString().equals("NA") ? boxList.get(j).get("AREA").toString().trim() : "S99";
                String supportCode = boxList.get(j).get("SHELF") != null && !boxList.get(j).get("SHELF").toString().equals(null) && !boxList.get(j).get("SHELF").toString().equals("NA") ? boxList.get(j).get("SHELF").toString().trim() : "R99";

                //设备
                Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);

                if (entity == null) {
                    throw new BankServiceException("设备未导入:" + equipmentCode);
                }
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.save(area);
                }
                //冻存架
                SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                if (supportRack == null) {
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                        .area(supportRackMapper.areaFromId(area.getId()));
                    supportRackRepository.save(supportRack);
                }
                String posInShelf = null;

                Boolean flag = false;
                if (boxList.get(j).get("POS") == null || boxList.get(j).get("POS") == "NA") {
                    Long count = frozenBoxRepository.countByEquipmentCodeAndAreaCodeAndSupportRackCode(
                        equipmentCode, areaCode, supportCode);
                    posInShelf = "A" + (count.intValue() + 1);
                } else {
                    posInShelf = boxList.get(j).get("POS").toString().trim();
                }
                String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
                String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
                //保存冻存盒

                if (frozenBox == null) {
                    frozenBox = new FrozenBox()
                        .frozenBoxCode(boxCode)
                        .frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode())
                        .frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows())
                        .frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                        .projectCode(project.getProjectCode())
                        .projectName(project.getProjectName())
                        .projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null)
                        .projectSiteName(projectSite != null ? projectSite.getProjectSiteName() : null)
                        .equipmentCode(entity.getEquipmentCode())
                        .areaCode(areaCode)
                        .supportRackCode(supportCode)
                        .sampleTypeCode(sampleType.getSampleTypeCode())
                        .sampleTypeName(sampleType.getSampleTypeName())
                        .isSplit(0)
                        .status("2004")
                        .emptyTubeNumber(0)
                        .emptyHoleNumber(0)
                        .dislocationNumber(0)
                        .isRealData(1).frozenBoxType(frozenBoxType).sampleType(sampleType).sampleClassification(sampleClassification).equipment(entity).area(area)
                        .supportRack(supportRack)
                        .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).project(project).projectSite(projectSite);
                    frozenBoxRepository.save(frozenBox);
                }
                List<Map<String, Object>> tubeList = sampleMap.get(boxList.get(j).get("BOX_CODE_2").toString().substring(0, boxList.get(j).get("BOX_CODE_2").toString().length() - 1));

                //保存入库盒
                StockInBox stockInBox = new StockInBox()
                    .equipmentCode(equipmentCode)
                    .areaCode(areaCode)
                    .supportRackCode(supportCode)
                    .rowsInShelf(rowsInShelf)
                    .columnsInShelf(columnsInShelf)
                    .status(Constants.FROZEN_BOX_STOCKED).countOfSample(tubeList.size())
                    .frozenBoxCode(boxCode).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(area).equipment(entity).supportRack(supportRack)
                    .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockInBox.setCreatedDate(createDate);
                stockInBoxRepository.save(stockInBox);

                //保存入库盒子位置
                StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
                stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                    .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                    .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                    .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                    .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                    .stockInBox(stockInBox);
                stockInBoxPosition.setCreatedDate(createDate);
                stockInBoxPositionRepository.save(stockInBoxPosition);

                //保存入库管子\
                for (int m = 0; m < tubeList.size(); m++) {
                    if (tubeList.get(m).get("TUBE_CODE") == null) {
                        throw new BankServiceException("样本编码为空！" + tableName + ":盒子编码：" + key, tubeList.get(m).toString());
                    }
                    String sampleCode = tubeList.get(m).get("TUBE_CODE").toString();
                    int row = Integer.valueOf(tubeList.get(m).get("BOX_COLNO").toString());
                    String tubeColumns = tubeList.get(m).get("BOX_ROWNO").toString();
                    String tubeRows = String.valueOf((char) (row + 64));
                    if (row >= 9) {
                        tubeRows = String.valueOf((char) (row + 65));
                    }
                    String status = "3001";
                    String memo = tubeList.get(m).get("MEMO") != null ? tubeList.get(m).get("MEMO").toString() : null;
                    String age_ = tubeList.get(m).get("AGE") != null ? tubeList.get(m).get("AGE").toString() : null;
                    Integer age = null;
                    if (age_ != null) {
                        Float age_N = Float.parseFloat(age_);
                        age = age_N.intValue();
                    }
                    String sex = tubeList.get(m).get("SEX") != null ? tubeList.get(m).get("SEX").toString() : null;
                    String gender = Constants.SEX_MAP.get(sex);
                    String patientCode = tubeList.get(m).get("BLOOD_CODE") != null ? tubeList.get(m).get("BLOOD_CODE").toString() : null;
                    Long patientId = StringUtils.isEmpty(patientCode) ? null : Long.valueOf(patientCode);
                    String nation = tubeList.get(m).get("NATION") != null ? tubeList.get(m).get("NATION").toString() : null;

                    FrozenTube tube = new FrozenTube()
                        .projectCode("0029").projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null)
                        .sampleCode(sampleCode)
                        .frozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName())
                        .sampleTypeCode(sampleType.getSampleTypeCode())
                        .sampleTypeName(sampleType.getSampleTypeName())
                        .sampleUsedTimesMost(frozenTubeType.getSampleUsedTimesMost())
                        .sampleUsedTimes(0)
                        .frozenTubeVolumns(frozenTubeType.getFrozenTubeVolumn())
                        .frozenTubeVolumnsUnit(frozenTubeType.getFrozenTubeVolumnUnit())
                        .tubeRows(tubeRows)
                        .tubeColumns(tubeColumns)
                        .status(status).memo(memo).age(age).gender(gender).patientId(patientId).nation(nation)
                        .frozenBoxCode(boxCode).frozenTubeType(frozenTubeType).sampleType(sampleType).sampleClassification(sampleClassification)
                        .project(project).projectSite(projectSite).frozenBox(frozenBox).frozenTubeState("2004");
                    tube.setCreatedDate(createDate);
                    frozenTubeRepository.save(tube);


                    StockInTube stockInTube = new StockInTube()
                        .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                        .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                        .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                        .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                        .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                        .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                        .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                        .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                        .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                        .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                        .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                    stockInTube.setCreatedDate(createDate);
                    stockInTubeRepository.save(stockInTube);
                }
            }
        }
    }
    //new 一个新冻存管
    public List<Map<String, Object>> createFrozenTubeForEmptyFrozenTube(String boxCode) {
        List<Map<String, Object>> alist = new ArrayList<>();
        int m = 1;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Map<String, Object> map = new HashMap();
                map.put("BOX_CODE", boxCode);
                map.put("MEMO", "问题样本（数据库未记录）");
                map.put("BOX_COLNO", i + 1);
                String code = String.format("%0" + 3 + "d", m);
                map.put("TUBE_CODE", boxCode + code);
                map.put("BOX_ROWNO", j + 1);
                alist.add(map);
                m++;
            }
        }
        return alist;
    }
    //出库保存
    public void importBoxOutForPeace2(List<Map<String, Object>> tubeList, String sampleTypeCode) {
        Map<List<String>, List<Map<String, Object>>> outTask = new HashMap<>();
        ArrayList<String> peopleMemo = new ArrayList<>();
        String applyCode = "";
        for (Map<String, Object> map : tubeList) {
            Date date = null;
            try {
                date = strToDate2(map.get("OLD_DATE").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            peopleMemo = new ArrayList<>();
            if (map.get("TASK_CODE").toString().equals("NA")) {
                continue;
            }
            applyCode = map.get("TASK_CODE") != null && !map.get("TASK_CODE").toString().equals("NA") ? map.get("TASK_CODE").toString() : applyCode;
            if (StringUtils.isEmpty(applyCode)) {
                applyCode = bankUtil.getUniqueIDByDate("C", date);
            }
            String optPerson1 = map.get("OPT_PERSON_1") != null && !map.get("OPT_PERSON_1").toString().equals("NA") ? map.get("OPT_PERSON_1").toString() : "NA";
            String optPerson2 = map.get("OPT_PERSON_2") != null && !map.get("OPT_PERSON_2").toString().equals("NA") ? map.get("OPT_PERSON_2").toString() : "NA";
            if (optPerson1.contains("&")) {
                String[] person = optPerson1.split("&");
                optPerson1 = person[0].substring(person[0].length() - 2, person[0].length());
                if (optPerson2.equals("NA")) {
                    optPerson2 = person[1].substring(0, 2);
                }
                peopleMemo.add(optPerson1);
            }
            List<String> keyList = new ArrayList<>();
            keyList.add(applyCode);
            keyList.add(optPerson1);
            keyList.add(optPerson2);
            keyList.add(map.get("OLD_DATE").toString());
            if (outTask.get(keyList) == null || outTask.get(keyList).size() == 0) {
                List<Map<String, Object>> mapList = new ArrayList<>();
                mapList.add(map);
                outTask.put(keyList, mapList);
            } else {
                List<Map<String, Object>> mapList = outTask.get(keyList);
                mapList.add(map);
                outTask.put(keyList, mapList);
            }
        }
        //创建出库委托方
        Delegate delegate = delegateRepository.findByDelegateCode("D_00001");
        Project project = projectRepository.findByProjectCode("0029");
        for (List<String> key : outTask.keySet()) {
            List<Map<String, Object>> alist = outTask.get(key);
            Date stockOutDate = null;
            try {
                stockOutDate = strToDate2(key.get(3));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ArrayList<String> remark = new ArrayList<>();
            String optPerson1 = key.get(1);
            String optPerson2 = key.get(2);
            String handOverPerson = alist.get(0).get("HAND_OVER_PERSON") != null && !alist.get(0).get("HAND_OVER_PERSON").toString().equals("NA") ? alist.get(0).get("HAND_OVER_PERSON").toString() : "NA";

            LocalDate date = stockOutDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            ZonedDateTime createDate = stockOutDate.toInstant().atZone(ZoneId.systemDefault());
            Long opt_user_id = null;
            Long opt_user_id_2 = null;
            Long over_person_id = null;
            if (!StringUtils.isEmpty(optPerson1)) {
                opt_user_id = Constants.RECEIVER_MAP.get(optPerson1);
            }
            if (!StringUtils.isEmpty(optPerson2)) {
                opt_user_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
            }
            if (!StringUtils.isEmpty(handOverPerson)) {
                over_person_id = Constants.RECEIVER_MAP.get(handOverPerson);
            }
            Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
            String boxCode = BankUtil.getUniqueCODE();

            for (Map<String, Object> map : alist) {
                remark = new ArrayList<>();
                boxCode = map.get("TEMP_BOX") != null && !map.get("TEMP_BOX").toString().equals("NA") ? map.get("TEMP_BOX").toString() : boxCode;

                String memo = map.get("MEMO") != null ? map.get("MEMO").toString() : null;
                String special = map.get("SPECIAL") != null ? map.get("SPECIAL").toString() : null;
                String result = map.get("RESULT").toString();

                if (!StringUtils.isEmpty(memo)) {
                    remark.add(memo);
                }
                if (!StringUtils.isEmpty(special)) {
                    remark.add(special);
                }


                if (result.equals("正确")) {
                    map.put("TEMP_BOX", boxCode);
                    List<Map<String, Object>> resultList = new ArrayList<>();
                    if (resultMap.get(boxCode) == null || resultMap.get(boxCode).size() == 0) {
                        resultList.add(map);
                        resultMap.put(boxCode, resultList);
                    } else {
                        if (resultMap.get(boxCode) != null && resultMap.get(boxCode).size() == 100) {
                            boxCode = BankUtil.getUniqueCODE();
                            resultList = new ArrayList<>();
                            resultList.add(map);
                        } else {
                            resultList = resultMap.get(boxCode);
                            resultList.add(map);
                        }
                        resultMap.put(boxCode, resultList);
                    }
                }
            }

            int countOfSample = 0;
            for (String keys : resultMap.keySet()) {
                countOfSample += resultMap.get(keys).size();
            }
            if (countOfSample == 0) {
                continue;
            }
            //创建出库申请
            String code = key.get(0);
            StockOutApply stockOutApply = stockOutApplyRepository.findByApplyCode(code);
            String stockOutPlanCode = "";
            if (code.length() > 10) {
                stockOutPlanCode = bankUtil.getUniqueIDByDate("E", stockOutDate);
            } else {
                stockOutPlanCode = "E" + code;
            }
            StockOutPlan stockOutPlan = stockOutPlanRepository.findByStockOutPlanCode(stockOutPlanCode);
            if (stockOutApply == null) {
                stockOutApply = new StockOutApply()
                    .status(Constants.STOCK_OUT_APPROVED).applyCode(code)
                    .applyDate(date).applyPersonName(null).delegate(delegate)
                    .approverId(opt_user_id).approveTime(date)
                    .endTime(null).startTime(null).purposeOfSample(null).recordId(opt_user_id).recordTime(date);
                stockOutApplyRepository.saveAndFlush(stockOutApply);
                StockOutApplyProject stockOutApplyProject = new StockOutApplyProject().project(project).stockOutApply(stockOutApply).status(Constants.VALID);

                stockOutApplyProjectRepository.saveAndFlush(stockOutApplyProject);
            }
            //创建出库计划

            if (stockOutPlan == null) {
                stockOutPlan = new StockOutPlan().stockOutPlanCode(stockOutPlanCode).stockOutApply(stockOutApply)
                    .status(Constants.STOCK_OUT_PLAN_COMPLETED).stockOutPlanDate(date)
                    .applyNumber(stockOutApply.getApplyCode())
                    .stockOutApply(stockOutApply);
                stockOutPlan = stockOutPlanRepository.saveAndFlush(stockOutPlan);
            }
            //创建出库需求
            String requirementCode = bankUtil.getUniqueIDByDate("D", stockOutDate);
            StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findByRequirementCode(requirementCode);

            if (stockOutRequirement == null) {
                stockOutRequirement = new StockOutRequirement().applyCode(code).requirementCode(requirementCode).requirementName("出库" + countOfSample + "支样本")
                    .status(Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS).stockOutApply(stockOutApply)
                    .countOfSample(countOfSample).countOfSampleReal(countOfSample);
            }
            //创建出库任务
            String taskCode = bankUtil.getUniqueIDByDate("F", stockOutDate);
            StockOutTask stockOutTask = new StockOutTask()
                .status(Constants.STOCK_OUT_TASK_COMPLETED)
                .stockOutTaskCode(taskCode)
                .stockOutPlan(stockOutPlan).usedTime(0)
                .stockOutDate(date).stockOutHeadId1(opt_user_id).stockOutHeadId2(opt_user_id_2)
                .taskStartTime(createDate).taskEndTime(createDate).memo(String.join(",", peopleMemo));
            stockOutTaskRepository.saveAndFlush(stockOutTask);

            stockOutRequirementRepository.saveAndFlush(stockOutRequirement);
            for (String box : resultMap.keySet()) {
                List<Map<String, Object>> tubeListResult = resultMap.get(box);
                String equipmentCode = "F1-01";
                String areaCode = "S01";
                Equipment equipment = equipmentRepository.findOneByEquipmentCode(equipmentCode);
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, equipment.getId());
                //创建临时盒
                FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(box);
                if (frozenBox == null) {
                    frozenBox = new FrozenBox();
                }
                FrozenBoxType boxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
                frozenBox.frozenBoxCode(box).frozenBoxType(boxType)
                    .frozenBoxTypeCode(boxType.getFrozenBoxTypeCode())
                    .frozenBoxTypeRows(boxType.getFrozenBoxTypeRows())
                    .frozenBoxTypeColumns(boxType.getFrozenBoxTypeColumns())
                    .status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER).areaCode(areaCode).area(area).equipment(equipment).equipmentCode(equipmentCode)
                    .supportRackCode(null).supportRack(null).columnsInShelf(null).rowsInShelf(null);
                frozenBoxRepository.saveAndFlush(frozenBox);
                //保存出库盒
                StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
                stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_HANDOVER);
                stockOutFrozenBox.setFrozenBox(frozenBox);
                stockOutFrozenBox.setStockOutTask(stockOutTask);
                stockOutFrozenBox = stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode())
                    .frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).
                        sampleTypeCode(frozenBox.getSampleTypeCode())
                    .sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .areaCode(areaCode).area(area).equipment(equipment).equipmentCode(equipmentCode)
                    .supportRack(null).supportRackCode(null).columnsInShelf(null).rowsInShelf(null)
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockOutFrozenBoxRepository.saveAndFlush(stockOutFrozenBox);
                //保存冻存盒位置
                StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition();

                stockOutBoxPosition.setStockOutFrozenBox(stockOutFrozenBox);
                stockOutBoxPosition.setEquipment(equipment);
                stockOutBoxPosition.setEquipmentCode(equipment != null ? equipment.getEquipmentCode() : null);
                stockOutBoxPosition.setArea(area);
                stockOutBoxPosition.setAreaCode(area != null ? area.getAreaCode() : null);
                stockOutBoxPosition.setStatus(Constants.VALID);
                stockOutBoxPosition.setCreatedDate(createDate);
                stockOutBoxPositionRepository.save(stockOutBoxPosition);
                String handOverTime = tubeListResult.get(0).get("HAND_OVER_DATE").toString();
                Date handOverDate = null;
                try {
                    handOverDate = strToDate(handOverTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                LocalDate overdate = handOverDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                //创建出库交接
                String handOverCode = bankUtil.getUniqueIDByDate("G", stockOutDate);
                StockOutHandover stockOutHandover = new StockOutHandover().handoverCode(handOverCode)
                    .stockOutTask(stockOutTask)
                    .stockOutApply(stockOutTask.getStockOutPlan().getStockOutApply())
                    .stockOutPlan(stockOutTask.getStockOutPlan())
                    .status(Constants.STOCK_OUT_HANDOVER_COMPLETED).handoverPersonId(over_person_id).handoverTime(overdate);

                stockOutHandoverRepository.saveAndFlush(stockOutHandover);
                StockOutHandoverBox stockOutHandoverBox = new StockOutHandoverBox().stockOutHandover(stockOutHandover)
                    .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                    .equipmentCode(stockOutFrozenBox.getEquipmentCode())
                    .areaCode(stockOutFrozenBox.getAreaCode())
                    .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                    .rowsInShelf(stockOutFrozenBox.getRowsInShelf())
                    .columnsInShelf(stockOutFrozenBox.getColumnsInShelf())
                    .frozenBoxCode(stockOutFrozenBox.getFrozenBoxCode())
                    .frozenBoxCode1D(stockOutFrozenBox.getFrozenBoxCode1D())
                    .area(stockOutFrozenBox.getArea())
                    .equipment(stockOutFrozenBox.getEquipment())
                    .supportRack(stockOutFrozenBox.getSupportRack())
                    .sampleTypeCode(stockOutFrozenBox.getSampleTypeCode())
                    .sampleType(stockOutFrozenBox.getSampleType())
                    .sampleTypeName(stockOutFrozenBox.getSampleTypeName())
                    .sampleClassification(stockOutFrozenBox.getSampleClassification())
                    .sampleClassificationCode(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(stockOutFrozenBox.getDislocationNumber())
                    .emptyHoleNumber(stockOutFrozenBox.getEmptyHoleNumber())
                    .emptyTubeNumber(stockOutFrozenBox.getEmptyTubeNumber())
                    .frozenBoxType(stockOutFrozenBox.getFrozenBoxType())
                    .frozenBoxTypeCode(stockOutFrozenBox.getFrozenBoxTypeCode())
                    .frozenBoxTypeColumns(stockOutFrozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(stockOutFrozenBox.getFrozenBoxTypeRows())
                    .isRealData(stockOutFrozenBox.getIsRealData())
                    .isSplit(stockOutFrozenBox.getIsSplit())
                    .project(stockOutFrozenBox.getProject())
                    .projectCode(stockOutFrozenBox.getProjectCode())
                    .projectName(stockOutFrozenBox.getProjectName())
                    .projectSite(stockOutFrozenBox.getProjectSite())
                    .projectSiteCode(stockOutFrozenBox.getProjectSiteCode())
                    .projectSiteName(stockOutFrozenBox.getProjectSiteName())

                    .stockOutFrozenBox(stockOutFrozenBox)
                    .memo(stockOutFrozenBox.getMemo())
                    .status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);
                stockOutHandoverBoxRepository.saveAndFlush(stockOutHandoverBox);
                Map<String, String> posMap = new HashMap<>();

                List<StockOutReqFrozenTube> stockOutReqFrozenTubes = new ArrayList<>();
                List<FrozenTube> frozenTubeList = new ArrayList<>();
                for (Map<String, Object> map : tubeListResult) {
                    String sampleCode = map.get("BOX_CODE").toString();
                    //出库需求样本
                    FrozenTube frozenTube = frozenTubeRepository.findBySampleCodeAndSampleTypeCode(sampleCode, sampleTypeCode);
                    if (frozenTube == null) {
                        continue;
                    }
                    String posInTube = map.get("POS_IN_TUBE") != null ? map.get("POS_IN_TUBE").toString() : "NA";
                    if (map.get("SPECIAL") != null && map.get("SPECIAL").toString().contains("整盒出库")) {
                        posInTube = frozenTube.getTubeRows() + frozenTube.getTubeColumns();
                    }
                    String tubeRows = posInTube.substring(0, 1);
                    String tubeColumns = posInTube.substring(1);
                    if (!posInTube.equals("NA")) {
                        posMap.put(posInTube, box);
                    } else {
                        String[][] posStr = new String[10][10];
                        Boolean flag = false;
                        for (int m = 0; m < posStr.length; m++) {
                            String row = String.valueOf((char) (m + 65));
                            if (m >= 8) {
                                row = String.valueOf((char) (m + 66));
                            }
                            for (int n = 0; n < posStr[m].length; n++) {
                                String column = String.valueOf(n + 1);
                                if (posMap.get(row + column) != null && posMap.get(row + column).equals(box)) {
                                    continue;
                                } else {
                                    flag = true;
                                    tubeRows = row;
                                    tubeColumns = column;
                                    posMap.put(row + column, box);
                                    break;
                                }
                            }
                            if (flag) {
                                break;
                            }
                        }
                    }
                    frozenTube.setFrozenBox(frozenBox);
                    frozenTube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                    frozenTube.setTubeColumns(tubeColumns);
                    frozenTube.setTubeRows(tubeRows);
                    int times = frozenTube.getSampleUsedTimes() != null ? (frozenTube.getSampleUsedTimes() + 1) : 1;
                    frozenTube.setSampleUsedTimes(times);
                    frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);
                    String memo = map.get("MEMO") != null ? map.get("MEMO").toString() : null;
                    String special = map.get("SPECIAL") != null ? map.get("SPECIAL").toString() : null;
                    ArrayList<String> memoList = new ArrayList<>();
                    if (!StringUtils.isEmpty(frozenTube.getMemo())) {
                        memoList.add(frozenTube.getMemo());
                    }
                    if (!StringUtils.isEmpty(memo)) {
                        memoList.add(memo);
                    }
                    if (!StringUtils.isEmpty(special)) {
                        memoList.add(special);
                    }
                    frozenTube.setMemo(String.join(",", memoList));
                    frozenTubeRepository.saveAndFlush(frozenTube);
                    System.out.print(String.join(",", remark));
                    StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube().status(Constants.STOCK_OUT_SAMPLE_COMPLETED)
                        .stockOutRequirement(stockOutRequirement).memo(String.join(",", remark))
                        .frozenBox(frozenTube.getFrozenBox())
                        .frozenTube(frozenTube)
                        .tubeColumns(frozenTube.getTubeColumns())
                        .tubeRows(frozenTube.getTubeRows()).stockOutFrozenBox(stockOutFrozenBox).stockOutTask(stockOutTask);
                    stockOutReqFrozenTube = stockOutReqFrozenTube.frozenBoxCode1D(frozenTube.getFrozenBox().getFrozenBoxCode1D())
                        .frozenBoxCode(frozenTube.getFrozenBoxCode()).errorType(frozenTube.getErrorType())
                        .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
                        .frozenTubeTypeId(frozenTube.getFrozenTubeType().getId()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                        .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                        .projectId(frozenTube.getProject() != null ? frozenTube.getProject().getId() : null).projectCode(frozenTube.getProjectCode())
                        .projectSiteId(frozenTube.getProjectSite() != null ? frozenTube.getProjectSite().getId() : null)
                        .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassificationId(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getId() : null)
                        .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                        .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode())
                        .sampleTypeId(frozenTube.getSampleType() != null ? frozenTube.getSampleType().getId() : null)
                        .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                        .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());
                    stockOutReqFrozenTubeRepository.saveAndFlush(stockOutReqFrozenTube);
                    //保存交接详情
                    StockOutHandoverDetails stockOutHandoverDetails = new StockOutHandoverDetails();
                    stockOutHandoverDetails = stockOutHandoverDetails.status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)
                        .stockOutReqFrozenTube(stockOutReqFrozenTube)
                        .stockOutHandoverBox(stockOutHandoverBox);
                    stockOutHandoverDetails.setMemo(String.join(",", memoList));
                    stockOutHandoverDetailsRepository.saveAndFlush(stockOutHandoverDetails);
                }
                executedOperations[0]++;
                log.info("Finished:(%d/%d)", executedOperations[0], allOperations);
            }
        }
    }
    //移位入库和复位入库的实现
    private void importBoxForReInStock(List<Map<String, Object>> inBoxAllList, String sampleTypeCode, String stockInType) {
        String optDate1 = inBoxAllList.get(0).get("OLD_DATE").toString();
        String optPerson1 = inBoxAllList.get(0).get("OPT_PERSON_1") != null && !inBoxAllList.get(0).get("OPT_PERSON_1").toString().equals("NA") ? inBoxAllList.get(0).get("OPT_PERSON_1").toString() : "NA";
        String optPerson2 = inBoxAllList.get(0).get("OPT_PERSON_2") != null && !inBoxAllList.get(0).get("OPT_PERSON_2").toString().equals("NA") ? inBoxAllList.get(0).get("OPT_PERSON_2").toString() : "NA";
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode(sampleTypeCode);
        Project project = projectRepository.findByProjectCode("0029");
        List<ProjectSampleClass> projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeId(project.getId(), sampleType.getId());
        SampleClassification sampleClassification = projectSampleClass.get(0).getSampleClassification();
        Date stockInDate = null;
        try {
            stockInDate = strToDate2(optDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ZonedDateTime createDate = stockInDate.toInstant().atZone(ZoneId.systemDefault());
        Long optPerson_id_1 = null;
        Long optPerson_id_2 = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            optPerson_id_1 = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            optPerson_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }

        String stockInCode = bankUtil.getUniqueIDByDate("B", stockInDate);
        //保存入库记录
        StockIn stockIn = new StockIn().projectCode("0029")
            .projectSiteCode(null)
            .receiveId(optPerson_id_1).receiveDate(date)
            .receiveName(optPerson1)
            .stockInType(stockInType)
            .storeKeeperId1(optPerson_id_1)
            .storeKeeper1(optPerson1)
            .storeKeeperId2(optPerson_id_2)
            .storeKeeper2(optPerson2)
            .stockInDate(date)
            .countOfSample(0).stockInCode(stockInCode).project(project).projectSite(null)
            .status(Constants.STOCK_IN_COMPLETE);
        stockInRepository.saveAndFlush(stockIn);
        Map<String, List<Map<String, Object>>> listGroupByTask =
            inBoxAllList.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));
        int countOfSample = 0;
        for (String task : listGroupByTask.keySet()) {
            List<Map<String, Object>> inBoxList = listGroupByTask.get(task);
            String equipmentCode = inBoxList.get(0).get("EQUIPMENT").toString();
            String areaCode = inBoxList.get(0).get("AREA") != null && !inBoxList.get(0).get("AREA").toString().equals("NA") ? inBoxList.get(0).get("AREA").toString() : "S99";
            String supportCode = inBoxList.get(0).get("SHELF") != null && !inBoxList.get(0).get("SHELF").toString().equals("NA") ? inBoxList.get(0).get("SHELF").toString() : "R99";
            String posInShelf = inBoxList.get(0).get("POS").toString();
            String columnsInShelf = posInShelf.substring(0, 1);
            String rowsInShelf = posInShelf.substring(1);
            String boxCode = inBoxList.get(0).get("BOX_CODE").toString();
            if (boxCode.contains("A") || boxCode.contains("R") || boxCode.contains("E") || boxCode.contains("W")) {
                Connection con = null;// 创建一个数据库连接
                PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
                ResultSet result = null;// 创建一个结果集对象
                try {
                    con = DBUtilForTemp.open();
                    System.out.println("连接成功！");
                    String tableName = "";
                    if (boxCode.contains("R")) {
                        tableName = "HE_R_0908";
                    }
                    if (boxCode.contains("W")) {
                        tableName = "HE_W_0908";
                    }
                    if (boxCode.contains("A")) {
                        tableName = "HE_A_0908";
                    }
                    if (boxCode.contains("E")) {
                        tableName = "HE_E_0908";
                    }
                    String sqlForSelect = "select box_code from " + tableName + " where box_code_2 = '" + boxCode + "' and rownum=1";// 预编译语句

                    pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
                    result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
                    ResultSetMetaData rsMeta = result.getMetaData();
                    while (result.next()) {
                        boxCode = result.getString("BOX_CODE");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        DBUtilForTemp.close(con);
                        System.out.println("数据库连接已关闭！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //设备
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            if (entity == null) {
                throw new BankServiceException("设备未导入:" + equipmentCode);
            }
            //区域
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
            if (area == null) {
                area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                    .status("0001");
                areaRepository.saveAndFlush(area);
            }
            //冻存架
            SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
            if (supportRack == null) {
                SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                    .area(supportRackMapper.areaFromId(area.getId()));
                supportRackRepository.saveAndFlush(supportRack);
            }
            FrozenBox frozenBox = new FrozenBox();

            if (stockInType.equals(Constants.STORANGE_IN_TYPE_MOVE)) {
                FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
                frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
                if (frozenBox == null) {
                    //保存冻存盒
                    frozenBox = new FrozenBox()
                        .frozenBoxCode(boxCode)
                        .frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode())
                        .frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows())
                        .frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                        .projectCode(project.getProjectCode())
                        .projectName(project.getProjectName())
                        .projectSiteCode(null)
                        .projectSiteName(null)
                        .equipmentCode(entity.getEquipmentCode())
                        .areaCode(areaCode)
                        .supportRackCode(supportCode)
                        .sampleTypeCode(sampleType.getSampleTypeCode())
                        .sampleTypeName(sampleType.getSampleTypeName())
                        .isSplit(0)
                        .status("2004")
                        .emptyTubeNumber(0)
                        .emptyHoleNumber(0)
                        .dislocationNumber(0)
                        .isRealData(1).frozenBoxType(frozenBoxType).sampleType(sampleType).sampleClassification(sampleClassification).equipment(entity).area(area)
                        .supportRack(supportRack)
                        .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).project(project).projectSite(null);
                }
            } else {
                String sampleCode = inBoxList.get(0).get("BOX_CODE_1").toString();
                StockInTube stockInTube = stockInTubeRepository.findBySampleCodeLast(sampleCode, sampleTypeCode);
                String frozenBoxCode = stockInTube.getFrozenBoxCode();
                frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
            }
            frozenBox = frozenBox.equipment(entity).area(area).equipmentCode(entity.getEquipmentCode())
                .areaCode(areaCode).supportRackCode(supportCode)
                .supportRack(supportRack)
                .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).status(Constants.FROZEN_BOX_STOCKED);
            frozenBoxRepository.saveAndFlush(frozenBox);
            //保存入库盒
            StockInBox stockInBox = new StockInBox()
                .equipmentCode(equipmentCode)
                .areaCode(areaCode)
                .supportRackCode(supportCode)
                .rowsInShelf(rowsInShelf)
                .columnsInShelf(columnsInShelf)
                .status(Constants.FROZEN_BOX_STOCKED).countOfSample(inBoxList.size())
                .frozenBoxCode(boxCode).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(area).equipment(entity).supportRack(supportRack)
                .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName());

            stockInBoxRepository.saveAndFlush(stockInBox);
            countOfSample += stockInBox.getCountOfSample();
            //保存入库盒子位置
            StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
            stockInBoxPosition = stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                .stockInBox(stockInBox);
            stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);
            //保存盒子以及盒内样本
            for (int j = 0; j < inBoxList.size(); j++) {
                String sampleCode = inBoxList.get(j).get("BOX_CODE_1").toString();
                log.info(sampleCode + ":" + inBoxList.get(j).get("POS_IN_TUBE"));
                FrozenTube tube = frozenTubeRepository.findBySampleCodeAndSampleTypeCode(sampleCode, sampleTypeCode);
                if (tube == null) {
                    continue;
                }
                String tubeRows = "";
                String tubeColumns = "";
                if (stockInType.equals(Constants.STORANGE_IN_TYPE_REVERT)) {
                    StockInTube stockInTube = stockInTubeRepository.findBySampleCodeLast(sampleCode, sampleTypeCode);
                    tubeRows = stockInTube.getTubeRows();
                    tubeColumns = stockInTube.getTubeColumns();
                } else {
                    String posInTube = inBoxList.get(j).get("POS_IN_TUBE").toString();
                    tubeRows = posInTube.substring(0, 1);
                    tubeColumns = posInTube.substring(1);
                }
                if (inBoxList.get(j).get("BOX_CODE_1") == null) {
                    throw new BankServiceException("样本编码为空！");
                }
                tube.setFrozenBox(frozenBox);
                tube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                tube = tube.tubeColumns(tubeColumns).tubeRows(tubeRows).frozenTubeState(Constants.FROZEN_BOX_STOCKED);
                frozenTubeRepository.saveAndFlush(tube);
                StockInTube stockInTube = new StockInTube()
                    .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                    .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                    .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                    .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                    .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                    .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                    .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                    .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                    .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                stockInTubeRepository.saveAndFlush(stockInTube);

                executedOperations[0]++;
                log.info("Finished:(%d/%d)", executedOperations[0], allOperations);
            }
        }
        stockIn.setCountOfSample(countOfSample);
        stockInRepository.saveAndFlush(stockIn);
    }
  //导入0029项目总方法
    @Test
    public void import0029MethodAll() throws Exception {
        this.importBaseData_first();//基础数据
        this.importForPeace2();//首次入库
        this.importOptRecordForPeace2();//操作记录
    }
    //修改冻存盒的一维码
    @Test
    public void importBoxCode1ForAllFrozenBox() {
        List<FrozenBox> frozenBoxList = frozenBoxRepository.findAll();
        List<StockInBox> stockInBoxes = stockInBoxRepository.findAll();
        List<StockOutFrozenBox> stockOutFrozenBoxes = stockOutFrozenBoxRepository.findAll();
        List<StockOutHandoverBox> stockOutHandoverBoxes = stockOutHandoverBoxRepository.findAll();
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from FIRST_STOCK_IN";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (FrozenBox frozenBox : frozenBoxList) {
            for (Map<String, Object> boxMap : list) {
                String boxCode = boxMap.get("BOX_CODE").toString().trim();
                String boxCode1 = boxMap.get("BOX_CODE_2").toString().trim();
                if (frozenBox.getFrozenBoxCode().equals(boxCode)) {
                    frozenBox.setFrozenBoxCode1D(boxCode1);
                }
            }
        }
        frozenBoxRepository.save(frozenBoxList);

        for (StockInBox stockInBox : stockInBoxes) {
            for (Map<String, Object> boxMap : list) {
                String boxCode = boxMap.get("BOX_CODE").toString().trim();
                String boxCode1 = boxMap.get("BOX_CODE_2").toString().trim();
                if (stockInBox.getFrozenBoxCode().equals(boxCode)) {
                    stockInBox.setFrozenBoxCode1D(boxCode1);
                }
            }
        }
        stockInBoxRepository.save(stockInBoxes);
        for (StockOutFrozenBox box : stockOutFrozenBoxes) {
            for (Map<String, Object> boxMap : list) {
                String boxCode = boxMap.get("BOX_CODE").toString().trim();
                String boxCode1 = boxMap.get("BOX_CODE_2").toString().trim();
                if (box.getFrozenBoxCode().equals(boxCode)) {
                    box.setFrozenBoxCode1D(boxCode1);
                }
            }
        }
        stockOutFrozenBoxRepository.save(stockOutFrozenBoxes);
        for (StockOutHandoverBox box : stockOutHandoverBoxes) {
            for (Map<String, Object> boxMap : list) {
                String boxCode = boxMap.get("BOX_CODE").toString().trim();
                String boxCode1 = boxMap.get("BOX_CODE_2").toString().trim();
                if (box.getFrozenBoxCode().equals(boxCode)) {
                    box.setFrozenBoxCode1D(boxCode1);
                }
            }
        }
        stockOutHandoverBoxRepository.save(stockOutHandoverBoxes);
    }
    //导入0029项目的转运记录
    @Test
    public void importTranshipFor0029() {
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        Project project = projectRepository.findByProjectCode("0029");
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> fristStockInBox = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
//            String sqlForSelect = " select *  from TRANSHIP_0029 where BOX_CODE  in ('530120022') and RECEIVER='王铁柱'";// 预编译语句
            String sqlForSelect = " select *  from TRANSHIP_0029 where BOX_CODE  in ('510420126','530120022','610120072','520320050')";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }

            String sql = "select * from FIRST_STOCK_IN";// 预编译语句
            PreparedStatement preT = con.prepareStatement(sql);// 实例化预编译语句
            ResultSet resultT = preT.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetaT = resultT.getMetaData();
            Map<String, Object> mapT = null;
            while (resultT.next()) {
                mapT = this.Result2Map(resultT, rsMetaT);
                fristStockInBox.add(mapT);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<String, List<Map<String, Object>>> listGroupByTrackNumber =
            list.stream().collect(Collectors.groupingBy(w -> w.get("TRACK_NUMBER")
                + "&" + w.get("RECEIVE_DATE").toString()
                + "&" + w.get("RECEIVER")
                + "&" + w.get("LCC").toString()
                + "&" + w.get("BATCH")));
        for (String key : listGroupByTrackNumber.keySet()) {
            List<Map<String, Object>> transhipBoxList = listGroupByTrackNumber.get(key);
            String[] keyList = key.split("&");
            String trackNumber = keyList[0];
            String date = keyList[1];
            String receiver = keyList[2] != null ? keyList[2] : "NA";
            String lcc = keyList[3];
            String batch = keyList[4];
            if (lcc.equals("2206") || lcc.equals("3706")) {
                lcc = lcc + "00";
            }
            ProjectSite projectSite = projectSiteRepository.findByProjectSiteCode(lcc);
            Date transhipDate = null;
            try {
                transhipDate = strToDate3(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            LocalDate transhipDateNew = transhipDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Long user_id = null;
            if (!StringUtils.isEmpty(receiver)) {
                user_id = Constants.RECEIVER_MAP.get(receiver);
            }
            Tranship tranship = transhipRepository.findByTrackNumber(trackNumber);
            if(tranship == null){

                String transhipCode = bankUtil.getUniqueIDByDate("A", transhipDate);
                tranship = new Tranship()
                    .transhipDate(transhipDateNew)
                    .project(project)
                    .projectCode(project.getProjectCode())
                    .projectName(project.getProjectName())
                    .projectSite(projectSite)
                    .projectSiteCode(projectSite.getProjectSiteCode())
                    .projectSiteName(projectSite.getProjectSiteName())
                    .trackNumber(trackNumber)
                    .transhipBatch(batch)
                    .transhipState(Constants.TRANSHIPE_IN_STOCKED)
                    .receiverId(user_id)
                    .receiver(receiver)
                    .receiveDate(transhipDateNew)
                    .sampleNumber(0)
                    .frozenBoxNumber(transhipBoxList.size())
                    .emptyTubeNumber(0)
                    .emptyHoleNumber(0)
                    .sampleSatisfaction(5)
                    .effectiveSampleNumber(0)
                    .memo(null)
                    .status(Constants.VALID)
                    .transhipCode(transhipCode)
                    .tempEquipmentId(null)
                    .tempEquipmentCode(null)
                    .tempAreaId(null)
                    .tempAreaCode(null);
                transhipRepository.save(tranship);
            }


            for (Map<String, Object> box : transhipBoxList) {
                //获取冻存管
                String boxCode1 = box.get("BOX_CODE").toString();
                List<Map<String, Object>> tubeList = new ArrayList<>();
                Connection con1 = null;// 创建一个数据库连接
                PreparedStatement pre1 = null;// 创建预编译语句对象，一般都是用这个而不用Statement
                ResultSet result1 = null;// 创建一个结果集对象;
                try {
                    con1 = DBUtilForTemp.open();
                    System.out.println("连接成功！");
                    String sql = "select * from SAMPLE_0029 where box_code = " + boxCode1;// 预编译语句
                    pre1 = con1.prepareStatement(sql);// 实例化预编译语句
                    result1 = pre1.executeQuery();// 执行查询，注意括号中不需要再加参数
                    ResultSetMetaData rsMetas1 = result1.getMetaData();
                    Map<String, Object> sampleMap1 = null;
                    while (result1.next()) {
                        sampleMap1 = this.Result2Map(result1, rsMetas1);
                        tubeList.add(sampleMap1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        DBUtilForTemp.close(con);
                        System.out.println("数据库连接已关闭！");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (tubeList == null || tubeList.size() == 0) {
                    tubeList = createFrozenTubeForEmptyFrozenTube(boxCode1);
                }
                String type = box.get("TYPE").toString();
                Map<String, Object> boxMap = new HashMap();
                for (Map<String, Object> map : fristStockInBox) {
                    String boxCode2 = map.get("BOX_CODE_2").toString().substring(0, map.get("BOX_CODE_2").toString().length() - 1);
                    String sampleType = map.get("TYPE").toString();
                    if (boxCode2.equals(boxCode1) && sampleType.equals(type)) {
                        boxMap = map;
                    }
                }
                String boxCode = boxMap.get("BOX_CODE").toString().trim();
                SampleType sampleType = sampleTypeRepository.findBySampleTypeCode(type);
                List<ProjectSampleClass> projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeId(project.getId(), sampleType.getId());
                SampleClassification sampleClassification = projectSampleClass.get(0).getSampleClassification();
                String equipmentCode = boxMap.get("EQUIPMENT").toString().trim();
                String areaCode = boxMap.get("AREA") != null && !boxMap.get("AREA").toString().equals("NA") ? boxMap.get("AREA").toString().trim() : "S99";
                String supportCode = boxMap.get("SHELF") != null && !boxMap.get("SHELF").toString().equals(null) && !boxMap.get("SHELF").toString().equals("NA") ? boxMap.get("SHELF").toString().trim() : "R99";

                //设备
                Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);

                if (entity == null) {
                    throw new BankServiceException("设备未导入:" + equipmentCode);
                }
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.save(area);
                }
                //冻存架
                SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                if (supportRack == null) {
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                        .area(supportRackMapper.areaFromId(area.getId()));
                    supportRackRepository.save(supportRack);
                }
                String posInShelf = boxMap.get("POS") != null ? boxMap.get("POS").toString().trim() : "NA";
                String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
                String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;

                //保存冻存盒
                FrozenBox frozenBox = new FrozenBox()
                    .frozenBoxCode1D(boxCode1)
                    .frozenBoxCode(boxCode)
                    .frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode())
                    .frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows())
                    .frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                    .projectCode(project.getProjectCode())
                    .projectName(project.getProjectName())
                    .projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null)
                    .projectSiteName(projectSite != null ? projectSite.getProjectSiteName() : null)
                    .equipmentCode(entity != null ? entity.getEquipmentCode() : null)
                    .areaCode(area != null ? area.getAreaCode() : null)
                    .supportRackCode(supportRack != null ? supportRack.getSupportRackCode() : null)
                    .sampleTypeCode(sampleType.getSampleTypeCode())
                    .sampleTypeName(sampleType.getSampleTypeName())
                    .isSplit(0)
                    .status("2004")
                    .emptyTubeNumber(0)
                    .emptyHoleNumber(0)
                    .dislocationNumber(0)
                    .isRealData(1).frozenBoxType(frozenBoxType).sampleType(sampleType)
                    .sampleClassification(sampleClassification).equipment(entity).area(area)
                    .supportRack(supportRack)
                    .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).project(project).projectSite(projectSite);
                frozenBoxRepository.save(frozenBox);

                String tempPos = box.get("TEMP_POS") != null ? box.get("TEMP_POS").toString() : "NA";

                String reg = "[\u4e00-\u9fa5]";

                Pattern pat = Pattern.compile(reg);

                Matcher mat = pat.matcher(tempPos);

                String memo = box.get("MEMO") != null ? box.get("MEMO").toString() : null;
                String repickStr = mat.replaceAll("");
                System.out.print("临时位置" + repickStr);
                Equipment equipment = null;
                Area areaForTranship = null;
                String equipmentCodeForTranship = null;
                String areaCodeForTranship = null;
                if (!repickStr.equals("NA")) {
                    if (repickStr.contains(".")) {
                        String[] pos = repickStr.split("\\.");
                        equipmentCodeForTranship = pos[0];
                        areaCodeForTranship = pos[1];
                    } else {
                        equipmentCodeForTranship = repickStr;
                    }
                    equipment = equipmentRepository.findOneByEquipmentCode(equipmentCodeForTranship);
                    if (!StringUtils.isEmpty(areaCodeForTranship)) {
                        areaForTranship = areaRepository.findOneByAreaCodeAndEquipmentId(areaCodeForTranship, equipment.getId());
                    }
                }
                //保存转运盒
                TranshipBox transhipBox = new TranshipBox();
                transhipBox.setEquipmentCode(equipmentCode);
                transhipBox.setAreaCode(areaCode);
                transhipBox.setSupportRackCode(null);
                transhipBox.setColumnsInShelf(null);
                transhipBox.setRowsInShelf(null);
                transhipBox.setStatus(Constants.FROZEN_BOX_TRANSHIP_COMPLETE);
                transhipBox.setMemo(memo);
                transhipBox.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                transhipBox.setFrozenBox(frozenBox);
                transhipBox.setTranship(tranship);
                transhipBox.setCountOfSample(tubeList.size());
                transhipBox.setEquipment(equipment);
                transhipBox.setArea(areaForTranship);
                transhipBox.setSupportRack(null);
                transhipBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName()).frozenBoxCode1D(boxCode1);
                transhipBoxRepository.save(transhipBox);
                //保存转运盒位置
                TranshipBoxPosition transhipBoxPosition = new TranshipBoxPosition();
                transhipBoxPosition.transhipBox(transhipBox).memo(transhipBox.getMemo()).status(Constants.INVALID)
                    .equipment(transhipBox.getFrozenBox().getEquipment()).equipmentCode(transhipBox.getEquipmentCode())
                    .area(transhipBox.getFrozenBox().getArea()).areaCode(transhipBox.getAreaCode())
                    .supportRack(transhipBox.getFrozenBox().getSupportRack()).supportRackCode(transhipBox.getSupportRackCode())
                    .rowsInShelf(transhipBox.getRowsInShelf()).columnsInShelf(transhipBox.getColumnsInShelf());
                transhipBoxPositionRepository.save(transhipBoxPosition);
                //保存冻存管，转运冻存管
                for (int m = 0; m < tubeList.size(); m++) {

                    String sampleCode = tubeList.get(m).get("TUBE_CODE").toString();
                    int row = Integer.valueOf(tubeList.get(m).get("BOX_COLNO").toString());
                    String tubeColumns = tubeList.get(m).get("BOX_ROWNO").toString();
                    String tubeRows = String.valueOf((char) (row + 64));
                    if (row >= 9) {
                        tubeRows = String.valueOf((char) (row + 65));
                    }
                    String status = "3001";
                    String tubememo = tubeList.get(m).get("MEMO") != null ? tubeList.get(m).get("MEMO").toString() : null;
                    String age_ = tubeList.get(m).get("AGE") != null ? tubeList.get(m).get("AGE").toString() : null;
                    Integer age = null;
                    if (age_ != null) {
                        Float age_N = Float.parseFloat(age_);
                        age = age_N.intValue();
                    }
                    String sex = tubeList.get(m).get("SEX") != null ? tubeList.get(m).get("SEX").toString() : null;
                    String gender = Constants.SEX_MAP.get(sex);
                    String patientCode = tubeList.get(m).get("BLOOD_CODE") != null ? tubeList.get(m).get("BLOOD_CODE").toString() : null;
                    Long patientId = StringUtils.isEmpty(patientCode) ? null : Long.valueOf(patientCode);
                    String nation = tubeList.get(m).get("NATION") != null ? tubeList.get(m).get("NATION").toString() : null;

                    FrozenTube frozenTube = new FrozenTube()
                        .projectCode("0029").projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null)
                        .sampleCode(sampleCode)
                        .frozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName())
                        .sampleTypeCode(sampleType.getSampleTypeCode())
                        .sampleTypeName(sampleType.getSampleTypeName())
                        .sampleUsedTimesMost(frozenTubeType.getSampleUsedTimesMost())
                        .sampleUsedTimes(0)
                        .frozenTubeVolumns(frozenTubeType.getFrozenTubeVolumn())
                        .frozenTubeVolumnsUnit(frozenTubeType.getFrozenTubeVolumnUnit())
                        .tubeRows(tubeRows)
                        .tubeColumns(tubeColumns)
                        .status(status).memo(tubememo).age(age).gender(gender).patientId(patientId).nation(nation)
                        .frozenBoxCode(boxCode).frozenTubeType(frozenTubeType).sampleType(sampleType).sampleClassification(sampleClassification)
                        .project(project).projectSite(projectSite).frozenBox(frozenBox).frozenTubeState("2004");
                    frozenTubeRepository.save(frozenTube);

                    TranshipTube transhipTube = new TranshipTube().status(frozenTube.getStatus()).memo(frozenTube.getMemo())
                        .rowsInTube(frozenTube.getTubeRows()).frozenTube(frozenTube).columnsInTube(frozenTube.getTubeColumns())
                        .transhipBox(transhipBox).errorType(frozenTube.getErrorType()).frozenBoxCode(frozenTube.getFrozenBoxCode())
                        .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(Constants.FROZEN_BOX_TRANSHIP_COMPLETE)
                        .frozenTubeType(frozenTube.getFrozenTubeType()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                        .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                        .project(frozenTube.getProject()).projectCode(frozenTube.getProjectCode()).projectSite(frozenTube.getProjectSite())
                        .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassification(frozenTube.getSampleClassification())
                        .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                        .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode()).sampleType(frozenTube.getSampleType())
                        .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                        .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());
                    transhipTubeRepository.save(transhipTube);
                }
            }
//            this.updateTranshipSampleNumber(tranship);
        }
    }
    //导入0029项目的入库记录
    @Test
    public void importStockInFor0029() {
        Project project = projectRepository.findByProjectCode("0029");
        List<Tranship> transhipList = transhipRepository.findAll();
        List<TranshipBox> transhipBoxes = transhipBoxRepository.findAll();
        Map<String, List<TranshipBox>> listGroupByTranshipCode =
            transhipBoxes.stream().collect(Collectors.groupingBy(w -> w.getTranship().getTranshipCode()));
        for (Tranship tranship : transhipList) {
            List<TranshipBox> transhipBoxList = listGroupByTranshipCode.get(tranship.getTranshipCode());
            FrozenBox frozenBoxFirst = transhipBoxList.get(0).getFrozenBox();
            Connection con = null;// 创建一个数据库连接
            PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
            ResultSet result = null;// 创建一个结果集对象
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            try {
                con = DBUtilForTemp.open();
                System.out.println("连接成功！");
//                String sqlForSelect = " select * from first_stock_in where box_code_2 in ('530120022A','530120022R','530120022W','530120022E')";// 预编译语句
                String sqlForSelect = " select *  from FIRST_STOCK_IN where BOX_CODE = '" + frozenBoxFirst.getFrozenBoxCode() + "'";// 预编译语句
                pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
                result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
                ResultSetMetaData rsMeta = result.getMetaData();
                Map<String, Object> map = null;
                while (result.next()) {
                    map = this.Result2Map(result, rsMeta);
                    list.add(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    DBUtilForTemp.close(con);
                    System.out.println("数据库连接已关闭！");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Map<String, Object> boxMap = list.get(0);
            String optPerson1 = boxMap.get("OPT_PERSON").toString();
            String optDate = boxMap.get("OPT_DATE").toString();
            String optPerson2 = "NA";
            Date stockInDate = null;
            try {
                stockInDate = strToDate(optDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Long optPerson_id_1 = null;
            Long optPerson_id_2 = null;
            if (!StringUtils.isEmpty(optPerson1)) {
                optPerson_id_1 = Constants.RECEIVER_MAP.get(optPerson1);
            }
            if (!StringUtils.isEmpty(optPerson2)) {
                optPerson_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
            }

            String stockInCode = bankUtil.getUniqueIDByDate("B", stockInDate);
            //保存入库单
            StockIn stockIn = new StockIn().projectCode("0029")
                .projectSiteCode(null)
                .receiveId(tranship.getReceiverId()).receiveDate(tranship.getReceiveDate())
                .receiveName(tranship.getReceiver())
                .stockInType(Constants.STORANGE_IN_TYPE_1ST)
                .storeKeeperId1(optPerson_id_1)
                .storeKeeper1(optPerson1)
                .storeKeeperId2(optPerson_id_2)
                .storeKeeper2(optPerson2)
                .stockInDate(date)
                .countOfSample(tranship.getEffectiveSampleNumber()).stockInCode(stockInCode).project(project).projectSite(tranship.getProjectSite())
                .status(Constants.STOCK_IN_COMPLETE);
            stockInRepository.save(stockIn);
            //创建入库与转运的关系
            TranshipStockIn transhipStockIn = new TranshipStockIn();
            transhipStockIn.stockInCode(stockIn.getStockInCode()).stockIn(stockIn).transhipCode(tranship.getTranshipCode()).tranship(tranship).status(Constants.VALID);
            transhipStockInRepository.save(transhipStockIn);
            //保存入库盒

            for (TranshipBox transhipBox : transhipBoxList) {
                FrozenBox frozenBox = transhipBox.getFrozenBox();
                List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(transhipBox.getFrozenBoxCode());
                StockInBox stockInBox = new StockInBox().equipmentCode(frozenBox.getEquipmentCode())
                    .areaCode(frozenBox.getAreaCode())
                    .supportRackCode(frozenBox.getSupportRackCode())
                    .rowsInShelf(frozenBox.getRowsInShelf())
                    .columnsInShelf(frozenBox.getColumnsInShelf())
                    .status(Constants.FROZEN_BOX_STOCKED).countOfSample(frozenTubeList.size())
                    .frozenBoxCode1D(frozenBox.getFrozenBoxCode1D())
                    .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockIn.getStockInCode()).area(frozenBox.getArea())
                    .equipment(frozenBox.getEquipment()).supportRack(frozenBox.getSupportRack())
                    .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockInBoxRepository.save(stockInBox);
                //保存入库盒位置
                StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
                stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                    .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                    .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                    .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                    .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                    .stockInBox(stockInBox);
                stockInBoxPositionRepository.save(stockInBoxPosition);
                //保存入库管

                List<StockInTube> stockInTubes = new ArrayList<>();
                for (FrozenTube tube : frozenTubeList) {
                    StockInTube stockInTube = new StockInTube()
                        .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                        .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                        .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                        .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                        .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                        .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                        .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                        .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                        .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                        .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                        .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                    stockInTubes.add(stockInTube);
                }
                stockInTubeRepository.save(stockInTubes);
            }
        }
    }
    //0029的操作记录中移位入库是有出库后再入库。
    @Test
    public void importOptFor1020Data(){
        allOperations = 0;
        executedOperations[0] = 0;
        //从视图中查询所有的操作记录
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
//            System.out.println("连接成功！");
            log.info("链接成功！");
            String sqlForSelect = "select * from w_record where tablename = '出库' and box_code like '%W%' ";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                log.info("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, List<Map<String, Object>>> listGroupByDateAndOptionType =
            list.stream().collect(Collectors.groupingBy(w -> w.get("OLD_DATE").toString() + "&" + w.get("TABLENAME").toString() + "&" + w.get("TASK_CODE").toString()));

        TreeMap<String, List<Map<String, Object>>> mapGroupByDateAndOptionType = new TreeMap<>();
        mapGroupByDateAndOptionType.putAll(listGroupByDateAndOptionType);
        allOperations = list.size();
        for (String optionType : mapGroupByDateAndOptionType.keySet()) {
            List<Map<String, Object>> opts1 = mapGroupByDateAndOptionType.get(optionType);
            Map<String, List<Map<String, Object>>> listGroupByTask = null;
            optionType = optionType.split("&")[1];

            switch (optionType) {
                case "出库":
                    this.importBoxOutForPeace2_1020(opts1);
                    break;
                case "入库":
                    this.importBoxForReInStock_1020(opts1, Constants.STORANGE_IN_TYPE_MOVE);
                    break;
                default:
                    break;
            }
        }
    }
    //样本入库
    private void importBoxForReInStock_1020(List<Map<String, Object>> opts1, String stockInType) {
        String optDate1 = opts1.get(0).get("OLD_DATE").toString();
        String optPerson1 = opts1.get(0).get("OPT_PERSON_1") != null && !opts1.get(0).get("OPT_PERSON_1").toString().equals("NA") ? opts1.get(0).get("OPT_PERSON_1").toString() : "NA";
        String optPerson2 = opts1.get(0).get("OPT_PERSON_2") != null && !opts1.get(0).get("OPT_PERSON_2").toString().equals("NA") ? opts1.get(0).get("OPT_PERSON_2").toString() : "NA";
        Project project = projectRepository.findByProjectCode("0029");
        Date stockInDate = null;
        try {
            stockInDate = strToDate2(optDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ZonedDateTime createDate = stockInDate.toInstant().atZone(ZoneId.systemDefault());
        Long optPerson_id_1 = null;
        Long optPerson_id_2 = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            optPerson_id_1 = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            optPerson_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }

        String stockInCode = bankUtil.getUniqueIDByDate("B", stockInDate);
        //保存入库记录
        StockIn stockIn = new StockIn().projectCode("0029")
            .projectSiteCode(null)
            .receiveId(optPerson_id_1).receiveDate(date)
            .receiveName(optPerson1)
            .stockInType(stockInType)
            .storeKeeperId1(optPerson_id_1)
            .storeKeeper1(optPerson1)
            .storeKeeperId2(optPerson_id_2)
            .storeKeeper2(optPerson2)
            .stockInDate(date)
            .countOfSample(0).stockInCode(stockInCode).project(project).projectSite(null)
            .status(Constants.STOCK_IN_COMPLETE);
        stockInRepository.saveAndFlush(stockIn);
        Map<String, List<Map<String, Object>>> listGroupByTask =
            opts1.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE_1").toString()));
        int countOfSample = 0;
        for (String boxCode : listGroupByTask.keySet()) {
            log.info(boxCode);
            FrozenBox frozenBox = frozenBoxRepository.findByFrozenBoxCode1D(boxCode);
            if(frozenBox==null ||( frozenBox!=null&&!frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER))){
                throw new BankServiceException("冻存盒未出过库"+boxCode);
            }
            List<Map<String, Object>> inBoxList = listGroupByTask.get(boxCode);
            String equipmentCode = inBoxList.get(0).get("EQUIPMENT").toString();
            String areaCode = inBoxList.get(0).get("AREA") != null && !inBoxList.get(0).get("AREA").toString().equals("NA") ? inBoxList.get(0).get("AREA").toString() : "S99";
            String supportCode = inBoxList.get(0).get("SHELF") != null && !inBoxList.get(0).get("SHELF").toString().equals("NA") ? inBoxList.get(0).get("SHELF").toString() : "R99";
            String posInShelf = inBoxList.get(0).get("POS").toString();
            String columnsInShelf = posInShelf.substring(0, 1);
            String rowsInShelf = posInShelf.substring(1);

            //设备
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            if (entity == null) {
                throw new BankServiceException("设备未导入:" + equipmentCode);
            }
            //区域
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
            if (area == null) {
                area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                    .status("0001");
                areaRepository.saveAndFlush(area);
            }
            //冻存架
            SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
            if (supportRack == null) {
                SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                    .area(supportRackMapper.areaFromId(area.getId()));
                supportRackRepository.saveAndFlush(supportRack);
            }
            frozenBox = frozenBox.equipment(entity).area(area).equipmentCode(entity.getEquipmentCode())
                .areaCode(areaCode).supportRackCode(supportCode)
                .supportRack(supportRack)
                .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).status(Constants.FROZEN_BOX_STOCKED);
            frozenBoxRepository.saveAndFlush(frozenBox);
            //保存入库盒
            StockInBox stockInBox = new StockInBox()
                .equipmentCode(equipmentCode)
                .areaCode(areaCode)
                .supportRackCode(supportCode)
                .rowsInShelf(rowsInShelf)
                .columnsInShelf(columnsInShelf)
                .status(Constants.FROZEN_BOX_STOCKED).countOfSample(inBoxList.size())
                .frozenBoxCode(boxCode).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(area).equipment(entity).supportRack(supportRack)
                .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName());

            stockInBoxRepository.saveAndFlush(stockInBox);
            countOfSample += stockInBox.getCountOfSample();
            //保存入库盒子位置
            StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
            stockInBoxPosition = stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                .stockInBox(stockInBox);
            stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
            //保存盒子以及盒内样本
            for (int j = 0; j < inBoxList.size(); j++) {
                String sampleCode = inBoxList.get(j).get("SAMPLE_CODE").toString();
                log.info(sampleCode + ":" + inBoxList.get(j).get("POS_IN_TUBE"));
                FrozenTube tube = null;
                for(FrozenTube t :frozenTubeList){
                    if(t.getSampleCode().equals(sampleCode)){
                        tube=t;
                        break;
                    }
                }if (tube == null) {
                    continue;
                }
                String tubeRows = "";
                String tubeColumns = "";

                String posInTube = inBoxList.get(j).get("POS_IN_TUBE").toString();
                tubeRows = posInTube.substring(0, 1);
                tubeColumns = posInTube.substring(1);

                tube.setFrozenBox(frozenBox);
                tube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                tube = tube.tubeColumns(tubeColumns).tubeRows(tubeRows).frozenTubeState(Constants.FROZEN_BOX_STOCKED);
                frozenTubeRepository.saveAndFlush(tube);
                StockInTube stockInTube = new StockInTube()
                    .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                    .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                    .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                    .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                    .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                    .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                    .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                    .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                    .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                stockInTubeRepository.saveAndFlush(stockInTube);
                executedOperations[0]++;
                log.info("Finished:(%d/%d)"+executedOperations[0]+allOperations);
            }
        }
        stockIn.setCountOfSample(countOfSample);
        stockInRepository.saveAndFlush(stockIn);
    }
    //整盒出库
    public void importBoxOutForPeace2_1020(List<Map<String, Object>> boxList) {
        String boxCode1Str = "370520105W,150120068W,320120061W,530320065W,610520100W,610520101W,610520103W,610520104W,610520106W,610520108W,610520109W,520420032W";
        ArrayList<String> peopleMemo = new ArrayList<>();
        String applyCode = "";

        Date date = null;
        try {
            date = strToDate2(boxList.get(0).get("OLD_DATE").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        peopleMemo = new ArrayList<>();

        applyCode = boxList.get(0).get("TASK_CODE") != null && !boxList.get(0).get("TASK_CODE").toString().equals("NA") ? boxList.get(0).get("TASK_CODE").toString() : applyCode;
        if (StringUtils.isEmpty(applyCode)) {
            applyCode = bankUtil.getUniqueIDByDate("C", date);
        }
        String optPerson1 = boxList.get(0).get("OPT_PERSON_1") != null && !boxList.get(0).get("OPT_PERSON_1").toString().equals("NA") ? boxList.get(0).get("OPT_PERSON_1").toString() : "NA";
        String optPerson2 = boxList.get(0).get("OPT_PERSON_2") != null && !boxList.get(0).get("OPT_PERSON_2").toString().equals("NA") ? boxList.get(0).get("OPT_PERSON_2").toString() : "NA";
        if (optPerson1.contains("&")) {
            String[] person = optPerson1.split("&");
            optPerson1 = person[0].substring(person[0].length() - 2, person[0].length());
            if (optPerson2.equals("NA")) {
                optPerson2 = person[1].substring(0, 2);
            }
            peopleMemo.add(optPerson1);
        }

        //出库委托方
        Delegate delegate = delegateRepository.findByDelegateCode("D_00001");
        Project project = projectRepository.findByProjectCode("0029");

        String handOverPerson = boxList.get(0).get("HANDOVER_PERSON") != null&& !boxList.get(0).get("HANDOVER_PERSON") .toString().equals("NA") ? boxList.get(0).get("HANDOVER_PERSON") .toString() : "NA";

        LocalDate stockOutDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ZonedDateTime createDate = date.toInstant().atZone(ZoneId.systemDefault());
        Long opt_user_id = null;
        Long opt_user_id_2 = null;
        Long over_person_id = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            opt_user_id = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            opt_user_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }
        if (!StringUtils.isEmpty(handOverPerson)) {
            over_person_id = Constants.RECEIVER_MAP.get(handOverPerson);
        }
         //创建出库申请

        StockOutApply stockOutApply = stockOutApplyRepository.findByApplyCode(applyCode);
        if (stockOutApply == null) {
            stockOutApply = new StockOutApply()
                .status(Constants.STOCK_OUT_APPROVED).applyCode(applyCode)
                .applyDate(stockOutDate).applyPersonName(null).delegate(delegate)
                .approverId(opt_user_id).approveTime(stockOutDate)
                .endTime(null).startTime(null).purposeOfSample(null).recordId(opt_user_id).recordTime(stockOutDate);
            stockOutApplyRepository.save(stockOutApply);
            StockOutApplyProject stockOutApplyProject = new StockOutApplyProject().project(project).stockOutApply(stockOutApply).status(Constants.VALID);
            stockOutApplyProjectRepository.save(stockOutApplyProject);
        }
        String stockOutPlanCode = "";
        if (applyCode.length() > 10) {
            stockOutPlanCode = bankUtil.getUniqueIDByDate("E", date);
        } else {
            stockOutPlanCode = "E" + applyCode;
        }
        //创建出库计划
        StockOutPlan stockOutPlan = stockOutPlanRepository.findByStockOutPlanCode(stockOutPlanCode);
        if (stockOutPlan == null) {
            stockOutPlan = new StockOutPlan().stockOutPlanCode(stockOutPlanCode).stockOutApply(stockOutApply)
                .status(Constants.STOCK_OUT_PLAN_COMPLETED).stockOutPlanDate(stockOutDate)
                .applyNumber(stockOutApply.getApplyCode())
                .stockOutApply(stockOutApply);
            stockOutPlan = stockOutPlanRepository.saveAndFlush(stockOutPlan);
        }
        //创建出库需求
        String requirementCode = bankUtil.getUniqueIDByDate("D", date);
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findByRequirementCode(requirementCode);
        int countOfSample = 0;
        for(Map<String, Object> key: boxList){
//            countOfSample+= Integer.valueOf(key.get("COUNT_OF_SAMPLE").toString());
        }
        if (stockOutRequirement == null) {
            stockOutRequirement = new StockOutRequirement().applyCode(applyCode).requirementCode(requirementCode).requirementName("出库" + countOfSample + "支样本")
                .status(Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS).stockOutApply(stockOutApply)
                .countOfSample(countOfSample).countOfSampleReal(countOfSample);
            stockOutRequirementRepository.save(stockOutRequirement);
        }
        //创建出库任务
        String taskCode = bankUtil.getUniqueIDByDate("F", date);
        StockOutTask stockOutTask = new StockOutTask()
            .status(Constants.STOCK_OUT_TASK_COMPLETED)
            .stockOutTaskCode(taskCode)
            .stockOutPlan(stockOutPlan).usedTime(0)
            .stockOutDate(stockOutDate).stockOutHeadId1(opt_user_id).stockOutHeadId2(opt_user_id_2)
            .taskStartTime(createDate).taskEndTime(createDate).memo(String.join(",", peopleMemo));
        stockOutTaskRepository.save(stockOutTask);
        String handOverTime = boxList.get(0).get("HAND_OVER_DATE").toString();
        Date handOverDate = null;
        try {
            handOverDate = strToDate(handOverTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LocalDate overdate = handOverDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //创建出库交接
        String handOverCode = bankUtil.getUniqueIDByDate("G", handOverDate);
        StockOutHandover stockOutHandover = new StockOutHandover().handoverCode(handOverCode)
            .stockOutTask(stockOutTask)
            .stockOutApply(stockOutTask.getStockOutPlan().getStockOutApply())
            .stockOutPlan(stockOutTask.getStockOutPlan())
            .status(Constants.STOCK_OUT_HANDOVER_COMPLETED).handoverPersonId(over_person_id).handoverTime(overdate);

        stockOutHandoverRepository.save(stockOutHandover);

        for (Map<String, Object> key: boxList) {
            String boxCode1D = key.get("BOX_CODE").toString();
            FrozenBox frozenBox = frozenBoxRepository.findByFrozenBoxCode1D(boxCode1D);
            String equipmentCode = "F1-01";
            String areaCode = "S01";
            Equipment equipment = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, equipment.getId());
            if(!boxCode1Str.contains(boxCode1D)){
                if (frozenBox == null || !frozenBox.getStatus().equals(Constants.FROZEN_BOX_STOCKED)) {
                    throw new BankServiceException("冻存盒不存在！"+boxCode1D);
                }
                frozenBox.status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER).areaCode(areaCode).area(area).equipment(equipment)
                    .equipmentCode(equipmentCode)
                    .supportRackCode(null).supportRack(null).columnsInShelf(null).rowsInShelf(null);
                frozenBoxRepository.save(frozenBox);
            }
            //保存出库盒
            StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_HANDOVER);
            stockOutFrozenBox.setFrozenBox(frozenBox);
            stockOutFrozenBox.setStockOutTask(stockOutTask);
            stockOutFrozenBox = stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode())
                .frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).
                    sampleTypeCode(frozenBox.getSampleTypeCode())
                .sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .areaCode(areaCode).area(area).equipment(equipment).equipmentCode(equipmentCode)
                .supportRack(null).supportRackCode(null).columnsInShelf(null).rowsInShelf(null)
                .projectSiteName(frozenBox.getProjectSiteName());
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
            //保存冻存盒位置
            StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition();

            stockOutBoxPosition.setStockOutFrozenBox(stockOutFrozenBox);
            stockOutBoxPosition.setEquipment(equipment);
            stockOutBoxPosition.setEquipmentCode(equipment != null ? equipment.getEquipmentCode() : null);
            stockOutBoxPosition.setArea(area);
            stockOutBoxPosition.setAreaCode(area != null ? area.getAreaCode() : null);
            stockOutBoxPosition.setStatus(Constants.VALID);
            stockOutBoxPosition.setCreatedDate(createDate);
            stockOutBoxPositionRepository.save(stockOutBoxPosition);

            StockOutHandoverBox stockOutHandoverBox = new StockOutHandoverBox().stockOutHandover(stockOutHandover)
                .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                .equipmentCode(stockOutFrozenBox.getEquipmentCode())
                .areaCode(stockOutFrozenBox.getAreaCode())
                .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                .rowsInShelf(stockOutFrozenBox.getRowsInShelf())
                .columnsInShelf(stockOutFrozenBox.getColumnsInShelf())
                .frozenBoxCode(stockOutFrozenBox.getFrozenBoxCode())
                .frozenBoxCode1D(stockOutFrozenBox.getFrozenBoxCode1D())
                .area(stockOutFrozenBox.getArea())
                .equipment(stockOutFrozenBox.getEquipment())
                .supportRack(stockOutFrozenBox.getSupportRack())
                .sampleTypeCode(stockOutFrozenBox.getSampleTypeCode())
                .sampleType(stockOutFrozenBox.getSampleType())
                .sampleTypeName(stockOutFrozenBox.getSampleTypeName())
                .sampleClassification(stockOutFrozenBox.getSampleClassification())
                .sampleClassificationCode(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(stockOutFrozenBox.getDislocationNumber())
                .emptyHoleNumber(stockOutFrozenBox.getEmptyHoleNumber())
                .emptyTubeNumber(stockOutFrozenBox.getEmptyTubeNumber())
                .frozenBoxType(stockOutFrozenBox.getFrozenBoxType())
                .frozenBoxTypeCode(stockOutFrozenBox.getFrozenBoxTypeCode())
                .frozenBoxTypeColumns(stockOutFrozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(stockOutFrozenBox.getFrozenBoxTypeRows())
                .isRealData(stockOutFrozenBox.getIsRealData())
                .isSplit(stockOutFrozenBox.getIsSplit())
                .project(stockOutFrozenBox.getProject())
                .projectCode(stockOutFrozenBox.getProjectCode())
                .projectName(stockOutFrozenBox.getProjectName())
                .projectSite(stockOutFrozenBox.getProjectSite())
                .projectSiteCode(stockOutFrozenBox.getProjectSiteCode())
                .projectSiteName(stockOutFrozenBox.getProjectSiteName())

                .stockOutFrozenBox(stockOutFrozenBox)
                .memo(stockOutFrozenBox.getMemo())
                .status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);
            stockOutHandoverBoxRepository.save(stockOutHandoverBox);

            List<FrozenTube> frozenTubeList = new ArrayList<>();
            List<StockInTube> stockInTubes = stockInTubeRepository.findByFrozenBoxCode(frozenBox.getFrozenBoxCode());
            frozenTubeList = stockInTubes.stream().map(s->s.getFrozenTube()).collect(Collectors.toList());
            if(frozenTubeList.size()==0 || frozenTubeList.size()>100){
                throw new BankServiceException("数据错误");
            }
            List<StockOutReqFrozenTube> stockOutReqFrozenTubes = new ArrayList<>();
            List<StockOutHandoverDetails> stockOutHandoverDetailss = new ArrayList<>();
            String memo = key.get("MEMO") != null ? key.get("MEMO").toString() : null;
            String special = key.get("OPT_MEMO") != null ? key.get("OPT_MEMO").toString() : null;
            for (FrozenTube frozenTube : frozenTubeList) {
                int times = frozenTube.getSampleUsedTimes() != null ? (frozenTube.getSampleUsedTimes() + 1) : 1;
                frozenTube.setSampleUsedTimes(times);
                if(!boxCode1Str.contains(boxCode1D)){
                    frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);
                }
                ArrayList<String> memoList = new ArrayList<>();
                if (!StringUtils.isEmpty(frozenTube.getMemo())) {
                    memoList.add(frozenTube.getMemo());
                }
                if (!StringUtils.isEmpty(memo)) {
                    memoList.add(memo);
                }
                if (!StringUtils.isEmpty(special)) {
                    memoList.add(special);
                }
                frozenTube.setMemo(String.join(",", memoList));
                frozenTubeRepository.save(frozenTube);
                StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube().status(Constants.STOCK_OUT_SAMPLE_COMPLETED)
                    .stockOutRequirement(stockOutRequirement).memo(String.join(",", memoList))
                    .frozenBox(frozenTube.getFrozenBox())
                    .frozenTube(frozenTube)
                    .tubeColumns(frozenTube.getTubeColumns())
                    .tubeRows(frozenTube.getTubeRows()).stockOutFrozenBox(stockOutFrozenBox).stockOutTask(stockOutTask);
                stockOutReqFrozenTube = stockOutReqFrozenTube.frozenBoxCode1D(frozenTube.getFrozenBox().getFrozenBoxCode1D())
                    .frozenBoxCode(frozenTube.getFrozenBoxCode()).errorType(frozenTube.getErrorType())
                    .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
                    .frozenTubeTypeId(frozenTube.getFrozenTubeType().getId()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                    .projectId(frozenTube.getProject() != null ? frozenTube.getProject().getId() : null).projectCode(frozenTube.getProjectCode())
                    .projectSiteId(frozenTube.getProjectSite() != null ? frozenTube.getProjectSite().getId() : null)
                    .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassificationId(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getId() : null)
                    .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                    .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode())
                    .sampleTypeId(frozenTube.getSampleType() != null ? frozenTube.getSampleType().getId() : null)
                    .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                    .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());
                stockOutReqFrozenTubes.add(stockOutReqFrozenTube);
                //保存交接详情
                StockOutHandoverDetails stockOutHandoverDetails = new StockOutHandoverDetails();
                stockOutHandoverDetails = stockOutHandoverDetails.status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)
                    .stockOutReqFrozenTube(stockOutReqFrozenTube)
                    .stockOutHandoverBox(stockOutHandoverBox);
                stockOutHandoverDetails.setMemo(String.join(",", memoList));
                stockOutHandoverDetailss.add(stockOutHandoverDetails);
            }
            countOfSample+=stockOutReqFrozenTubes.size();
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubes);
            stockOutHandoverDetailsRepository.save(stockOutHandoverDetailss);
            executedOperations[0]++;
            log.info("Finished:(%d/%d)", executedOperations[0], allOperations);
        }
        stockOutRequirement.countOfSample(countOfSample).countOfSampleReal(countOfSample).requirementName("出库" + countOfSample + "支样本");
        stockOutRequirementRepository.save(stockOutRequirement);
    }
    ///////////////////////////////////////////////////0029导入结束///////////////////////////////////////


    ///////////////////////////////////////////////////佳通导入开始///////////////////////////////////////

    //佳通的操作记录主方法
    @Test
    public void importOptForJT1023Data(){
        allOperations = 0;
        executedOperations[0] = 0;
        //从视图中查询所有的操作记录
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
//            System.out.println("连接成功！");
            log.info("链接成功！");
            String sqlForSelect = "select * from " + "jt_opt_1128" + " a order by  a.OLD_DATE";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                log.info("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, List<Map<String, Object>>> listGroupByDateAndOptionType =
            list.stream().collect(Collectors.groupingBy(w -> w.get("OLD_DATE").toString() + "&" + w.get("OPT_NAME").toString()));

        TreeMap<String, List<Map<String, Object>>> mapGroupByDateAndOptionType = new TreeMap<>();
        mapGroupByDateAndOptionType.putAll(listGroupByDateAndOptionType);
        allOperations = list.size();
        for (String optionType : mapGroupByDateAndOptionType.keySet()) {
            List<Map<String, Object>> opts1 = mapGroupByDateAndOptionType.get(optionType);
            Map<String, List<Map<String, Object>>> listGroupByTask = null;
            optionType = optionType.split("&")[1];

            switch (optionType) {
                case "出库":
                    this.importBoxOutFromPeace2ForJT_1023(opts1);
                    break;
                case "移位入库":
                    this.importBoxForJTInStock_1023(opts1, Constants.STORANGE_IN_TYPE_MOVE);
                    break;
                case "复位入库":
                    this.importBoxForJTInStock_1023(opts1, Constants.STORANGE_IN_TYPE_REVERT);
                    break;
                default:
                    break;
            }
        }
        log.info(alist.toString());
    }
    //导入佳通移位记录
    @Test
    public void importMoveOptForJT1023Data(){
        allOperations = 0;
        executedOperations[0] = 0;
        //从视图中查询所有的操作记录
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
//            System.out.println("连接成功！");
            log.info("链接成功！");
            String sqlForSelect = "select * from " + "jt_move_1116";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                log.info("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, List<Map<String, Object>>> listGroupByDate =
            list.stream().collect(Collectors.groupingBy(w -> w.get("OPT_DATE").toString()+"&"+w.get("BOX_CODE").toString()));

        allOperations = list.size();
        for (String key : listGroupByDate.keySet()) {
            List<Map<String, Object>> opts1 = listGroupByDate.get(key);
            importMoveRecordFor0029_JT(opts1,key);
        }
        log.info(alist.toString());
    }
    //佳通入库的具体实现
    private void importBoxForJTInStock_1023(List<Map<String, Object>> opts1, String stockInType) {
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findTopOne();
        String optDate1 = opts1.get(0).get("OLD_DATE").toString();

        String optPerson1 = "王铁柱";
        String optPerson2 = "张丽萍";
        Project project = projectRepository.findByProjectCode("0029");
        Date stockInDate = null;
        try {
            stockInDate = strToDate2(optDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        ZonedDateTime createDate = stockInDate.toInstant().atZone(ZoneId.systemDefault());
        Long optPerson_id_1 = null;
        Long optPerson_id_2 = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            optPerson_id_1 = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            optPerson_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }
        String stockInCode = bankUtil.getUniqueIDByDate("B", stockInDate);
        //保存入库记录
        StockIn stockIn = new StockIn().projectCode("0029")
            .projectSiteCode(null)
            .receiveId(optPerson_id_1).receiveDate(date)
            .receiveName(optPerson1)
            .stockInType(stockInType)
            .storeKeeperId1(optPerson_id_1)
            .storeKeeper1(optPerson1)
            .storeKeeperId2(optPerson_id_2)
            .storeKeeper2(optPerson2)
            .stockInDate(date)
            .countOfSample(0).stockInCode(stockInCode).project(project).projectSite(null)
            .status(Constants.STOCK_IN_COMPLETE);
        stockInRepository.save(stockIn);
        int countOfSample = 0;
        List<SampleType> sampleTypeList = sampleTypeRepository.findAllSampleTypes();
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findSampleTypeByProjectCode("0029");

        for (Map<String,Object> opt : opts1) {
            String boxCode = opt.get("BOX_CODE_1").toString();
            log.info(boxCode);
            String type = boxCode.substring(boxCode.length()-1,boxCode.length());
            FrozenBox frozenBox = frozenBoxRepository.findByFrozenBoxCode1D(boxCode);
            if(frozenBox==null){
                SampleType sampleType = sampleTypeList.stream().filter(s->s.getSampleTypeCode().equals(type)).findFirst().orElse(null);
                if(sampleType == null){
                    throw new BankServiceException("样本类型不存在！",type);
                }
                ProjectSampleClass projectSampleClass = projectSampleClasses.stream().filter(s->s.getSampleType().getSampleTypeCode().equals(type)).findFirst().orElse(null);
                if(projectSampleClass == null){
                    throw new BankServiceException("样本类型"+type+"在0029项目下未配置样本分类！");
                }
                SampleClassification sampleClassification = projectSampleClass.getSampleClassification();
                if(sampleClassification == null){
                    throw new BankServiceException("样本分类获取失败！");
                }
                FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
                frozenBox = new FrozenBox().frozenBoxCode1D(boxCode)
                    .frozenBoxCode(" ")
                    .frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode())
                    .frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows())
                    .frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                    .projectCode(project.getProjectCode())
                    .projectName(project.getProjectName())
                    .sampleTypeCode(sampleType.getSampleTypeCode())
                    .sampleTypeName(sampleType.getSampleTypeName())
                    .isSplit(0)
                    .status("2004")
                    .emptyTubeNumber(0)
                    .emptyHoleNumber(0)
                    .dislocationNumber(0)
                    .isRealData(1).frozenBoxType(frozenBoxType).sampleType(sampleType)
                    .sampleClassification(sampleClassification).project(project);
            }

            String boxCode1d = boxCode.substring(0,boxCode.length()-1);
            List<net.sf.json.JSONObject> jsonObjects = frozenBoxImportService.importFrozenBoxByBoxCode(boxCode1d);
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
            List<net.sf.json.JSONObject> sampleDatas = new ArrayList<>();
            int countForstockInBox = 0;
            ArrayList<String> memoList1 = new ArrayList<>();
            if(!StringUtils.isEmpty(frozenBox.getMemo())){
                memoList1.add(frozenBox.getMemo());
            }
            if(jsonObjects==null || jsonObjects.size()==0){
                countForstockInBox = frozenTubeList.size();
                memoList1.add("佳通未扫码");
            }else{
                Map<String, List<net.sf.json.JSONObject>> listGroupByBoxType =
                    jsonObjects.stream().collect(Collectors.groupingBy(w -> w.get("boxType").toString()));
                sampleDatas = listGroupByBoxType.get(type);
                countForstockInBox = sampleDatas.size();
            }

            String equipmentCode = opt.get("EQUIPMENT").toString();
            String areaCode = opt.get("AREA") != null && !opt.get("AREA").toString().equals("NA") ? opt.get("AREA").toString() : "S99";
            String supportCode = opt.get("SHELF") != null && !opt.get("SHELF").toString().equals("NA") ? opt.get("SHELF").toString() : "R99";
            String posInShelf = opt.get("POS_IN_SHELF").toString();
            String columnsInShelf = posInShelf.substring(0, 1);
            String rowsInShelf = posInShelf.substring(1);
            String memo = opt.get("MEMO") != null ? opt.get("MEMO").toString() : null;


            if(memo!=null){
                memoList1.add(memo);
            }
            //设备
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            if (entity == null) {
                throw new BankServiceException("设备未导入:" + equipmentCode);
            }
            //区域
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
            if (area == null) {
                area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                    .status("0001");
                areaRepository.saveAndFlush(area);
            }
            //冻存架
            SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
            if (supportRack == null) {
                SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                    .area(supportRackMapper.areaFromId(area.getId()));
                supportRackRepository.save(supportRack);
            }
            frozenBox = frozenBox.equipment(entity).area(area).equipmentCode(entity.getEquipmentCode())
                .areaCode(areaCode).supportRackCode(supportCode)
                .supportRack(supportRack).memo(String.join(",",memoList1))
                .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).status(Constants.FROZEN_BOX_STOCKED);
            frozenBoxRepository.save(frozenBox);

            //保存入库盒
            StockInBox stockInBox = new StockInBox()
                .equipmentCode(equipmentCode)
                .areaCode(areaCode)
                .supportRackCode(supportCode)
                .rowsInShelf(rowsInShelf)
                .columnsInShelf(columnsInShelf)
                .status(Constants.FROZEN_BOX_STOCKED).countOfSample(countForstockInBox)
                .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(area).equipment(entity).supportRack(supportRack)
                .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName());

            stockInBoxRepository.save(stockInBox);
            countOfSample += stockInBox.getCountOfSample();
            //保存入库盒子位置
            StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
            stockInBoxPosition = stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                .stockInBox(stockInBox);
            stockInBoxPositionRepository.save(stockInBoxPosition);
            //保存盒子以及盒内样本
            List<FrozenTube> frozenTubesLast = new ArrayList<>();
            List<StockInTube> stockInTubes = new ArrayList<>();
            if(sampleDatas!=null&&sampleDatas.size()>0){
                Map<String, List<net.sf.json.JSONObject>> f2 =
                    sampleDatas.stream().collect(Collectors.groupingBy(w -> w.getString("tubeCode")));
                List<String> wrongSample = new ArrayList<>();
                for(String key :f2.keySet()){
                    if(f2.get(key).size()>1){
                        alist.addAll(f2.get(key));
                    }
                }
                List<String> oldSampleCodes = new ArrayList<>();
                for (int j = 0; j < sampleDatas.size(); j++) {
                    String sampleCode = sampleDatas.get(j).get("tubeCode").toString();
                    if(sampleCode.contains("A")||sampleCode.contains("R")||sampleCode.contains("E")||sampleCode.contains("W")){
                        continue;
                    }
                    String boxColno = sampleDatas.get(j).getString("boxColno");
                    Integer boxRowno = Integer.valueOf(sampleDatas.get(j).getString("boxRowno"));
                    String row = "";
                    row = String.valueOf((char) (boxRowno + 64));
                    if (boxRowno >= 9) {
                        row = String.valueOf((char) (boxRowno + 65));
                    }
                    String tubeColumns = boxColno;
                    String tubeRows = row;
                    FrozenTube tube = frozenTubeList.stream().filter(s->s.getSampleCode().equals(sampleCode)).findFirst().orElse(null);
//                FrozenTube tube=frozenTubeList.stream().filter(s->s.getTubeColumns().equals(tubeColumns)&&s.getTubeRows().equals(tubeRows)).findFirst().orElse(null);

                    if (tube == null) {
                        SampleType sampleType = sampleTypeList.stream().filter(s->s.getSampleTypeCode().equals(type)).findFirst().orElse(null);
                        if(sampleType == null){
                            throw new BankServiceException("样本类型不存在！",type);
                        }
                        ProjectSampleClass projectSampleClass = projectSampleClasses.stream().filter(s->s.getSampleType().getSampleTypeCode().equals(type)).findFirst().orElse(null);
                        if(projectSampleClass == null){
                            throw new BankServiceException("样本类型"+type+"在0029项目下未配置样本分类！");
                        }
                        SampleClassification sampleClassification = projectSampleClass.getSampleClassification();
                        if(sampleClassification == null){
                            throw new BankServiceException("样本分类获取失败！");
                        }
                        tube = createFrozenTubeForNewStockIn(sampleType,sampleClassification,frozenTubeType,project,null,sampleDatas.get(j));

                    }
                    if(wrongSample.contains(sampleCode)){
                        FrozenTube tube1 = new FrozenTube();
                        BeanUtils.copyProperties(tube,tube1);
                        tube1.setId(null);
                        ArrayList arrayList = new ArrayList();
                        if(!StringUtils.isEmpty(tube.getMemo())){
                            arrayList.add(tube.getMemo());
                        }
                        arrayList.add("盒内重复样本");
                        tube1.setMemo(String.join(",",arrayList));
                        tube1.setSampleCode(sampleCode);
                        tube1.setFrozenBox(frozenBox);
                        tube1.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                        tube1.setTubeRows(tubeRows);
                        tube1.setTubeColumns(tubeColumns);
                        tube1.setFrozenTubeState(Constants.FROZEN_BOX_STOCKED);
                        frozenTubesLast.add(tube1);
                    }else{
                        wrongSample.add(sampleCode);
                        tube.setSampleCode(sampleCode);
                        tube.setFrozenBox(frozenBox);
                        tube.setFrozenBoxCode(frozenBox.getFrozenBoxCode());
                        tube.setTubeRows(tubeRows);
                        tube.setTubeColumns(tubeColumns);
                        tube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKED);
                        frozenTubesLast.add(tube);
                    }
//                frozenTubeRepository.saveAndFlush(tube);
                    executedOperations[0]++;
                    log.info("Finished:(%d/%d)"+executedOperations[0]+allOperations);
                }
            }else{
                for(FrozenTube tube : frozenTubeList){
                    tube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKED);
                    frozenTubesLast.add(tube);
                }
            }

            frozenTubeRepository.save(frozenTubesLast);
            stockInBox.setCountOfSample(frozenTubesLast.size());
            stockInBoxRepository.save(stockInBox);
            for(FrozenTube tube : frozenTubesLast){
                StockInTube stockInTube = new StockInTube()
                    .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                    .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                    .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                    .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                    .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                    .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                    .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                    .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                    .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                stockInTubes.add(stockInTube);
            }
            stockInTubeRepository.save(stockInTubes);
        }
        stockIn.setCountOfSample(countOfSample);
        stockInRepository.save(stockIn);
    }
   //佳通移位的具体实现
    private void importMoveRecordFor0029_JT(List<Map<String, Object>> boxList, String key) {
        String optDate = key.split("&")[0];//操作日期
        String boxCode = key.split("&")[1];//冻存盒编码
        Date stockInDate = null;
        try {
            stockInDate = strToDate(optDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.print("移位盒:" + boxCode);

        FrozenBox frozenBox = frozenBoxRepository.findByFrozenBoxCode1D(boxCode);
        if (frozenBox == null) {
            throw new BankServiceException("冻存盒不存在" + boxCode);
        }
        Map<String, Object> map = boxList.get(0);
        String optPerson1 = "张丽萍";
        String optPerson2 = "王铁柱";
        String memo = map.get("MEMO") != null ? map.get("MEMO").toString() : null;
        LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Long opt_user_id = null;
        Long opt_user_id_2 = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            opt_user_id = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            opt_user_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }
        //移位
        PositionMove positionMove = new PositionMove();
        positionMove = positionMove.moveAffect("无").moveReason("无").moveType(Constants.MOVE_TYPE_FOR_BOX)
            .memo(memo)
            .whetherFreezingAndThawing(false).positionMoveDate(date)
            .status(Constants.VALID).operatorId1(opt_user_id).operatorId2(opt_user_id_2);
        positionMoveRepository.save(positionMove);

        //移位入库详情
        //先查询到盒子的首次入库记录

        String equipmentCode = map.get("EQ_CODE").toString().trim();
        String areaCode = map.get("AREA") != null && !map.get("AREA").toString().equals("NA") ? map.get("AREA").toString().trim() : "S99";

        String supportCode = map.get("SHELF") != null && !map.get("SHELF").toString().equals("NA")
            ? map.get("SHELF").toString().trim() : "R99";

        //设备
        Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
        if (entity == null) {
            throw new BankServiceException("设备未导入:" + equipmentCode);
        }
        //区域
        Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
        if (area == null) {
            throw new BankServiceException("区域未导入:" + equipmentCode + areaCode);
        }
        //冻存架
        SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
        if (supportRack == null) {
            SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
            supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType)
                .supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                .area(supportRackMapper.areaFromId(area.getId()));
            supportRackRepository.save(supportRack);
        }
        String posInShelf = map.get("POS") != null ? map.get("POS").toString() : "NA";
        String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
        String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
        frozenBox = frozenBox.equipment(entity).equipmentCode(entity.getEquipmentCode())
            .area(area)
            .areaCode(area.getAreaCode())
            .supportRack(supportRack)
            .supportRackCode(supportRack.getSupportRackCode())
            .columnsInShelf(columnsInShelf)
            .rowsInShelf(rowsInShelf);
        String memoOld = frozenBox.getMemo();
        String memoLast = memo;
        if (!StringUtils.isEmpty(memoOld)) {
            memoLast = memoOld + "," + memo;
        }
        frozenBox.setMemo(memoLast);
        frozenBoxRepository.save(frozenBox);
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
        saveMoveDetail(positionMove, Constants.MOVE_TYPE_FOR_BOX, frozenTubeList);

    }
    //构造新的冻存管
    private FrozenTube createFrozenTubeForNewStockIn(SampleType sampleType, SampleClassification sampleClassification, FrozenTubeType frozenTubeType,Project project,FrozenTube oldTube, net.sf.json.JSONObject jsonObject) {

        FrozenTube frozenTube = new FrozenTube().projectCode("0029").projectSiteCode(oldTube!=null&&oldTube.getProjectSite()!=null?oldTube.getProjectSite().getProjectSiteCode():null)
            .sampleCode(sampleType.getSampleTypeCode())
            .frozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode())
            .frozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName())
            .sampleTypeCode(sampleType.getSampleTypeCode())
            .sampleTypeName(sampleType.getSampleTypeName())
            .sampleUsedTimesMost(frozenTubeType.getSampleUsedTimesMost())
            .sampleUsedTimes(oldTube!=null?oldTube.getSampleUsedTimes():0)
            .frozenTubeVolumns(frozenTubeType.getFrozenTubeVolumn())
            .frozenTubeVolumnsUnit(frozenTubeType.getFrozenTubeVolumnUnit())
            .tubeRows(null)
            .tubeColumns(null)
            .status(Constants.FROZEN_TUBE_NORMAL).memo(new Date()+"数据导入")
            .age(oldTube!=null?oldTube.getAge():null).gender(oldTube!=null?oldTube.getGender():null).patientId(oldTube!=null?oldTube.getPatientId():null).nation(oldTube!=null?oldTube.getNation():null)
            .frozenBoxCode(null).frozenTubeType(frozenTubeType).sampleType(sampleType).sampleClassification(sampleClassification)
            .project(project).projectSite(oldTube!=null?oldTube.getProjectSite():null).frozenBox(null).frozenTubeState("2004");
        return frozenTube;
    }
    //佳通出库的具体实现
    private void importBoxOutFromPeace2ForJT_1023(List<Map<String, Object>> boxList) {

        String applyCode = "";

        Date date = null;
        try {
            date = strToDate2(boxList.get(0).get("OLD_DATE").toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        applyCode = bankUtil.getUniqueIDByDate("C", date);
        String optPerson1 = "王铁柱";
        String optPerson2 = "张丽萍";
        //出库委托方
        Delegate delegate = delegateRepository.findByDelegateCode("D_000020");
        Project project = projectRepository.findByProjectCode("0029");
        String handOverPerson = "王铁柱";

        LocalDate stockOutDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ZonedDateTime createDate = date.toInstant().atZone(ZoneId.systemDefault());
        Long opt_user_id = null;
        Long opt_user_id_2 = null;
        Long over_person_id = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            opt_user_id = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            opt_user_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }
        if (!StringUtils.isEmpty(handOverPerson)) {
            over_person_id = Constants.RECEIVER_MAP.get(handOverPerson);
        }
        //创建出库申请

        StockOutApply stockOutApply = stockOutApplyRepository.findByApplyCode(applyCode);
        if (stockOutApply == null) {
            stockOutApply = new StockOutApply()
                .status(Constants.STOCK_OUT_APPROVED).applyCode(applyCode)
                .applyDate(stockOutDate).applyPersonName(null).delegate(delegate)
                .approverId(opt_user_id).approveTime(stockOutDate)
                .endTime(null).startTime(null).purposeOfSample(null).recordId(opt_user_id).recordTime(stockOutDate);
            stockOutApplyRepository.save(stockOutApply);
            StockOutApplyProject stockOutApplyProject = new StockOutApplyProject().project(project).stockOutApply(stockOutApply).status(Constants.VALID);
            stockOutApplyProjectRepository.save(stockOutApplyProject);
        }
        String stockOutPlanCode = bankUtil.getUniqueIDByDate("E", date);

        //创建出库计划
        StockOutPlan stockOutPlan = stockOutPlanRepository.findByStockOutPlanCode(stockOutPlanCode);
        if (stockOutPlan == null) {
            stockOutPlan = new StockOutPlan().stockOutPlanCode(stockOutPlanCode).stockOutApply(stockOutApply)
                .status(Constants.STOCK_OUT_PLAN_COMPLETED).stockOutPlanDate(stockOutDate)
                .applyNumber(stockOutApply.getApplyCode())
                .stockOutApply(stockOutApply);
            stockOutPlan = stockOutPlanRepository.save(stockOutPlan);
        }
        //创建出库需求
        String requirementCode = bankUtil.getUniqueIDByDate("D", date);
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findByRequirementCode(requirementCode);
        int countOfSample = 0;
        for(Map<String, Object> key: boxList){
            countOfSample+= Integer.valueOf(key.get("COUNT_OF_TUBE").toString());
        }
        if (stockOutRequirement == null) {
            stockOutRequirement = new StockOutRequirement().applyCode(applyCode).requirementCode(requirementCode).requirementName("出库" + countOfSample + "支样本")
                .status(Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS).stockOutApply(stockOutApply)
                .countOfSample(countOfSample).countOfSampleReal(countOfSample);
            stockOutRequirementRepository.save(stockOutRequirement);
        }
        //创建出库任务
        String taskCode = bankUtil.getUniqueIDByDate("F", date);
        StockOutTask stockOutTask = new StockOutTask()
            .status(Constants.STOCK_OUT_TASK_COMPLETED)
            .stockOutTaskCode(taskCode)
            .stockOutPlan(stockOutPlan).usedTime(0)
            .stockOutDate(stockOutDate).stockOutHeadId1(opt_user_id).stockOutHeadId2(opt_user_id_2)
            .taskStartTime(createDate).taskEndTime(createDate);
        stockOutTaskRepository.save(stockOutTask);
        String handOverTime = boxList.get(0).get("HANDOVER_DATE").toString();
        Date handOverDate = null;
        try {
            handOverDate = strToDate(handOverTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        LocalDate overdate = handOverDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        //创建出库交接
        String handOverCode = bankUtil.getUniqueIDByDate("G", handOverDate);
        StockOutHandover stockOutHandover = new StockOutHandover().handoverCode(handOverCode)
            .stockOutTask(stockOutTask)
            .stockOutApply(stockOutTask.getStockOutPlan().getStockOutApply())
            .stockOutPlan(stockOutTask.getStockOutPlan())
            .status(Constants.STOCK_OUT_HANDOVER_COMPLETED).handoverPersonId(over_person_id).handoverTime(overdate);

        stockOutHandoverRepository.save(stockOutHandover);

        int countOfStockOutReal = 0;
        for (Map<String, Object> key: boxList) {
            String boxCode1D = key.get("BOX_CODE_1").toString();
            FrozenBox frozenBox = frozenBoxRepository.findByFrozenBoxCode1D(boxCode1D);
            if (frozenBox == null) {
                log.info("冻存盒不存在！"+boxCode1D);
                continue;
            }
            String equipmentCode = "F1-01";
            String areaCode = "S01";
            String memo = key.get("MEMO") != null ? key.get("MEMO").toString() : null;
            ArrayList<String> memoList1 = new ArrayList<>();
            if(!StringUtils.isEmpty(frozenBox.getMemo())){
                memoList1.add(frozenBox.getMemo());
            }
            if(memo!=null){
                memoList1.add(memo);
            }
            Equipment equipment = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, equipment.getId());
            frozenBox.status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER).areaCode(areaCode).area(area).equipment(equipment)
                .equipmentCode(equipmentCode)
                .supportRackCode(null).supportRack(null).columnsInShelf(null).rowsInShelf(null).memo(String.join(",", memoList1));
            frozenBoxRepository.save(frozenBox);
            //保存出库盒
            StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_HANDOVER);
            stockOutFrozenBox.setFrozenBox(frozenBox);
            stockOutFrozenBox.setStockOutTask(stockOutTask);
            stockOutFrozenBox = stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode())
                .frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).
                    sampleTypeCode(frozenBox.getSampleTypeCode())
                .sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .areaCode(areaCode).area(area).equipment(equipment).equipmentCode(equipmentCode)
                .supportRack(null).supportRackCode(null).columnsInShelf(null).rowsInShelf(null)
                .projectSiteName(frozenBox.getProjectSiteName()).memo(frozenBox.getMemo());
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
            //保存冻存盒位置
            StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition();

            stockOutBoxPosition.setStockOutFrozenBox(stockOutFrozenBox);
            stockOutBoxPosition.setEquipment(equipment);
            stockOutBoxPosition.setEquipmentCode(equipment != null ? equipment.getEquipmentCode() : null);
            stockOutBoxPosition.setArea(area);
            stockOutBoxPosition.setAreaCode(area != null ? area.getAreaCode() : null);
            stockOutBoxPosition.setStatus(Constants.VALID);
            stockOutBoxPosition.setCreatedDate(createDate);
            stockOutBoxPositionRepository.save(stockOutBoxPosition);

            StockOutHandoverBox stockOutHandoverBox = new StockOutHandoverBox().stockOutHandover(stockOutHandover)
                .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                .equipmentCode(stockOutFrozenBox.getEquipmentCode())
                .areaCode(stockOutFrozenBox.getAreaCode())
                .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                .rowsInShelf(stockOutFrozenBox.getRowsInShelf())
                .columnsInShelf(stockOutFrozenBox.getColumnsInShelf())
                .frozenBoxCode(stockOutFrozenBox.getFrozenBoxCode())
                .frozenBoxCode1D(stockOutFrozenBox.getFrozenBoxCode1D())
                .area(stockOutFrozenBox.getArea())
                .equipment(stockOutFrozenBox.getEquipment())
                .supportRack(stockOutFrozenBox.getSupportRack())
                .sampleTypeCode(stockOutFrozenBox.getSampleTypeCode())
                .sampleType(stockOutFrozenBox.getSampleType())
                .sampleTypeName(stockOutFrozenBox.getSampleTypeName())
                .sampleClassification(stockOutFrozenBox.getSampleClassification())
                .sampleClassificationCode(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(stockOutFrozenBox.getDislocationNumber())
                .emptyHoleNumber(stockOutFrozenBox.getEmptyHoleNumber())
                .emptyTubeNumber(stockOutFrozenBox.getEmptyTubeNumber())
                .frozenBoxType(stockOutFrozenBox.getFrozenBoxType())
                .frozenBoxTypeCode(stockOutFrozenBox.getFrozenBoxTypeCode())
                .frozenBoxTypeColumns(stockOutFrozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(stockOutFrozenBox.getFrozenBoxTypeRows())
                .isRealData(stockOutFrozenBox.getIsRealData())
                .isSplit(stockOutFrozenBox.getIsSplit())
                .project(stockOutFrozenBox.getProject())
                .projectCode(stockOutFrozenBox.getProjectCode())
                .projectName(stockOutFrozenBox.getProjectName())
                .projectSite(stockOutFrozenBox.getProjectSite())
                .projectSiteCode(stockOutFrozenBox.getProjectSiteCode())
                .projectSiteName(stockOutFrozenBox.getProjectSiteName())

                .stockOutFrozenBox(stockOutFrozenBox)
                .memo(stockOutFrozenBox.getMemo())
                .status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);
            stockOutHandoverBoxRepository.save(stockOutHandoverBox);
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
            List<FrozenTube> frozenTubeListLast = new ArrayList<>();
            List<StockOutReqFrozenTube> stockOutReqFrozenTubes = new ArrayList<>();
            List<StockOutHandoverDetails> stockOutHandoverDetailss = new ArrayList<>();
            for (FrozenTube frozenTube : frozenTubeList) {
                int times = frozenTube.getSampleUsedTimes() != null ? (frozenTube.getSampleUsedTimes() + 1) : 1;
                frozenTube.setSampleUsedTimes(times);
                frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);

                ArrayList<String> memoList = new ArrayList<>();
                if (!StringUtils.isEmpty(frozenTube.getMemo())) {
                    memoList.add(frozenTube.getMemo());
                }
                if (!StringUtils.isEmpty(memo)) {
                    memoList.add(memo);
                }
                frozenTube.setMemo(String.join(",", memoList));
                frozenTubeListLast.add(frozenTube);
//                frozenTubeRepository.saveAndFlush(frozenTube);

                StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube().status(Constants.STOCK_OUT_SAMPLE_COMPLETED)
                    .stockOutRequirement(stockOutRequirement).memo(String.join(",", memoList))
                    .frozenBox(frozenTube.getFrozenBox())
                    .frozenTube(frozenTube)
                    .tubeColumns(frozenTube.getTubeColumns())
                    .tubeRows(frozenTube.getTubeRows()).stockOutFrozenBox(stockOutFrozenBox).stockOutTask(stockOutTask);
                stockOutReqFrozenTube = stockOutReqFrozenTube.frozenBoxCode1D(frozenTube.getFrozenBox().getFrozenBoxCode1D())
                    .frozenBoxCode(frozenTube.getFrozenBoxCode()).errorType(frozenTube.getErrorType())
                    .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
                    .frozenTubeTypeId(frozenTube.getFrozenTubeType().getId()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                    .projectId(frozenTube.getProject() != null ? frozenTube.getProject().getId() : null).projectCode(frozenTube.getProjectCode())
                    .projectSiteId(frozenTube.getProjectSite() != null ? frozenTube.getProjectSite().getId() : null)
                    .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassificationId(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getId() : null)
                    .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                    .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode())
                    .sampleTypeId(frozenTube.getSampleType() != null ? frozenTube.getSampleType().getId() : null)
                    .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                    .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());
                stockOutReqFrozenTubes.add(stockOutReqFrozenTube);
//                stockOutReqFrozenTubeRepository.saveAndFlush(stockOutReqFrozenTube);
                //保存交接详情
                StockOutHandoverDetails stockOutHandoverDetails = new StockOutHandoverDetails();
                stockOutHandoverDetails = stockOutHandoverDetails.status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)
                    .stockOutReqFrozenTube(stockOutReqFrozenTube)
                    .stockOutHandoverBox(stockOutHandoverBox);
                stockOutHandoverDetails.setMemo(String.join(",", memoList));
                stockOutHandoverDetailss.add(stockOutHandoverDetails);
//                stockOutHandoverDetailsRepository.saveAndFlush(stockOutHandoverDetails);
            }
            frozenTubeRepository.save(frozenTubeListLast);
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubes);
            stockOutHandoverDetailsRepository.save(stockOutHandoverDetailss);
            executedOperations[0]++;
            countOfStockOutReal+=stockOutReqFrozenTubes.size();
            log.info("Finished:(%d/%d)"+ executedOperations[0]+"/" +allOperations);
        }
        stockOutApply.countOfStockSample(countOfStockOutReal).countOfHandOverSample(countOfStockOutReal);
        stockOutPlan.countOfStockOutPlanSample(countOfStockOutReal);
        stockOutTask.countOfHandOverSample(countOfStockOutReal).countOfStockOutSample(countOfStockOutReal);
        stockOutHandover.countOfHandoverSample(countOfStockOutReal);
        stockOutApplyRepository.save(stockOutApply);
        stockOutPlanRepository.save(stockOutPlan);
        stockOutTaskRepository.save(stockOutTask);
        stockOutHandoverRepository.save(stockOutHandover);
    }
    ///////////////////////////////////////////////////佳通导入结束///////////////////////////////////////


    ///////////////////////////////////////////////////0037项目导入开始///////////////////////////////////////
    //创建0037项目的冻存盒----暂不使用
    public void createFrozenBoxFor0037(String tableName, String sampleTypeCode, FrozenTubeType frozenTubeType) throws Exception {
        Project project = projectRepository.findByProjectCode("0037");
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode(sampleTypeCode);
        if (sampleType == null) {
            throw new BankServiceException("样本类型为空！表名为：" + tableName + "样本类型编码为：" + sampleTypeCode);
        }
        if (frozenTubeType == null) {
            throw new BankServiceException("冻存管类型为空！表名为：" + tableName + "样本类型编码为：" + sampleTypeCode);
        }
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = " select a.*,to_date(a.opt_date,'MM/dd/YYYY') as time  from " + tableName + " a order by time";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, Equipment> equipmentCodeMap = new HashMap<String, Equipment>();
        TreeMap<String, List<Map<String, Object>>> map = new TreeMap<>(list.stream().collect(Collectors.groupingBy(s -> s.get("OPT_DATE").toString() + "&" + s.get("BOX_CODE").toString() + "&" + s.get("OPT").toString())));

        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode(sampleTypeCode.equals("RNA") ? "DJH" : "DCH");
        int m = 0;
        for (String key : map.keySet()) {
            m++;
            String opt = key.split("&")[2];//操作名称(首次或移位)
            List<Map<String, Object>> boxList = map.get(key);
            switch (opt) {
                case "首次入库":
                    importStockInFor0037first(frozenTubeType, sampleType, key, boxList, sampleTypeCode, project, frozenBoxType);
                    break;
                case "移位入库":
                    importStockInFor0037Move(boxList, key);
                    break;
                default:
                    break;
            }
        }
    }
    //导入0037首次入库记录的具体实现----暂不使用
    private void importStockInFor0037first(FrozenTubeType frozenTubeType, SampleType sampleType, String key, List<Map<String, Object>> boxList, String sampleTypeCode, Project project, FrozenBoxType frozenBoxType) throws ParseException {
        String optDate = key.split("&")[0];//操作日期
        String boxCode = key.split("&")[1];//冻存盒编码

        Date stockInDate = null;
        try {
            stockInDate = strToDate(optDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String sampleClassTypeCode = sampleTypeCode != "RNA" ? boxList.get(0).get("SAMPLE_TYPE_CODE").toString() : "11";
        String sampleClassTypeName = Constants.SAMPLE_TYPE_MAP.get(sampleClassTypeCode);
        Long LCC_ID = null;
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelectSite = "select DISTINCT LCC_ID from biobank_temp_01.fen_he_ji_lu where SAMPLE_ID = ?";
            PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sqlForSelectSite);
            pstmt.setString(1, boxList.get(0).get("SAMPLE_CODE").toString());
            result = pstmt.executeQuery();// 执行查询，注意括号中不需要再加参数
            while (result.next()) {
                LCC_ID = result.getLong("LCC_ID");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ProjectSite projectSite = null;
        if (LCC_ID != null) {
            projectSite = projectSiteRepository.findByProjectSiteCode(LCC_ID.toString());
        }
        String stockInCode = bankUtil.getUniqueIDByDate("B", stockInDate);
        StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCode);
        String receiver = boxList.get(0).get("ECEIVER") != null ? boxList.get(0).get("ECEIVER").toString() : null;
//            String opt = sampleList.get(0).get("OPT")!=null?sampleList.get(0).get("OPT").toString():null;
        String storeKeeper1 = boxList.get(0).get("USER_1") != null ? boxList.get(0).get("USER_1").toString() : null;
        String storeKeeper2 = boxList.get(0).get("USER_2") != null ? boxList.get(0).get("USER_2").toString() : null;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        LocalDate receiveDate = boxList.get(0).get("REC_DATE") != null && !boxList.get(0).get("REC_DATE").equals("NA") ? format.parse(boxList.get(0).get("REC_DATE").toString()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

        Long receiverId = null;
        String stockInType = null;
        Long storeKeeperId1 = null;
        Long storeKeeperId2 = null;
        if (!StringUtils.isEmpty(receiver)) {
            receiverId = Constants.RECEIVER_MAP.get(receiver);
        }
        if (!StringUtils.isEmpty(storeKeeper1)) {
            storeKeeperId1 = Constants.RECEIVER_MAP.get(storeKeeper1);
        }
        if (!StringUtils.isEmpty(storeKeeper2)) {
            storeKeeperId2 = Constants.RECEIVER_MAP.get(storeKeeper2);
        }
        if (stockIn == null) {
            stockIn = new StockIn().projectCode("0037")
                .projectSiteCode(LCC_ID != null ? LCC_ID.toString() : null)
                .receiveId(receiverId).receiveDate(receiveDate)
                .receiveName(receiver)
                .stockInType("8001")
                .storeKeeperId1(storeKeeperId1)
                .storeKeeper1(storeKeeper1)
                .storeKeeperId2(storeKeeperId2)
                .storeKeeper2(storeKeeper2)
                .stockInDate(stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .countOfSample(boxList.size()).stockInCode(stockInCode).project(project).projectSite(projectSite)
                .memo("PEACE5 项目" + LocalDate.now() + "数据导入")
                .status(Constants.STOCK_IN_COMPLETE);
            stockInRepository.save(stockIn);
        }
        Map<String, List<Map<String, Object>>> tubeForBoxMap = boxList.stream().collect(Collectors.groupingBy(s -> s.get("BOX_CODE").toString()));
        for (String box : tubeForBoxMap.keySet()) {
            List<Map<String, Object>> sampleList = tubeForBoxMap.get(box);
            if (sampleTypeCode != "RNA" && sampleList.get(0).get("POS_IN_SHELF") == null) {
                throw new BankServiceException("所在架子内位置为空！盒子编码：" + boxCode);
            }

            ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectCodeAndSampleTypeCodeAndSampleClassificationCode("0037", sampleTypeCode, sampleClassTypeCode);
            SampleClassification sampleClassification = projectSampleClass.getSampleClassification();

            String equipmentCode = sampleList.get(0).get("EQ_CODE").toString().trim();
            String areaCode = sampleList.get(0).get("AREA_CODE").toString().trim();
            String supportCode = "";
            if (sampleTypeCode != "RNA") {
                supportCode = sampleList.get(0).get("SHELF_CODE").toString().trim();
            } else {
                Long count = frozenBoxRepository.countByEquipmentCodeAndAreaCode(equipmentCode, areaCode);
                if (count.intValue() < 25) {
                    supportCode = "R01";
                } else {
                    supportCode = "R02";
                }
            }
            //设备
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            if (entity == null) {
                throw new BankServiceException("设备未导入:" + equipmentCode);
            }
            //区域
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
            if (area == null) {
                throw new BankServiceException("设备未导入:" + areaCode);
            }
            //冻存架
            SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
            if (supportRack == null) {
                throw new BankServiceException("冻存架未导入:" + supportRack);
            }
            String pos = null;
            String columns[] = new String[]{"A", "B", "C", "D", "E"};
            Boolean flag = false;
            if (sampleTypeCode.equals("RNA")) {
                for (int j = 0; j < columns.length; j++) {
                    for (int i = 1; i <= 5; i++) {
                        String bb = columns[j] + i;
                        FrozenBox frozenBox = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(equipmentCode, areaCode, supportCode, columns[j], String.valueOf(i));
                        if (frozenBox != null) {
                            continue;
                        } else {
                            flag = true;
                            pos = bb;
                            break;
                        }
                    }
                    if (flag) {
                        break;
                    }
                }
            }
            String posInShelf = sampleTypeCode != "RNA" ? sampleList.get(0).get("POS_IN_SHELF").toString() : pos;
            String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
            String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
            //保存冻存盒
            FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
            if (frozenBox == null) {
                frozenBox = new FrozenBox()
                    .frozenBoxCode(boxCode)
                    .frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode())
                    .frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows())
                    .frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                    .projectCode(project.getProjectCode())
                    .projectName(project.getProjectName())
                    .projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null)
                    .projectSiteName(projectSite != null ? projectSite.getProjectSiteName() : null)
                    .equipmentCode(entity.getEquipmentCode())
                    .areaCode(areaCode)
                    .supportRackCode(supportCode)
                    .sampleTypeCode(sampleType.getSampleTypeCode())
                    .sampleTypeName(sampleType.getSampleTypeName())
                    .isSplit(0)
                    .status("2004")
                    .emptyTubeNumber(0)
                    .emptyHoleNumber(0)
                    .dislocationNumber(0)
                    .isRealData(1).frozenBoxType(frozenBoxType).sampleType(sampleType).sampleClassification(sampleClassification).equipment(entity).area(area)
                    .supportRack(supportRack)
                    .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).project(project).projectSite(projectSite);
            }
            frozenBoxRepository.save(frozenBox);
            assertThat(frozenBox).isNotNull();

            //保存入库盒
            List<StockInBox> stockInBoxList = stockInBoxRepository.findByFrozenBoxCode(key);
            StockInBox stockInBox = new StockInBox();
            if (stockInBoxList == null || stockInBoxList.size() == 0) {
                stockInBox = new StockInBox()
                    .equipmentCode(equipmentCode)
                    .areaCode(areaCode)
                    .supportRackCode(supportCode)
                    .rowsInShelf(rowsInShelf)
                    .columnsInShelf(columnsInShelf)
                    .status(Constants.FROZEN_BOX_STOCKED).countOfSample(sampleList.size())
                    .frozenBoxCode(boxCode).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(area).equipment(entity).supportRack(supportRack)
                    .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockInBoxRepository.save(stockInBox);
                assertThat(stockInBox).isNotNull();
            } else {
                stockInBox = stockInBoxList.get(0);
            }
            //保存入库盒子位置
            List<StockInBoxPosition> stockInBoxPositionList = stockInBoxPositionRepository.findByStockInBoxIdAndStatus(stockInBox.getId(), Constants.STOCK_IN_BOX_POSITION_COMPLETE);
            if (stockInBoxPositionList == null || stockInBoxPositionList.size() == 0) {
                StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
                stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                    .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                    .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                    .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                    .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                    .stockInBox(stockInBox);
                stockInBoxPositionRepository.save(stockInBoxPosition);
                assertThat(stockInBoxPosition).isNotNull();
            }
            //保存入库管子
            for (int i = 0; i < sampleList.size(); i++) {
                if (sampleList.get(i).get("SAMPLE_CODE") == null) {
                    throw new BankServiceException("样本编码为空！盒子编码：" + boxCode, sampleList.get(i).toString());
                }
                String sampleCode = sampleList.get(i).get("SAMPLE_CODE").toString();

                if (sampleTypeCode != "RNA" && sampleList.get(i).get("POS_IN_BOX") == null) {
                    throw new BankServiceException("盒内位置为空！盒子编码：" + boxCode + ",样本编码为：" + sampleCode, sampleList.get(i).toString());
                }
                String posInBox = sampleList.get(i).get("POS_IN_BOX").toString();
                String tubeRows = posInBox.substring(0, 1);
                String tubeColumns = posInBox.substring(1);
                String status = sampleList.get(i).get("SAMPLE_STATUS") != null ? Constants.FROZEN_TUBE_STATUS_MAP.get(sampleList.get(i).get("SAMPLE_STATUS").toString()).toString() : "3001";
                String memo = sampleList.get(i).get("MEMO") != null ? sampleList.get(i).get("MEMO").toString() : null;
                String times = sampleList.get(i).get("TIMES") != null ? sampleList.get(i).get("TIMES").toString() : null;

                FrozenTube tube = new FrozenTube()
                    .projectCode("0037").projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null)
                    .sampleCode(sampleCode)
                    .frozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName())
                    .sampleTypeCode(sampleType.getSampleTypeCode())
                    .sampleTypeName(sampleType.getSampleTypeName())
                    .sampleUsedTimesMost(frozenTubeType.getSampleUsedTimesMost())
                    .sampleUsedTimes(0)
                    .frozenTubeVolumns(frozenTubeType.getFrozenTubeVolumn())
                    .frozenTubeVolumnsUnit(frozenTubeType.getFrozenTubeVolumnUnit())
                    .tubeRows(tubeRows)
                    .tubeColumns(tubeColumns)
                    .status(status).memo(memo).sampleStage(times)
                    .frozenBoxCode(boxCode).frozenTubeType(frozenTubeType).sampleType(sampleType).sampleClassification(sampleClassification)
                    .project(project).projectSite(projectSite).frozenBox(frozenBox).frozenTubeState("2004");
                frozenTubeRepository.save(tube);

                StockInTube stockInTube = new StockInTube()
                    .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                    .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                    .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                    .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                    .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                    .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                    .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                    .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                    .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                stockInTubeRepository.save(stockInTube);
            }
        }
    }
    //导入0037项目
    @Test
    public void importOptFor0037(){
        allOperations = 0;
        executedOperations[0] = 0;
        Project project = projectRepository.findByProjectCode("0037");
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode("99");
        SampleType sampleTypeRNA = sampleTypeRepository.findBySampleTypeCode("RNA");
//        List<SampleType> sampleTypes = sampleTypeRepository.findAllSampleTypes();
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        FrozenBoxType frozenBoxTypeRNA = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DJH");
        FrozenTubeType dcgTube = frozenTubeTypeRepository.findByFrozenTubeTypeCode("RNA");
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        List<ProjectSampleClass> projectSampleClassList = projectSampleClassRepository.findByProjectIdAndSampleTypeId(project.getId(),sampleType.getId());
        //获取0037项目的转运计划记录
        List<Map<String, Object>> transhipList = getTranshipList0037();
        //获取0037项目的小盒分盒记录
        List<Map<String, Object>> transhipBoxList = getTranshipBoxList0037();
        //获取0037项目RNA类型的数据
        List<Map<String, Object>> RNAList = getRNADataFor0037();
        List<Map<String, Object>> RNAFirstStockInList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> rnalistGroupByOpt =
            RNAList.stream().collect(Collectors.groupingBy(w -> w.get("OPT").toString()));
        for(String opt:rnalistGroupByOpt.keySet()){
            switch (opt){
                case "首次入库":RNAFirstStockInList =rnalistGroupByOpt.get(opt);break;
                default:break;
            }
        }
        Map<String, List<Map<String, Object>>> rnalistGroupByLccAndDate =
            RNAFirstStockInList.stream().collect(Collectors.groupingBy(w -> w.get("TRANSHIP_DATE_REAL").toString() + "&" + w.get("LCCID").toString()));

        Map<String, List<Map<String, Object>>> listGroupByBoxForCount =
            transhipBoxList.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));

        Map<String, List<Map<String, Object>>> listGroupByDateAndLcc =
            transhipBoxList.stream().collect(Collectors.groupingBy(w -> w.get("TRANSHIP_DATE_REAL").toString() + "&" + w.get("LCC_ID").toString()));
        allOperations = listGroupByBoxForCount.size()+RNAFirstStockInList.stream().collect(Collectors.groupingBy(w -> w.get("REC_BOX_CODE").toString())).size();
        List<String> boxCodeRNAAll = new ArrayList<>();
        Map<String, List<Map<String, Object>>> bo = RNAFirstStockInList.stream().collect(Collectors.groupingBy(w -> w.get("REC_BOX_CODE").toString()));
        for(Map<String, Object> transhipMap :transhipList) {
            //每一条转运记录获取一次转运盒
            String lcc_id = transhipMap.get("LCC_ID").toString();
            String trackNumber = transhipMap.get("TRACK_NUMBER").toString();
            ProjectRelate projectRelate = projectRelateRepository.findByProjectCodeAndProjectSiteCode("0037", lcc_id);
            if (projectRelate == null) {
                throw new BankServiceException(transhipMap.toString());
            }
            ProjectSite projectSite = projectRelate.getProjectSite();
            String transhipDateStr = transhipMap.get("TRANSHIP_DATE_REAL").toString();

            Date transhipDate = null;
            try {
                transhipDate = strToDate2(transhipDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //获取RNA的转运记录
            List<Map<String, Object>> transhipBoxsForRNA = rnalistGroupByLccAndDate.get(transhipDateStr + "&" + lcc_id);
            if (transhipBoxsForRNA == null) {
                transhipBoxsForRNA = new ArrayList<>();
            }
            Map<String, List<Map<String, Object>>> listGroupByBoxRNA =
                transhipBoxsForRNA.stream().collect(Collectors.groupingBy(w -> w.get("REC_BOX_CODE").toString()));

            //获取11个类型的小盒分装记录
            List<Map<String, Object>> transhipBoxs = listGroupByDateAndLcc.get(transhipDateStr + "&" + lcc_id);
            Map<String, List<Map<String, Object>>> listGroupByBox =
                transhipBoxs.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));

            LocalDate transhipDateNew = transhipDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String transhipCode = bankUtil.getUniqueIDByDate("A", transhipDate);

            String receiver = "NA";
            Long receiverId = Constants.RECEIVER_MAP.get("NA");
            //保存转运记录
            Tranship tranship = new Tranship().transhipDate(transhipDateNew)
                .project(project).projectCode(project.getProjectCode()).projectName(project.getProjectName()).projectSite(projectSite).projectSiteCode(projectSite.getProjectSiteCode())
                .projectSiteName(projectSite.getProjectSiteName()).trackNumber(trackNumber).transhipBatch(null).transhipState(Constants.TRANSHIPE_IN_COMPLETE)
                .receiverId(receiverId).receiver(receiver).receiveDate(transhipDateNew).sampleNumber(0).frozenBoxNumber(listGroupByBoxRNA.size()).emptyTubeNumber(0)
                .emptyHoleNumber(0).sampleSatisfaction(5).effectiveSampleNumber(0).memo(null).status(Constants.VALID)
                .transhipCode(transhipCode).tempEquipmentId(null).tempEquipmentCode(null).tempAreaId(null).tempAreaCode(null);
            transhipRepository.save(tranship);
            int emptyTubeNumberForTranip = 0;
            int countOfTube = 0;
            int yangbenrenfen = 0;
            for (String boxCodeRNA : listGroupByBoxRNA.keySet()) {
                log.info("导入RNA冻存盒：" + boxCodeRNA);
                List<Map<String, Object>> tubeList = listGroupByBoxRNA.get(boxCodeRNA);
                //保存冻存盒
                FrozenBox frozenBox = new FrozenBox().frozenBoxCode1D(null).frozenBoxCode(boxCodeRNA).frozenBoxTypeCode(frozenBoxTypeRNA.getFrozenBoxTypeCode()).frozenBoxTypeRows(frozenBoxTypeRNA.getFrozenBoxTypeRows()).frozenBoxTypeColumns(frozenBoxTypeRNA.getFrozenBoxTypeColumns())
                    .projectCode(project.getProjectCode()).projectName(project.getProjectName()).projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null).projectSiteName(projectSite != null ? projectSite.getProjectSiteName() : null)
                    .sampleTypeCode(sampleTypeRNA.getSampleTypeCode()).sampleTypeName(sampleTypeRNA.getSampleTypeName()).isSplit(1).status(Constants.FROZEN_BOX_TRANSHIP_COMPLETE)
                    .emptyTubeNumber(0).emptyHoleNumber(0).dislocationNumber(0).isRealData(1).frozenBoxType(frozenBoxTypeRNA).sampleType(sampleTypeRNA).sampleClassification(null)
                    .project(project).projectSite(projectSite);
                frozenBoxRepository.save(frozenBox);
                //保存转运盒
                TranshipBox transhipBox = new TranshipBox().status(Constants.FROZEN_BOX_TRANSHIP_COMPLETE).frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBox(frozenBox).tranship(tranship);
                transhipBox.setCountOfSample(tubeList.size());
                transhipBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
                transhipBoxRepository.save(transhipBox);
                //保存转运盒位置
                TranshipBoxPosition transhipBoxPosition = new TranshipBoxPosition();
                transhipBoxPosition.transhipBox(transhipBox).memo(transhipBox.getMemo()).status(Constants.INVALID)
                    .equipment(transhipBox.getFrozenBox().getEquipment()).equipmentCode(transhipBox.getEquipmentCode())
                    .area(transhipBox.getFrozenBox().getArea()).areaCode(transhipBox.getAreaCode())
                    .supportRack(transhipBox.getFrozenBox().getSupportRack()).supportRackCode(transhipBox.getSupportRackCode())
                    .rowsInShelf(transhipBox.getRowsInShelf()).columnsInShelf(transhipBox.getColumnsInShelf());
                transhipBoxPositionRepository.save(transhipBoxPosition);
                ArrayList<String> memoList = new ArrayList<>();
                List<FrozenTube> frozenTubeList = new ArrayList<>();
                List<TranshipTube> transhipTubeList = new ArrayList<>();
                int emptyTubeNumber = 0;
                int renfen = 0;
                for (Map<String, Object> tubeMap : tubeList) {
                    String posInBox = tubeMap.get("POS_IN_BOX").toString();
                    String rowNum = posInBox.substring(0, 1);
                    String colNum = posInBox.substring(1);
                    String sampleCode = tubeMap.get("SAMPLE_CODE").toString();
                    FrozenTube frozenTube = new FrozenTube().sampleType(sampleType).projectCode("0037").projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null)
                        .sampleCode(sampleCode).frozenTubeTypeCode(dcgTube.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(dcgTube.getFrozenTubeTypeName())
                        .sampleTypeCode(sampleTypeRNA.getSampleTypeCode())
                        .sampleTypeName(sampleTypeRNA.getSampleTypeName())
                        .sampleUsedTimesMost(dcgTube.getSampleUsedTimesMost())
                        .sampleUsedTimes(0)
                        .frozenTubeVolumns(dcgTube.getFrozenTubeVolumn())
                        .frozenTubeVolumnsUnit(dcgTube.getFrozenTubeVolumnUnit())
                        .tubeRows(rowNum)
                        .tubeColumns(colNum)
                        .status(Constants.FROZEN_TUBE_NORMAL).memo(null)
                        .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenTubeType(dcgTube).sampleType(sampleType).sampleClassification(null)
                        .project(project).projectSite(projectSite).frozenBox(frozenBox).frozenTubeState(Constants.FROZEN_BOX_TRANSHIP_COMPLETE);
                    frozenTubeList.add(frozenTube);
                    getTranshipTubeFor99Type(frozenTube, transhipBox, transhipTubeList);
                    renfen++;
                }
                frozenBox.emptyTubeNumber(emptyTubeNumber).memo(String.join(",", memoList));
                transhipBox.memo(String.join(",", memoList)).emptyTubeNumber(emptyTubeNumber).countOfSample(frozenTubeList.size());
                countOfTube += frozenTubeList.size();
                emptyTubeNumberForTranip += emptyTubeNumber;
                yangbenrenfen += renfen;
                frozenBoxRepository.save(frozenBox);
                transhipBoxRepository.save(transhipBox);
                frozenTubeRepository.save(frozenTubeList);
                transhipTubeRepository.save(transhipTubeList);
                executedOperations[0]++;
                log.info("Finished:(%d/%d)" + executedOperations[0] + "/" + allOperations);

            }
            for (String boxCode99 : listGroupByBox.keySet()) {

                log.info("导入冻存盒：" + boxCode99);
                List<Map<String, Object>> tubeList = listGroupByBox.get(boxCode99);
                String tempPos = tubeList.get(0).get("TEMP_POS").toString();
                String equipmentCode = "";
                String areaCode = "";
                Equipment equipment = new Equipment();
                Area area = null;
                if (tempPos.contains(".")) {
                    String[] pos = tempPos.split("\\.");
                    equipmentCode = pos[0];
                    areaCode = pos[1];
                } else {
                    equipmentCode = tempPos;
                }
                equipment = equipmentRepository.findOneByEquipmentCode(equipmentCode);
                if (!StringUtils.isEmpty(areaCode)) {
                    area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, equipment.getId());
                }
                //保存冻存盒
                FrozenBox frozenBox = new FrozenBox().frozenBoxCode1D(null).frozenBoxCode(boxCode99).frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode()).frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows()).frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                    .projectCode(project.getProjectCode()).projectName(project.getProjectName()).projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null).projectSiteName(projectSite != null ? projectSite.getProjectSiteName() : null)
                    .equipmentCode(equipment != null ? equipment.getEquipmentCode() : null).areaCode(area != null ? area.getAreaCode() : null).supportRackCode(null).sampleTypeCode(sampleType.getSampleTypeCode()).sampleTypeName(sampleType.getSampleTypeName()).isSplit(1).status(Constants.FROZEN_BOX_TRANSHIP_COMPLETE)
                    .emptyTubeNumber(0).emptyHoleNumber(0).dislocationNumber(0).isRealData(1).frozenBoxType(frozenBoxType).sampleType(sampleType).sampleClassification(null).equipment(equipment).area(area)
                    .supportRack(null).columnsInShelf(null).rowsInShelf(null).project(project).projectSite(projectSite);
                frozenBoxRepository.save(frozenBox);
                //保存转运盒
                TranshipBox transhipBox = new TranshipBox().equipmentCode(equipmentCode).areaCode(areaCode).supportRackCode(null).columnsInShelf(null).rowsInShelf(null)
                    .status(Constants.FROZEN_BOX_TRANSHIP_COMPLETE).frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBox(frozenBox).tranship(tranship);
                transhipBox.setCountOfSample(tubeList.size());
                transhipBox.equipment(equipment).area(area).supportRack(null).sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName()).frozenBoxCode1D(null);
                transhipBoxRepository.save(transhipBox);
                //保存转运盒位置
                TranshipBoxPosition transhipBoxPosition = new TranshipBoxPosition();
                transhipBoxPosition.transhipBox(transhipBox).memo(transhipBox.getMemo()).status(Constants.INVALID)
                    .equipment(transhipBox.getFrozenBox().getEquipment()).equipmentCode(transhipBox.getEquipmentCode())
                    .area(transhipBox.getFrozenBox().getArea()).areaCode(transhipBox.getAreaCode())
                    .supportRack(transhipBox.getFrozenBox().getSupportRack()).supportRackCode(transhipBox.getSupportRackCode())
                    .rowsInShelf(transhipBox.getRowsInShelf()).columnsInShelf(transhipBox.getColumnsInShelf());
                transhipBoxPositionRepository.save(transhipBoxPosition);

                ArrayList<String> memoList = new ArrayList<>();
                List<FrozenTube> frozenTubeList = new ArrayList<>();
                List<TranshipTube> transhipTubeList = new ArrayList<>();
                int emptyTubeNumber = 0;
                int renfen = 0;
                for (Map<String, Object> tubeMap : tubeList) {
                    String rowNum = tubeMap.get("ROW_NO").toString();
                    String sampleCode = tubeMap.get("SAMPLE_ID").toString();
                    String memo = tubeMap.get("MEMO") != null ? tubeMap.get("MEMO").toString() : null;
                    if (memo != null) {
                        memoList.add(memo);
                    }
                    String col01 = tubeMap.get("COL_01") != null ? tubeMap.get("COL_01").toString() : null;
                    FrozenTube frozenTube01 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "01", frozenTubeType, frozenBox, project, projectSite, rowNum, "1", col01, sampleCode, frozenTubeList, emptyTubeNumber);

                    String col02 = tubeMap.get("COL_02") != null ? tubeMap.get("COL_02").toString() : null;
                    FrozenTube frozenTube02 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "02", frozenTubeType, frozenBox, project, projectSite, rowNum, "2", col02, sampleCode, frozenTubeList, emptyTubeNumber);

                    String col03 = tubeMap.get("COL_03") != null ? tubeMap.get("COL_03").toString() : null;
                    FrozenTube frozenTube03 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "03", frozenTubeType, frozenBox, project, projectSite, rowNum, "3", col03, sampleCode, frozenTubeList, emptyTubeNumber);

                    String col04 = tubeMap.get("COL_04") != null ? tubeMap.get("COL_04").toString() : null;
                    FrozenTube frozenTube04 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "04", frozenTubeType, frozenBox, project, projectSite, rowNum, "4", col04, sampleCode, frozenTubeList, emptyTubeNumber);
                    frozenTubeList.add(frozenTube04);

                    String col05 = tubeMap.get("COL_05") != null ? tubeMap.get("COL_05").toString() : null;
                    FrozenTube frozenTube05 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "05", frozenTubeType, frozenBox, project, projectSite, rowNum, "5", col05, sampleCode, frozenTubeList, emptyTubeNumber);

                    String col06 = tubeMap.get("COL_06") != null ? tubeMap.get("COL_06").toString() : null;
                    FrozenTube frozenTube06 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "06", frozenTubeType, frozenBox, project, projectSite, rowNum, "6", col06, sampleCode, frozenTubeList, emptyTubeNumber);

                    String col07 = tubeMap.get("COL_07") != null ? tubeMap.get("COL_07").toString() : null;
                    FrozenTube frozenTube07 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "07", frozenTubeType, frozenBox, project, projectSite, rowNum, "7", col07, sampleCode, frozenTubeList, emptyTubeNumber);

                    String col08 = tubeMap.get("COL_08") != null ? tubeMap.get("COL_08").toString() : null;
                    FrozenTube frozenTube08 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "08", frozenTubeType, frozenBox, project, projectSite, rowNum, "8", col08, sampleCode, frozenTubeList, emptyTubeNumber);

                    String col09 = tubeMap.get("COL_09") != null ? tubeMap.get("COL_09").toString() : null;
                    FrozenTube frozenTube09 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "09", frozenTubeType, frozenBox, project, projectSite, rowNum, "9", col09, sampleCode, frozenTubeList, emptyTubeNumber);

                    String col10 = tubeMap.get("COL_10") != null ? tubeMap.get("COL_10").toString() : null;
                    FrozenTube frozenTube10 = getFrozenTubeFor99Type(sampleType, projectSampleClassList, "10", frozenTubeType, frozenBox, project, projectSite, rowNum, "10", col10, sampleCode, frozenTubeList, emptyTubeNumber);


                    TranshipTube transhipTube01 = getTranshipTubeFor99Type(frozenTube01, transhipBox, transhipTubeList);
                    TranshipTube transhipTube02 = getTranshipTubeFor99Type(frozenTube02, transhipBox, transhipTubeList);
                    TranshipTube transhipTube03 = getTranshipTubeFor99Type(frozenTube03, transhipBox, transhipTubeList);
                    TranshipTube transhipTube04 = getTranshipTubeFor99Type(frozenTube04, transhipBox, transhipTubeList);
                    TranshipTube transhipTube05 = getTranshipTubeFor99Type(frozenTube05, transhipBox, transhipTubeList);
                    TranshipTube transhipTube06 = getTranshipTubeFor99Type(frozenTube06, transhipBox, transhipTubeList);
                    TranshipTube transhipTube07 = getTranshipTubeFor99Type(frozenTube07, transhipBox, transhipTubeList);
                    TranshipTube transhipTube08 = getTranshipTubeFor99Type(frozenTube08, transhipBox, transhipTubeList);
                    TranshipTube transhipTube09 = getTranshipTubeFor99Type(frozenTube09, transhipBox, transhipTubeList);
                    TranshipTube transhipTube10 = getTranshipTubeFor99Type(frozenTube10, transhipBox, transhipTubeList);
                    renfen++;
                }
                frozenBox.emptyTubeNumber(emptyTubeNumber).memo(String.join(",", memoList));
                transhipBox.memo(String.join(",", memoList)).emptyTubeNumber(emptyTubeNumber).countOfSample(frozenTubeList.size());
                countOfTube += frozenTubeList.size();
                emptyTubeNumberForTranip += emptyTubeNumber;
                yangbenrenfen += renfen;
                frozenBoxRepository.save(frozenBox);
                transhipBoxRepository.save(transhipBox);
                frozenTubeRepository.save(frozenTubeList);
                transhipTubeRepository.save(transhipTubeList);
                executedOperations[0]++;
                log.info("Finished:(%d/%d)" + executedOperations[0] + "/" + allOperations);
            }
            tranship.emptyTubeNumber(emptyTubeNumberForTranip);
            tranship.sampleNumber(yangbenrenfen).effectiveSampleNumber(countOfTube).emptyTubeNumber(emptyTubeNumberForTranip);
            transhipRepository.save(tranship);
        }
    }
    //导入0037项目首次入库记录--99类型
    @Test
    public void importOptStockInFor0037(){
        List<Object[]> countOfAllTranshipTube = frozenTubeRepository.countTubeByProjectCodeGroupByTranshipCode("0037","99");

        List<String> wrongSample = new ArrayList<>();
        allOperations = 0;
        executedOperations[0] = 0;
        Project project = projectRepository.findByProjectCode("0037");
        List<SampleType> sampleTypes = sampleTypeRepository.findAllSampleTypes();
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findSampleTypeByProjectCode("0037");
        List<Map<String,Object>> firstStockInList = getFirstStockIn0037();
        Map<String, List<Map<String, Object>>> listGroupByOptPersonAndDate =
            firstStockInList.stream().collect(Collectors.groupingBy(w -> w.get("OLD_DATE").toString()+"&"+ w.get("USER_1").toString()+"&"+ w.get("USER_2").toString()));
        TreeMap<String, List<Map<String, Object>>> mapGroupByOptPersonAndDate = new TreeMap<>(listGroupByOptPersonAndDate);

        allOperations = firstStockInList.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString())).size();
        List<String> boxCode99StrAll = new ArrayList<>();
        firstStockInList.forEach(s->{
            if(!boxCode99StrAll.contains(s.get("BOX_CODE_1D").toString())){
                boxCode99StrAll.add(s.get("BOX_CODE_1D").toString());
            }
        });
        List<FrozenTube> frozenTubeListAll = frozenTubeRepository.findByFrozenBoxCodeIn(boxCode99StrAll);
        Map<String,List<FrozenTube>> tubeListGroupByBoxCode = frozenTubeListAll.stream().collect(Collectors.groupingBy(c->c.getFrozenBoxCode()));

        for(String key :mapGroupByOptPersonAndDate.keySet()){
            String optDate = key.split("&")[0];//操作日期
            String opt_1 = key.split("&")[1];//操作人1
            String opt_2 = key.split("&")[2];//操作人1
            List<Map<String, Object>> boxList = mapGroupByOptPersonAndDate.get(key);
            String receiver = boxList.get(0).get("RECEIVER")!=null?boxList.get(0).get("RECEIVER").toString():"NA";
            Date stockInDate = null;
            try {
                stockInDate = strToDate2(optDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        ZonedDateTime createDate = stockInDate.toInstant().atZone(ZoneId.systemDefault());
            Long optPerson_id_1 = null;
            Long optPerson_id_2 = null;
            Long receiverId = null;
            if (!StringUtils.isEmpty(opt_1)) {
                optPerson_id_1 = Constants.RECEIVER_MAP.get(opt_1);
            }
            if (!StringUtils.isEmpty(opt_2)) {
                optPerson_id_2 = Constants.RECEIVER_MAP.get(opt_2);
            }
            if (!StringUtils.isEmpty(receiver)) {
                receiverId = Constants.RECEIVER_MAP.get(receiver);
            }
            int countOfSampleForStockIn = 0;
            String stockInCode = bankUtil.getUniqueIDByDate("B", stockInDate);
            //保存入库记录
            StockIn stockIn = new StockIn().projectCode("0037")
                .projectSiteCode(null)
                .receiveId(receiverId).receiveDate(date)
                .receiveName(receiver)
                .stockInType(Constants.STORANGE_IN_TYPE_1ST)
                .storeKeeperId1(optPerson_id_1)
                .storeKeeper1(opt_1)
                .storeKeeperId2(optPerson_id_2)
                .storeKeeper2(opt_2)
                .stockInDate(date)
                .countOfSample(0).stockInCode(stockInCode).project(project).projectSite(null)
                .status(Constants.STOCK_IN_COMPLETE);
            stockInRepository.save(stockIn);
            //入库的每一个盒子
            Map<String, List<Map<String, Object>>> tubeForBoxMap =
                boxList.stream().collect(Collectors.groupingBy(s -> s.get("BOX_CODE").toString()));
            //获取入库的每一盒子所属的转运盒
            List<String> boxCode99Str = new ArrayList<>();
            boxList.forEach(s->{
                if(!boxCode99Str.contains(s.get("BOX_CODE_1D").toString())){
                    boxCode99Str.add(s.get("BOX_CODE_1D").toString());
                }
            });
            //获取转运盒来自于哪次转运，创建转运和入库的关系
            List<TranshipBox> transhipBoxes = transhipBoxRepository.findByFrozenBoxCodesAndStatus(boxCode99Str);
            List<TranshipStockIn> transhipStockIns = new ArrayList<>();
            List<StockInBox> stockInBoxes99 = new ArrayList<>();
            List<FrozenBox> frozenBoxList99 = new  ArrayList<>();
            List<String> transhipCodes = new ArrayList<>();
            List<Tranship> tranships = new ArrayList<>();

            for(String boxCode :tubeForBoxMap.keySet()){
                log.info("导入盒"+boxCode);
                //冻存管List
                List<Map<String, Object>> tubeList = tubeForBoxMap.get(boxCode);
                String sampleTypeCode =  tubeList.get(0).get("TYPE").toString();
                String sampleClassificationCode =  tubeList.get(0).get("SAMPLE_TYPE_CODE").toString();
                SampleType sampleType = sampleTypes.stream().filter(s->s.getSampleTypeCode().equals(sampleTypeCode)).findFirst().orElse(null);
                ProjectSampleClass projectSampleClass = projectSampleClasses.stream().filter(s->s.getSampleClassificationCode().equals(sampleClassificationCode)).findFirst().orElse(null);
                SampleClassification sampleClassification = projectSampleClass.getSampleClassification();
                String equipmentCode = tubeList.get(0).get("EQ_CODE").toString().trim();
                String areaCode = !tubeList.get(0).get("AREA_CODE").toString().equals("NA")?tubeList.get(0).get("AREA_CODE").toString().trim():"S99";
                String supportCode =  !tubeList.get(0).get("SHELF_CODE").toString().equals("NA")?tubeList.get(0).get("SHELF_CODE").toString().trim():"R99";
                //设备
                Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
                if (entity == null) {
                    throw new BankServiceException("设备未导入:" + equipmentCode);
                }
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.saveAndFlush(area);
                }
                //冻存架
                SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                if (supportRack == null) {
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                        .area(supportRackMapper.areaFromId(area.getId()));
                    supportRackRepository.saveAndFlush(supportRack);
                }
                String posInShelf = tubeList.get(0).get("POS_IN_SHELF").toString();
                String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
                String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
                final int[] emptyNumber = {0};
                emptyNumber[0]=0;
                tubeList.forEach(s->{
                    if(s.get("SAMPLE_STATUS").toString().equals("空管")){
                        emptyNumber[0]++;
                    }
                });
                FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
                if(frozenBox==null) {
                    frozenBox = new FrozenBox()
                        .frozenBoxCode1D(null).frozenBoxCode(boxCode).frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode()).frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows()).frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                        .projectCode(project.getProjectCode()).projectName(project.getProjectName()).projectSiteCode(null).projectSiteName(null)
                        .equipmentCode(entity != null ? entity.getEquipmentCode() : null).areaCode(area != null ? area.getAreaCode() : null)
                        .supportRackCode(supportRack != null ? supportRack.getSupportRackCode() : null)
                        .sampleTypeCode(sampleType.getSampleTypeCode())
                        .sampleTypeName(sampleType.getSampleTypeName())
                        .isSplit(0)
                        .status("2004")
                        .emptyTubeNumber(emptyNumber[0])
                        .emptyHoleNumber(0)
                        .dislocationNumber(0)
                        .isRealData(1).frozenBoxType(frozenBoxType).sampleType(sampleType)
                        .sampleClassification(sampleClassification).equipment(entity).area(area)
                        .supportRack(supportRack)
                        .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).project(project).projectSite(null);
                }
                frozenBoxRepository.save(frozenBox);
                //保存入库盒
                StockInBox stockInBox = new StockInBox()
                    .equipmentCode(equipmentCode)
                    .areaCode(areaCode)
                    .supportRackCode(supportCode)
                    .rowsInShelf(rowsInShelf)
                    .columnsInShelf(columnsInShelf)
                    .status(Constants.FROZEN_BOX_STOCKED).countOfSample(tubeList.size())
                    .frozenBoxCode(boxCode).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(area).equipment(entity).supportRack(supportRack)
                    .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockInBoxRepository.save(stockInBox);
                //保存入库盒子位置
                StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
                stockInBoxPosition = stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                    .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                    .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                    .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                    .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                    .stockInBox(stockInBox);
                stockInBoxPositionRepository.save(stockInBoxPosition);
                int countOfSample = 0;
                List<StockInTube> stockInTubes = new ArrayList<>();
                List<FrozenTube> frozenTubeList = new ArrayList<>();
                for(Map<String, Object> tubeMap :tubeList){
                    String status = tubeMap.get("SAMPLE_STATUS").toString();

                    if(status.equals("空孔")){
                       continue;
                    }
                    String boxCode99 = tubeMap.get("BOX_CODE_1D").toString();
                    countOfAllTranshipTube.forEach(s-> {
                        if(s[1].toString().equals(boxCode99)){
                            int a= Integer.valueOf(s[2].toString())-1;
                            s[2]= a;
                        }
                        }
                    );
                    countOfSample++;
                    String sampleCode = tubeMap.get("SAMPLE_CODE").toString();
                    log.info("导入冻存管："+sampleCode);
                    String posInBox = tubeMap.get("POS_IN_BOX").toString();
                    String tubeRows = posInBox.substring(0, 1);
                    String tubeColumns = posInBox.substring(1);
                    String memo = tubeMap.get("MEMO") != null ? tubeMap.get("MEMO").toString() : null;
                    String times = tubeMap.get("TIMES") != null ? tubeMap.get("TIMES").toString() : null;

//                    FrozenTube tube =
//                        frozenTubeRepository.findBySampleCodeAndProjectCodeAndSampleTypeCodeAndSampleClassificationCode(sampleCode,"0037","99",sampleClassificationCode);

                    FrozenTube tube = frozenTubeListAll.stream().filter(s->s.getSampleTypeCode().equals("99") && s.getSampleClassification().getSampleClassificationCode().equals(sampleClassificationCode)&&s.getSampleCode().equals(sampleCode)).findFirst().orElse(null);
                    if(tube==null){
                        if(sampleClassificationCode.contains("1")||sampleClassificationCode.contains("3")||sampleClassificationCode.contains("5")||sampleClassificationCode.contains("7")||sampleClassificationCode.contains("9")){

                            Integer a = Integer.valueOf(sampleClassificationCode)+1;
                            String n = String.format("%0"+2+"d", a);
                            tube = frozenTubeListAll.stream().filter(s->s.getSampleTypeCode().equals("99") && s.getSampleClassification().getSampleClassificationCode().equals(n)&&s.getSampleCode().equals(sampleCode)).findFirst().orElse(null);
                            if(tube==null){
                                wrongSample.add(sampleCode+"/"+sampleClassificationCode);
                                continue;
                            }
                        }else{
                            wrongSample.add(sampleCode+"/"+sampleClassificationCode);
                            continue;
                        }
                    }
                    tube = tube.sampleType(sampleType).sampleTypeCode(sampleType.getSampleTypeCode()).sampleTypeName(sampleType.getSampleTypeName())
                        .frozenTubeState(Constants.FROZEN_BOX_STOCKED).frozenBox(frozenBox).frozenBoxCode(boxCode).sampleClassification(sampleClassification)
                        .tubeColumns(tubeColumns).tubeRows(tubeRows).sampleStage(times).memo(memo);
                    frozenTubeList.add(tube);
                    StockInTube stockInTube = new StockInTube()
                        .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                        .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                        .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                        .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                        .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                        .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                        .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                        .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                        .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                        .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                        .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                    stockInTubes.add(stockInTube);
                }
                countOfSampleForStockIn+=countOfSample;
                stockInBox.countOfSample(countOfSample);
                stockInBoxRepository.save(stockInBox);
                frozenTubeRepository.save(frozenTubeList);
                stockInTubeRepository.save(stockInTubes);
                executedOperations[0]++;
                log.info("Finished:(%d/%d)"+ executedOperations[0]+"/" +allOperations);
            }
            stockIn.countOfSample(countOfSampleForStockIn);
            stockInRepository.save(stockIn);
            Map<String,List<Object[]>> mapGroupByTranshipCode = countOfAllTranshipTube.stream().collect(Collectors.groupingBy(s->s[0].toString()));
            Map<String,List<Object[]>> mapGroupByBoxCode = countOfAllTranshipTube.stream().collect(Collectors.groupingBy(s->s[1].toString()));
            for(TranshipBox transhipBox : transhipBoxes){
                Tranship tranship = transhipBox.getTranship();
                FrozenBox frozenBox = transhipBox.getFrozenBox();
                if(!transhipCodes.contains(tranship.getTranshipCode())){
                    transhipCodes.add(tranship.getTranshipCode());
                    TranshipStockIn transhipStockIn = new TranshipStockIn();
                    transhipStockIn.stockInCode(stockIn.getStockInCode()).stockIn(stockIn).transhipCode(tranship.getTranshipCode()).tranship(tranship).status(Constants.VALID);
                    transhipStockIns.add(transhipStockIn);
                    tranship.transhipState(Constants.TRANSHIPE_IN_STOCKING).receiverId(receiverId).receiver(receiver);
                    List<Object[]> alist = mapGroupByTranshipCode.get(tranship.getTranshipCode());
                    int countUnStock = 0;
                    for(Object[] o : alist){
                        countUnStock+=Integer.valueOf(o[2].toString());
                    }
                    if(countUnStock==0){//转运全部都入库完成
                        tranship.transhipState(Constants.TRANSHIPE_IN_STOCKED);
                        tranships.add(tranship);
                    }
                }

                StockInBox stockInBox = new StockInBox();
                List<StockInBox> oldStockInBox = stockInBoxRepository.findByFrozenBoxCode(frozenBox.getFrozenBoxCode());
                if(oldStockInBox.size()>0){
                    stockInBox = oldStockInBox.get(0);
                }else{
                    stockInBox = new StockInBox()
                        .equipmentCode(frozenBox.getEquipmentCode())
                        .areaCode(frozenBox.getAreaCode())
                        .supportRackCode(frozenBox.getSupportRackCode())
                        .rowsInShelf(frozenBox.getSupportRackCode())
                        .columnsInShelf(frozenBox.getColumnsInShelf())
                        .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(frozenBox.getArea()).equipment(frozenBox.getEquipment()).supportRack(frozenBox.getSupportRack())
                        .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                        .sampleClassification(frozenBox.getSampleClassification())
                        .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                        .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                        .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                        .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                        .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                        .projectSiteName(frozenBox.getProjectSiteName());
                }
                int  count = Integer.valueOf(mapGroupByBoxCode.get(frozenBox.getFrozenBoxCode()).get(0)[2].toString());
                stockInBox.countOfSample(count).frozenBox(transhipBox.getFrozenBox()).stockIn(stockIn).stockInCode(stockInCode);
                if (count == 0) {
                    stockInBox.status(Constants.FROZEN_BOX_SPLITED);
                    frozenBox.status(Constants.FROZEN_BOX_SPLITED);
                }else{
                    stockInBox.status(Constants.FROZEN_BOX_STOCKING);
                    frozenBox.status(Constants.FROZEN_BOX_STOCKING);
                }
                stockInBox.setStockIn(stockIn);
                stockInBoxes99.add(stockInBox);
                frozenBoxList99.add(frozenBox);
            }
            frozenBoxRepository.save(frozenBoxList99);
            stockInBoxRepository.save(stockInBoxes99);
            transhipRepository.save(tranships);
            transhipStockInRepository.save(transhipStockIns);
        }
        log.info(wrongSample.toString());
    }
    //获取0037项目首次入库记录
    private List<Map<String,Object>> getFirstStockIn0037() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        //获取0037项目的转运记录
        List<Map<String, Object>> transhipBoxList = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            String sqlForTranshipBox = "select * from PEACE5_FIRST_STOCK_IN ";// 预编译语句
            pre = con.prepareStatement(sqlForTranshipBox);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetaForTranshipBox = result.getMetaData();
            Map<String, Object> mapForTranshipBox = null;
            while (result.next()) {
                mapForTranshipBox = this.Result2Map(result, rsMetaForTranshipBox);
                transhipBoxList.add(mapForTranshipBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  transhipBoxList;
    }
    //导入0037项目---创建99类型转运管
    private TranshipTube getTranshipTubeFor99Type(FrozenTube frozenTube, TranshipBox transhipBox, List<TranshipTube> transhipTubeList) {
        if(frozenTube == null ){
            return null;
        }
        TranshipTube transhipTube = new TranshipTube().status(frozenTube.getStatus()).memo(frozenTube.getMemo())
            .rowsInTube(frozenTube.getTubeRows()).frozenTube(frozenTube).columnsInTube(frozenTube.getTubeColumns())
            .transhipBox(transhipBox).errorType(frozenTube.getErrorType()).frozenBoxCode(frozenTube.getFrozenBoxCode())
            .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(Constants.FROZEN_BOX_TRANSHIP_COMPLETE)
            .frozenTubeType(frozenTube.getFrozenTubeType()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
            .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
            .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
            .project(frozenTube.getProject()).projectCode(frozenTube.getProjectCode()).projectSite(frozenTube.getProjectSite())
            .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassification(frozenTube.getSampleClassification())
            .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
            .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
            .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode()).sampleType(frozenTube.getSampleType())
            .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
            .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());
        transhipTubeList.add(transhipTube);
        return transhipTube;
    }
    //导入0037项目--创建99类型冻存管
    private FrozenTube getFrozenTubeFor99Type(SampleType sampleType, List<ProjectSampleClass> projectSampleClassList, String sampleClassCode, FrozenTubeType frozenTubeType, FrozenBox frozenBox, Project project, ProjectSite projectSite, String rowNum, String colNum, String col01, String sampleCode, List<FrozenTube> frozenTubeList,int emptyTubeNumber) {
        if(col01.equals("0")){
            return null;
        }
        if(col01.equals("NA")){
            emptyTubeNumber++;
        }
        String status01 = Constants.UBE_STATUS_MAP.get(col01);
        ProjectSampleClass projectSampleClass = projectSampleClassList.stream().filter(s->s.getSampleClassificationCode().equals(sampleClassCode)).findFirst().orElse(null);
        SampleClassification sampleClassification = projectSampleClass.getSampleClassification();
        FrozenTube frozenTube = new FrozenTube().sampleType(sampleType).projectCode("0037").projectSiteCode(projectSite != null ? projectSite.getProjectSiteCode() : null)
            .sampleCode(sampleCode)
            .frozenTubeTypeCode(frozenTubeType.getFrozenTubeTypeCode())
            .frozenTubeTypeName(frozenTubeType.getFrozenTubeTypeName())
            .sampleTypeCode(sampleType.getSampleTypeCode())
            .sampleTypeName(sampleType.getSampleTypeName())
            .sampleUsedTimesMost(frozenTubeType.getSampleUsedTimesMost())
            .sampleUsedTimes(0)
            .frozenTubeVolumns(frozenTubeType.getFrozenTubeVolumn())
            .frozenTubeVolumnsUnit(frozenTubeType.getFrozenTubeVolumnUnit())
            .tubeRows(rowNum)
            .tubeColumns(colNum)
            .status(status01).memo(null)
            .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenTubeType(frozenTubeType).sampleType(sampleType).sampleClassification(sampleClassification)
            .project(project).projectSite(projectSite).frozenBox(frozenBox).frozenTubeState(Constants.FROZEN_BOX_TRANSHIP_COMPLETE);
        frozenTubeList.add(frozenTube);
        return frozenTube;
    }
    //导入0037项目---获取小盒分装记录
    private List<Map<String,Object>> getTranshipBoxList0037() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        //获取0037项目的转运记录
        List<Map<String, Object>> transhipBoxList = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            String sqlForTranshipBox = "select t.*,to_date(t.rec_date,'MM/DD/YYYY') as tranship_date_real from FEN_HE_JI_LU t  order by tranship_date_real";// 预编译语句
            pre = con.prepareStatement(sqlForTranshipBox);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetaForTranshipBox = result.getMetaData();
            Map<String, Object> mapForTranshipBox = null;
            while (result.next()) {
                mapForTranshipBox = this.Result2Map(result, rsMetaForTranshipBox);
                transhipBoxList.add(mapForTranshipBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  transhipBoxList;
    }
    //获取0037项目转运入库记录
    private List<Map<String,Object>> getTranshipList0037() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        //获取0037项目的转运记录
        List<Map<String, Object>> transhipList = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            String sqlForSelect = "select t.*,to_date(t.rec_date,'MM/DD/YYYY') as tranship_date_real from TRANSHIP_0037 t where t.rec_date!='NA' order by tranship_date_real";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                transhipList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  transhipList;
    }
    //获取0037移位记录
    private List<Map<String,Object>> getMoveOptFor0037() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        //获取0037项目的转运记录
        List<Map<String, Object>> transhipBoxList = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            String sqlForTranshipBox = "select * from PEACE5_MOVE_OPT order by OLD_DATE";// 预编译语句
            pre = con.prepareStatement(sqlForTranshipBox);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetaForTranshipBox = result.getMetaData();
            Map<String, Object> mapForTranshipBox = null;
            while (result.next()) {
                mapForTranshipBox = this.Result2Map(result, rsMetaForTranshipBox);
                transhipBoxList.add(mapForTranshipBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  transhipBoxList;
    }
    //获取RNA记录
    private List<Map<String,Object>> getRNADataFor0037() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        //获取0037项目的转运记录
        List<Map<String, Object>> transhipBoxList = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            String sqlForTranshipBox = "select t.*,to_date(t.REC_DATE,'MM/DD/YYYY') as TRANSHIP_DATE_REAL,to_date(t.OPT_DATE,'MM/DD/YYYY') as OLD_DATE from HE_COL_11_RNA t";// 预编译语句
            pre = con.prepareStatement(sqlForTranshipBox);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetaForTranshipBox = result.getMetaData();
            Map<String, Object> mapForTranshipBox = null;
            while (result.next()) {
                mapForTranshipBox = this.Result2Map(result, rsMetaForTranshipBox);
                transhipBoxList.add(mapForTranshipBox);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  transhipBoxList;
    }
    //导入0037项目移位记录
    @Test
    public void importOptFor0037Move(){
        List<Map<String, Object>> moveList = getMoveOptFor0037();
        //10个类型的移位记录
        Map<String, List<Map<String, Object>>> optMoveGroupByBox =
            moveList.stream().collect(Collectors.groupingBy(w -> w.get("OPT_DATE").toString()+"&"+w.get("BOX_CODE").toString()));
        for(String key :optMoveGroupByBox.keySet()){
            importStockInFor0037Move(optMoveGroupByBox.get(key),key);
        }
    }
    //导入0037项目移位记录具体实现
    private void importStockInFor0037Move(List<Map<String, Object>> boxList, String key) {
        String optDate = key.split("&")[0];//操作日期
        String boxCode = key.split("&")[1];//冻存盒编码
        Date stockInDate = null;
        try {
            stockInDate = strToDate(optDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.print("移位盒:" + boxCode);

        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
        if (frozenBox == null) {
            throw new BankServiceException("冻存盒不存在" + boxCode);
        }
        Map<String, Object> map = boxList.get(0);
        String optPerson1 = map.get("USER_1") != null ? map.get("USER_1").toString() : "NA";
        String optPerson2 = map.get("USER_2") != null ? map.get("USER_2").toString() : "NA";
        String memo = map.get("MEMO") != null ? map.get("MEMO").toString() : null;
        LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        ZonedDateTime createDate = stockInDate.toInstant().atZone(ZoneId.systemDefault());
        Long opt_user_id = null;
        Long opt_user_id_2 = null;
        if (!StringUtils.isEmpty(optPerson1)) {
            opt_user_id = Constants.RECEIVER_MAP.get(optPerson1);
        }
        if (!StringUtils.isEmpty(optPerson2)) {
            opt_user_id_2 = Constants.RECEIVER_MAP.get(optPerson2);
        }
        //移位
        PositionMove positionMove = new PositionMove();
        positionMove = positionMove.moveAffect("无").moveReason("无").moveType(Constants.MOVE_TYPE_FOR_BOX)
            .memo(memo)
            .whetherFreezingAndThawing(false).positionMoveDate(date)
            .status(Constants.VALID).operatorId1(opt_user_id).operatorId2(opt_user_id_2);
        positionMoveRepository.save(positionMove);

        //移位入库详情
        //先查询到盒子的首次入库记录

        String equipmentCode = map.get("EQ_CODE").toString().trim();
        String areaCode = map.get("AREA_CODE") != null && !map.get("AREA_CODE").toString().equals("NA") ? map.get("AREA_CODE").toString().trim() : "S99";

        String supportCode = map.get("SHELF_CDOE") != null && !map.get("SHELF_CDOE").toString().equals("NA") && !map.get("SHELF_CDOE").toString().equals("NA")
            ? map.get("SHELF_CDOE").toString().trim() : "R99";

        //设备
        Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
        if (entity == null) {
            throw new BankServiceException("设备未导入:" + equipmentCode);
        }
        //区域
        Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
        if (area == null) {
            throw new BankServiceException("区域未导入:" + equipmentCode + areaCode);
        }
        //冻存架
        SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
        if (supportRack == null) {
            SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
            supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType)
                .supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                .area(supportRackMapper.areaFromId(area.getId()));
            supportRackRepository.save(supportRack);
        }
        String posInShelf = map.get("POS_IN_SHELF") != null ? map.get("POS_IN_SHELF").toString() : "NA";
        String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
        String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
        frozenBox = frozenBox.equipment(entity).equipmentCode(entity.getEquipmentCode())
            .area(area)
            .areaCode(area.getAreaCode())
            .supportRack(supportRack)
            .supportRackCode(supportRack.getSupportRackCode())
            .columnsInShelf(columnsInShelf)
            .rowsInShelf(rowsInShelf);
        String memoOld = frozenBox.getMemo();
        String memoLast = memo;
        if (!StringUtils.isEmpty(memoOld)) {
            memoLast = memoOld + "," + memo;
        }
        frozenBox.setMemo(memoLast);
        frozenBoxRepository.save(frozenBox);
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
        saveMoveDetail(positionMove, Constants.MOVE_TYPE_FOR_BOX, frozenTubeList);

    }
    //导入0037项目首次入库记录--RNA类型
    @Test
    public void importOptStockInRNAFor0037(){
        List<Object[]> countOfAllTranshipTube = frozenTubeRepository.countTubeByProjectCodeGroupByTranshipCode("0037","RNA");

        List<String> wrongSample = new ArrayList<>();
        allOperations = 0;
        executedOperations[0] = 0;
        Project project = projectRepository.findByProjectCode("0037");
        List<SampleType> sampleTypes = sampleTypeRepository.findAllSampleTypes();
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DJH");
        List<ProjectSampleClass> projectSampleClasses = projectSampleClassRepository.findSampleTypeByProjectCode("0037");
        //获取0037项目RNA类型的数据
        List<Map<String, Object>> RNAList = getRNADataFor0037();
        List<Map<String, Object>> RNAFirstStockInList = new ArrayList<>();
        Map<String, List<Map<String, Object>>> rnalistGroupByOpt =
            RNAList.stream().collect(Collectors.groupingBy(w -> w.get("OPT").toString()));
        for(String opt:rnalistGroupByOpt.keySet()){
            switch (opt){
                case "首次入库":RNAFirstStockInList =rnalistGroupByOpt.get(opt);break;
                default:break;
            }
        }
        Map<String, List<Map<String, Object>>> listGroupByOptPersonAndDate =
            RNAFirstStockInList.stream().collect(Collectors.groupingBy(w -> w.get("OLD_DATE").toString()));
        TreeMap<String, List<Map<String, Object>>> mapGroupByOptPersonAndDate = new TreeMap<>(listGroupByOptPersonAndDate);

        allOperations = RNAFirstStockInList.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString())).size();
        List<String> boxCodeRNAStrAll = new ArrayList<>();
        RNAFirstStockInList.forEach(s->{
            if(!boxCodeRNAStrAll.contains(s.get("REC_BOX_CODE").toString())){
                boxCodeRNAStrAll.add(s.get("REC_BOX_CODE").toString());
            }
        });
        List<FrozenTube> frozenTubeListAll = frozenTubeRepository.findByFrozenBoxCodeIn(boxCodeRNAStrAll);
        for(String key :mapGroupByOptPersonAndDate.keySet()){
            String optDate = key.split("&")[0];//操作日期
            String opt_1 = "NA";//操作人1
            String opt_2 = "NA";//操作人1
            List<Map<String, Object>> boxList = mapGroupByOptPersonAndDate.get(key);
            String receiver = "NA";
            Date stockInDate = null;
            try {
                stockInDate = strToDate2(optDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            LocalDate date = stockInDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//        ZonedDateTime createDate = stockInDate.toInstant().atZone(ZoneId.systemDefault());
            Long optPerson_id_1 = null;
            Long optPerson_id_2 = null;
            Long receiverId = null;
            if (!StringUtils.isEmpty(opt_1)) {
                optPerson_id_1 = Constants.RECEIVER_MAP.get(opt_1);
            }
            if (!StringUtils.isEmpty(opt_2)) {
                optPerson_id_2 = Constants.RECEIVER_MAP.get(opt_2);
            }
            if (!StringUtils.isEmpty(receiver)) {
                receiverId = Constants.RECEIVER_MAP.get(receiver);
            }
            int countOfSampleForStockIn = 0;
            String stockInCode = bankUtil.getUniqueIDByDate("B", stockInDate);
            //保存入库记录
            StockIn stockIn = new StockIn().projectCode("0037")
                .projectSiteCode(null)
                .receiveId(receiverId).receiveDate(date)
                .receiveName(receiver)
                .stockInType(Constants.STORANGE_IN_TYPE_1ST)
                .storeKeeperId1(optPerson_id_1)
                .storeKeeper1(opt_1)
                .storeKeeperId2(optPerson_id_2)
                .storeKeeper2(opt_2)
                .stockInDate(date)
                .countOfSample(0).stockInCode(stockInCode).project(project).projectSite(null)
                .status(Constants.STOCK_IN_COMPLETE);
            stockInRepository.save(stockIn);
            //入库的每一个盒子
            Map<String, List<Map<String, Object>>> tubeForBoxMap =
                boxList.stream().collect(Collectors.groupingBy(s -> s.get("BOX_CODE").toString()));
            //获取入库的每一盒子所属的转运盒
            List<String> boxCode99Str = new ArrayList<>();
            boxList.forEach(s->{
                if(!boxCode99Str.contains(s.get("REC_BOX_CODE").toString())){
                    boxCode99Str.add(s.get("REC_BOX_CODE").toString());
                }
            });
            //获取转运盒来自于哪次转运，创建转运和入库的关系
            List<TranshipBox> transhipBoxes = transhipBoxRepository.findByFrozenBoxCodesAndStatus(boxCode99Str);
            List<TranshipStockIn> transhipStockIns = new ArrayList<>();
            List<StockInBox> stockInBoxes99 = new ArrayList<>();
            List<FrozenBox> frozenBoxList99 = new ArrayList<>();
            List<String> transhipCodes = new ArrayList<>();
            List<Tranship> tranships = new ArrayList<>();

            for(String boxCode :tubeForBoxMap.keySet()){
                log.info("导入盒"+boxCode);
                //冻存管List
                List<Map<String, Object>> tubeList = tubeForBoxMap.get(boxCode);
                String sampleTypeCode =  "RNA";
                SampleType sampleType = sampleTypes.stream().filter(s->s.getSampleTypeCode().equals(sampleTypeCode)).findFirst().orElse(null);
              String equipmentCode = tubeList.get(0).get("EQ_CODE").toString().trim();
                String areaCode = !tubeList.get(0).get("AREA_CODE").toString().equals("NA")?tubeList.get(0).get("AREA_CODE").toString().trim():"S99";
                String supportCode =  "R99";
                //设备
                Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
                if (entity == null) {
                    throw new BankServiceException("设备未导入:" + equipmentCode);
                }
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.saveAndFlush(area);
                }
                //冻存架
                SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                if (supportRack == null) {
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                        .area(supportRackMapper.areaFromId(area.getId()));
                    supportRackRepository.saveAndFlush(supportRack);
                }
                String posInShelf = "NA";
                String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
                String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
                FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
                if(frozenBox == null) {
                    frozenBox = new FrozenBox()
                        .frozenBoxCode1D(null).frozenBoxCode(boxCode).frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode()).frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows()).frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                        .projectCode(project.getProjectCode()).projectName(project.getProjectName()).projectSiteCode(null).projectSiteName(null)
                        .equipmentCode(entity != null ? entity.getEquipmentCode() : null).areaCode(area != null ? area.getAreaCode() : null)
                        .supportRackCode(supportRack != null ? supportRack.getSupportRackCode() : null)
                        .sampleTypeCode(sampleType.getSampleTypeCode())
                        .sampleTypeName(sampleType.getSampleTypeName())
                        .isSplit(0)
                        .status("2004")
                        .emptyTubeNumber(0)
                        .emptyHoleNumber(0)
                        .dislocationNumber(0)
                        .isRealData(1).frozenBoxType(frozenBoxType).sampleType(sampleType)
                        .sampleClassification(null).equipment(entity).area(area)
                        .supportRack(supportRack)
                        .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).project(project).projectSite(null);
                }
                frozenBoxRepository.save(frozenBox);
                //保存入库盒
                StockInBox stockInBox = new StockInBox()
                    .equipmentCode(equipmentCode)
                    .areaCode(areaCode)
                    .supportRackCode(supportCode)
                    .rowsInShelf(rowsInShelf)
                    .columnsInShelf(columnsInShelf)
                    .status(Constants.FROZEN_BOX_STOCKED).countOfSample(tubeList.size())
                    .frozenBoxCode(boxCode).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(area).equipment(entity).supportRack(supportRack)
                    .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockInBoxRepository.save(stockInBox);
                //保存入库盒子位置
                StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
                stockInBoxPosition = stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                    .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                    .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                    .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                    .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                    .stockInBox(stockInBox);
                stockInBoxPositionRepository.save(stockInBoxPosition);
                int countOfSample = 0;
                List<StockInTube> stockInTubes = new ArrayList<>();
                List<FrozenTube> frozenTubeList = new ArrayList<>();
                for(Map<String, Object> tubeMap :tubeList){
                    String boxCodeRNA = tubeMap.get("REC_BOX_CODE").toString();
                    countOfAllTranshipTube.forEach(s-> {
                            if(s[1].toString().equals(boxCodeRNA)){
                                int a= Integer.valueOf(s[2].toString())-1;
                                s[2]= a;
                            }
                        }
                    );
                    countOfSample++;
                    String sampleCode = tubeMap.get("SAMPLE_CODE").toString();
                    log.info("导入冻存管："+sampleCode);
                    String posInBox = tubeMap.get("POS_IN_BOX").toString();
                    String tubeRows = posInBox.substring(0, 1);
                    String tubeColumns = posInBox.substring(1);
                    String memo = tubeMap.get("MEMO") != null ? tubeMap.get("MEMO").toString() : null;

//                    FrozenTube tube =
//                        frozenTubeRepository.findBySampleCodeAndProjectCodeAndSampleTypeCodeAndSampleClassificationCode(sampleCode,"0037","99",sampleClassificationCode);

                    FrozenTube tube = frozenTubeListAll.stream().filter(s->s.getSampleCode().equals(sampleCode)&&s.getSampleTypeCode().equals("RNA")&&s.getTubeColumns().equals(tubeColumns)&&s.getTubeRows().equals(tubeRows)).findFirst().orElse(null);
                    if(tube==null){
                        wrongSample.add(sampleCode);
                            continue;
//                        throw new BankServiceException("样本不存在！"+sampleCode);
                    }
                    tube = tube.sampleType(sampleType).sampleTypeCode(sampleType.getSampleTypeCode()).sampleTypeName(sampleType.getSampleTypeName())
                        .frozenTubeState(Constants.FROZEN_BOX_STOCKED).frozenBox(frozenBox).frozenBoxCode(boxCode)
                        .tubeColumns(tubeColumns).tubeRows(tubeRows).memo(memo);
                    frozenTubeList.add(tube);
                    StockInTube stockInTube = new StockInTube()
                        .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                        .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                        .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                        .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                        .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                        .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                        .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                        .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                        .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                        .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                        .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                    stockInTubes.add(stockInTube);
                }
                countOfSampleForStockIn+=countOfSample;
                stockInBox.countOfSample(countOfSample);
                stockInBoxRepository.save(stockInBox);
                frozenTubeRepository.save(frozenTubeList);
                stockInTubeRepository.save(stockInTubes);
                executedOperations[0]++;
                log.info("Finished:(%d/%d)"+ executedOperations[0]+"/" +allOperations);
            }

            stockIn.countOfSample(countOfSampleForStockIn);
            stockInRepository.save(stockIn);
            Map<String,List<Object[]>> mapGroupByTranshipCode = countOfAllTranshipTube.stream().collect(Collectors.groupingBy(s->s[0].toString()));
            Map<String,List<Object[]>> mapGroupByBoxCode = countOfAllTranshipTube.stream().collect(Collectors.groupingBy(s->s[1].toString()));
            for(TranshipBox transhipBox : transhipBoxes){
                Tranship tranship = transhipBox.getTranship();
                FrozenBox frozenBox = transhipBox.getFrozenBox();
                if(!transhipCodes.contains(tranship.getTranshipCode())){
                    transhipCodes.add(tranship.getTranshipCode());
                    TranshipStockIn transhipStockIn = new TranshipStockIn();
                    transhipStockIn.stockInCode(stockIn.getStockInCode()).stockIn(stockIn).transhipCode(tranship.getTranshipCode()).tranship(tranship).status(Constants.VALID);
                    transhipStockIns.add(transhipStockIn);
                    tranship.transhipState(Constants.TRANSHIPE_IN_STOCKING).receiverId(receiverId).receiver(receiver);
                    List<Object[]> alist = mapGroupByTranshipCode.get(tranship.getTranshipCode());
                    int countUnStock = 0;
                    for(Object[] o : alist){
                        countUnStock+=Integer.valueOf(o[2].toString());
                    }
                    if(countUnStock==0){//转运全部都入库完成
                        tranship.transhipState(Constants.TRANSHIPE_IN_STOCKED);
                        tranships.add(tranship);
                    }
                }

                StockInBox stockInBox = new StockInBox();
                List<StockInBox> oldStockInBox = stockInBoxRepository.findByFrozenBoxCode(frozenBox.getFrozenBoxCode());
                if(oldStockInBox.size()>0){
                    stockInBox = oldStockInBox.get(0);
                }else{
                     stockInBox = new StockInBox()
                        .equipmentCode(frozenBox.getEquipmentCode())
                        .areaCode(frozenBox.getAreaCode())
                        .supportRackCode(frozenBox.getSupportRackCode())
                        .rowsInShelf(frozenBox.getSupportRackCode())
                        .columnsInShelf(frozenBox.getColumnsInShelf())
                        .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(frozenBox.getArea()).equipment(frozenBox.getEquipment()).supportRack(frozenBox.getSupportRack())
                        .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                        .sampleClassification(frozenBox.getSampleClassification())
                        .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                        .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                        .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                        .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                        .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                        .projectSiteName(frozenBox.getProjectSiteName());
                }
                int  count = Integer.valueOf(mapGroupByBoxCode.get(frozenBox.getFrozenBoxCode()).get(0)[2].toString());
                stockInBox.countOfSample(count).frozenBox(transhipBox.getFrozenBox()).stockIn(stockIn).stockInCode(stockInCode);
                if (count == 0) {
                    stockInBox.status(Constants.FROZEN_BOX_SPLITED);
                    frozenBox.status(Constants.FROZEN_BOX_SPLITED);
                }else{
                    stockInBox.status(Constants.FROZEN_BOX_STOCKING);
                    frozenBox.status(Constants.FROZEN_BOX_STOCKING);
                }
                stockInBox.setStockIn(stockIn);
                stockInBoxes99.add(stockInBox);
                frozenBoxList99.add(frozenBox);
            }
            frozenBoxRepository.save(frozenBoxList99);
            stockInBoxRepository.save(stockInBoxes99);
            transhipRepository.save(tranships);
            transhipStockInRepository.save(transhipStockIns);
        }
        log.info(wrongSample.toString());
    }
   //将0037项目的未入库的所有样本放在一个入库单内
    @Test
    public void importOptUnStockInFor0037() {
        Project project = projectRepository.findByProjectCode("0037");
        String stockInDateStr = "2017/10/26";
        Date date = new Date();
        try {
            date = strToDate3(stockInDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<FrozenBox> frozenBoxList = frozenBoxRepository.findByProjectCodeAndStatus("0037", Constants.FROZEN_BOX_STOCKING);


        List<String> frozenBoxCodeStr = new ArrayList<>();
        frozenBoxList.forEach(s -> frozenBoxCodeStr.add(s.getFrozenBoxCode()));
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findByFrozenBoxCodeIn(frozenBoxCodeStr);
        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByFrozenBoxCodesAndStatus(frozenBoxCodeStr);

        String stockInCode = bankUtil.getUniqueIDByDate("B", date);
        String receiver = "NA";
        Long optPerson_id_1 = null;
        Long optPerson_id_2 = null;
        Long receiverId = null;
        if (!StringUtils.isEmpty(receiver)) {
            receiverId = Constants.RECEIVER_MAP.get(receiver);
        }
        StockIn stockIn = new StockIn().projectCode("0037")
            .projectSiteCode(null)
            .receiveId(receiverId).receiveDate( date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            .receiveName(receiver)
            .stockInType("8001")
            .stockInDate(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
            .countOfSample(frozenTubeList.size()).stockInCode(stockInCode).project(project).projectSite(null)
            .status(Constants.STOCK_IN_PENDING);
        stockInRepository.save(stockIn);

        List<TranshipStockIn> transhipStockIns = new ArrayList<>();
        List<String> transhipCodes = new ArrayList<>();
        List<Tranship> tranships = new ArrayList<>();
        for (TranshipBox transhipBox : transhipBoxes) {
            Tranship tranship = transhipBox.getTranship();
            if (!transhipCodes.contains(tranship.getTranshipCode())) {
                transhipCodes.add(tranship.getTranshipCode());
                TranshipStockIn transhipStockIn = new TranshipStockIn();
                transhipStockIn.stockInCode(stockIn.getStockInCode()).stockIn(stockIn).transhipCode(tranship.getTranshipCode()).tranship(tranship).status(Constants.VALID);
                transhipStockIns.add(transhipStockIn);
                tranship.transhipState(Constants.TRANSHIPE_IN_STOCKING);
                tranships.add(tranship);
            }
        }
        transhipRepository.save(tranships);
        transhipStockInRepository.save(transhipStockIns);
        Map<String,List<FrozenTube>> tubeListGroupByBox = frozenTubeList.stream().collect(Collectors.groupingBy(s->s.getFrozenBoxCode()));

        for(FrozenBox frozenBox : frozenBoxList){

            List<FrozenTube> frozenTubes = tubeListGroupByBox.get(frozenBox.getFrozenBoxCode());
            StockInBox stockInBox = new StockInBox();
            List<StockInBox> oldStockInBox = stockInBoxRepository.findByFrozenBoxCode(frozenBox.getFrozenBoxCode());
            if(oldStockInBox.size()>0){
                stockInBox = oldStockInBox.get(0);
            }else{
                stockInBox = new StockInBox()
                    .equipmentCode(frozenBox.getEquipmentCode())
                    .areaCode(frozenBox.getAreaCode())
                    .supportRackCode(frozenBox.getSupportRackCode())
                    .rowsInShelf(frozenBox.getSupportRackCode())
                    .columnsInShelf(frozenBox.getColumnsInShelf())
                    .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode).area(frozenBox.getArea()).equipment(frozenBox.getEquipment()).supportRack(frozenBox.getSupportRack())
                    .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
            }

            stockInBox.countOfSample(frozenTubes.size()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCode);
            stockInBox.status(Constants.FROZEN_BOX_STOCKING);
            frozenBox.status(Constants.FROZEN_BOX_STOCKING);
            stockInBox.setStockIn(stockIn);
            stockInBoxRepository.save(stockInBox);
            frozenBoxRepository.save(frozenBox);
            List<StockInTube> stockInTubes = new ArrayList<>();
            List<FrozenTube> frozenTubeList1 = new ArrayList<>();
            for(FrozenTube tube : frozenTubes){
                tube.setFrozenTubeState(Constants.FROZEN_BOX_STOCKING);
                frozenTubeList1.add(tube);

                StockInTube stockInTube = new StockInTube()
                    .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                    .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(Constants.FROZEN_BOX_STOCKING)
                    .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                    .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                    .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                    .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                    .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                    .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                    .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                stockInTubes.add(stockInTube);
            }
            frozenTubeRepository.save(frozenTubeList1);
            stockInTubeRepository.save(stockInTubes);
        }
    }
    //导入0037项目的所有方法
    @Test
    public void import0037DataAllMethod(){
        this.importOptFor0037();
        this.importOptStockInFor0037();
        this.importOptStockInRNAFor0037();
        this.importOptUnStockInFor0037();
        this.importOptFor0037Move();
    }

///////////////////////////////////////////////////0037项目导入结束///////////////////////////////////////


    //检查有两次出库记录的冻存盒编码
    @Test
    public void checkRepeatStockOutRecord() throws ParseException {
        List<String> wrongBox = new ArrayList<>();
        List<Object> frozenBoxRecord = frozenBoxRepository.findFrozenBoxStockInAndOutRecord();
        List<String> frozenBoxCodeStr = frozenBoxRecord.stream().map(s->s.toString()).collect(Collectors.toList());
        List<List<String>> frozenBoxCodeStrEach100 = Lists.partition(frozenBoxCodeStr,100);
        for(List<String> frozenBoxCodeStr100 : frozenBoxCodeStrEach100){
            List<Object[]> frozenBoxRecordDetail = frozenBoxRepository.findFrozenBoxStockInAndOutRecordByBoxCodeIn(frozenBoxCodeStr100);
            Map<String,List<Object[]>> map = frozenBoxRecordDetail.stream().collect(Collectors.groupingBy(s->s[1].toString()));
            for(String key: map.keySet()){
                List<Object[]> recordList = map.get(key);
                String optName = "";
                for(Object[] obj : recordList){
                    log.info(obj[0].toString()+","+obj[1].toString()+","+obj[2].toString()+","+obj[3].toString());
                    String opt = obj[0].toString();
                    String frozenBoxCode = obj[1].toString();
                    if(frozenBoxCode.equals("00290804863")||frozenBoxCode.equals("00290806021")||frozenBoxCode.equals("00290804862")||frozenBoxCode.equals("00290908017")){
                        continue;
                    }
                    if(!optName.equals(opt)){
                        optName=opt;
                    }else{
                        wrongBox.add(obj[0].toString()+","+obj[1].toString()+","+obj[2].toString()+","+obj[3].toString());
                        //删除出库冻存盒，删除出库样本，删除交接冻存盒，删除交接样本
                        String time = obj[3].toString().substring(0,10);
                        StockOutFrozenBox stockOutFrozenBox = stockOutFrozenBoxRepository.findByFrozenBoxCodeAndCreatedDateLike(frozenBoxCode,time);
                        if(stockOutFrozenBox == null){
                            throw new BankServiceException("Not find");
                        }
                        StockOutBoxPosition stockOutBoxPosition = stockOutBoxPositionRepository.findByStockOutFrozenBoxId(stockOutFrozenBox.getId());
                        StockOutHandoverBox stockOutHandoverBox = stockOutHandoverBoxRepository.findByStockOutFrozenBoxId(stockOutFrozenBox.getId());
                        if(stockOutHandoverBox!=null){
                            List<StockOutHandoverDetails> stockOutHandoverDetailss = stockOutHandoverDetailsRepository.findByStockOutHandoverBoxId(stockOutHandoverBox.getId());
                            stockOutHandoverDetailsRepository.deleteInBatch(stockOutHandoverDetailss);
                            stockOutHandoverBoxRepository.delete(stockOutHandoverBox);
                        }
                       List<StockOutReqFrozenTube> stockOutReqFrozenTubes = stockOutReqFrozenTubeRepository.findByStockOutFrozenBoxId(stockOutFrozenBox.getId());
                        stockOutReqFrozenTubeRepository.deleteInBatch(stockOutReqFrozenTubes);
                        stockOutBoxPositionRepository.delete(stockOutBoxPosition);
                        stockOutFrozenBoxRepository.delete(stockOutFrozenBox);
                    }
                }
            }
        }

        log.info(wrongBox.toString());
    }
    //补充导入0029项目数据
    @Test
    public void importStockInFor0029_specialBox() {
        List<Tranship> transhipList = transhipRepository.findAll();
        List<String> boxCode1Str = new ArrayList<String>(){{add("530120022A");add("530120022R");add("530120022E");add("530120022W");}};
        List<TranshipBox> transhipBoxes = transhipBoxRepository.findByFrozenBoxCode1DIn(boxCode1Str);
        Map<String, List<TranshipBox>> listGroupByTranshipCode =
            transhipBoxes.stream().collect(Collectors.groupingBy(w -> w.getTranship().getTranshipCode()));
        for (Tranship tranship : transhipList) {
            if(!tranship.getTrackNumber().equals("1608055301-126930-1")&&!tranship.getTrackNumber().equals("1608055301-126930-2")
                &&!tranship.getTrackNumber().equals("1608055301-126930-3")&&!tranship.getTrackNumber().equals("1608055301-126930-4")){
                continue;
            }
            List<TranshipBox> transhipBoxList = listGroupByTranshipCode.get(tranship.getTranshipCode());
            TranshipStockIn transhipStockIn = transhipStockInRepository.findByTranshipCode(tranship.getTranshipCode());
            StockIn stockIn = transhipStockIn.getStockIn();
            stockIn.setCountOfSample(stockIn.getCountOfSample()+99);

            stockInRepository.save(stockIn);
            //保存入库盒
            for (TranshipBox transhipBox : transhipBoxList) {
                if(!transhipBox.getFrozenBoxCode1D().contains("530120022")){
                    continue;
                }
                FrozenBox frozenBox = transhipBox.getFrozenBox();
                List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxCode(transhipBox.getFrozenBoxCode());
                StockInBox stockInBox = new StockInBox().equipmentCode(frozenBox.getEquipmentCode())
                    .areaCode(frozenBox.getAreaCode())
                    .supportRackCode(frozenBox.getSupportRackCode())
                    .rowsInShelf(frozenBox.getRowsInShelf())
                    .columnsInShelf(frozenBox.getColumnsInShelf())
                    .status(Constants.FROZEN_BOX_STOCKED).countOfSample(frozenTubeList.size())
                    .frozenBoxCode1D(frozenBox.getFrozenBoxCode1D())
                    .frozenBoxCode(frozenBox.getFrozenBoxCode()).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockIn.getStockInCode()).area(frozenBox.getArea())
                    .equipment(frozenBox.getEquipment()).supportRack(frozenBox.getSupportRack())
                    .sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockInBoxRepository.save(stockInBox);
                //保存入库盒位置
                StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
                stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                    .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                    .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                    .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                    .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                    .stockInBox(stockInBox);
                stockInBoxPositionRepository.save(stockInBoxPosition);
                //保存入库管

                List<StockInTube> stockInTubes = new ArrayList<>();
                for (FrozenTube tube : frozenTubeList) {
                    StockInTube stockInTube = new StockInTube()
                        .status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                        .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                        .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                        .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                        .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                        .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                        .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                        .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                        .sampleClassificationCode(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationCode() : null)
                        .sampleClassificationName(tube.getSampleClassification() != null ? tube.getSampleClassification().getSampleClassificationName() : null)
                        .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                        .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                        .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
                    stockInTubes.add(stockInTube);
                }
                stockInTubeRepository.save(stockInTubes);
            }
        }
    }
    //补充导入11月17日出库数据
    @Test
    public void importStockOutFor0029_specialBox1117(){
        allOperations = 0;
        executedOperations[0] = 0;
        //从视图中查询所有的操作记录
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
//            System.out.println("连接成功！");
            log.info("链接成功！");
            String sqlForSelect = "select * from " + "jt_opt_1117" + " a where a.box_code_1 in  ('530120022A','530120022R','530120022W','530120022E') order by  a.OLD_DATE ";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()) {
                map = this.Result2Map(result, rsMeta);
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                DBUtilForTemp.close(con);
                log.info("数据库连接已关闭！");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Map<String, List<Map<String, Object>>> listGroupByDateAndOptionType =
            list.stream().collect(Collectors.groupingBy(w -> w.get("OLD_DATE").toString() + "&" + w.get("OPT_NAME").toString()));

        TreeMap<String, List<Map<String, Object>>> mapGroupByDateAndOptionType = new TreeMap<>();
        mapGroupByDateAndOptionType.putAll(listGroupByDateAndOptionType);
        allOperations = list.size();
        for (String optionType : mapGroupByDateAndOptionType.keySet()) {
            List<Map<String, Object>> opts1 = mapGroupByDateAndOptionType.get(optionType);
            optionType = optionType.split("&")[1];

            switch (optionType) {
                case "出库":
                    this.importBoxOutFromPeace2ForJT_1117(opts1);
                    break;
                case "移位入库":
                    this.importBoxForJTInStock_1023(opts1, Constants.STORANGE_IN_TYPE_MOVE);
                    break;
                case "复位入库":
                    this.importBoxForJTInStock_1023(opts1, Constants.STORANGE_IN_TYPE_REVERT);
                    break;
                default:
                    break;
            }
        }
        log.info(alist.toString());
    }
    private void importBoxOutFromPeace2ForJT_1117(List<Map<String, Object>> boxList) {

        //创建出库需求
        StockOutRequirement stockOutRequirement = stockOutRequirementRepository.findByRequirementCode("D20171117001");
        int countOfSample = 0;
        for(Map<String, Object> key: boxList){
            countOfSample+= Integer.valueOf(key.get("COUNT_OF_TUBE").toString());
        }
        stockOutRequirement.countOfSample(stockOutRequirement.getCountOfSample()+countOfSample).countOfSampleReal(stockOutRequirement.getCountOfSample()+countOfSample);
        stockOutRequirementRepository.save(stockOutRequirement);
        //创建出库任务
        StockOutTask stockOutTask =  stockOutTaskRepository.findByStockOutTaskCode("F20171117002");

        StockOutHandover stockOutHandover =  stockOutHandoverRepository.findByHandoverCode("G20171117003");
        for (Map<String, Object> key: boxList) {
            String boxCode1D = key.get("BOX_CODE_1").toString();
            FrozenBox frozenBox = frozenBoxRepository.findByFrozenBoxCode1D(boxCode1D);
            if (frozenBox == null) {
                log.info("冻存盒不存在！"+boxCode1D);
                continue;
            }
            String equipmentCode = "F1-01";
            String areaCode = "S01";
            String memo = key.get("MEMO") != null ? key.get("MEMO").toString() : null;
            ArrayList<String> memoList1 = new ArrayList<>();
            if(!StringUtils.isEmpty(frozenBox.getMemo())){
                memoList1.add(frozenBox.getMemo());
            }
            if(memo!=null){
                memoList1.add(memo);
            }
            Equipment equipment = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, equipment.getId());
            frozenBox.status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER).areaCode(areaCode).area(area).equipment(equipment)
                .equipmentCode(equipmentCode)
                .supportRackCode(null).supportRack(null).columnsInShelf(null).rowsInShelf(null).memo(String.join(",", memoList1));
            frozenBoxRepository.save(frozenBox);
            //保存出库盒
            StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
            stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_HANDOVER);
            stockOutFrozenBox.setFrozenBox(frozenBox);
            stockOutFrozenBox.setStockOutTask(stockOutTask);
            stockOutFrozenBox = stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode())
                .frozenBoxCode1D(frozenBox.getFrozenBoxCode1D()).
                    sampleTypeCode(frozenBox.getSampleTypeCode())
                .sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .areaCode(areaCode).area(area).equipment(equipment).equipmentCode(equipmentCode)
                .supportRack(null).supportRackCode(null).columnsInShelf(null).rowsInShelf(null)
                .projectSiteName(frozenBox.getProjectSiteName()).memo(frozenBox.getMemo());
            stockOutFrozenBoxRepository.save(stockOutFrozenBox);
            //保存冻存盒位置
            StockOutBoxPosition stockOutBoxPosition = new StockOutBoxPosition();

            stockOutBoxPosition.setStockOutFrozenBox(stockOutFrozenBox);
            stockOutBoxPosition.setEquipment(equipment);
            stockOutBoxPosition.setEquipmentCode(equipment != null ? equipment.getEquipmentCode() : null);
            stockOutBoxPosition.setArea(area);
            stockOutBoxPosition.setAreaCode(area != null ? area.getAreaCode() : null);
            stockOutBoxPosition.setStatus(Constants.VALID);
            stockOutBoxPositionRepository.save(stockOutBoxPosition);

            StockOutHandoverBox stockOutHandoverBox = new StockOutHandoverBox().stockOutHandover(stockOutHandover)
                .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                .equipmentCode(stockOutFrozenBox.getEquipmentCode())
                .areaCode(stockOutFrozenBox.getAreaCode())
                .supportRackCode(stockOutFrozenBox.getSupportRackCode())
                .rowsInShelf(stockOutFrozenBox.getRowsInShelf())
                .columnsInShelf(stockOutFrozenBox.getColumnsInShelf())
                .frozenBoxCode(stockOutFrozenBox.getFrozenBoxCode())
                .frozenBoxCode1D(stockOutFrozenBox.getFrozenBoxCode1D())
                .area(stockOutFrozenBox.getArea())
                .equipment(stockOutFrozenBox.getEquipment())
                .supportRack(stockOutFrozenBox.getSupportRack())
                .sampleTypeCode(stockOutFrozenBox.getSampleTypeCode())
                .sampleType(stockOutFrozenBox.getSampleType())
                .sampleTypeName(stockOutFrozenBox.getSampleTypeName())
                .sampleClassification(stockOutFrozenBox.getSampleClassification())
                .sampleClassificationCode(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(stockOutFrozenBox.getSampleClassification() != null ? stockOutFrozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(stockOutFrozenBox.getDislocationNumber())
                .emptyHoleNumber(stockOutFrozenBox.getEmptyHoleNumber())
                .emptyTubeNumber(stockOutFrozenBox.getEmptyTubeNumber())
                .frozenBoxType(stockOutFrozenBox.getFrozenBoxType())
                .frozenBoxTypeCode(stockOutFrozenBox.getFrozenBoxTypeCode())
                .frozenBoxTypeColumns(stockOutFrozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(stockOutFrozenBox.getFrozenBoxTypeRows())
                .isRealData(stockOutFrozenBox.getIsRealData())
                .isSplit(stockOutFrozenBox.getIsSplit())
                .project(stockOutFrozenBox.getProject())
                .projectCode(stockOutFrozenBox.getProjectCode())
                .projectName(stockOutFrozenBox.getProjectName())
                .projectSite(stockOutFrozenBox.getProjectSite())
                .projectSiteCode(stockOutFrozenBox.getProjectSiteCode())
                .projectSiteName(stockOutFrozenBox.getProjectSiteName())

                .stockOutFrozenBox(stockOutFrozenBox)
                .memo(stockOutFrozenBox.getMemo())
                .status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);
            stockOutHandoverBoxRepository.save(stockOutHandoverBox);
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
            List<FrozenTube> frozenTubeListLast = new ArrayList<>();
            List<StockOutReqFrozenTube> stockOutReqFrozenTubes = new ArrayList<>();
            List<StockOutHandoverDetails> stockOutHandoverDetailss = new ArrayList<>();
            for (FrozenTube frozenTube : frozenTubeList) {
                int times = frozenTube.getSampleUsedTimes() != null ? (frozenTube.getSampleUsedTimes() + 1) : 1;
                frozenTube.setSampleUsedTimes(times);
                frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER);

                ArrayList<String> memoList = new ArrayList<>();
                if (!StringUtils.isEmpty(frozenTube.getMemo())) {
                    memoList.add(frozenTube.getMemo());
                }
                if (!StringUtils.isEmpty(memo)) {
                    memoList.add(memo);
                }
                frozenTube.setMemo(String.join(",", memoList));
                frozenTubeListLast.add(frozenTube);

                StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube().status(Constants.STOCK_OUT_SAMPLE_COMPLETED)
                    .stockOutRequirement(stockOutRequirement).memo(String.join(",", memoList))
                    .frozenBox(frozenTube.getFrozenBox())
                    .frozenTube(frozenTube)
                    .tubeColumns(frozenTube.getTubeColumns())
                    .tubeRows(frozenTube.getTubeRows()).stockOutFrozenBox(stockOutFrozenBox).stockOutTask(stockOutTask);
                stockOutReqFrozenTube = stockOutReqFrozenTube.frozenBoxCode1D(frozenTube.getFrozenBox().getFrozenBoxCode1D())
                    .frozenBoxCode(frozenTube.getFrozenBoxCode()).errorType(frozenTube.getErrorType())
                    .frozenTubeCode(frozenTube.getFrozenTubeCode()).frozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
                    .frozenTubeTypeId(frozenTube.getFrozenTubeType().getId()).frozenTubeTypeCode(frozenTube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(frozenTube.getFrozenTubeTypeName()).frozenTubeVolumns(frozenTube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(frozenTube.getFrozenTubeVolumnsUnit()).sampleVolumns(frozenTube.getSampleVolumns())
                    .projectId(frozenTube.getProject() != null ? frozenTube.getProject().getId() : null).projectCode(frozenTube.getProjectCode())
                    .projectSiteId(frozenTube.getProjectSite() != null ? frozenTube.getProjectSite().getId() : null)
                    .projectSiteCode(frozenTube.getProjectSiteCode()).sampleClassificationId(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getId() : null)
                    .sampleClassificationCode(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenTube.getSampleClassification() != null ? frozenTube.getSampleClassification().getSampleClassificationName() : null)
                    .sampleCode(frozenTube.getSampleCode()).sampleTempCode(frozenTube.getSampleTempCode())
                    .sampleTypeId(frozenTube.getSampleType() != null ? frozenTube.getSampleType().getId() : null)
                    .sampleTypeCode(frozenTube.getSampleTypeCode()).sampleTypeName(frozenTube.getSampleTypeName()).sampleUsedTimes(frozenTube.getSampleUsedTimes())
                    .sampleUsedTimesMost(frozenTube.getSampleUsedTimesMost());
                stockOutReqFrozenTubes.add(stockOutReqFrozenTube);
                //保存交接详情
                StockOutHandoverDetails stockOutHandoverDetails = new StockOutHandoverDetails();
                stockOutHandoverDetails = stockOutHandoverDetails.status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER)
                    .stockOutReqFrozenTube(stockOutReqFrozenTube)
                    .stockOutHandoverBox(stockOutHandoverBox);
                stockOutHandoverDetails.setMemo(String.join(",", memoList));
                stockOutHandoverDetailss.add(stockOutHandoverDetails);
            }
            frozenTubeRepository.save(frozenTubeListLast);
            stockOutReqFrozenTubeRepository.save(stockOutReqFrozenTubes);
            stockOutHandoverDetailsRepository.save(stockOutHandoverDetailss);
            executedOperations[0]++;
            log.info("Finished:(%d/%d)"+ executedOperations[0]+"/" +allOperations);
        }
    }


    //合并交接单
    @Test
    public void combineStockOutHandover(){
        List<StockOutHandover> stockOutHandoverList = stockOutHandoverRepository.findAll();
       Map<String,List<StockOutHandover>> handoverListGroupByStockOutApplyAndHandoverTime =
           stockOutHandoverList.stream().collect(Collectors.groupingBy(s->s.getStockOutApply().getId()+"&"+s.getStockOutTask().getId()+"&"+s.getHandoverTime()));
       TreeMap<String,List<StockOutHandover>> handoverListTreeMapGroupByStockOutApplyAndHandoverTime = new TreeMap<>(handoverListGroupByStockOutApplyAndHandoverTime);
        for(String key :handoverListTreeMapGroupByStockOutApplyAndHandoverTime.keySet()){
           if(handoverListTreeMapGroupByStockOutApplyAndHandoverTime.get(key).size()==1){
               continue;
           }
            List<StockOutHandover> alist = handoverListTreeMapGroupByStockOutApplyAndHandoverTime.get(key);
           alist.sort(new Comparator<StockOutHandover>() {
               @Override
               public int compare(StockOutHandover o1, StockOutHandover o2) {
                   return o1.getHandoverCode().compareTo(o2.getHandoverCode());
               }
           });

            StockOutHandover stockOutHandover = alist.get(0);

           for(StockOutHandover s : alist){
               if(s.getId().equals(stockOutHandover.getId())){
                   continue;
               }
               List<StockOutHandoverBox> stockOutHandoverBoxes = stockOutHandoverBoxRepository.findByStockOutHandoverId(s.getId());
               stockOutHandoverBoxes.stream().forEach(b->b.stockOutHandover(stockOutHandover));
               stockOutHandoverBoxRepository.save(stockOutHandoverBoxes);
               stockOutHandoverRepository.delete(s);
           }
       }
    }
}
