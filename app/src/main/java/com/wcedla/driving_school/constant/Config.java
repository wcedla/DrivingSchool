package com.wcedla.driving_school.constant;

public class Config {

    /**
     * 当前是都是debug模式
     */
    public final static boolean DEBUG = true;

    /**
     * log打印的TAG标志
     */
    public final static String LOG_TAG = "wcedlaLog";

    /**
     * 键盘延迟弹出毫秒数
     */
    public final static int INPUTMETHOD_DELAY = 500;

    /**
     * 转圈圈动画延迟结束时间
     */
    public final static int DIALOG_DELAY = 1000;

    /**
     * 用户名允许输入的正则表达式
     */
    public final static String USERNAME_REGEX = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$";

    /**
     * 密码允许输入的正则表达式
     */
    public final static String PASSWORD_REGEX = "^[A-Za-z0-9._*]+$";

    /**
     * 邮箱允许输入的正则表达式
     */
    public final static String EMAIL_REGEX = "^[A-Za-z0-9._@]+$";

    /**
     * 邮箱码允许输入的正则表达式
     */
    public final static String EMAIL_CHECK_REGEX = "^[0-9]+$";

    /**
     * 邮箱是否合法正则表达式验证
     */
    public final static String EMAIL_LEGAL_REGEX = "^(([\\w\\.]+)@([[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|((\\w+\\.?)+)@([a-zA-Z]{2,4}|[0-9]{1,3})(\\.[a-zA-Z]{2,4}))$";

    /**
     * 验证码允许输入的正则表达式
     */
    public final static String YZM_REGEX = "^[A-Za-z0-9]+$";

    /**
     * 验证手机号是否合法
     */
    public final static String MOBILE_REGEX = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";

    /**
     * 用户名允许的最长输入长度
     */
    public final static int USERNAME_MAX_LENGTH = 12;

    /**
     * 用户名最短应输入的长度
     */
    public final static int USERNAME_MIN_LENGTH = 3;

    /**
     * 密码允许的最长输入长度
     */
    public final static int PASSWORD_MAX_LENGTH = 15;


    /**
     * 密码最短应输入的长度
     */
    public final static int PASSWORD_MIN_LENGTH = 6;

    /**
     * 邮箱允许的最长输入长度
     */
    public final static int EMAIL_MAX_LENGTH = 20;

    /**
     * 邮箱码允许的最长输入长度
     */
    public final static int EMAIL_CHECK_MAX_LENGTH = 6;

    /**
     * 验证码允许的最长输入长度
     */
    public final static int YZM_MAX_LENGTH = 4;

    /**
     * 登录网址
     */
    public final static String LOGIN_URL = "http://192.168.191.1:8080/DrivingSchoolServer/UserLogin";

    /**
     * 注册网址
     */
    public final static String REGISTER_URL = "http://192.168.191.1:8080/DrivingSchoolServer/UserRegister";

    public final static String CHECK_NICK_NAME_EXIST = "http://192.168.191.1:8080/DrivingSchoolServer/GetNickNameExistServlet";

    public final static String GET_EMAIL_URL = "http://192.168.191.1:8080/DrivingSchoolServer/GetEmail";

    public final static String RESET_PASSWORD_URL = "http://192.168.191.1:8080/DrivingSchoolServer/ResetPassword";

    public final static String AUTHENTCATION_URL = "http://192.168.191.1:8080/DrivingSchoolServer/AuthenticationServlet";

    public final static String AUTHENTICSTION_CHECK_URL = "http://192.168.191.1:8080/DrivingSchoolServer/GetAuthStatusServlet";

    public final static String AUTHENTICATION_CANCEL_URL = "http://192.168.191.1:8080/DrivingSchoolServer/CancelAuthServlet";

    public final static String AUTHENTICAtion_ALL_URL = "http://192.168.191.1:8080/DrivingSchoolServer/GetAllAuthServlet";

    public final static String AUTHENTICATION_SET_STATUS_URL = "http://192.168.191.1:8080/DrivingSchoolServer/SetAuthStatusServlet";

    public final static String GET_BANNER_URL = "http://192.168.191.1:8080/DrivingSchoolServer/GetBannerServlet";

    public final static String GET_SKILL_URL = "http://192.168.191.1:8080/DrivingSchoolServer/GetSkillInfoServlet";

    public final static String GET_LAW_URL = "http://192.168.191.1:8080/DrivingSchoolServer/GetLawInfoServlet";

    public final static String GET_MESSAGE_URL = "http://192.168.191.1:8080/DrivingSchoolServer/GetMessageServlet";

    public final static String SEND_MESSAGE_URL = "http://192.168.191.1:8080/DrivingSchoolServer/SendMessageServlet";

    public final static String GET_COACH_RECOMMENDED = "http://192.168.191.1:8080/DrivingSchoolServer/GetCoachRecommendedServlet";

    public final static String GET_STUDENT_RECOMMENDED = "http://192.168.191.1:8080/DrivingSchoolServer/GetStudentRecommendedServlet";

    public final static String GET_STUDENT_INFO = "http://192.168.191.1:8080/DrivingSchoolServer/GetAllStudentServlet";

    public final static String SEND_STUDENT_APPRAISE = "http://192.168.191.1:8080/DrivingSchoolServer/StudentAppraiseServlet";

    public final static String GET_STUDY_INFO = "http://192.168.191.1:8080/DrivingSchoolServer/GetStudyInfoServlet";

    public final static String UPDATE_STUDY_PROGRESS = "http://192.168.191.1:8080/DrivingSchoolServer/UpdateStudyServlet";

    public final static String GET_EXAM_PROGRESS = "http://192.168.191.1:8080/DrivingSchoolServer/GetExamInfoServlet";

