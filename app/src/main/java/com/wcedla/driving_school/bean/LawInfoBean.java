package com.wcedla.driving_school.bean;

import java.util.List;

public class LawInfoBean {


    private List<LawBean> law;

    public List<LawBean> getLaw() {
        return law;
    }

    public void setLaw(List<LawBean> law) {
        this.law = law;
    }

    public static class LawBean {
        /**
         * img : http://192.168.191.1:8080/DrivingSchoolServer/lawinfo/law2019.png
         * contentText : wcedla
         * id : 2
         * title : 2019年驾照新政策出台
         */

        private String img;
        private String contentText;
        private String id;
        private String title;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getContentText() {
            return contentText;
        }

        public void setContentText(String contentText) {
            this.contentText = contentText;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
