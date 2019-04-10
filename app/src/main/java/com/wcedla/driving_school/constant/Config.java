package com.wcedla.driving_school.constant;

public class Config {

    /**
     * 当前是都是debug模式
     */
    public final static boolean DEBUG=true;

    /**
     * log打印的TAG标志
     */
    public final static String LOG_TAG="wcedlaLog";

    /**
     * 键盘延迟弹出毫秒数
     */
    public final static int INPUTMETHOD_DELAY=500;

    /**
     * 转圈圈动画延迟结束时间
     */
    public final static int DIALOG_DELAY=1000;

    /**
     * 用户名允许输入的正则表达式
     */
    public final static String USERNAME_REGEX="^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";

    /**
     * 密码允许输入的正则表达式
     */
    public final static String PASSWORD_REGEX="^[A-Za-z0-9._*]+$";

    /**
     * 邮箱允许输入的正则表达式
     */
    public final static String EMAIL_REGEX="^[A-Za-z0-9._@]+$";

    /**
     * 邮箱码允许输入的正则表达式
     */
    public final static String EMAIL_CHECK_REGEX="^[0-9]+$";

    /**
     * 邮箱是否合法正则表达式验证
     */
    public final static String EMAIL_LEGAL_REGEX="^(([\\w\\.]+)@([[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|((\\w+\\.?)+)@([a-zA-Z]{2,4}|[0-9]{1,3})(\\.[a-zA-Z]{2,4}))$";

    /**
     * 验证码允许输入的正则表达式
     */
    public final static String YZM_REGEX="^[A-Za-z0-9]+$";

    /**
     * 用户名允许的最长输入长度
     */
    public final static int USERNAME_MAX_LENGTH=12;

    /**
     * 用户名最短应输入的长度
     */
    public final static int USERNAME_MIN_LENGTH=3;

    /**
     * 密码允许的最长输入长度
     */
    public final static int PASSWORD_MAX_LENGTH=15;


    /**
     * 密码最短应输入的长度
     */
    public final static int PASSWORD_MIN_LENGTH=6;

    /**
     * 邮箱允许的最长输入长度
     */
    public final static int EMAIL_MAX_LENGTH=20;

    /**
     * 邮箱码允许的最长输入长度
     */
    public final static int EMAIL_CHECK_MAX_LENGTH=6;

    /**
     * 验证码允许的最长输入长度
     */
    public final static int YZM_MAX_LENGTH=4;

    /**
     * 登录网址
     */
    public final static String LOGIN_URL="http://192.168.191.1:8080/DrivingSchoolServer/UserLogin";

    /**
     * 注册网址
     */
    public final static String REGISTER_URL="http://192.168.191.1:8080/DrivingSchoolServer/UserRegister";

    public final static String GET_EMAIL_URL="http://192.168.191.1:8080/DrivingSchoolServer/GetEmail";

    public final static String RESET_PASSWORD_URL="http://192.168.191.1:8080/DrivingSchoolServer/ResetPassword";

    public final static String AUTHENTCATION_URL="http://192.168.191.1:8080/DrivingSchoolServer/AuthenticationServlet";

    public final static String AUTHENTICSTION_CHECK_URL="http://192.168.191.1:8080/DrivingSchoolServer/GetAuthStatusServlet";

    public final static String AUTHENTICATION_CANCEL_URL="http://192.168.191.1:8080/DrivingSchoolServer/CancelAuthServlet";

    public final static String AUTHENTICAtion_ALL_URL="http://192.168.191.1:8080/DrivingSchoolServer/GetAllAuthServlet";

    public final static String AUTHENTICATION_SET_STATUS_URL="http://192.168.191.1:8080/DrivingSchoolServer/SetAuthStatusServlet";

    public final static String EMAIL_SUBJECT="驾校易管理邮箱码";

    public final static String EMAIL_CODE_TIME="emailcodetime";

    public final static String EMAIL_CODE_TIME_KEY="time";

}
