package com.lahmloon.occupations_isic_code

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.lahmloon.occupations_isic_code_sdk.occupations.data.Occupations
import com.lahmloon.occupations_isic_code_sdk.OccupationsSdk
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * Performs occupation search
 */
class MainActivityModel(application: Application) : AndroidViewModel(application) {
    private var subscription: Disposable
    private val searchSubject = PublishSubject.create<String>()

    /**
     * Search
     */
    fun search(string: String) {
        searchSubject.onNext(string)
    }

    /**
     * Search results
     */
    val occupations = MutableLiveData<List<Occupations>>()

    init {
        subscription = searchSubject
            .debounce(300L, TimeUnit.MILLISECONDS, Schedulers.computation())
            .filter { it.isNotBlank() }
            .switchMap {
                Observable
                    .fromCallable {
                        OccupationsSdk.instance(this.getApplication()).searchOccupationsByIsicCode("$it%", 30)
                    }
                    .subscribeOn(Schedulers.computation())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { searchResult -> occupations.value = searchResult },
                { error -> throw error }
            )
    }

    /**
     * This method will be called when this ViewModel is no longer used and will be destroyed.
     */
    override fun onCleared() {
        super.onCleared()
        subscription.dispose()
    }
}