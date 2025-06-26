package sk.minv.meid_sample_app.ui.browser

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import org.koin.core.component.KoinComponent
import sk.minv.meid_sample_app.R

/**
 * Sample Activity that handles custom tabs / browsers.
 * Preferred browsers for custom tabs - Chrome, Samsung Browser, Firefox.
 * If none of these browsers is installed Activity opens regular browser.
 */
class BrowserActivity : AppCompatActivity(), KoinComponent {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val preferredBrowsers = listOf(CHROME_PACKAGE_NAME, SAMSUNG_BROWSER_PACKAGE_NAME, FIREFOX_PACKAGE_NAME)

    /*-------------------------*/
    /*   STATIC CONSTRUCTORS   */
    /*-------------------------*/

    companion object {
        const val EXTRA_URI = "uri"
        const val CHROME_PACKAGE_NAME = "com.android.chrome"
        const val SAMSUNG_BROWSER_PACKAGE_NAME = "com.sec.android.app.sbrowser"
        const val FIREFOX_PACKAGE_NAME = "org.mozilla.firefox"

        fun createIntent(context: Context, uri: Uri): Intent {
            val intent =  Intent(context, BrowserActivity::class.java)
            intent.putExtra(EXTRA_URI, uri)
            return intent
        }
    }

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Parse parameters
        val uri: Uri? = intent.getParcelableExtra(EXTRA_URI)

        if (uri != null) {
            // Open URI
            val defaultBrowserPackageName = getDefaultBrowserPackageName(uri)
            val browserPackageName = getCustomTabsPackageName(defaultBrowserPackageName)
            openUri(uri, defaultBrowserPackageName, browserPackageName)
        } else {
            // Show error
            finish()
        }
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun openUri(uri: Uri, defaultBrowserPackageName: String?, browserPackageName: String?) {
        if (browserPackageName == null) {
            fallbackToRegularBrowser(uri)
            return
        }

        val toolbarColor = ContextCompat.getColor(this, R.color.purple_700)
        val colorParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(toolbarColor)
            .build()

        val customTabsIntent = CustomTabsIntent.Builder()
            .setDefaultColorSchemeParams(colorParams)
            .setShowTitle(true)
            .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
            .build()

        customTabsIntent.intent.setPackage(browserPackageName)

        try {
            customTabsIntent.launchUrl(this, uri)
            finish()
        } catch (e: Exception) {
            val newPackageName = getAnotherCustomTabsPackageName(defaultBrowserPackageName, browserPackageName)
            if (newPackageName == null) {
                fallbackToRegularBrowser(uri)
                return
            }
            openUri(uri, defaultBrowserPackageName, newPackageName)
        }
    }

    private fun getCustomTabsPackageName(defaultBrowserPackage: String?): String? {
        if (defaultBrowserPackage in preferredBrowsers) {
            return defaultBrowserPackage
        }

        for (packageName in preferredBrowsers) {
            if (isPackageInstalled(packageManager, packageName)) {
                return packageName
            }
        }

        return defaultBrowserPackage
    }

    private fun getAnotherCustomTabsPackageName(defaultBrowserPackage: String?, currentPackageName: String): String? {
        val availableBrowsers = preferredBrowsers
            .filter { it != currentPackageName }
            .filter { isPackageInstalled(packageManager, it) }

        return availableBrowsers.firstOrNull() ?: defaultBrowserPackage
    }

    private fun getDefaultBrowserPackageName(uri: Uri): String? {
        val defaultBrowserPackage = packageManager.resolveActivity(
            Intent(Intent.ACTION_VIEW, uri),
            PackageManager.MATCH_DEFAULT_ONLY
        )?.activityInfo?.packageName

        if (defaultBrowserPackage == "android") {
            return null
        }

        return defaultBrowserPackage
    }

    private fun isPackageInstalled(pm: PackageManager, packageName: String): Boolean {
        return try {
            pm.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun fallbackToRegularBrowser(uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        startActivity(intent)
        finish()
    }
}