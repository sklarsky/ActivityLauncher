package de.szalkowski.activitylauncher.ui

import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dagger.hilt.android.AndroidEntryPoint
import de.szalkowski.activitylauncher.R
import de.szalkowski.activitylauncher.services.SettingsService
import javax.inject.Inject

@AndroidEntryPoint
class DisclaimerDialogFragment : DialogFragment() {
    @Inject
    internal lateinit var settingsService: SettingsService
    
    private var isAutomotive = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Check if running on Android Automotive OS
        isAutomotive = requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_AUTOMOTIVE)
        
        val builder = AlertDialog.Builder(requireActivity())
        
        // Create dialog message based on platform
        var message = getString(R.string.dialog_disclaimer)
        
        // Add automotive warning if on AAOS
        if (isAutomotive && resources.getIdentifier("automotive_warning", "string", requireContext().packageName) != 0) {
            message = getString(R.string.dialog_disclaimer) + "\n\n" + getString(R.string.automotive_warning)
        }
        
        // Create spannable string to support links
        val spannableMessage = SpannableString(message)
        Linkify.addLinks(spannableMessage, Linkify.WEB_URLS)
        
        builder.setTitle(R.string.title_dialog_disclaimer)
            .setMessage(spannableMessage)
            .setPositiveButton(android.R.string.yes) { _, _ ->
                settingsService.disclaimerAccepted = true
            }.setNegativeButton(android.R.string.cancel) { _, _ ->
                settingsService.disclaimerAccepted = false
                requireActivity().finish()
            }
            
        val dialog = builder.create()
        
        // Enable clickable links
        dialog.setOnShowListener {
            val textView = dialog.findViewById<TextView>(android.R.id.message)
            textView?.movementMethod = LinkMovementMethod.getInstance()
        }
        
        return dialog
    }
}