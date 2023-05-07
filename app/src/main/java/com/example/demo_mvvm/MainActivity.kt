package com.example.demo_mvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.demo_mvvm.databinding.ActivityMainBinding
import com.example.demo_mvvm.model.APIResponse
import com.example.demo_mvvm.model.LoginResponse
import com.example.demo_mvvm.viewmodel.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    //Create View Model Object to function call & provide view model
    val loginViewModel : LoginViewModel by viewModels()
    lateinit var viewModel:LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding.loginViewModel = viewModel

        binding.btnLogin.setOnClickListener {
            loginApi()
        }
    }

    private fun loginApi() {
        CoroutineScope(Dispatchers.Main).launch {
            loginViewModel.apilogin().observe(this@MainActivity){
                binding.prgbar.visibility= View.VISIBLE
                when(it){
                    is APIResponse.Loading -> {

                    }

                    is APIResponse.APIError -> {
                        binding.prgbar.visibility= View.GONE
                        Toast.makeText(this@MainActivity, ""+it.errorCode+"\n"+it.message, Toast.LENGTH_SHORT).show()
                    }

                    is APIResponse.Success<*> -> {
                        binding.prgbar.visibility= View.GONE
                        val response = it.data as LoginResponse
                        Toast.makeText(this@MainActivity, " "+response.code+"\n"+response.message, Toast.LENGTH_SHORT).show()

                        val printUserID = response.data?.userId
                        val printName = response.data?.firstName +" "+ response.data?.lastName
                        val printContact = response.data?.contactNumber
                        val printEmail = response.data?.email
                        val printReferal = response.data?.refralCode
                        val printDate = response.data?.createdDate

                        val bundle = Bundle()
                        bundle.putString("Id",printUserID.toString())
                        bundle.putString("name",printName.toString())
                        bundle.putString("contact",printContact.toString())
                        bundle.putString("email",printEmail.toString())
                        bundle.putString("referal",printReferal.toString())
                        bundle.putString("date",printDate.toString())

                        Log.e("check1",printUserID.toString())
                        Log.e("check2",printName.toString())
                        Log.e("check3",printContact.toString())


                        val intent = Intent(this@MainActivity, Home::class.java)
                        intent.putExtras(bundle)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}