package com.sevenpick.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by Administrator on 2017/8/14.
 */

public class SampleApplication extends TinkerApplication {
    public SampleApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.sevenpick.app.SampleApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
