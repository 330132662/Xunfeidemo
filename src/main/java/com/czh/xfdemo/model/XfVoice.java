package com.czh.xfdemo.model;

/**
 * Created by LiJianfei on 2016/8/10.
 */
public class XfVoice {
    /**
     * rc : 0
     * operation : ANSWER
     * service : openQA
     * answer : {"type":"T","text":"你太过奖啦~~"}
     * text : 呵呵！
     */

    private int rc;
    private String operation;
    private String service;
    /**
     * type : T
     * text : 你太过奖啦~~
     */

    private AnswerBean answer;
    private String text;

    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public static class AnswerBean {
        private String type;
        private String text;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
