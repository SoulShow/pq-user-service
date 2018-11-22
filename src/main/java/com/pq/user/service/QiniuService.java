package com.pq.user.service;

/**
 * Created by sunxiaoyan on 15/12/29.
 */
public interface QiniuService {
    String uploadFile(byte[] data, String platform);

    void deleteFile(String url);
}
