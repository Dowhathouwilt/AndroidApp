package com.example.criminalintent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.databinding.ListItemCrimeBinding
import java.util.UUID

class CrimeHolder(private val binding: ListItemCrimeBinding):
    RecyclerView.ViewHolder(binding.root) {
    fun bind (crime: Crime, onCrimeClicked: (crimeId:UUID) -> Unit){
        binding.crimeTitle.text = crime.title
        binding.crimeDate.text = crime.date.toString()
        binding.crimeSolved.visibility = if (crime.isSolved) {
            View.VISIBLE
        } else {
            View.GONE
        }
        binding.root.setOnClickListener(){
            onCrimeClicked(crime.id)
        }
    }
}
class CrimeListAdapter(private val crimes: List<Crime>, private val onCrimeClicked: (crimeId:UUID) -> Unit): RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemCrimeBinding.inflate(inflater, parent, false)
        return CrimeHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val crime = crimes[position]
        (holder as CrimeHolder).apply {
            bind(crime, onCrimeClicked)
        }
    }
    override fun getItemCount() = crimes.size

}