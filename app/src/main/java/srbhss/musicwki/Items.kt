package srbhss.musicwki

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONException
import org.json.JSONObject

class Items(var genre: String, var itemType: String)
    : Fragment() {
    lateinit var fragment: View
    private lateinit var gridView: GridView
    private var names = arrayOf<String>()
    private var images = arrayOf<Int>()

    private val networkClient: NetworkClient = NetworkClient()
    private val params: RequestParams = networkClient.params
    private val client: AsyncHttpClient = networkClient.client

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragment = inflater.inflate(R.layout.fragment_items, container, false)
        gridView = fragment.findViewById(R.id.gridViewItems) as GridView

        val url = "?method=tag.gettop${itemType}s&tag=$genre&api_key=${networkClient.getApiKey()}&format=json"
        client.get(networkClient.getAbsoluteUrl(url), params, object : JsonHttpResponseHandler() {
            override fun onSuccess(code: Int, headers: Array<Header?>?, body: JSONObject) {
                try {
                    val albumArray = body.getJSONArray("top${itemType}s")
                    var listN: MutableList<String> = names.toMutableList()
                    var listI: MutableList<Int> = images.toMutableList()
                    for (i in 0 until albumArray.length()) {
                        listN.add((albumArray[i] as JSONObject).getJSONObject(itemType).getJSONObject("name").toString())
                        listI.add((albumArray[i] as JSONObject).getJSONObject(itemType).getJSONArray("image")[0] as Int)
                    }
                    names = listN.toTypedArray()
                    images = listI.toTypedArray()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, res: String, t: Throwable) {
            }
        })

        val mainAdapter = MainAdapter(fragment.context, names, images)
        gridView.adapter = mainAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(fragment.context, names[+position],
                    Toast.LENGTH_SHORT).show()
            goToDetails(fragment.context, names[+position].toString())
        }
        return fragment
    }

    private fun goToDetails(context: Context, name: String) {
        val intent = Intent(context, Details::class.java)
        intent.putExtra("key_name", name)
        intent.putExtra("key_type", itemType)
        startActivity(intent)
    }
}
