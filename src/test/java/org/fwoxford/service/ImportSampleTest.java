package org.fwoxford.service;

import org.fwoxford.BioBankApp;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

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

private final Logger log = LoggerFactory.getLogger(ImportSampleTest.class);
    /**
     * 创建项目
     * @throws Exception
     */
    @Test
    public void createProject() throws Exception {
        Project project = projectRepository.findByProjectCode("0037");
        if(project == null){
            project = new Project().projectCode("0037").projectName("PEACE5").status("0001");
            projectRepository.saveAndFlush(project);
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
                ProjectRelate projectRelate = projectRelateRepository.findByProjectIdAndProjectSiteId(1L, projectSite.getId());
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
        SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode("5*5");
        if(supportRackType==null){
            supportRackType = new SupportRackType();
            supportRackType.setSupportRackTypeCode("5*5");
            supportRackType.setSupportRackRows("5");
            supportRackType.setSupportRackColumns("5");
            supportRackType.setStatus("0001");
            supportRackTypeRepository.saveAndFlush(supportRackType);
            assertThat(supportRackType).isNotNull();
        }
        //冻存架类型
        SupportRackType supportRackType1 = supportRackTypeRepository.findBySupportRackTypeCode("1*75");
        if(supportRackType1==null){
            supportRackType1 = new SupportRackType();
            supportRackType1.setSupportRackTypeCode("1*75");
            supportRackType1.setSupportRackRows("1");
            supportRackType1.setSupportRackColumns("75");
            supportRackType1.setStatus("0001");
            supportRackTypeRepository.saveAndFlush(supportRackType1);
            assertThat(supportRackType1).isNotNull();
        }
    }

    /**
     * 冻存管类型--固定值
     * 2017年6月29日09:32:45
     * @throws Exception
     */
    @Test
    public void createFrozenTubeType() throws Exception {
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode("2ml");
        if(frozenTubeType == null){
            frozenTubeType = new FrozenTubeType()
                .frozenTubeTypeCode("2ml")
                .frozenTubeTypeName("2ml")
                .sampleUsedTimesMost(10)
                .frozenTubeVolumn(2)
                .frozenTubeVolumnUnit("ml")
                .status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType);
            assertThat(frozenTubeType).isNotNull();
        }
        FrozenTubeType frozenTubeType1 = frozenTubeTypeRepository.findByFrozenTubeTypeCode("6ml");
        if(frozenTubeType1 == null){
            frozenTubeType1 = new FrozenTubeType()
                .frozenTubeTypeCode("6ml")
                .frozenTubeTypeName("6ml")
                .sampleUsedTimesMost(10)
                .frozenTubeVolumn(6)
                .frozenTubeVolumnUnit("ml")
                .status("0001");
            frozenTubeTypeRepository.saveAndFlush(frozenTubeType1);
            assertThat(frozenTubeType1).isNotNull();
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
        EquipmentModle equipmentModle = equipmentModleRepository.findByEquipmentModelCode("01");
        if(equipmentModle == null){
            equipmentModle = new EquipmentModle()
                .equipmentModelCode("01")
                .equipmentModelName("冰箱")
                .equipmentType("冰箱")
                .areaNumber(4)
                .shelveNumberInArea(6)
                .status("0001");
            equipmentModle.setCreatedBy("admin");
            equipmentModleRepository.saveAndFlush(equipmentModle);
            assertThat(equipmentModle).isNotNull();
        }
    }
    /**
     * 创建冻存盒类型--固定值
     * @throws Exception
     */
    @Test
    public void createFrozenBoxType() throws Exception {
        //冻存盒类型
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("B1010");
        if(frozenBoxType == null){
            frozenBoxType = new FrozenBoxType().frozenBoxTypeCode("B1010")
                .frozenBoxTypeName("10*10").frozenBoxTypeColumns("10").frozenBoxTypeRows("10").status("0001");
            frozenBoxType.setCreatedBy("admin");
            frozenBoxTypeRepository.saveAndFlush(frozenBoxType);
            assertThat(frozenBoxType).isNotNull();
        }
    }
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
    /**
     * 导入冻存盒
     * @throws Exception
     */
    public void createFrozenBoxForA02(String tableName,String stockInCode,String sampleTypeCode) throws Exception {
        Project project = projectRepository.findByProjectCode("0037");
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode(sampleTypeCode);
        if(sampleType == null){
            throw new BankServiceException("样本类型为空！表名为："+tableName+"样本类型编码为："+sampleTypeCode);
        }
        FrozenTubeType frozenTubeType = frozenTubeTypeRepository.findByFrozenTubeTypeCode(sampleTypeCode!="RNA"?"2ml":"6ml");
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

        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("B1010");
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
            String stockInCodeNew = stockInCode +m;
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
            String posInShelf = sampleTypeCode!="RNA"?sampleList.get(0).get("POS_IN_SHELF").toString():null;
            String columnsInShelf = posInShelf!=null?posInShelf.substring(0, 1):null;
            String rowsInShelf =  posInShelf!=null?posInShelf.substring(1):null;
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

                ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(1L,sampleType.getId(),sampleClassification.getId());;
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
            String supportCode = sampleTypeCode!="RNA"?sampleList.get(0).get("SHELF_CODE").toString().trim():"R01";
            Equipment entity = equipmentRepository.findOneByEquipmentCode(equipmentCode);
            if(entity==null){
                entity = new Equipment().equipmentAddress("样本中心D座").equipmentCode(equipmentCode)
                    .equipmentGroup(equipmentMapper.equipmentGroupFromId(1L))
                    .equipmentModle(equipmentMapper.equipmentModleFromId(1L)).temperature(-40).ampoulesMax(60000).ampoulesMin(38400).status("0001");
                entity.setCreatedBy("admin");
            }
            //设备
            equipmentRepository.saveAndFlush(entity);
            assertThat(entity).isNotNull();

            //区域
            Area area = areaRepository.findOneByAreaCodeAndEquipmentId(areaCode,entity.getId());
            if(area==null){
                area = new Area().areaCode(areaCode).equipment(areaMapper.equipmentFromId(entity.getId())).freezeFrameNumber(6).equipmentCode(equipmentCode)
                    .status("0001");
                areaRepository.saveAndFlush(area);
                assertThat(area).isNotNull();
            }
            //冻存架
            SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),supportCode);
            SupportRackType supportRackType = supportRackTypeRepository.findBySupportRackTypeCode(sampleTypeCode!="RNA"?"5*5":"1*75");
            if(supportRackType == null){
                throw new BankServiceException("冻存架类型为空！表名为："+tableName+"；盒号为"+key+";样本类型为："+sampleTypeCode);
            }
            if(supportRack==null){
                supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackType).supportRackTypeCode(supportRackType.getSupportRackTypeCode())
                    .area(supportRackMapper.areaFromId(area.getId()));
                supportRackRepository.saveAndFlush(supportRack);
                assertThat(supportRack).isNotNull();
            }

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
                stockInBoxRepository.saveAndFlush(stockInBox);
                assertThat(stockInBox).isNotNull();
            }

