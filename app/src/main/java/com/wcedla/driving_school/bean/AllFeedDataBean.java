package com.wcedla.driving_school.bean;

import java.util.List;

public class AllFeedDataBean {

    private List<AllFeedInfoBean> AllFeedInfo;

    public List<AllFeedInfoBean> getAllFeedInfo() {
        return AllFeedInfo;
    }

    public void setAllFeedInfo(List<AllFeedInfoBean> AllFeedInfo) {
        this.AllFeedInfo = AllFeedInfo;
    }

    public static class AllFeedInfoBean {
        /**
         * realName : 许晓楠
         * progress : 1
         * studentNo : 2019002333
         * type : 1
         * message : 5
         * status : 审核中
         */

        private String realName;
        private String progress;
        private String studentNo;
        private String type;
        private String message;
        private String status;

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getStudentNo() {
            return studentNo;
        }

        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
