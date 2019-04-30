package com.wcedla.driving_school.bean;

import java.util.List;

public class StudentInfoDataBean {


    private List<StudentInfoBean> studentInfo;

    public List<StudentInfoBean> getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(List<StudentInfoBean> studentInfo) {
        this.studentInfo = studentInfo;
    }

    public static class StudentInfoBean {
        /**
         * realName : 许晓楠
         * enrollTime : 2019-01-02
         * nickName : wcedla2
         * mobile : 17859985768
         * headImg:null
         * studentNo : 2019002333
         */

        private String realName;
        private String enrollTime;
        private String nickName;
        private String mobile;
        private Object headImg;
        private String studentNo;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getEnrollTime() {
            return enrollTime;
        }

        public void setEnrollTime(String enrollTime) {
            this.enrollTime = enrollTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public Object getHeadImg() {
            return headImg;
        }

        public void setHeadImg(Object headImg) {
            this.headImg = headImg;
        }

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }
    }
}
