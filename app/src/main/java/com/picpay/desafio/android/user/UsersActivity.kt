package com.picpay.desafio.android.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.picpay.desafio.android.R
import com.picpay.desafio.android.common.viewmodel.observe
import com.picpay.desafio.android.data.repository.model.User
import com.picpay.desafio.android.extensions.hide
import com.picpay.desafio.android.extensions.show
import com.picpay.desafio.android.injector.ViewModelFactoryProvider
import com.picpay.desafio.android.user.action.UserActionDispatcher
import com.picpay.desafio.android.user.data.UserDataDispatcher
import kotlinx.android.synthetic.main.activity_users.*

class UsersActivity : AppCompatActivity(), UserHandler {

    private val viewModel: UserViewModel by viewModels { ViewModelFactoryProvider.provideUserViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        viewModel.actions.observe(this, UserActionDispatcher(this))
        viewModel.data.observe(this, UserDataDispatcher(this))

        error.setOnClickListener { viewModel.load() }
    }

    override fun goToUserDetailScreen(userId: Long) {
        Log.d(TAG, "goToUserDetailScreen: $userId")
        Toast.makeText(this, "goToUserDetailScreen: $userId", Toast.LENGTH_LONG).show()
    }

    override fun bindData(data: List<User>) {
        progress.hide()
        error.hide()
        recyclerView.apply {
            adapter = UserAdapter(data) { viewModel.goToUserDetailClicked(it) }
            layoutManager = LinearLayoutManager(this@UsersActivity)
        }
    }

    override fun showProgress() {
        progress.show()
        error.hide()
    }

    override fun showError() {
        progress.hide()
        error.show()
    }

    companion object {

        const val TAG = "TAG::UsersActivity"
        fun newIntent(context: Context) = Intent(context, UsersActivity::class.java)
    }
}