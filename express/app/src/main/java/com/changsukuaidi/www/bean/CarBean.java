package com.changsukuaidi.www.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Describe
 * @Author thc
 * @Date 2018/4/24
 */

public class CarBean implements Serializable {

    /**
     * "device": {
     * "id": "7",
     * "title": "标致4008",
     * "memo": "2",
     * "sn": "川A2654",
     * "sort": "22",
     * "status": "1",
     * "last_place": null,
     * "user_id": null,
     * "last_user": "0",
     * "type": "1",
     * "oil_cost": "7.4"
     * },
     * "log": {
     * "id": "5",
     * "deviceid": "7",
     * "uid": "43",
     * "stime": "1524623413",
     * "etime": null,
     * "distance": "0",
     * "time": "1524585600",
     * "area1": "中国四川省成都市武侯区G4201(四环路南段)",
     * "area2": null
     * }
     */
    private Device device;
    @SerializedName("log")
    private PunchInfo punchInfo;

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public PunchInfo getPunchInfo() {
        return punchInfo;
    }

    public void setPunchInfo(PunchInfo punchInfo) {
        this.punchInfo = punchInfo;
    }

    public class PunchInfo implements Serializable {
        private String stime; // 上班打卡时间
        private String etime; // 下班打卡时间
        private String area1; // 上班打卡地点
        private String area2; // 下班打卡地点

        public String getStime() {
            return stime == null ? "" : stime;
        }

        public void setStime(String stime) {
            this.stime = stime;
        }

        public String getEtime() {
            return etime == null ? "" : etime;
        }

        public void setEtime(String etime) {
            this.etime = etime;
        }

        public String getArea1() {
            return area1 == null ? "" : area1;
        }

        public void setArea1(String area1) {
            this.area1 = area1;
        }

        public String getArea2() {
            return area2 == null ? "" : area2;
        }

        public void setArea2(String area2) {
            this.area2 = area2;
        }
    }

    public class Device implements Serializable {
        private String title;
        private String sn;
        private int type;
        private String oil_cost;

        public String getTitle() {
            return title == null ? "" : title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSn() {
            return sn == null ? "" : sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getOil_cost() {
            return oil_cost == null ? "" : oil_cost;
        }

        public void setOil_cost(String oil_cost) {
            this.oil_cost = oil_cost;
        }
    }
}
