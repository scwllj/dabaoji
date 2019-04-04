package com.axiba.chiji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant extends BaseConstant {
    public static final String START_URL = "https://www.baidu.com";
    public static final String HOME_URL = "https://hscp.i8app.app";
    public static final String NAVIGATION = "R.layout.navigation_layout";

    public static final boolean PULL_REFRESH = false;
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
        popMenu.add(new PopView.Menu("主页","http://43.249.204.90:85"));
        popMenu.add(new PopView.Menu("手机版", "http://43.249.204.90:86"));
        popMenu.add(new PopView.Menu("在线客服", "http://43.249.204.90:8011"));
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
        return new int[]{2, 1, 4};
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
}
