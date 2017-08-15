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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
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
    FrozenBoxPositionMapper frozenBoxPositionMapper;
    @Autowired
    FrozenBoxPositionRepository frozenBoxPositionRepository;
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

private final Logger log = LoggerFactory.getLogger(ImportSampleTest.class);
    /**
     * 创建项目
     * @throws Exception
     */
    @Test
    public void createProject() throws Exception {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try{
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from PROJECT";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()){
                map = this.Result2Map(result,rsMeta);
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        for(int i = 0 ;i<list.size();i++) {
            String projectCode = list.get(i).get("PROJECT_CODE").toString().trim();
            String projectName = list.get(i).get("PROJECT_NAME").toString().trim();
            String delegateName = list.get(i).get("DELEGATE").toString().trim();
            Delegate delegate = delegateRepository.findByDelegateName(delegateName);
            if(delegate == null){
                delegate = new Delegate().delegateCode("D_0000"+(i+1)).delegateName(delegateName).status("0001");
                delegateRepository.saveAndFlush(delegate);
            }
            Project project = projectRepository.findByProjectCode(projectCode);
            if(project == null){
                project = new Project().projectCode(projectCode).projectName(projectName).status("0001").delegate(delegate);
                projectRepository.saveAndFlush(project);
            }
        }
    }
    /**
     * 创建项目点
     * @throws Exception
     */
    /**
     * 创建项目点
     * @throws Exception
     */
    @Test
    public void createProjectSite() throws Exception {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Long projectId = null;
        try{
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from LCC";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()){
                map = this.Result2Map(result,rsMeta);
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Project project = projectRepository.findByProjectCode("0037");
        for(int i = 0 ;i<list.size();i++) {
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
    public void createProjectSiteForPeace3() {
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listAll = new ArrayList<Map<String, Object>>();
        Long projectId = null;
        try{
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from LCC_2";// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()){
                map = this.Result2Map(result,rsMeta);
                list.add(map);
            }
            String sqlForSelectAllLcc = "select * from LCC_1";// 预编译语句
            PreparedStatement pres = con.prepareStatement(sqlForSelectAllLcc);// 实例化预编译语句
            ResultSet result2 = pres.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMetas2 = result2.getMetaData();
            Map<String, Object> mapForAll = null;
            while (result2.next()){
                mapForAll = this.Result2Map(result2,rsMetas2);
                listAll.add(mapForAll);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Project project = projectRepository.findByProjectCode("0038");
        for(int i = 0 ;i<list.size();i++) {
            Object projectSiteCodeObj = list.get(i).get("LCC_ID");
            if(projectSiteCodeObj == null){
                continue;
            }
            String projectSiteCode = list.get(i).get("LCC_ID").toString();
            Object names = list.get(i).get("NAME");
            if(names == null){
                continue;
            }
            String name = list.get(i).get("NAME").toString();
            if(projectSiteCode.length()<4){
                throw new BankServiceException("项目点异常:"+projectSiteCode);
            }
            String projectSiteId =  projectSiteCode.substring(0,4);
            String area = null;
            for(int j = 0 ;j<listAll.size();j++){
                String lcc = listAll.get(j).get("LCC_ID").toString();
                if(projectSiteId .equals( lcc)){
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
     * @throws Exception
     */
    @Test
    public void createSupportRackType() throws Exception {
        //冻存架类型
        SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("S5x5");
        if(supportRackType==null){
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
        if(supportRackType55==null){
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
        if(supportRackType1==null){
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
        if(supportRackType2==null){
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
     * @throws Exception
     */
    @Test
    public void createFrozenTubeType() throws Exception {
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("DCG");
        if(frozenTubeType == null){
            frozenTubeType = new FrozenTubeType().frozenTubeTypeCode("DCG").frozenTubeTypeName("冻存管(1ml)").sampleUsedTimesMost(10).frozenTubeVolumn(2.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType);
            assertThat(frozenTubeType).isNotNull();
        }
        FrozenTubeType frozenTubeType1 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("2DDCG");
        if(frozenTubeType1 == null){
            frozenTubeType1 = new FrozenTubeType().frozenTubeTypeCode("2DDCG").frozenTubeTypeName("2D冻存管(0.5ml)").sampleUsedTimesMost(10).frozenTubeVolumn(0.5).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType1);
            assertThat(frozenTubeType1).isNotNull();
        }
        FrozenTubeType frozenTubeType2 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("6CXG");
        if(frozenTubeType2 == null){
            frozenTubeType2 = new FrozenTubeType().frozenTubeTypeCode("6CXG").frozenTubeTypeName("采血管(6ml)").sampleUsedTimesMost(10).frozenTubeVolumn(6.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType2);
            assertThat(frozenTubeType2).isNotNull();
        }
        FrozenTubeType frozenTubeType3 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("RNA");
        if(frozenTubeType3 == null){
            frozenTubeType3 = new FrozenTubeType().frozenTubeTypeCode("RNA").frozenTubeTypeName("RNA管(9ml)").sampleUsedTimesMost(10).frozenTubeVolumn(9.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType3);
            assertThat(frozenTubeType3).isNotNull();
        }
        FrozenTubeType frozenTubeType4 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("9CXG");
        if(frozenTubeType4 == null){
            frozenTubeType4 = new FrozenTubeType().frozenTubeTypeCode("9CXG").frozenTubeTypeName("采血管(9ml)").sampleUsedTimesMost(10).frozenTubeVolumn(9.0).frozenTubeVolumnUnit("ml").status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType4);
            assertThat(frozenTubeType4).isNotNull();
        }
    }

    /**
     * 创建设备组--固定值
     * 2017年6月29日09:44:50
     * @throws Exception
     */
    @Test
    public void createEquipmentGroup() throws Exception {
        //创建设备组
        EquipmentGroup equipmentGroup = equipmentGroupRepository.findByEquipmentGroupName("样本库");
        if(equipmentGroup == null){
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
     * @throws Exception
     */
    @Test
    public void createEquipmentModel() throws Exception {
        EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("FOMA907");
        if(equipmentModle == null){
            equipmentModle = new EquipmentModle().equipmentModelCode("FOMA907")
                .equipmentModelName("FOMA907")
                .equipmentType("Freezer")
                .areaNumber(4)
                .shelveNumberInArea(0)
                .status("0001");
            equipmentModleRepository.saveAndFlush(equipmentModle);
        }
        EquipmentModle equipmentModle1 = equipmentModleRepository.findByEquipmentModelCode("ColdRoom-3");
        if(equipmentModle1 == null){
            equipmentModle1 = new EquipmentModle().equipmentModelCode("ColdRoom-3")
                .equipmentModelName("ColdRoom-3")
                .equipmentType("coldroom")
                .areaNumber(117)
                .shelveNumberInArea(0)
                .status("0001");
            equipmentModleRepository.saveAndFlush(equipmentModle1);
        }
        EquipmentModle equipmentModle2= equipmentModleRepository.findByEquipmentModelCode("Haier-B");
        if(equipmentModle2 == null){
            equipmentModle2 = new EquipmentModle().equipmentModelCode("Haier-B")
                .equipmentModelName("Haier-B")
                .equipmentType("Freezer")
                .areaNumber(4)
                .shelveNumberInArea(0)
                .status("0001");
            equipmentModleRepository.saveAndFlush(equipmentModle2);
        }
    }

    private void createEquipment() {
        String[] equipmentCodeStr = new String[]{"F1-22","F1-23","F1-24"};
        for(String equipmentCode:equipmentCodeStr){
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("Haier-B");
            if(equipmentModle == null){
                throw new BankServiceException("设备型号导入失败");
            }
            Integer temperature =-80;
            if(entity==null){
                entity = new Equipment().equipmentAddress("样本中心D座").equipmentCode(equipmentCode)
                    .equipmentGroup(equipmentMapper.equipmentGroupFromId(1L))
                    .equipmentModle(equipmentModle).temperature(temperature).ampoulesMax(0).ampoulesMin(0).status("0001");
            }
            //设备
            equipmentRepository.saveAndFlush(entity);

            String[] areaCodeStr = new String[]{"S01","S02","S03","S04"};
            for(String areaCode :areaCodeStr){
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode,entity.getId());
                if(area==null){
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(5).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.saveAndFlush(area);
                }
                String[] supportCodeStr = new String[]{"R01","R02","R03","R04","R05"};
                for(String supportCode :supportCodeStr){
                    //冻存架
                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),supportCode);
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("S5x4");
                    if(supportRack==null){
                        supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                            .area(supportRackMapper.areaFromId(area.getId()));
                        supportRackRepository.saveAndFlush(supportRack);
                        assertThat(supportRack).isNotNull();
                    }
                }
            }
        }
    }

    private void createEquipmentForFOMA907() {
        String[] equipmentCodeStr = new String[]{"F1-03","F1-04"};
        for(String equipmentCode:equipmentCodeStr){
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("FOMA907");
            if(equipmentModle == null){
                throw new BankServiceException("设备型号导入失败");
            }
            Integer temperature =-80;
            if(entity==null){
                entity = new Equipment().equipmentAddress("样本中心D座").equipmentCode(equipmentCode)
                    .equipmentGroup(equipmentMapper.equipmentGroupFromId(1L))
                    .equipmentModle(equipmentModle).temperature(temperature).ampoulesMax(0).ampoulesMin(0).status("0001");
            }
            //设备
            equipmentRepository.saveAndFlush(entity);

            String[] areaCodeStr = new String[]{"S01","S02","S03","S04"};
            for(String areaCode :areaCodeStr){
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode,entity.getId());
                if(area==null){
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(6).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.saveAndFlush(area);
                }
                String[] supportCodeStr = new String[]{"R01","R02","R03","R04","R05","R06"};
                for(String supportCode :supportCodeStr){
                    //冻存架
                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),supportCode);
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    if(supportRack==null){
                        supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                            .area(supportRackMapper.areaFromId(area.getId()));
                        supportRackRepository.saveAndFlush(supportRack);
                    }
                }
            }
        }
    }

    private void createEquipmentForColdRoom3() {
        String[] equipmentCodeStr = new String[]{"R4-03"};
        for(String equipmentCode:equipmentCodeStr){
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("ColdRoom-3");
            if(equipmentModle == null){
                throw new BankServiceException("设备型号导入失败");
            }
            Integer temperature =-40;
            if(entity==null){
                entity = new Equipment().equipmentAddress("样本中心D座").equipmentCode(equipmentCode)
                    .equipmentGroup(equipmentMapper.equipmentGroupFromId(1L))
                    .equipmentModle(equipmentModle).temperature(temperature).ampoulesMax(0).ampoulesMin(0).status("0001");
            }
            //设备
            equipmentRepository.saveAndFlush(entity);

            String[] areaCodeStr = new String[]{"S022","S202"};
            for(String areaCode :areaCodeStr){
                int freezeFrameNumber = areaCode.equals("S022")?13:10;
                //区域
                Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode,entity.getId());
                if(area==null){
                    area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(freezeFrameNumber).equipmentCode(equipmentCode)
                        .status("0001");
                    areaRepository.saveAndFlush(area);
                }
                String[] supportCodeStr = new String[]{};
                if(areaCode.equals("S022")){
                    supportCodeStr = new String[]{"R01","R02","R03","R04","R05","R06","R07","R08","R09","R10","R11","R12","R13"};
                }else{
                    supportCodeStr = new String[]{"R01","R02","R03","R04","R05","R06","R07","R08","R09","R10"};
                }
                for(String supportCode :supportCodeStr){
                    //冻存架
                    SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),supportCode);
                    SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("B5x5");
                    if(supportRack==null){
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
     * @throws Exception
     */
    @Test
    public void createFrozenBoxType() throws Exception {
        //冻存盒类型
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DCH");
        if(frozenBoxType == null){
            frozenBoxType = new FrozenBoxType().frozenBoxTypeCode("DCH")
                .frozenBoxTypeName("冻存盒(10*10)").frozenBoxTypeColumns("10").frozenBoxTypeRows("10").status("0001");
            frozenBoxType.setCreatedBy("admin");
            frozenBoxTypeRepository.saveAndFlush(frozenBoxType);
        }
        //冻存盒类型
        FrozenBoxType frozenBoxType1 = frozenBoxTypeRepository.findByFrozenBoxTypeCode("DJH");
        if(frozenBoxType1 == null){
            frozenBoxType1 = new FrozenBoxType().frozenBoxTypeCode("DJH")
                .frozenBoxTypeName("大橘盒(10*10)").frozenBoxTypeColumns("10").frozenBoxTypeRows("10").status("DJH");
            frozenBoxTypeRepository.saveAndFlush(frozenBoxType1);
        }
        //冻存盒类型
        FrozenBoxType frozenBoxType2 = frozenBoxTypeRepository.findByFrozenBoxTypeCode("96KB");
        if(frozenBoxType2 == null){
            frozenBoxType2 = new FrozenBoxType().frozenBoxTypeCode("96KB")
                .frozenBoxTypeName("96孔板(8*12)").frozenBoxTypeColumns("12").frozenBoxTypeRows("8").status("0001");
            frozenBoxTypeRepository.saveAndFlush(frozenBoxType2);
        }
    }

    /**
     * 创建样本类型
     * @throws Exception
     */
    @Test
    public void createSampleType() throws Exception {

        SampleType sampleType1 = sampleTypeRepository.findBySampleTypeCode("A");
        if(sampleType1 == null){
            sampleType1 = new SampleType().sampleTypeCode("A").sampleTypeName("血浆")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(240,224,255)");
            sampleTypeRepository.saveAndFlush(sampleType1);
            assertThat(sampleType1).isNotNull();
        }
        SampleType sampleType2 = sampleTypeRepository.findBySampleTypeCode("W");
        if(sampleType2 == null){
            sampleType2 = new SampleType().sampleTypeCode("W").sampleTypeName("白细胞")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,255,255)");
            sampleTypeRepository.saveAndFlush(sampleType2);
            assertThat(sampleType2).isNotNull();
        }
        SampleType sampleType3 = sampleTypeRepository.findBySampleTypeCode("F");
        if(sampleType3 == null){
            sampleType3 = new SampleType().sampleTypeCode("F").sampleTypeName("血清")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,179,179)");
            sampleTypeRepository.saveAndFlush(sampleType3);
            assertThat(sampleType3).isNotNull();
        }
        SampleType sampleType4 = sampleTypeRepository.findBySampleTypeCode("E");
        if(sampleType4 == null){
            sampleType4 = new SampleType().sampleTypeCode("E").sampleTypeName("尿")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,255,179)");
            sampleTypeRepository.saveAndFlush(sampleType4);
            assertThat(sampleType4).isNotNull();
        }
        SampleType sampleType5 = sampleTypeRepository.findBySampleTypeCode("R");
        if(sampleType5 == null){
            sampleType5 = new SampleType().sampleTypeCode("R").sampleTypeName("红细胞")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(236,236,236)");
            sampleTypeRepository.saveAndFlush(sampleType5);
            assertThat(sampleType5).isNotNull();
        }
        SampleType sampleType6 = sampleTypeRepository.findBySampleTypeCode("RNA");
        if(sampleType6 == null){
            sampleType6 = new SampleType().sampleTypeCode("RNA").sampleTypeName("RNA")
                .status("0001").isMixed(0).frontColor("black").backColor("rgb(255,220,165)");
            sampleTypeRepository.saveAndFlush(sampleType6);
            assertThat(sampleType6).isNotNull();
        }
    }
    @Autowired
    private SerialNoRepository serialNoRepository;
    /**
     * 导入冻存盒
     * @throws Exception
     */
    public void createFrozenBoxForA02(String tableName,String sampleTypeCode,FrozenTubeType frozenTubeType) throws Exception {
        Project project = projectRepository.findByProjectCode("0037");
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode(sampleTypeCode);
        if(sampleType == null){
            throw new BankServiceException("样本类型为空！表名为："+tableName+"样本类型编码为："+sampleTypeCode);
        }
        if(frozenTubeType == null){
            throw new BankServiceException("冻存管类型为空！表名为："+tableName+"样本类型编码为："+sampleTypeCode);
        }
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try{
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from "+tableName;// 预编译语句
            pre = con.prepareStatement(sqlForSelect);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            ResultSetMetaData rsMeta = result.getMetaData();
            Map<String, Object> map = null;
            while (result.next()){
                map = this.Result2Map(result,rsMeta);
                list.add(map);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                DBUtilForTemp.close(con);
                System.out.println("数据库连接已关闭！");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Map<String,Equipment> equipmentCodeMap = new HashMap<String,Equipment>();
        Map<String,List<Map<String, Object>>> map = new HashMap<String,List<Map<String, Object>>>();
        for(int i = 0;i<list.size();i++){

            String boxCode = list.get(i).get("BOX_CODE").toString();
            if(map.get(boxCode)==null){
                List<Map<String, Object>> mapList = new ArrayList<>();
                mapList.add(list.get(i));
                map.put(boxCode,mapList);
            }else{
                List<Map<String, Object>> mapList = map.get(boxCode);
                mapList.add(list.get(i));
                map.put(boxCode,mapList);
            }
        }

        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode(sampleTypeCode.equals("RNA")?"DJH":"DCH");
        int m = 0;
        for(String key :map.keySet()){
            m++;
            List<Map<String, Object>> sampleList = map.get(key);
            if(sampleList.get(0).get("SAMPLE_CODE") ==null){
                throw new BankServiceException("样本编码为空！"+tableName+":盒子编码："+key,sampleList.get(0).toString());
            }
            String sampleClassTypeCode = sampleTypeCode!="RNA"?sampleList.get(0).get("SAMPLE_TYPE_CODE").toString():"11";
            String sampleClassTypeName = Constants.SAMPLE_TYPE_MAP.get(sampleClassTypeCode);
            Long LCC_ID = null;
            try{
                con = DBUtilForTemp.open();
                System.out.println("连接成功！");
                String sqlForSelectSite = "select DISTINCT LCC_ID from biobank_temp_01.fen_he_ji_lu where SAMPLE_ID = ?";
                PreparedStatement pstmt=(PreparedStatement) con.prepareStatement(sqlForSelectSite);
                pstmt.setString(1,sampleList.get(0).get("SAMPLE_CODE").toString());
                result = pstmt.executeQuery();// 执行查询，注意括号中不需要再加参数
                while (result.next()){
                    LCC_ID = result.getLong("LCC_ID");
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    DBUtilForTemp.close(con);
                    System.out.println("数据库连接已关闭！");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            ProjectSite projectSite = null;
            if(LCC_ID !=null){
                projectSite = projectSiteRepository.findByProjectSiteCode(LCC_ID.toString());
            }
            String stockInCodeNew = bankUtil.getUniqueID("B");
            StockIn stockIn = stockInRepository.findStockInByStockInCode(stockInCodeNew);
            String receiver = sampleList.get(0).get("ECEIVER")!=null?sampleList.get(0).get("ECEIVER").toString():null;
            String opt = sampleList.get(0).get("OPT")!=null?sampleList.get(0).get("OPT").toString():null;
            String storeKeeper1 = sampleList.get(0).get("USER_1")!=null?sampleList.get(0).get("USER_1").toString():null;
            String storeKeeper2 = sampleList.get(0).get("USER_2")!=null?sampleList.get(0).get("USER_2").toString():null;
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            LocalDate stockInDate  = sampleList.get(0).get("OPT_DATE")!=null&&!sampleList.get(0).get("OPT_DATE").equals("NA")?format.parse(sampleList.get(0).get("OPT_DATE").toString()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate():null;
            LocalDate receiveDate  = sampleList.get(0).get("REC_DATE")!=null&&!sampleList.get(0).get("REC_DATE").equals("NA")?format.parse(sampleList.get(0).get("REC_DATE").toString()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate():null;

            Long receiverId = null;String stockInType = null; Long storeKeeperId1 = null; Long storeKeeperId2 = null;
            if(!StringUtils.isEmpty(receiver)){
                receiverId = Constants.RECEIVER_MAP.get(receiver);
            }
            if(!StringUtils.isEmpty(opt)){
                stockInType = Constants.STOCK_IN_TYPE_MAP.get(opt);
            }
            if(!StringUtils.isEmpty(storeKeeper1)){
                storeKeeperId1 = Constants.RECEIVER_MAP.get(storeKeeper1);
            }
            if(!StringUtils.isEmpty(storeKeeper2)){
                storeKeeperId2 = Constants.RECEIVER_MAP.get(storeKeeper2);
            }
            if(stockIn == null){
                stockIn = new StockIn().projectCode("0037")
                    .projectSiteCode(LCC_ID!=null?LCC_ID.toString():null)
                    .receiveId(receiverId).receiveDate(receiveDate)
                    .receiveName(receiver)
                    .stockInType(stockInType)
                    .storeKeeperId1(storeKeeperId1)
                    .storeKeeper1(storeKeeper1)
                    .storeKeeperId2(storeKeeperId2)
                    .storeKeeper2(storeKeeper2)
                    .stockInDate(stockInDate)
                    .countOfSample(sampleList.size()).stockInCode(stockInCodeNew).project(project).projectSite(projectSite)
                    .memo("PEACE5 项目"+LocalDate.now()+"数据导入")
                    .status(Constants.STOCK_IN_COMPLETE);
                stockInRepository.saveAndFlush(stockIn);
                assertThat(stockIn).isNotNull();
            }

            if(sampleTypeCode!="RNA"&&sampleList.get(0).get("POS_IN_SHELF") == null){
                throw new BankServiceException("所在架子内位置为空！"+tableName+":盒子编码："+key);
            }

            SampleClassification sampleClassification = null;
            sampleClassification = sampleClassificationRepository.findBySampleClassificationCode(sampleClassTypeCode);
            String frontColor = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get(sampleClassTypeCode).toString();
            if(sampleClassification == null){
                sampleClassification = new SampleClassification()
                    .sampleClassificationCode(sampleClassTypeCode)
                    .sampleClassificationName(sampleClassTypeName)
                    .status("0001")
                    .backColor(frontColor)
                    .frontColor("black");
                sampleClassificationRepository.saveAndFlush(sampleClassification);
                assertThat(sampleClassification).isNotNull();

                ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(),sampleType.getId(),sampleClassification.getId());;
                String columnNumber = Constants.COLUMNNUMBER_MAP.get(sampleClassTypeCode);
                if(projectSampleClass == null){
                    projectSampleClass = new ProjectSampleClass()
                        .project(project).projectCode("0037")
                        .sampleClassification(projectSampleClassMapper.sampleClassificationFromId(sampleClassification.getId()))
                        .sampleType(projectSampleClassMapper.sampleTypeFromId(sampleType.getId()))
                        .columnsNumber("")
                        .status("0001");
                    projectSampleClassRepository.saveAndFlush(projectSampleClass);
                    assertThat(projectSampleClass).isNotNull();
                }
            }
            String equipmentCode = sampleList.get(0).get("EQ_CODE").toString().trim();
            String areaCode = sampleList.get(0).get("AREA_CODE").toString().trim();
            String supportCode ="";
            if(sampleTypeCode!="RNA"){
                supportCode = sampleList.get(0).get("SHELF_CODE").toString().trim();
            }else{
                Long count = frozenBoxRepository.countByEquipmentCodeAndAreaCode(equipmentCode,areaCode);
                if(count.intValue()<25){
                    supportCode = "R01";
                }else{
                    supportCode = "R02";
                }
            }
            //设备
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            if(entity==null){
               throw new BankServiceException("设备未导入:"+equipmentCode);
            }
            //区域
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode,entity.getId());
            if(area==null){
                throw new BankServiceException("设备未导入:"+areaCode);
            }
            //冻存架
            SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),supportCode);
            if(supportRack==null){
                throw new BankServiceException("冻存架未导入:"+supportRack);
            }
            String pos = null;
            String columns[] = new String[]{"A","B","C","D","E"};
            Boolean flag = false;
            if(sampleTypeCode.equals("RNA")){
                for(int j = 0;j<columns.length;j++){
                    for(int i=1;i<=5;i++){
                        String bb = columns[j]+i;
                        FrozenBox frozenBox = frozenBoxRepository.findByEquipmentCodeAndAreaCodeAndSupportRackCodeAndColumnsInShelfAndRowsInShelf(equipmentCode,areaCode,supportCode,columns[j],String.valueOf(i));
                        if(frozenBox != null){
                            continue;
                        }else {
                            flag = true;
                            pos=bb;
                            break;
                        }
                    }
                    if(flag){
                        break;
                    }
                }
            }
            String posInShelf = sampleTypeCode!="RNA"?sampleList.get(0).get("POS_IN_SHELF").toString():pos;
            String columnsInShelf = posInShelf!=null?posInShelf.substring(0, 1):null;
            String rowsInShelf =  posInShelf!=null?posInShelf.substring(1):null;
            //保存冻存盒
            FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(key);
            if(frozenBox == null){
                frozenBox = new FrozenBox()
                    .frozenBoxCode(key)
                    .frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode())
                    .frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows())
                    .frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                    .projectCode(project.getProjectCode())
                    .projectName(project.getProjectName())
                    .projectSiteCode(projectSite!=null?projectSite.getProjectSiteCode():null)
                    .projectSiteName(projectSite!=null?projectSite.getProjectSiteName():null)
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
            StockInBox stockInBox = stockInBoxRepository.findStockInBoxByStockInCodeAndFrozenBoxCode(stockIn.getStockInCode(),key);
            if(stockInBox == null){
                 stockInBox = new StockInBox()
                    .equipmentCode(equipmentCode)
                    .areaCode(areaCode)
                    .supportRackCode(supportCode)
                    .rowsInShelf(rowsInShelf)
                    .columnsInShelf(columnsInShelf)
                    .status(Constants.FROZEN_BOX_STOCKED).countOfSample(sampleList.size())
                    .frozenBoxCode(key).frozenBox(frozenBox).stockIn(stockIn).stockInCode(stockInCodeNew).area(area).equipment(entity).supportRack(supportRack);
                stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                    .sampleClassification(frozenBox.getSampleClassification())
                    .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
                    .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
                    .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                    .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                    .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                    .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                    .projectSiteName(frozenBox.getProjectSiteName());
                stockInBoxRepository.saveAndFlush(stockInBox);
                assertThat(stockInBox).isNotNull();
            }
            //保存入库盒子位置
            StockInBoxPosition stockInBoxPosition = new StockInBoxPosition();
            stockInBoxPosition.status(Constants.STOCK_IN_BOX_POSITION_COMPLETE).memo(stockInBox.getMemo())
                .equipment(stockInBox.getEquipment()).equipmentCode(stockInBox.getEquipmentCode())
                .area(stockInBox.getArea()).areaCode(stockInBox.getAreaCode())
                .supportRack(stockInBox.getSupportRack()).supportRackCode(stockInBox.getSupportRackCode())
                .columnsInShelf(stockInBox.getColumnsInShelf()).rowsInShelf(stockInBox.getRowsInShelf())
                .stockInBox(stockInBox);
            stockInBoxPositionRepository.saveAndFlush(stockInBoxPosition);assertThat(stockInBoxPosition).isNotNull();
            //保存入库管子
            for(int i = 0 ; i<sampleList.size();i++){
                if(sampleList.get(i).get("SAMPLE_CODE") ==null){
                    throw new BankServiceException("样本编码为空！"+tableName+":盒子编码："+key,sampleList.get(i).toString());
                }
                String sampleCode = sampleList.get(i).get("SAMPLE_CODE").toString();

                if(sampleTypeCode!="RNA"&&sampleList.get(i).get("POS_IN_BOX") ==null){
                    throw new BankServiceException("盒内位置为空！"+tableName+":盒子编码："+key+",样本编码为："+sampleCode,sampleList.get(i).toString());
                }
                String posInBox = sampleList.get(i).get("POS_IN_BOX").toString();
                String tubeRows = posInBox.substring(0, 1);
                String tubeColumns =  posInBox.substring(1);
                FrozenTube frozenTubeList = frozenTubeRepository.findBySampleCodeAndProjectCodeAndSampleTypeCodeAndSampleClassificationCode(sampleCode,"0037",sampleType.getSampleTypeCode(),sampleClassTypeCode);
                String status = sampleList.get(i).get("SAMPLE_STATUS")!=null?Constants.FROZEN_TUBE_STATUS_MAP.get(sampleList.get(i).get("SAMPLE_STATUS").toString()).toString():"3001";
                String memo = sampleList.get(i).get("MEMO")!=null?sampleList.get(i).get("MEMO").toString():null;
                String times = sampleList.get(i).get("TIMES")!=null?sampleList.get(i).get("TIMES").toString():null;
                if(frozenTubeList == null){
                    FrozenTube tube = new FrozenTube()
                        .projectCode("0037").projectSiteCode(projectSite!=null?projectSite.getProjectSiteCode():null)
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
                    if(stockInTube == null){
                        stockInTube = new StockInTube().status(tube.getStatus())
                            .memo(memo)
                            .frozenTube(tube)
                            .tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                            .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox);
                        stockInTubeRepository.saveAndFlush(stockInTube);assertThat(stockInTube).isNotNull();
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
    @Test
    public void createSampleTypeMix() throws Exception {
        Project project = projectRepository.findByProjectCode("0037");
        SampleType sampleType7 = sampleTypeRepository.findBySampleTypeCode("98");
        List<SampleClassification> sampleClassifications = sampleClassificationRepository.findAll();
        if(sampleType7 == null){
            sampleType7 = new SampleType().sampleTypeCode("98").sampleTypeName("98")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(169,241,253)");
            sampleTypeRepository.saveAndFlush(sampleType7);
            assertThat(sampleType7).isNotNull();
        }
        SampleType sampleType8 = sampleTypeRepository.findBySampleTypeCode("99");
        if(sampleType8 == null){
            sampleType8 = new SampleType().sampleTypeCode("99").sampleTypeName("99")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(169,241,253)");
            sampleTypeRepository.saveAndFlush(sampleType8);
            assertThat(sampleType8).isNotNull();
        }
        for(SampleClassification s :sampleClassifications){
            if(s.getSampleClassificationCode().equals("11")){
                continue;
            }
            ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(project.getId(),sampleType8.getId(),s.getId());;
            String columnNumber = Constants.COLUMNNUMBER_MAP.get(s.getSampleClassificationCode());
            if(projectSampleClass == null){
                projectSampleClass = new ProjectSampleClass()
                    .project(project).projectCode(project.getProjectCode())
                    .sampleClassification(s)
                    .sampleType(sampleType8)
                    .columnsNumber(columnNumber)
                    .status("0001");
                projectSampleClassRepository.saveAndFlush(projectSampleClass);
                assertThat(projectSampleClass).isNotNull();
            }
        }

        SampleType sampleType97 = sampleTypeRepository.findBySampleTypeCode("97");
        if(sampleType97 == null){
            sampleType97 = new SampleType().sampleTypeCode("97").sampleTypeName("97")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(255, 0, 0)");
            sampleTypeRepository.saveAndFlush(sampleType97);
            assertThat(sampleType97).isNotNull();
        }
    }


    @Test
    public void updateStockInBoxAndStockInTube() throws Exception {
        List<StockInBox> stockInBoxes = stockInBoxRepository.findAll();
        for(StockInBox stockInBox : stockInBoxes){
            FrozenBox frozenBox = stockInBox.getFrozenBox();
            stockInBox.sampleTypeCode(frozenBox.getSampleTypeCode()).sampleType(frozenBox.getSampleType()).sampleTypeName(frozenBox.getSampleTypeName())
                .sampleClassification(frozenBox.getSampleClassification())
                .sampleClassificationCode(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationCode():null)
                .sampleClassificationName(frozenBox.getSampleClassification()!=null?frozenBox.getSampleClassification().getSampleClassificationName():null)
                .dislocationNumber(frozenBox.getDislocationNumber()).emptyHoleNumber(frozenBox.getEmptyHoleNumber()).emptyTubeNumber(frozenBox.getEmptyTubeNumber())
                .frozenBoxType(frozenBox.getFrozenBoxType()).frozenBoxTypeCode(frozenBox.getFrozenBoxTypeCode()).frozenBoxTypeColumns(frozenBox.getFrozenBoxTypeColumns())
                .frozenBoxTypeRows(frozenBox.getFrozenBoxTypeRows()).isRealData(frozenBox.getIsRealData()).isSplit(frozenBox.getIsSplit()).project(frozenBox.getProject())
                .projectCode(frozenBox.getProjectCode()).projectName(frozenBox.getProjectName()).projectSite(frozenBox.getProjectSite()).projectSiteCode(frozenBox.getProjectSiteCode())
                .projectSiteName(frozenBox.getProjectSiteName());
            List<StockInTube> stockInTubes = stockInTubeRepository.findByStockInBoxId(stockInBox.getId());
            for(StockInTube stockInTube : stockInTubes){
                FrozenTube tube = stockInTube.getFrozenTube();
                stockInTube.status(tube.getStatus()).memo(tube.getMemo()).frozenTube(tube).tubeColumns(tube.getTubeColumns()).tubeRows(tube.getTubeRows())
                    .frozenBoxCode(tube.getFrozenBoxCode()).stockInBox(stockInBox).errorType(tube.getErrorType())
                    .frozenTubeCode(tube.getFrozenTubeCode()).frozenTubeState(tube.getFrozenTubeState())
                    .frozenTubeType(tube.getFrozenTubeType()).frozenTubeTypeCode(tube.getFrozenTubeTypeCode())
                    .frozenTubeTypeName(tube.getFrozenTubeTypeName()).frozenTubeVolumns(tube.getFrozenTubeVolumns())
                    .frozenTubeVolumnsUnit(tube.getFrozenTubeVolumnsUnit()).sampleVolumns(tube.getSampleVolumns())
                    .project(tube.getProject()).projectCode(tube.getProjectCode()).projectSite(tube.getProjectSite())
                    .projectSiteCode(tube.getProjectSiteCode()).sampleClassification(tube.getSampleClassification())
                    .sampleClassificationCode(tube.getSampleClassification()!=null?tube.getSampleClassification().getSampleClassificationCode():null)
                    .sampleClassificationName(tube.getSampleClassification()!=null?tube.getSampleClassification().getSampleClassificationName():null)
                    .sampleCode(tube.getSampleCode()).sampleTempCode(tube.getSampleTempCode()).sampleType(tube.getSampleType())
                    .sampleTypeCode(tube.getSampleTypeCode()).sampleTypeName(tube.getSampleTypeName()).sampleUsedTimes(tube.getSampleUsedTimes())
                    .sampleUsedTimesMost(tube.getSampleUsedTimesMost());
            }
            stockInTubeRepository.save(stockInTubes);
        }
        stockInBoxRepository.save(stockInBoxes);
    }


    @Test
    public  void createPositionInShelf()
    {
        String[] b=new String[]{"A","B","C","D","E"};  //定义数组b
        boolean flag = false;
        for(int j = 0;j<b.length;j++){
            for(int i=1;i<=5;i++){
                String bb = b[j]+i;
               if(bb.equals("A1")){//若库存里有，则继续取下一个，若没有，就取当前的这个
                   continue;
               }else{
                   flag = true;
                   System.out.print(bb);
                   break ;
               }
           }
           if(flag){
                break;
           }
        }
    }
    @Test
    public  void getLocation() throws IOException, JSONException {
        //这里调用百度的ip定位api服务 详见 http://api.map.baidu.com/lbsapi/cloud/ip-location-api.htm
        JSONObject json = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?address=","曲沃县医院");
        ObjectMapper objectMapper=new ObjectMapper();
        GeocoderSearchResponse response = objectMapper.readValue(json.getString("GeocoderSearchResponse"),GeocoderSearchResponse.class);
        BigDecimal lat = response.getResult().getLocation().getLat();
        BigDecimal lng = response.getResult().getLocation().getLng();
        JSONObject jsonForAddress = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?location=",lat+","+lng);
        ObjectMapper objectMapperForAddress = new ObjectMapper();
        GeocoderSearchAddressResponse res = objectMapperForAddress.readValue(jsonForAddress.getString("GeocoderSearchResponse"),GeocoderSearchAddressResponse.class);
        System.out.println(jsonForAddress.toString());
    }
    @Test
    public  void importLocationForProjectSite() throws IOException, JSONException {
        List<ProjectSite> projectSites = projectSiteRepository.findAll();
        for(ProjectSite p :projectSites){
            String projectSiteCode = p.getProjectSiteCode();
            String projectSiteId = null;
            if(projectSiteCode.length()<4){
                projectSiteId = projectSiteCode;
            }else{
                projectSiteId = projectSiteCode.substring(0,4);
            }
            if(StringUtils.isEmpty(p.getProjectSiteName())){
                throw new BankServiceException("项目点名称异常"+p.toString());
            }
            JSONObject json = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?address=",p.getProjectSiteName().replaceAll("\r|\n", "").replaceAll(" +",""));
            ObjectMapper objectMapper=new ObjectMapper();
            System.out.print(json.toString());
            if(json.length()==0||json == null || json.getString("GeocoderSearchResponse") == null ){

            }else{
                if(json.getJSONObject("GeocoderSearchResponse").get("status").equals("OK")){
                    GeocoderSearchResponse response = objectMapper.readValue(json.getString("GeocoderSearchResponse"),GeocoderSearchResponse.class);
                    if(response != null &&response.getStatus().equals("OK")) {
                        BigDecimal lat = response.getResult().getLocation().getLat();
                        BigDecimal lng = response.getResult().getLocation().getLng();
                        String address = lat + "," + lng;
                        JSONObject jsonForAddress = GeocoderReaderUtils.readJsonFromUrl("http://api.map.baidu.com/geocoder?location=", address);
                        ObjectMapper objectMapperForAddress = new ObjectMapper();
                        if (jsonForAddress.length()>0&&jsonForAddress.getJSONObject("GeocoderSearchResponse").get("status").equals("OK")) {
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
    @Test
    public void main() throws Exception {
        this.createProject();
        this.createProjectSite();
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
        if(rnaTube == null || dcgTube == null){
            throw new BankServiceException("冻存管类型导入失败！");
        }
        this.createFrozenBoxForA02("HE_COL_01","A",dcgTube);
        this.createFrozenBoxForA02("HE_COL_02","A",dcgTube);
        this.createFrozenBoxForA02("HE_COL_03","W",dcgTube);
        this.createFrozenBoxForA02("HE_COL_04","R",dcgTube);
        this.createFrozenBoxForA02("HE_COL_05","A",dcgTube);
        this.createFrozenBoxForA02("HE_COL_06","A",dcgTube);
        this.createFrozenBoxForA02("HE_COL_07","F",dcgTube);
        this.createFrozenBoxForA02("HE_COL_08","F",dcgTube);
        this.createFrozenBoxForA02("HE_COL_09","E",dcgTube);
        this.createFrozenBoxForA02("HE_COL_10","E",dcgTube);
        this.createFrozenBoxForA02("HE_COL_11_RNA","RNA",rnaTube);
        this.createSampleTypeMix();

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
    public void aa() throws Exception {
        HashSet<Integer> hashSet = new HashSet<>();
        randomSet(5000000,5001000,200,hashSet);
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
            List<FrozenTubeImportingForm> frozenTubeImportingForms = (List<FrozenTubeImportingForm>) JSONArray.toCollection(jsonArray,FrozenTubeImportingForm.class);
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
    public  void test() {
        int i=1;
        int j=2;
        char c1=(char) (i+64);
        char c2=(char) (j+64);
        System.out.println(c1);
        System.out.println(c2);
    }
}
