package com.tutorial.tvapp.search

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.leanback.app.SearchSupportFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter
import com.google.gson.Gson
import com.tutorial.tvapp.DataModel
import com.tutorial.tvapp.ItemPresenter
import com.tutorial.tvapp.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class SearchFragment : SearchSupportFragment(),SearchSupportFragment.SearchResultProvider{
   private lateinit var mRowsAdapter : ArrayObjectAdapter
   private lateinit var speechRecognizer:SpeechRecognizer
   private val REQUEST_SPEECH= 0x00000010
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSearchResultProvider(this)
        mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizer.setRecognitionListener(object :RecognitionListener{
            override fun onReadyForSpeech(p0: Bundle?) {
            }

            override fun onBeginningOfSpeech() {
            }

            override fun onRmsChanged(p0: Float) {
            }

            override fun onBufferReceived(p0: ByteArray?) {
            }

            override fun onEndOfSpeech() {
            }

            override fun onError(p0: Int) {
            }

            override fun onResults(results: Bundle) {
                val recognizedResults = results.getStringArrayList((SpeechRecognizer.RESULTS_RECOGNITION))
                if(recognizedResults != null && recognizedResults.size > 0) {
                    val recognizedText = recognizedResults[0]
                    setSearchQuery(recognizedText,true)
                }
            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {
            }
        })
        startSpeedRecognition()
    }
    private fun startSpeedRecognition(){
        try {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            startActivityForResult(intent, REQUEST_SPEECH)
        } catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_SPEECH && resultCode == Activity.RESULT_OK){
            setSearchQuery(data,true)
        }
    }
    override fun getResultsAdapter(): ObjectAdapter {
        val gson = Gson()
        val i : InputStream = requireContext().assets.open("movies.json")
        val br = BufferedReader(InputStreamReader(i))
        val mItems : DataModel = gson.fromJson(br,DataModel::class.java)
        val listRowAdapter = ArrayObjectAdapter(ItemPresenter())
        listRowAdapter.addAll(0,mItems.result[0].details)
        val header = HeaderItem("Your Search Results")
        mRowsAdapter.add(ListRow(header,listRowAdapter))
        return mRowsAdapter
    }

    override fun onQueryTextChange(newQuery: String?): Boolean {
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Toast.makeText(context,query,Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= super.onCreateView(inflater, container, savedInstanceState)
        val microphoneButton= view?.findViewById<View>(androidx.leanback.R.id.lb_search_bar_speech_orb)
        microphoneButton?.setOnClickListener{
            startSpeedRecognition()
        }
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
    }

}