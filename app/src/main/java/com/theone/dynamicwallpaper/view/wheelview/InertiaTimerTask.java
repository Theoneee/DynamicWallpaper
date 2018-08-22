// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.theone.dynamicwallpaper.view.wheelview;

import java.util.TimerTask;

final class InertiaTimerTask extends TimerTask {

    float a;
    final float velocityY;
    final WheelView wheelView;

    InertiaTimerTask(WheelView wheelview, float velocityY) {
        super();
        wheelView = wheelview;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            wheelView.cancelFuture();
            wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        WheelView wheelview = wheelView;
        wheelview.totalScrollY = wheelview.totalScrollY - i;
        if (!wheelView.isLoop) {
            float itemHeight = wheelView.itemHeightOuter;
            if (wheelView.totalScrollY <= (int) ((float) (-wheelView.initPosition) * itemHeight)) {
                a = 40F;
                wheelView.totalScrollY = (int) ((float) (-wheelView.initPosition) * itemHeight);
            } else if (wheelView.totalScrollY >= (int) ((float) (wheelView.items.size() - 1 - wheelView.initPosition) * itemHeight)) {
                wheelView.totalScrollY = (int) ((float) (wheelView.items.size() - 1 - wheelView.initPosition) * itemHeight);
                a = -40F;
            }
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        wheelView.handler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}
