package com.wcedla.driving_school.bean;

import java.util.List;

public class CoachRecommendedBean {


    private List<CoachBean> coach;

    public List<CoachBean> getCoach() {
        return coach;
    }

    public void setCoach(List<CoachBean> coach) {
        this.coach = coach;
    }

    public static class CoachBean {
        /**
         * headimg : http://192.168.191.1:8080/DrivingSchoolServer/UserHeads/headdemo.png
         * star : 4星
         * name : 李晓明
         * id : 1
         * time : 7年
         */

        private String headimg;
        private String star;
        private String name;
        private String id;
        private String time;

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
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
