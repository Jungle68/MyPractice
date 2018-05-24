package com.changsukuaidi.www.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class ColleagueBean extends BaseBean implements Parcelable {
    private int type;           // 1、司机  2、快递员
    private String cover;       // 头像
    private String name;        // 名字
    private String phone;       // 电话
    private String address;     // 当前所在位置地址
    private double latitude;    // 当前所在位置纬度
    private double longitude;   // 当前所在位置经度

    public ColleagueBean() {
    }

    public ColleagueBean(int type, String cover, String name, String phone, String address, double latitude, double longitude) {
        this.type = type;
        this.cover = cover;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.cover);
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.address);
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    private ColleagueBean(Parcel in) {
        this.type = in.readInt();
        this.cover = in.readString();
        this.name = in.readString();
        this.phone = in.readString();
        this.address = in.readString();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Parcelable.Creator<ColleagueBean> CREATOR = new Parcelable.Creator<ColleagueBean>() {
        @Override
        public ColleagueBean createFromParcel(Parcel source) {
            return new ColleagueBean(source);
        }

        @Override
        public ColleagueBean[] newArray(int size) {
            return new ColleagueBean[size];
        }
    };
}
