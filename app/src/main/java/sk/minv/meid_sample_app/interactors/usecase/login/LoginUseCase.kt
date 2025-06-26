package sk.minv.meid_sample_app.interactors.usecase.login

import android.net.Uri
import sk.minv.base.base.interactor.Result

interface LoginUseCase {

    suspend operator fun invoke(): Result<Uri>
}