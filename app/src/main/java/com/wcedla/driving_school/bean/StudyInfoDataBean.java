package com.wcedla.driving_school.bean;

import java.util.List;

public class StudyInfoDataBean {


    private List<StudyInfoBean> studyInfo;

    public List<StudyInfoBean> getStudyInfo() {
        return studyInfo;
    }

    public void setStudyInfo(List<StudyInfoBean> studyInfo) {
        this.studyInfo = studyInfo;
    }

    public static class StudyInfoBean {
        /**
         * studentNo : 2019002135
         * time : 1
         * studyProgress : 1
         */

        private String studentNo;
        private String time;
        private String realName;
        private String studyProgress;

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

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }
    }
}
