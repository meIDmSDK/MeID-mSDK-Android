package sk.minv.meid_sample_app.ui.main

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import org.koin.core.component.KoinComponent
import sk.minv.base.base.activity.BaseActivity
import sk.minv.base.base.activity.NoParameters
import sk.minv.meid_sample_app.ui.browser.BrowserActivity
import java.util.LinkedHashMap

class MainActivity : BaseActivity<NoParameters, MainFragment>(), MainHandler, KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private lateinit var noActionLauncher: ActivityResultLauncher<Intent>

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (intent.action == Intent.ACTION_VIEW) {
            handleDeeplink(intent.dataString)
        }
    }

    override fun createContentFragment(): MainFragment = MainFragment.newInstance()

    override fun onViewReady() {
        noActionLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }
    }

    /*-------------------------*/
    /*     FRAGMENT METHODS    */
    /*-------------------------*/

    override fun openUri(uri: Uri) {
        noActionLauncher.launch(BrowserActivity.createIntent(this, uri))
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun handleDeeplink(deepLinkData: String?) {
        deepLinkData?.let {
            val deepLinkUri = it.toUri()
            val deeplinkScheme = deepLinkUri.scheme
            val deepLinkHost = deepLinkUri.host
            val deepLinkParameters = getQueryParameters(deepLinkUri)
            fragment.onDeeplinkReceived(scheme = deeplinkScheme, host = deepLinkHost, parameters = deepLinkParameters)
        }
    }

    private fun getQueryParameters(uri: Uri): Map<String, String>? {
        val queryPairs: MutableMap<String, String> = LinkedHashMap()

        val parameterNames = uri.queryParameterNames
        if (parameterNames.size == 0) {
            return null
        }

        for (parameterName in parameterNames) {
            val parameter = uri.getQueryParameter(parameterName)
            parameter?.let {
                queryPairs[parameterName] = it
            }
        }

        if (queryPairs.isNotEmpty()) {
            return queryPairs
        }

        return null
    }
}