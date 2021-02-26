import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.elokira.R
import com.example.elokira.data.Election
import com.example.elokira.databinding.ElectionsLayoutBinding

class ElectionsAdapter(
    private val elections: List<Election?>,
    val listListener: (Election) -> Unit,
    val participateListener: (Election) -> Unit
    ) : RecyclerView.Adapter<ElectionsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
//        val context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ElectionsLayoutBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.elections_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = elections[position]
        holder.bind(item)
        holder.itemView.setOnClickListener{ listListener(item!!)}
        holder.binding.participate.setOnClickListener{participateListener(item!!)
        }
    }

    override fun getItemCount(): Int {
        return elections.size
    }


    class ViewHolder(val binding: ElectionsLayoutBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Election?){
            binding.apply {
                electionName.text = item?.electionName

            }

        }

        init {
            binding.participate.setOnClickListener {

            }
        }

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
            adapterPosition
        }
    }

    }