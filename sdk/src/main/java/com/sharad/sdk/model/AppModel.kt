package com.sharad.sdk.model

import android.graphics.drawable.Drawable

/**
 * Created by Sharad-PC on 04-06-2021.
 */
class AppModel {
    lateinit var appIcon: Drawable
    var appName: String? = null
    var packageName: String? = null
    var activityName: String? = null
    var versionCode = 0L
    var versionName: String? = null

}