package com.wcedla.driving_school.bean;

import java.util.List;

public class MyExamInfoDataBean {


    private List<MyExamInfoBean> myExamInfo;

    public List<MyExamInfoBean> getMyExamInfo() {
        return myExamInfo;
    }

    public void setMyExamInfo(List<MyExamInfoBean> myExamInfo) {
        this.myExamInfo = myExamInfo;
    }

    public static class MyExamInfoBean {
        /**
         * realName : 许晓楠
         * studentNo : 2019002333
         * examProgress : 15
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
