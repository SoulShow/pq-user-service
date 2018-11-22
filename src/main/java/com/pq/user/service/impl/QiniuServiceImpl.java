package com.pq.user.service.impl;

import com.pq.common.util.StringUtil;
import com.pq.user.service.QiniuService;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liutao on 15/12/29.
 */
@Service
public class QiniuServiceImpl implements QiniuService {

    @Value("${qiniu.ak}")
    private String ACCESS_KEY;

    @Value("${qiniu.sk}")
    private String SECRET_KEY;

    @Value("${qiniu.download.domain}")
    private String DOWNLOAD_DOMAIN;

    @Value("${qiniu.download.key}")
    private String DOWNLOAD_KEY;

    @Value("${qiniu.user.avatar.bucket}")
    private String BUCKET;

    private Auth auth = null;

    private String getUpToken(String key) {
        if (auth == null) {
            auth = Auth.create(ACCESS_KEY, SECRET_KEY);
        }
        return auth.uploadToken(BUCKET, key);
    }

    @Override
    public String uploadFile(byte[] data, String platform) {
        if (StringUtil.isEmpty(platform)) {
            return null;
        }
        UploadManager manager = new UploadManager();
        SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
        String key = DOWNLOAD_KEY + platform + "/" + format.format(new Date());
        try {
            manager.put(data, key, getUpToken(key));
        } catch (QiniuException e) {
            e.printStackTrace();
            return null;
        }
        return DOWNLOAD_DOMAIN + key;
    }

    @Override
    public void deleteFile(String url) {
        if (!StringUtil.isBlank(url) && url.length() > DOWNLOAD_DOMAIN.length()) {
            try {
                Auth dummyAuth = Auth.create(ACCESS_KEY, SECRET_KEY);
                BucketManager bucketManager = new BucketManager(dummyAuth);
                bucketManager.delete(BUCKET, url.substring(DOWNLOAD_DOMAIN.length()).trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
