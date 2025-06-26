package sk.minv.meid_sample_app.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class LocalPreferencesImpl(private val context: Context) : LocalPreferences {

    /*-------------------------*/
    /*        CONSTANTS        */
    /*-------------------------*/

    private companion object {
        private const val PKCE_VERIFIER = "pkce_verifier"
        private const val PKCE_VERIFIER_IV = "pkce_verifier_iv"
    }

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    private val default by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }
    /*-------------------------*/
    /*       SET METHODS       */
    /*-------------------------*/

    override fun savePKCEVerifier(verifier: String, iv: String) {
        default.edit { putString(PKCE_VERIFIER, verifier).putString(PKCE_VERIFIER_IV, iv) }
    }

    override fun deletePKCEVerifier() {
        default.edit { remove(PKCE_VERIFIER).remove(PKCE_VERIFIER_IV) }
    }

    /*-------------------------*/
    /*       GET METHODS       */
    /*-------------------------*/

    override fun getPKCEVerifier(): String {
        return getString(default, PKCE_VERIFIER)
    }

    override fun getPKCEVerifierIV(): String {
        return getString(default, PKCE_VERIFIER_IV)
    }

    /*-------------------------*/
    /*     PRIVATE METHODS     */
    /*-------------------------*/

    private fun getString(
        preferences: SharedPreferences,
        key: String,
        defValue: String = ""
    ): String {
        return preferences.getString(key, defValue) ?: defValue
    }
}