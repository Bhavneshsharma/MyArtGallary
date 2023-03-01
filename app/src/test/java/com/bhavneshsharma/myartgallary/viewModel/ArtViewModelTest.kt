package com.bhavneshsharma.myartgallary.viewModel

import com.bhavneshsharma.myartgallary.repo.FakeArtRepository
import com.bhavneshsharma.myartgallary.util.getOrAwaitValueTest
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.bhavneshsharma.myartgallary.roomdb.Art
import com.bhavneshsharma.myartgallary.util.MainCoroutineRule
import com.bhavneshsharma.myartgallary.util.Resource
import com.bhavneshsharma.myartgallary.util.Status
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var  viewModel : ArtViewModel
    private lateinit var value : Resource<Art>

    @Before
    fun setUp(){
        viewModel = ArtViewModel(FakeArtRepository())

    }

    @Test
    fun `insert art without year return error`(){
        viewModel.makeArt("MonaLisa","DavinChi","")
        value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertEquals(
            "Without Year Data",
            Status.ERROR,
            value.status
        )
    }

    @Test
    fun `insert art without name return error`(){
        viewModel.makeArt("","DavinChi","1998")
        value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertEquals(
            "Without Name Data",
            Status.ERROR,
            value.status
        )
    }

    @Test
    fun `insert art without Artist name return error`(){
        viewModel.makeArt("MonaLisa","","1998")
        value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertEquals(
            "Without Artist Name",
            Status.ERROR,
            value.status
        )
    }

    @Test
    fun `insert Art with all data return success`(){
        viewModel.makeArt("Bhvanesh","paresnts","1998")
        value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertEquals(
            "When all data available",
            Status.SUCCESS,
            value.status
        )
    }
}