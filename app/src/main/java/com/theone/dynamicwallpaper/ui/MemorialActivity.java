package com.theone.dynamicwallpaper.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.base.BaseActivity;
import com.theone.dynamicwallpaper.service.Memorial;
import com.theone.dynamicwallpaper.util.DateUtil;
import com.theone.dynamicwallpaper.util.GlideUtil;
import com.theone.dynamicwallpaper.util.QMUIDialogUtils;
import com.theone.dynamicwallpaper.util.SharedPreferencesUtil;
import com.theone.dynamicwallpaper.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author The one
 * @date 2018/7/5 0005
 * @describe 纪念日 - 桌面小插件
 * @email 625805189@qq.com
 * @remark
 */
public class MemorialActivity extends BaseActivity implements OnDateSetListener {
    public static final String ICON1 = "icon1";
    public static final String ICON2 = "icon2";
    public static final String TIME = "time";
    public static final String DES = "des";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.icon1)
    ImageView icon1;
    @BindView(R.id.icon2)
    ImageView icon2;
    @BindView(R.id.tv_start)
    TextView tvStart;

    TimePickerDialog timePickerDialog;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.tv_finish)
    TextView tvFinish;

    private QMUITipDialog successTisDialog;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private String Icon1, Icon2, Time, Des;

    private String ICON_TYPE;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memorial);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    public static void starThisActivity(Activity activity){
        Intent intent = new Intent(activity,MemorialActivity.class);
        activity.startActivity(intent);
    }

    private void initData() {
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
        Icon1 = sharedPreferencesUtil.getString(ICON1, "");
        Icon2 = sharedPreferencesUtil.getString(ICON2, "");
        Time = sharedPreferencesUtil.getString(TIME, "2016-07-02");
        Des = sharedPreferencesUtil.getString(DES, "已相恋");
        successTisDialog = QMUIDialogUtils.showTipsDialog(this, QMUITipDialog.Builder.ICON_TYPE_SUCCESS, "设置成功");
    }

    private void initView() {
        title.setText("纪念日");
        if (Icon1.isEmpty())
            icon1.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon1));
        else
            GlideUtil.load(this, Icon1, icon1);
        if (Icon2.isEmpty())
            icon2.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon2));
        else
            GlideUtil.load(this, Icon2, icon2);
        tvStart.setText(Time);
        tvDes.setText(Des);
        initTimePick(Time);
    }

    private void initTimePick(String time) {
        timePickerDialog = null;
        timePickerDialog = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("取消")
                .setSureStringId("确定")
                .setTitleStringId("纪念日")
                .setMinMillseconds(DateUtil.stringToLong("1970-1-1", "yyyy-MM-dd"))
                .setSelectorMillseconds(DateUtil.stringToLong(time, "yyyy-MM-dd"))
                .setCyclic(false)
                .setThemeColor(getResources().getColor(R.color.gray_color))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextSize(15)
                .build();
    }

    @OnClick({R.id.iv_back, R.id.tv_start, R.id.icon1, R.id.icon2, R.id.tv_des, R.id.tv_finish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_start:
                showTimePick();
                break;
            case R.id.icon1:
                ICON_TYPE = ICON1;
                getIcon();
                break;
            case R.id.icon2:
                ICON_TYPE = ICON2;
                getIcon();
                break;
            case R.id.tv_des:
                showEditDialog();
                break;
            case R.id.tv_finish:
                onFinish();
                break;
        }
    }

    private void showTimePick() {
        timePickerDialog.show(getSupportFragmentManager(), "start");
    }

    private void getIcon() {
        PictureSelector.create(MemorialActivity.this)
                .openGallery(PictureMimeType.ofImage())//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
//                .theme(R.style.picture_white_style)//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .imageSpanCount(3)// 每行显示个数 int
                .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .previewImage(false)// 是否可预览图片 true or false
                .isCamera(true)// 是否显示拍照按钮 true or false
                .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .setOutputCameraPath("/DynamicWallPaper")// 自定义拍照保存路径,可不填
                .enableCrop(true)// 是否裁剪 true or false
                .compress(false)// 是否压缩 true or false
                .glideOverride(200, 200)// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                .isGif(false)// 是否显示gif图片 true or false
                .freeStyleCropEnabled(false)// 裁剪框是否可拖拽 true or false
                .circleDimmedLayer(true)// 是否圆形裁剪 true or false
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                .showCropGrid(false)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                .openClickSound(true)// 是否开启点击声音 true or false
                .selectionMedia(null)// 是否传入已选图片 List<LocalMedia> list
                .rotateEnabled(true) // 裁剪是否可旋转图片 true or false
                .scaleEnabled(true)// 裁剪是否可放大缩小图片 true or false
                .isDragFrame(true)// 是否可拖动裁剪框(固定)
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    LocalMedia media = selectList.get(0);
                    boolean type = ICON_TYPE.equals(ICON1);
                    String path = media.getCutPath();

                    GlideUtil.load(this, path, type ? icon1 : icon2);
                    if (type) {
                        Icon1 = media.getCutPath();
                    } else {
                        Icon2 = media.getCutPath();
                    }
                    updateFinish();
                    break;
            }
        }
    }

    private void showEditDialog() {
        QMUIDialogUtils.showEditTextDialog(this, "输入描述文字", "已相恋", new QMUIDialogUtils.OnEditTextConfirmClickListener() {
            @Override
            public void getEditText(QMUIDialog dialog, String content) {
                if (content.isEmpty()) {
                    ToastUtils.showToast(MemorialActivity.this, "不能不空");
                    return;
                }
                if (content.equals(tvDes.getText().toString())) {
                    ToastUtils.showToast(MemorialActivity.this, "写点不一样的吧");
                    return;
                }
                tvDes.setText(content);
                updateFinish();
                dialog.dismiss();
            }
        });
    }

    private void onFinish() {
        String time = tvStart.getText().toString();
        String des = tvDes.getText().toString();
        sharedPreferencesUtil.putString(ICON1, Icon1);
        sharedPreferencesUtil.putString(ICON2, Icon2);
        sharedPreferencesUtil.putString(TIME, time);
        sharedPreferencesUtil.putString(DES, des);
        sendBroadcast(Memorial.SERVICE_INTENT);
        tvFinish.setEnabled(false);
        showTipsDialog(successTisDialog);
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if (millseconds > System.currentTimeMillis()) {
            ToastUtils.showToast(this, "汪汪汪~");
            return;
        }
        String time = DateUtil.longToString(millseconds, "yyyy-MM-dd");
        tvStart.setText(time);
        initTimePick(time);
        if (time.equals(sharedPreferencesUtil.getString(TIME)) || time.equals(tvStart.getText().toString()))
            updateFinish();
    }

    private void updateFinish() {
        if (!tvFinish.isEnabled()) {
            tvFinish.setEnabled(true);
        }
    }

    private void showTipsDialog(final QMUITipDialog dialog) {
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                dialog.dismiss();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        PictureFileUtils.deleteCacheDirFile(MemorialActivity.this);
    }
}
