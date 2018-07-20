package me.guanyi.getip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vondear.rxtool.RxNetTool;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

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

    @SuppressLint("SetTextI18n")
    private void upDataUI(Context context){


        if (RxNetTool.isWifi(context)){
            WifiManager wifiManager = (WifiManager) context.getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            System.out.println("wifi信息==="+wifiInfo.toString());
            System.out.println("wifi名称===="+wifiInfo.getSSID());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //  大于等于24即为7.0及以上执行内容
                mTxt.setText(getPhoneIp());
            } else {
                //  低于24即为7.0以下执行内容
                mTxt.setText("WIFI名称: " + wifiInfo.getSSID() + "\n" + "\n" + getPhoneIp());
            }



        }else {
            mTxt.setText("未连接 WIFI");
        }
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



}
