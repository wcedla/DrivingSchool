package com.wcedla.driving_school.bean;

import java.util.List;

public class CoachInfoDataBean {


    private List<CoachInfoBean> coachInfo;

    public List<CoachInfoBean> getCoachInfo() {
        return coachInfo;
    }

    public void setCoachInfo(List<CoachInfoBean> coachInfo) {
        this.coachInfo = coachInfo;
    }

    public static class CoachInfoBean {
        /**
         * realName : 温龙安
         * coachNo : 2019001016
         * driveAge : 3年
         * headImg : http://192.168.191.1:8080/DrivingSchoolServer/UserHeads/2019001016.jpg
         * nickName : wcedla
         * mobile : 18350904693
         */

        private String realName;
        private String coachNo;
        private String driveAge;
        private Object headImg;
        private String nickName;
        private String mobile;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getCoachNo() {
            return coachNo;
        }

        public void setCoachNo(String coachNo) {
            this.coachNo = coachNo;
        }

        public String getDriveAge() {
            return driveAge;
        }

        public void setDriveAge(String driveAge) {
            this.driveAge = driveAge;
        }

        public Object getHeadImg() {
            return headImg;
        }

        public void setHeadImg(Object headImg) {
            this.headImg = headImg;
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
    }
}
