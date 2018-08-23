package com.example.weibotest08_31.acty;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.example.weibotest08_31.R;
import com.example.weibotest08_31.api.DotNetManager;
import com.example.weibotest08_31.utils.MyToast;
import com.example.weibotest08_31.widget.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhouxiaolong on 15/9/7.
 */
public class EditPersonalActy extends BaseActy{
    private DotNetManager dotNetManager;
    private final int EDIT_OK = 0x12;
    private final int EDIT_FAIL = 0x112;
    private TitleBar titieBar;
    private EditText phoneEV;
    private EditText sexEV;
    private EditText ageEV;
    private EditText nameEV;
    private Handler handler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EDIT_OK:
                    MyToast.showToast(EditPersonalActy.this, "修改成功");
                    finish();
                    break;
                case EDIT_FAIL:
                    MyToast.showToast(EditPersonalActy.this, msg.obj.toString());
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acty_editpersonal);

        titieBar=(TitleBar)findViewById(R.id.aep_tb_title);
        titieBar.leftIV.setOnClickListener(this);
        nameEV=(EditText)findViewById(R.id.aep_et_name);
        ageEV=(EditText)findViewById(R.id.aep_et_age);
        sexEV=(EditText)findViewById(R.id.aep_et_sex);
        phoneEV=(EditText)findViewById(R.id.aep_et_phone);
        sexEV.setText(getWeiboApp().getUser().getSex());
        ageEV.setText(getWeiboApp().getUser().getAge());
        nameEV.setText(getWeiboApp().getUser().getName());
        phoneEV.setText(getWeiboApp().getUser().getPhone());
        findViewById(R.id.aep_bt_update).setOnClickListener(this);
        dotNetManager=new DotNetManager();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aep_bt_update:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String backStr=dotNetManager.updatePeopleDetail(getWeiboApp().getUser().getId()+"", nameEV.getText().toString(), sexEV.getText().toString(),
                                ageEV.getText().toString(), phoneEV.getText().toString());
                        try {
                            JSONObject jsonObject=new JSONObject(backStr);
                            if("0".equals(jsonObject.getString("result"))){
                                handler.sendEmptyMessage(EDIT_OK);
                            }
                            else {
                                String message = jsonObject.getString("message");
                                Message msg = Message.obtain();
                                msg.what = EDIT_FAIL;
                                msg.obj = message;
                                handler.sendMessage(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
        }
        super.onClick(v);
    }
}
