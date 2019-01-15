package com.tq.clickfunnel.lambda.handler;

import java.util.HashMap;
import java.util.Map;

import com.tq.common.lambda.config.Config;

public enum OptInType {
    DEFAULT_OPTIN(Config.INFUSION_CLICKFUNNEL_OPTIN_TAG),
    YOUTYBE_OPT(Config.INFUSION_CLICKFUNNEL_YOUTUBE_OPTIN_TAG),
    GOOGLE_AD_OPT(Config.INFUSION_CLICKFUNNEL_GOOGLE_AD_OPTIN_TAG),
    FACEBOOK_LEAD(Config.INFUSION_CLICKFUNNEL_FACEBOOK_LEAD_OPTIN_TAG),
    WANTS_TO_QUIT(Config.INFUSION_CLICKFUNNEL_WANTSTOQUIT_OPTIN_TAG)
    ;
    
    private static Map<String, OptInType> mapTagIDs = new HashMap<String, OptInType>() {{
        put("Optin", DEFAULT_OPTIN);
        put("YoutubeOptin", YOUTYBE_OPT);
        put("GoogleOptin", GOOGLE_AD_OPT);
        put("FacebookLead", FACEBOOK_LEAD);
        put("Wantstoquit", WANTS_TO_QUIT);
    }};

    private String tag;
    
    OptInType(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
    
    public static OptInType parse(String type) {
        return mapTagIDs.get(type);
    }
    
}
