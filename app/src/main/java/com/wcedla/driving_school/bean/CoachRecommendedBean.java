package com.wcedla.driving_school.bean;

import java.util.List;

public class CoachRecommendedBean {


    private List<CoachAppraiseBean> coachAppraise;

    public List<CoachAppraiseBean> getCoachAppraise() {
        return coachAppraise;
    }

    public void setCoachAppraise(List<CoachAppraiseBean> coachAppraise) {
        this.coachAppraise = coachAppraise;
    }

    public static class CoachAppraiseBean {
        /**
         * realName : 温龙安
         * driveAge : 3年
         * headImg : http://192.168.191.1:8080/DrivingSchoolServer/UserHeads/2019001016.jpg
         * star : 5
         */

        private String realName;
        private String driveAge;
        private String headImg;
        private String star;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getDriveAge() {
            return driveAge;
        }

        public void setDriveAge(String driveAge) {
            this.driveAge = driveAge;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }
    }
}
