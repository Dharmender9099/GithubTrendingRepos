package com.example.trendingrepos.ui.trendingrepos

import androidx.lifecycle.*
import com.example.trendingrepos.component.handleError
import com.example.trendingrepos.data.ApiResult
import com.example.trendingrepos.data.TrendingRepo
import com.example.trendingrepos.network.TrendingReposRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TrendingReposViewModel @Inject constructor(private val trendingReposRepository: TrendingReposRepository) :
    ViewModel() {
    /** Describe coroutine scope */
    private val coroutineScope = viewModelScope.coroutineContext + Job()
    private val trendingRepos = MutableLiveData(DEFAULT_QUERY)

    /** Trending github repos item list */
    private var trendingReposList = listOf<TrendingRepo>()

    /**
     * Retry to fetch github trending repos
     */
    fun retry() {
        trendingRepos.value = DEFAULT_QUERY
    }

    /**  Initiate http request to fetch github trending repos */
    val repos = trendingRepos.switchMap { query ->
        liveData(context = coroutineScope) {
            try {
                emit(ApiResult.Loading())
                val result = trendingReposRepository.getTrendingRepos(query)
                trendingReposList = result.items
                emit(ApiResult.Success(getTrendingReposList()))
            } catch (ex: Exception) {
                emit(ApiResult.Error(ex.handleError(), null))
            }
        }
    }

    /** Trending repos collections*/
    private fun getTrendingReposList() = trendingReposList

    /** Update the status of selected item
     * @param selectedPos: Selected item position
     * @param onComplete: Invoke fun with update trending list
     * @return Trending list with updated status for selected item
     */
    fun updateSelectedItemStatus(selectedPos: Int, onComplete: (List<TrendingRepo>) -> Unit) {
        val trendingList = getTrendingReposList()
        val selectedItem = trendingList[selectedPos]
        selectedItem.isSelected = !selectedItem.isSelected
        trendingList.toMutableList()[selectedPos] = selectedItem
        onComplete.invoke(trendingList)
    }

    /** Search the item inside trending repos and filter out relevant list
     * @param searchQuery: Search query
     * @return Filter list
     */
    fun filterTrendingRepoList(searchQuery: String, onComplete: (List<TrendingRepo>) -> Unit) {
        val newText = searchQuery.lowercase(Locale.getDefault())
        val filteredDataList = arrayListOf<TrendingRepo>()
        for (dataFromDataList in getTrendingReposList()) {
            val name = dataFromDataList.name.lowercase(Locale.ROOT)
            val fullName = dataFromDataList.fullName.lowercase(Locale.ROOT)
            val desc = dataFromDataList.description?.lowercase(Locale.ROOT)
            if (name.contains(newText) || fullName.contains(newText) || desc?.contains(newText) == true) {
                filteredDataList.add(dataFromDataList)
            }
        }
        onComplete.invoke(filteredDataList)
    }

    /** Clear coroutine scope */
    override fun onCleared() {
        super.onCleared()
        coroutineScope.cancel() //to prevent leak
    }

    companion object {
        private const val DEFAULT_QUERY = "language:Java"
    }
}