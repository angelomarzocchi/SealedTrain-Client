package com.example.secure_unico

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.secure_unico.databinding.FragmentQrcodeBinding
import com.example.secure_unico.model.UserViewModel


class QrcodeFragment : Fragment() {

    private lateinit var binding: FragmentQrcodeBinding

    private val userViewModel: UserViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR

        // Inflate the layout for this fragment
        val fragmentBinding = FragmentQrcodeBinding.inflate(inflater)
        binding = fragmentBinding
        binding.lifecycleOwner = this
        binding.viewModel = userViewModel

        val colorMain = TypedValue()
        requireContext().theme.resolveAttribute(
            androidx.appcompat.R.attr.colorPrimaryDark,
            colorMain,
            true
        )
        userViewModel.generateQrCode(colorMain.data)


        binding.qrcodeImage.setImageBitmap(userViewModel.bitmap)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.window?.clearFlags(
            WindowManager.LayoutParams.FLAG_SECURE
        )
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
    }

}