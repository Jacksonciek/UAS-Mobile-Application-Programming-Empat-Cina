import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.uts_empat_cina_map.R
import com.example.uts_empat_cina_map.SeeAllActivity

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Find the seeAll TextView by its ID
        val seeAll: TextView = view.findViewById(R.id.seeAll)

        // Set OnClickListener to navigate to another page
        seeAll.setOnClickListener {

            // Option 2: Start a new Activity
             val intent = Intent(activity, SeeAllActivity::class.java)
             startActivity(intent)
        }

        return view
    }
}
