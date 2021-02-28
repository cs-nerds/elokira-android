import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.elokira.R
import com.example.elokira.data.Election
import com.example.elokira.data.Position
import com.example.elokira.databinding.ListGroupBinding


class PositionsAdapter(
    private val elections: List<Position?>,
//    val listListener: (Position) -> Unit
) : RecyclerView.Adapter<PositionsAdapter.ViewHolder>() {

    val currentTime = System.currentTimeMillis()

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
//        val context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListGroupBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.list_group, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = elections[position]
        holder.bind(item)
//        holder.itemView.setOnClickListener{ listListener(item!!)}

    }

    override fun getItemCount(): Int {
        return elections.size
    }




    class ViewHolder(val binding: ListGroupBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        fun bind(item: Position?){
            binding.apply {
                positionName.text = item?.positionName

            }

        }


        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
            adapterPosition
        }
    }

}