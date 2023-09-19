package by.nowo.autoschoolapp.model


import retrofit2.http.Field

data class User(
    @Field("id")
    val id: Int,

    @Field("lname")
    val lname: String,

    @Field("fname")
    val fname: String,

    @Field("sname")
    val sname: String,

    @Field("phonenumber")
    val phonenumber: String,

    @Field("category")
    val category: String,
)
