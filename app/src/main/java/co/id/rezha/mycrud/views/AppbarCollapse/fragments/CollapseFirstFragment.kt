package co.id.rezha.mycrud.views.AppbarCollapse.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentCollapseFirstBinding
import co.id.rezha.mycrud.databinding.FragmentHomeBinding

class CollapseFirstFragment : Fragment() {
    private var _binding: FragmentCollapseFirstBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollapseFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupScrollListener()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun setupRecyclerView() {
        val matches = listOf(
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30"),
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30"),
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30"),
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30"),
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30"),
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30"),
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30"),
            Match("Bayern München", "FC Barcelona", "Fr., 20.1. 20:30")
        )

        binding.recyclerViewMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewMatches.adapter = MatchAdapter2(matches)
    }

    private fun setupScrollListener() {
        binding.recyclerViewMatches.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Efek fade pada card hijau saat scroll
                if (dy > 0) { // Scroll down
                    val alpha = (100 - recyclerView.computeVerticalScrollOffset()).toFloat() / 100
//                    binding.greenCard.alpha = alpha.coerceIn(0f, 1f)
                } else { // Scroll up
                    val alpha = (100 - recyclerView.computeVerticalScrollOffset()).toFloat() / 100
//                    binding.greenCard.alpha = alpha.coerceIn(0f, 1f)
                }
            }
        })
    }
}

class MatchAdapter2(private val matches: List<Match>) : RecyclerView.Adapter<MatchAdapter2.MatchViewHolder>() {

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

data class Match(
    val homeTeam: String,
    val awayTeam: String,
    val dateTime: String
)