package cn.libery.switchhost;

/**
 * @author shizhiqiang on 2018/5/15.
 */
public class AppUtil {

    public static boolean canSwitchHost(){
        return BuildConfig.DEBUG;
    }
}
