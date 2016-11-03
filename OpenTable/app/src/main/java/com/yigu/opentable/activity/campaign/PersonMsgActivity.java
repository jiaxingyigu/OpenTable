package com.yigu.opentable.activity.campaign;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.base.BaseActivity;


import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonMsgActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.school)
    TextView school;
    @Bind(R.id.origin)
    TextView origin;
    @Bind(R.id.speciality)
    TextView speciality;
    @Bind(R.id.strong)
    TextView strong;
    @Bind(R.id.phone)
    TextView phone;

    MapiItemResult itemResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_msg);
        ButterKnife.bind(this);

        if(null!=getIntent())
            itemResult = (MapiItemResult)getIntent().getSerializableExtra("item");
        if (null!=itemResult) {
            initView();
        }


    }

    private void initView() {
        back.setImageResource(R.mipmap.back);
        center.setText("个人报名");
        name.setText("姓名："+itemResult.getName());
        school.setText("毕业学校："+itemResult.getSchool());
        origin.setText("籍贯："+itemResult.getOrigo());
        speciality.setText("专业："+itemResult.getMajor());
        strong.setText("特长："+itemResult.getSpeciality());
        phone.setText("联系方式："+itemResult.getTel());

    }

    @OnClick({R.id.back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }


}
