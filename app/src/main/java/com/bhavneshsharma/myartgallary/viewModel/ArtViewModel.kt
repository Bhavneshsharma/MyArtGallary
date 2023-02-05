package com.bhavneshsharma.myartgallary.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bhavneshsharma.myartgallary.model.ImageResponse
import com.bhavneshsharma.myartgallary.repo.ArtRepositoryInterface
import com.bhavneshsharma.myartgallary.roomdb.Art
import com.bhavneshsharma.myartgallary.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtViewModel @Inject constructor(
    private val repository : ArtRepositoryInterface
) : ViewModel() {

    //Art Fragment
    val artList = repository.getArt()

    //Image API Fragment
    private val image = MutableLiveData<Resource<ImageResponse>>()
    val imageList : LiveData<Resource<ImageResponse>>
    get() = image

    private val selectedImage = MutableLiveData<String>()
    val selectImageUrl : LiveData<String>
    get() = selectedImage

    // Art Details Fragment
    private var insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage : LiveData<Resource<Art>>
    get() = insertArtMsg

    fun resetInsertArtMsg(){
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url : String){
        selectedImage.postValue(url)
    }

    fun deleteArt(art: Art)= viewModelScope.launch{
        repository.deleteArt(art)
    }

    fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art)
    }

    fun searchForImage(searchString : String){
        if (searchString.isEmpty()){
            return
        }
        image.value = Resource.loading(null)
        viewModelScope.launch {
            val response = repository.searchImage(searchString)
            image.value = response
        }
    }

    fun makeArt(name : String, artistName : String, year : String){
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()){
            insertArtMsg.postValue(Resource.error("Enter Name, artist, Year",null))
            return
        }

        val yearInt = try {
            year.toInt()
        }catch (e : Exception){
            insertArtMsg.postValue(Resource.error("Year should be number",null))
            return
        }

        val art = Art(name,artistName,yearInt,selectedImage.value?:"")
        insertArt(art)
        setSelectedImage("")
        insertArtMsg.postValue(Resource.success(art))
    }

}