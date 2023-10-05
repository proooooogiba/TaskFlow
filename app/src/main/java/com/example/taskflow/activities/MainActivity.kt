package com.example.taskflow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taskflow.R
import com.example.taskflow.adapters.BoardItemsAdapter
import com.example.taskflow.databinding.ActivityCreateBoardBinding
import com.example.taskflow.databinding.ActivityMainBinding
import com.example.taskflow.databinding.AppBarMainBinding
import com.example.taskflow.databinding.MainContentBinding
import com.example.taskflow.databinding.NavHeaderMainBinding
import com.example.taskflow.firebase.FirestoreClass
import com.example.taskflow.models.Board
import com.example.taskflow.models.User
import com.example.taskflow.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarMainBinding: AppBarMainBinding
    private lateinit var mainContentBinding: MainContentBinding
    private lateinit var navHeaderBinding: NavHeaderMainBinding


    companion object {
        const val MY_PROFILE_REQUEST_CODE: Int = 11
        const val CREATE_BOARD_REQUEST_CODE: Int = 12
    }

    private lateinit var mUserName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        appBarMainBinding = binding.appBarMain
        mainContentBinding = binding.appBarMain.mainContent
        navHeaderBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0))

        setContentView(binding.root)

        setUpActionBar()

        binding.navView.setNavigationItemSelectedListener(this)

        FirestoreClass().loadUserData(this, true)
        this.onBackPressedDispatcher.addCallback(this, callback)
        binding.appBarMain.fabCreateBoard.setOnClickListener {
            val intent = Intent(this,
                CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME, mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }
    }

    fun populateBoardListToUI(boardList: ArrayList<Board>) {
        hideProgressDialog()

        if (boardList.size > 0) {
            mainContentBinding.rvBoardsList.visibility = View.VISIBLE
            mainContentBinding.tvNoBoardsAvailable.visibility = View.GONE

            mainContentBinding.rvBoardsList.layoutManager = LinearLayoutManager(this)
            mainContentBinding.rvBoardsList.setHasFixedSize(true)

            val adapter = BoardItemsAdapter(this, boardList)
            mainContentBinding.rvBoardsList.adapter = adapter

            adapter.setOnClickListener(object: BoardItemsAdapter.OnClickListener{
                override fun onClick(position: Int, model: Board) {
                    val intent = Intent(this@MainActivity, TaskListActivity::class.java)
                    intent.putExtra(Constants.DOCUMENT_ID, model.documentId)
                    startActivity(intent)
                }
            })
        } else {
            mainContentBinding.rvBoardsList.visibility = View.GONE
            mainContentBinding.tvNoBoardsAvailable.visibility = View.VISIBLE
        }
    }

    private fun setUpActionBar() {

        setSupportActionBar(appBarMainBinding.toolbarMainActivity)
        appBarMainBinding.toolbarMainActivity.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        appBarMainBinding.toolbarMainActivity.setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                doubleBackToExit()
            }
        }
    }

    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {
        mUserName = user.name

        Glide
            .with(this)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(navHeaderBinding.navUserImage)
        navHeaderBinding.tvUsername.text = user.name

        if (readBoardsList) {
            showProgressDialog(resources.getString((R.string.please_wait)))
            FirestoreClass().getBoardsList(this)
        }
    }

    // save changes without reload database
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == MY_PROFILE_REQUEST_CODE) {
            FirestoreClass().loadUserData(this)
        } else if (resultCode == Activity.RESULT_OK
            && requestCode == CREATE_BOARD_REQUEST_CODE) {
            FirestoreClass().getBoardsList(this)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.nav_my_profile -> {
                startActivityForResult(
                    Intent(this,
                        MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE)
            }
            R.id.nav_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}

