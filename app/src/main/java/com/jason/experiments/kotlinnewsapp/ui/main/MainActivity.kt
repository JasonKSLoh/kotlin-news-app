package com.jason.experiments.kotlinnewsapp.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.jason.experiments.kotlinnewsapp.BuildConfig
import com.jason.experiments.kotlinnewsapp.R
import com.jason.experiments.kotlinnewsapp.util.addFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainContract.ParentView {

    private val TAG_MAIN_FRAGMENT = BuildConfig.APPLICATION_ID + "main_fragment"

    lateinit var presenter: MainPresenter
    lateinit var mainFragment: MainFragment
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUp()
    }

    fun setUp() {
        toolbar = toolbar_main
        setSupportActionBar(toolbar)
        val retrievedFragment = supportFragmentManager.findFragmentByTag(TAG_MAIN_FRAGMENT)
        if(retrievedFragment != null){
            mainFragment = retrievedFragment as MainFragment
            presenter = mainFragment.retrievePresenter() as MainPresenter
        } else {
            mainFragment = MainFragment.newInstance()
            addFragment(mainFragment, R.id.main_container, TAG_MAIN_FRAGMENT)
            presenter = MainPresenter(this, mainFragment)
            mainFragment.attachPresenter(presenter)
        }
    }

}
