package com.theone.dynamicwallpaper.ui;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.base.BaseActivity;
import com.theone.dynamicwallpaper.service.VideoLiveWallpaper;
import com.theone.dynamicwallpaper.service.VideoLiveWallpaper2;
import com.theone.dynamicwallpaper.util.QMUIDialogUtils;
import com.theone.dynamicwallpaper.util.SharedPreferencesUtil;
import com.theone.dynamicwallpaper.view.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.groupListView)
    QMUIGroupListView groupListView;
    @BindView(R.id.title)
    TextView title;
    private boolean isUpdate = false;
    private boolean sortUpdate = false;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private QMUICommonListItemView itemVoice, itemDuration, itemSort, itemHelp, itemAbout,download;
    private List<String> minDatas;
    private List<String> maxDatas;
    private PopupWindow popupWindow;
    private WheelView wheelView_min;
    private WheelView wheelView_max;
    private String minStr = "";
    private String maxStr = "";
    private String[] SORTS = {"时间排序", "大小排序"};
    private int sort = 0;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        title.setText("设置");
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
        boolean voice = sharedPreferencesUtil.getBoolean("voice", false);

         minStr = sharedPreferencesUtil.getString("min", "5s");
          maxStr = sharedPreferencesUtil.getString("max", "1min");
        sort = sharedPreferencesUtil.getInt("sort", 0);

        itemVoice = groupListView.createItemView("视频声音");
        itemDuration = groupListView.createItemView("视频大小");
        itemSort = groupListView.createItemView("视频顺序");
        download = groupListView.createItemView("视频下载");
        itemHelp = groupListView.createItemView("如何获取视频");
        itemAbout = groupListView.createItemView("关于");

        itemSort.setDetailText(SORTS[sort]);
        itemVoice.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemVoice.getSwitch().setChecked(voice);
        itemVoice.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String service = sharedPreferencesUtil.getString("service", MainActivity.SERCIVE_1);
                if (isChecked) {
                    if (service.equals(MainActivity.SERCIVE_1))
                        VideoLiveWallpaper.voiceNormal(SettingActivity.this);
                    else
                        VideoLiveWallpaper2.voiceNormal(SettingActivity.this);
                } else {
                    if (service.equals(MainActivity.SERCIVE_1))
                        VideoLiveWallpaper.voiceSilence(SettingActivity.this);
                    else
                        VideoLiveWallpaper2.voiceSilence(SettingActivity.this);
                }
                sharedPreferencesUtil.putBoolean("voice", isChecked);
            }
        });
        itemHelp.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemAbout.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        download.setAccessoryType(QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);

        updateDetailText();

        QMUIGroupListView.newSection(this)
                .setTitle("通用")
                .addItemView(itemVoice, null)
                .addItemView(itemDuration, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDurationWheelDialog(view);
                    }
                })
                .addItemView(itemSort, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTypeChoose();
                    }
                })
                .addItemView(download, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SettingActivity.this,DownloadActivity.class);
                        startActivity(intent);
                    }
                })
                .addTo(groupListView);

        QMUIGroupListView.newSection(this)
                .setTitle("帮助")
                .addItemView(itemHelp, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SettingActivity.this, HelpActivity.class);
                        startActivity(intent);
                    }
                })
                .addItemView(itemAbout, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
                        startActivity(intent);
                    }
                })
                .addTo(groupListView);

    }

    QMUIDialog typeDialog;

    private void showTypeChoose() {
        if (null == typeDialog) {
            typeDialog = QMUIDialogUtils.showSingleChoiceDialog(this, SORTS, sort, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (i != sort){
                        sortUpdate = true;
                        sort = i;
                        itemSort.setDetailText(SORTS[sort]);
                    } else
                        sortUpdate = false;
                    sharedPreferencesUtil.putInt("sort", i);
                    // 更换排序方式后只更新 全部
                    typeDialog.dismiss();
                }
            });

        } else {
            typeDialog.show();
        }
    }

    private void showDurationWheelDialog(View views) {
        if (popupWindow == null) {
            View view = getLayoutInflater().inflate(R.layout.duration_selector_dialog, null);
            wheelView_min = view.findViewById(R.id.wheel_min);
            wheelView_min.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int selectedIndex, String item) {
                    minStr = item;
                }
            });
            wheelView_max = view.findViewById(R.id.wheel_max);
            wheelView_max.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int selectedIndex, String item) {
                    maxStr = item;
                }
            });
            view.findViewById(R.id.tv_all).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wheelView_min.setItems(minDatas, 0);
                    wheelView_max.setItems(maxDatas, maxDatas.size());
                    minStr = minDatas.get(0);
                    maxStr = maxDatas.get(maxDatas.size() - 1);
                    udpateChanged();
                }
            });
            view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    udpateChanged();
                }
            });
            view.findViewById(R.id.out_layout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchable(true);
        }
        wheelView_min.setItems(minDatas, getPosition(minDatas, minStr));
        wheelView_max.setItems(maxDatas, getPosition(maxDatas, maxStr));
        popupWindow.showAsDropDown(views);
    }

    private void initData() {
        minDatas = new ArrayList<>();
        minDatas.add(getResourceString(R.string.duration_zero_seconds));
        minDatas.add(getResourceString(R.string.duration_five_seconds));
        minDatas.add(getResourceString(R.string.duration_ten_seconds));
        minDatas.add(getResourceString(R.string.duration_fifteen_seconds));
        minDatas.add(getResourceString(R.string.duration_twenty_seconds));

        maxDatas = new ArrayList<>();
        maxDatas.add(getResourceString(R.string.duration_one_minutes));
        maxDatas.add(getResourceString(R.string.duration_second_minutes));
        maxDatas.add(getResourceString(R.string.duration_three_minutes));
        maxDatas.add(getResourceString(R.string.duration_four_minutes));
        maxDatas.add(getResourceString(R.string.duration_max));
    }

    private String getResourceString(int id) {
        return getResources().getString(id);
    }

    private void udpateChanged() {
        sharedPreferencesUtil.putString("min", minStr);
        sharedPreferencesUtil.putString("max", maxStr);
        isUpdate = true;
        updateDetailText();
        popupWindow.dismiss();
    }

    private int getPosition(List<String> datas, String str) {
        int position = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (str.equals(datas.get(i))) {
                position = i;
            }
        }
        return position;
    }

    private void updateDetailText() {
        if (minStr.equals("0s") && maxStr.equals("不限")) itemDuration.setDetailText("全部");
        else itemDuration.setDetailText(minStr + " - " + maxStr);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        onFinish();
    }

    /**
     * 监听Back键按下事件,方法2:
     * 注意:
     * 返回值表示:是否能完全处理该事件
     * 在此处返回false,所以会继续传播该事件.
     * 在具体项目中此处的返回值视情况而定.
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onFinish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private void onFinish() {
        if (isUpdate)
            setResult(MainActivity.REQUEST_UPDATE_DURATION);
        else if (sortUpdate)
            setResult(MainActivity.REQUEST_UPDATE_SORT);
        finish();
    }
}
