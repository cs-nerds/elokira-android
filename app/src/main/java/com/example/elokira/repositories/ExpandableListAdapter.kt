import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ListView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.elokira.R
import com.example.elokira.data.Position

class ExpandableListAdapter(
    private val context: Context,
    private val positions: List<String>,
    private val candidates: HashMap<String, List<String>>
): BaseExpandableListAdapter(){

    override fun getGroupCount(): Int {
        return this.positions.size
    }

    override fun getChildrenCount(listPosition: Int): Int {
       return this.candidates[this.positions[listPosition]]!!.size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.positions[listPosition]
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        return this.candidates[this.positions[listPosition]]!![expandedListPosition]
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(listPostion: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        val positionName = getGroup(listPostion) as String

        if(convertView == null){
            val layoutInflater =
                this.context.getSystemService((Context.LAYOUT_INFLATER_SERVICE)) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.list_group, null)
        }
        val positionTitle = convertView!!.findViewById<TextView>(R.id.lblListHeader)
        positionTitle.setTypeface(null, Typeface.BOLD)
        positionTitle.text = positionName
        return convertView
    }

    override fun getChildView(listPosition: Int,
                              expandedListPosition: Int,
                              isLastChild: Boolean,
                              convertView: View?,
                              parent: ViewGroup?): View {
        var convertView = convertView
        val expandedListText = getChild(listPosition, expandedListPosition) as String
        if(convertView == null){
            val layoutInflater =
                this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView= layoutInflater.inflate(R.layout.list_item, null)
        }
        val candidateName = convertView!!.findViewById<TextView>(R.id.candidateName)
        candidateName.text = expandedListText


        val radioButton = convertView.findViewById<RadioButton>(R.id.vote_radio_button)
        val childCheckedState: HashMap<Int, Int> = HashMap()
         try {
             if(childCheckedState != null){
                 if(childCheckedState.size > 0){
                     radioButton.isChecked = expandedListPosition == childCheckedState[listPosition]
                 }
             }
         }catch (e: Exception){
             e.printStackTrace()
         }

        radioButton.setOnClickListener {
            for(i in 0 until groupCount-1){
                for(k in 0..getChildrenCount(i)){
                    radioButton.isChecked = false
                    childCheckedState.remove(i, k)

                }
                radioButton.isChecked = expandedListPosition == childCheckedState[listPosition]
                radioButton.tag = expandedListPosition
                childCheckedState.put(i, expandedListPosition)
                notifyDataSetChanged()
            }

        }
        return convertView
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }

}