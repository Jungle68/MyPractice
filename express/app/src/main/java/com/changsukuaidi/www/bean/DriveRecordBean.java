package com.changsukuaidi.www.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class DriveRecordBean extends BaseBean implements Parcelable {
    /**
     * "id": "14",
     * "deviceid": "7",
     * "uid": "43",
     * "stime": "1524649428",
     * "etime": "1524650082",
     * "distance": "0.0",
     * "time": "1524585600",
     * "area1": "中国四川省成都市武侯区G4201(四环路南段)",
     * "area2": "中国四川省成都市武侯区G4201(四环路南段)",
     * "oils": "",
     * "position": {
     * "_id": "5ae0506298657def368b4568",
     * "d_id": 14,
     * "position": "",
     * "time": 1524650082,
     * "id": 3
     * }
     */
    private int id;
    private int deviceid;
    private int uid;
    private String stime;
    private String etime;
    private double distance;
    private String area1;
    private String area2;
    private String oils;
    private Position position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(int deviceid) {
        this.deviceid = deviceid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
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

    public String getOils() {
        return TextUtils.isEmpty(oils) ? "0" : oils;
    }

    public void setOils(String oils) {
        this.oils = oils;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public static class Position implements Parcelable {
        private String _id;
        private int d_id;
        private String position;
        private int id;

        public String get_id() {
            return _id == null ? "" : _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public int getD_id() {
            return d_id;
        }

        public void setD_id(int d_id) {
            this.d_id = d_id;
        }

        public String getPosition() {
            return position == null ? "" : position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeInt(this.d_id);
            dest.writeString(this.position);
            dest.writeInt(this.id);
        }

        public Position() {
        }

        Position(Parcel in) {
            this._id = in.readString();
            this.d_id = in.readInt();
            this.position = in.readString();
            this.id = in.readInt();
        }

        public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
            @Override
            public Position createFromParcel(Parcel source) {
                return new Position(source);
            }

            @Override
            public Position[] newArray(int size) {
                return new Position[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.deviceid);
        dest.writeInt(this.uid);
        dest.writeString(this.stime);
        dest.writeString(this.etime);
        dest.writeDouble(this.distance);
        dest.writeString(this.area1);
        dest.writeString(this.area2);
        dest.writeString(this.oils);
        dest.writeParcelable(this.position, flags);
    }

    public DriveRecordBean() {
    }

    private DriveRecordBean(Parcel in) {
        this.id = in.readInt();
        this.deviceid = in.readInt();
        this.uid = in.readInt();
        this.stime = in.readString();
        this.etime = in.readString();
        this.distance = in.readDouble();
        this.area1 = in.readString();
        this.area2 = in.readString();
        this.oils = in.readString();
        this.position = in.readParcelable(Position.class.getClassLoader());
    }

    public static final Parcelable.Creator<DriveRecordBean> CREATOR = new Parcelable.Creator<DriveRecordBean>() {
        @Override
        public DriveRecordBean createFromParcel(Parcel source) {
            return new DriveRecordBean(source);
        }

        @Override
        public DriveRecordBean[] newArray(int size) {
            return new DriveRecordBean[size];
        }
    };
}

