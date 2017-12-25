package org.fwoxford.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import org.apache.commons.httpclient.HttpException;
import org.fwoxford.BioBankApp;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.dto.response.FrozenTubeImportingForm;
import org.fwoxford.service.dto.response.GeocoderSearchAddressResponse;
import org.fwoxford.service.dto.response.GeocoderSearchResponse;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.fwoxford.web.rest.util.GeocoderReaderUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.math.BigDecimal;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Test class for the ImportSampleTest REST controller.
 *
 * @see ImportSampleTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BioBankApp.class)
//@Transactional
public class ImportSampleTest {
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
    private final Logger log = LoggerFactory.getLogger(ImportSampleTest.class);

    /**
     * 创建项目
     *
     * @throws Exception
     */
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
                delegateRepository.saveAndFlush(delegate);
            }
            Project project = projectRepository.findByProjectCode(projectCode);
            if (project == null) {
                project = new Project().projectCode(projectCode).projectName(projectName).status("0001").delegate(delegate);
                projectRepository.saveAndFlush(project);
            }
        }
    }

    /**
     * 创建项目点
     *
     * @throws Exception
     */
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
                projectSiteRepository.saveAndFlush(projectSite);
                ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(project.getId(), projectSite.getId());
                if (projectRelate == null) {
                    projectRelate = new ProjectRelate().project(project).projectSite(projectSite).status("0001");
                    projectRelateRepository.saveAndFlush(projectRelate);
                }
            }
        }
    }

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
                projectSiteRepository.saveAndFlush(projectSite);
                ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(project.getId(), projectSite.getId());
                if (projectRelate == null) {
                    projectRelate = new ProjectRelate().project(project).projectSite(projectSite).status("0001");
                    projectRelateRepository.saveAndFlush(projectRelate);
                }
            }
        }
    }

    /**
     * 冻存架类型，固定值
     * 2017年6月29日09:32:05
     *
     * @throws Exception
     */
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
            supportRackTypeRepository.saveAndFlush(supportRackType);
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
            supportRackTypeRepository.saveAndFlush(supportRackType55);
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
            supportRackTypeRepository.saveAndFlush(supportRackType1);
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
            supportRackTypeRepository.saveAndFlush(supportRackType2);
        }
    }

    /**
     * 冻存管类型--固定值
     * 2017年6月29日09:32:45
     *
     * @throws Exception
     */
    @Test
    public void createFrozenTubeType() throws Exception {
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        if (frozenTubeType == null) {
            frozenTubeType = new FrozenTubeType().frozenTubeTypeCode("DCG").frozenTubeTypeName("冻存管(1ml)").sampleUsedTimesMost(10).frozenTubeVolumn(2.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType);
            assertThat(frozenTubeType).isNotNull();
        }
        FrozenTubeType frozenTubeType1 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("2DDCG");
        if (frozenTubeType1 == null) {
            frozenTubeType1 = new FrozenTubeType().frozenTubeTypeCode("2DDCG").frozenTubeTypeName("2D冻存管(0.5ml)").sampleUsedTimesMost(10).frozenTubeVolumn(0.5).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType1);
            assertThat(frozenTubeType1).isNotNull();
        }
        FrozenTubeType frozenTubeType2 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("6CXG");
        if (frozenTubeType2 == null) {
            frozenTubeType2 = new FrozenTubeType().frozenTubeTypeCode("6CXG").frozenTubeTypeName("采血管(6ml)").sampleUsedTimesMost(10).frozenTubeVolumn(6.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType2);
            assertThat(frozenTubeType2).isNotNull();
        }
        FrozenTubeType frozenTubeType3 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("RNA");
        if (frozenTubeType3 == null) {
            frozenTubeType3 = new FrozenTubeType().frozenTubeTypeCode("RNA").frozenTubeTypeName("RNA管(9ml)").sampleUsedTimesMost(10).frozenTubeVolumn(9.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType3);
            assertThat(frozenTubeType3).isNotNull();
        }
        FrozenTubeType frozenTubeType4 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("9CXG");
        if (frozenTubeType4 == null) {
            frozenTubeType4 = new FrozenTubeType().frozenTubeTypeCode("9CXG").frozenTubeTypeName("采血管(9ml)").sampleUsedTimesMost(10).frozenTubeVolumn(9.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType4);
            assertThat(frozenTubeType4).isNotNull();
        }
    }

    /**
     * 创建设备组--固定值
     * 2017年6月29日09:44:50
     *
     * @throws Exception
     */
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
            equipmentGroupRepository.saveAndFlush(equipmentGroup);
            assertThat(equipmentGroup).isNotNull();
        }
    }

    /**
     * 创建设备类型--固定值
     *
     * @throws Exception
     */
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
                    .equipmentModelName(type)
                    .equipmentType(code)
                    .areaNumber(area)
                    .shelveNumberInArea(0)
                    .status("0001");
                equipmentModleRepository.saveAndFlush(equipmentModle);
            }
        }
//        EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("FOMA907");
//        if(equipmentModle == null){
//            equipmentModle = new EquipmentModle().equipmentModelCode("FOMA907")
//                .equipmentModelName("FOMA907")
//                .equipmentType("Freezer")
//                .areaNumber(4)
//                .shelveNumberInArea(0)
//                .status("0001");
//            equipmentModleRepository.saveAndFlush(equipmentModle);
//        }
//        EquipmentModle equipmentModle1 = equipmentModleRepository.findByEquipmentModelCode("ColdRoom-3");
//        if(equipmentModle1 == null){
//            equipmentModle1 = new EquipmentModle().equipmentModelCode("ColdRoom-3")
//                .equipmentModelName("ColdRoom-3")
//                .equipmentType("coldroom")
//                .areaNumber(117)
//                .shelveNumberInArea(0)
//                .status("0001");
//            equipmentModleRepository.saveAndFlush(equipmentModle1);
//        }
//        EquipmentModle equipmentModle2= equipmentModleRepository.findByEquipmentModelCode("Haier-B");
//        if(equipmentModle2 == null){
//            equipmentModle2 = new EquipmentModle().equipmentModelCode("Haier-B")
//                .equipmentModelName("Haier-B")
//                .equipmentType("Freezer")
//                .areaNumber(4)
//                .shelveNumberInArea(0)
//                .status("0001");
//            equipmentModleRepository.saveAndFlush(equipmentModle2);
//        }
//        EquipmentModle equipmentModle3= equipmentModleRepository.findByEquipmentModelCode("Haier-C");
//        if(equipmentModle3 == null){
//            equipmentModle3 = new EquipmentModle().equipmentModelCode("Haier-C")
//                .equipmentModelName("Haier-C")
//                .equipmentType("Freezer")
//                .areaNumber(2)
//                .shelveNumberInArea(0)
//                .status("0001");
//            equipmentModleRepository.saveAndFlush(equipmentModle3);
//        }
    }

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
            equipmentRepository.saveAndFlush(entity);
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
                    areaRepository.saveAndFlush(area);
                }
                List<String> supportCodeStr = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    String newString = String.format("%0" + 2 + "d", i + 1);
                    supportCodeStr.add("R" + newString);
                }
                SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("S5x4");
                for (String supportCode : supportCodeStr) {
                    //冻存架
                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                    if (supportRack == null) {
                        supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                            .area(supportRackMapper.areaFromId(area.getId()));
                        supportRackRepository.saveAndFlush(supportRack);
                    }
                }
            }

        }

