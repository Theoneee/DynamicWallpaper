// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package com.theone.dynamicwallpaper.view.wheelview;

final class OnItemSelectedRunnable implements Runnable {
    final WheelView wheelView;
    public int oldIndex = -1;

    OnItemSelectedRunnable(WheelView wheelview) {
        wheelView = wheelview;
    }

    @Override
    public final void run() {
        if (wheelView.getSelectedPosition() < 0) {
            return;
        }
        if (wheelView.getSelectedPosition() == oldIndex) {
            return;
        }
        oldIndex = wheelView.getSelectedPosition();
        wheelView.onItemSelectedListener.onItemSelected(wheelView.getSelectedPosition(),wheelView.getSelectedItem());
    }
}
