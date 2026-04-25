package com.lubelsoft.countriesapp.screens

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore

import com.lubelsoft.countriesapp.R
import com.lubelsoft.countriesapp.utils.UserPreferences
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        userPreferences = UserPreferences(this)

        //Verificar si ya antes habiamos iniciado sesion
        lifecycleScope.launch {
            userPreferences.isLoggedIn.collect { isLoggedIn ->
                    if(isLoggedIn){
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
        }


        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val btnStart = findViewById<AppCompatButton>(R.id.btnStart)
        val etUserName = findViewById<AppCompatEditText>(R.id.etUserName)


        btnStart.setOnClickListener {
            val userName = etUserName.text.toString()
            if (userName.isNotEmpty()) {
                searchUser(userName)
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("Debe de capturar su nombre de usuario y contraseña")
                .setPositiveButton("Aceptar"){ dialog, _ -> dialog.dismiss() }
                .show()

        }

    }


    fun hashPassword(password: String): String {
        val messageDigest = java.security.MessageDigest.getInstance("SHA-256")
        val hashBytes = messageDigest.digest(password.toByteArray(Charsets.UTF_8))
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
    private fun searchUser(userMail: String) {
        val passwordTest = hashPassword(userMail)
        Log.d("MainActivity", "Buscando usuario: $passwordTest")
        db.collection("users")
            .whereEqualTo("email", userMail)
            .whereEqualTo("password", hashPassword(userMail)).get()
            .addOnSuccessListener { result ->

            if(!result.isEmpty){
                val document = result.documents[0]
                //val password = document.get("password") as String
                val name = document.get("name") as String
                val role = document.get("role") as String

                //Guardar la sesion
                lifecycleScope.launch {
                    userPreferences.saveUserSession(name, userMail, role)

                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    intent.putExtra("USER_NAME", name)
                    intent.putExtra("USER_MAIL", userMail)
                    intent.putExtra("USER_ROLE", role)
                    startActivity(intent)

                }





            }else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Usuario o contraseña no valido")
                    .setPositiveButton("Aceptar"){ dialog, _ -> dialog.dismiss() }
                    .show()
            }

        }.addOnFailureListener {}
    }
}
