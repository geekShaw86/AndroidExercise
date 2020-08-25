package nz.co.shawcong.androidtest

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import nz.co.shawcong.androidtest.manager.LogManager

/**
 *
 * Created by Shaw Cong on 25/08/20
 */
@HiltAndroidApp
class AndroidExerciseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        LogManager.init()
    }
}