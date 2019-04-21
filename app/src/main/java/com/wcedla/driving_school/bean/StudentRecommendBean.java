package com.wcedla.driving_school.bean;

import java.util.List;

public class StudentRecommendBean {


    private List<StudentBean> student;

    public List<StudentBean> getStudent() {
        return student;
    }

    public void setStudent(List<StudentBean> student) {
        this.student = student;
    }

    public static class StudentBean {
        /**
         * headImg : http://192.168.191.1:8080/DrivingSchoolServer/UserHeads/studentdemo.png
         * star : 4星
         * name : 孙红梅
         * id : 1
         * time : 225小时
         */

        private String headImg;
        private String star;
        private String name;
        private String id;
        private String time;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
