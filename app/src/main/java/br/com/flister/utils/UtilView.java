package br.com.flister.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by junior on 23/12/2016.
 */

public class UtilView {

    private static ProgressDialog progressDialog;

    public static void showIndeterminateProgressDialog(String message, Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void cancelIndeterminateProgressDialog(){
        progressDialog.cancel();
    }
}
