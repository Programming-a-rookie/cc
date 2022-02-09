package org.sinosoft.webservice.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.sinosoft.webservice.config.EncryptConfig;
import org.sinosoft.webservice.entity.CustomerUserDatas;
import org.sinosoft.webservice.entity.CustomerUserOptimal;
import org.sinosoft.webservice.util.Base64Util;
import org.sinosoft.webservice.util.CloseConnectUtil;
import org.sinosoft.webservice.config.DBConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Component
@WebService(name = "customerUserService",
        targetNamespace = "http://sinosoft.org/webservice/service",
        endpointInterface = "org.sinosoft.webservice.service.CustomerUserService",
        portName = "10001")
@Slf4j
public class CustomerUserServiceImp implements CustomerUserService{

    @Autowired
    private DBConfig hc;

    @Autowired
    private EncryptConfig ec;

//    private final static Logger logger = LoggerFactory.getLogger(CustomerUserServiceImp.class);

    private Connection conn = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;


    public Connection getConn() throws ClassNotFoundException, SQLException {

        Class.forName(hc.getDriver());
        conn = DriverManager.getConnection(hc.getUrl());
        return conn;
    }

    public boolean isLegalDate(String complementDate){
        int legalLen = 10;
        if(complementDate.length() != legalLen){
            return false;
        }
        // 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(complementDate);
            return complementDate.equals(format.format(date));
        } catch (ParseException e) {
            return false;
        }

    }



    @Override
    public String selectCustomerUser(String custInfoParam) {
        if(custInfoParam == null){
            return "参数不能为空";
        }
        String id = JSON.parseObject(custInfoParam).getString("id");
        if (id == null || "".equals(id) ){
            log.warn("参数id=" + id);
            return "id参数不能为空";
        }
        //补数日期
        String complementDate = JSON.parseObject(custInfoParam).getString("complementDate");
        String sql = null;
        if(complementDate == null || "".equals(complementDate) ){
            sql = "select last_userid,custcodeori from h_cif.cif_customer_user_optimal_inc";
        }else if(isLegalDate(complementDate)){
            sql = "select last_userid,custcodeori from h_cif.cif_customer_user_optimal_his where substr(etldate,1,10) = " + "\'" + complementDate + "\'";
        }
        else{
            return "参数complementDate不合法，若获取增量数据，complementDate参数请设置为空串，如\"\",如获取历史数据，请输入正确的日期格式\"YYYY-MM-dd\"，如\"2022-01-01\"";
        }
        //Y3VzdG9tZXJVc2VyT3B0aW1hbA==
        String iddecode = "";
        try {
            iddecode = Base64Util.decodeUtil(id);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

//        System.out.println("id=" + ec.getId());

        if( !Arrays.asList(ec.getId().split(",")).contains(iddecode)){
            log.warn("id=" + id);
            return "id参数" + id + "错误";
        }

        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//hh为小写时是12小时制
        CustomerUserDatas cud = new CustomerUserDatas();
        ArrayList<CustomerUserOptimal> list = new ArrayList<>();

        log.info("开始查询,执行sql=" + sql);
        try {
            Connection conn = getConn();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()){
                CustomerUserOptimal cuo = new CustomerUserOptimal();

                cuo.setLastUserid(rs.getString("last_userid"));
                cuo.setCustCodeori(rs.getString("custcodeori"));
                list.add(cuo);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            try {
                CloseConnectUtil.close(conn,ps,rs);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        log.info("数据封装到ZHBGDataBean对象中");
        try {
            cud.setId(Base64Util.decodeUtil(id));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cud.setComplementDate(complementDate);
        cud.setPushTime(dateFormat.format(new Date()));
        cud.setDatas(list);
        log.info("ZHBGDataBean转JSONString并返回");
        return JSON.toJSONString(cud);
    }


}