//            //增加冻存盒位置记录
//            FrozenBoxPosition frozenBoxPos = new FrozenBoxPosition();
//            frozenBoxPos = frozenBoxPositionMapper.frozenBoxToFrozenBoxPosition(frozenBoxPos,frozenBox);
//            frozenBoxPos.setStatus(Constants.FROZEN_BOX_STOCKED);
//            frozenBoxPos = frozenBoxPositionRepository.save(frozenBoxPos);
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
                        stockInTube = new StockInTube().status(Constants.STOCK_IN_TUBE_COMPELETE)
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
        SampleType sampleType7 = sampleTypeRepository.findBySampleTypeCode("98");
        List<SampleClassification> sampleClassifications = sampleClassificationRepository.findAll();
        if(sampleType7 == null){
            sampleType7 = new SampleType().sampleTypeCode("98").sampleTypeName("98")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(169,241,253)");
            sampleTypeRepository.saveAndFlush(sampleType7);
            assertThat(sampleType7).isNotNull();
        }
//        for(SampleClassification s :sampleClassifications){
//            ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(1L,sampleType7.getId(),s.getId());;
//            String columnNumber = Constants.COLUMNNUMBER_MAP.get(s.getSampleClassificationCode());
//            if(projectSampleClass == null){
//                projectSampleClass = new ProjectSampleClass()
//                    .project(projectSampleClassMapper.projectFromId(1L)).projectCode("0037")
//                    .sampleClassification(s)
//                    .sampleType(sampleType7)
//                    .columnsNumber(columnNumber)
//                    .status("0001");
//                projectSampleClassRepository.saveAndFlush(projectSampleClass);
//                assertThat(projectSampleClass).isNotNull();
//            }
//        }
        SampleType sampleType8 = sampleTypeRepository.findBySampleTypeCode("99");
        if(sampleType8 == null){
            sampleType8 = new SampleType().sampleTypeCode("99").sampleTypeName("99")
                .status("0001").isMixed(1).frontColor("black").backColor("rgb(169,241,253)");
            sampleTypeRepository.saveAndFlush(sampleType8);
            assertThat(sampleType8).isNotNull();
        }
        for(SampleClassification s :sampleClassifications){
            ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(1L,sampleType8.getId(),s.getId());;
            String columnNumber = Constants.COLUMNNUMBER_MAP.get(s.getSampleClassificationCode());
            if(projectSampleClass == null){
                projectSampleClass = new ProjectSampleClass()
                    .project(projectSampleClassMapper.projectFromId(1L)).projectCode("0037")
                    .sampleClassification(s)
                    .sampleType(sampleType8)
                    .columnsNumber(columnNumber)
                    .status("0001");
                projectSampleClassRepository.saveAndFlush(projectSampleClass);
                assertThat(projectSampleClass).isNotNull();
            }
        }
    }

    @Test
    public void createDelegate() throws Exception {
        Delegate delegate = delegateRepository.findByDelegateCode("D_00001");
        if(delegate == null){
            delegate = new Delegate().delegateCode("D_00001").delegateName("中国牛津国际医学研究中心 中心实验室").status("0001");
            delegateRepository.saveAndFlush(delegate);
        }
    }
    @Test
    public void main() throws Exception {
        this.createProject();
        this.createProjectSite();
        this.createSupportRackType();
        this.createEquipmentGroup();
        this.createEquipmentModel();
        this.createFrozenTubeType();
        this.createFrozenBoxType();
        this.createSampleType();
        this.createFrozenBoxForA02("HE_COL_01","1498790361822","A");
        this.createFrozenBoxForA02("HE_COL_02","1498793134929","A");
        this.createFrozenBoxForA02("HE_COL_03","1498802077085","W");
        this.createFrozenBoxForA02("HE_COL_04","1498802084828","R");
        this.createFrozenBoxForA02("HE_COL_05","1498802097495","A");
        this.createFrozenBoxForA02("HE_COL_06","1498802124180","A");
        this.createFrozenBoxForA02("HE_COL_07","1498802133675","F");
        this.createFrozenBoxForA02("HE_COL_08","1498802143791","F");
        this.createFrozenBoxForA02("HE_COL_09","1498802167424","E");
        this.createFrozenBoxForA02("HE_COL_10","1498802175768","E");
        this.createFrozenBoxForA02("HE_COL_11_RNA","1499052532056","RNA");
        this.createSampleTypeMix();
//        this.createDelegate();
    }
}
