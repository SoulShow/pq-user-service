package com.pq.user.dto;

import java.util.List;

public class PraiseDto {
    private List<DynamicPraiseDto> list;
    private int praiseCount;

    public List<DynamicPraiseDto> getList() {
        return list;
    }

    public void setList(List<DynamicPraiseDto> list) {
        this.list = list;
    }

    public int getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(int praiseCount) {
        this.praiseCount = praiseCount;
    }
}