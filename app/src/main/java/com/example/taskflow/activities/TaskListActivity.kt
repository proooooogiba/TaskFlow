package com.example.taskflow.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskflow.R
import com.example.taskflow.adapters.TaskListItemsAdapter
import com.example.taskflow.databinding.ActivitySplashBinding
import com.example.taskflow.databinding.ActivityTaskListBinding
import com.example.taskflow.firebase.FirestoreClass
import com.example.taskflow.models.Board
import com.example.taskflow.models.Card
import com.example.taskflow.models.Task
import com.example.taskflow.utils.Constants
import com.google.firebase.firestore.DocumentId
import java.text.FieldPosition

class TaskListActivity : BaseActivity() {
    private lateinit var binding: ActivityTaskListBinding

    private lateinit var mBoardDetails: Board
    private lateinit var mBoardDocumentID: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.DOCUMENT_ID)) {
            mBoardDocumentID = intent.getStringExtra(Constants.DOCUMENT_ID).toString()
        }

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this, mBoardDocumentID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == MEMBERS_REQUEST_CODE) {
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardDetails(this, mBoardDocumentID)
        } else {
            Log.e("Cancelled", "Cancelled")
        }
    }

    fun cardDetails(taskListPosition: Int, cardPosition: Int) {
        startActivity(Intent(this, CardDetailsActivity::class.java))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_members -> {
                val intent = Intent(this, MembersActivity::class.java)
                intent.putExtra(Constants.BOARD_DETAIL, mBoardDetails)
                startActivityForResult(intent, MEMBERS_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpActionBar() {
        setSupportActionBar(binding.toolbarTaskListActivity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = mBoardDetails.name
        }

        binding.toolbarTaskListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun boardDetails(board: Board) {
        mBoardDetails = board

        hideProgressDialog()
        setUpActionBar()

        val addTaskList = Task(resources.getString(R.string.add_list))
        board.taskList.add(addTaskList)

        binding.rvTaskList.layoutManager = LinearLayoutManager(this@TaskListActivity,
            LinearLayoutManager.HORIZONTAL, false)
        binding.rvTaskList.setHasFixedSize(true)

        val adapter = TaskListItemsAdapter(this@TaskListActivity, board.taskList)
        binding.rvTaskList.adapter = adapter
    }

    fun addUpdateTaskListSuccess() {
        hideProgressDialog()

        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getBoardDetails(this, mBoardDetails.documentId)
    }

    fun createTaskList(taskListName: String) {
        val task = Task(taskListName, FirestoreClass().getCurrentUserId())
        mBoardDetails.taskList.add(0, task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun updateTaskList(position: Int, listName: String, model: Task) {
        val task = Task(listName, model.createdBy)

        mBoardDetails.taskList[position] = task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun deleteTaskList(position: Int) {
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun addCardToTaskList(position: Int, cardName: String) {
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size - 1)

        val cardAssignedUsersList: ArrayList<String> = ArrayList()
        val user = FirestoreClass().getCurrentUserId()
        cardAssignedUsersList.add(user)

        val card = Card(cardName, user, cardAssignedUsersList)

        val cardList = mBoardDetails.taskList[position].cards
        cardList.add(card)

        val task = Task(
            mBoardDetails.taskList[position].title,
            mBoardDetails.taskList[position].createdBy,
            cardList
        )

        mBoardDetails.taskList[position] = task

        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }


    companion object {
        const val MEMBERS_REQUEST_CODE: Int = 13

    }
}