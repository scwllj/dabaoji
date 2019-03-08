package com.axiba.chiji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseConstant {

    public static enum FloatMenuItem {
        ITEM_BACK(1),
        ITEM_REFRESH(2),
        ITEM_HOME(3);

        private final int i;

        FloatMenuItem(int i) {
            this.i = i;
        }

        public static FloatMenuItem getItem(int i) {
            if (i == 1) return ITEM_BACK;
            else if (i == 2) return ITEM_REFRESH;
            else if (i == 3) return ITEM_HOME;
            else return ITEM_BACK;
        }


    }

    protected abstract String getStartUrl();

    protected abstract String getHomeUrl();

    protected boolean isPullRefresh() {
        return false;
    }

    protected boolean isFullScreen() {
        return false;
    }

    protected boolean isShowProgressBar() {
        return true;
    }

    protected boolean isOuterWeb() {
        return false;
    }

    protected boolean isScaled() {
        return true;
    }

    protected boolean isFloadtMenu() {
        return false;
    }

    protected boolean isNeedGuide() {
        return false;
    }

    protected boolean isShowLoading() {
        return false;
    }

    protected boolean isCanGoBack() {
        return true;
    }

    protected boolean isNeedCache() {
        return true;
    }

    protected List<PopView.Menu> getPopMenu() {
        return new ArrayList<>();
    }

    protected Map<String, String> getOuterLink() {
        return new HashMap<>();
    }

    protected int[] getFloatMenuItem() {
        return new int[]{3, 1};
    }

    protected String getFloatMenuBg() {
        return "#aa000000";
    }
}
