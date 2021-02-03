package srbhss.musicwki

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.chip.Chip
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class GenreDetails : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager: ViewPager
    lateinit var genreTitle: TextView
    lateinit var genreDetail: TextView
    private val networkClient: NetworkClient = NetworkClient()
    private val params: RequestParams = networkClient.params
    private val client: AsyncHttpClient = networkClient.client

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        genreTitle = findViewById(R.id.genreTitle)
        genreDetail = findViewById(R.id.genreDetail)

        val genre = (intent.extras as android.os.Bundle).getString("key").toString()
        genreTitle.text = genre

        val url = "?method=tag.getinfo&tag=$genre&api_key=${networkClient.getApiKey()}&format=json"
        client.get(networkClient.getAbsoluteUrl(url), params, object : JsonHttpResponseHandler() {
            override fun onSuccess(code: Int, headers: Array<Header?>?, body: JSONObject) {
                try {
                    val tagDetail = body.getJSONObject("tag").getJSONObject("wiki")
                    genreDetail.text = tagDetail.getJSONObject("content").toString()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, res: String, t: Throwable) {
            }
        })
        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        tabLayout.addTab(tabLayout.newTab().setText("Albums"))
        tabLayout.addTab(tabLayout.newTab().setText("Artists"))
        tabLayout.addTab(tabLayout.newTab().setText("Tracks"))
        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        val adapter = MyAdapter(this, supportFragmentManager,
                tabLayout.tabCount, genre)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}