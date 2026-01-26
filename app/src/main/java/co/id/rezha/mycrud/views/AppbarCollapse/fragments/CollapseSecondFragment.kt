package co.id.rezha.mycrud.views.AppbarCollapse.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.core.helpers.Consts
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentCollapseSecondBinding

class CollapseSecondFragment : Fragment() {
    private var _binding: FragmentCollapseSecondBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollapseSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupClickListeners()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun setupRecyclerView() {
        val matches = List(20) {
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30")
        }

        binding.recyclerViewMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMatches.adapter = MatchAdapter3(matches)
    }

    private fun setupClickListeners() {
        binding.btnWatch.setOnClickListener {
            Log.w(Consts.TAG, "Click Watch!")
        }
    }

}

class MatchAdapter3(private val matches: List<Match>) : RecyclerView.Adapter<MatchAdapter3.MatchViewHolder>() {

    class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val homeTeam: TextView = itemView.findViewById(R.id.tvHomeTeam)
        val awayTeam: TextView = itemView.findViewById(R.id.tvAwayTeam)
        val dateTime: TextView = itemView.findViewById(R.id.tvDateTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match2, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        val match = matches[position]
        holder.homeTeam.text = match.homeTeam
        holder.awayTeam.text = match.awayTeam
        holder.dateTime.text = match.dateTime
    }

    override fun getItemCount(): Int = matches.size
}
