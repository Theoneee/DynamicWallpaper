package com.theone.dynamicwallpaper.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.qmuiteam.qmui.widget.QMUIEmptyView;
import com.qmuiteam.qmui.widget.pullRefreshLayout.QMUIPullRefreshLayout;
import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.adapter.VideoAdapter;
import com.theone.dynamicwallpaper.bean.Video;
import com.theone.dynamicwallpaper.service.VideoLiveWallpaper;
import com.theone.dynamicwallpaper.service.VideoLiveWallpaper2;
import com.theone.dynamicwallpaper.util.DataCache;
import com.theone.dynamicwallpaper.util.FileUtil;
import com.theone.dynamicwallpaper.util.QMUIDialogUtils;
import com.theone.dynamicwallpaper.util.SharedPreferencesUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author The one
 * @date 2018/6/28 0028
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class VideoFragment extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    GridView gridView;
    QMUIEmptyView emptyView;
    View view;
    Unbinder unbinder;
    QMUIPullRefreshLayout refreshLayout;

    private String currentService = MainActivity.SERCIVE_1;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private List<Video> videos;
    private VideoAdapter videoAdapter;
    private DataCache dataCache;
    private MainActivity mainActivity;


    private int TYPE = 0;
    private String[] itmes = {"移除视频", "删除视频", "添加到喜欢"};
    private String[] itmes2 = {"移除视频", "删除视频"};
    private int SET_POSITION;

    public static final VideoFragment newInstance(int index) {
        VideoFragment fragment = new VideoFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TYPE = getArguments().getInt("index");
        view = inflater.inflate(R.layout.video_fragment, null);
        initView();
        initData();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initView() {
        sharedPreferencesUtil = SharedPreferencesUtil.getInstance();
        dataCache = DataCache.getInstance();
        gridView = view.findViewById(R.id.gridview);
        emptyView = view.findViewById(R.id.empty_view);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        mainActivity = (MainActivity) getActivity();
        //  当为 "全部" 时，才能进行下拉刷新
        if (TYPE == 0)
            refreshLayout.setEnabled(true);
        else
            refreshLayout.setEnabled(false);
        refreshLayout.setOnPullListener(new QMUIPullRefreshLayout.OnPullListener() {
                @Override
                public void onMoveTarget(int offset) {

                }

                @Override
                public void onMoveRefreshView(int offset) {

                }

                @Override
                public void onRefresh() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.freshData();
                            }
                        },1000);
                }
            });
    }

    public void initData() {
        if (null == gridView)
            return;
        refreshLayout.finishRefresh();
        String emptyTips = "";
        if (videos == null)
            videos = new ArrayList<>();
        if (TYPE == 0) {
            videos = dataCache.getVideos();
            emptyTips = "无视频";
        } else if (TYPE == 1) {
            videos = dataCache.getHistory();
            emptyTips = "无历史";
        } else {
            videos = dataCache.getLike();
            emptyTips = "无喜欢";
        }
        if (null == videoAdapter) {
            videoAdapter = new VideoAdapter(getActivity());
            gridView.setAdapter(videoAdapter);
            gridView.setOnItemClickListener(this);
            gridView.setOnItemLongClickListener(this);
        }
        if (null !=videos &&videos.size() > 0) {
            showContent();
            videoAdapter.addHeadData(videos);
            Log.e("LOG", " addHeadData  " + TYPE + " videos.size() = " + videos.size() + " path = " + videos.get(0).getPath());
        } else {
            showEmptyView(emptyTips);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SET_POSITION = i;
        String path = videos.get(i).getPath();
        savePath(path);
        // 判断当前运行的是哪个Service，然后跳转到不是当前运行的Service，并记录下跳转到的Service名称
        // 如果只用一个Service，更改了视频源后，跳转到同一个Service，预览界面的视频改变了，但是点击设置可是设置无效
        // 后来发现用其他的更改后可以生效，才想出两个Service方法
        // 用到此方法后如何解决 用户是点击了设置还是点击了返回键（因为onActivityResult（）里面接收到的都是同一个参数）
        // 点击了设置退出到桌面，点击返回则继续留在此界面
        // 解决方法：请参照下面 isLiveWallpaperChanged（）
        if (getCurrentService().equals(MainActivity.SERCIVE_2)) {
            VideoLiveWallpaper.setToWallPaper(getActivity());
            currentService = MainActivity.SERCIVE_1;
        } else {
            VideoLiveWallpaper2.setToWallPaper(getActivity());
            currentService = MainActivity.SERCIVE_2;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        showLongClickDialog(i);
        return true;
    }

    private void showLongClickDialog(final int position) {
        String[] strings;
        // 当为喜欢列表或者全部列表、历史列表选择的视频已经标记为喜欢时，不显示 “添加到喜欢”操作
        // TYPE == 2 这个条件可以去掉，为了避免每次判断时都要 get()，所以加入此条件
        if (TYPE == 2 || videos.get(position).isLike()) {
            strings = itmes2;
        } else {
            strings = itmes;
        }
        // 为了下面的switch操作，特意将 添加到喜欢 放到第三个，这样就可以使用一个switch
        QMUIDialogUtils.showMenuDialog(getActivity(), strings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Video video = videos.get(position);
                switch (i) {
                    case 0:
                        // 移除视频
                        removeVideo(video);
                        mainActivity.showRemoveDialog();
                        mainActivity.fresh();
                        break;
                    case 1:
                        // 删除视频
                        FileUtil.deleteFile(getActivity(), video.getPath());
                        LitePal.delete(Video.class, video.getId());
                        videos.remove(video);
                        updateDataCache(video);
                        mainActivity.fresh();
                        mainActivity.showDeleteDialog();
                        break;
                    case 2:
                        // 添加到喜欢
                        video.setLike(true);
                        video.setLikeTime(System.currentTimeMillis());
                        check(video);
                        dataCache.getLike().add(video);
                        mainActivity.freshLike();
                        mainActivity.showAddLikeDialog();
                        break;
                }
                dialogInterface.dismiss();
            }
        });
    }

    /**
     * 移除视频，不显示在列表（不符合作为动态壁纸的视频可以进行此操作后让选择更少）
     * @param video
     */
    private void removeVideo(Video video) {
        switch (TYPE) {
            case 0:
                // 全部
                video.setShow(false);
                dataCache.getVideos().remove(video);
                video.setHistory(false);
                dataCache.getHistory().remove(video);
                video.setLike(false);
                dataCache.getLike().remove(video);
                break;
            case 1:
                // 历史
                video.setHistory(false);
                dataCache.getHistory().remove(video);
                break;
            case 2:
                // 喜欢
                video.setLike(false);
                dataCache.getLike().remove(video);
                break;
        }
        video.save();
        videos.remove(video);
    }

    private void updateDataCache(Video video) {
        dataCache.getVideos().remove(video);
        dataCache.getHistory().remove(video);
        dataCache.getLike().remove(video);
    }

    private void showEmptyView(String content) {
        emptyView.show(content, "");
        ViewVisible(emptyView);
        ViewGone(refreshLayout);
    }

    private void showContent() {
        ViewGone(emptyView);
        ViewVisible(refreshLayout);
    }

    private void ViewGone(View view) {
        if (view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.GONE);
    }

    private void ViewVisible(View view) {
        if (view.getVisibility() == View.GONE)
            view.setVisibility(View.VISIBLE);
    }

    private void savePath(String path) {
        sharedPreferencesUtil.putString("path", path);
    }

    public String getCurrentService() {
        return sharedPreferencesUtil.getString("service", MainActivity.SERCIVE_1);
    }

    public void updateService() {
        sharedPreferencesUtil.putString("service", currentService);
    }

    public void setCurrentService(String currentService) {
        this.currentService = currentService;
    }

    public String getCurrentServices() {
        return currentService;
    }

    public void onSeted() {
        updateService();
        Video video = videos.get(SET_POSITION);
        video.setHistory(true);
        video.setHistoryTime(System.currentTimeMillis());
        check(video);
        if (!isInHistory(video)) {
            dataCache.getHistory().add(video);
        }
    }

    /**
     * 检查数据库里是否已经存在该视频信息，如果没有则添加，如果有则更新
     *
     * @param video
     * @return
     */
    private void check(Video video) {
        List<Video> videos = LitePal.where("path = ?", video.getPath()).find(Video.class);
        if (videos.size() > 0) {
            video.update(video.getId());
        } else {
            video.save();
        }
    }

    /**
     * 设置完壁纸后，判断当前设置的壁纸是否已经存在于历史中
     * 如果历史中有，则要更新设置时间，让其排到第一位
     */
    private boolean isInHistory(Video video) {
        boolean isIn = false;
        for (Video video1 : dataCache.getHistory()) {
            if (video1 == video) {
                isIn = true;
                break;
            }
        }
        return isIn;
    }

    /**
     * 判断是否已经存在于喜欢中
     * （由于被标记为喜欢后是无法再弹出添加到喜欢操作，所以这个判断也就无用了）
     */
    private boolean isInLike(Video video) {
        boolean isIn = false;
        for (Video video1 : dataCache.getLike()) {
            if (video1 == video) {
                isIn = true;
                break;
            }
        }
        return isIn;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
