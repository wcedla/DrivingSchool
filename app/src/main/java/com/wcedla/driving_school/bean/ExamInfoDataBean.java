package com.wcedla.driving_school.bean;

import java.util.List;

public class ExamInfoDataBean {

    private List<ExamInfoBean> examInfo;

    public List<ExamInfoBean> getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(List<ExamInfoBean> examInfo) {
        this.examInfo = examInfo;
    }

    public static class ExamInfoBean {
        /**
         * realName : 钟明喆
         * studentNo : 2019002135
         * examProgress : 0
         */

        private String realName;
        private String studentNo;
        private String examProgress;

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

        public String getExamProgress() {
            return examProgress;
        }

        public void setExamProgress(String examProgress) {
            this.examProgress = examProgress;
        }
    }
}
