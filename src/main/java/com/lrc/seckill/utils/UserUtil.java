package com.lrc.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lrc.seckill.VO.RespBean;
import com.lrc.seckill.pojo.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//生成用户工具类
public class UserUtil {
    private static void createUser(int count) throws Exception {
        List<User> users = new ArrayList<>(count);
        for(int i = 0; i < count; i ++) {
            User user = new User();
            user.setId(18763740000L + i);
            user.setNickname("admin" + i);
            user.setSalt("1a2b3c4d");
            user.setPassword(MD5Util.frontendToStorageMD5("951esz753",user.getSalt()));
            user.setLoginCount(0);
            user.setRegisterDate(new Date());
            users.add(user);
        }
        System.out.println("create user");

        //插入数据库
        Connection conn = getConn();
        String sql = "insert into t_user(login_count,nickname,register_date,salt,password,id) values(?,?,?,?,?,?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        for(int i = 0; i < users.size(); i ++) {
            User user = users.get(i);
            preparedStatement.setInt(1,user.getLoginCount());
            preparedStatement.setString(2,user.getNickname());
            preparedStatement.setTimestamp(3,new Timestamp(user.getRegisterDate().getTime()));
            preparedStatement.setString(4,user.getSalt());
            preparedStatement.setString(5,user.getPassword());
            preparedStatement.setLong(6,user.getId());
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.clearParameters();
        conn.close();
        System.out.println("insert into db");

        //登录生成userTicket
        String urlString = "http://localhost:8080/login/doLogin";
        File file = new File("D:\\Codes\\seckill\\config.txt");
        if(file.exists()) file.delete();
        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        raf.seek(0);
        for (int i = 0; i < users.size(); i ++) {
            User user = users.get(i);
            URL url = new URL(urlString);
            HttpURLConnection co = (HttpURLConnection) url.openConnection();
            co.setRequestMethod("POST");
            co.setDoOutput(true);
            OutputStream out = co.getOutputStream();
            String params = "mobile=" + user.getId() + "&password=" + MD5Util.frontendMD5("951esz753");
            out.write(params.getBytes());
            out.flush();
            InputStream inputstream = co.getInputStream();
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int len = 0;
            while((len = inputstream.read(buff)) >= 0) {
                bout.write(buff,0,len);
            }
            inputstream.close();
            bout.close();
            String response = new String(bout.toByteArray());
            ObjectMapper mapper = new ObjectMapper();
            RespBean respBean = mapper.readValue(response,RespBean.class);
            String userTicket = (String) respBean.getObj();
            System.out.println("create userTicket: " + user.getId());
            String row = user.getId() + "," + userTicket;
            raf.seek(raf.length());
            raf.write(row.getBytes());
            raf.write("\r\n".getBytes());
            System.out.println("write to file: " + user.getId());
        }
        raf.close();
        System.out.println("over");
    }

    private static Connection getConn() throws Exception {
        String url = "jdbc:mysql://Localhost:3306/seckill?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "951esz753";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        return DriverManager.getConnection(url, username, password);
    }

    public static void main(String[] args) throws Exception {
        createUser(5000);
    }
}
