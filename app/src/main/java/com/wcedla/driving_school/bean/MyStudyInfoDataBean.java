package com.wcedla.driving_school.bean;

import java.util.List;

public class MyStudyInfoDataBean {


    private List<AllStudyInfoBean> AllStudyInfo;

    public List<AllStudyInfoBean> getAllStudyInfo() {
        return AllStudyInfo;
    }

    public void setAllStudyInfo(List<AllStudyInfoBean> AllStudyInfo) {
        this.AllStudyInfo = AllStudyInfo;
    }

    public static class AllStudyInfoBean {
        /**
         * realName : 钟明喆
         * studentNo : 2019002135
         * time : 3
         * studyProgress : 1
         */

        private String realName;
        private String studentNo;
        private String time;
        private String studyProgress;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getStudyProgress() {
            return studyProgress;
        }

        public void setStudyProgress(String studyProgress) {
            this.studyProgress = studyProgress;
        }
    }
}
