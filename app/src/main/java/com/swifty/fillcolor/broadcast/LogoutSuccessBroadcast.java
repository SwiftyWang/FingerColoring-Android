package com.swifty.fillcolor.broadcast;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Swifty on 2015/10/4.
 */
public class LogoutSuccessBroadcast {
    private static LogoutSuccessBroadcast ourInstance = new LogoutSuccessBroadcast();

    public static LogoutSuccessBroadcast getInstance() {
        return ourInstance;
    }

    private LogoutSuccessBroadcast() {
    }

    public void sendBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction("userLoginAction");
        intent.putExtra("msg", "logoutsuccess");
        context.sendBroadcast(intent);
    }
}
