package cn.jiguang.demo;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.tencent.mmkv.MMKV;

import org.w3c.dom.Text;

import cn.jiguang.demo.baselibrary.ClipUtils;
import cn.jiguang.demo.baselibrary.ToastHelper;
import cn.jiguang.demo.jpush.ExampleUtil;
import cn.jpush.android.api.JPushInterface;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private ToggleButton toggle;
    private TextView tvOnline;
    private TextView tvRid;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        getContext().registerReceiver(reciver, new IntentFilter("com.jiguang.demo.message"));
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.jpush_demo_activity_push, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.tv_notify).setOnClickListener(this);
        view.findViewById(R.id.tv_adv).setOnClickListener(this);
        view.findViewById(R.id.iv_info).setOnClickListener(this);
        view.findViewById(R.id.iv_back).setOnClickListener(this);
        toggle = view.findViewById(R.id.toggle);
        toggle.setOnClickListener(this);
        boolean checked = MMKV.defaultMMKV().getBoolean("PushOnline", true);
        toggle.setChecked(checked);

        tvOnline = view.findViewById(R.id.tv_online);
        updateStatu();
        tvRid = view.findViewById(R.id.tv_rid_desc);

        String registrationID = JPushInterface.getRegistrationID(getContext().getApplicationContext());
        tvRid.setText(registrationID);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.toggle) {
            updateStatu();
            ToastHelper.showOk(getContext().getApplicationContext(), getString(R.string.toast_modify_ok));
        } else if (id == R.id.iv_info) {
            ClipUtils.copyText(v.getContext(), tvRid.getText().toString());
        } else if (id == R.id.tv_notify) {

            if (toggle.isChecked()) {
                if (JPushInterface.isNotificationEnabled(getContext())==1) {
                    ExampleUtil.buildLocalNotification(getContext().getApplicationContext(), getString(R.string.app_name), "这是一条测试消息。");
                } else {
                    pushPermissionDialog();
                }
            } else {
                ToastHelper.showOther(getContext().getApplicationContext(), "不接收通知，不发送本地通知请求");
            }
        } else if (id == R.id.tv_adv) {
            startActivity(new Intent(getContext(), cn.jiguang.demo.jpush.AdvActivity.class));
        }
    }

    private void pushPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dv = View.inflate(getContext(), R.layout.d_dialog_two_button, null);
        TextView tvOk = (TextView) dv.findViewById(R.id.btn_ok);
        TextView tvCancel = (TextView) dv.findViewById(R.id.btn_cancel);
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setContentView(dv);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JPushInterface.goToAppNotificationSettings(getContext());
                dialog.dismiss();
            }
        });
    }

    private final BroadcastReceiver reciver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(!TextUtils.isEmpty(intent.getStringExtra("rid"))&&tvRid!=null){
                tvRid.setText(intent.getStringExtra("rid"));
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dv = View.inflate(context, R.layout.d_dialog_msg, null);
            TextView tvOk = (TextView) dv.findViewById(R.id.btn_ok);
            TextView msg = (TextView) dv.findViewById(R.id.msg);
            msg.setText(intent.getStringExtra("msg"));
            final Dialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setContentView(dv);
            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    };

    private void updateStatu(){
        boolean checked = toggle.isChecked();
        tvOnline.setText(checked ? "接收" : "不接收");
        if(checked){
            JPushInterface.resumePush(getContext());
        }else{
            JPushInterface.stopPush(getContext());
        }
        MMKV.defaultMMKV().putBoolean("PushOnline", checked);
    }

    @Override
    public void onDestroyView() {
        getContext().unregisterReceiver(reciver);
        super.onDestroyView();
    }
}
