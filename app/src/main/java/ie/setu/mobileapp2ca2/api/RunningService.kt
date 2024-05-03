package ie.setu.mobileapp2ca2.api

import ie.setu.mobileapp2ca2.models.RunningModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RunningService {
    @GET("/tracks")
    fun findall(): Call<List<RunningModel>>

    @GET("/tracks/{email}")
    fun findall(@Path("email") email: String?)
            : Call<List<RunningModel>>

    @GET("/tracks/{email}/{id}")
    fun get(@Path("email") email: String?,
            @Path("id") id: String): Call<RunningModel>

    @DELETE("/tracks/{email}/{id}")
    fun delete(@Path("email") email: String?,
               @Path("id") id: String): Call<RunningWrapper>

    @POST("/tracks/{email}")
    fun post(@Path("email") email: String?,
             @Body track: RunningModel)
            : Call<RunningWrapper>

    @PUT("/tracks/{email}/{id}")
    fun put(@Path("email") email: String?,
            @Path("id") id: String,
            @Body track: RunningModel
    ): Call<RunningWrapper>
}