package cn.itcast.util;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ImageUploadUtils {

    private static String accessKey = "COuoDRVa7JLsuurzIvQSI_pEDceHDw3yGfJEmvwv";
    private static String secretKey = "3RWpTjB5Jxg3QosUFr4mxbHXJ5JR2m6AHQqYsSlr";
    private static String bucket = "saas31";
    private static String imageUrl = "http://qevqo93as.hd-bkt.clouddn.com/";


    /**
     * 将图片资源的byte数组上传到七牛云服务器
     *  参数：图片的byte数组
     *  返回值：访问地址
     */
    public String upload(byte[] uploadBytes) {
        String url = "";
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.region0());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = null;
        try {
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(uploadBytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                url = imageUrl + putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return url;
    }

    public static void main(String[] args) throws Exception {
        File file = new File("D:\\logo.png");
        InputStream input = new FileInputStream(file);
        byte[] byt = new byte[input.available()];
        input.read(byt);
        //调用工具类上传
        String url = new ImageUploadUtils().upload(byt);
        System.out.println(url);
    }
}
