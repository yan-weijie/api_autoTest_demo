package com.zdlh.test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import net.sf.ezmorph.array.IntArrayMorpher;
import net.sf.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

import static com.jayway.jsonpath.JsonPath.*;
import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import com.zdlh.test.ConnSql;

/**
 * Created by Administrator on 2018/1/5.
 */
public class loanApiTest {
    private String baseUrl = "http://172.17.2.17";
    private int port = 8085;
    private int periods;
    private ConnSql connSql = new ConnSql();
    private List<Map<String, Object>> personList;
    private List<Map<String, String>> listPm;

    @BeforeClass
        public void setUp () {
            RestAssured.baseURI = baseUrl; //设置默认请求URL
            RestAssured.port = port; //设置请求默认端口号
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); //如果验证失败，启用请求和响应日志记录
            RestAssured.requestSpecification = new RequestSpecBuilder().build().accept(JSON).contentType(JSON); //设置请求规范
            personList = connSql.getParameter();

        }

    /**
     * 数据库获取数据
     * @param key
     * @return
     */
    public String getSqlPmarameters(int index, String key) {

        String pmarameters = "";

        if (key.equals("balanceTotal")) {
            pmarameters  = personList.get(index).get("loan_amount").toString();
        }else if (key.equals("balanceDelay")) {
            pmarameters  = personList.get(index).get("final_payment").toString();
        }else if (key.equals("yearRate")) {
            pmarameters  = personList.get(index).get("apr").toString();
        }else if (key.equals("lendingDay")) {
            pmarameters  = personList.get(index).get("payment_date").toString();
        }else if (key.equals("customerId")) {
            pmarameters  = personList.get(index).get("customer_loan_id").toString();
        }else if (key.equals("periods")) {
            pmarameters  = personList.get(index).get("periods").toString();
        }

        return pmarameters;
    }

    /**
     * post请求获取String类型响应文本
     * @return
     */
    //@Test (priority = 1)
    public String runRequest(int pmIndex) {
        String str=getSqlPmarameters(pmIndex, "yearRate");
        double d=Double.valueOf(str).doubleValue()/100.00;

        String responseJson = "";
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("balanceTotal", getSqlPmarameters(pmIndex, "balanceTotal"));
        parameters.put("balanceDelay", getSqlPmarameters(pmIndex, "balanceDelay"));
        parameters.put("yearRate", d);
        parameters.put("lendingDay", getSqlPmarameters(pmIndex, "lendingDay"));
        parameters.put("totalMonth",getSqlPmarameters(pmIndex, "periods"));
        parameters.put("yearDays","360");
        parameters.put("fundId","1");

        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(parameters);
        System.out.println("Body: "+jsonObject.toString());

        Response response = given().body(jsonObject).when().post("/services/fund/confirmLoanInfo");

        int  responseCode = response.statusCode();
        if (responseCode == 200) {

            responseJson = response.asString();

        }else {
            System.out.println("请求失败code：" + responseCode);
            //输出log
            response.then().log().all();
        }
        System.out.println("响应文本："+responseJson);

        return responseJson;
    }

    /**
     * 获取接口响应参数
     * @return
     */
