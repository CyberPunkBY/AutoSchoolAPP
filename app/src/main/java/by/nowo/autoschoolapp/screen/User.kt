package by.nowo.autoschoolapp.screen

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import by.nowo.autoschoolapp.model.User
import by.nowo.autoschoolapp.retrofit.RetrofitHelper
import by.nowo.autoschoolapp.retrofit.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun UserListScreen() {
    val retrofit = RetrofitHelper.getInstance().create(UserService::class.java)
    val userService = remember { retrofit }
    var users by remember { mutableStateOf(emptyList<User>()) }
    val scope = rememberCoroutineScope()

    // Выполняем запрос к серверу в корутине
    LaunchedEffect(true) {
        scope.launch {
            var userList = emptyList<User>()
            withContext(Dispatchers.IO) {
                try {
                    userList = userService.getAllUsers().body()!!
                } catch (e: Exception) {
                    // Обработка ошибки
                    userList = emptyList<User>()
                    Log.d(ContentValues.TAG, "${e.message}")
                }
            }
            users = userList
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "List Users",
            style = MaterialTheme.typography.displayMedium
        )
        users.forEach { user ->
            //UserListItem(user)
            UserItem(user)
        }
    }
}

@Composable
fun UserItem(user: User) {
//    val user = User(
//        13,
//        "Навагродскі",
//        "Андрэй",
//        "Иванавіч",
//        "375297890751",
//        "E")

    fun categoryColor(category: String): Color {
        return when (category) {
            "C" -> Color("#FFA500".toColorInt())
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
                .padding(5.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(5.dp)
            ) {
                Box(modifier = Modifier
                    .width(10.dp)
                    .height(35.dp)
                    .background(categoryColor(user.category)))
                Column {
                    Row {
                        Text(
                            text = "${user.id}",
                            modifier = Modifier.padding(5.dp, 0.dp),
                            fontSize = 10.sp
                        )
                        Text(
                            text = "${user.lname} ${user.fname} ${user.sname}"
                        )
                    }
                    Row{
                        Text(
                            text = "+${user.phonenumber}",
                            textAlign = TextAlign.End,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth().height(20.dp).wrapContentHeight()
                        )
                    }
                }
            }
        }
    }
}