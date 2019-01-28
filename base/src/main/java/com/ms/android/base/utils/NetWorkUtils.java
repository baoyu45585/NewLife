package com.ms.android.base.utils;/**
 * Created by del on 17/3/15.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * created by lbw at 17/3/15
 */
public class NetWorkUtils {
    /**
     * 判断是否有网络连接      * @param context      * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用      * @param context      * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mgrConn = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            TelephonyManager mgrTel = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                    .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                    .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用      * @param context      * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 获取当前网络连接的类型信息      * @param context      * @return
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 有线
     * @param context
     * @return
     */
    public static boolean checkEthernet(Context context)
    {
        ConnectivityManager conn =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        return networkInfo.isConnected();
    }

    /**
     * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3      *      * @param context      * @return
     */
    public static int getAPNType(Context context) {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager)
                    context.getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G
            } else {
                netType = 3;// 2G
            }
        }
        return netType;
    }

    public static boolean isInternetConnected(Context context)
    {
        if(isNetworkConnected(context))
        {
            return true;
        }
        if (isWifiConnected(context)) {
                return true;
        }
        if (isMobileConnected(context)) {
                    return true;
         }
        return false;
    }

    public static boolean isNetworkAvailable(Context context) {

        if (context != null) {
            ConnectivityManager mgrConn = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            TelephonyManager mgrTel = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                    .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                    .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
        }
        return false;
//        ConnectivityManager cm = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        if (cm == null) {
//        } else {
//           //如果仅仅是用来判断网络连接
//           //则可以使用 cm.getActiveNetworkInfo().isAvailable();
//            NetworkInfo[] info = cm.getAllNetworkInfo();
//            if (info != null) {
//                for (int i = 0; i < info.length; i++) {
//                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        }
//        if(isNetworkConnected(context))
//        {
//            return true;
//        }
//        if (isWifiConnected(context))
//        {
//            return true;
//        }
//        if(isMobileConnected(context))
//        {
//            return true;
//        }
//        if(checkEthernet(context))
//        {
//            return true;
//        }
//        ToastUtil.show(context,"无网络连接");
//        return false;
    }


    /**
     * 根据IP获取本地Mac
     * @return
     */
    public static String getLocalMacAddressFromIp() {
        String mac_s= "";
        try {
            byte[] mac;
            NetworkInterface ne=NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = byte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    /**
     * 根据IP获取本地Mac
     * @return
     */
    public static String getLocalMac() {
        String mac_s= "";
        try {
            byte[] mac;
            NetworkInterface ne=NetworkInterface.getByInetAddress(InetAddress.getByName(getLocalIpAddress()));
            mac = ne.getHardwareAddress();
            mac_s = setbyte2hex(mac);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mac_s;
    }

    public static  String setbyte2hex(byte[] b) {
        if (b==null)return "";
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (n==len-1){
                if (stmp.length() == 1)
                    hs = hs.append("0").append(stmp);
                else {
                    hs = hs.append(stmp);
                }
            }else{
                if (stmp.length() == 1)
                    hs = hs.append("0").append(stmp+":");
                else {
                    hs = hs.append(stmp+":");
                }
            }

        }
        return String.valueOf(hs);
    }

    public static  String byte2hex(byte[] b) {
        if (b==null)return "";
        StringBuffer hs = new StringBuffer(b.length);
        String stmp = "";
        int len = b.length;
        for (int n = 0; n < len; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            if (stmp.length() == 1)
                hs = hs.append("0").append(stmp);
            else {
                hs = hs.append(stmp);
            }
        }
        return String.valueOf(hs);
    }

    /**
     * 获取本地IP
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }

        return null;
    }
}

