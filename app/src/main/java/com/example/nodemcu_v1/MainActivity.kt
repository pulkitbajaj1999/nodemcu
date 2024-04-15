package com.example.nodemcu_v1


import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var btnForward: View
    private lateinit var btnBackward: View
    private lateinit var btnLeft: View
    private lateinit var btnRight: View

//    private lateinit var socket: Socket
//    private lateinit var outputStream: OutputStream
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        btnForward = findViewById(R.id.btn_forward)
        btnBackward = findViewById(R.id.btn_forward)
        btnLeft = findViewById(R.id.btn_forward)
        btnRight = findViewById(R.id.btn_forward)

//        // initialize socket and output stream
//        try {
//            socket = Socket("192.168.4.1", 80) // Replace with your NodeMCU IP and port
//            outputStream = socket.getOutputStream()
//            Log.i(TAG, "outputstream=======>$outputStream")
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(this@MainActivity, "Failed to connect to NodeMCU", Toast.LENGTH_SHORT)
//                .show()
//        }


        setOnTouchListener(btnForward, "F", "S")
        setOnTouchListener(btnBackward, "B", "S")
        setOnTouchListener(btnLeft, "L", "S")
        setOnTouchListener(btnRight, "R", "S")
    }

    private fun setOnTouchListener(btnView: View, beginCommand: String, stopCommand: String) {
        btnView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        // On hold - Send forward command
                        sendCommand(beginCommand)
                        Log.i("MAIN", "action=========>begin")
                    }

                    MotionEvent.ACTION_UP -> {
                        // Release - Stop the car
                        sendCommand(stopCommand)
                        Log.i("MAIN", "action==========>release")
                    }
                }
                return true
            }
        })
    }

    private fun sendCommand(command: String) {
        AsyncTask.execute {
            try {
                // Create a URL object with the endpoint you want to send the GET request to
                val url = URL("http://")

                // Open HttpURLConnection
                val connection = url.openConnection() as HttpURLConnection

                // Set request method to GET
                connection.requestMethod = "GET"

                // Read the response
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                var line: String?
                val response = StringBuilder()

                while (reader.readLine().also { line = it } != null) {
                    response.append(line)
                }
                reader.close()

                // Print the response
                println("Response: ${response.toString()}")

                // Close the connection
                connection.disconnect()

//                outputStream.write(command.toByteArray())
//                outputStream.flush()
            } catch (e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to send command", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
//            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

//    private fun executeCommand(command: String) {
//        SendCommandTask().execute(command)
//    }
//
//    private inner class SendCommandTask : AsyncTask<String, Void, Boolean>() {
//        override fun doInBackground(vararg params: String): Boolean {
//            val command = params[0]
//            Log.i("main", "command======>$command")
//            try {
//                val url = URL("http://192.168.4.1/")
//                val connection = url.openConnection() as HttpURLConnection
//                connection.requestMethod = "POST"
//                connection.doOutput = true
//                val outputStream: OutputStream = connection.outputStream
//                outputStream.write(command.toByteArray())
//                outputStream.flush()
//                outputStream.close()
//                val responseCode = connection.responseCode
//                return responseCode == HttpURLConnection.HTTP_OK
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            return false
//        }
//
//        override fun onPostExecute(result: Boolean) {
//            if (result) {
//                Toast.makeText(applicationContext, "Command sent successfully", Toast.LENGTH_SHORT)
//                    .show()
//            } else {
//                Toast.makeText(applicationContext, "Failed to send command", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }
}