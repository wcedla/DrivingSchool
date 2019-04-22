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
         * realName : 温龙安
         * password : b57ee49cacc7ba5537710fba427c32e6
         * nickName : wcedla
         * authStatus : 审核中
         * id : 4
         * type : 教练
         * email : wcedla@live.com
         */

        private String no;
        private String realName;
        private String password;
        private String nickName;
        private String authStatus;
        private String id;
        private String type;
        private String email;

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAuthStatus() {
            return authStatus;
        }

        public void setAuthStatus(String authStatus) {
            this.authStatus = authStatus;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
