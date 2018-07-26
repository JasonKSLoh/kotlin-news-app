@file:Suppress("unused")

package com.jason.experiments.kotlinnewsapp.util

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * ActivityUtils
 * Created by jason on 19/3/18.
 */


//Extensions
fun AppCompatActivity.addFragment(fragment: Fragment, containerId: Int, tag: String? = null) {
    val txn = supportFragmentManager.beginTransaction()
    txn.add(containerId, fragment, tag)
    txn.commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, containerId: Int) {
    val txn = supportFragmentManager.beginTransaction()
    txn.replace(containerId, fragment)
    txn.commit()
}

fun AppCompatActivity.removeFragment(fragment: Fragment) {
    val txn = supportFragmentManager.beginTransaction()
    txn.remove(fragment)
    txn.commit()
}

