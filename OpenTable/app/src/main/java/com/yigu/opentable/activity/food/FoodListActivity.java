package com.yigu.opentable.activity.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yigu.commom.api.BasicApi;
import com.yigu.commom.api.OrderApi;
import com.yigu.commom.result.MapiCampaignResult;
import com.yigu.commom.result.MapiOrderResult;
import com.yigu.commom.util.FileUtil;
import com.yigu.commom.util.RequestExceptionCallback;
import com.yigu.commom.util.RequestPageCallback;
import com.yigu.commom.widget.MainToast;
import com.yigu.opentable.R;
import com.yigu.opentable.activity.SearchCampaignActivity;
import com.yigu.opentable.adapter.tenant.TenantListAdapter;
import com.yigu.opentable.base.BaseActivity;
import com.yigu.opentable.base.RequestCode;
import com.yigu.opentable.interfaces.RecyOnItemClickListener;
import com.yigu.opentable.util.ControllerUtil;
import com.yigu.opentable.widget.BestSwipeRefreshLayout;
import com.yigu.opentable.widget.FoodTypeDialog;
import com.yigu.opentable.widget.ShareDialog;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoodListActivity extends BaseActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.center)
    TextView center;
    @Bind(R.id.unit_name)
    EditText searchEt;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.swipRefreshLayout)
    BestSwipeRefreshLayout swipRefreshLayout;
    @Bind(R.id.tv_right)
    TextView tvRight;
    @Bind(R.id.clear_iv)
    ImageView clearIv;

    private List<MapiOrderResult> mList;
    TenantListAdapter mAdapter;

    private Integer pageIndex=0;
    private Integer pageSize = 8;
    private Integer ISNEXT = 1;

    MapiCampaignResult campaignResult;
    String companyId = "  ";
    private DbManager db;

    FoodTypeDialog foodTypeDialog;
    int selPos = -1;
    String USERNAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        ButterKnife.bind(this);
        initView();
        initListener();
        load();
    }

    private void initView() {

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("open").setDbDir(new File(FileUtil.getFolderPath(this,FileUtil.TYPE_DB)));
        db = x.getDb(daoConfig);

        if (foodTypeDialog == null)
            foodTypeDialog = new FoodTypeDialog(this, R.style.image_dialog_theme);

        companyId  = TextUtils.isEmpty(userSP.getUserBean().getCOMPANY())?"  ":userSP.getUserBean().getCOMPANY();
        back.setImageResource(R.mipmap.back);
        center.setText("美食坊");
        tvRight.setText("切换单位");
        tvRight.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.down_white,0);

        mList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        mAdapter = new TenantListAdapter(this,mList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener(){

        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//EditorInfo.IME_ACTION_SEARCH、EditorInfo.IME_ACTION_SEND等分别对应EditText的imeOptions属性
                    //TODO回车键按下时要执行的操作
                    String keyWord = searchEt.getText().toString().trim();
                    USERNAME = keyWord;
                    refreshData();
                }
                return true;
            }
        });

        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count,
                                          int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    clearIv.setVisibility(View.VISIBLE);
                } else {
                    clearIv.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        swipRefreshLayout.setOnRefreshListener(new BestSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });
        mAdapter.setRecyOnItemClickListener(new RecyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                selPos = position;
                foodTypeDialog.showDialog();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if ((newState == RecyclerView.SCROLL_STATE_IDLE) && manager.findLastVisibleItemPosition()>=0&&(manager.findLastVisibleItemPosition() == (manager.getItemCount() - 1))) {
                    loadNext();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        foodTypeDialog.setDialogItemClickListner(new FoodTypeDialog.DialogItemClickListner() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0://打包
                        if(selPos>=0) {
                            MapiOrderResult orderResult = mList.get(selPos);
                            orderResult.setCompanyId(companyId);
                            ControllerUtil.go2FoodMenu(orderResult);
                        }
                        break;
                    case 1://订座
                        if(selPos>=0) {
                            MapiOrderResult orderResult = mList.get(selPos);
                            orderResult.setCompanyId(companyId);
                            ControllerUtil.go2FoodOrder(orderResult);
                        }
                        break;
                }
            }
        });
    }

    private void load(){
        showLoading();
        OrderApi.getWorkersHomelist(this, companyId, pageIndex + "", pageSize + "",USERNAME, new RequestPageCallback<List<MapiOrderResult>>() {
            @Override
            public void success(Integer isNext, List<MapiOrderResult> success) {
                swipRefreshLayout.setRefreshing(false);
                hideLoading();
                ISNEXT = isNext;
                if(success.isEmpty())
                    return;
                mList.addAll(success);
                mAdapter.notifyDataSetChanged();
            }
        }, new RequestExceptionCallback() {
            @Override
            public void error(String code, String message) {
                swipRefreshLayout.setRefreshing(false);
                hideLoading();
                MainToast.showShortToast(message);
            }
        });

    }

    private void loadNext() {
        if (ISNEXT != null && ISNEXT==0) {
            return;
        }
        pageIndex++;
        load();
    }

    public void refreshData() {
        if (null != mList) {
            mList.clear();
            pageIndex = 0;
            mAdapter.notifyDataSetChanged();
            load();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        try {
            db.delete(MapiOrderResult.class);
        } catch (DbException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        finish();
    }

    @OnClick({R.id.back,R.id.tv_right,R.id.clear_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                try {
                    db.delete(MapiOrderResult.class);
                } catch (DbException e) {
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
                finish();
                break;
            case R.id.tv_right:
                Intent intent = new Intent(this, SearchCampaignActivity.class);
                startActivityForResult(intent, RequestCode.search_campaign);
                break;
            case R.id.clear_iv:
                searchEt.setText("");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.search_campaign:
                    if (null != data)
                        campaignResult = (MapiCampaignResult) data.getSerializableExtra("item");
                    companyId = campaignResult.getId();
                    tvRight.setText(campaignResult.getName());
                    refreshData();
                    break;
            }
        }
    }

}
