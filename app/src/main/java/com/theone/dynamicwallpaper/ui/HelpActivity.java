package com.theone.dynamicwallpaper.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.textview.QMUILinkTextView;
import com.theone.dynamicwallpaper.R;
import com.theone.dynamicwallpaper.base.BaseActivity;
import com.theone.dynamicwallpaper.util.QMUIDialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author The one
 * @date 2018/5/29 0029
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class HelpActivity extends BaseActivity {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.link_text)
    QMUILinkTextView linkText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_activity);
        ButterKnife.bind(this);
        title.setText("帮助");
        linkText.setOnLinkClickListener(new QMUILinkTextView.OnLinkClickListener() {
            @Override
            public void onTelLinkClick(String phoneNumber) {

            }

            @Override
            public void onMailLinkClick(String mailAddress) {

            }

            @Override
            public void onWebUrlLinkClick(String url) {
                showTipDialog(url);
            }
        });
    }

    private void showTipDialog(final String url){
        QMUIDialogUtils.showSimpleDialog(this, QMUIDialogUtils.PositiveDialog, "即将跳转",  url, new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
