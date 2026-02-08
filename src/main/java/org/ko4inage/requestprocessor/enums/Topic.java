package org.ko4inage.requestprocessor.enums;

public enum Topic {
    SMS {
        @Override
        public String getTopicName() {
            return "sms-events";
        }
    },
    EMAIL{
        @Override
        public String getTopicName() {
            return "email-events";
        }
    },
    PUSH{
        @Override
        public String getTopicName() {
            return "push-events";
        }
    },
    TG_MESSAGE{
        @Override
        public String getTopicName() {
            return "telegram-events";
        }
    };
    public abstract String getTopicName();
}
