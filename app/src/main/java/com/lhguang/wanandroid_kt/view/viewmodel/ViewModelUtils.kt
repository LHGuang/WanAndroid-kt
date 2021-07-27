package com.lhguang.wanandroid_kt.view.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewmodels() =
    lazy {
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(VM::class.java)
    }

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewmodels(application: Application) =
    lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(VM::class.java)
    }