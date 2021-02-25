import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.elokira.R
import com.example.elokira.data.Election
import com.example.elokira.databinding.ElectionsLayoutBinding

class MyAdapter(private val elections: List<Election?>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ViewHolder {
//        val context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ElectionsLayoutBinding = DataBindingUtil.inflate(layoutInflater,
            R.layout.elections_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(elections[position])

    override fun getItemCount(): Int {
        return elections.size
    }

    class ViewHolder(val binding: ElectionsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Election?){
            binding.apply {
                electionName.text = item?.electionName
            }

            }
        }
    }