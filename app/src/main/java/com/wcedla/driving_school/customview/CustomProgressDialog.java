package com.wcedla.driving_school.customview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wcedla.driving_school.R;

public class CustomProgressDialog extends Dialog {

    private static CustomProgressDialog customProgressDialog = null;

    private CustomProgressDialog(Context context, int themeId, String message, boolean cancelable) {
        super(context, themeId);
        setCancelable(cancelable);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_custom_progress, null);
        TextView messageView = view.findViewById(R.id.message_view);
        messageView.setText(message);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
        setContentView(view);

    }

    public synchronized static CustomProgressDialog create(Context context, String message) {
        return create(context, message, true);
    }

    public synchronized static CustomProgressDialog create(Context context, String message, boolean cancelAble) {
        customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog, message, cancelAble);
        return customProgressDialog;
    }


    public void showProgressDialog() {
        if (customProgressDialog != null) {
            if (customProgressDialog.isShowing())
                customProgressDialog.cancel();
            customProgressDialog.show();
        }

    }

    public void dismissProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.dismiss();
        customProgressDialog = null;
    }

    public void cancelProgressDialog() {
        if (customProgressDialog != null && customProgressDialog.isShowing())
            customProgressDialog.cancel();
        customProgressDialog = null;
    }

    public void setProgressDialogOnCancelListener(DialogInterface.OnCancelListener cancelListener) {
        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.setOnCancelListener(cancelListener);
        }
    }

    public void setProgressDialogOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.setOnDismissListener(dismissListener);
        }
    }

    public void setProgressDialogOnKeyListener(DialogInterface.OnKeyListener keyListener) {
        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.setOnKeyListener(keyListener);
        }
    }

    public void setProgressDialogOnShowListener(DialogInterface.OnShowListener showListener) {
        if (customProgressDialog != null && !customProgressDialog.isShowing()) {
            customProgressDialog.setOnShowListener(showListener);
        }
    }
}
