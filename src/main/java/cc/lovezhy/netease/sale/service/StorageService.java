package cc.lovezhy.netease.sale.service;

import cc.lovezhy.netease.sale.util.IdGenerator;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Base64;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class StorageService implements InitializingBean {

    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Value("${qiniu.basicUrl}")
    private String basicUrl;


    private String upToken;
    private UploadManager uploadManager;

    private BucketManager bucketManager;

    private static final ScheduledExecutorService updateTokenService = Executors.newScheduledThreadPool(1);

    @Override
    public void afterPropertiesSet() throws Exception {
        Auth auth = Auth.create(this.accessKey, this.secretKey);
        upToken = auth.uploadToken(bucket);


        updateTokenService.scheduleAtFixedRate(() -> {
            upToken = auth.uploadToken(bucket);
        }, 3000, 3000, TimeUnit.SECONDS);

        Zone z = Zone.zone2();
        Configuration c = new Configuration(z);
        uploadManager = new UploadManager(c);
        bucketManager = new BucketManager(auth, c);
    }

    public String upload(byte[] uploadBytes, String key) throws QiniuException {
        String encodedFileName = Base64.getEncoder().encodeToString(key.getBytes());
        Response response = uploadManager.put(uploadBytes, encodedFileName, upToken);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        return basicUrl + "/" + putRet.key;
    }

    public RemoteFileInfo upload(File file, String plainVideoType) throws Exception {
        String encodedFileName = IdGenerator.newId();
        Response response = uploadManager.put(file, encodedFileName + "." + plainVideoType, upToken);
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);


        RemoteFileInfo remoteFileInfo = new RemoteFileInfo();
        remoteFileInfo.setKey(putRet.key);
        remoteFileInfo.setHash(putRet.hash);
        remoteFileInfo.setUrl(basicUrl + "/" + putRet.key);

        FileInfo fileInfo = bucketManager.stat(bucket, remoteFileInfo.key);
        remoteFileInfo.setMimeType(fileInfo.mimeType);
        remoteFileInfo.setSize(fileInfo.fsize);
        remoteFileInfo.setPutTime(fileInfo.putTime);
        return remoteFileInfo;

    }

    public void deleteFile(String fileName) throws QiniuException {
        bucketManager.delete(bucket, fileName);
    }

    public void updateFileInfo(String fileName, RemoteFileInfo fileInfo) throws Exception {
        if (!StringUtils.isEmpty(fileInfo.mimeType)) {
            bucketManager.changeMime(bucket, fileName, fileInfo.mimeType);
        }
        if (!StringUtils.isEmpty(fileInfo.key)) {
            bucketManager.rename(bucket, fileName, fileInfo.key);
        }
    }

    @Data
    public static class RemoteFileInfo {
        /**
         * 文件名
         */
        private String key;
        /**
         * 前缀url+文件名，可以直接在浏览器中查看
         */
        private String url;
        /**
         * hash值
         */
        private String hash;
        /**
         * 文件大小
         */
        private long size;
        /**
         * 文件类型
         */
        private String mimeType;
        /**
         * 上传时间
         */
        private long putTime;
    }


}
