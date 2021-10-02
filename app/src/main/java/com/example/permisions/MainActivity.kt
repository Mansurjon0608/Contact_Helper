package com.example.permisions

import Adapter.RvAdapter
import Helper.MyButton
import Helper.MySwipeHelper
import Models.Contact
import Models.MyButtonClickListener
import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.permisions.databinding.ActivityMainBinding
import com.github.florent37.runtimepermission.kotlin.askPermission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var adapter : RvAdapter
    lateinit var contactList:ArrayList<Contact>

    @RequiresApi(Build.VERSION_CODES.O)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_contact.setHasFixedSize(true)

        supportActionBar?.hide()

        val swipe = object : MySwipeHelper(this, rv_contact, 170){
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(
                    MyButton(this@MainActivity,
                        "Sms",
                        30,
                        R.drawable.ic_baseline_message_24,
                        Color.parseColor("#E11057"),
                        object : MyButtonClickListener {
                            override fun onClick(position: Int) {
                                val intent = Intent(this@MainActivity, SMSActivity::class.java)
                                intent.putExtra("key", contactList[position])
                                startActivity(intent)
                            }
                        })
                )
                buffer.add(
                    MyButton(this@MainActivity,
                        "Call",
                        30,
                        R.drawable.ic_baseline_call_24,
                        Color.parseColor("#F8CA2A"),
                        object : MyButtonClickListener {
                            override fun onClick(position: Int) {
                                Toast.makeText(this@MainActivity, "Update id $position", Toast.LENGTH_SHORT).show()
                                telefonQilish(position)
                            }
                        })
                )
            }

        }
        readContact()
    }

    private fun telefonQilish(position:Int) {

        askPermission(Manifest.permission.CALL_PHONE){
            val phonNumber = contactList[position].number
            val intent = Intent(Intent(Intent.ACTION_CALL))
            intent.data = Uri.parse("tel:$phonNumber")
            startActivity(intent)
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if(e.hasForeverDenied()) {
                e.goToSettings();
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun readContact(){
        contactList = ArrayList()
        askPermission(Manifest.permission.READ_CONTACTS){
            val contacts = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null)
            while (contacts!!.moveToNext()){
                val contact = Contact(
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),
                    contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                )

                contactList.add(contact)
            }
            contacts.close()

            adapter = RvAdapter(contactList)
            rv_contact.adapter = adapter
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Ruxsat bermasangiz ilova ishlay olmaydi ruxsat bering...")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    }
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if(e.hasForeverDenied()) {
                e.goToSettings();
            }
        }


    }
}
