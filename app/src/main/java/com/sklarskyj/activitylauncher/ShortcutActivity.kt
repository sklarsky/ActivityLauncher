package com.sklarskyj.activitylauncher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import dagger.hilt.android.AndroidEntryPoint
import com.sklarskyj.activitylauncher.services.ActivityLauncherService
import com.sklarskyj.activitylauncher.services.IconCreatorService
import com.sklarskyj.activitylauncher.services.IntentSigningService
import javax.inject.Inject

@AndroidEntryPoint
class ShortcutActivity : AppCompatActivity() {
    @Inject
    internal lateinit var launcherService: ActivityLauncherService

    @Inject
    internal lateinit var signingService: IntentSigningService


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val launchIntent = Intent.parseUri(intent.getStringExtra(IconCreatorService.INTENT_EXTRA_INTENT), 0)
            val signature = intent.getStringExtra(IconCreatorService.INTENT_EXTRA_SIGNATURE).orEmpty()
            val asRoot = intent.action == IconCreatorService.INTENT_LAUNCH_ROOT_SHORTCUT

            if (asRoot && !signingService.validateIntentSignature(launchIntent, signature)) {
                return
            }

            launcherService.launchActivity(launchIntent.component!!,
                asRoot,
                showToast = false
            )
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            ActivityCompat.finishAffinity(this);
        }
    }
}

