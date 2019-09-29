package com.example.testoftest.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import com.example.androidassignment.utils.Constants
import com.example.testoftest.R
import com.example.testoftest.base.BaseActivity
import com.example.testoftest.dashboard.pojo.Data
import com.example.testoftest.databinding.ActivityDetailBinding
import com.example.testoftest.databinding.ActivitySearchBinding
import com.example.testoftest.storage.database.ObjectBox
import com.example.testoftest.storage.entity.GifInfo
import com.example.testoftest.storage.entity.GifInfo_
import com.example.testoftest.utils.SnackbarUtil
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.txt_title

class DetailActivity : BaseActivity(){

    private lateinit var viewdata: Data
    private lateinit var boxstore: Box<GifInfo>
    var clickhandler: ClickHandler = ClickHandler(this)
    private lateinit var binding: ActivityDetailBinding
    private var up_vote_count : Long = 0
    private var down_vote_count : Long = 0




    override fun TAG() = "DetailActivity"



    override fun getLayout() = R.layout.activity_detail



    override fun initViews(viewDataBinding: ViewDataBinding, savedInstanceState: Bundle?) {
        binding = viewDataBinding as ActivityDetailBinding
        viewdata = intent.extras!!.getSerializable(Constants.DATA) as Data
        initToolbar()
        initdatabase()



    }

    private fun initToolbar() {
        txt_title.text = intent.extras!!.getString(Constants.QUERY)
        img_back.setOnClickListener {
            finish()
        }
    }

    private fun initdatabase(){

        boxstore =  ObjectBox.boxStore!!.boxFor<GifInfo>()
        val gifinfo : List<GifInfo> = boxstore.query().equal(GifInfo_.gif_id,viewdata.id.trim()).build().find()

        if(gifinfo.size > 0){
            // data is present
            up_vote_count = gifinfo[0].up_vote_count!!
            down_vote_count = gifinfo[0].down_vote_count!!

        }else{
            // data is not present
            up_vote_count = 0
            down_vote_count = 0
        }

        viewdata.let {
            it.upvote = up_vote_count
            it.downvote = down_vote_count
        }

        binding.clickhandler = clickhandler
        binding.data=viewdata
        binding.executePendingBindings()

    }





    inner class ClickHandler(internal var detailactivity: DetailActivity) {

        fun onUpVote(view: View,model :Data) {

           up_vote_count = up_vote_count + 1
            val gifinfo : List<GifInfo> = boxstore.query().equal(GifInfo_.gif_id,model.id.trim()).build().find()
            if(gifinfo.size > 0){
                // iteam is present
                gifinfo[0].down_vote_count =down_vote_count
                gifinfo[0].up_vote_count=up_vote_count
                boxstore.put(gifinfo)

            }else{
                // iteam is notpresent
                boxstore.put(GifInfo(0,model.id,up_vote_count,down_vote_count))
            }

            down_count.text=down_vote_count.toString().trim()
            up_count.text=up_vote_count.toString().trim()

        }

        fun onDownVote(view: View,model :Data) {

            down_vote_count = down_vote_count + 1
            val gifinfo : List<GifInfo> = boxstore.query().equal(GifInfo_.gif_id,model.id.trim()).build().find()
            if(gifinfo.size > 0){
                // iteam is present
                gifinfo[0].down_vote_count =down_vote_count
                gifinfo[0].up_vote_count=up_vote_count
                boxstore.put(gifinfo)

            }else{
                // iteam is notpresent
                boxstore.put(GifInfo(0,model.id,up_vote_count,down_vote_count))
            }

            down_count.text=down_vote_count.toString().trim()
            up_count.text=up_vote_count.toString().trim()

        }
    }


}