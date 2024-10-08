package com.papb.praktikum2


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.cardview.widget.CardView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.papb.praktikum2.ui.theme.Praktikum2Theme

class ListActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore
    private val matkulList = mutableStateListOf<matkul>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance() // Inisialisasi Firestore di sini

        // Panggil fetchMatkulData untuk mengambil data saat Activity dibuat
        fetchMatkulData()

        setContent {
            Praktikum2Theme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    MatkulListScreen(matkulList) // Panggil MatkulListScreen di sini
                }
            }
        }
    }

    private fun fetchMatkulData() {
        db.collection("matkul")
            .get()
            .addOnSuccessListener { result ->
                matkulList.clear()
                for (document in result) {
                    try {
                        val matkulItem = document.toObject(matkul::class.java)
                        matkulList.add(matkulItem)
                    } catch (e: Exception) {
                        println("Error parsing document: ${e.message}")
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents", exception)
            }
    }


}


@Composable
fun MatkulListScreen(matkulList: List<matkul>) {
    LazyColumn {
        items(matkulList) { item ->
            MatkulCard(matkul = item) // Gunakan MatkulCard yang sudah ada
        }
    }
}

@Composable
fun MatkulCard(matkul: matkul) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Mata Kuliah: ${matkul.matkul}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Hari: ${matkul.hari}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Jam: ${matkul.jam}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Ruang: ${matkul.ruang}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Praktikum: ${if (matkul.praktikum) "Ya" else "Tidak"}", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

