package an.imation.filmsapp


import android.app.Application
import com.andretietz.retrofit.responseCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import okhttp3.Cache
import retrofit2.Retrofit
import java.io.File

object OkhttpCache {

    private var cache: Cache? = null
    private val isInit = MutableStateFlow(false)

    private fun getCache(apl: Application): Cache? {
        isInit.update {
            if (it) return cache
            true
        }
        runCatching {
            val currentVersion = BuildConfig.VERSION_CODE
            fun getFile(code: Int) = File("${apl.filesDir}/okhttp_cache/${code}")

            val cacheFile = getFile(currentVersion).apply {
                mkdirs()
            }
            cache = Cache(cacheFile, 1024 * 1024 * 10)

            (MainScope() + Dispatchers.IO).launch {
                (1 until currentVersion).forEach {
                    runCatching {
                        getFile(it).deleteRecursively()
                    }
                }
            }
        }
        return cache
    }

    fun Retrofit.setOkhttpCache(apl: Application): Retrofit {
        val mCache = getCache(apl)
        return if (mCache != null) {
            responseCache(mCache)
        } else {
            this
        }
    }

}