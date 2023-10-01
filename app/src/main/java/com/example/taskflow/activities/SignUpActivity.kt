package com.example.taskflow.activities

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.example.taskflow.R
import com.example.taskflow.activities.BaseActivity
import com.example.taskflow.firebase.FirestoreClass
import com.example.taskflow.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        setContentView(R.layout.activity_sign_up)

        setupActionBar()
        // Click event for sign-up button.
        findViewById<Button>(R.id.btn_sign_up).setOnClickListener {
            registerUser()
        }
    }

    fun userRegisteredSuccess() {
        Toast.makeText(
            this@SignUpActivity,
            "you have successfully registered",
            Toast.LENGTH_SHORT
        ).show()
        hideProgressDialog()

        FirebaseAuth.getInstance().signOut()
        finish()
    }


    private fun setupActionBar() {
        val toolbar_sign_up_activity = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_sign_up_activity)
        setSupportActionBar(toolbar_sign_up_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_sign_up_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun registerUser() {
        // Here we get the text from editText and trim the space
        val et_name = findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.et_name)
        val et_email = findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.et_email)
        val et_password = findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.et_password)
        val name: String = et_name.text.toString().trim { it <= ' ' }
        val email: String = et_email.text.toString().trim { it <= ' ' }
        val password: String = et_password.text.toString().trim { it <= ' ' }

        if (validateForm(name, email, password)) {
            // Show the progress dialog.
            showProgressDialog(resources.getString(R.string.please_wait))
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val registeredEmail = firebaseUser.email!!
                            val user = User(firebaseUser.uid, name, registeredEmail)
                            FirestoreClass().registerUser(this, user)
                        } else {
                            Toast.makeText(
                                this@SignUpActivity,
                                "Registration failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name.")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email.")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password.")
                false
            }
            else -> {
                true
            }
        }
    }
}