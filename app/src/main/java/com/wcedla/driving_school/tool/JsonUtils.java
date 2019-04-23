package com.wcedla.driving_school.tool;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.bean.AuthCheckBean;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    public static int getLoginResult(String responseString) {
        String result = "";
        String auth = "";
        String type="";
        String no="";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
            auth = jsonObject.getString("auth");
            type=jsonObject.getString("type");
            no=jsonObject.getString("no");
        } catch (JSONException e) {
            e.printStackTrace();
            return 1;
        }
        if ("match".equals(result)) {

            if (auth.length() > 0) {
                if ("审核通过".equals(auth)) {
                    Hawk.put("loginType", type);
                    Hawk.put("loginNo", no);
                    return -1;
                } else {
                    return 0;
                }
            }
        } else {
            return 1;
        }
        return 1;
    }

    public static int getRegisterResult(String responseString) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("ok".equals(result)) {
            return -1;
        } else if ("Account Exist".equals(result)) {
            return 0;
        } else {
            return 1;
        }
    }

    public static int getNickNameStatus(String responseString) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("none".equals(result)) {
            return -1;
        } else if ("exist".equals(result)) {
            return 0;
        } else {
            return 1;
        }
    }

    public static String getEmailResult(String responseString) {
        JSONObject jsonObject = null;
        String result = "";
        String email = "";
        try {
            jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("ok".equals(result)) {
            try {
                if (jsonObject != null) {
                    email = jsonObject.getString("email");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return email;

        } else {
            return email;
        }
    }

    public static int getResetStatus(String responseString) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("ok".equals(result)) {
            return -1;
        } else {
            return 1;
        }
    }

    public static int getAuthenticationStatus(String responseString) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("ok".equals(result)) {
            return -1;
        } else {
            return 1;
        }
    }

    public static AuthCheckBean getAuthCheckStatus(String responseString) {
        try {
            return new Gson().fromJson(responseString, AuthCheckBean.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new AuthCheckBean();
        }

    }

    public static String getMQTTStatus(String message) {
        try {
            JSONObject jsonObject = new JSONObject(message);
            return jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static int getMessageSendStatus(String responseString) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("ok".equals(result)) {
            return -1;
        } else {
            return 1;
        }
    }

}


