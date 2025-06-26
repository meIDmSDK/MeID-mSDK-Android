package sk.minv.meid_sample_app.interactors.usecase.logout

import android.net.Uri
import sk.minv.base.base.interactor.Result

interface LogoutUseCase {

    suspend operator fun invoke(): Result<Uri>
}