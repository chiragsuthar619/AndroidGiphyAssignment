package com.example.testoftest.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.example.androidassignment.utils.Constants
import com.example.testoftest.R
import com.example.testoftest.base.BaseActivity
import com.example.testoftest.databinding.ActivitySearchBinding
import com.example.testoftest.storage.database.ObjectBox
import com.example.testoftest.storage.entity.GifInfo
import com.example.testoftest.storage.entity.GifInfo_
import com.example.testoftest.utils.SnackbarUtil
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity(){

    var clickhandler: ClickHandler = ClickHandler(this)
    private lateinit var binding: ActivitySearchBinding




    override fun TAG() = "SearchActivity"



    override fun getLayout() = R.layout.activity_search



    override fun initViews(viewDataBinding: ViewDataBinding, savedInstanceState: Bundle?) {
        binding = viewDataBinding as ActivitySearchBinding
        setLightStatusBar(this)
        binding.clickhandler = clickhandler
        binding.executePendingBindings()

        search_edt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                hideKeyboard()
                clickhandler.onFindClicked(v!!)
                return false
            }
        })

    }




    inner class ClickHandler(internal var searchactivity: SearchActivity) {

        fun onFindClicked(view: View) {
            hideKeyboard()
            if(search_edt.text!!.trim().length > 0){
                startActivity(Intent(this@SearchActivity,GifsList::class.java).putExtra(Constants.QUERY,search_edt.text!!.toString().trim()))

            }else{
                SnackbarUtil.errorSnackbar(getString(R.string.enteryour_keyword),rootcontainer)
            }

        }
    }


}