//    @Test (priority = 2)
    public List<Map<String, String>> responsePm(int sqlIndex,String responseString) {

        String aaa = "{\"code\":200,\"msg\":\"操作成功！\",\"data\":{\"result\":{\"loanDetail\":{\"month_1\":{\"orderId\":1,\"monthCapital\":0.00,\"interest\":213027.50,\"monthIncome\":213027.50,\"monthBalance\":0.00,\"monthTotalInterest\":213027.50,\"capitalLeft\":121730.00,\"timeRepayment\":1491350400000},\"month_2\":{\"orderId\":2,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1493942400000},\"month_3\":{\"orderId\":3,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1496620800000},\"month_4\":{\"orderId\":4,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1499212800000},\"month_5\":{\"orderId\":5,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1501891200000},\"month_6\":{\"orderId\":6,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1504569600000},\"month_7\":{\"orderId\":7,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1507161600000},\"month_8\":{\"orderId\":8,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1509840000000},\"month_9\":{\"orderId\":9,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1512432000000},\"month_10\":{\"orderId\":10,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1515110400000},\"month_11\":{\"orderId\":11,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1517788800000},\"month_12\":{\"orderId\":12,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1520208000000},\"month_13\":{\"orderId\":13,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1522886400000},\"month_14\":{\"orderId\":14,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1525478400000},\"month_15\":{\"orderId\":15,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1528156800000},\"month_16\":{\"orderId\":16,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1530748800000},\"month_17\":{\"orderId\":17,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1533427200000},\"month_18\":{\"orderId\":18,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1536105600000},\"month_19\":{\"orderId\":19,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1538697600000},\"month_20\":{\"orderId\":20,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1541376000000},\"month_21\":{\"orderId\":21,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1543968000000},\"month_22\":{\"orderId\":22,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1546646400000},\"month_23\":{\"orderId\":23,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1549324800000},\"month_24\":{\"orderId\":24,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1551744000000},\"month_25\":{\"orderId\":25,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1554422400000},\"month_26\":{\"orderId\":26,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1557014400000},\"month_27\":{\"orderId\":27,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1559692800000},\"month_28\":{\"orderId\":28,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1562284800000},\"month_29\":{\"orderId\":29,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1564963200000},\"month_30\":{\"orderId\":30,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1567641600000},\"month_31\":{\"orderId\":31,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1570233600000},\"month_32\":{\"orderId\":32,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1572912000000},\"month_33\":{\"orderId\":33,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1575504000000},\"month_34\":{\"orderId\":34,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1578182400000},\"month_35\":{\"orderId\":35,\"monthCapital\":0.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":182595.00,\"capitalLeft\":121730.00,\"timeRepayment\":1580860800000},\"month_36\":{\"orderId\":36,\"monthCapital\":121730.00,\"interest\":null,\"monthIncome\":182595.00,\"monthBalance\":0.00,\"monthTotalInterest\":60865.00,\"capitalLeft\":0.00,\"timeRepayment\":1583366400000}},\"sumCapital\":121730.00,\"sumInterest\":6482122.50,\"sumPayment\":6603852.50}},\"exception\":null}\n";
        List<String> responseKey = new ArrayList<String>();
        responseKey.add("customerId");
        responseKey.add("orderId");
        responseKey.add("monthCapital");
        responseKey.add("monthTotalInterest");
        responseKey.add("monthIncome");
        responseKey.add("capitalLeft");
        responseKey.add("timeRepayment");

        List<Map<String, String>> responsePmList = new ArrayList<Map<String, String>>();

//        JsonPath jsonPath = new JsonPath(responseString).setRoot("data");
//        Object KeyVaalue = jsonPath.get("result.loanDetail.month_"+ 1 +".timeRepayment");
//        System.out.println("时间："+stampToDate(KeyVaalue.toString()));
        String periodsInt = getSqlPmarameters(sqlIndex,"periods");
        int per = Integer.parseInt(periodsInt);

        for (int i=1; i<=per; i++) {
            Map<String, String> rowDate = new HashMap<String, String>();
            String KeyValueString = "";

            for (int j=0; j<responseKey.size(); j++) {
                String KeyName = responseKey.get(j);
                //System.out.println("KeyName:"+KeyName);
                if (!KeyName.equals("customerId")) {
                    Object KeyValue = read(responseString, "data.result.loanDetail.month_"+ i +"."+KeyName);
                    try {
                        KeyValueString = KeyValue.toString();
                    }catch (NullPointerException e) {
                    }

                    //将timestamp时间戳转换成时间“2017-04-05 08:00:00”
                    if (KeyName.equals("timeRepayment")) {
                        //System.out.println("时间戳：" + KeyValueString);
                        KeyValueString = stampToDate(KeyValueString);
                    }
                }else {
                    KeyValueString = getSqlPmarameters(sqlIndex,"customerId");
                }


                rowDate.put(KeyName, KeyValueString);
            }
            responsePmList.add(rowDate);

        }

        return responsePmList;
    }

    /**
     * 结果插入数据库
     */
//    @Test (priority = 3)
    public void pmSqlInsert() {

        //connSql.insertDate(listPm);
    }

    @Test
    public void test() {
        String responseJsonSring;
        int sqlSize = personList.size();
        System.out.println("sqlSize: "+sqlSize);

        for (int i=0; i < 3; i++) {
            //执行接口获取响应文本
            responseJsonSring = runRequest(i);
            //解析文本获得参数
            listPm = responsePm(i,responseJsonSring);
            //接口参数插入数据库
            //connSql.insertDate(listPm);
        }

    }

     /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }



}
