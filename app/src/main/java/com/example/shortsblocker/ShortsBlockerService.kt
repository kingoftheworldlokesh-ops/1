package com.example.shortsblocker

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Toast

class ShortsBlockerService : AccessibilityService() {

    private var activeShortAllowed = false
    private var baselineNodeCount = -1

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        val rootNode = rootInActiveWindow ?: return

        // 1. Verify if user is viewing a YouTube Short layout
        if (isViewingShortsPlayer(rootNode)) {
            
            // Extract UI tree structural node count to map layout mutations/swipes
            val currentNodeCount = countUiNodes(rootNode)

            if (!activeShortAllowed) {
                // First video sequence registered
                activeShortAllowed = true
                baselineNodeCount = currentNodeCount
                Toast.makeText(this, "1 Short remaining for this loop.", Toast.LENGTH_SHORT).show()
            } else {
                // Check if layout node hierarchy structural differences reveal a swipe action
                if (baselineNodeCount != -1 && Math.abs(currentNodeCount - baselineNodeCount) > 5) {
                    executeSystemBlock()
                }
            }
        } else {
            // Reset state machine parameters if user exits YouTube Shorts interface completely
            activeShortAllowed = false
            baselineNodeCount = -1
        }
    }

    private fun isViewingShortsPlayer(node: AccessibilityNodeInfo): Boolean {
        // YouTube Shorts view layouts uniquely present both text indicators
        val hasComments = node.findAccessibilityNodeInfosByText("Comments").isNotEmpty()
        val hasShare = node.findAccessibilityNodeInfosByText("Share").isNotEmpty()
        return hasComments && hasShare
    }

    private fun countUiNodes(node: AccessibilityNodeInfo?): Int {
        if (node == null) return 0
        var count = 1
        for (i in 0 until node.childCount) {
            count += countUiNodes(node.getChild(i))
        }
        return count
    }

    private fun executeSystemBlock() {
        // Force minimize YouTube app immediately by calling the Android Home Launcher
        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(homeIntent)
        
        Toast.makeText(this, "Limit reached! Next Short blocked.", Toast.LENGTH_LONG).show()
        
        // Reset state so that they get exactly 1 new video if they manually tap back inside
        activeShortAllowed = false
        baselineNodeCount = -1
    }

    override fun onInterrupt() {}
}
