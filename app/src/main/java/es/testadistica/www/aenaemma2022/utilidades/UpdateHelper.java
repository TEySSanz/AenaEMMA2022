package es.testadistica.www.aenaemma2022.utilidades;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

public class UpdateHelper {

    public static String KEY_UPDATE_ENABLE = "isUpdate";
    public static String KEY_UPDATE_VERSION = "version";
    public static String KEY_UPDATE_URL = "update_url";

    public interface OnUpdateCheckListener {
        void onUpdateCheckListener(String urlApp);
    }

    public interface OnNoUpdateCheckListener {
        void onNoUpdateCheckListener();
    }

    public static Builder with(Context context){
        return new Builder (context);
    }

    private OnUpdateCheckListener onUpdateCheckListener;
    private OnNoUpdateCheckListener onNoUpdateCheckListener;
    private Context context;

    public UpdateHelper (OnUpdateCheckListener onUpdateCheckListener, OnNoUpdateCheckListener onNoUpdateCheckListener, Context context){
        this.onUpdateCheckListener = onUpdateCheckListener;
        this.onNoUpdateCheckListener = onNoUpdateCheckListener;
        this.context = context;
    }

    public void check(){
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        if (remoteConfig.getBoolean(KEY_UPDATE_ENABLE)) {
            String currentVersion = remoteConfig.getString(KEY_UPDATE_VERSION);
            String appVersion = getAppVersion(context);
            String updateURL = remoteConfig.getString(KEY_UPDATE_URL);

            if(!TextUtils.equals(currentVersion,appVersion) && onUpdateCheckListener != null){
                onUpdateCheckListener.onUpdateCheckListener(updateURL);
            } else {
                onNoUpdateCheckListener.onNoUpdateCheckListener();
            }
        } else {
            onNoUpdateCheckListener.onNoUpdateCheckListener();
        }
    }

    private String getAppVersion (Context context){
        String result = "";

        try {
            result = context.getPackageManager().getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            //result = result.replaceAll("[a-zA-Z]|-","");
        } catch (PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }

        return result;
    }

    public static class Builder {
        private Context context;
        private OnUpdateCheckListener onUpdateCheckListener;
        private OnNoUpdateCheckListener onNoUpdateCheckListener;

        public Builder (Context context) {
            this.context = context;
        }

        public Builder onUpdateCheck (OnUpdateCheckListener onUpdateCheckListener, OnNoUpdateCheckListener onNoUpdateCheckListener){
            this.onUpdateCheckListener = onUpdateCheckListener;
            this.onNoUpdateCheckListener = onNoUpdateCheckListener;
            return this;
        }

        public UpdateHelper build() {

            return new UpdateHelper(onUpdateCheckListener, onNoUpdateCheckListener, context);
        }

        public UpdateHelper check() {
            UpdateHelper updateHelper = build();

            updateHelper.check();

            return updateHelper;
        }
    }
}
