package com.wcedla.driving_school.bean;

import java.util.List;

public class MessageShowBean {


    private List<MessageBean> message;

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * messageTime : 2019-04-08 12:32:25
         * id : 1
         * messageContent : 李教练最帅！
         */

        private String messageTime;
        private String id;
        private String messageContent;

        public String getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(String messageTime) {
            this.messageTime = messageTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMessageContent() {
            return messageContent;
        }

        public void setMessageContent(String messageContent) {
            this.messageContent = messageContent;
        }
    }
}
