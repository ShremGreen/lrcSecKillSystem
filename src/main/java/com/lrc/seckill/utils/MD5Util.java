package com.lrc.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    /***
     * 第一次加密，前端表单获取到的密码进行加密。
     * 防止前端脚本恶意获取密码
     * salt获取和md5由前端完成
     * @param password
     * @return
     */
    public static String frontendMD5(String password) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + password + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    /***
     * 第二次加密，后端存入数据库时需要再次加密
     * 防止数据库泄露带来的密码泄露
     * salt随机生成
     * @param password
     * @param salt
     * @return
     */
    public static String storageMD5(String password, String salt) {
        String str = "" + salt.charAt(0) + salt.charAt(2) + password + salt.charAt(5) + salt.charAt(4);;
        return md5(str);
    }

    public static String frontendToStorageMD5(String password, String salt) {
        String frPass = frontendMD5(password);
        String dbPass = storageMD5(frPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {
        System.out.println(frontendMD5("951esz753"));//ce21b747de5af71ab5c2e20ff0a60eea
        System.out.println(storageMD5("52b9dbabea0e8a5ade2108e2d2a4dafd", salt));//0687f9701bca74827fcefcd7e743d179
        System.out.println(frontendToStorageMD5("951esz753", salt));
    }
}
