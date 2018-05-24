package com.changsukuaidi.www.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by donglua on 15/6/30.
 */
public class PhotoBean extends BaseBean implements Parcelable {

    private int id;
    private String path;
    private boolean select;

    public PhotoBean(String path) {
        this.path = path;
    }

    public PhotoBean(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public PhotoBean() {
    }

    @Override
    public String toString() {
        return "PhotoBean{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", select=" + select +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoBean)) return false;

        PhotoBean photo = (PhotoBean) o;

        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
        dest.writeInt(this.id);
        dest.writeString(this.path);
        dest.writeByte(this.select ? (byte) 1 : (byte) 0);
    }

    protected PhotoBean(Parcel in) {
        this.id = in.readInt();
        this.path = in.readString();
        this.select = in.readByte() != 0;
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel source) {
            return new PhotoBean(source);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };
}
