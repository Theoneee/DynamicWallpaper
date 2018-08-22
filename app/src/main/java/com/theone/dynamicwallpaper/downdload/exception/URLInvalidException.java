package com.theone.dynamicwallpaper.downdload.exception;

import android.annotation.SuppressLint;

public class URLInvalidException extends Exception {
    public URLInvalidException() {
    }

    public URLInvalidException(String message) {
        super(message);
    }

    public URLInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public URLInvalidException(Throwable cause) {
        super(cause);
    }

    @SuppressLint("NewApi")
    public URLInvalidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
