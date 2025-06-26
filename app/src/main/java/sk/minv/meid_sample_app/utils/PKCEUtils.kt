package sk.minv.meid_sample_app.utils

import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom

object PKCEUtils {

    fun generatePKCEVerifier(): String {
        val randomBytes = ByteArray(64)
        SecureRandom().nextBytes(randomBytes)
        return Base64.encodeToString(randomBytes, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }

    fun generatePKCEChallenge(verifier: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(verifier.toByteArray())
        return Base64.encodeToString(hashedBytes, Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP)
    }

    fun encryptPKCEVerifier(verifier: String): Pair<String, String> {
        val secretKey = KeyStoreUtils.getOrCreateKey()
        val (encryptedData, iv) = KeyStoreUtils.encryptData(verifier, secretKey)
        return Pair(Base64.encodeToString(encryptedData, Base64.DEFAULT), Base64.encodeToString(iv, Base64.DEFAULT))
    }
}