package com.sklarskyj.activitylauncher.ui

interface ActionBarSearch {
    var actionBarSearchText: String
    var onActionBarSearchListener: ((String) -> Unit)?
}