package com.postterminal.postterminal;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;

public class DialogUtils {

    public static Dialog showProgress(Context context, String title, String message, DialogInterface.OnCancelListener cancelListener) {
        return ProgressDialog.show(context, title, message, false, true, cancelListener);
    }

    public static Dialog show(Context context, String title, String message, String confirmButton, DialogInterface.OnClickListener confirmListener, DialogInterface.OnCancelListener onCancelListener) {
        return show(context, title, message, null, true, confirmButton, confirmListener, null, null, null, null, null, onCancelListener, null);
    }

    public static Dialog show(Context context, String title, String message, View view, boolean cancelable,
                              String confirmButton, DialogInterface.OnClickListener confirmListener,
                              String centerButton, DialogInterface.OnClickListener centerListener,
                              String cancelButton, DialogInterface.OnClickListener cancelListener,
                              DialogInterface.OnShowListener onShowListener,
                              DialogInterface.OnCancelListener onCancelListener,
                              DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(cancelable);

        if (title != null) {
            builder.setTitle(title);
        }
        if (message != null) {
            builder.setMessage(message);
        }
        if (confirmButton != null) {
            builder.setPositiveButton(confirmButton, confirmListener);
        }
        if (centerButton != null) {
            builder.setNeutralButton(centerButton, centerListener);
        }
        if (cancelButton != null) {
            builder.setNegativeButton(cancelButton, cancelListener);
        }
        if (cancelable) {
            builder.setOnCancelListener(onCancelListener);
        }
        if (view != null) {
            builder.setView(view);
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(onShowListener);
        alertDialog.setOnDismissListener(onDismissListener);
        if (!(context instanceof Activity)) {
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
        }
        alertDialog.show();
        return alertDialog;
    }



}
