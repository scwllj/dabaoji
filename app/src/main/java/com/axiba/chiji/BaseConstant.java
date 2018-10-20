package com.axiba.chiji;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseConstant {
    protected abstract String getStartUrl();
    protected abstract String getHomeUrl();

    protected boolean isPullRefresh(){
        return false;
    }
    protected boolean isFullScreen(){
        return false;
    }
    protected boolean isShowProgressBar(){
        return true;
    }
    protected boolean isOuterWeb(){
        return false;
    }
    protected boolean isScaled(){
        return true;
    }
    protected boolean isFloadtMenu(){
        return false;
    }

    protected boolean isNeedGuide(){
        return false;
    }

    protected boolean isShowLoading(){
        return false;
    }

    protected boolean isCanGoBack(){
        return true;
    }

    protected boolean isNeedCache(){
        return true;
    }

    protected List<PopView.Menu> getPopMenu(){
        return new ArrayList<>();
    }

    protected Map<String, String> getOuterLink(){
        return new HashMap<>();
    }
}
