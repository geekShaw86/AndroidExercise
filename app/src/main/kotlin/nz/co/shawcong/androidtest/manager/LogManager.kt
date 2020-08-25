package nz.co.shawcong.androidtest.manager

import nz.co.shawcong.androidtest.BuildConfig
import timber.log.Timber

/**
 *
 * Created by Shaw Cong on 25/08/20
 */
object LogManager {
    fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}