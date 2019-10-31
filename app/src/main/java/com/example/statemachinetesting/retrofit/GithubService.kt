package com.example.statemachinetesting.retrofit

import com.example.statemachinetesting.model.Repo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

/**
 *GithubService
 *
 *@author Vesko Nikolov /vnikolov@pkdevs.com/
 *@since 28.10.2019 г.
 */
interface GitHubService {
    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String): Single<List<Repo>>
}