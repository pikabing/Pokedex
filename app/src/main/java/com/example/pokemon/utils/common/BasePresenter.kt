package com.example.pokemon.utils.common

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : MvpView> : MvpPresenter<T> {

    override var mView: T? = null
    override var mCompositeDisposable: CompositeDisposable = CompositeDisposable()

}