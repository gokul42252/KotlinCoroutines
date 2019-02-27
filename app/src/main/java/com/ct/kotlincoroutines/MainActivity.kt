package com.ct.kotlincoroutines

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.ct.kotlincoroutines.adapter.PhotoAdapter
import com.ct.kotlincoroutines.adapter.PostAdapter
import com.ct.kotlincoroutines.data.PlaceholderPhotos
import com.ct.kotlincoroutines.data.PlaceholderPosts
import com.ct.kotlincoroutines.network.ApiFactory
import com.ct.kotlincoroutines.network.PlaceholderApi
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class MainActivity : AppCompatActivity(), CoroutineScope {
    private val job: Job = SupervisorJob()
    private var service: PlaceholderApi? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val scope = CoroutineScope(Dispatchers.Default + job)

    /**
     * https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/new-fixed-thread-pool-context.html
     * Creates new co routine execution context with the fixed-size thread-pool and built-in yield support.
     *  Parameters
    nThreads - the number of threads.
    name - the base name of the created threads.
     */
    @ObsoleteCoroutinesApi
    private val background = newFixedThreadPoolContext(2, "bg")

    private var recView: RecyclerView? = null

    init {
        service = ApiFactory.placeholderApi
    }

    /**
     * In the kotlinx.coroutines library, you can start new co-routine using either launch or async function.
     *
     *
     * The difference is that launch returns a Job and does not carry any resulting value,
     * while async returns a Deferred - a light-weight non-blocking future that represents a promise to provide a result later.
     * You can use .await() on a deferred value to get its eventual result,
     * but Deferred is also a Job, so you can cancel it if needed.
     *
     * runBlocking
     * ///////////
     * Runs new co routine and blocks current thread interruptibly until its completion.
     * This function should not be used from coroutine. It is designed to bridge regular blocking code to
     * libraries that are written in suspending style, to be used in main functions and in tests.
     */

    override fun onCreate(savedInstanceState: Bundle?) = runBlocking {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recView = findViewById(R.id.rec)
        //  getPosts()
        getPhotos()
        //  getUsers()
    }

    /**
     * Method to get photo list from service
     */
    private fun getPhotos() {
        scope.launch {
            val photoRequest = service!!.getPhotos()
            val photoList = photoRequest.await()
            if (photoList.isSuccessful) {
                val posts = photoList.body()
                populateView(posts!!)

            }
        }
    }

    /**
     * Method to populate recycler view from list
     *   launch(Dispatchers.Main) used to populate list in Main thread
     */
    private fun populateView(photoList: List<PlaceholderPhotos>) {
        launch(Dispatchers.Main) {
            recView!!.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            recView!!.adapter = PhotoAdapter(this@MainActivity, photoList)
            recView!!.adapter!!.notifyDataSetChanged()
        }
    }

    @ObsoleteCoroutinesApi
    private fun getUsers() {
        val def = async(background) {
            val userRequest = service!!.getUsers()
            try {
                val users = userRequest.await()
                if (users.isSuccessful) {
                    val userList = users.body()
                } else {
                    Log.d("", "")
                }
            } catch (e: Exception) {
                Log.d("", "")
            }

        }
    }


    private fun getPosts() {
        /**
         * The following standard implementations are provided by kotlinx.coroutines as properties on Dispatchers objects:

        /// Dispatchers.Default – is used by all standard builder if no dispatcher nor any other ContinuationInterceptor
        is specified in their context. It uses a common pool of shared background threads.
        This is an appropriate choice for compute-intensive coroutines that consume CPU resources.
        /// Dispatchers.IO – uses a shared pool of on-demand created threads and is designed for offloading of
        IO-intensive blocking operations (like file I/O and blocking socket I/O).
        ///  Dispatchers.Unconfined – starts coroutine execution in the current call-frame until the first suspension.
        On first suspension the coroutine builder function returns. The coroutine resumes in whatever thread that is
        used by the corresponding suspending function, without confining it to any specific thread or pool.
        Unconfined dispatcher should not be normally used in code.
        Private thread pools can be created with newSingleThreadContext and newFixedThreadPoolContext.
        An arbitrary Executor can be converted to dispatcher with as CoroutineDispatcher extension function.
         */

        /**
         * Global scope is used to launch top-level coroutines which are
         * operating on the whole application lifetime and are not cancelled prematurely.
         *
         *
         * Application code usually should use application-defined CoroutineScope,
         * using async or launch on the instance of GlobalScope is highly discouraged.
         *
         */
        launch(Dispatchers.Main) {
            val postRequest = service!!.getPosts()
            val response = postRequest.await()
            try {
                if (response.isSuccessful) {
                    val posts = response.body()
                    populateList(posts)

                }
            } catch (e: Exception) {
                Log.d("", "")
            }
        }
    }

    /**
     * Method to populate post in recycler view
     */
    private fun populateList(posts: List<PlaceholderPosts>?) {
        launch(Dispatchers.Main) {
            recView!!.layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            recView!!.adapter = PostAdapter(posts!!)
            recView!!.adapter!!.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancelChildren()
    }

}
