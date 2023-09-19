package by.nowo.autoschoolapp.screen

import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.nowo.autoschoolapp.createTokenManager
import by.nowo.autoschoolapp.datastore.ApiService
import by.nowo.autoschoolapp.datastore.TokenManager
import by.nowo.autoschoolapp.model.DriveSlots
import by.nowo.autoschoolapp.model.users.ShortUsersInfo
import by.nowo.autoschoolapp.retrofit.DriveSlotService
import by.nowo.autoschoolapp.retrofit.RetrofitHelper
import by.nowo.autoschoolapp.retrofit.ShortUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Timestamp

@Composable
fun DriveSlotList() {

    val retrofit = RetrofitHelper.getInstance().create(DriveSlotService::class.java)
    val driveSlotService = remember { retrofit }
    var driveSlots by remember { mutableStateOf(emptyList<DriveSlots>()) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            var dsList = emptyList<DriveSlots>()
            withContext(Dispatchers.IO) {
                try {
                    dsList = driveSlotService.getAllDriveSlots().body()!!
                } catch (e: Exception) {
                    // Обработка ошибки
                    dsList = emptyList<DriveSlots>()
                    Log.d(ContentValues.TAG, "${e.message}")
                }
            }
            driveSlots = dsList
        }
    }

    Column {
        driveSlots.forEach { it ->
            DriveSlotItem(it)
            Spacer(modifier = Modifier.height(1.dp))
        }
    }
}



//@Preview(showBackground = true)
@Composable
fun DriveSlotItem(driveSlot: DriveSlots) {
    //val day = driveSlot.value.date
    fun categoryColor(category: String): Color {
        return when (category) {
            "C" -> Color.Yellow
            "E" -> Color.Red
            else -> Color.LightGray
        }
    }

    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text("ID: ${driveSlot.id}", fontSize = 10.sp)
                Text("Value: ${driveSlot.value}")
                Text(
                    "Category: ${driveSlot.category}",
                    modifier = Modifier.background(categoryColor(driveSlot.category))
                )
                //Text("UserID: ${driveSlot.userid}")


                if(driveSlot.userid != null) {
                    val retrofit2 = RetrofitHelper.getInstance().create(ShortUserService::class.java)
                    val shortUsersService = remember { retrofit2 }
                    var shortUsersInfo by remember { mutableStateOf<ShortUsersInfo?>(null) }
                    //val shortUsersInfo by remember { mutableStateOf(emptyList<ShortUsersInfo>())}
                    val coroutineScope = rememberCoroutineScope()

                    LaunchedEffect(key1 = Unit) {
                        coroutineScope.launch {
                            var shortUsrInf = ShortUsersInfo()
                            withContext(Dispatchers.IO) {
                                try {
                                    Log.d(ContentValues.TAG, "ShortUserInfo -> start")
                                    shortUsrInf = shortUsersService.getUserById(driveSlot.userid!!.toInt()).body()!!
                                } catch (e: Exception) {
                                    // Обработка ошибки
                                    shortUsrInf = ShortUsersInfo()
                                    Log.d(ContentValues.TAG, "${e.message}")
                                }
                            }
                            shortUsersInfo = shortUsrInf
                        }
                    }
                    Text("User: ${shortUsersInfo?.name}")
                } else {
                    Text("User: ${driveSlot.userid}")
                }



            }
        }
    }

}

@Composable
fun ShortUsersInfoItem(shortUsersInfo: ShortUsersInfo) {
    MaterialTheme(
        colorScheme = lightColorScheme()
    ) {
        Column {
            Text("${shortUsersInfo.name} ${shortUsersInfo.category}")
        }
    }
}

@Preview
@Composable
fun DriveSlotListPreview() {
    val driveSlots = listOf<DriveSlots>(
        DriveSlots(1, Timestamp.valueOf("2023-09-17 09:30:00.000"), "E", 1),
        DriveSlots(2, Timestamp.valueOf("2023-09-17 11:00:00.000"), "E", null),
        DriveSlots(3, Timestamp.valueOf("2023-09-17 14:00:00.000"), "E", null),
        DriveSlots(4, Timestamp.valueOf("2023-09-17 15:30:00.000"), "E", 2),
        DriveSlots(5, Timestamp.valueOf("2023-09-18 09:30:00.000"), "E", 1),
        DriveSlots(6, Timestamp.valueOf("2023-09-18 11:00:00.000"), "E", 3),
        DriveSlots(7, Timestamp.valueOf("2023-09-18 14:00:00.000"), "E", null),
        DriveSlots(8, Timestamp.valueOf("2023-09-18 15:30:00.000"), "E", 2),
        DriveSlots(9, Timestamp.valueOf("2023-09-19 09:30:00.000"), "C", null),
        DriveSlots(10, Timestamp.valueOf("2023-09-19 11:00:00.000"), "C", 4),
        DriveSlots(11, Timestamp.valueOf("2023-09-19 14:00:00.000"), "C", null),
        DriveSlots(11, Timestamp.valueOf("2023-09-19 05:30:00.000"), "C", null)
    )

    val usersList = listOf<ShortUsersInfo>(
        ShortUsersInfo(1, "Игнатович А.И.", "E"),
        ShortUsersInfo(2, "Новогродский А.И.", "E"),
        ShortUsersInfo(3, "Рачинский Д.И.", "E"),
        ShortUsersInfo(4, "Ханько А.К.", "С")
    )




    Column {
        driveSlots.forEach { it ->
            DriveSlotItem(it)
            Spacer(modifier = Modifier.height(3.dp))
        }
    }

}