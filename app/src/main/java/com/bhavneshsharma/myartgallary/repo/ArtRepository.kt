package com.bhavneshsharma.myartgallary.repo

import androidx.lifecycle.LiveData
import com.bhavneshsharma.myartgallary.api.RetrofitAPI
import com.bhavneshsharma.myartgallary.model.ImageResponse
import com.bhavneshsharma.myartgallary.roomdb.Art
import com.bhavneshsharma.myartgallary.roomdb.ArtDao
import com.bhavneshsharma.myartgallary.util.Resource
import javax.inject.Inject;

class ArtRepository @Inject constructor(
    private val artDao: ArtDao,
    private val retrofitAPI: RetrofitAPI
) : ArtRepositoryInterface {


    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.deleteArt(art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {
            val response = retrofitAPI.imageSearch(imageString)
            if (response.isSuccessful){
                response.body()?.let {
                    return  Resource.success(it)
                } ?: Resource.error("Error",null)
            }else{
                Resource.error("Error",null)
            }
        }catch (e : Exception){
            Resource.error("No data!", null)
        }
    }
}