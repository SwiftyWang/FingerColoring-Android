package com.swifty.fillcolor.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.swifty.fillcolor.MyApplication;
import com.swifty.fillcolor.broadcast.LoginSuccessBroadcast;
import com.swifty.fillcolor.broadcast.LogoutSuccessBroadcast;
import com.swifty.fillcolor.controller.main.ThemeListFragment;
import com.swifty.fillcolor.controller.main.UserFragment;
import com.swifty.fillcolor.factory.MyDialogFactory;
import com.swifty.fillcolor.factory.SharedPreferencesFactory;
import com.swifty.fillcolor.listener.OnLoginSuccessListener;
import com.swifty.fillcolor.R;
import com.swifty.fillcolor.model.bean.UserBean;
import com.swifty.fillcolor.view.MyProgressDialog;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.facebook.controller.UMFacebookHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Swifty.Wang on 2015/9/10.
 */
public class UmengLoginUtil {
    private static UMSocialService mController;
    private static OnLoginSuccessListener mOnLoginSuccessListener;
    private static UmengLoginUtil ourInstance;
    private AsyncTask asyncTask;

    public void serverBackgroundLogin(OnLoginSuccessListener mOnLoginSuccessListener) {
        this.mOnLoginSuccessListener = mOnLoginSuccessListener;
        asyncTask = new LoginAsyn();
        asyncTask.execute();
    }

