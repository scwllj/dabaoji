package com.axiba.chiji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.axiba.chiji.BaseConstant.FloatMenuItem.ITEM_CLEAR;
import static com.axiba.chiji.BaseConstant.FloatMenuItem.ITEM_HOME;
import static com.axiba.chiji.BaseConstant.FloatMenuItem.ITEM_REFRESH;

public class Constant extends BaseConstant {
    public static final String START_URL = "http://www.hzctz888.com";
    public static final String HOME_URL = "http://35.194.242.223:8131/leDuCPLoginWeb/app/home";
    public static final String NAVIGATION = "R.layout.navigation_layout";

    public static final boolean PULL_REFRESH = true;
    public static final boolean FULL_SCREEN = false;
    public static final boolean SHOW_PROGRESSBAR = true;
    public static final boolean OUTER_WEB = false;
    public static final boolean SCALED = true;
    public static final boolean FLOAT_MENU = false;

    public static final boolean NEED_GUIDE = false;
    public static final boolean SHOW_LOADING = false;
    public static final boolean CAN_GOBACK = true;
    public static final boolean NEED_CACHE = true;
    public static final List<PopView.Menu> popMenu = new ArrayList<>();
    public static final Map<String, String> outerLink = new HashMap<>();
    static {

        }

    @Override
    protected String getStartUrl() {
        return START_URL;
    }

    @Override
    protected String getHomeUrl() {
        return HOME_URL;
    }

    @Override
    protected int[] getFloatMenuItem() {
        return null;
    }

    @Override
    protected boolean isNeedCache() {
        return NEED_CACHE;
    }

    @Override
    protected boolean isPullRefresh() {
        return PULL_REFRESH;
    }

    @Override
    protected boolean isOuterWeb() {
        return OUTER_WEB;
    }

    @Override
    protected boolean isShowProgressBar() {
        return SHOW_PROGRESSBAR;
    }

    @Override
    protected List<SliderMenu> getSliderMenu() {
        List<SliderMenu> list = new ArrayList<>();
        FloatMenuItem[] actions = {ITEM_HOME, ITEM_REFRESH, ITEM_CLEAR};
        String[] names = {"首页", "刷新", "清除缓存"};
        int[] iconIds = {R.drawable.home, R.drawable.refresh, R.drawable.clear};
        for (int i = 0; i < 3; i++) {
            list.add(new SliderMenu(names[i], actions[i], iconIds[i]));
        }
        return list;
    }
}
