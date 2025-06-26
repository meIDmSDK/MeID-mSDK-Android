package sk.minv.meid_sample_app.ui.main

import android.net.Uri
import sk.minv.base.base.activity.BaseHandler

interface MainHandler : BaseHandler {

    fun openUri(uri: Uri)
}