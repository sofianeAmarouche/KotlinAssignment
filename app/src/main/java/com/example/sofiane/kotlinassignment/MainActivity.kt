package com.example.sofiane.kotlinassignment
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), DataAdapter.Listener {

    private var BASE_URL = "http://www.recipepuppy.com/"

    private val TAG = MainActivity::class.java.simpleName

    private var mAdapter: DataAdapter? = null
    private var mDataSet: ArrayList<RecipesJSON>? = null
    private var mCompositeDisposable: CompositeDisposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCompositeDisposable = CompositeDisposable()

        initRecyclerView()

        loadJSON()
    }

    private fun initRecyclerView() {
        recycle_view.setHasFixedSize(true)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recycle_view.layoutManager = layoutManager
    }

    private fun loadJSON() {
        val requestInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RecipesInterface::class.java)

        mCompositeDisposable?.add(requestInterface.getData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError))
    }

    private fun handleResponse(recipeList: JSONResponse) {
        mDataSet = ArrayList(recipeList.results)
        mAdapter = DataAdapter(mDataSet!!, this)

        recycle_view.adapter = mAdapter
    }

    private fun handleError(error: Throwable) {
        Log.d(TAG, error.localizedMessage)

        Toast.makeText(this, "Error ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
    }


    override fun onItemClick(recipesJSON: RecipesJSON) {
        Toast.makeText(this, "${recipesJSON.title} Clicked !", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }
}

