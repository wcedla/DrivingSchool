package com.wcedla.driving_school.tool;

public class ExamUtils {

    public static String getExamProgress(String code) {
        if ("0".equals(code)) {
            return "暂未开始";
        } else if ("1".equals(code)) {
            return "科目一预约成功";
        } else if ("2".equals(code)) {
            return "科目一通过";
        } else if ("3".equals(code)) {
            return "科目二预约成功";
        } else if ("4".equals(code)) {
            return "科目二通过";
        } else if ("5".equals(code)) {
            return "科目三预约成功";
        } else if ("6".equals(code)) {
            return "科目三通过";
        } else if ("7".equals(code)) {
            return "科目四预约成功";
        } else if ("8".equals(code)) {
            return "科目四通过";
        }
        return "考试进度获取失败";
    }
}
