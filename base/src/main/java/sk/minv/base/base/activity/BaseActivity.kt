package sk.minv.base.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import sk.minv.base.R
import sk.minv.base.utils.helpers.getExtra
import sk.minv.base.utils.helpers.showFragment

abstract class BaseActivity<PARAMETERS, FRAGMENT>() : AppCompatActivity() where PARAMETERS : AndroidParameters, FRAGMENT : Fragment {

    /*-------------------------*/
    /*         FIELDS          */
    /*-------------------------*/

    protected val TAG = this.javaClass.name

    protected var parameters: PARAMETERS? = null
    protected lateinit var fragment: FRAGMENT

    /*-------------------------*/
    /*   OVERRIDDEN METHODS    */
    /*-------------------------*/

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        parameters = intent.extras?.getExtra()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity__container)

        fragment = createContentFragment()
        showFragment(R.id.container, fragment, TAG)

        onViewReady()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.anim_stay, R.anim.anim_slide_down)
    }

    /*-------------------------*/
    /*    PROTECTED METHODS    */
    /*-------------------------*/

    protected abstract fun createContentFragment(): FRAGMENT

    protected open fun onViewReady() {}
}