package com.wcedla.driving_school.bean;

import java.util.List;

public class AllUserInfoDataBean {

    private List<CoachUserBean> coachUser;
    private List<StudentUserBean> studentUser;

    public List<CoachUserBean> getCoachUser() {
        return coachUser;
    }

    public void setCoachUser(List<CoachUserBean> coachUser) {
        this.coachUser = coachUser;
    }

    public List<StudentUserBean> getStudentUser() {
        return studentUser;
    }

    public void setStudentUser(List<StudentUserBean> studentUser) {
        this.studentUser = studentUser;
    }

    public static class CoachUserBean {
        /**
         * realName : 吴文翰
         * coachNo : 2006001010
         * driveAge : 20年
         * mobile : 18060949754
         * id : 16
         * nickName : wcedla
         */

        private String realName;
        private String coachNo;
        private String driveAge;
        private String mobile;
        private String id;
        private String nickName;

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

    public static class StudentUserBean {
        /**
         * realName : 钟明喆
         * enrollTime : 2019-01-03
         * nickName : wcedla3
         * mobile : 18059235663
         * studentNo : 2019002135
         * id : 1
         * coach : 2019001016
         */

        private String realName;
        private String enrollTime;
        private String nickName;
        private String mobile;
        private String studentNo;
        private String id;
        private String coach;

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

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCoach() {
            return coach;
        }

        public void setCoach(String coach) {
            this.coach = coach;
        }
    }
}
