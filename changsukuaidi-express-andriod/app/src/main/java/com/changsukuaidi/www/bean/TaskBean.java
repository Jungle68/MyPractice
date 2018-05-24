package com.changsukuaidi.www.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Describe
 * @Author zhouhao
 * @Date 2018/3/2
 * @Contact 605626708@qq.com
 */

public class TaskBean extends BaseBean implements Parcelable {
    private int status; // 任务状态 ：1、代取件，2、配送中，3、异常件，4、已完成，5、已取消
    private int priority;// 是否是优先件 ：1、是，其他、不是优先件
    private int taskType; // 任务类型：1、药品配送，2、药品中转，3、物料配送等
    private int isTransfer; // 是否是中转：1、是中转，其他、不是中转
    private String startAddress; // 起点
    private double startLatitude; // 起点纬度
    private double startLongitude; // 起点经度
    private String endAddress; // 终点
    private double endLatitude; // 终点纬度
    private double endLongitude; // 终点经度
    private float startDistance; // 距离起点的位置
    private float endDistance; // 距离终点的位置
    private String receiverName; // 收件人姓名
    private String receiverPhone; // 收件人电话
    private String medicineTime; // 出药时间

    public TaskBean() {
    }

    public TaskBean(int status, int priority, int taskType, String startAddress, double startLatitude, double startLongitude, String endAddress, double endLatitude, double endLongitude, float startDistance, float endDistance, String receiverName, String receiverPhone, String medicineTime, int isTransfer) {
        this.status = status;
        this.priority = priority;
        this.taskType = taskType;
        this.startAddress = startAddress;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endAddress = endAddress;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.medicineTime = medicineTime;
        this.isTransfer = isTransfer;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public int getIsTransfer() {
        return isTransfer;
    }

    public void setIsTransfer(int isTransfer) {
        this.isTransfer = isTransfer;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public float getStartDistance() {
        return startDistance;
    }

    public void setStartDistance(float startDistance) {
        this.startDistance = startDistance;
    }

    public float getEndDistance() {
        return endDistance;
    }

    public void setEndDistance(float endDistance) {
        this.endDistance = endDistance;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getMedicineTime() {
        return medicineTime;
    }

    public void setMedicineTime(String medicineTime) {
        this.medicineTime = medicineTime;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status);
        dest.writeInt(this.priority);
        dest.writeInt(this.taskType);
        dest.writeInt(this.isTransfer);
        dest.writeString(this.startAddress);
        dest.writeDouble(this.startLatitude);
        dest.writeDouble(this.startLongitude);
        dest.writeString(this.endAddress);
        dest.writeDouble(this.endLatitude);
        dest.writeDouble(this.endLongitude);
        dest.writeFloat(this.startDistance);
        dest.writeFloat(this.endDistance);
        dest.writeString(this.receiverName);
        dest.writeString(this.receiverPhone);
        dest.writeString(this.medicineTime);
        dest.writeString(this.message);
        dest.writeInt(this.code);
    }

    protected TaskBean(Parcel in) {
        this.status = in.readInt();
        this.priority = in.readInt();
        this.taskType = in.readInt();
        this.isTransfer = in.readInt();
        this.startAddress = in.readString();
        this.startLatitude = in.readDouble();
        this.startLongitude = in.readDouble();
        this.endAddress = in.readString();
        this.endLatitude = in.readDouble();
        this.endLongitude = in.readDouble();
        this.startDistance = in.readFloat();
        this.endDistance = in.readFloat();
        this.receiverName = in.readString();
        this.receiverPhone = in.readString();
        this.medicineTime = in.readString();
        this.message = in.readString();
        this.code = in.readInt();
    }

    public static final Parcelable.Creator<TaskBean> CREATOR = new Parcelable.Creator<TaskBean>() {
        @Override
        public TaskBean createFromParcel(Parcel source) {
            return new TaskBean(source);
        }

        @Override
        public TaskBean[] newArray(int size) {
            return new TaskBean[size];
        }
    };
}
