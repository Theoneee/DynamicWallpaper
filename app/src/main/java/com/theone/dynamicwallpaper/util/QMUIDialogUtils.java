package com.theone.dynamicwallpaper.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIKeyboardHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.util.QMUIViewHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.theone.dynamicwallpaper.R;

/**
 * @author The one
 * @date 2018/5/3 0003
 * @describe QMUI - Dialog
 * @email 625805189@qq.com
 * @remark
 */

public class QMUIDialogUtils {

    public static final int PositiveDialog = 1;
    public static final int NegativeDialog = 2;

    /**
     * 消息类型对话框
     * @param context 上下文
     * @param type 类型  PositiveDialog 蓝色右边按钮 NegativeDialog 红色右边按钮
     * @param title 标题
     * @param content 内容
     * @param rightBtnListener 右Btn监听
     * @return
     */
    public static void showSimpleDialog(final Context context, final int type, final String title, final String content, QMUIDialogAction.ActionListener rightBtnListener){

           showSimpleDialog(context, type,title, content, "取消", new QMUIDialogAction.ActionListener() {
               @Override
               public void onClick(QMUIDialog dialog, int index) {
                   dialog.dismiss();
               }
           }, "确定", rightBtnListener);
    }

    /**
     * 消息类型对话框（蓝色按钮）
     * @param context 上下文
     * @param type 类型  PositiveDialog 蓝色右边按钮 NegativeDialog 红色右边按钮
     * @param title 标题
     * @param content 内容
     * @param btnLeftString 左Btn文字
     * @param leftBtnListener 左Btn监听
     * @param btnRightString 右Btn文字
     * @param rightBtnListener 右Btn监听
     * @return
     */
    public static void showSimpleDialog(final Context context, final int type, final String title, final String content, final String btnLeftString, final QMUIDialogAction.ActionListener leftBtnListener, final String btnRightString, final QMUIDialogAction.ActionListener rightBtnListener){
        switch (type){
            case PositiveDialog:
                new QMUIDialog.MessageDialogBuilder(context)
                        .setTitle(title)
                        .setMessage(content)
                        .addAction(btnLeftString, leftBtnListener)
                        .addAction(btnRightString,rightBtnListener).show();
                break;
            case NegativeDialog:
                new QMUIDialog.MessageDialogBuilder(context)
                        .setTitle(title)
                        .setMessage(content)
                        .addAction(btnLeftString, leftBtnListener)
                        .addAction(0,btnRightString, rightBtnListener)
                        .show();
                break;
        }

    }

    /**
     * 单选菜单类型对话框
     * @param context
     * @param items
     * @param listener
     * @return
     */
    public  static QMUIDialog showMenuDialog(Context context, String[] items, DialogInterface.OnClickListener listener) {
        return new QMUIDialog.MenuDialogBuilder(context)
                .addItems(items, listener).show();
    }

    /**
     * 单选菜单类型对话框
     * @param context
     * @param items
     * @param checkedIndex
     * @param listener
     */
    public static QMUIDialog showSingleChoiceDialog(Context context, String[] items, final int checkedIndex, DialogInterface.OnClickListener listener) {
      return   new QMUIDialog.CheckableDialogBuilder(context)
                .setCheckedIndex(checkedIndex)
                .addItems(items, listener)
                .show();
    }

    /**
     * 多选菜单类型对话框
     * @param context
     * @param items
     * @param checkedItems
     * @param onCheckListener
     * @param rightBtnString
     * @param onMultiChoiceConfirmClickListener
     */
    public static void showMultiChoiceDialog(Context context, final String[] items, final int[] checkedItems, DialogInterface.OnClickListener onCheckListener, String rightBtnString, final OnMultiChoiceConfirmClickListener onMultiChoiceConfirmClickListener) {
        showMultiChoiceDialog(context, items, checkedItems, onCheckListener, "取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }
        ,rightBtnString,onMultiChoiceConfirmClickListener);
    }

