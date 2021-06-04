package com.sharad.sdk.viewmodel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sharad.sdk.model.AppModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Sharad-PC on 04-06-2021.
 */
class MainViewModel(context: Context) : ViewModel() {
    private var appLiveData: MutableLiveData<ArrayList<AppModel>?> = MutableLiveData()
    private var appArrayList: ArrayList<AppModel>? = null
    val appMutableLiveData: MutableLiveData<ArrayList<AppModel>?>
        get() = appLiveData

    init {
        GlobalScope.launch {
            getAppListFromDevice(context)

            appLiveData.postValue(appArrayList)
        }
    }


    private fun getAppListFromDevice(context: Context) {
        appArrayList = ArrayList()
        val packageManager: PackageManager = context.packageManager
        val list = packageManager.getInstalledApplications(0)
        list.sortWith(Comparator { o1, o2 ->
            o1.loadLabel(packageManager).toString()
                .compareTo(o2.loadLabel(packageManager).toString(), ignoreCase = true)
        })
        for (info in list) {
            if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                val app = AppModel()
                app.packageName = info.packageName
                app.appName = info.loadLabel(packageManager).toString()
                val packageInfo: PackageInfo =
                    context.packageManager.getPackageInfo(info.packageName, 0)
                val mainIntent = Intent(Intent.ACTION_MAIN, null)
                mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                val appList: List<ResolveInfo> =
                    packageManager.queryIntentActivities(mainIntent, 0)
                for (temp in appList) {
                    app.activityName = temp.activityInfo.name
                }
                app.versionName = packageInfo.versionName
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    app.versionCode = packageInfo.longVersionCode
                } else {
                    app.versionCode = packageInfo.versionCode.toLong()
                }
                val icon:  Drawable=context.packageManager.getApplicationIcon(info.packageName)
                app.appIcon=icon
                appArrayList?.add(app)
            }
        }
        appArrayList?.sortWith(Comparator { o1, o2 ->
            if (o1.appName == null || o2.appName == null) -1 else o1.appName!!
                .compareTo(o2.appName!!, ignoreCase = true)
        })


    }

}