//        String[] equipmentCodeStr = new String[]{"F1-22","F1-23","F1-24"};
//        for(String equipmentCode:equipmentCodeStr){
//            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
//            EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("Haier-B");
//            if(equipmentModle == null){
//                throw new BankServiceException("设备型号导入失败");
//            }
//            Integer temperature =-80;
//            if(entity==null){
//                entity = new Equipment().equipmentAddress("样本中心D座").equipmentCode(equipmentCode)
//                    .equipmentGroup(equipmentMapper.equipmentGroupFromId(1L))
//                    .equipmentModle(equipmentModle).temperature(temperature).ampoulesMax(0).ampoulesMin(0).status("0001");
//            }
//            //设备
//            equipmentRepository.saveAndFlush(entity);
//
//            String[] areaCodeStr = new String[]{"S01","S02","S03","S04"};
//            for(String areaCode :areaCodeStr){
//                //区域
//                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode,entity.getId());
//                if(area==null){
//                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(5).equipmentCode(equipmentCode)
//                        .status("0001");
//                    areaRepository.saveAndFlush(area);
//                }
//                String[] supportCodeStr = new String[]{"R01","R02","R03","R04","R05"};
//                for(String supportCode :supportCodeStr){
//                    //冻存架
//                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),supportCode);
//                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("S5x4");
//                    if(supportRack==null){
//                        supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
//                            .area(supportRackMapper.areaFromId(area.getId()));
//                        supportRackRepository.saveAndFlush(supportRack);
//                        assertThat(supportRack).isNotNull();
//                    }
//                }
//            }
//        }
    }

    @Test
    public void createEquipmentForFOMA907() {
        String[] equipmentCodeStr = new String[]{"F1-03", "F1-04"};
        for (String equipmentCode : equipmentCodeStr) {
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("FOMA907");
            if (equipmentModle == null) {
                throw new BankServiceException("设备型号导入失败");
            }
            Integer temperature = -80;
            if (entity == null) {
                entity = new Equipment().equipmentAddress("样本中心D座").equipmentCode(equipmentCode)
                    .equipmentGroup(equipmentMapper.equipmentGroupFromId(1L))
                    .equipmentModle(equipmentModle).temperature(temperature).ampoulesMax(0).ampoulesMin(0).status("0001");
            }
            //设备
            equipmentRepository.saveAndFlush(entity);

            String[] areaCodeStr = new String[]{"S01", "S02", "S03", "S04"};
            for (String areaCode : areaCodeStr) {
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(6).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.saveAndFlush(area);
                }
                String[] supportCodeStr = new String[]{"R01", "R02", "R03", "R04", "R05", "R06"};
                for (String supportCode : supportCodeStr) {
                    //冻存架
                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    if (supportRack == null) {
                        supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                            .area(supportRackMapper.areaFromId(area.getId()));
                        supportRackRepository.saveAndFlush(supportRack);
                    }
                }
            }
        }
    }

    @Test
    public void createEquipmentForColdRoom3() {
        String[] equipmentCodeStr = new String[]{"R4-03"};
        for (String equipmentCode : equipmentCodeStr) {
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("ColdRoom-3");
            if (equipmentModle == null) {
                throw new BankServiceException("设备型号导入失败");
            }
            Integer temperature = -40;
            if (entity == null) {
                entity = new Equipment().equipmentAddress("样本中心D座").equipmentCode(equipmentCode)
                    .equipmentGroup(equipmentMapper.equipmentGroupFromId(1L))
                    .equipmentModle(equipmentModle).temperature(temperature).ampoulesMax(0).ampoulesMin(0).status("0001");
            }
            //设备
            equipmentRepository.saveAndFlush(entity);

            String[] areaCodeStr = new String[]{"S022", "S202"};
            for (String areaCode : areaCodeStr) {
                int freezeFrameNumber = areaCode.equals("S022") ? 13 : 10;
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(freezeFrameNumber).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.saveAndFlush(area);
                }
                String[] supportCodeStr = new String[]{};
                if (areaCode.equals("S022")) {
                    supportCodeStr = new String[]{"R01", "R02", "R03", "R04", "R05", "R06", "R07", "R08", "R09", "R10", "R11", "R12", "R13"};
                } else {
                    supportCodeStr = new String[]{"R01", "R02", "R03", "R04", "R05", "R06", "R07", "R08", "R09", "R10"};
                }
                for (String supportCode : supportCodeStr) {
                    //冻存架
                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    if (supportRack == null) {
                        supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                            .area(supportRackMapper.areaFromId(area.getId()));
                        supportRackRepository.saveAndFlush(supportRack);
                    }
                }
            }
        }
    }

    /**
     * 创建冻存盒类型--固定值
     *
     * @throws Exception
     */
    @Test
    public void createFrozenBoxType() throws Exception {
        //冻存盒类型
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        if (frozenBoxType == null) {
            frozenBoxType = new FrozenBoxType().frozenBoxTypeCode("DCH")
                .frozenBoxTypeName("冻存盒(10*10)").frozenBoxTypeColumns("10").frozenBoxTypeRows("10").status("0001");
            frozenBoxType.setCreatedBy("admin");
            frozenBoxTypeRepository.saveAndFlush(frozenBoxType);
        }
        //冻存盒类型
        FrozenBoxType frozenBoxType1 = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DJH");
        if (frozenBoxType1 == null) {
            frozenBoxType1 = new FrozenBoxType().frozenBoxTypeCode("DJH")
                .frozenBoxTypeName("大橘盒(10*10)").frozenBoxTypeColumns("10").frozenBoxTypeRows("10").status("0001");
            frozenBoxTypeRepository.saveAndFlush(frozenBoxType1);
        }
        //冻存盒类型
        FrozenBoxType frozenBoxType2 = frozenBoxTypeRepository.findByFrozenBoxTypeCode("96KB");
        if (frozenBoxType2 == null) {
            frozenBoxType2 = new FrozenBoxType().frozenBoxTypeCode("96KB")
                .frozenBoxTypeName("96孔板(8*12)").frozenBoxTypeColumns("12").frozenBoxTypeRows("8").status("0001");
            frozenBoxTypeRepository.saveAndFlush(frozenBoxType2);
        }
    }

    /**
     * 创建样本类型
     *
     * @throws Exception
     */
    @Test
    public void createSampleType() throws Exception {

        SampleType sampleType1 = sampleTypeRepository.findBySampleTypeCode("A");
        if (sampleType1 == null) {
            sampleType1 = new SampleType().sampleTypeCode("A").sampleTypeName("血浆")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(240,224,255)");
            sampleTypeRepository.saveAndFlush(sampleType1);
            assertThat(sampleType1).isNotNull();
        }
        SampleType sampleType2 = sampleTypeRepository.findBySampleTypeCode("W");
        if (sampleType2 == null) {
            sampleType2 = new SampleType().sampleTypeCode("W").sampleTypeName("白细胞")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,255,255)");
            sampleTypeRepository.saveAndFlush(sampleType2);
            assertThat(sampleType2).isNotNull();
        }
        SampleType sampleType3 = sampleTypeRepository.findBySampleTypeCode("F");
        if (sampleType3 == null) {
            sampleType3 = new SampleType().sampleTypeCode("F").sampleTypeName("血清")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,179,179)");
            sampleTypeRepository.saveAndFlush(sampleType3);
            assertThat(sampleType3).isNotNull();
        }
        SampleType sampleType4 = sampleTypeRepository.findBySampleTypeCode("E");
        if (sampleType4 == null) {
            sampleType4 = new SampleType().sampleTypeCode("E").sampleTypeName("尿")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,255,179)");
            sampleTypeRepository.saveAndFlush(sampleType4);
            assertThat(sampleType4).isNotNull();
        }
        SampleType sampleType5 = sampleTypeRepository.findBySampleTypeCode("R");
        if (sampleType5 == null) {
            sampleType5 = new SampleType().sampleTypeCode("R").sampleTypeName("红细胞")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(236,236,236)");
            sampleTypeRepository.saveAndFlush(sampleType5);
            assertThat(sampleType5).isNotNull();
        }
        SampleType sampleType6 = sampleTypeRepository.findBySampleTypeCode("RNA");
        if (sampleType6 == null) {
            sampleType6 = new SampleType().sampleTypeCode("RNA").sampleTypeName("RNA")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,220,165)");
            sampleTypeRepository.saveAndFlush(sampleType6);
            assertThat(sampleType6).isNotNull();
        }
    }

    /**
     * 导入冻存盒
     *
     * @throws Exception
     */
    public void createFrozenBoxForA02(String tableName, String sampleTypeCode, FrozenTubeType frozenTubeType) throws Exception {
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
            String sqlForSelect = "select * from " + tableName;// 预编译语句
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
        Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
        for (int i = 0; i < list.size(); i++) {

            String boxCode = list.get(i).get("BOX_CODE").toString();
            if (map.get(boxCode) == null) {
                List<Map<String, Object>> mapList = new ArrayList<>();
                mapList.add(list.get(i));
                map.put(boxCode, mapList);
            } else {
                List<Map<String, Object>> mapList = map.get(boxCode);
                mapList.add(list.get(i));
                map.put(boxCode, mapList);
            }
        }

        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode(sampleTypeCode.equals("RNA") ? "DJH" : "DCH");
        int m = 0;
        for (String key : map.keySet()) {
            m++;
            List<Map<String, Object>> sampleList = map.get(key);
            if (sampleList.get(0).get("SAMPLE_CODE") == null) {
                throw new BankServiceException("样本编码为空！" + tableName + ":盒子编码：" + key, sampleList.get(0).toString());
            }
            String sampleClassTypeCode = sampleTypeCode != "RNA" ? sampleList.get(0).get("SAMPLE_TYPE_CODE").toString() : "11";
            String sampleClassTypeName = Constants.SAMPLE_TYPE_MAP.get(sampleClassTypeCode);
            Long LCC_ID = null;
            try {
                con = DBUtilForTemp.open();
                System.out.println("连接成功！");
                String sqlForSelectSite = "select DISTINCT LCC_ID from biobank_temp_01.fen_he_ji_lu where SAMPLE_ID = ?";
                PreparedStatement pstmt = (PreparedStatement) con.prepareStatement(sqlForSelectSite);
                pstmt.setString(1, sampleList.get(0).get("SAMPLE_CODE").toString());
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
            String stockInCodeNew = bankUtil.getUniqueID("B");
            StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCodeNew);
            String receiver = sampleList.get(0).get("ECEIVER") != null ? sampleList.get(0).get("ECEIVER").toString() : null;
            String opt = sampleList.get(0).get("OPT") != null ? sampleList.get(0).get("OPT").toString() : null;
            String storeKeeper1 = sampleList.get(0).get("USER_1") != null ? sampleList.get(0).get("USER_1").toString() : null;
            String storeKeeper2 = sampleList.get(0).get("USER_2") != null ? sampleList.get(0).get("USER_2").toString() : null;
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            LocalDate stockInDate = sampleList.get(0).get("OPT_DATE") != null && !sampleList.get(0).get("OPT_DATE").equals("NA") ? format.parse(sampleList.get(0).get("OPT_DATE").toString()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            LocalDate receiveDate = sampleList.get(0).get("REC_DATE") != null && !sampleList.get(0).get("REC_DATE").equals("NA") ? format.parse(sampleList.get(0).get("REC_DATE").toString()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

            Long receiverId = null;
            String stockInType = null;
            Long storeKeeperId1 = null;
            Long storeKeeperId2 = null;
            if (!StringUtils.isEmpty(receiver)) {
                receiverId = Constants.RECEIVER_MAP.get(receiver);
            }
            if (!StringUtils.isEmpty(opt)) {
                stockInType = Constants.STOCK_IN_TYPE_MAP.get(opt);
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
                    .stockInType(stockInType)
                    .storeKeeperId1(storeKeeperId1)
                    .storeKeeper1(storeKeeper1)
                    .storeKeeperId2(storeKeeperId2)
                    .storeKeeper2(storeKeeper2)
                    .stockInDate(stockInDate)
                    .countOfSample(sampleList.size()).stockInCode(stockInCodeNew).project(project).projectSite(projectSite)
                    .memo("PEACE5 项目" + LocalDate.now() + "数据导入")
                    .status(Constants.STOCK_IN_COMPLETE);
                stockInRepository.saveAndFlush(stockIn);
                assertThat(stockIn).isNotNull();
            }

            if (sampleTypeCode != "RNA" && sampleList.get(0).get("POS_IN_SHELF") == null) {
                throw new BankServiceException("所在架子内位置为空！" + tableName + ":盒子编码：" + key);
            }

            SampleClassification sampleClassification = null;
            sampleClassification = sampleClassificationRepository.findBySampleClassificationCode(sampleClassTypeCode);
            String frontColor = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get(sampleClassTypeCode).toString();
            if (sampleClassification == null) {
                sampleClassification = new SampleClassification()
                    .sampleClassificationCode(sampleClassTypeCode)
                    .sampleClassificationName(sampleClassTypeName)
                    .status("0001")
                    .backColor(frontColor)
                    .frontColor("black");
                sampleClassificationRepository.saveAndFlush(sampleClassification);
                assertThat(sampleClassification).isNotNull();

                ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType.getId(), sampleClassification.getId());
                ;
                String columnNumber = Constants.COLUMNNUMBER_MAP.get(sampleClassTypeCode);
                if (projectSampleClass == null) {
                    projectSampleClass = new ProjectSampleClass()
                        .project(project).projectCode("0037")
                        .sampleClassification(sampleClassification)
                        .sampleType(sampleType).sampleClassificationCode(sampleClassification.getSampleClassificationCode()).sampleClassificationName(sampleClassification.getSampleClassificationName())
                        .columnsNumber("")
                        .status("0001");
                    projectSampleClassRepository.saveAndFlush(projectSampleClass);
                    assertThat(projectSampleClass).isNotNull();
                }
            }
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
            FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(key);
            if (frozenBox == null) {
                frozenBox = new FrozenBox()
                    .frozenBoxCode(key)
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
            frozenBoxRepository.saveAndFlush(frozenBox);
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
                    .frozenBoxCode(key).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCodeNew).area(area).equipment(entity).supportRack(supportRack)
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
                stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);
                assertThat(stockInBoxPosition).isNotNull();
            }
            //保存入库管子
            for (int i = 0; i < sampleList.size(); i++) {
                if (sampleList.get(i).get("SAMPLE_CODE") == null) {
                    throw new BankServiceException("样本编码为空！" + tableName + ":盒子编码：" + key, sampleList.get(i).toString());
                }
                String sampleCode = sampleList.get(i).get("SAMPLE_CODE").toString();

                if (sampleTypeCode != "RNA" && sampleList.get(i).get("POS_IN_BOX") == null) {
                    throw new BankServiceException("盒内位置为空！" + tableName + ":盒子编码：" + key + ",样本编码为：" + sampleCode, sampleList.get(i).toString());
                }
                String posInBox = sampleList.get(i).get("POS_IN_BOX").toString();
                String tubeRows = posInBox.substring(0, 1);
                String tubeColumns = posInBox.substring(1);
                FrozenTube frozenTubeList = frozenTubeRepository.findBySampleCodeAndProjectCodeAndSampleTypeCodeAndSampleClassificationCode(sampleCode, "0037", sampleType.getSampleTypeCode(), sampleClassTypeCode);
                String status = sampleList.get(i).get("SAMPLE_STATUS") != null ? Constants.FROZEN_TUBE_STATUS_MAP.get(sampleList.get(i).get("SAMPLE_STATUS").toString()).toString() : "3001";
                String memo = sampleList.get(i).get("MEMO") != null ? sampleList.get(i).get("MEMO").toString() : null;
                String times = sampleList.get(i).get("TIMES") != null ? sampleList.get(i).get("TIMES").toString() : null;
                if (frozenTubeList == null) {
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
                        .frozenBoxCode(key).frozenTubeType(frozenTubeType).sampleType(sampleType).sampleClassification(sampleClassification)
                        .project(project).projectSite(projectSite).frozenBox(frozenBox).frozenTubeState("2004");
                    frozenTubeRepository.saveAndFlush(tube);
                    assertThat(tube).isNotNull();

                    StockInTube stockInTube = stockInTubeRepository.findByFrozenTubeId(tube.getId());
                    if (stockInTube == null) {
                        stockInTube = new StockInTube()
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
                        assertThat(stockInTube).isNotNull();
                    }
                }
            }
        }
    }

    private Map<String, Object> Result2Map(ResultSet rs, ResultSetMetaData meta) throws SQLException {
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            map.put(meta.getColumnName(i), rs.getObject(meta.getColumnName(i)));
        }
        return map;
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
            sampleClassificationRepository.saveAndFlush(sampleClassification01A);
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
                projectSampleClassRepository.saveAndFlush(projectSampleClass);
            }
        }


        SampleType sampleType7 = sampleTypeRepository.findBySampleTypeCode("98");
        List<SampleClassification> sampleClassifications = sampleClassificationRepository.findAll();
        if (sampleType7 == null) {
            sampleType7 = new SampleType().sampleTypeCode("98").sampleTypeName("98")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(169,241,253)");
            sampleTypeRepository.saveAndFlush(sampleType7);
            assertThat(sampleType7).isNotNull();
        }
        SampleType sampleType8 = sampleTypeRepository.findBySampleTypeCode("99");
        if (sampleType8 == null) {
            sampleType8 = new SampleType().sampleTypeCode("99").sampleTypeName("99")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(169,241,253)");
            sampleTypeRepository.saveAndFlush(sampleType8);
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
                projectSampleClassRepository.saveAndFlush(projectSampleClass);
                assertThat(projectSampleClass).isNotNull();
            }
        }

        SampleType sampleType97 = sampleTypeRepository.findBySampleTypeCode("97");
        if (sampleType97 == null) {
            sampleType97 = new SampleType().sampleTypeCode("97").sampleTypeName("97")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(255, 0, 0)");
            sampleTypeRepository.saveAndFlush(sampleType97);
            assertThat(sampleType97).isNotNull();
        }

    }


    @Test
    public void updateStockInBoxAndStockInTube() throws Exception {
        List<StockInBox> stockInBoxes = stockInBoxRepository.findAll();
        for (StockInBox stockInBox : stockInBoxes) {
            FrozenBox frozenBox = stockInBox.getFrozenBox();
            stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName());
            stockInBoxRepository.saveAndFlush(stockInBox);
            List<StockInTube> stockInTubes = stockInTubeRepository.findByStockInBoxId(stockInBox.getId());
            for (StockInTube stockInTube : stockInTubes) {
                FrozenTube tube = stockInTube.getFrozenTube();
                stockInTube.status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
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
            }
        }
    }


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
                            projectSiteRepository.saveAndFlush(p);
                        }
                    }
                }
            }
            p.setProjectSiteId(projectSiteId);
            projectSiteRepository.saveAndFlush(p);
        }
    }

    public static void randomSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            randomSet(min, max, n - setSize, set);// 递归
        }
    }

    @Test
    public void createRadomNumber() throws Exception {
        HashSet<Integer> hashSet = new HashSet<>();
        randomSet(2001000, 2002000, 200, hashSet);
        System.out.print(hashSet.toString());
    }

    @Test
    public void getHttpClientInfo() {
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod("http://10.24.10.43:8080/biobank/specimens?fromdate=2017-01-01&todate=2017-02-01");
//        getMethod.getParams().setParameter("fromdate","2017-01-01");
//        getMethod.getParams().setParameter("todate","2017-02-01");
        HttpMethodParams params = new HttpMethodParams();
//        params.setParameter("boxcode","650420093");
        getMethod.setParams(params);
        getMethod.setRequestHeader("X-Auth-Token", "d93925bc7axKfXc2A5WFJEzuPXYX4q");
        try {
            int statusCode = httpClient.executeMethod(getMethod);
            if (statusCode != 200) {
                System.err.println("Method failed: "
                    + getMethod.getStatusLine());
            }
            byte[] responseBody = getMethod.getResponseBody();
            String str = new String(responseBody);
            JSONArray jsonArray = JSONArray.fromObject(str);
            List<FrozenTubeImportingForm> frozenTubeImportingForms = (List<FrozenTubeImportingForm>) JSONArray.toCollection(jsonArray, FrozenTubeImportingForm.class);
            System.out.println(frozenTubeImportingForms.toString());
        } catch (HttpException e) {
            System.out.println("Please check your provided http address!");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getMethod.releaseConnection();
        }
    }

    @Test
    public void test() {
//        int i=1;
//        int j=2;
//        char c1=(char) (i+64);
//        char c2=(char) (j+64);
//        System.out.println(c1);
//        System.out.println(c2);
//        String c1="A";
//        String c2="B";
//        int i= c1.toCharArray()[0]-64;
//        int j = c2.toCharArray()[0]-64;
//        System.out.println(i);
//        System.out.println(j);
//        List<Project> projects = new ArrayList<>();
//        for(int i = 0 ;i<1000;i++){
//            Project project = new Project().projectCode("123").status("0001").projectName("3333");
//            projectRepository.saveAndFlush(project);
//        }
//        String a ="68.00";
//        Float bb = Float.parseFloat(a);
//        int b3 = bb.intValue();
//        String c = "";
//        String[] str = new String[]{};
//        List<String> alist = new ArrayList<>();
//        for(int i=0;i<20;i++){
//            String newString = String.format("%0"+2+"d", i+1);
//            alist.add(newString);
//        }
//
//        String a = "";
        String optPerson1 = "2人组(王伟&YY&KK)";
        String optPerson2 = "NA";
        if (optPerson1.contains("&")) {
            String[] person = optPerson1.split("&");
            optPerson1 = person[0].substring(person[0].length() - 2, person[0].length());
            if (optPerson2.equals("NA")) {
                optPerson2 = person[1].substring(0, 2);
            }

        }
    }

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
        sampleClassificationRepository.saveAndFlush(sampleClassification08W);
        ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_W.getId(), sampleClassification08W.getId());
        if (projectSampleClass == null) {
            projectSampleClass = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification08W)
                .sampleType(sampleType_W)
                .columnsNumber(null).sampleClassificationCode("08").sampleClassificationName("W-白细胞")
                .status("0001");
            projectSampleClassRepository.saveAndFlush(projectSampleClass);
        }
        String frontColorE = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("09").toString();
        SampleClassification sampleClassification05E = new SampleClassification()
            .sampleClassificationCode("05")
            .sampleClassificationName("E-尿")
            .status("0001")
            .backColor(frontColorE)
            .frontColor("black");
        sampleClassificationRepository.saveAndFlush(sampleClassification05E);
        ProjectSampleClass projectSampleClass_E = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_E.getId(), sampleClassification05E.getId());
        if (projectSampleClass_E == null) {
            projectSampleClass_E = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification05E)
                .sampleType(sampleType_E)
                .columnsNumber(null).sampleClassificationCode("05").sampleClassificationName("E-尿")
                .status("0001");
            projectSampleClassRepository.saveAndFlush(projectSampleClass_E);
        }
        String frontColorR = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("04").toString();
        SampleClassification sampleClassification09R = new SampleClassification()
            .sampleClassificationCode("09")
            .sampleClassificationName("R-红细胞")
            .status("0001")
            .backColor(frontColorR)
            .frontColor("black");
        sampleClassificationRepository.saveAndFlush(sampleClassification09R);
        ProjectSampleClass projectSampleClass_R = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_R.getId(), sampleClassification09R.getId());
        if (projectSampleClass_R == null) {
            projectSampleClass_R = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification09R)
                .sampleType(sampleType_R)
                .columnsNumber(null).sampleClassificationCode("09").sampleClassificationName("R-红细胞")
                .status("0001");
            projectSampleClassRepository.saveAndFlush(projectSampleClass_R);
        }
        String frontColorA = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("01").toString();
        SampleClassification sampleClassification01A = new SampleClassification()
            .sampleClassificationCode("01")
            .sampleClassificationName("A-血浆")
            .status("0001")
            .backColor(frontColorA)
            .frontColor("black");
        sampleClassificationRepository.saveAndFlush(sampleClassification01A);
        ProjectSampleClass projectSampleClass_A = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_A.getId(), sampleClassification01A.getId());
        if (projectSampleClass_A == null) {
            projectSampleClass_A = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification01A)
                .sampleType(sampleType_A)
                .columnsNumber(null).sampleClassificationCode("01").sampleClassificationName("A-血浆")
                .status("0001");
            projectSampleClassRepository.saveAndFlush(projectSampleClass_A);
        }
    }

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
        sampleClassificationRepository.saveAndFlush(sampleClassification08W);
        ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_W.getId(), sampleClassification08W.getId());
        if (projectSampleClass == null) {
            projectSampleClass = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification08W)
                .sampleType(sampleType_W)
                .columnsNumber(null).sampleClassificationCode("08").sampleClassificationName("EDTA抗凝白细胞")
                .status("0001");
            projectSampleClassRepository.saveAndFlush(projectSampleClass);
        }
        String frontColorE = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("09").toString();
        SampleClassification sampleClassification05E = new SampleClassification()
            .sampleClassificationCode("05")
            .sampleClassificationName("尿")
            .status("0001")
            .backColor(frontColorE)
            .frontColor("black");
        sampleClassificationRepository.saveAndFlush(sampleClassification05E);
        ProjectSampleClass projectSampleClass_E = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_E.getId(), sampleClassification05E.getId());
        if (projectSampleClass_E == null) {
            projectSampleClass_E = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification05E)
                .sampleType(sampleType_E)
                .columnsNumber(null).sampleClassificationCode("05").sampleClassificationName("尿")
                .status("0001");
            projectSampleClassRepository.saveAndFlush(projectSampleClass_E);
        }
        String frontColorR = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("04").toString();
        SampleClassification sampleClassification09R = new SampleClassification()
            .sampleClassificationCode("09")
            .sampleClassificationName("红细胞")
            .status("0001")
            .backColor(frontColorR)
            .frontColor("black");
        sampleClassificationRepository.saveAndFlush(sampleClassification09R);
        ProjectSampleClass projectSampleClass_R = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_R.getId(), sampleClassification09R.getId());
        if (projectSampleClass_R == null) {
            projectSampleClass_R = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification09R)
                .sampleType(sampleType_R)
                .columnsNumber(null).sampleClassificationCode("09").sampleClassificationName("红细胞")
                .status("0001");
            projectSampleClassRepository.saveAndFlush(projectSampleClass_R);
        }
        String frontColorA = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get("01").toString();
        SampleClassification sampleClassification01A = new SampleClassification()
            .sampleClassificationCode("01")
            .sampleClassificationName("EDTA抗凝血浆")
            .status("0001")
            .backColor(frontColorA)
            .frontColor("black");
        sampleClassificationRepository.saveAndFlush(sampleClassification01A);
        ProjectSampleClass projectSampleClass_A = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(), sampleType_A.getId(), sampleClassification01A.getId());
        if (projectSampleClass_A == null) {
            projectSampleClass_A = new ProjectSampleClass()
                .project(project).projectCode(project.getProjectCode())
                .sampleClassification(sampleClassification01A)
                .sampleType(sampleType_A)
                .columnsNumber(null).sampleClassificationCode("01").sampleClassificationName("EDTA抗凝血浆")
                .status("0001");
            projectSampleClassRepository.saveAndFlush(projectSampleClass_A);
        }
    }

    @Test
    public void main() throws Exception {
        this.createProject();
        this.createProjectSite();//peace5项目点
        this.createProjectSiteForPeace3();
        this.createSupportRackType();
        this.createEquipmentGroup();
        this.createEquipmentModel();
        this.createEquipment();
        this.createEquipmentForFOMA907();
        this.createEquipmentForColdRoom3();
        this.createFrozenTubeType();
        this.createFrozenBoxType();
        this.createSampleType();
        FrozenTubeType rnaTube = frozenTubeTypeRepository.findByFrozenTubeTypeCode("RNA");
        FrozenTubeType dcgTube = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        if (rnaTube == null || dcgTube == null) {
            throw new BankServiceException("冻存管类型导入失败！");
        }
        this.createFrozenBoxForA02("HE_COL_01", "A", dcgTube);
        this.createFrozenBoxForA02("HE_COL_02", "A", dcgTube);
        this.createFrozenBoxForA02("HE_COL_03", "W", dcgTube);
        this.createFrozenBoxForA02("HE_COL_04", "R", dcgTube);
        this.createFrozenBoxForA02("HE_COL_05", "A", dcgTube);
        this.createFrozenBoxForA02("HE_COL_06", "A", dcgTube);
        this.createFrozenBoxForA02("HE_COL_07", "F", dcgTube);
        this.createFrozenBoxForA02("HE_COL_08", "F", dcgTube);
        this.createFrozenBoxForA02("HE_COL_09", "E", dcgTube);
        this.createFrozenBoxForA02("HE_COL_10", "E", dcgTube);
        this.createFrozenBoxForA02("HE_COL_11_RNA", "RNA", rnaTube);
        this.createSampleTypeFor0037();
        this.importLocationForProjectSite();
        this.createSampleTypeAndClassFor0038();
//        this.createSampleTypeAndClassFor0029();// 需要在这里执行时去掉方法上的@Test
    }

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
                projectSiteRepository.saveAndFlush(projectSite);
                ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(project.getId(), projectSite.getId());
                if (projectRelate == null) {
                    projectRelate = new ProjectRelate().project(project).projectSite(projectSite).status("0001");
                    projectRelateRepository.saveAndFlush(projectRelate);
                }
            }
        }
    }

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
            projectSiteRepository.saveAndFlush(projectSite);
            ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(project.getId(), projectSite.getId());
            if (projectRelate == null) {
                projectRelate = new ProjectRelate().project(project).projectSite(projectSite).status("0001");
                projectRelateRepository.saveAndFlush(projectRelate);
            }
        }
    }

    @Autowired
    CoordinateRepository coordinateRepository;

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
            coordinateRepository.saveAndFlush(coordinate);
        }

    }

    @Autowired
    ProvinceRepository provinceRepository;

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
                        provinceRepository.saveAndFlush(province);
                    }
                }
            }
        }

    }

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
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(equipment.getId())).freezeFrameNumber(freezeFrameNumber).equipmentCode(equipmentCode.get(i))
                        .status("0001");
                    areaRepository.saveAndFlush(area);
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
                        supportRackRepository.saveAndFlush(supportRack);
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

    /**
     * 导入第一步，导入基础数据
     *
     * @throws Exception
     */
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

    /**
     * 导入第二步，导入首次入库记录
     *
     * @throws Exception
     */
    @Test
    public void importForPeace2() throws Exception {
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        if (frozenTubeType == null) {
            throw new BankServiceException("冻存管类型导入失败！");
        }
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sql = "select * from SAMPLE_0029 ";// 预编译语句
            pre = con.prepareStatement(sql);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetas = result.getMetaData();
            Map<String, Object> sampleMap = null;
            while (result.next()) {
                sampleMap = this.Result2Map(result, rsMetas);
                list.add(sampleMap);
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
        Map<String, List<Map<String, Object>>> sampleMap = new HashMap<>();
        List<String> sampleCodes = new ArrayList<>();
        for (Map<String, Object> sample : list) {
            if (sample.get("BOX_CODE") == null) {
                throw new BankServiceException("冻存盒编码不存在", sample.toString());
            }
            String key = sample.get("BOX_CODE").toString();
            String sampleCode = sample.get("TUBE_CODE").toString();
            if (sampleCodes.contains(sampleCode)) {
                continue;
            }
            sampleCodes.add(sampleCode);
            List<Map<String, Object>> alist = new ArrayList<>();
            if (sampleMap.get(key) == null || sampleMap.get(key).size() == 0) {
                alist.add(sample);
                sampleMap.put(key, alist);
            } else {
                alist = sampleMap.get(key);
                alist.add(sample);
                sampleMap.put(key, alist);
            }
        }
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        List<Equipment> equipments = equipmentRepository.findAll();
        List<Area> areas = areaRepository.findAll();
        List<SupportRack> supportRacks = supportRackRepository.findAll();
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode("A");
        Project project = projectRepository.findByProjectCode("0029");

        importBoxForPeace2("HE_A_0908", "A", frozenTubeType, frozenBoxType, sampleMap, null, projectSiteList, equipments, areas, supportRacks, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("R");
        importBoxForPeace2("HE_R_0908", "R", frozenTubeType, frozenBoxType, sampleMap, null, projectSiteList, equipments, areas, supportRacks, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("E");
        importBoxForPeace2("HE_E_0908", "E", frozenTubeType, frozenBoxType, sampleMap, null, projectSiteList, equipments, areas, supportRacks, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("W");
        importBoxForPeace2("HE_W_0908", "W", frozenTubeType, frozenBoxType, sampleMap, null, projectSiteList, equipments, areas, supportRacks, sampleType, project);
    }

    /**
     * 导入第三步，导入操作记录
     *
     * @throws Exception
     */
    @Test
    public void importOptRecordForPeace2() {
        importBoxRecordForPeace2("A_RECORD", "A");
        importBoxRecordForPeace2("E_RECORD", "E");
        importBoxRecordForPeace2("W_RECORD", "W");
        importBoxRecordForPeace2("R_RECORD", "R");
    }

    /**
     * 导入操作记录的具体实现
     *
     * @param tableName
     * @param type
     */
    private void importBoxRecordForPeace2(String tableName, String type) {
        //从视图中查询所有的操作记录
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from " + tableName + " a order by a.OPT_YEAR, a.OLD_DATE, a.OPT_PERSON_1, a.OPT_PERSON_2, a.BOX_CODE_1, a.BOX_CODE";// 预编译语句
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

        Map<String, List<Map<String, Object>>> listGroupByDateAndOptionType =
            list.stream().collect(Collectors.groupingBy(w -> w.get("OLD_DATE").toString() + "&" + w.get("TABLENAME").toString()));

        TreeMap<String, List<Map<String, Object>>> mapGroupByDateAndOptionType = new TreeMap<>();
        mapGroupByDateAndOptionType.putAll(listGroupByDateAndOptionType);
        for (String optionType : mapGroupByDateAndOptionType.keySet()) {
            List<Map<String, Object>> opts1 = mapGroupByDateAndOptionType.get(optionType);
            Map<String, List<Map<String, Object>>> listGroupByTask = null;
            optionType = optionType.split("&")[1];
            switch (optionType) {
                case "移位":
                    // 不分组直接导入
                    opts1.forEach(o -> this.importMoveBoxForPeace2(o, type));
                    break;
                case "销毁":
                    // 不分组直接导入
                    opts1.forEach(o -> this.importDestroyRecord(o));
                    break;
                case "出库":
                case "出库2":
                    // 根据临时盒编码分组, 为空时任务编码分组, 为空时操作员分组, 为空时一维冻存盒编码进行的分组
                    listGroupByTask =
                        opts1.stream().collect(Collectors.groupingBy(w -> {
                            Object oprator1 = Optional.ofNullable(w.get("OPT_PERSON_1")).orElse("");
                            Object oprator2 = Optional.ofNullable(w.get("OPT_PERSON_2")).orElse("");
                            String oprators = oprator1.toString() + oprator2.toString();
                            Object taskKey = Optional.ofNullable(w.get("TEMP_BOX")).orElse(w.get("TASK_CODE"));
                            if (oprators.length() > 0) {
                                taskKey = Optional.ofNullable(taskKey).orElse(oprators);
                            } else {
                                taskKey = Optional.ofNullable(taskKey).orElse(w.get("BOX_CODE_1"));
                            }
                            return taskKey.toString();
                        }));

                    for (String task : listGroupByTask.keySet()) {
                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
                        this.importBoxOutForPeace2(opts2, type);
                    }
                    break;
                case "移位入库":
                    // 根据二维冻存盒编码进行的分组
                    listGroupByTask =
                        opts1.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));

                    for (String task : listGroupByTask.keySet()) {
                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
                        this.importBoxForReInStock(opts2, type, Constants.STORANGE_IN_TYPE_MOVE);
                    }
                    break;
                case "复位":
                    // 根据二维冻存盒编码进行的分组
                    listGroupByTask =
                        opts1.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));

                    for (String task : listGroupByTask.keySet()) {
                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
                        this.importBoxForReInStock(opts2, type, Constants.STORANGE_IN_TYPE_REVERT);
                    }
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
        FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(boxCode);
        if (frozenBox == null) {
            return;
        }
        String type = Constants.MOVE_TYPE_FOR_TUBE;
        //整盒销毁
        if (boxCode1.equals(boxCode)) {
            type = Constants.MOVE_TYPE_FOR_BOX;
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
            frozenBoxRepository.saveAndFlush(frozenBox);
            List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
            for (FrozenTube f : frozenTubeList) {
                f.setFrozenTubeState(Constants.FROZEN_BOX_DESTROY);
                frozenTubeRepository.saveAndFlush(f);
            }
            saveDestroyDetail(positionDestroy, Constants.MOVE_TYPE_FOR_BOX, frozenTubeList);
        } else {
            FrozenTube frozenTube = frozenTubeRepository.findBySampleCodeAndSampleTypeCode(boxCode, sampleType);
            frozenTube.setFrozenTubeState(Constants.FROZEN_BOX_DESTROY);
            frozenTubeRepository.saveAndFlush(frozenTube);
            List<FrozenTube> frozenTubeList = new ArrayList<FrozenTube>() {{
                add(frozenTube);
            }};
            saveDestroyDetail(positionDestroy, Constants.MOVE_TYPE_FOR_BOX, frozenTubeList);
        }

    }

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
        String boxCode = map.get("BOX_CODE").toString();
        System.out.print(boxCode);
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
        if (!StringUtils.isEmpty(memo)) {
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
            throw new BankServiceException("区域未导入:" + equipmentCode + areaCode);
        }
        //冻存架
        SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
        if (supportRack == null) {
            throw new BankServiceException("冻存架未导入:" + supportCode);
        }
        String posInShelf = map.get("POS") != null ? map.get("POS").toString() : null;
        if (posInShelf == null || posInShelf.equals("NA")) {
            Long count = frozenBoxRepository.countByEquipmentCodeAndAreaCodeAndSupportRackCode(
                equipmentCode, areaCode, supportCode);
            posInShelf = "A" + (count.intValue() + 1);
        }

        String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
        String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
        frozenBox = frozenBox.equipment(supportRack.getArea().getEquipment()).equipmentCode(supportRack.getArea().getEquipmentCode())
            .area(supportRack.getArea())
            .areaCode(supportRack.getArea().getAreaCode())
            .supportRack(supportRack)
            .supportRackCode(supportRack.getSupportRackCode())
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

    public void saveMoveDetail(PositionMove positionMove, String moveType, List<FrozenTube> frozenTubeList) {
        List<PositionMoveRecord> positionMoveRecordList = new ArrayList<PositionMoveRecord>();
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
            positionMoveRecord.setCreatedDate(positionMove.getCreatedDate());

            positionMoveRecordRepository.save(positionMoveRecord);
        }
    }

    //首次入库保存
    public void importBoxForPeace2(String tableName, String sampleTypeCode, FrozenTubeType frozenTubeType, FrozenBoxType frozenBoxType
        , Map<String, List<Map<String, Object>>> sampleMap, String sqlAppend, List<ProjectSite> projectSiteList, List<Equipment> equipments, List<Area> areas, List<SupportRack> supportRacks, SampleType sampleType, Project project) {

        List<ProjectSampleClass> projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeId(project.getId(), sampleType.getId());
        SampleClassification sampleClassification = projectSampleClass.get(0).getSampleClassification();

        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from " + tableName + " where opt_name = '首次入库' ";// 预编译语句
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
            for (int j = 0; j < boxList.size(); j++) {
                String boxCode = boxList.get(j).get("BOX_CODE").toString().trim();
                String boxCode1 = boxList.get(j).get("BOX_CODE_2").toString().substring(0, boxList.get(j).get("BOX_CODE_2").toString().length() - 1);
                List<Map<String, Object>> tubeList = sampleMap.get(boxCode1);
                if (tubeList == null || tubeList.size() == 0) {
                    tubeList = createFrozenTubeForEmptyFrozenTube(boxCode1);
                    sampleMap.put(boxCode1, tubeList);
                }
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
            stockInRepository.saveAndFlush(stockIn);

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
                Equipment entity = null;
                for (Equipment e : equipments) {
                    if (e.getEquipmentCode().equals(equipmentCode)) {
                        entity = e;
                    }
                }
                if (entity == null) {
                    throw new BankServiceException("设备未导入:" + equipmentCode);
                }
                //区域
                Area area = null;
                for (Area e : areas) {
                    if (e.getEquipmentCode().equals(equipmentCode) && e.getAreaCode().equals(areaCode)) {
                        area = e;
                    }
                }
                if (area == null) {
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(0).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.saveAndFlush(area);
                }
                //冻存架
                SupportRack supportRack = null;
                for (SupportRack e : supportRacks) {
                    if (e.getArea().getId().equals(area.getId()) && e.getSupportRackCode().equals(supportCode)) {
                        supportRack = e;
                    }
                }
                if (supportRack == null) {
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                        .area(supportRackMapper.areaFromId(area.getId()));
                    supportRackRepository.saveAndFlush(supportRack);
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
                    frozenBoxRepository.saveAndFlush(frozenBox);
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
                stockInBoxRepository.saveAndFlush(stockInBox);

                //保存入库盒子位置
                StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
                stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                    .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                    .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                    .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                    .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                    .stockInBox(stockInBox);
                stockInBoxPosition.setCreatedDate(createDate);
                stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);

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
                    stockInTube.setCreatedDate(createDate);
                    stockInTubeRepository.saveAndFlush(stockInTube);
                }
            }
        }
    }

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
        for (Map<String, Object> map : tubeList) {
            Date date = null;
            try {
                date = strToDate2(map.get("OLD_DATE").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            peopleMemo = new ArrayList<>();
            String taskCode = map.get("TASK_CODE") != null && !map.get("TASK_CODE").toString().equals("NA") ? map.get("TASK_CODE").toString() : bankUtil.getUniqueIDByDate("F", date);
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
            keyList.add(taskCode);
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

            //创建出库申请
            String applyCode = bankUtil.getUniqueIDByDate("C", stockOutDate);
            StockOutApply stockOutApply = new StockOutApply()
                .status(Constants.STOCK_OUT_APPROVED).applyCode(applyCode)
                .applyDate(date).applyPersonName(null).delegate(delegate)
                .approverId(opt_user_id).approveTime(date)
                .endTime(null).startTime(null).purposeOfSample(null).recordId(opt_user_id).recordTime(date);
            stockOutApply.setCreatedDate(createDate);
            stockOutApplyRepository.saveAndFlush(stockOutApply);

            StockOutApplyProject stockOutApplyProject = new StockOutApplyProject().project(project).stockOutApply(stockOutApply).status(Constants.VALID);
            stockOutApplyProject.setCreatedDate(createDate);
            stockOutApplyProjectRepository.saveAndFlush(stockOutApplyProject);
            //创建出库需求
            String requirementCode = bankUtil.getUniqueIDByDate("D", stockOutDate);
            StockOutRequirement stockOutRequirement = new StockOutRequirement()
                .applyCode(applyCode).requirementCode(requirementCode).requirementName("出库")
                .status(Constants.STOCK_OUT_REQUIREMENT_CHECKED_PASS).stockOutApply(stockOutApply).countOfSample(tubeList.size()).countOfSampleReal(tubeList.size());
            stockOutRequirement.setCreatedDate(createDate);
            stockOutRequirementRepository.saveAndFlush(stockOutRequirement);
            //创建出库计划
            String stockOutPlanCode = bankUtil.getUniqueIDByDate("E", stockOutDate);
            StockOutPlan stockOutPlan = new StockOutPlan().stockOutPlanCode(stockOutPlanCode).stockOutApply(stockOutApply).status(Constants.STOCK_OUT_PLAN_COMPLETED)
                .applyNumber(stockOutApply.getApplyCode())
                .stockOutApply(stockOutApply);
            stockOutPlan.setCreatedDate(createDate);
            stockOutPlan = stockOutPlanRepository.saveAndFlush(stockOutPlan);
            //创建出库任务
            StockOutTask stockOutTask = new StockOutTask()
                .status(Constants.STOCK_OUT_TASK_COMPLETED)
                .stockOutTaskCode(key.get(0))
                .stockOutPlan(stockOutPlan).usedTime(0)
                .stockOutDate(date).stockOutHeadId1(opt_user_id).stockOutHeadId2(opt_user_id_2).taskStartTime(createDate).taskEndTime(createDate).memo(String.join(",", peopleMemo));
            stockOutTask.setCreatedDate(createDate);
            stockOutTaskRepository.saveAndFlush(stockOutTask);

            Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
            String boxCode = BankUtil.getUniqueCODE();
            for (Map<String, Object> map : alist) {
                remark = new ArrayList<>();
                boxCode = map.get("TEMP_BOX") != null && !map.get("TEMP_BOX").toString().equals("NA") ? map.get("TEMP_BOX").toString() : boxCode;
                String sampleCode = map.get("BOX_CODE").toString();
                String memo = map.get("MEMO") != null ? map.get("MEMO").toString() : null;
                String special = map.get("SPECIAL") != null ? map.get("SPECIAL").toString() : null;
                String result = map.get("RESULT").toString();

                if (!StringUtils.isEmpty(memo)) {
                    remark.add(memo);
                }
                if (!StringUtils.isEmpty(special)) {
                    remark.add(special);
                }

                //出库需求样本
                FrozenTube frozenTube = frozenTubeRepository.findBySampleCodeAndSampleTypeCode(sampleCode, sampleTypeCode);
                if (frozenTube == null) {
                    continue;
                }
                System.out.print(String.join(",", remark));
                StockOutReqFrozenTube stockOutReqFrozenTube = new StockOutReqFrozenTube().status(Constants.STOCK_OUT_SAMPLE_IN_USE)
                    .stockOutRequirement(stockOutRequirement).memo(String.join(",", remark))
                    .frozenBox(frozenTube.getFrozenBox())
                    .frozenTube(frozenTube)
                    .tubeColumns(frozenTube.getTubeColumns())
                    .tubeRows(frozenTube.getTubeRows());
                stockOutReqFrozenTube.setCreatedDate(createDate);
                stockOutReqFrozenTubeRepository.saveAndFlush(stockOutReqFrozenTube);
                //出库计划样本
                StockOutPlanFrozenTube planTube = new StockOutPlanFrozenTube();
                String status = Constants.STOCK_OUT_PLAN_TUBE_COMPLETED;
                if (!result.equals("正确")) {
                    status = Constants.STOCK_OUT_PLAN_TUBE_CANCEL;
                }
                planTube = planTube.status(status)
                    .stockOutPlan(stockOutPlan)
                    .stockOutReqFrozenTube(stockOutReqFrozenTube).memo(String.join(",", remark));
                planTube.setCreatedDate(createDate);
                stockOutPlanFrozenTubeRepository.saveAndFlush(planTube);

                //出库任务样本
                StockOutTaskFrozenTube stockOutTaskFrozenTube = new StockOutTaskFrozenTube();
                String planStatus = Constants.STOCK_OUT_FROZEN_TUBE_COMPLETED;
                if (!result.equals("正确")) {
                    planStatus = Constants.STOCK_OUT_FROZEN_TUBE_CANCEL;
                }
                stockOutTaskFrozenTube = stockOutTaskFrozenTube.status(planStatus).stockOutTask(stockOutTask).stockOutPlanFrozenTube(planTube).memo(String.join(",", remark));
                stockOutTaskFrozenTube.setCreatedDate(createDate);
                stockOutTaskFrozenTubeRepository.saveAndFlush(stockOutTaskFrozenTube);
                if (result.equals("正确")) {
                    map.put("TEMP_BOX", boxCode);
                    map.put("FROZEN_TUBE", frozenTube);
                    map.put("TASK_TUBE", stockOutTaskFrozenTube);
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
                    .status(Constants.FROZEN_BOX_STOCK_OUT_HANDOVER).areaCode(areaCode).area(area).equipment(equipment).equipmentCode(equipmentCode).supportRackCode(null).supportRack(null).columnsInShelf(null).rowsInShelf(null);
                frozenBoxRepository.saveAndFlush(frozenBox);
                //保存出库盒
                StockOutFrozenBox stockOutFrozenBox = new StockOutFrozenBox();
                stockOutFrozenBox.setStatus(Constants.STOCK_OUT_FROZEN_BOX_COMPLETED);
                stockOutFrozenBox.setFrozenBox(frozenBox);
                stockOutFrozenBox.setStockOutTask(stockOutTask);
                stockOutFrozenBox = stockOutFrozenBox.frozenBoxCode(frozenBox.getFrozenBoxCode()).sampleTypeCode(frozenBox.getSampleTypeCode())
                    .sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationCode() : null)
                    .sampleClassificationName(frozenBox.getSampleClassification() != null ? frozenBox.getSampleClassification().getSampleClassificationName() : null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .areaCode(areaCode).area(area).equipment(equipment).equipmentCode(equipmentCode)
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockOutFrozenBox.setCreatedDate(createDate);
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
                stockOutBoxPositionRepository.saveAndFlush(stockOutBoxPosition);
                String handOverTime = tubeListResult.get(0).get("HAND_OVER_DATE").toString();
                Date handOverDate = null;
                try {
                    handOverDate = strToDate(handOverTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                LocalDate overdate = handOverDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                ZonedDateTime overdateTime = handOverDate.toInstant().atZone(ZoneId.systemDefault());
                //创建出库交接
                String handOverCode = bankUtil.getUniqueIDByDate("G", stockOutDate);
                StockOutHandover stockOutHandover = new StockOutHandover().handoverCode(handOverCode)
                    .stockOutTask(stockOutTask)
                    .stockOutApply(stockOutTask.getStockOutPlan().getStockOutApply())
                    .stockOutPlan(stockOutTask.getStockOutPlan())
                    .status(Constants.STOCK_OUT_HANDOVER_COMPLETED).handoverPersonId(over_person_id).handoverTime(overdate);
                stockOutHandover.setCreatedDate(createDate);
                stockOutHandoverRepository.saveAndFlush(stockOutHandover);
                Map<String, String> posMap = new HashMap<>();

                for (Map<String, Object> map : tubeListResult) {
                    FrozenTube frozenTube = (FrozenTube) map.get("FROZEN_TUBE");
                    StockOutTaskFrozenTube stockOutTaskFrozenTube = (StockOutTaskFrozenTube) map.get("TASK_TUBE");
                    String posInTube = map.get("POS_IN_TUBE") != null ? map.get("POS_IN_TUBE").toString() : "NA";
                    if (map.get("SPECIAL") != null && map.get("SPECIAL").toString().contains("整盒出库")) {
                        posInTube = frozenTube.getTubeRows() + frozenTube.getTubeColumns();
                    }
                    String tubeRows = posInTube.substring(0, 1);
                    String tubeColumns = posInTube.substring(1);
                    if (!posInTube.equals("NA")) {
                        posMap.put(posInTube, frozenBox.getFrozenBoxCode());
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
                                if (posMap.get(row + column) != null && posMap.get(row + column).equals(boxCode)) {
                                    continue;
                                } else {
                                    flag = true;
                                    tubeRows = row;
                                    tubeColumns = column;
                                    posMap.put(row + column, frozenBox.getFrozenBoxCode());
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
                    StockOutBoxTube stockOutBoxTube = new StockOutBoxTube();

                    stockOutBoxTube.setStockOutFrozenBox(stockOutFrozenBox);
                    stockOutBoxTube.setStockOutTaskFrozenTube(stockOutTaskFrozenTube);
                    FrozenTube tube = stockOutTaskFrozenTube.getStockOutPlanFrozenTube().getStockOutReqFrozenTube().getFrozenTube();
                    stockOutBoxTube = stockOutBoxTube.status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tubeColumns).tubeRows(tubeRows)
                        .frozenBoxCode(tube.getFrozenBoxCode()).errorType(tube.getErrorType())
                        .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(Constants.FROZEN_BOX_STOCK_OUT_COMPLETED)
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
                    stockOutBoxTube.setCreatedDate(createDate);
                    stockOutBoxTubeRepository.saveAndFlush(stockOutBoxTube);
                    //保存交接详情
                    StockOutHandoverDetails stockOutHandoverDetails = new StockOutHandoverDetails();
                    stockOutHandoverDetails = stockOutHandoverDetails.status(Constants.STOCK_OUT_HANDOVER_COMPLETED);
//                        .stockOutBoxTube(stockOutBoxTube)
//                        .stockOutHandover(stockOutHandover);
                    stockOutHandoverDetails.setCreatedDate(overdateTime);
                    stockOutHandoverDetailsRepository.saveAndFlush(stockOutHandoverDetails);
                }
            }
        }
    }

    @Test
    public void importBoxRecordForPeace2_A() {
        //从视图中查询所有的操作记录
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from A_RECORD";// 预编译语句
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
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        Map<String, List<Map<String, Object>>> outTubeMap = new HashMap<>();
        Map<String, List<Map<String, Object>>> inBoxMap = new HashMap<>();
        for (Map<String, Object> key : list) {
            String boxCode1 = key.get("BOX_CODE_1").toString();
            String boxCode = key.get("BOX_CODE").toString();
            if (key.get("TABLENAME").toString().equals("出库")) {
                List<Map<String, Object>> oldOutList = outTubeMap.get(boxCode1);
                List<Map<String, Object>> newOutList = new ArrayList<>();
                if (oldOutList == null || oldOutList.size() == 0) {
                    newOutList.add(key);
                    outTubeMap.put(boxCode1, newOutList);
                } else {
                    oldOutList.add(key);
                    outTubeMap.put(boxCode1, oldOutList);
                }
            }
            if (key.get("TABLENAME").toString().equals("移位入库")) {
                List<Map<String, Object>> oldOutList = inBoxMap.get(boxCode);
                List<Map<String, Object>> newOutList = new ArrayList<>();
                if (oldOutList == null || oldOutList.size() == 0) {
                    newOutList.add(key);
                    inBoxMap.put(boxCode, newOutList);
                } else {
                    newOutList = oldOutList;
                    newOutList.add(key);
                    inBoxMap.put(boxCode, newOutList);
                }
            }
            List<Map<String, Object>> oldList = map.get(boxCode1);
            List<Map<String, Object>> newList = new ArrayList<>();
            if (oldList == null || oldList.size() == 0) {
                newList.add(key);
                map.put(boxCode1, newList);
            } else {
                newList = oldList;
                newList.add(key);
                Collections.sort(newList, new Comparator<Map<String, Object>>() {
                    @Override
                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                        try {
                            Date dt1 = format.parse(o1.get("OPT_DATE").toString());
                            Date dt2 = format.parse(o2.get("OPT_DATE").toString());
                            if (dt1.getTime() > dt2.getTime()) {
                                return 1;
                            } else if (dt1.getTime() < dt2.getTime()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return 0;
                    }
                });
                map.put(boxCode1, newList);
            }
        }
        List<String> outTubeCode = new ArrayList<>();
        List<String> boxCodeList = new ArrayList<>();
        for (String key : map.keySet()) {
            List<Map<String, Object>> boxList = map.get(key);
            for (Map<String, Object> box : boxList) {
                if (box.get("TABLENAME").toString().equals("移位")) {
//                    this.importMoveBoxForPeace2(box);
                } else if (box.get("TABLENAME").toString().equals("出库")) {
                    if (outTubeCode.contains(box.get("BOX_CODE_1").toString())) {
                        continue;
                    }
                    List<Map<String, Object>> tubeList = outTubeMap.get(key);
                    for (Map<String, Object> tube : tubeList) {
                        outTubeCode.add(tube.get("BOX_CODE_1").toString());
                    }
                    this.importBoxOutForPeace2(tubeList, "A");
                } else if (box.get("TABLENAME").toString().equals("移位入库")) {
                    if (boxCodeList.contains(box.get("BOX_CODE").toString())) {
                        continue;
                    }
                    List<Map<String, Object>> inBoxList = inBoxMap.get(box.get("BOX_CODE"));
                    for (Map<String, Object> tube : inBoxList) {
                        boxCodeList.add(tube.get("BOX_CODE").toString());
                    }
                    this.importBoxForReInStock(inBoxList, "A", Constants.STORANGE_IN_TYPE_MOVE);
                }
            }
        }
    }

    private void importBoxForReInStock(List<Map<String, Object>> inBoxList, String sampleTypeCode, String stockInType) {
        String equipmentCode = inBoxList.get(0).get("EQUIPMENT").toString();
        String areaCode = inBoxList.get(0).get("AREA") != null && !inBoxList.get(0).get("AREA").toString().equals("NA") ? inBoxList.get(0).get("AREA").toString() : "S99";
        String supportCode = inBoxList.get(0).get("SHELF") != null && !inBoxList.get(0).get("SHELF").toString().equals("NA") ? inBoxList.get(0).get("SHELF").toString() : "R99";
        String posInShelf = inBoxList.get(0).get("POS").toString();
        String columnsInShelf = posInShelf.substring(0, 1);
        String rowsInShelf = posInShelf.substring(1);
        String boxCode = inBoxList.get(0).get("BOX_CODE").toString();
//        String optDate = inBoxList.get(0).get("OPT_DATE").toString();
        String optDate1 = inBoxList.get(0).get("OLD_DATE").toString();
        String optPerson1 = inBoxList.get(0).get("OPT_PERSON_1") != null && !inBoxList.get(0).get("OPT_PERSON_1").toString().equals("NA") ? inBoxList.get(0).get("OPT_PERSON_1").toString() : "NA";
        String optPerson2 = inBoxList.get(0).get("OPT_PERSON_2") != null && !inBoxList.get(0).get("OPT_PERSON_2").toString().equals("NA") ? inBoxList.get(0).get("OPT_PERSON_2").toString() : "NA";
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
            .countOfSample(inBoxList.size()).stockInCode(stockInCode).project(project).projectSite(null)
            .memo("PEACE2 项目" + LocalDate.now() + "数据导入")
            .status(Constants.STOCK_IN_COMPLETE);
        stockIn.setCreatedDate(createDate);
        stockInRepository.saveAndFlush(stockIn);
        //设备
        Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
        if (entity == null) {
            throw new BankServiceException("设备未导入:" + equipmentCode);
        }
        //区域
        Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode, entity.getId());
        if (area == null) {
            throw new BankServiceException("区域未导入:" + equipmentCode + "." + areaCode);
        }
        //冻存架
        SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(), supportCode);
        if (supportRack == null) {
            throw new BankServiceException("冻存架未导入:" + equipmentCode + "." + areaCode + "." + supportCode);
        }
        FrozenBox frozenBox = new FrozenBox();

        if (stockInType.equals(Constants.STORANGE_IN_TYPE_MOVE)) {
            FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
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
            frozenBox.setCreatedDate(createDate);
        } else {
            String sampleCode = inBoxList.get(0).get("BOX_CODE_1").toString();
            StockInTube stockInTube = stockInTubeRepository.findBySampleCodeLast(sampleCode, sampleTypeCode);
            String frozenBoxCode = stockInTube.getFrozenBoxCode();
            frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(frozenBoxCode);
            frozenBox = frozenBox.equipment(entity).area(area).equipmentCode(entity.getEquipmentCode()).areaCode(areaCode).supportRackCode(supportCode)
                .supportRack(supportRack)
                .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf);
        }
        frozenBoxRepository.saveAndFlush(frozenBox);
        //保存入库盒
        StockInBox stockInBox = new StockInBox()
            .equipmentCode(equipmentCode)
            .areaCode(areaCode)
            .supportRackCode(supportCode)
            .rowsInShelf(rowsInShelf)
            .columnsInShelf(columnsInShelf)
            .status(Constants.FROZEN_BOX_STOCKED).countOfSample(inBoxList.size())
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
        stockInBoxRepository.saveAndFlush(stockInBox);
        //保存入库盒子位置
        StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
        stockInBoxPosition = stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
            .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
            .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
            .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
            .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
            .stockInBox(stockInBox);
        stockInBoxPosition.setCreatedDate(createDate);
        stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);
        //保存盒子以及盒内样本
        for (int j = 0; j < inBoxList.size(); j++) {
            String sampleCode = inBoxList.get(j).get("BOX_CODE_1").toString();
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
                String posInTube = inBoxList.get(0).get("POS_IN_TUBE").toString();
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
        }
    }

    //问题冻存盒导入首次入库
    @Test
    public void importSpecialBoxForPeace2() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("and ( box_code_2 like '%150220115%' or box_code like '%150220115%'");
        buffer.append(" or box_code_2 like '%220120044%' or box_code like '%220120044%'");
        buffer.append(" or  box_code_2 like '%320320073%' or box_code like '%320320073%'");
        buffer.append(" or  box_code_2 like '%220320009%' or box_code like '%220320009%'");
        buffer.append(" or  box_code_2 like '%510420126%' or box_code like '%510420126%'");
        buffer.append(" or  box_code_2 like '%530120022%' or box_code like '%530120022%'");
        buffer.append(" or  box_code_2 like '%360520047%' or box_code like '%360520047%'");
        buffer.append(" or  box_code_2 like '%420420001%' or box_code like '%420420001%'");
        buffer.append(" or  box_code_2 like '%420120071%' or box_code like '%420120071%'");
        buffer.append(" or  box_code_2 like '%370620047%' or box_code like '%370620047%')");
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        if (frozenTubeType == null) {
            throw new BankServiceException("冻存管类型导入失败！");
        }
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            StringBuffer buffer1 = new StringBuffer();
            buffer1.append("and ( box_code like '%150220115%'");
            buffer1.append("or box_code like '%220120044%'");
            buffer1.append("or box_code like '%320320073%'");
            buffer1.append("or box_code like '%220320009%'");
            buffer1.append("or box_code like '%510420126%'");
            buffer1.append("or box_code like '%530120022%'");
            buffer1.append("or box_code like '%360520047%'");
            buffer1.append("or box_code like '%420420001%'");
            buffer1.append("or box_code like '%420120071%'");
            buffer1.append("or box_code like '%370620047%')");

            String sql = "select * from SAMPLE_0029 WHERE 1=1  ";// 预编译语句
            sql += buffer1.toString();
            pre = con.prepareStatement(sql);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetas = result.getMetaData();
            Map<String, Object> sampleMap = null;
            while (result.next()) {
                sampleMap = this.Result2Map(result, rsMetas);
                list.add(sampleMap);
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
        Map<String, List<Map<String, Object>>> sampleMap = new HashMap<>();
        List<String> sampleCodes = new ArrayList<>();
        for (Map<String, Object> sample : list) {
            if (sample.get("BOX_CODE") == null) {
                throw new BankServiceException("冻存盒编码不存在", sample.toString());
            }
            String key = sample.get("BOX_CODE").toString();
            String sampleCode = sample.get("TUBE_CODE").toString();
            if (sampleCodes.contains(sampleCode)) {
                continue;
            }
            sampleCodes.add(sampleCode);
            List<Map<String, Object>> alist = new ArrayList<>();
            if (sampleMap.get(key) == null || sampleMap.get(key).size() == 0) {
                alist.add(sample);
                sampleMap.put(key, alist);
            } else {
                alist = sampleMap.get(key);
                alist.add(sample);
                sampleMap.put(key, alist);
            }
        }
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        List<ProjectSite> projectSiteList = projectSiteRepository.findAll();
        List<Equipment> equipments = equipmentRepository.findAll();
        List<Area> areas = areaRepository.findAll();
        List<SupportRack> supportRacks = supportRackRepository.findAll();
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode("A");
        Project project = projectRepository.findByProjectCode("0029");
        importBoxForPeace2("HE_A_0908", "A", frozenTubeType, frozenBoxType, sampleMap, buffer.toString(), projectSiteList, equipments, areas
            , supportRacks, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("R");
        importBoxForPeace2("HE_R_0908", "R", frozenTubeType, frozenBoxType, sampleMap, buffer.toString(), projectSiteList, equipments, areas, supportRacks, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("E");
        importBoxForPeace2("HE_E_0908", "E", frozenTubeType, frozenBoxType, sampleMap, buffer.toString(), projectSiteList, equipments, areas, supportRacks, sampleType, project);
        sampleType = sampleTypeRepository.findBySampleTypeCode("W");
        importBoxForPeace2("HE_W_0908", "W", frozenTubeType, frozenBoxType, sampleMap, buffer.toString(), projectSiteList, equipments, areas, supportRacks, sampleType, project);
    }

    //问题冻存盒导入操作记录
    @Test
    public void importSpecialBoxOptForPeace2() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("and ( a.box_code_1 like '%150220115%' or a.box_code like '%150220115%'");
        buffer.append(" or a.box_code_1 like '%220120044%' or a.box_code like '%220120044%'");
        buffer.append(" or  a.box_code_1 like '%320320073%' or a.box_code like '%320320073%'");
        buffer.append(" or  a.box_code_1 like '%220320009%' or a.box_code like '%220320009%'");
        buffer.append(" or  a.box_code_1 like '%510420126%' or a.box_code like '%510420126%'");
        buffer.append(" or  a.box_code_1 like '%530120022%' or a.box_code like '%530120022%'");
        buffer.append(" or  a.box_code_1 like '%360520047%' or a.box_code like '%360520047%'");
        buffer.append(" or  a.box_code_1 like '%420420001%' or a.box_code like '%420420001%'");
        buffer.append(" or  a.box_code_1 like '%420120071%' or a.box_code like '%420120071%'");
        buffer.append(" or  a.box_code_1 like '%370620047%' or a.box_code like '%370620047%')");

        importSpecialBoxRecordForPeace2("A_RECORD", "A", buffer.toString());
        importSpecialBoxRecordForPeace2("E_RECORD", "E", buffer.toString());
        importSpecialBoxRecordForPeace2("W_RECORD", "W", buffer.toString());
        importSpecialBoxRecordForPeace2("R_RECORD", "R", buffer.toString());
    }

    private void importSpecialBoxRecordForPeace2(String tableName, String type, String sql) {
        //从视图中查询所有的操作记录
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from " + tableName + " a where 1=1 " + sql + " order by a.OPT_YEAR, a.OLD_DATE, a.OPT_PERSON_1, a.OPT_PERSON_2, a.BOX_CODE_1, a.BOX_CODE";// 预编译语句
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


        Map<String, List<Map<String, Object>>> listGroupByDateAndOptionType =
            list.stream().collect(Collectors.groupingBy(w -> w.get("OPT_DATE").toString() + "&" + w.get("TABLENAME").toString()));

        for (String optionType : listGroupByDateAndOptionType.keySet()) {
            List<Map<String, Object>> opts1 = listGroupByDateAndOptionType.get(optionType);
            Map<String, List<Map<String, Object>>> listGroupByTask = null;
            optionType = optionType.split("&")[1];
            switch (optionType) {
                case "移位":
                    // 不分组直接导入
                    opts1.forEach(o -> this.importMoveBoxForPeace2(o, type));
                    break;
                case "销毁":
                    // 不分组直接导入
                    opts1.forEach(o -> this.importDestroyRecord(o));
                    break;
                case "出库":
                case "出库2":
                    // 根据临时盒编码分组, 为空时任务编码分组, 为空时操作员分组, 为空时一维冻存盒编码进行的分组
                    listGroupByTask =
                        opts1.stream().collect(Collectors.groupingBy(w -> {
                            Object oprator1 = Optional.ofNullable(w.get("OPT_PERSON_1")).orElse("");
                            Object oprator2 = Optional.ofNullable(w.get("OPT_PERSON_2")).orElse("");
                            String oprators = oprator1.toString() + oprator2.toString();
                            Object taskKey = Optional.ofNullable(w.get("TEMP_BOX")).orElse(w.get("TASK_CODE"));
                            if (oprators.length() > 0) {
                                taskKey = Optional.ofNullable(taskKey).orElse(oprators);
                            } else {
                                taskKey = Optional.ofNullable(taskKey).orElse(w.get("BOX_CODE_1"));
                            }
                            return taskKey.toString();
                        }));

                    for (String task : listGroupByTask.keySet()) {
                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
                        this.importBoxOutForPeace2(opts2, type);
                    }
                    break;
                case "移位入库":
                    // 根据二维冻存盒编码进行的分组
                    listGroupByTask =
                        opts1.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));

                    for (String task : listGroupByTask.keySet()) {
                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
                        this.importBoxForReInStock(opts2, type, Constants.STORANGE_IN_TYPE_MOVE);
                    }
                    break;
                case "复位":
                    // 根据二维冻存盒编码进行的分组
                    listGroupByTask =
                        opts1.stream().collect(Collectors.groupingBy(w -> w.get("BOX_CODE").toString()));

                    for (String task : listGroupByTask.keySet()) {
                        List<Map<String, Object>> opts2 = listGroupByTask.get(task);
                        this.importBoxForReInStock(opts2, type, Constants.STORANGE_IN_TYPE_REVERT);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Test
    public void importFor0037() throws Exception {
        FrozenTubeType dcgTube = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        this.createFrozenBoxFor0037("HE_COL_01", "A", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_02", "A", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_03", "W", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_04", "R", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_05", "A", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_06", "A", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_07", "F", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_08", "F", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_09", "E", dcgTube);
        this.createFrozenBoxFor0037("HE_COL_10", "E", dcgTube);
//        this.createFrozenBoxForA02("HE_COL_11_RNA","RNA",rnaTube);
    }

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

    private void importStockInFor0037Move(List<Map<String, Object>> boxList, String key) {
        String optDate = key.split("&")[0];//操作日期
        String boxCode = key.split("&")[1];//冻存盒编码
        Date stockInDate = null;
        try {
            stockInDate = strToDate(optDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.print("导入盒:" + boxCode);

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
        positionMove.setCreatedDate(createDate);
        positionMoveRepository.saveAndFlush(positionMove);

        //移位入库详情
        //先查询到盒子的首次入库记录

        String equipmentCode = map.get("EQ_CODE").toString().trim();
        String areaCode = map.get("AREA_CODE") != null && !map.get("AREA_CODE").toString().equals("NA") ? map.get("AREA_CODE").toString().trim() : "S99";

        String supportCode = map.get("SHELF_CDOE") != null && !map.get("SHELF_CDOE").toString().equals(null) && !map.get("SHELF_CDOE").toString().equals("NA")
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
            supportRackRepository.saveAndFlush(supportRack);
        }
        String posInShelf = map.get("POS_IN_SHELF") != null ? map.get("POS_IN_SHELF").toString() : null;
        if (posInShelf == null || posInShelf.equals("NA")) {
            Long count = frozenBoxRepository.countByEquipmentCodeAndAreaCodeAndSupportRackCode(
                equipmentCode, areaCode, supportCode);
            posInShelf = "A" + (count.intValue() + 1);
        }

        String columnsInShelf = posInShelf != null ? posInShelf.substring(0, 1) : null;
        String rowsInShelf = posInShelf != null ? posInShelf.substring(1) : null;
        frozenBox = frozenBox.equipment(supportRack.getArea().getEquipment()).equipmentCode(supportRack.getArea().getEquipmentCode())
            .area(supportRack.getArea())
            .areaCode(supportRack.getArea().getAreaCode())
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
        frozenBoxRepository.saveAndFlush(frozenBox);
        List<FrozenTube> frozenTubeList = frozenTubeRepository.findFrozenTubeListByBoxId(frozenBox.getId());
        saveMoveDetail(positionMove, Constants.MOVE_TYPE_FOR_BOX, frozenTubeList);

    }

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
            stockInRepository.saveAndFlush(stockIn);
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
            frozenBoxRepository.saveAndFlush(frozenBox);
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
                stockInBoxRepository.saveAndFlush(stockInBox);
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
                stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);
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
            }
        }
    }

    @Test
    private void importCheckType(){
        CheckType checkType1 = new CheckType().checkTypeCode("SH").checkTypeName("生化").status(Constants.VALID);
        CheckType checkType2 = new CheckType().checkTypeCode("TH").checkTypeName("糖化血红蛋白").status(Constants.VALID);
        CheckType checkType3 = new CheckType().checkTypeCode("MY").checkTypeName("免疫").status(Constants.VALID);
        CheckType checkType4 = new CheckType().checkTypeCode("DNA").checkTypeName("DNA提取").status(Constants.VALID);
        CheckType checkType5 = new CheckType().checkTypeCode("XP").checkTypeName("芯片检测").status(Constants.VALID);
    }
}

