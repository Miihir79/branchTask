package com.mihir.branchtask.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mihir.branchtask.BASE_URL
import kotlinx.coroutines.flow.first
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppObjectController {

    companion object {
        private val gson: Gson = GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create()
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        val service: Service by lazy { retrofit.create(Service::class.java) }

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "branch")

        suspend fun save(context: Context, key: String, value: String) {
            val dataStoreKey = stringPreferencesKey(key)
            context.dataStore.edit { store ->
                store[dataStoreKey] = value
            }
        }

        suspend fun read(context: Context, key: String): String? {
            val dataStoreKey = stringPreferencesKey(key)
            val preferences = context.dataStore.data.first()
            return preferences[dataStoreKey]
        }

        suspend fun remove(context: Context){
            context.dataStore.edit {
                it.clear()
            }
        }

    }

}