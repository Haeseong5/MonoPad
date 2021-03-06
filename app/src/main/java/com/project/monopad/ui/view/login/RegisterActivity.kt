package com.project.monopad.ui.view.login

import android.app.AlertDialog
import android.util.Log
import android.view.View
import com.project.monopad.R
import com.project.monopad.databinding.ActivityRegisterBinding
import com.project.monopad.extension.showToast
import com.project.monopad.ui.base.BaseActivity
import com.project.monopad.ui.viewmodel.RegisterViewModel
import com.project.monopad.util.LoginPatternCheckUtil.isNotValidEmail
import com.project.monopad.util.LoginPatternCheckUtil.isNotValidName
import com.project.monopad.util.LoginPatternCheckUtil.isNotValidPassword
import com.project.monopad.util.LoginPatternCheckUtil.isPasswordCheckSuccess
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : BaseActivity<ActivityRegisterBinding, RegisterViewModel>(),
    AuthListener, EmailCheckListener {

    override val layoutResourceId: Int
        get() = R.layout.activity_register
    override val viewModel: RegisterViewModel by viewModel()

    override fun initStartView() {
        initView()
        initClickEvent()
    }

    override fun initBeforeBinding() {
        viewDataBinding.lifecycleOwner = this
    }

    override fun initAfterBinding() {
        viewModel.setRegisterListener(this)
        viewModel.setEmailCheckListener(this)
    }

    private fun initView() {
        hideProgressBar()
    }

    private fun initClickEvent() {
        viewDataBinding.apply {
            btnRegister.setOnClickListener {
                register()
            }
            btnEmailCheck.setOnClickListener {
                checkEmail()
            }
        }
    }

    private fun register() {
        val name = viewDataBinding.etName.text.toString()
        val email = viewDataBinding.etEmail.text.toString()
        val password = viewDataBinding.etPw.text.toString()
        val passwordCheck = viewDataBinding.etPwCheck.text.toString()

        if(isNotValidName(name)){
            showToast(this, R.string.message_name_error)
        }
        else if(isNotValidEmail(email) || !viewModel.isEmailCheckSuccessful) {
            showToast(this, R.string.message_plz_email_check)
        }
        else if(isNotValidPassword(password)) {
            showToast(this, R.string.message_password_error)
        }
        else if(!isPasswordCheckSuccess(password, passwordCheck)) {
            showToast(this, R.string.message_password_inconsistent)
        }
        else {
            viewModel.createUserWithEmailAndPassword(email, password, passwordCheck, name)
        }
    }

    private fun checkEmail() {
        et_email.text.toString().let {
            if (isNotValidEmail(it)){
                showToast(this, R.string.message_is_not_valid_email)
            }
            else {
                viewModel.isAvailableEmail(it)
            }
        }
    }

    private fun showProgressBar() {
        viewDataBinding.progressbar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        viewDataBinding.progressbar.visibility = View.GONE
    }

    //AuthListener
    override fun onStarted() {
        showProgressBar()
    }

    override fun onSuccess() {
        hideProgressBar()
        finish()
    }

    override fun onFailure(message: String) {
        hideProgressBar()
        showToast(this, message)
    }

    //EmailCheckListener
    override fun onEmailCheckSuccess(isEmailCheckSucccesful: Boolean) {
        val message = if(isEmailCheckSucccesful) R.string.message_is_not_duplicated else R.string.message_is_duplicated

        AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Dialog_Alert)
            .setTitle(R.string.message_check_email_duplicate)
            .setMessage(message)
            .setPositiveButton("확인", null)
            .create()
            .show()
    }

    override fun onEmailCheckFailure(message: String) {
        Log.e(TAG, message)
    }

    companion object{
        private const val TAG = "RegisterActivity"
    }
}