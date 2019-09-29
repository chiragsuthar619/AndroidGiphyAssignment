package com.example.testoftest.base


import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.androidassignment.network.LiveDataWrapper
import com.example.testoftest.BuildConfig
import com.example.testoftest.R
import com.example.testoftest.helper.extention.isJSONValid
import com.example.testoftest.network.APIConstants
import com.example.testoftest.network.APIConstants.CONNECTION_RESET
import com.example.testoftest.network.APIConstants.NO_INTERNET
import com.example.testoftest.network.APIConstants.UNAUTHORIZED
import com.example.testoftest.network.model.ErrorModel
import com.example.testoftest.utils.SnackbarUtil
import com.google.gson.Gson


abstract class BaseActivity : AppCompatActivity() {


    protected abstract fun TAG(): String

    /**
     * parent class of binding
     */
    private lateinit var mBinding: ViewDataBinding

    /**
     * abstract method for set view
     *
     * @return int (R.layout.XXX)
     */
    protected abstract fun getLayout(): Int


    open fun isInternetConnect(error_msg : String) {}

    /**
     * abstract method to initialize components
     */
    protected abstract fun initViews(viewDataBinding: ViewDataBinding, savedInstanceState: Bundle?)

    /**
     * rootview is parent view
     */
    lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loge(TAG(), "onCreate--" + savedInstanceState)
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mBinding = DataBindingUtil.setContentView(this, getLayout())
        // mBinding.setVariable(BR.click, this@BaseAct)
        rootView = mBinding.root
        initViews(mBinding, savedInstanceState)
    }

    /**
     * log the data
     */

    fun loge(tag: String, msg: String) {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg)
    }

    fun setLightStatusBar( activity: Activity) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            var flags = activity.getWindow().getDecorView().systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.getWindow().getDecorView().systemUiVisibility = flags
            activity.window.statusBarColor = ContextCompat.getColor(activity,R.color.colorAccent)
        }
    }


    /**
     * Check response success or error
     * @return Pair(isResponse true or false, response code)
     */
    fun <T> isSuccess(liveDataWrapper: LiveDataWrapper<T>?): Pair<Boolean, Int> {
        return if (liveDataWrapper?.errorMessage != null) {
            /**
             * Check if error json or String
             */
            if (liveDataWrapper.errorMessage!!.first.isJSONValid()) {

                val mError =
                    Gson().fromJson(liveDataWrapper.errorMessage!!.first, ErrorModel::class.java)

                if (liveDataWrapper.errorMessage!!.second == APIConstants.INTERNAL_SERVER_ERROR) {

                    isInternetConnect(getString(R.string.internal_server_error))

                } else if (liveDataWrapper.errorMessage!!.second == UNAUTHORIZED) {

                    isInternetConnect(getString(R.string.internal_server_error))

                } else if (liveDataWrapper.errorMessage!!.second == APIConstants.NOT_FOUND || liveDataWrapper.errorMessage!!.second == CONNECTION_RESET) {

                        isInternetConnect(getString(R.string.internal_server_error))

                } else {

                    if (mError.error != null && mError.error.toString().length > 1) {
                        isInternetConnect(mError.error)
                    } else {
                        isInternetConnect(mError.message)
                    }
                }

                Pair(false, liveDataWrapper.errorMessage!!.second)

            } else if (liveDataWrapper.errorMessage!!.second == NO_INTERNET) {

                isInternetConnect(getString(R.string.internet_not_available))
                Pair(false, liveDataWrapper.errorMessage!!.second)

            } else {
                isInternetConnect(liveDataWrapper.errorMessage!!.first)
                Pair(false, liveDataWrapper.errorMessage!!.second)
            }
        } else {
            Pair(true, APIConstants.BAD_REQUEST)
        }
    }

    /**
     * Showing success snackbar
     */
    fun showSuccess(msg: String, listener: SnackbarUtil.OnSnackbarDismiss? = null) {
        SnackbarUtil.successSnackbar(msg, rootView, listener)
    }

    /**
     * showing error snackbar
     */
    fun showError(msg: String, listener: SnackbarUtil.OnSnackbarDismiss? = null) {
        SnackbarUtil.errorSnackbar(msg, rootView, listener)
    }

    /**
     * hide keyboard
     */
    fun hideKeyboard() {
        try {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * show toast
     */

    fun showToast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show()
    }



}