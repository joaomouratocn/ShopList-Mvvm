package br.com.devjmcn.shoplist.util

sealed class ResponseStatus<out T>{
    object Loading : ResponseStatus<Nothing>()
    object EmptyData: ResponseStatus<Nothing>()
    data class ShowData<T>(val data: T):ResponseStatus<T>()
}
