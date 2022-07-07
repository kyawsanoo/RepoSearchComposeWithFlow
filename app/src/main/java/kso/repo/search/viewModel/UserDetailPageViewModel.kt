package kso.repo.search.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kso.repo.search.model.User
import javax.inject.Inject

@HiltViewModel
class UserDetailPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val jsonString = savedStateHandle.get<String>("user")
    private val argUser = jsonString?.fromJson(User::class.java)
    val user = MutableStateFlow(argUser)
    private val tag: String = "UserDetailViewModel"

    init {
        user.toJson()?.let { Log.e(tag, "user: $it" ) };
    }

    private fun <A> String.fromJson(type: Class<A>): A {
        return Gson().fromJson(this, type)
    }


    private fun <A> A.toJson(): String? {
        return Gson().toJson(this)
    }

}