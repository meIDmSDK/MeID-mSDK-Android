package sk.minv.meid_sample_app.ui.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.base.base.interactor.onFailure
import sk.minv.base.base.interactor.onSuccess
import sk.minv.base.utils.helpers.launch
import sk.minv.meid_sample_app.data.GetTokenResponse
import sk.minv.meid_sample_app.data.GetTokenUseCaseParams
import sk.minv.meid_sample_app.interactors.usecase.getToken.GetTokenUseCase
import sk.minv.meid_sample_app.interactors.usecase.login.LoginUseCase
import sk.minv.meid_sample_app.interactors.usecase.logout.LogoutUseCase
import sk.minv.meid_sample_app.utils.AppConstants

class MainViewModel(
    private val loginUseCase: LoginUseCase,
    private val getTokenUseCase: GetTokenUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    /*-------------------------*/
    /*          ENUMS          */
    /*-------------------------*/

    enum class Action {
        LOGOUT
    }

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val _loginActionDataLoadState = MutableLiveData<DataLoadState<Uri>>()
    val loginActionDataLoadState: LiveData<DataLoadState<Uri>> = _loginActionDataLoadState

    private val _deeplinkActionDataLoadState = MutableLiveData<DataLoadState<Action>>()
    val deeplinkActionDataLoadState: LiveData<DataLoadState<Action>> = _deeplinkActionDataLoadState

    private val _getTokenDataLoadState = MutableLiveData<DataLoadState<GetTokenResponse>>()
    val getTokenDataLoadState: LiveData<DataLoadState<GetTokenResponse>> = _getTokenDataLoadState

    private val _logoutActionDataLoadState = MutableLiveData<DataLoadState<Uri>>()
    val logoutActionDataLoadState: LiveData<DataLoadState<Uri>> = _logoutActionDataLoadState

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    // Create Login URL
    fun login() {
        launch {
            loginUseCase()
                .onSuccess { _loginActionDataLoadState.postValue(DataLoadState.Success(it)) }
                .onFailure { _loginActionDataLoadState.postValue(DataLoadState.Error(it)) }
        }
    }

    // Handle Deeplink
    fun onDeeplinkReceived(scheme: String?, host: String?, parameters: Map<String, String>?) {
        if (scheme == AppConstants.DEEPLINK_SCHEME) {
            if (host == AppConstants.DEEPLINK_HOST_ACCOUNT) {
                val authorizationCode = parameters?.get(AppConstants.RESPONSE_TYPE_CODE)
                if (authorizationCode != null) {
                    getToken(authorizationCode)
                } else {
                    _deeplinkActionDataLoadState.postValue(DataLoadState.Error(Exception("Missing authorization code")))
                }
            } else if (host == AppConstants.DEEPLINK_HOST_LOGOUT) {
                _deeplinkActionDataLoadState.postValue(DataLoadState.Success(Action.LOGOUT))
            } else {
                _deeplinkActionDataLoadState.postValue(DataLoadState.Error(Exception("Invalid Deeplink host")))
            }
        } else {
            _deeplinkActionDataLoadState.postValue(DataLoadState.Error(Exception("Invalid Deeplink scheme")))
        }
    }

    // Create Logout URL
    fun logout() {
        launch {
            logoutUseCase()
                .onSuccess { _logoutActionDataLoadState.postValue(DataLoadState.Success(it)) }
                .onFailure { _logoutActionDataLoadState.postValue(DataLoadState.Error(it)) }
        }
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    // Following function should be on your backend.
    // Send authorization code to your backend and handle tokens there
    private fun getToken(authorizationCode: String) {
        // Notify UI - Loading
        _getTokenDataLoadState.postValue(DataLoadState.Loading())

        launch {
            val params = GetTokenUseCaseParams(authorizationCode)
            getTokenUseCase(params)
                .onSuccess {
                    // Notify UI - Show user token
                    _getTokenDataLoadState.postValue(DataLoadState.Success(it))
                }
                .onFailure {
                    // Notify UI - Show error
                    _getTokenDataLoadState.postValue(DataLoadState.Error(it))
                }
        }
    }
}