    public void loginSuccessEvent(Context context, UserBean userBean, MyDialogFactory myDialogFactory) {
        if (userBean.getUsers() != null) {
            Toast.makeText(context, context.getString(R.string.loginSuccess), Toast.LENGTH_SHORT).show();
            MyApplication.user = userBean.getUsers();
            SharedPreferencesFactory.saveString(context, SharedPreferencesFactory.USERSESSION, MyApplication.user.getToken());
            MyApplication.userToken = MyApplication.user.getToken();
            myDialogFactory.dismissDialog();
            LoginSuccessBroadcast.getInstance().sendBroadcast(context);
        } else {
            Toast.makeText(context, context.getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
        }
    }

    public void loginSuccessEvent(Context context, UserBean userBean) {
        if (userBean.getUsers() != null) {
            Toast.makeText(context, context.getString(R.string.loginSuccess), Toast.LENGTH_SHORT).show();
            MyApplication.user = userBean.getUsers();
            SharedPreferencesFactory.saveString(context, SharedPreferencesFactory.USERSESSION, MyApplication.user.getToken());
            MyApplication.userToken = MyApplication.user.getToken();
            LoginSuccessBroadcast.getInstance().sendBroadcast(context);
        } else {
            Toast.makeText(context, context.getString(R.string.loginFailed), Toast.LENGTH_SHORT).show();
        }
    }

    public enum LoginMethod {
        QQ,
        FACEBOOK,
    }

    public static UmengLoginUtil getInstance() {
        if (ourInstance == null)
            ourInstance = new UmengLoginUtil();
        return ourInstance;
    }

    private UmengLoginUtil() {
    }

    public UMSocialService qqLogin(final Context context, OnLoginSuccessListener onLoginSuccessListener) {
        mOnLoginSuccessListener = onLoginSuccessListener;
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "1104727259",
                "NddWVIV3BNdRqh97");
        qqSsoHandler.addToSocialSDK();
        MyProgressDialog.show(context, null, context.getString(R.string.pullqqloging));
        mController.doOauthVerify(context, SHARE_MEDIA.QQ, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(context, "授权错误", Toast.LENGTH_SHORT).show();
                MyProgressDialog.DismissDialog();
            }

            @Override
            public void onComplete(final Bundle value, SHARE_MEDIA platform) {
                StringBuilder sb = new StringBuilder();
                Set<String> keys = value.keySet();
                for (String key : keys) {
                    sb.append(key + "=" + value.get(key).toString() + "\r\n");
                }
                L.d("TestData2", sb.toString());
                //获取相关授权信息
                mController.getPlatformInfo(context, SHARE_MEDIA.QQ, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for (String key : keys) {
                                sb.append(key + "=" + info.get(key).toString() + "\r\n");
                            }
                            //do register or login
                            registerToServer(LoginMethod.QQ, value, info);
                            L.d("TestData", sb.toString());
                        } else {
                            L.d("TestData", "发生错误：" + status);
                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
                MyProgressDialog.DismissDialog();
            }
        });
        return mController;
    }

    private void registerToServer(LoginMethod loginMethod, Bundle bundle, Map<String, Object> info) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String ret;
        switch (loginMethod) {
            case QQ:
                params.clear();
                params.add(new BasicNameValuePair("type", "qq"));
                params.add(new BasicNameValuePair("uid", bundle.getString("uid")));
                params.add(new BasicNameValuePair("usericon", info.get("profile_image_url").toString()));
                params.add(new BasicNameValuePair("gender", convertGender(info.get("gender").toString())));
                params.add(new BasicNameValuePair("location", info.get("province").toString() + " " + info.get("city").toString()));
                params.add(new BasicNameValuePair("name", info.get("screen_name").toString()));
                asyncTask = new RegisterAsyn();
                asyncTask.execute(params);
                break;
            case FACEBOOK:
                params.clear();
                params.add(new BasicNameValuePair("type", "facebook"));
                params.add(new BasicNameValuePair("uid", info.get("id").toString()));
                params.add(new BasicNameValuePair("usericon", info.get("profilePictureUri").toString()));
                params.add(new BasicNameValuePair("name", info.get("name").toString()));
                asyncTask = new RegisterAsyn();
                asyncTask.execute(params);
                break;
        }
    }

    private String convertGender(String gender) {
        if (gender == null) {
            return null;
        }
        if (gender.toLowerCase().trim().equals("male") || gender.toLowerCase().trim().equals("男")) {
            return "M";
        } else {
            return "F";
        }
    }

    public UMSocialService faceBookLogin(final Context context, OnLoginSuccessListener onLoginSuccessListener) {
        mOnLoginSuccessListener = onLoginSuccessListener;
        mController = UMServiceFactory.getUMSocialService("com.umeng.login");
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMFacebookHandler mFacebookHandler = new UMFacebookHandler((Activity) context);
        mFacebookHandler.addToSocialSDK();

        MyProgressDialog.show(context, null, context.getString(R.string.pullfacebookloging));
        mController.doOauthVerify(context, SHARE_MEDIA.FACEBOOK, new SocializeListeners.UMAuthListener() {
            @Override
            public void onStart(SHARE_MEDIA platform) {
            }

            @Override
            public void onError(SocializeException e, SHARE_MEDIA platform) {
                Toast.makeText(context, "授权错误", Toast.LENGTH_SHORT).show();
                MyProgressDialog.DismissDialog();
            }

            @Override
            public void onComplete(final Bundle value, SHARE_MEDIA platform) {
                StringBuilder sb = new StringBuilder();
                Set<String> keys = value.keySet();
                for (String key : keys) {
                    sb.append(key + "=" + value.get(key).toString() + "\r\n");
                }
                L.d("TestData2", sb.toString());
                //获取相关授权信息
                mController.getPlatformInfo(context, SHARE_MEDIA.FACEBOOK, new SocializeListeners.UMDataListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(int status, Map<String, Object> info) {
                        if (status == 200 && info != null) {
                            StringBuilder sb = new StringBuilder();
                            Set<String> keys = info.keySet();
                            for (String key : keys) {
                                sb.append(key + "=" + info.get(key).toString() + "\r\n");
                            }
                            //do register or login
                            registerToServer(LoginMethod.FACEBOOK, value, info);
                            L.d("TestData", sb.toString());
                        } else {
                            L.d("TestData", "发生错误：" + status);
                        }
                    }
                });
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(context, "授权取消", Toast.LENGTH_SHORT).show();
                MyProgressDialog.DismissDialog();
            }
        });
        return mController;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void logout(Context mContext) {
        MyApplication.user = null;
        SharedPreferencesFactory.saveString(mContext, SharedPreferencesFactory.USERSESSION, null);
        LogoutSuccessBroadcast.getInstance().sendBroadcast(mContext);
    }

    class RegisterAsyn extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            MyHttpClient myHttpClient = new MyHttpClient();
            List<NameValuePair> params = (List<NameValuePair>) objects[0];
            String ret = myHttpClient.executePostRequest(MyApplication.UserRegisterUrl, params);
            L.e(ret);
            Gson gson = new Gson();
            UserBean userBean = gson.fromJson(ret, UserBean.class);
            return userBean;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            MyProgressDialog.DismissDialog();
            if (UserFragment.getInstance().isAdded() && mOnLoginSuccessListener != null) {
                mOnLoginSuccessListener.onLoginSuccess((UserBean) o);
            }
        }
    }

    class LoginAsyn extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MyHttpClient myHttpClient = new MyHttpClient();
            String ret;
            UserBean userBean = null;
            try {
                ret = myHttpClient.executeGetRequest(MyApplication.UserLoginUrl);
                L.e(ret);
                Gson gson = new Gson();
                userBean = gson.fromJson(ret, UserBean.class);
                if (userBean.getUsers() != null) {
                    MyApplication.user = userBean.getUsers();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return userBean;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (mOnLoginSuccessListener != null) {
                mOnLoginSuccessListener.onLoginSuccess((UserBean) o);
            }
        }
    }
}
