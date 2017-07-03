package org.fwoxford.service;

import org.fwoxford.BioBankApp;
import org.fwoxford.config.Constants;
import org.fwoxford.domain.*;
import org.fwoxford.repository.*;
import org.fwoxford.service.dto.SupportRackTypeDTO;
import org.fwoxford.service.mapper.*;
import org.fwoxford.web.rest.errors.BankServiceException;
import org.fwoxford.web.rest.util.BankUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.Date;

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
//    @Autowired
//    private  JdbcTemplate jdbcTemplate;
private final Logger log = LoggerFactory.getLogger(ImportSampleTest.class);
    /**
     * 创建项目
     * @throws Exception
     */
    @Test
    @Transactional
    public void createProject() throws Exception {
        Project project = projectRepository.findByProjectCode("0037");
        if(project != null){
            return;
        }
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
            con = DBUtilTest.open();
            System.out.println("连接成功！");
            String sql = "INSERT INTO project(project_code,project_name,status,created_by) VALUES (?,?,?,?)";// 预编译语句
            PreparedStatement pstmt=(PreparedStatement) con.prepareStatement(sql);
            pstmt.setString(1,"0037");
            pstmt.setString(2,"PEACE5");
            pstmt.setString(3,"0001");
            pstmt.setString(4,"admin");
//            pre = con.prepareStatement(sql);// 实例化预编译语句
            pstmt.executeUpdate();// 执行查询，注意括号中不需要再加参数
        }catch (Exception e){
            e.printStackTrace();
        }
        finally
        {
            try{
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
//                if (result != null)
//                    result.close();
//                if (pre != null)
//                    pre.close();
//                if (con != null)
//                    con.close();
                DBUtilTest.close(con);
                System.out.println("数据库连接已关闭！");
            }
            catch (Exception e) {
                e.printStackTrace();
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
    @Transactional
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


        try
        {
            con = DBUtilTest.open();// 获取连接
            System.out.println("连接成功！");
//            String sqlForSelect = "select * from project where project_code =?";// 预编译语句
//            PreparedStatement pstmt=(PreparedStatement) con.prepareStatement(sqlForSelect);
//            pstmt.setString(1,"0037");
//            ResultSet rs=pstmt.executeQuery();
//            if(rs.next())
//            {
//                projectId=rs.getLong(1);
//            }
            projectId=1L;
            for(int i = 0 ;i<list.size();i++){
                String projectSiteCode = list.get(i).get("LCC_ID").toString();
                ProjectSite projectSite = projectSiteRepository.findByProjectSiteCode(projectSiteCode);
                if(projectSite !=null){
                    return;
                }
                String sqlForInsert = "insert  into project_site(project_site_code,project_site_name,area," +
                    "status,detailed_location,department,detailed_address,zip_code," +
                    "username_1,phone_number_1,username_2,phone_number_2,created_by) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";// 预编译语句
                PreparedStatement ps=(PreparedStatement) con.prepareStatement(sqlForInsert);
                ps.setString(1,list.get(i).get("LCC_ID").toString());
                ps.setString(2,list.get(i).get("NAME").toString());
                ps.setString(3,list.get(i).get("AREA").toString());
                ps.setString(4,"0001");
                ps.setObject(5,list.get(i).get("LOCATION"));
                ps.setString(6,list.get(i).get("DEPARTMENT").toString());
                ps.setString(7,list.get(i).get("ADDRESS").toString());
                ps.setObject(8,list.get(i).get("ZIP_CODE"));
                ps.setString(9,list.get(i).get("USER_1").toString());
                ps.setString(10,list.get(i).get("PHONE_1").toString());
                ps.setString(11,list.get(i).get("USER_2").toString());
                ps.setString(12,list.get(i).get("PHONE_2").toString());
                ps.setString(13,"admin");
                int id = ps.executeUpdate();
//
                String sqlForInsertRelate = "insert  into project_relate(project_id,project_site_id,status,created_by) VALUES (?,?,?,?)";// 预编译语句
                PreparedStatement pss=(PreparedStatement) con.prepareStatement(sqlForInsertRelate);
                pss.setLong(1,projectId);
                pss.setLong(2,i+1L);
                pss.setString(3,"0001");
                pss.setString(4,"admin");
                int ids = pss.executeUpdate();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        finally
        {
            try{
                DBUtilTest.close(con);
                System.out.println("数据库连接已关闭！");
            }
            catch (Exception e) {
                e.printStackTrace();
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
        SupportRackType supportRackType = new SupportRackType();

        supportRackType.setSupportRackTypeCode("5*5");
        supportRackType.setSupportRackRows("5");
        supportRackType.setSupportRackColumns("5");
        supportRackType.setStatus("0001");
        supportRackType.setCreatedBy("admin");
        supportRackTypeRepository.saveAndFlush(supportRackType);
        assertThat(supportRackType).isNotNull();
    }

    /**
     * 冻存管类型--固定值
     * 2017年6月29日09:32:45
     * @throws Exception
     */
    @Test
    public void createFrozenTubeType() throws Exception {
        //冻存架类型
        FrozenTubeType frozenTubeType = new FrozenTubeType()
            .frozenTubeTypeCode("2ml")
            .frozenTubeTypeName("2ml")
            .sampleUsedTimesMost(10)
            .frozenTubeVolumn(2)
            .frozenTubeVolumnUnit("ml")
            .status("0001");
        frozenTubeType.setCreatedBy("admin");
        frozenTubeTypeRepository.saveAndFlush(frozenTubeType);
        assertThat(frozenTubeType).isNotNull();
    }

    /**
     * 创建设备组--固定值
     * 2017年6月29日09:44:50
     * @throws Exception
     */
    @Test
    public void createEquipmentGroup() throws Exception {
        //创建设备组
        EquipmentGroup equipmentGroup = new EquipmentGroup()
            .equipmentGroupName("样本库")
            .equipmentGroupManagerId(5L)
            .equipmentManagerName("钟慧")
            .equipmentGroupAddress("样本中心D座")
            .status("0001");
        equipmentGroup.setCreatedBy("admin");
        equipmentGroupRepository.saveAndFlush(equipmentGroup);
        assertThat(equipmentGroup).isNotNull();
    }

    /**
     * 创建设备类型--固定值
     * @throws Exception
     */
    @Test
    public void createEquipmentModel() throws Exception {
        EquipmentModle equipmentModle = new EquipmentModle()
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
    public void createSampleTypeAndClassification() throws Exception {

        SampleType sampleType1 = sampleTypeRepository.findBySampleTypeCode("A");
        if(sampleType1 == null){
            sampleType1 = new SampleType().sampleTypeCode("A").sampleTypeName("血浆")
                .status("0001").isMixed(0).backColor("black").frontColor("rgb(240,224,255)");
            sampleTypeRepository.saveAndFlush(sampleType1);
            assertThat(sampleType1).isNotNull();
        }
        SampleType sampleType2 = sampleTypeRepository.findBySampleTypeCode("W");
        if(sampleType2 == null){
            sampleType2 = new SampleType().sampleTypeCode("W").sampleTypeName("白细胞")
                .status("0001").isMixed(0).backColor("black").frontColor("rgb(255,255,255)");
            sampleTypeRepository.saveAndFlush(sampleType2);
            assertThat(sampleType2).isNotNull();
        }
        SampleType sampleType3 = sampleTypeRepository.findBySampleTypeCode("F");
        if(sampleType3 == null){
            sampleType3 = new SampleType().sampleTypeCode("F").sampleTypeName("血清")
                .status("0001").isMixed(0).backColor("black").frontColor("rgb(255,179,179)");
            sampleTypeRepository.saveAndFlush(sampleType3);
            assertThat(sampleType3).isNotNull();
        }
        SampleType sampleType4 = sampleTypeRepository.findBySampleTypeCode("E");
        if(sampleType4 == null){
            sampleType4 = new SampleType().sampleTypeCode("E").sampleTypeName("尿")
                .status("0001").isMixed(0).backColor("black").frontColor("rgb(255,255,179)");
            sampleTypeRepository.saveAndFlush(sampleType4);
            assertThat(sampleType4).isNotNull();
        }
        SampleType sampleType5 = sampleTypeRepository.findBySampleTypeCode("R");
        if(sampleType5 == null){
            sampleType5 = new SampleType().sampleTypeCode("R").sampleTypeName("红细胞")
                .status("0001").isMixed(0).backColor("black").frontColor("rgb(236,236,236)");
            sampleTypeRepository.saveAndFlush(sampleType5);
            assertThat(sampleType5).isNotNull();
        }
        SampleType sampleType6 = sampleTypeRepository.findBySampleTypeCode("RNA");
        if(sampleType6 == null){
            sampleType6 = new SampleType().sampleTypeCode("RNA").sampleTypeName("RNA")
                .status("0001").isMixed(0).backColor("black").frontColor("rgb(255,220,165)");
            sampleTypeRepository.saveAndFlush(sampleType6);
            assertThat(sampleType6).isNotNull();
        }
    }
    /**
     * 导入冻存盒--A类型--EDTA血浆1
     * @throws Exception
     */
    @Test
    public void createFrozenBoxForA01() throws Exception {
        String sql = "select * from HE_COL_01";
        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try{
            con = DBUtilForTemp.open();
            System.out.println("连接成功！");
            String sqlForSelect = "select * from HE_COL_01";// 预编译语句
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

        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode("A");
        FrozenBoxType frozenBoxType = frozenBoxTypeRepository.findByFrozenBoxTypeCode("B1010");
        for(String key :map.keySet()){
            Long LCC_ID = null;
            try{
                con = DBUtilForTemp.open();
                System.out.println("连接成功！");
                String sqlForSelectSite = "select DISTINCT LCC_ID from biobank_temp_01.fen_he_ji_lu where box_code = ?";
                PreparedStatement pstmt=(PreparedStatement) con.prepareStatement(sqlForSelectSite);
                pstmt.setString(1,key);
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
            List<Map<String, Object>> sampleList = map.get(key);
            String sampleClassTypeCode = sampleList.get(0).get("SAMPLE_TYPE_CODE").toString();
            String sampleClassTypeName = sampleList.get(0).get("SAMPLE_TYPE").toString();
            if(sampleList.get(0).get("POS_IN_SHELF") == null){
                throw new BankServiceException("所在架子内位置为空！");
            }
            String posInShelf = sampleList.get(0).get("POS_IN_SHELF").toString();
            String columnsInShelf = posInShelf.substring(0, 1);
            String rowsInShelf =  posInShelf.substring(1);
            SampleClassification sampleClassification = sampleClassificationRepository.findBySampleClassificationCode(sampleClassTypeCode);
                if(sampleClassification == null){
                    sampleClassification = new SampleClassification()
                        .sampleClassificationCode(sampleClassTypeCode)
                        .sampleClassificationName(sampleClassTypeName)
                        .status("0001")
                        .backColor("black")
                        .frontColor("rgb(240,224,255)");
                    sampleClassification.setCreatedBy("admin");
                    sampleClassificationRepository.saveAndFlush(sampleClassification);
                    assertThat(sampleClassification).isNotNull();

                    ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(1L,sampleType.getId(),sampleClassification.getId());;
                    if(projectSampleClass == null){
                        projectSampleClass = new ProjectSampleClass()
                            .project(projectSampleClassMapper.projectFromId(1L)).projectCode("0037")
                            .sampleClassification(projectSampleClassMapper.sampleClassificationFromId(sampleClassification.getId()))
                            .sampleType(projectSampleClassMapper.sampleTypeFromId(sampleType.getId()))
                            .columnsNumber("1")
                            .status("0001");
                        projectSampleClass.setCreatedBy("admin");
                        projectSampleClassRepository.saveAndFlush(projectSampleClass);
                        assertThat(projectSampleClass).isNotNull();
                    }
            }

            String equipmentCode = sampleList.get(0).get("EQ_CODE").toString();
            String areaCode = sampleList.get(0).get("AREA_CODE").toString();
            String supportCode = sampleList.get(0).get("SHELF_CODE").toString();
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
                area.setCreatedBy("admin");
                areaRepository.saveAndFlush(area);
                assertThat(area).isNotNull();
            }
            //冻存架
            SupportRack supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),supportCode);
            if(supportRack==null){
                supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackMapper.supportRackTypeFromId(1L)).supportRackTypeCode("5*5")
                .area(supportRackMapper.areaFromId(area.getId()));
                supportRack.setCreatedBy("admin");
                supportRackRepository.saveAndFlush(supportRack);
                assertThat(supportRack).isNotNull();
            }

            //保存冻存盒
            FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(key);
            if(frozenBox == null){
                frozenBox = new FrozenBox()
                    .frozenBoxCode(key)
                    .frozenBoxTypeCode("B1010")
                    .frozenBoxTypeRows("10")
                    .frozenBoxTypeColumns("10")
                    .projectCode("0037")
                    .projectName("PEACE5")
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
                    .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).project(frozenBoxMapper.projectFromId(1L)).projectSite(projectSite);
                frozenBox.setCreatedBy("admin");
            }
            frozenBoxRepository.saveAndFlush(frozenBox);
            assertThat(frozenBox).isNotNull();
            for(int i = 0 ; i<sampleList.size();i++){
                if(sampleList.get(i).get("SAMPLE_CODE") ==null){
                    throw new BankServiceException("样本编码为空！",sampleList.toString());
                }
                String sampleCode = sampleList.get(i).get("SAMPLE_CODE").toString();
                String posInBox = sampleList.get(i).get("POS_IN_BOX").toString();
                String tubeRows = posInBox.substring(0, 1);
                String tubeColumns =  posInBox.substring(1);
                List<FrozenTube> frozenTubeList = frozenTubeRepository.findBySampleCodeAndProjectCodeAndSampleTypeCode(sampleCode,"0037","A");
                if(frozenTubeList.size()==0){
                    FrozenTube frozenTube = new FrozenTube()
                        .projectCode("0037").projectSiteCode(projectSite!=null?projectSite.getProjectSiteCode():null)
                        .sampleCode(sampleCode)
                        .frozenTubeTypeCode("2ml")
                        .frozenTubeTypeName("2ml")
                        .sampleTypeCode("A")
                        .sampleTypeName("血浆")
                        .sampleUsedTimesMost(10)
                        .sampleUsedTimes(0)
                        .frozenTubeVolumns(2)
                        .frozenTubeVolumnsUnit("ml")
                        .tubeRows(tubeRows)
                        .tubeColumns(tubeColumns)
                        .status("3001")
                        .frozenBoxCode(key).frozenTubeType(frozenTubeMapper.frozenTubeTypeFromId(1L)).sampleType(sampleType).sampleClassification(sampleClassification)
                        .project(frozenTubeMapper.projectFromId(1L)).projectSite(projectSite).frozenBox(frozenBox).frozenTubeState("2004");
                    frozenTube.setCreatedBy("admin");
                    frozenTubeRepository.saveAndFlush(frozenTube);
                    assertThat(frozenTube).isNotNull();
                }
            }
        }
    }

    /**
     * 导入冻存盒--A类型--EDTA血浆2
     * @throws Exception
     */
    public void createFrozenBoxForA02(String tableName,String stockInCode,String sampleTypeCode) throws Exception {
        SampleType sampleType = sampleTypeRepository.findBySampleTypeCode(sampleTypeCode);
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
            if(!StringUtils.isEmpty(storeKeeperId1)){
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
                    .countOfSample(sampleList.size()).stockInCode(stockInCodeNew).project(stockInMapper.projectFromId(1L)).projectSite(projectSite)
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
            SampleClassification sampleClassification = sampleClassificationRepository.findBySampleClassificationCode(sampleClassTypeCode);
            String frontColor = Constants.SAMPLECLASSIFICATION_COLOR_MAP.get(sampleClassTypeCode).toString();
            if(sampleClassification == null){
                sampleClassification = new SampleClassification()
                    .sampleClassificationCode(sampleClassTypeCode)
                    .sampleClassificationName(sampleClassTypeName)
                    .status("0001")
                    .backColor("black")
                    .frontColor(frontColor);
                sampleClassificationRepository.saveAndFlush(sampleClassification);
                assertThat(sampleClassification).isNotNull();

                ProjectSampleClass projectSampleClass = projectSampleClassRepository.findByProjectIdAndSampleTypeIdAndSampleClassificationId(1L,sampleType.getId(),sampleClassification.getId());;
                String columnNumber = Constants.COLUMNNUMBER_MAP.get(sampleClassTypeCode);
                if(projectSampleClass == null){
                    projectSampleClass = new ProjectSampleClass()
                        .project(projectSampleClassMapper.projectFromId(1L)).projectCode("0037")
                        .sampleClassification(projectSampleClassMapper.sampleClassificationFromId(sampleClassification.getId()))
                        .sampleType(projectSampleClassMapper.sampleTypeFromId(sampleType.getId()))
                        .columnsNumber(columnNumber)
                        .status("0001");
                    projectSampleClassRepository.saveAndFlush(projectSampleClass);
                    assertThat(projectSampleClass).isNotNull();
                }
            }

            String equipmentCode = sampleList.get(0).get("EQ_CODE").toString();
            String areaCode = sampleList.get(0).get("AREA_CODE").toString();
            String supportCode = sampleTypeCode!="RNA"?sampleList.get(0).get("SHELF_CODE").toString():null;
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
            SupportRack supportRack = null;
            if(sampleTypeCode!="RNA"){
                supportRack = supportRackRepository.findByAreaIdAndSupportRackCode(area.getId(),supportCode);
                if(supportRack==null){
                    supportRack = new SupportRack().status("0001").supportRackCode(supportCode).supportRackType(supportRackMapper.supportRackTypeFromId(1L)).supportRackTypeCode("5*5")
                        .area(supportRackMapper.areaFromId(area.getId()));
                    supportRackRepository.saveAndFlush(supportRack);
                    assertThat(supportRack).isNotNull();
                }
            }

            //保存冻存盒
            FrozenBox frozenBox = frozenBoxRepository.findFrozenBoxDetailsByBoxCode(key);
            if(frozenBox == null){
                frozenBox = new FrozenBox()
                    .frozenBoxCode(key)
                    .frozenBoxTypeCode(frozenBoxType.getFrozenBoxTypeCode())
                    .frozenBoxTypeRows(frozenBoxType.getFrozenBoxTypeRows())
                    .frozenBoxTypeColumns(frozenBoxType.getFrozenBoxTypeColumns())
                    .projectCode("0037")
                    .projectName("PEACE5")
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
                    .columnsInShelf(columnsInShelf).rowsInShelf(rowsInShelf).project(frozenBoxMapper.projectFromId(1L)).projectSite(projectSite);
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

            //增加冻存盒位置记录
            FrozenBoxPosition frozenBoxPos = new FrozenBoxPosition();
            frozenBoxPos = frozenBoxPositionMapper.frozenBoxToFrozenBoxPosition(frozenBoxPos,frozenBox);
            frozenBoxPos.setStatus(Constants.FROZEN_BOX_STOCKED);
            frozenBoxPos = frozenBoxPositionRepository.save(frozenBoxPos);
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
                        .frozenTubeTypeCode("2ml")
                        .frozenTubeTypeName("2ml")
                        .sampleTypeCode(sampleType.getSampleTypeCode())
                        .sampleTypeName(sampleType.getSampleTypeName())
                        .sampleUsedTimesMost(10)
                        .sampleUsedTimes(0)
                        .frozenTubeVolumns(2)
                        .frozenTubeVolumnsUnit("ml")
                        .tubeRows(tubeRows)
                        .tubeColumns(tubeColumns)
                        .status(status).memo(memo).sampleStage(times)
                        .frozenBoxCode(key).frozenTubeType(frozenTubeMapper.frozenTubeTypeFromId(1L)).sampleType(sampleType).sampleClassification(sampleClassification)
                        .project(frozenTubeMapper.projectFromId(1L)).projectSite(projectSite).frozenBox(frozenBox).frozenTubeState("2004");
                    frozenTubeRepository.saveAndFlush(tube);
                    assertThat(tube).isNotNull();

                    StockInTube stockInTube = stockInTubeRepository.findByFrozenTubeId(tube.getId());
                    if(stockInTube == null){
                        stockInTube = new StockInTube().status(Constants.STOCK_IN_TUBE_PENDING)
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
    @Test
    public void importSample() throws Exception {
//        String sql = "select * from frozen_box";
//        List<Map<String,Object>> map = jdbcTemplate.queryForList(sql);

        Connection con = null;// 创建一个数据库连接
        PreparedStatement pre = null;// 创建预编译语句对象，一般都是用这个而不用Statement
        ResultSet result = null;// 创建一个结果集对象
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");// 加载Oracle驱动程序
            System.out.println("开始尝试连接数据库！");
            String url = "jdbc:oracle:" + "thin:@10.24.10.56:1521:xe";// 本机地址，XE是精简版Oracle的默认数据库名
            String user = "biobank_temp_01";// 用户名
            String password = "root123";// 密码
            con = DriverManager.getConnection(url, user, password);// 获取连接
            System.out.println("连接成功！");
            String sql = "select * from frozen_box ";// 预编译语句
            pre = con.prepareStatement(sql);// 实例化预编译语句
            result = pre.executeQuery();// 执行查询，注意括号中不需要再加参数
            while (result.next())
                // 当结果集不为空时
                System.out.println( result.getInt("id"));
        }catch (Exception e){
            e.printStackTrace();
        }
        finally
        {
            try{
                // 逐一将上面的几个对象关闭，因为不关闭的话会影响性能、并且占用资源
                // 注意关闭的顺序，最后使用的最先关闭
                if (result != null)
                    result.close();
                if (pre != null)
                    pre.close();
                if (con != null)
                    con.close();
                System.out.println("数据库连接已关闭！");
            }
            catch (Exception e) {
                e.printStackTrace();
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
    public void main() throws Exception {
        this.createProject();
        this.createProjectSite();
        this.createSupportRackType();
        this.createEquipmentGroup();
        this.createEquipmentModel();
        this.createFrozenTubeType();
        this.createFrozenBoxType();
        this.createSampleTypeAndClassification();
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
        this.createFrozenBoxForA02("HE_COL_11_RNA","1498802167424","RNA");
    }
}
