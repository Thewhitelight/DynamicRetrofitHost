package cn.libery.switchhost;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Libery on 2015/8/4.
 * Email:libery.szq@qq.com
 */
public class PixelUtil {

    private PixelUtil() {
        //
    }

    /**
     * 获取屏幕宽度
     */
    public static int getWith() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int dp2px(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dp * density);
    }

    /**
     * dp转px.
     *
     * @param value the value
     * @return the int
     */
    public static int dp2px(float value) {
        final float scale = Resources.getSystem().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    /**
     * px转dp.
     *
     * @param value the value
     * @return the int
     */
    public static int px2dp(float value) {
        final float scale = Resources.getSystem().getDisplayMetrics().densityDpi;
        return (int) ((value * 160) / scale + 0.5f);
    }

    /**
     * sp转px.
     *
     * @param value the value
     * @return the int
     */
    public static int sp2px(float value) {
        float spvalue = value * Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spvalue + 0.5f);
    }

    /**
     * px转sp.
     *
     * @param value the value
     * @return the int
     */
    public static int px2sp(float value) {
        final float scale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (value / scale + 0.5f);
    }

}


