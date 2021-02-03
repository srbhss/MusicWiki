package srbhss.musicwki

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
@Suppress("DEPRECATION")
internal class MyAdapter(
        var context: Context,
        fm: FragmentManager,
        var totalTabs: Int,
        var genre: String
) :
        FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Items(genre, "album")
            }
            1 -> {
                Items(genre, "artist")
            }
            2 -> {
                Items(genre, "track")
            }
            else -> getItem(position)
        }
    }
    override fun getCount(): Int {
        return totalTabs
    }
}
