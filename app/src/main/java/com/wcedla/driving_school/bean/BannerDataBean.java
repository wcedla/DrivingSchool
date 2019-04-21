package com.wcedla.driving_school.bean;

import java.util.List;

public class BannerDataBean {

    private List<BannerBean> banner;

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class BannerBean {
        /**
         * img : http://192.168.191.1:8080/DrivingSchoolServer/banner/banner1.png
         * id : 3
         * text : 驾校易管理
         */

        private String img;
        private String id;
        private String text;

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
