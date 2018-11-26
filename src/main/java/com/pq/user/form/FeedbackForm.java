package com.pq.user.form;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author liutao
 */
public class FeedbackForm implements Serializable {

    @NotBlank(message = "请输入反馈内容")
    @NotNull(message = "请输入反馈内容")
    private String content;

    private String userId;

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
}
