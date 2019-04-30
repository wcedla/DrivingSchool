package com.wcedla.driving_school.bean;

import java.util.List;

public class MyCoachInfoDataBean {


    private List<CoachInfoBean> coachInfo;

    public List<CoachInfoBean> getCoachInfo() {
        return coachInfo;
    }

    public void setCoachInfo(List<CoachInfoBean> coachInfo) {
        this.coachInfo = coachInfo;
    }

    public static class CoachInfoBean {
        /**
         * appraiseCount : 3
         * realName : 温龙安
         * coachNo : 2019001016
         * driveAge : 3年
         * star : 5.0
         * mobile : 18350904693
         * appraiseNo : done
         */

        private String appraiseCount;
        private String realName;
        private String coachNo;
        private String driveAge;
        private String star;
        private String mobile;
        private String appraiseNo;

        public String getAppraiseCount() {
            return appraiseCount;
        }

        public void setAppraiseCount(String appraiseCount) {
            this.appraiseCount = appraiseCount;
        }

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

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAppraiseNo() {
            return appraiseNo;
        }

        public void setAppraiseNo(String appraiseNo) {
            this.appraiseNo = appraiseNo;
        }
    }
}
