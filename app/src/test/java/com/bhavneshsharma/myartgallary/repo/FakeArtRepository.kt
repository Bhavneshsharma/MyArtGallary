package com.bhavneshsharma.myartgallary.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bhavneshsharma.myartgallary.model.ImageResponse
import com.bhavneshsharma.myartgallary.roomdb.Art
import com.bhavneshsharma.myartgallary.util.Resource

class FakeArtRepository : ArtRepositoryInterface {

    private val arts = mutableListOf<Art>()
    private val artsLiveData = MutableLiveData<List<Art>>(arts)

    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refreshData()
    }

    override suspend fun deleteArt(art: Art) {
        arts.remove(art)
        refreshData()
    }

    override fun getArt(): LiveData<List<Art>> {
        return artsLiveData
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return Resource.success(ImageResponse(listOf(),0,0))
    }

    fun refreshData(){
        artsLiveData.postValue(arts)
    }
}