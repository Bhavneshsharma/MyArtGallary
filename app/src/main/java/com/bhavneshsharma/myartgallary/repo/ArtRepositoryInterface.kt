package com.bhavneshsharma.myartgallary.repo

import androidx.lifecycle.LiveData
import com.bhavneshsharma.myartgallary.model.ImageResponse
import com.bhavneshsharma.myartgallary.roomdb.Art
import com.bhavneshsharma.myartgallary.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art : Art)

    suspend fun deleteArt(art: Art)

    fun getArt() : LiveData<List<Art>>

    suspend fun searchImage(imageString : String) : Resource<ImageResponse>
}