package com.example.testoftest.dashboard.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidassignment.utils.Constants
import com.example.testoftest.R
import com.example.testoftest.base.BaseActivity
import com.example.testoftest.dashboard.pojo.Data
import com.example.testoftest.dashboard.pojo.Gifs
import com.example.testoftest.dashboard.viewmodel.Gifsviewmodel
import com.example.testoftest.databinding.GifslistActivityBinding
import com.example.testoftest.databinding.RowUserinfolistBinding
import com.example.testoftest.helper.adapter.RecyclerCallback
import com.example.testoftest.helper.adapter.UniversalAdapter
import com.example.testoftest.helper.extention.hideView
import com.example.testoftest.helper.extention.showView
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.gifslist_activity.*
import kotlinx.android.synthetic.main.toolbar.*


class GifsList : BaseActivity() {


    private lateinit var mAdapter: UniversalAdapter<Data, RowUserinfolistBinding>
    private lateinit var binding: GifslistActivityBinding
    private var gifsinfo_list: Gifs? = null
    lateinit var mViewModel: Gifsviewmodel
    var clickhandler: ClickHandler = ClickHandler(this)


    companion object {


    }

    /**
     * @param TAG: activity tag to filter out any log statement
     */
    override fun TAG() = "GifsList"

    /**
     * put your root layout here
     */
    override fun getLayout() = R.layout.gifslist_activity


    /**
     * Initialise :
     *
     * @param viewDataBinding : view binding (root layout)
     */

    override fun initViews(viewDataBinding: ViewDataBinding, savedInstanceState: Bundle?) {
        binding = viewDataBinding as GifslistActivityBinding
        binding.myswipeRefreshlayout.setOnRefreshListener {
            getUserInfo(true)
        }
        binding.retry.setOnClickListener {
            getUserInfo(true)
        }
        initToolbar()
        initViewModel()

    }

    /**
     * Toolbar initialization
     */

    private fun initToolbar() {
        loge(TAG(),"after -- "+intent.extras!!.getString(Constants.QUERY))
        txt_title.text = intent.extras!!.getString(Constants.QUERY)
        img_back.setImageResource(R.drawable.ic_back)
        img_back.setOnClickListener {
            finish()
        }
    }


    /**
     * Viewmodel initialization
     */

    private fun initViewModel() {
        mViewModel = ViewModelProviders.of(this).get(Gifsviewmodel::class.java)

        mViewModel.loadingVisibility.observe(this, Observer { anyprogress ->

            // any progressloader you want to show/hide
            binding.errorView.visibility = View.GONE

            if (anyprogress) {
                binding.myswipeRefreshlayout.isRefreshing = true
            } else {
                binding.myswipeRefreshlayout.isRefreshing = false

            }
        })

        getUserInfo(false)

    }


    /**
     * @param flag : If API call is forcefully applied (true/false)
     *
     * */

    fun getUserInfo(flag: Boolean) {

        val map: HashMap<String, String> = HashMap()
        map.put(Constants.API_KEY, Constants.API_VALUE)
        map.put(Constants.Q, intent.extras!!.getString(Constants.QUERY) ?: "")
        map.put(Constants.LIMIT, "100")
        map.put(Constants.OFFSET, "0")
        map.put(Constants.RATING, "G")
        map.put(Constants.LANG, "en")

        mViewModel.getGifs(flag, map).observe(this, Observer { liveDataWrapper ->

            loge(TAG(), "" + isSuccess(liveDataWrapper).first)
            if (isSuccess(liveDataWrapper).first) {
                gifsinfo_list = liveDataWrapper.data as Gifs
                if(gifsinfo_list!!.data.size > 0){
                    // refresh ui
                    onUIRefresh()
                }else{
                    binding.errorTxt.text = getString(R.string.nodatafound)
                    binding.errorView.visibility = View.VISIBLE
                }

            }
        })

    }

    /**
     * error from API call
     * @param error_msg : error message
     */

    override fun isInternetConnect(error_msg: String) {
        super.isInternetConnect(error_msg)

        // show error : as you like


        if (::mAdapter.isInitialized) {
            showError(error_msg)
        } else {
            binding.errorTxt.text = error_msg
            binding.errorView.visibility = View.VISIBLE
        }


    }


    /**
     * refresh any UI contain from here
     */

    private fun onUIRefresh() {

        loge(TAG(), "on uirefresh--")
        // Refresh screen layout and parms.

        gifsinfo_list?.let {


            recyclerview.apply {

                if (::mAdapter.isInitialized) {

                    mAdapter.notifyDataSetChanged()

                } else {
                    mAdapter =
                        UniversalAdapter(this@GifsList,
                            gifsinfo_list!!.data, R.layout.row_userinfolist, object :
                                RecyclerCallback<RowUserinfolistBinding, Data> {
                                override fun bindData(binder: RowUserinfolistBinding, model: Data) {
                                    binder.data = model
                                    binder.clickhandler=clickhandler
                                    binder.executePendingBindings()
                                }
                            })
                    layoutManager = GridLayoutManager(this@GifsList, 3)
                    adapter = mAdapter
                }


            }


        }


    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    inner class ClickHandler(internal var gifslist: GifsList) {

        fun onItemClicked(view: View,model: Data) {
            val bundle = Bundle()
            bundle.putSerializable(Constants.DATA,model)
            bundle.putString(Constants.QUERY,intent.extras!!.getString(Constants.QUERY))
            startActivity(Intent(this@GifsList,DetailActivity::class.java).putExtras(bundle))


        }
    }


}
