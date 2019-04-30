package com.wcedla.driving_school.tool;

import java.util.ArrayList;
import java.util.List;

public class ExamUtils {

    public static String getExamProgress(String code) {
        if ("0".equals(code)) {
            return "暂未开始";
        } else if ("1".equals(code)) {
            return "科目一预约成功";
        } else if ("2".equals(code)) {
            return "科目一预约失败";
        } else if ("3".equals(code)) {
            return "科目一补考";
        } else if ("4".equals(code)) {
            return "科目一通过";
        } else if ("5".equals(code)) {
            return "科目二预约成功";
        } else if ("6".equals(code)) {
            return "科目二预约失败";
        } else if ("7".equals(code)) {
            return "科目二补考";
        } else if ("8".equals(code)) {
            return "科目二通过";
        } else if ("9".equals(code)) {
            return "科目三预约成功";
        } else if ("10".equals(code)) {
            return "科目三预约失败";
        } else if ("11".equals(code)) {
            return "科目三补考";
        } else if ("12".equals(code)) {
            return "科目三通过";
        } else if ("13".equals(code)) {
            return "科目四预约成功";
        } else if ("14".equals(code)) {
            return "科目四预约失败";
        } else if ("15".equals(code)) {
            return "科目四补考";
        } else if ("16".equals(code)) {
            return "科目四通过";
        } else if ("17".equals(code)) {
            return "取得驾照";
        }
        return "考试进度获取失败";
    }

    public static List<String> examProgressList() {
        List<String> examProgressList = new ArrayList<>();
        examProgressList.add("科目一预约成功");
        examProgressList.add("科目一预约失败");
        examProgressList.add("科目一补考");
        examProgressList.add("科目一通过");
        examProgressList.add("科目二预约成功");
        examProgressList.add("科目二预约失败");
        examProgressList.add("科目二补考");
        examProgressList.add("科目二通过");
        examProgressList.add("科目三预约成功");
        examProgressList.add("科目三预约失败");
        examProgressList.add("科目三补考");
        examProgressList.add("科目三通过");
        examProgressList.add("科目四预约成功");
        examProgressList.add("科目四预约失败");
        examProgressList.add("科目四补考");
        examProgressList.add("科目四通过");
        examProgressList.add("取得驾照");
        return examProgressList;
    }

    public static int getProgressCode(String progressText) {
        if (progressText.equals("科目一预约成功")) {
            return 1;
        } else if (progressText.equals("科目一预约失败")) {
            return 2;
        } else if (progressText.equals("科目一补考")) {
            return 3;
        } else if (progressText.equals("科目一通过")) {
            return 4;
        } else if (progressText.equals("科目二预约成功")) {
            return 5;
        } else if (progressText.equals("科目二预约失败")) {
            return 6;
        } else if (progressText.equals("科目二补考")) {
            return 7;
        } else if (progressText.equals("科目二通过")) {
            return 8;
        } else if (progressText.equals("科目三预约成功")) {
            return 9;
        } else if (progressText.equals("科目三预约失败")) {
            return 10;
        } else if (progressText.equals("科目三补考")) {
            return 11;
        } else if (progressText.equals("科目三通过")) {
            return 12;
        } else if (progressText.equals("科目四预约成功")) {
            return 13;
        } else if (progressText.equals("科目四预约失败")) {
            return 14;
        } else if (progressText.equals("科目四补考")) {
            return 15;
        } else if (progressText.equals("科目四通过")) {
            return 16;
        } else if (progressText.equals("取得驾照")) {
            return 17;
        }
        return 0;
    }
}
