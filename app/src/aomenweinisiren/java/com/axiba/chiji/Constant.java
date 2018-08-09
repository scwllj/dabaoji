package com.axiba.chiji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Constant extends BaseConstant{
    public static final String START_URL = "http://222.186.30.220:8099";
    public static final String HOME_URL = "http://222.186.30.220:8099";
    public static final String NAVIGATION = "R.layout.navigation_layout";

    public static final boolean PULL_REFRESH = false;
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
        popMenu.add(new PopView.Menu("主页", "http://43.249.204.90:85"));
        popMenu.add(new PopView.Menu("手机版", "http://43.249.204.90:86"));
        popMenu.add(new PopView.Menu("在线客服", "http://43.249.204.90:8011"));

        outerLink.put("https://33382rr.com/", "http://222.186.30.220:801");
        outerLink.put("https://33382bb.com/", "http://222.186.30.220:802");
        outerLink.put("https://33382qq.com/", "http://222.186.30.220:803");
        outerLink.put("https://33382cc.com/", "http://222.186.30.220:804");
        outerLink.put("https://33382ii.com/", "http://222.186.30.220:805");
    }

}
