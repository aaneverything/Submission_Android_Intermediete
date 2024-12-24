package com.aantriav.intermediate2.ui.screen


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aantriav.intermediate2.R
import com.aantriav.intermediate2.data.pref.UserPreferences
import com.aantriav.intermediate2.ui.MainActivity

class LogoutFragment : Fragment() {

    private lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userPreferences = UserPreferences(requireContext())
        performLogout()
        return inflater.inflate(R.layout.fragment_logout, container, false)
    }

    private fun performLogout() {
        userPreferences.clearToken()
        Toast.makeText(requireContext(), getString(R.string.logout_success), Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }, 1000)
    }
}
