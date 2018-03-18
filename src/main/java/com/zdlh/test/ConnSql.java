package com.zdlh.test;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.*;
import java.sql.*;

/**
 * Created by Administrator on 2018/1/5.
 */
public class ConnSql {
    static Connection conn;

    @BeforeClass
    public void setUp() {
        String sqlURL = "xxx";
        String sqlUSER = "xxx";
        String sqlPASS = "xxx";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(sqlURL, sqlUSER, sqlPASS);
        } catch (Exception e) {
            System.out.println("数据库连接失败" + e.getMessage());
        }

    }

    @AfterClass
    public void tearDown() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询结果转成List数据
     * @return
     */
    public List<Map<String, Object>> getParameter () {
        setUp();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String sql = "SELECT a.customer_loan_id,b.periods,b.loan_amount,b.final_payment,b.apr,c.payment_date "
                + "FROM t_loan_flow_platform a LEFT JOIN t_loan_customer_product b ON a.customer_loan_id = b.customer_loan_id "
                + "LEFT JOIN t_loan_customer_apply c ON a.customer_loan_id = c.id "
                + "WHERE a.audit_result IN (23, 26, 27, 28, 29, 30) AND a.`status`=1 AND b.`status`=1";
        try {
            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(sql);

            ResultSetMetaData md = resultSet.getMetaData();
            //取得列数
            int columnCount = md.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> rowDate = new HashMap<String, Object>();
                for(int i= 1; i <= columnCount; i++) {

                    rowDate.put(md.getColumnName(i),  resultSet.getString(i)); //getColumnName(i)获取列名
                }
                list.add(rowDate);
            }

        }catch (SQLException e) {
            System.out.println("查询数据失败" + e.getMessage());
        }

        tearDown();
        return list;

    }



    /**
     * 将数据结果保存到数据库
     * @param map
     */
    public void insertDate(List<Map<String, String>> map) {
        setUp();
        String sqlStr = "INSERT INTO t_loan_customer_detail_copy(customer_loan_id, periods_no, capital, interest, month_income, capital_left, repayment_time)" +
                " VALUES ";
//        String isertSql = "(72, 121730.0, 121730.0, 182595.0, 0.0, '2020-03-05 08:00:00')";
        String isertSql = "";

        List<String> sqlList = new ArrayList<String>();
        for (int i=0; i<map.size(); i++) {
            String sql = "("+ map.get(i).get("customerId")
                    +", "+map.get(i).get("orderId")
                    +", "+map.get(i).get("monthCapital")
                    +", "+map.get(i).get("monthTotalInterest")
                    +", "+map.get(i).get("monthIncome")
                    +", "+map.get(i).get("capitalLeft")
                    +", '"+map.get(i).get("timeRepayment")+"'),";
            sqlList.add(sql);
            isertSql += sqlList.get(i);
        }

        System.out.println("语句："+sqlStr+isertSql);

        try {

            Statement st = (Statement) conn.createStatement();
            st.executeUpdate(sqlStr+isertSql.substring(0,isertSql.length()-1));
        }catch (SQLException e) {
            System.out.println("数据库操作失败" + e.getMessage());
        }
        tearDown();

    }

    @Test
    public void tests() {

        String sss = "2020-03-05 08:00:00";
        List<Map<String, Object>> personList = getParameter();
        System.out.println("SQL :"+personList.size());

        for (int i=0; i<=10; i++) {
            System.out.println("SQL结果:"+personList.get(i).get("loan_amount"));
            System.out.println("SQL结果:"+personList.get(i).get("final_payment"));
            System.out.println("SQL结果:"+personList.get(i).get("apr"));
            System.out.println("SQL结果:"+personList.get(i).get("payment_date"));

        }

    }

    /**
     * 查询结果转成Map数据
     * @param rs
     * @return
     */
    public static Map<String, Object> convertMap(ResultSet rs) {
        Map<String, Object> map = new TreeMap<String, Object>();
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    map.put(md.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                rs = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return map;
        }
    }
}
