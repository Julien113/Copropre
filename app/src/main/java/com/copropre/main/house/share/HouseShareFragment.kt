package com.copropre.main.house.share

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.copropre.common.models.House
import com.copropre.common.services.common.TopBarService
import com.copropre.common.services.main.HouseService
import com.copropre.databinding.FragmentHouseShareBinding
import java.util.*

import android.content.Intent
import com.copropre.R


class HouseShareFragment(private val house: House) : Fragment(), View.OnClickListener {
    private var _binding: FragmentHouseShareBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHouseShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TopBarService.changeTopBar(TopBarService.FragmentName.FRAGMENT_SHARE)


        showOrCreateHouseInvitationCode()


        binding.bAdd.setOnClickListener(this)
        binding.bShare.setOnClickListener(this)
        binding.ibCopyCode.setOnClickListener(this)

    }

    private fun showOrCreateHouseInvitationCode() {
        if (house.invitationCode !== null &&
            (house.invitationCodeExpirationDate == null || house.invitationCodeExpirationDate.after(
                Date()
            ))
        ) {
            binding.tHouseCode.text = house.invitationCode
        } else {
            // créé un nouveau invitation code
            binding.tHouseCode.text = "..."
            HouseService.createNewInvitationCode(house) {
                if (it.isSuccessful) {
                    binding.tHouseCode.text = house.invitationCode
                } else {
                    Log.e("HouseShareFragment", "Cannot Create Invitation Code")
                    Toast.makeText(context, "Impossible de créer un code d'invitation. Veuillez réessayer.",Toast.LENGTH_LONG).show()
                    it.exception!!.printStackTrace()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bAdd -> {

            }
            R.id.bShare -> {
                if (house.invitationCode !== null) {
                    val shareIntent = Intent()
                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type="text/plain"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, house.invitationCode);
                    startActivity(Intent.createChooser(shareIntent,"Envoyer à"))
                }
            }
            R.id.ibCopyCode -> {
                if (house.invitationCode !== null) {
                    val clipboard = getSystemService(requireContext(), ClipboardManager::class.java)
                    val clip = ClipData.newPlainText("Invitation code", house.invitationCode)
                    clipboard!!.setPrimaryClip(clip)
                    Toast.makeText(context, house.invitationCode+" a été copié dans le presse papier",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}