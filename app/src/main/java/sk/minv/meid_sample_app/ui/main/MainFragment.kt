package sk.minv.meid_sample_app.ui.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.core.component.KoinComponent
import sk.minv.base.base.fragment.BaseFragment
import sk.minv.meid_sample_app.databinding.FragmentMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import sk.minv.base.base.interactor.DataLoadState
import sk.minv.base.utils.common.DialogUtils
import sk.minv.base.utils.helpers.onClick
import sk.minv.base.utils.helpers.subscribe
import sk.minv.meid_sample_app.R
import sk.minv.meid_sample_app.data.GetTokenResponse
import sk.minv.meid_sample_app.ui.main.MainViewModel.Action

class MainFragment : BaseFragment<FragmentMainBinding, MainHandler>(), KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val viewModel: MainViewModel by viewModel()

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun subscribeToData() {
        viewModel.loginActionDataLoadState.subscribe(this, ::handleLoginActionDataLoadState)
        viewModel.deeplinkActionDataLoadState.subscribe(this, ::handleDeeplinkActionDataLoadState)
        viewModel.getTokenDataLoadState.subscribe(this, ::handleGetTokenLoadState)
        viewModel.logoutActionDataLoadState.subscribe(this, ::handleLogoutActionDataLoadState)
    }

    override fun onViewReady() {
        binding.btnLogIn.visibility = View.VISIBLE
        binding.btnLogOut.visibility = View.INVISIBLE

        binding.btnLogIn.onClick {
            viewModel.login()
        }

        binding.btnLogOut.onClick {
            viewModel.logout()
        }
    }

    /*-------------------------*/
    /*      PUBLIC METHODS     */
    /*-------------------------*/

    fun onDeeplinkReceived(scheme: String?, host: String?, parameters: Map<String, String>?) {
        viewModel.onDeeplinkReceived(scheme, host, parameters)
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    // Open Login URL
    private fun handleLoginActionDataLoadState(dataLoadState: DataLoadState<Uri>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> handler.openUri(dataLoadState.data)
            is DataLoadState.Error -> showError(dataLoadState.error)
        }
    }

    // Handle deeplink action - Logout
    private fun handleDeeplinkActionDataLoadState(dataLoadState: DataLoadState<Action>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> {
                if (dataLoadState.data == Action.LOGOUT) {
                    binding.btnLogIn.visibility = View.VISIBLE
                    binding.btnLogOut.visibility = View.INVISIBLE
                    showLogoutSuccess()
                }
            }
            is DataLoadState.Error -> showError(dataLoadState.error)
        }
    }

    // Show dialog with ID token
    private fun handleGetTokenLoadState(dataLoadState: DataLoadState<GetTokenResponse>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> DialogUtils.showLoadingDialog(childFragmentManager)
            is DataLoadState.Success -> {
                DialogUtils.hideLoadingDialog(childFragmentManager)
                binding.btnLogIn.visibility = View.GONE
                binding.btnLogOut.visibility = View.VISIBLE
                showLoginSuccess(dataLoadState.data)
            }
            is DataLoadState.Error -> {
                DialogUtils.hideLoadingDialog(childFragmentManager)
                showError(dataLoadState.error)
            }
        }
    }

    // Open Logout URL
    private fun handleLogoutActionDataLoadState(dataLoadState: DataLoadState<Uri>) {
        when (dataLoadState) {
            is DataLoadState.Loading -> { }
            is DataLoadState.Success -> handler.openUri(dataLoadState.data)
            is DataLoadState.Error -> showError(dataLoadState.error)
        }
    }

    private fun showLoginSuccess(response: GetTokenResponse) {
        val message = getString(R.string.main__log_in__message_id_token, response.idToken.substring(0, 800))

        DialogUtils.showMessageDialog(
            getString(R.string.main__log_in__title),
            message,
            getString(R.string.main__ok),
            childFragmentManager)
    }

    private fun showLogoutSuccess() {
        DialogUtils.showMessageDialog(
            getString(R.string.main__log_out__title),
            getString(R.string.main__log_out__message),
            getString(R.string.main__ok),
            childFragmentManager)
    }

    private fun showError(error: Throwable) {
        val message = error.message ?: getString(R.string.main__error__message)

        DialogUtils.showMessageDialog(
            getString(R.string.main__error__title),
            message,
            getString(R.string.main__ok),
            childFragmentManager
        )
    }
}