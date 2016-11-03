package com.yigu.opentable.activity.campaign;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yigu.commom.api.CampaignApi;
import com.yigu.commom.application.AppContext;
import com.yigu.commom.application.ExitApplication;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiImageResult;
import com.yigu.commom.result.MapiItemResult;
import com.yigu.commom.util.RequestCallback;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.adapter.campaign.CompanyJobAdapter;
import com.yigu.opentable.adapter.campaign.PersonJobAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.widget.MainAlertDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonAddActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.name)
    EditText name;
    @Bind(R.id.school)
    EditText school;
    @Bind(R.id.origin)
    EditText origin;
    @Bind(R.id.speciality)
    EditText speciality;
    @Bind(R.id.strong)
    EditText strong;
    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.job)
    LinearLayout job;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<MapiCampaignResult> mList;

    String actid;
    String type;

    PersonJobAdapter mAdapter;

    MapiItemResult itemResult;

    MainAlertDialog deleteDialog;

    private int deletePos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_add);
        ExitApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        if (null != getIntent()) {
            actid = getIntent().getStringExtra("actid");
            type = getIntent().getStringExtra("type");
        }
        if (!TextUtils.isEmpty(actid)) {
            initView();
            initListener();
        }
    }

    private void initView() {
        itemResult = new MapiItemResult();
        itemResult.setActid(actid);
        back.setImageResource(R.mipmap.back);
        center.setText("个人报名");
        tvRight.setText("确定");
        if (type.equals("1") || type.equals("7")) {
            job.setVisibility(View.VISIBLE);
        } else
            job.setVisibility(View.GONE);
        mList = new ArrayList<>();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new PersonJobAdapter(this, mList);
        recyclerView.setAdapter(mAdapter);

        deleteDialog = new MainAlertDialog(this);
        deleteDialog.setLeftBtnText("取消").setRightBtnText("确认").setTitle("确认删除这个岗位?");

    }

    private void initListener(){

        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                deletePos = position;
                deleteDialog.show();

            }
        });

        deleteDialog.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.dismiss();
            }
        });

        deleteDialog.setRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(deletePos>=0){
                    mList.remove(deletePos);
                    mAdapter.notifyItemRemoved(deletePos);
                    mAdapter.notifyItemRangeRemoved(deletePos,mList.size()+1);
                }
                deleteDialog.dismiss();
            }
        });
    }

    @OnClick({R.id.back, R.id.tv_right, R.id.job})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.tv_right:
                String nameStr = name.getText().toString();
                String schoolStr = school.getText().toString();
                String originStr = origin.getText().toString();
                String specialityStr = speciality.getText().toString();
                String strongStr = strong.getText().toString();
                String phoneStr = phone.getText().toString();
                if(TextUtils.isEmpty(nameStr)){
                    MainToast.showShortToast("请输入您的姓名");
                    return;
                }
                if(TextUtils.isEmpty(schoolStr)){
                    MainToast.showShortToast("请输入您毕业的学校");
                    return;
                }
                if(TextUtils.isEmpty(originStr)){
                    MainToast.showShortToast("请输入您的籍贯");
                    return;
                }
                if(TextUtils.isEmpty(specialityStr)){
                    MainToast.showShortToast("请输入您所有的专业");
                    return;
                }
                if(TextUtils.isEmpty(strongStr)){
                    MainToast.showShortToast("请输入您的特长");
                    return;
                }
                if(TextUtils.isEmpty(phoneStr)){
                    MainToast.showShortToast("请输入您的联系方式");
                    return;
                }
                itemResult.setName(nameStr);
                itemResult.setSchool(schoolStr);
                itemResult.setOrigo(originStr);
                itemResult.setMajor(specialityStr);
                itemResult.setSpeciality(strongStr);
                itemResult.setTel(phoneStr);
                getItems();
                showLoading();
                CampaignApi.persign(this, itemResult.getPosts(), userSP.getUserBean().getUSER_ID(), itemResult.getActid(), itemResult.getName(), itemResult.getSchool(), itemResult.getOrigo(),
                        itemResult.getSpeciality(), itemResult.getMajor(), itemResult.getTel(), "", new RequestCallback() {
                            @Override
                            public void success(Object success) {
                                hideLoading();
                                MainToast.showShortToast("报名成功");
                                ExitApplication.getInstance().exit();
                            }
                        }, new RequestExceptionCallback() {
                            @Override
                            public void error(String code, String message) {
                                hideLoading();
                                MainToast.showShortToast(message);
                            }
                        });
                break;
            case R.id.job:
                Intent intent = new Intent(this, SelJobActivity.class);
                intent.putExtra("list",mList);
                intent.putExtra("actid", actid);
                startActivityForResult(intent, RequestCode.job_add);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.job_add:
                    ArrayList<MapiCampaignResult> jobList = (ArrayList<MapiCampaignResult>) data.getSerializableExtra("list");
                    if (null != jobList && !jobList.isEmpty()) {
                        mList.clear();
                        mList.addAll(jobList);
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    }

    private void getItems() {
        String images = "";
        StringBuilder sb = new StringBuilder();
        for (MapiCampaignResult imageResult : mList) {
            if (sb.length() != 0)
                sb.append(",");
            sb.append(imageResult.getId());
        }
        images = sb.toString();
        itemResult.setPosts(images);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", itemResult);
        outState.putSerializable("list", mList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        itemResult = (MapiItemResult) savedInstanceState.getSerializable("item");
        mList = (ArrayList<MapiCampaignResult>) savedInstanceState.getSerializable("list");
    }

}
