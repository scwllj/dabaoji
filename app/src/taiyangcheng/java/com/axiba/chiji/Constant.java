package com.axiba.chiji;

import java.util.HashMap;
import java.util.Map;

public class Constant  {
    public static final String START_URL="http://sg86tl1s1iksil1lijslja.xyz/m";
    public static final String HOME_URL="http://sg86tl1s1iksil1lijslja.xyz/m";
    public static final String NAVIGATION="R.layout.navigation_layout";
    public static final Map<String,String> popMenu;
    public static final boolean PULL_REFRESH=false;
    public static final boolean SHOW_PROGRESSBAR=true;
    public static final boolean OUTER_WEB=false;

    public static final boolean NEED_GUIDE=false;
    public static final boolean SHOW_LOADING=false;
    public static final boolean CAN_GOBACK=true;
    public static final boolean NEED_CACHE=true;

    static
    {
        popMenu = new HashMap<>();
        popMenu.put("官网首页", "b");
        popMenu.put("手机版", "b");
        popMenu.put("在线客服", "b");
    }

}
