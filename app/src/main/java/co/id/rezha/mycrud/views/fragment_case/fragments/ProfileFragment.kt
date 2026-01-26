package co.id.rezha.mycrud.views.fragment_case.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.BundleCompat
import co.id.rezha.mycrud.R
import co.id.rezha.mycrud.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var userArg: UserModel? = null

    companion object {
        private const val ARG_USER = "arg_user"
        fun newInstance(user: UserModel): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_USER, user)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userArg = arguments?.let {
            BundleCompat.getParcelable(it, ARG_USER, UserModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvProfileTitle.text = "Profile"

        // Prefill UI dari argumen
        userArg?.let { user ->
            binding.tvProfileTitle.text = "Profile (${user.usia} th)"
            binding.etName.setText(user.nama)
        }

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etName.text?.toString().orEmpty()
            Toast.makeText(requireContext(), "Tersimpan: $name", Toast.LENGTH_SHORT).show()
            // TODO: simpan ke ViewModel / DataStore / Repository jika diperlukan
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}