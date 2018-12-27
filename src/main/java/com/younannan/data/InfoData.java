package com.younannan.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class InfoData implements Serializable, Comparable<InfoData>{
    private static final long serialVersionUID = -5598711530571769223L;


    public String beginTime;
    public String endTime;
    public String conversationLocation;
    public int typePosition;
    public String times;
    public String name;
    public String idNumber;
    public String sex;
    public String birthDate;
    public String age;
    public String rendadaibiao;
    public String nowAddress;
    public String residenceAddress;
    public String phoneNumber;
    public String authorizeDept;
    public String litigant;

    public ArrayList<String> qaList;


    public long createTimeStamp;//创建时间
    public InfoData() {
        super();
        beginTime = "";
        endTime = "";
        conversationLocation = "";
        typePosition = 0;
        times = "";
        name = "";
        idNumber = "";
        sex = "";
        birthDate = "";
        age = "";
        rendadaibiao = "";
        nowAddress = "";
        residenceAddress = "";
        phoneNumber = "";
        authorizeDept = "";
        litigant = "";
        qaList = new ArrayList<String>();

        createTimeStamp = new Date().getTime();
    }

    //排序算法是时间逆序的
    @Override
    public int compareTo(InfoData other) {
        return (int)(((other.createTimeStamp - this.createTimeStamp) / 1000) & 0xffffffff);
    }
}
