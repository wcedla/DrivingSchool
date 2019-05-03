package com.wcedla.driving_school.tool;

import java.util.ArrayList;
import java.util.List;

public class StudyUtils {

    public static String getStudyProgress(String code) {
        if ("0".equals(code)) {
            return "暂未开始";
        } else if ("1".equals(code)) {
            return "熟悉车辆";
        } else if ("2".equals(code)) {
            return "控制车辆";
        } else if ("3".equals(code)) {
            return "坡道定点停车";
        } else if ("4".equals(code)) {
            return "侧方停车";
        } else if ("5".equals(code)) {
            return "S型曲线";
        } else if ("6".equals(code)) {
            return "直角转弯";
        } else if ("7".equals(code)) {
            return "倒车入库";
        } else if ("8".equals(code)) {
            return "科目二全程";
        } else if ("9".equals(code)) {
            return "灯光控制";
        } else if ("10".equals(code)) {
            return "速度与档位匹配";
        } else if ("11".equals(code)) {
            return "直线行驶";
        } else if ("12".equals(code)) {
            return "靠边停车";
        } else if ("13".equals(code)) {
            return "科目三全程";
        } else if ("14".equals(code)) {
            return "全部课程学习完成";
        }
        return "获取学习进度出错";
    }

    public static List<String> studyProgressList() {
        List<String> studyProgressList = new ArrayList<>();
        studyProgressList.add("熟悉车辆");
        studyProgressList.add("控制车辆");
        studyProgressList.add("坡道定点停车");
        studyProgressList.add("侧方停车");
        studyProgressList.add("S型曲线");
        studyProgressList.add("直角转弯");
        studyProgressList.add("倒车入库");
        studyProgressList.add("科目二全程");
        studyProgressList.add("灯光控制");
        studyProgressList.add("速度与档位匹配");
        studyProgressList.add("直线行驶");
        studyProgressList.add("靠边停车");
        studyProgressList.add("科目三全程");
        studyProgressList.add("全部课程学习完成");
        return studyProgressList;
    }

    public static String getStudyMinTime(String type) {
        if ("熟悉车辆".equals(type)) {
            return "3";
        } else if ("控制车辆".equals(type)) {
            return "3";
        } else if ("坡道定点停车".equals(type)) {
            return "12";
        } else if ("侧方停车".equals(type)) {
            return "21";
        } else if ("S型曲线".equals(type)) {
            return "15";
        } else if ("直角转弯".equals(type)) {
            return "12";
        } else if ("倒车入库".equals(type)) {
            return "30";
        } else if ("科目二全程".equals(type)) {
            return "30";
        } else if ("灯光控制".equals(type)) {
            return "6";
        } else if ("速度与档位匹配".equals(type)) {
            return "30";
        } else if ("直线行驶".equals(type)) {
            return "12";
        } else if ("靠边停车".equals(type)) {
            return "21";
        } else if ("科目三全程".equals(type)) {
            return "30";
        } else if ("全部课程学习完成".equals(type)) {
            return "0";
        }
        return "";
    }

    public static int getStudyProgressLevel(String progress) {
        if ("熟悉车辆".equals(progress)) {
            return 0;
        } else if ("控制车辆".equals(progress)) {
            return 1;
        } else if ("坡道定点停车".equals(progress)) {
            return 2;
        } else if ("侧方停车".equals(progress)) {
            return 3;
        } else if ("S型曲线".equals(progress)) {
            return 4;
        } else if ("直角转弯".equals(progress)) {
            return 5;
        } else if ("倒车入库".equals(progress)) {
            return 6;
        } else if ("科目二全程".equals(progress)) {
            return 7;
        } else if ("灯光控制".equals(progress)) {
            return 8;
        } else if ("速度与档位匹配".equals(progress)) {
            return 9;
        } else if ("直线行驶".equals(progress)) {
            return 10;
        } else if ("靠边停车".equals(progress)) {
            return 11;
        } else if ("科目三全程".equals(progress)) {
            return 12;
        } else if ("全部课程学习完成".equals(progress)) {
            return 13;
        }
        return -1;
    }
}
