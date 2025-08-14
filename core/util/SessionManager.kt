package com.lasec.monitoreoapp.core.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val PREFS_NAME = "user_session"
        private const val KEY_INDEX_EMPLOYEE_ID = "indexEmployeeId"
        private const val KEY_DISPATCH_EMPLOYEE_ID = "dispatchEmployeeId"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveIndexEmployeeId(id: Int) {
        prefs.edit { putInt(KEY_INDEX_EMPLOYEE_ID, id) }
    }

    fun getIndexEmployeeId(): Int {
        return prefs.getInt(KEY_INDEX_EMPLOYEE_ID, -1)
    }

    fun saveDispatchEmployeeId(id: Int) {
        prefs.edit { putInt(KEY_DISPATCH_EMPLOYEE_ID, id) }
    }

    fun getDispatchEmployeeId(): Int {
        return prefs.getInt(KEY_DISPATCH_EMPLOYEE_ID, -1)
    }

    fun clearSession() {
        prefs.edit { clear() }
    }
}