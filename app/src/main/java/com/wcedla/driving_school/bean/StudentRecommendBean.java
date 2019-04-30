package com.wcedla.driving_school.bean;

import java.util.List;

public class StudentRecommendBean {


    private List<StudentAppraiseBean> studentAppraise;

    public List<StudentAppraiseBean> getStudentAppraise() {
        return studentAppraise;
    }

    public void setStudentAppraise(List<StudentAppraiseBean> studentAppraise) {
        this.studentAppraise = studentAppraise;
    }

    public static class StudentAppraiseBean {
        /**
         * realName : 钟明喆
         * star : 5
         * time : 0
         * headImg : http://192.168.191.1:8080/DrivingSchoolServer/UserHeads/2019002333.jpg
         */

        private String realName;
        private String star;
        private String time;
        private String headImg;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getHeadImg() {
            return headImg;
        }

        public void setHeadImg(String headImg) {
            this.headImg = headImg;
        }
    }
}
