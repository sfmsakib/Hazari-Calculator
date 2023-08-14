package com.sprout.hazaricalculator.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sprout.aldebarandialog.AldebaranCustomDialog
import com.sprout.aldebarandialog.AldebaranDialog
import com.sprout.hazaricalculator.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_name_input.*
import kotlinx.android.synthetic.main.insert_score.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DashboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DashboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sessionManager:SessionManager
    private lateinit var database: LocalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        sessionManager = SessionManager(requireContext())
        database = LocalDatabase.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }
    private lateinit var customDialog: AldebaranCustomDialog
    private lateinit var scoreDialog: AldebaranCustomDialog

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (sessionManager.isLoggedIn){
            val players: Players = sessionManager.players!!
            name1.text = players.player1
            name2.text = players.player2
            name3.text = players.player3
            name4.text = players.player4
        }


        CoroutineScope(Dispatchers.IO).launch {
            Log.i("FUAD_LOG", database.ScoreDao().getSum().toString())
        }

        CoroutineScope(Dispatchers.IO).launch {
            Log.i("FUAD_LOG", database.ScoreDao().getAllScore().value.toString())
        }

        CoroutineScope(Dispatchers.IO).launch {
            val sum1 = database.ScoreDao().getSum()
            Log.i("FUAD_LOG", "Sum1: $sum1")

        }

        val sum :LiveData<Results> = database.ScoreDao().getSum()

        var winingValue : Int = 1000
        sum.observe(viewLifecycleOwner){
            Log.i("FUAD_LOG", "observe Sum:$it")
            result1.text = it.one.toString()
            result2.text = it.two.toString()
            result3.text = it.three.toString()
            result4.text = it.four.toString()

            if (it.one >= winingValue){
                showWishDialog(sessionManager.players!!.player1)
            }else if (it.two >= winingValue){
                showWishDialog(sessionManager.players!!.player2)
            }else if (it.three >= winingValue){
                showWishDialog(sessionManager.players!!.player3)
            }else if (it.four >= winingValue){
                showWishDialog(sessionManager.players!!.player4)
            }
        }


        recyclerview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)


        val allList :LiveData<List<Score>> = database.ScoreDao().getAllScore()

        allList.observe(viewLifecycleOwner){
            Log.i("FUAD_LOG", "observe all:$it")
            val adapter = ListAdapter(it)
            recyclerview.adapter = adapter
        }

        fab.setOnClickListener{
            Log.i("FUAD_LOG", "recyclerview")
            showScoreUpdateDialog()
        }

        tool_bar.inflateMenu(R.menu.dboard)
        tool_bar.setOnMenuItemClickListener{ item->
            when(item.itemId){
                R.id.add_name -> {
                    Log.i("FUAD_LOG", "onOptionsItemSelected1")

                    // User chose the "Settings" item, show the app settings UI...

                    customDialog = AldebaranCustomDialog(context)
                        .setView(R.layout.fragment_name_input)
                        .setCancelable(false)
                        .configureView{v ->
                            val et1 = v?.findViewById<EditText>(R.id.player1)
                            val et2 = v?.findViewById<EditText>(R.id.player2)
                            val et3 = v?.findViewById<EditText>(R.id.player3)
                            val et4 = v?.findViewById<EditText>(R.id.player4)
                            val btnSubmit = v?.findViewById<Button>(R.id.submit)
                            btnSubmit?.setOnClickListener{
                                var t1: String = ""
                                var t2: String = ""
                                var t3:String = ""
                                var t4:String = ""
                                if (et1?.text?.isNotEmpty() == true){
                                    t1 = et1?.text.toString().trim()
                                }else{
                                    et1?.error = "Required"
                                }

                                if(et2?.text?.isNotEmpty() == true){
                                    t2 = et2?.text.toString().trim()

                                }else{
                                    et2?.error = "Required"
                                 }
                                if(et3?.text?.isNotEmpty() == true){
                                    t3 = et3?.text.toString().trim()
                                }else{
                                    et3?.error = "Required"
                                 }

                                if(et4?.text?.isNotEmpty() == true){
                                    t4 = et4?.text.toString().trim()
                                }else{
                                    et4?.error = "Required"
                                }


                                if (t1.isNotEmpty() && t2.isNotEmpty() && t3.isNotEmpty() && t4.isNotEmpty()){
                                    Log.i("FUAD_LOG", "submit if : $t1 $t2 $t3 $t4")
                                    name1.text = t1
                                    name2.text = t2
                                    name3.text = t3
                                    name4.text = t4

                                    val pl = Players(t1,t2,t3,t4)

                                    sessionManager.players = pl
                                    sessionManager.setLogin(true)

                                    customDialog.dismiss()
                                }else{
                                    Log.i("FUAD_LOG", "submit else : $t1 $t2 $t3 $t4")

                                }


                            }
                            val btnCancel = v?.findViewById<Button>(R.id.cancel)
                            btnCancel?.setOnClickListener {
                                Log.i("FUAD_LOG", "btnCancel")
                                customDialog.dismiss()

                            }
                        }

                        customDialog.show()

                    true
                }

                R.id.add_score -> {
                    // User chose the "Favorite" action, mark the current item
                    Log.i("FUAD_LOG", "onOptionsItemSelected2")
                    showScoreUpdateDialog()
                    true
                }
                R.id.clear_record -> {
                    // User chose the "Favorite" action, mark the current item
                    Log.i("FUAD_LOG", "onOptionsItemSelected2")
                    showClearDialog()
                    true
                }


                else -> {        // If we got here, the user's action was not recognized.
                    // Invoke the superclass to handle it.
                    Log.i("FUAD_LOG", "onOptionsItemSelected3")

                    super.onOptionsItemSelected(item)
                }
            }
        }
    }

    private fun showWishDialog(player1: String) {
        AldebaranDialog(context)
            .setTitle("Game Over")
            .setMessage("Congratulation MR. $player1 for scoring 1000 up score!!")
            .setIcon(R.drawable.baseline_filter_vintage_24)
            .setCancelable(false)
            .setTopColor(requireContext().getColor(R.color.search_opaque))
            .setNeutralButton("ok") {}
            .show()

    }
    private fun showClearDialog() {
        AldebaranDialog(context)
            .setTitle("Clear Data!")
            .setMessage("Are you sure you want to delete all data?")
            .setIcon(R.drawable.baseline_clear_all_24)
            .setCancelable(false)
            .setTopColor(requireContext().getColor(R.color.search_opaque))
            .setPositiveButton("Yes") {
                CoroutineScope(Dispatchers.IO).launch {
                    database.ScoreDao().deleteAllScore()
                }
            }
            .setNegativeButton("No"){}
            .show()

    }

    private fun showScoreUpdateDialog() {

        scoreDialog = AldebaranCustomDialog(context)
            .setView(R.layout.insert_score)
            .setCancelable(false)
            .configureView{v ->

                if (sessionManager.isLoggedIn){
                    val tv1 = v?.findViewById<TextView>(R.id.is_player1)
                    tv1?.text = sessionManager.players?.player1.toString()

                    val tv2 = v?.findViewById<TextView>(R.id.is_player2)
                    tv2?.text = sessionManager.players?.player2.toString()

                    val tv3 = v?.findViewById<TextView>(R.id.is_player3)
                    tv3?.text = sessionManager.players?.player3.toString()

                    val tv4 = v?.findViewById<TextView>(R.id.is_player4)
                    tv4?.text = sessionManager.players?.player4.toString()
                }

                val et1 = v?.findViewById<EditText>(R.id.score1)
                val et2 = v?.findViewById<EditText>(R.id.score2)
                val et3 = v?.findViewById<EditText>(R.id.score3)
                val et4 = v?.findViewById<EditText>(R.id.score4)

                val btnSubmit = v?.findViewById<Button>(R.id.submit)
                btnSubmit?.setOnClickListener{
                    var t1 = 0
                    var t2 = 0
                    var t3 = 0
                    var t4 = 0
                    var count = 0

                    if (et1?.text?.isNotEmpty() == true){
                        t1 = et1?.text.toString().trim().toInt()
                        count++
                    }else{
                        et1?.error = "Required"
                    }

                    if(et2?.text?.isNotEmpty() == true){
                        t2 = et2?.text.toString().trim().toInt()
                        count++
                    }else{
                        et2?.error = "Required"
                    }

                    if(et3?.text?.isNotEmpty() == true){
                        t3 = et3?.text.toString().trim().toInt()
                        count++
                    }else{
                        et3?.error = "Required"
                    }

                    if(et4?.text?.isNotEmpty() == true){
                        t4 = et4?.text.toString().trim().toInt()
                        count++
                    }else{
                        et4?.error = "Required"
                    }


                    if (count>=3){

                        val remaining = 360 - (t1+t2+t3+t4)
                        Log.i("FUAD_LOG", "submit if :$remaining $t1 $t2 $t3 $t4")
                        if (et1?.text?.isEmpty()==true){
                            t1 = remaining
                        }else if (et2?.text?.isEmpty() == true){
                            t2 = remaining
                        }else if (et3?.text?.isEmpty() == true){
                            t3 = remaining
                        }else if (et4?.text?.isEmpty() == true){
                            t4 = remaining
                        }
                        val total = (t1+t2+t3+t4)

                        if (remaining < 0){
                            Toast.makeText(requireContext(),"Total value is exceeded 360!!",Toast.LENGTH_SHORT).show()
                        }else if (total != 360) {
                            Toast.makeText(requireContext(),"Total value is below 360!!",Toast.LENGTH_SHORT).show()
                        }else{
                            val sc = Score(null,t1,t2,t3,t4)


                            CoroutineScope(Dispatchers.IO).launch {
                                database.ScoreDao().insert(sc)
                            }
                            Log.i("FUAD_LOG", "submit sc : $sc")

                            scoreDialog.dismiss()
                        }
                    }else{
                        Log.i("FUAD_LOG", "submit else : $t1 $t2 $t3 $t4 ")

                    }


                }
                val btnCancel = v?.findViewById<Button>(R.id.cancel)
                btnCancel?.setOnClickListener {
                    Log.i("FUAD_LOG", "btnCancel")
                    scoreDialog.dismiss()

                }
            }

        scoreDialog.show()


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DashboardFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }

            }
    }


}