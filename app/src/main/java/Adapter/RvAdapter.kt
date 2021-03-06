package Adapter

import Models.Contact
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.permisions.R
import kotlinx.android.synthetic.main.item_rv.view.*

class RvAdapter(var list:List<Contact>):RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh (var itemView: View):RecyclerView.ViewHolder(itemView){
        fun onBind (contact: Contact){
            itemView.txt_name.text = contact.name
            itemView.txt_number.text = contact.number
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return  Vh(LayoutInflater.from(parent.context).inflate(R.layout.item_rv, parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return  list.size
    }
}