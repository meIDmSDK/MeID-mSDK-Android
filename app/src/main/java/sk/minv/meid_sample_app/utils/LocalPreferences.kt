package sk.minv.meid_sample_app.utils

interface LocalPreferences {

    fun savePKCEVerifier(verifier: String, iv: String)

    fun deletePKCEVerifier()

    fun getPKCEVerifier(): String

    fun getPKCEVerifierIV(): String
}