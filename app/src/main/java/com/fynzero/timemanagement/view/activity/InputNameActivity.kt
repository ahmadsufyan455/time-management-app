package com.fynzero.timemanagement.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fynzero.timemanagement.R
import com.fynzero.timemanagement.helper.UserPref
import kotlinx.android.synthetic.main.activity_input_name.*

class InputNameActivity : AppCompatActivity() {

    private lateinit var userPref: UserPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_name)

        userPref = UserPref(this)

        btn_next.setOnClickListener {
            if (edt_input_name.text.isNotEmpty()) {
                userPref.setName(UserPref.PREF_USERNAME, edt_input_name.text.toString())
                userPref.isLogin(UserPref.IS_LOGIN, true)
                moveIntent()
            } else {
                edt_input_name.error = getString(R.string.name_required)
            }
        }
    }

    // check status of input activity
    override fun onStart() {
        super.onStart()
        if (userPref.getLogin(UserPref.IS_LOGIN)) {
            moveIntent()
        }
    }

    // if true move to main activity
    private fun moveIntent() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}