    /**
     * 多选菜单类型对话框
     * @param context
     * @param items
     * @param checkedItems
     * @param onCheckListener
     * @param leftBtnString
     * @param leftBtnListener
     * @param rightBtnString
     * @param onMultiChoiceConfirmClickListener
     */
    public static void showMultiChoiceDialog(Context context, final String[] items, final int[] checkedItems, DialogInterface.OnClickListener onCheckListener, String leftBtnString, QMUIDialogAction.ActionListener leftBtnListener, String rightBtnString, final OnMultiChoiceConfirmClickListener onMultiChoiceConfirmClickListener) {
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(context)
                .setCheckedItems(checkedItems)
                .addItems(items,onCheckListener);
        builder.addAction(leftBtnString, leftBtnListener);
        builder.addAction(rightBtnString, new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                onMultiChoiceConfirmClickListener.getCheckedItemIndexes(builder.getCheckedItemIndexes());
            }
        });
        builder.show();
    }

    /**
     * 多选菜单类型对话框(item 数量很多)
     * @param context
     * @param items
     * @param checkedItems
     * @param OnItemClickListener
     * @param rightString
     * @param onMultiChoiceConfirmClickListener
     */
    public static void showNumerousMultiChoiceDialog(Context context, final String[] items, int[] checkedItems, DialogInterface.OnClickListener OnItemClickListener, String rightString, final  OnMultiChoiceConfirmClickListener onMultiChoiceConfirmClickListener) {
       showNumerousMultiChoiceDialog(context, items, checkedItems, OnItemClickListener, "取消", new QMUIDialogAction.ActionListener() {
           @Override
           public void onClick(QMUIDialog dialog, int index) {
               dialog.dismiss();
           }
       },rightString,onMultiChoiceConfirmClickListener);
    }

    /**
     * 多选菜单类型对话框(item 数量很多)
     * @param context
     * @param items
     * @param checkedItems
     * @param OnItemClickListener
     * @param leftBtnString
     * @param leftClickListener
     * @param rightString
     */
    public static void showNumerousMultiChoiceDialog(Context context, final String[] items, int[] checkedItems, DialogInterface.OnClickListener OnItemClickListener, String leftBtnString, QMUIDialogAction.ActionListener leftClickListener, String rightString, final OnMultiChoiceConfirmClickListener onMultiChoiceConfirmClickListener) {
        final QMUIDialog.MultiCheckableDialogBuilder builder = new QMUIDialog.MultiCheckableDialogBuilder(context)
                .setCheckedItems(checkedItems)
                .addItems(items,OnItemClickListener);
        builder.addAction(leftBtnString,leftClickListener);
        builder.addAction(rightString, new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                onMultiChoiceConfirmClickListener.getCheckedItemIndexes(builder.getCheckedItemIndexes());
            }
        });
        builder.show();
    }

    public interface OnMultiChoiceConfirmClickListener{
       void getCheckedItemIndexes(int[] checkedItems);
    }

    /**
     * 带输入框的对话框
     * @param context
     * @param title
     * @param hint
     * @param listener
     *
     */
    public static void showEditTextDialog(final Context context, String title, String hint, final OnEditTextConfirmClickListener listener) {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(context);
        builder.setTitle(title)
                .setPlaceholder(hint)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence text = builder.getEditText().getText();
                        listener.getEditText(dialog,text.toString());

                    }
                }).show();
    }

    public  interface OnEditTextConfirmClickListener{
        void getEditText(QMUIDialog dialog,String content);
    }

    /**
     * 消息类型对话框 (很长文案)
     * @param context
     * @param title
     * @param content
     * @param confirm
     * @param listener
     */
    public static void showLongMessageDialog(Context context, String title, String content, String confirm, QMUIDialogAction.ActionListener listener) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setTitle(title)
                .setMessage(content)
                .addAction(confirm, listener)
                .show();
    }


    /**
     * 高度适应键盘升降的对话框
     * @param context
     * @param title
     * @param content
     * @param leftString
     * @param leftListener
     * @param rightString
     * @param rightListener
     */
    public static void showAutoDialog(Context context, String title, String content, String leftString, QMUIDialogAction.ActionListener leftListener, String rightString, QMUIDialogAction.ActionListener rightListener) {
        QMAutoTestDialogBuilder autoTestDialogBuilder = (QMAutoTestDialogBuilder) new QMAutoTestDialogBuilder(context,title,content)
                .addAction(leftString, leftListener)
                .addAction(rightString, rightListener);
        autoTestDialogBuilder.show();
        QMUIKeyboardHelper.showKeyboard(autoTestDialogBuilder.getEditText(), true);
    }

   static class QMAutoTestDialogBuilder extends QMUIDialog.AutoResizeDialogBuilder {
        private Context mContext;
       private EditText mEditText;
       private String mTitle;
       private String mContent;

        public QMAutoTestDialogBuilder(Context context, String title, String content) {
            super(context);
            mContext = context;
            mTitle = title;
            mContent = content;
        }
       public EditText getEditText() {
           return mEditText;
       }
        @Override
        public View onBuildContent(QMUIDialog dialog, ScrollView parent) {
            LinearLayout layout = new LinearLayout(mContext);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            int padding = QMUIDisplayHelper.dp2px(mContext, 20);
            layout.setPadding(padding, padding, padding, padding);
            mEditText = new EditText(mContext);
            QMUIViewHelper.setBackgroundKeepingPadding(mEditText, QMUIResHelper.getAttrDrawable(mContext, R.attr.qmui_list_item_bg_with_border_bottom));
            mEditText.setHint(mTitle);
            LinearLayout.LayoutParams editTextLP = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, QMUIDisplayHelper.dpToPx(50));
            editTextLP.bottomMargin = QMUIDisplayHelper.dp2px(mContext, 15);
            mEditText.setLayoutParams(editTextLP);
            layout.addView(mEditText);
            TextView content = new TextView(mContext);
            content.setLineSpacing(QMUIDisplayHelper.dp2px(mContext, 4), 1.0f);
            content.setText(mContent);
            content.setTextColor(ContextCompat.getColor(mContext, R.color.gray_color));
            content.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layout.addView(content);
            return layout;
        }
    }

    public static  QMUITipDialog  showTipsDialog(Context context,int Type,String tips){
        return new QMUITipDialog.Builder(context)
                .setIconType(Type)
                .setTipWord(tips)
                .create();
    }

    public static  QMUITipDialog  SuccessTipsDialog(Context context,String tips){
        return new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(tips)
                .create();
    }

    public static  QMUITipDialog  LoadingTipsDialog(Context context,String tips){
        return new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(tips)
                .create();
    }
}
