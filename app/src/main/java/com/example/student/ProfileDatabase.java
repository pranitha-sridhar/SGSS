package com.example.student;

public class ProfileDatabase {
    String name;
    String College;
    String AddNo;
    String Dept;
    String Gender;
    String picurl;


    public ProfileDatabase(String name, String college, String addNo, String dept, String gender, String picurl) {
        this.name = name;
        this.College = college;
        this.AddNo = addNo;
        this.Dept = dept;
        this.Gender = gender;
        this.picurl = picurl;

    }

    public String getName() {
        return name;
    }


    public String getCollege() {
        return College;
    }


    public String getAddNo() {
        return AddNo;
    }


    public String getDept() {
        return Dept;
    }


    public String getGender() {
        return Gender;
    }

    public String getPicurl() {
        return picurl;
    }
}
