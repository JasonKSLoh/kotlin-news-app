package com.jason.experiments.kotlinnewsapp.ui.main

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatSpinner
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.jason.experiments.kotlinnewsapp.BuildConfig
import com.jason.experiments.kotlinnewsapp.R
import com.jason.experiments.kotlinnewsapp.model.NewsResult
import com.jason.experiments.kotlinnewsapp.ui.NewsAdapter
import com.jason.experiments.kotlinnewsapp.util.ApiConsts
import com.jason.experiments.kotlinnewsapp.util.MiscUtils
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * MainFragment
 * Created by jason on 8/3/18.
 */
class MainFragment: Fragment(), MainContract.PrimaryView {
    private val KEY_RV_LAYOUT = BuildConfig.APPLICATION_ID + ".key_rv_layout"
    private val KEY_RV_DATA = BuildConfig.APPLICATION_ID + ".key_rv_data"

    lateinit var spnCategory: AppCompatSpinner
    lateinit var rvMain: RecyclerView
    lateinit var ivSearch: ImageView
    lateinit var pgLoading: ProgressBar

    private lateinit var presenter: MainContract.Presenter

    companion object {
        @JvmStatic
        public fun newInstance(): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            fragment.arguments = args
            fragment.retainInstance = true
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spnCategory = spn_category
        ivSearch = iv_search
        pgLoading = pg_loading_main
        rvMain = rv_main
        setUpSpinner()
        setUpRecyclerView()
        ivSearch.setOnClickListener({onIvSearchPressed()})
    }

    private fun setUpRecyclerView(){
        rvMain.layoutManager = LinearLayoutManager(context)
        rvMain.addItemDecoration(DividerItemDecoration(context, (rvMain.layoutManager as LinearLayoutManager).orientation))
        rvMain.adapter = NewsAdapter(ArrayList<NewsResult>())
    }

    private fun setUpSpinner(){
        val spinnerCategoryAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, ApiConsts.SECTIONS)
        spinnerCategoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnCategory.adapter = spinnerCategoryAdapter
    }

    private fun onIvSearchPressed(){
        val category = spnCategory.selectedItem.toString()
        presenter.onSearchButtonClicked(category)
    }

    override fun showProgressIndicator(shouldShow: Boolean) {
        if(shouldShow){
            pgLoading.visibility = View.VISIBLE
        } else {
            pgLoading.visibility = View.GONE
        }
    }

    override fun canFetchNews(): Boolean{
        return if (context == null || !MiscUtils.isNetworkAvailable(context!!)) {
            Toast.makeText(context, getString(R.string.no_network_message), Toast.LENGTH_SHORT).show()
            false
        } else {
            true
        }
    }

    override fun attachPresenter(pres: MainContract.Presenter) {
        presenter = pres
    }

    override fun updateNews(fetchedResults: List<NewsResult>) {
        (rvMain.adapter as NewsAdapter).updateNewsResults(fetchedResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.primaryViewDestroyed()
    }

    override fun retrievePresenter(): MainContract.Presenter {
        return this.presenter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("+_", "SAVE INSTANCE STATE CALLED")
        val rvState = rvMain.layoutManager.onSaveInstanceState()
        outState.putParcelable(KEY_RV_LAYOUT, rvState)
        outState.putSerializable(KEY_RV_DATA, (rvMain.adapter as NewsAdapter).newsResults)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val data = savedInstanceState?.getSerializable(KEY_RV_DATA)
        if(data != null){
            (rvMain.adapter as NewsAdapter).newsResults = data as ArrayList<NewsResult>
            savedInstanceState.remove(KEY_RV_DATA)
        }
        rvMain.layoutManager.onRestoreInstanceState(savedInstanceState?.getParcelable(KEY_RV_LAYOUT))
        savedInstanceState?.remove(KEY_RV_LAYOUT)
    }
}