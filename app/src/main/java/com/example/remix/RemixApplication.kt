package com.example.remix

import android.app.Application
import com.example.remix.data.RemixDatabase
import com.example.remix.data.RemixRepository

class RemixApplication : Application() {
    val database by lazy { RemixDatabase.getDatabase(this) }
    val repository by lazy { RemixRepository(database.remixDao()) }
}
