package com.wcedla.driving_school.bean;

import java.util.List;

public class AuthInfoBean {


    private List<AuthBean> auth;

    public List<AuthBean> getAuth() {
        return auth;
    }

    public void setAuth(List<AuthBean> auth) {
        this.auth = auth;
    }

    public static class AuthBean {
        /**
         * no : 20154013023
         * userAuth : wcedla
         * name : 温龙安
         * id : 11
         * type : 教练
         * status : 审核中
         */

        private String no;
        private String userAuth;
        private String name;
        private String id;
        private String type;
        private String status;

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getUserAuth() {
            return userAuth;
        }

        public void setUserAuth(String userAuth) {
            this.userAuth = userAuth;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
