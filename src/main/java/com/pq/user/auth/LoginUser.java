package com.pq.user.auth;


public class LoginUser {
    private String userId;
    private String name;
    private String token;
    private String machineMd5;
    private String posId;
    private Long merchantId;
    private Long branchId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMachineMd5() {
        return machineMd5;
    }

    public void setMachineMd5(String machineMd5) {
        this.machineMd5 = machineMd5;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("LoginUser{");
        sb.append("userId='").append(userId).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", token='").append(token).append('\'');
        sb.append(", machineMd5='").append(machineMd5).append('\'');
        sb.append(", posId='").append(posId).append('\'');
        sb.append('}');
        return sb.toString();
    }


    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }
}
