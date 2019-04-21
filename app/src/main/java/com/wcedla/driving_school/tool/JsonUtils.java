package com.wcedla.driving_school.tool;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.orhanobut.logger.Logger;
import com.wcedla.driving_school.bean.AuthCheckBean;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    public static int getLoginResult(String responseString) {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if ("match".equals(result)) {
            return -1;
        } else if ("mismatch".equals(result)) {
            return 0;
        } else {
            return 1;
        }
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

    public static int getResetStatus(String responseString)
    {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if("ok".equals(result))
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

    public static int getAuthenticationStatus(String responseString)
    {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if("ok".equals(result))
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

    public static AuthCheckBean getAuthCheckStatus(String responseString)
    {
        try {
            return new Gson().fromJson(responseString,AuthCheckBean.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return new AuthCheckBean();
        }

    }

    public static String getMQTTStatus(String message)
    {
        try {
            JSONObject jsonObject = new JSONObject(message);
            return jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static int getMessageSendStatus(String responseString)
    {
        String result = "";
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            result = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if("ok".equals(result))
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

}


