package com.wcedla.driving_school.bean;

import java.util.List;

public class ScrollInfoBean {


    private List<SkillBean> skill;

    public List<SkillBean> getSkill() {
        return skill;
    }

    public void setSkill(List<SkillBean> skill) {
        this.skill = skill;
    }

    public static class SkillBean {
        /**
         * contentText : 让车情景相关试题：

         　　1.机动车跟非机动车在窄桥上会车，机动车减速靠右通过。

         　　2.右转弯让左转弯的车先行。

         　　3.下坡车让上坡车先行。

         　　4.非公交车让公交车先行。

         　　5.环型路口进口让出口先行。

         　　6.有障碍让无障碍先行。

         　　7.有让路条件让无让路条件先行。
         * id : 1
         * type : 科目一
         * title : 科目一记不住？试试题型分类记忆！
         */

        private String contentText;
        private String id;
        private String type;
        private String title;

        public String getContentText() {
            return contentText;
        }

        public void setContentText(String contentText) {
            this.contentText = contentText;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
