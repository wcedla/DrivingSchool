package com.wcedla.driving_school.bean;

import java.util.List;

public class StudentDataBean {


    private List<StudentInfoBean> studentInfo;

    public List<StudentInfoBean> getStudentInfo() {
        return studentInfo;
    }

    public void setStudentInfo(List<StudentInfoBean> studentInfo) {
        this.studentInfo = studentInfo;
    }

    public static class StudentInfoBean {
        /**
         * realName : 许晓楠
         * mobile : 17859985768
         * studentNo : 2019002333
         * examProgress : 1
         * studyProgress : 1
         */

        private String realName;
        private String mobile;
        private String studentNo;
        private String examProgress;
        private String studyProgress;
        private String star;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
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

        public String getExamProgress() {
            return examProgress;
        }

        public void setExamProgress(String examProgress) {
            this.examProgress = examProgress;
        }

        public String getStudyProgress() {
            return studyProgress;
        }

        public void setStudyProgress(String studyProgress) {
            this.studyProgress = studyProgress;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }
    }
}
