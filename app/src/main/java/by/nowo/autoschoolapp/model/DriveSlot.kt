package by.nowo.autoschoolapp.model

import java.sql.Timestamp

data class DriveSlots(
    val id: Long,
    val value: Timestamp,
    val category: String,
    var userid: Long?
)