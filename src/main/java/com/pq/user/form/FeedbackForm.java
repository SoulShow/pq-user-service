package com.pq.user.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author liutao
 */
public class FeedbackForm implements Serializable {

    @NotBlank(message = "请输入反馈内容")
    @NotNull(message = "请输入反馈内容")
    @Size(min = 1, max = 500,message = "反馈内容请限制在500字以内")
    private String content;

    private String userId;

    private List<String> imgList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getImgList() {
        return imgList;
    }

    public void setImgList(List<String> imgList) {
        this.imgList = imgList;
    }
}
