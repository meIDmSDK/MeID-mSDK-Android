package sk.minv.base.ui

import android.os.Bundle
import android.view.View
import sk.minv.base.R
import sk.minv.base.base.fragment.BaseDialogFragment

class LoadingDialog : BaseDialogFragment() {

    /*-------------------------*/
    /*       CONSTRUCTORS      */
    /*-------------------------*/

    companion object {
        fun newInstance(args: Bundle?): LoadingDialog {
            val dialog = LoadingDialog()
            dialog.arguments = args
            return dialog
        }
    }

    /*-------------------------*/
    /*    PROTECTED METHODS    */
    /*-------------------------*/

    override fun getParameters() { }

    override fun setListeners() { }

    override fun getLayoutRes(): Int = R.layout.dialog_fragment__loading

    override fun getDialogTheme(): Int = R.style.DialogTheme

    override fun inflateView(view: View) { }

    override fun alterView() { }

    /*-------------------------*/
    /*      INNER CLASSES      */
    /*-------------------------*/

    class Builder {
        private val args: Bundle = Bundle()

        fun build(): LoadingDialog {
            return newInstance(args)
        }
    }
}