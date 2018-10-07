package com.github.windsekirun.rxsociallogin

import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.github.windsekirun.rxsociallogin.intenal.exception.LoginFailedException
import com.github.windsekirun.rxsociallogin.intenal.impl.OnResponseListener
import com.github.windsekirun.rxsociallogin.intenal.model.LoginResultItem
import com.github.windsekirun.rxsociallogin.intenal.model.PlatformType
import com.github.windsekirun.rxsociallogin.intenal.utils.weak
import com.github.windsekirun.rxsociallogin.kakao.KakaoSDKAdapter
import io.reactivex.disposables.CompositeDisposable

/**
 * RxSocialLogin
 * Class: BaseSocialLogin
 * Created by Pyxis on 10/7/18.
 *
 * Description:
 */
abstract class BaseSocialLogin @JvmOverloads constructor(childActivity: FragmentActivity? = null) {
    internal var responseListener: OnResponseListener? = null

    protected val kakaoSDKAdapter: KakaoSDKAdapter by lazy { KakaoSDKAdapter(activity!!.applicationContext) }
    protected val compositeDisposable = CompositeDisposable()
    protected var activity: FragmentActivity? by weak(null)
    protected val TAG = RxSocialLogin::class.java.simpleName

    init {
        if (childActivity != null) { // using given activity object when creating instance
            this.activity = childActivity
        } else {
            this.activity = RxSocialLogin.activityReference
            if (this.activity == null) throw LoginFailedException(RxSocialLogin.NOT_HAVE_APPLICATION)
        }
    }

    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    abstract fun login()

    open fun onDestroy() {
        compositeDisposable.clear()
    }

    @JvmOverloads
    open fun logout(clearToken: Boolean = false) {

    }

    protected fun callbackFail(platformType: PlatformType) {
        callbackItem(LoginResultItem.createFail(platformType))
    }

    protected fun callbackItem(loginResultItem: LoginResultItem) {
        responseListener?.onResult(loginResultItem)
    }
}