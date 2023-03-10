package com.ghennadiiganenko.android.ghennadiiganenko_online_shop.fragment.page1.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.*
import com.ghennadiiganenko.android.ghennadiiganenko_online_shop.domain.db.model.User
import com.ghennadiiganenko.android.ghennadiiganenko_online_shop.domain.db.usecase.GetUserUseCase
import com.ghennadiiganenko.android.ghennadiiganenko_online_shop.domain.network.model.FlashSaleListEntity
import com.ghennadiiganenko.android.ghennadiiganenko_online_shop.domain.network.model.LatestListEntity
import com.ghennadiiganenko.android.ghennadiiganenko_online_shop.domain.network.usecase.GetFlashSaleUseCase
import com.ghennadiiganenko.android.ghennadiiganenko_online_shop.domain.network.usecase.GetLatestUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Page1ViewModel(
    private val getLatestUseCase: GetLatestUseCase,
    private val getFlashSaleUseCase: GetFlashSaleUseCase,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user

    private val _latestList: MutableLiveData<LatestListEntity> = MutableLiveData()

    private val _flashSaleList: MutableLiveData<FlashSaleListEntity> = MutableLiveData()

    val productsLists: MediatorLiveData<Pair<LatestListEntity?, FlashSaleListEntity?>> =
        MediatorLiveData<Pair<LatestListEntity?, FlashSaleListEntity?>>().apply {
            addSource(_latestList) { value = Pair(it, _flashSaleList.value) }
            addSource(_flashSaleList) { value = Pair(_latestList.value, it)}
        }

    @SuppressLint("SuspiciousIndentation")
    fun getUser(firstName: String) = viewModelScope.launch(Dispatchers.IO) {
        val result = getUserUseCase(firstName = firstName)
        withContext(Dispatchers.Main) {
            result.let {
                _user.value = result ?: User(0,"","","","")
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getLatestList() = viewModelScope.launch(Dispatchers.IO) {
        val result = getLatestUseCase()
            withContext(Dispatchers.Main) {
                result?.let {
                    _latestList.value = result ?: LatestListEntity(listOf())
                }
            }
    }

    @SuppressLint("SuspiciousIndentation")
    fun getFlashSaleList() = viewModelScope.launch(Dispatchers.IO) {
        val result = getFlashSaleUseCase()
            withContext(Dispatchers.Main) {
                result?.let {
                    _flashSaleList.value = result ?: FlashSaleListEntity(listOf())
                }
            }
    }



}