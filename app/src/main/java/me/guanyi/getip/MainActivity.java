package me.guanyi.getip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mBtn;
    private TextView mTxt;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTxt = findViewById(R.id.id_ip);
        mBtn = findViewById(R.id.id_btn);


        addListener(MainActivity.this);
        upDataUI(MainActivity.this);

    }

    private void addListener(final Context context){
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upDataUI(context);
                Toast.makeText(context,"刷新完成",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void upDataUI(Context context){

        getPermission(Manifest.permission.ACCESS_WIFI_STATE);

        if (getAPNType(context) == 1){
            WifiManager wifiManager= (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            System.out.println("wifi信息："+wifiInfo.toString());
            System.out.println("wifi名称："+wifiInfo.getSSID());
            mTxt.setText(wifiInfo.getSSID() + "\n" + "\n" + getPhoneIp());

        }else {
            mTxt.setText("未连接 WIFI");
        }
    }



    /**
     * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
     *
     * @param context
     * @return
     */
    public static int getAPNType(Context context) {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connMgr != null;
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            assert mTelephony != null;
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G
            } else {
                netType = 3;// 2G
            }
        }
        return netType;
    }


    /**
     * getPhoneIp 获取ip地址
     * @return
     */
    public static String getPhoneIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "没有获取到 IP";
    }

    /**
     * 获取系统权限
     * getPermission
     * @param permission
     */
    public static void getPermission(String permission){
        if(!PermissionUtils.isGranted(permission)){
            PermissionUtils.permission(permission)
                    .rationale(new PermissionUtils.OnRationaleListener() {
                        @Override
                        public void rationale(final ShouldRequest shouldRequest) {
                            PermissionDialogHelper.showRationaleDialog(shouldRequest);
                        }
                    })
                    .callback(new PermissionUtils.FullCallback() {
                        @Override
                        public void onGranted(List<String> permissionsGranted) {
                            //updateAboutPermission();
                            LogUtils.d(permissionsGranted);
                        }

                        @Override
                        public void onDenied(List<String> permissionsDeniedForever,
                                             List<String> permissionsDenied) {
                            if (!permissionsDeniedForever.isEmpty()) {
                                PermissionDialogHelper.showOpenAppSettingDialog();
                            }
                            LogUtils.d(permissionsDeniedForever, permissionsDenied);
                        }
                    })
                    .request();
        }
    }

}