    public final static String UPDATE_EXAM_PROGRESS = "http://192.168.191.1:8080/DrivingSchoolServer/UpdateExamServlet";

    public final static String GET_USER_HEAD = "http://192.168.191.1:8080/DrivingSchoolServer/GetUserHeadServlet";

    public final static String UPLOAD_USER_HEAD = "http://192.168.191.1:8080/DrivingSchoolServer/HeadUploadServlet";

    public final static String SET_USER_HEAD = "http://192.168.191.1:8080/DrivingSchoolServer/SetUserHeadServlet";

    public final static String GET_USER_INFO = "http://192.168.191.1:8080/DrivingSchoolServer/GetUserInfoServlet";

    public final static String UPDATE_MOBILE_NUMBER = "http://192.168.191.1:8080/DrivingSchoolServer/UpdateMobileSrvlet";

    public final static String CHECK_PASSWORD = "http://192.168.191.1:8080/DrivingSchoolServer/CheckPasswordServlet";

    public final static String GET_MY_COACH = "http://192.168.191.1:8080/DrivingSchoolServer/GetMyCoachServlet";

    public final static String SEND_COACH_APPRAISE = "http://192.168.191.1:8080/DrivingSchoolServer/CoachAppraiseServlet";

    public final static String GET_ALL_STUDY_INFO = "http://192.168.191.1:8080/DrivingSchoolServer/GetAllStudyInfoServlet";

    public final static String CHECK_FEED = "http://192.168.191.1:8080/DrivingSchoolServer/CheckFeedServlet";

    public final static String CHECK_FEED_TIME = "http://192.168.191.1:8080/DrivingSchoolServer/CheckTimeFeedServlet";

    public final static String SEND_FEED = "http://192.168.191.1:8080/DrivingSchoolServer/SendFeedServlet";

    public final static String GET_MY_EXAM = "http://192.168.191.1:8080/DrivingSchoolServer/GetMyExamServlet";

    public final static String CHECK_EXAM_FEED = "http://192.168.191.1:8080/DrivingSchoolServer/CheckExamFeedServlet";

    public final static String GET_MY_FEED = "http://192.168.191.1:8080/DrivingSchoolServer/GetAllFeedServlet";

    public final static String GET_ALL_USER_INFO = "http://192.168.191.1:8080/DrivingSchoolServer/GetAllUserInfoServlet";

    public final static String DELETE_USER_INFO = "http://192.168.191.1:8080/DrivingSchoolServer/DeleteUserServlet";

    public final static String GET_ALL_FEED_INFO = "http://192.168.191.1:8080/DrivingSchoolServer/GetAllUserFeedServlet";

    public final static String SET_FEED_STATUS = "http://192.168.191.1:8080/DrivingSchoolServer/SetFeedStudyServlet";

    public final static String ADD_USER_INFO = "http://192.168.191.1:8080/DrivingSchoolServer/AddUserServlet";

    public final static String EMAIL_SUBJECT = "驾校易管理邮箱码";

    public final static String EMAIL_CODE_TIME = "emailcodetime";

    public final static String EMAIL_CODE_TIME_KEY = "time";

    public final static String COMPANY_INTRODUCE = "\u3000\u3000维达汽车驾驶学校注册成立于2015年，是武夷山屈指可数的几家驾校之一。建校以来已培养了数万名合格的汽车驾驶员，其中，许多人已成为党政机关领导干部、著名企业家、社会知名人士。尤其是我国航天员、公安民警和在校大学生都把维达驾校作为他们学车的首选。\n"
            + "\u3000\u3000维达驾校始终坚持驾驶安全人命关天，驾校责任重于泰山的理念，潜心研究传授安全驾驶技能的教学方法，把让学员会驾驶技术、顺利取得驾照和固化安全驾驶技能紧密结合，努力实现让学员终身受益的目标。\n"
            + "\u3000\u3000维达驾校在全力保持学员安全驾驶率全市名列前茅的基础上，在价格定位上，充分考虑到多数学员都处于创业准备阶段的实际，采取压缩管理成本、让利于学员等途径，尽力让学员享受到最优惠的价格。为了让学员顺利通过考试，远大驾校在全力保持高通过率的同时，对个别学员也备有“吃小灶”的“暖心”教学方案。远大驾校严格禁止侵犯学员利益的所有行为，精心打造“学员之家”。\n"
            + "\u3000\u3000维达驾校坚持以学员为本，一切为学员着想、热心教学、廉洁教学、全心全意为学员服务。新的远大驾校，将以其雄厚的基础，热情的服务，在新的世纪，欢迎更多的学员前来培训学习，远大人将一如既往的以优质的服务、廉政的教学、快捷的手续为您轻松圆一个学车梦。\n"
            + "\u3000\u3000维达驾校有教练车500余辆，有桑塔纳、富康、爱丽舍等三种车型；有周末班、周一至周五班、快班、贵宾班、学生班、晚班等多种教学模式供学员自由选择。驾校坚持以人为本，教学方面保证学员一人一车一教练，狠抓教练队伍的技能培训和思想道德教育。教学严肃认真，保证及格率；教练员管理严字当头，杜绝“吃、拿、卡、要”，一经发现立即除名\n"
            + "\u3000\u3000维达驾校设有遍布武夷山的班车线路,免费接送学员。\n"
            + "\u3000\u3000维达驾校拥有自己花园般的教练场，应有尽有的训练科目设施醒目地镶嵌在红花绿草中。经常有学员反应：在远大学车，没有想象那么难，还得到了一种轻松愉悦的感受\n"
            + "\u3000\u3000祝愿您拥有远大前程，我们愿意伴您安全驾临！！\n"
            + "\u3000\u3000报名电话：666-666-666-6\n";

}
