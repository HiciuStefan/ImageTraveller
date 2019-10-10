package com.example.imagetraveller.imagetraveller.viewmodel

import android.content.Context
import com.example.imagetraveller.flickr.PhotosProvider
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness

class ImageTravellerViewModelTest {

    @get:Rule
    var mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    lateinit var photosProvider: PhotosProvider
    @Mock
    lateinit var context: Context

    lateinit var viewModel: ImageTravellerViewModel

    @Before
    fun setup() {
        viewModel = ImageTravellerViewModel(photosProvider, context)

    }

    @Test
    fun onResume() {
        val photoList: MutableList<String> = ArrayList()
        photoList.add("test")
        doReturn(photoList).`when`(photosProvider).getCurrentPhotos()

        assertEquals(0, viewModel.currentPhotos.size)
        assertFalse(viewModel.subscribed.get())

        viewModel.onResume()

        verify(photosProvider, times(1)).subscribePhotoStream(anyy(PhotosProvider.PhotosStream::class.java))
        assertEquals(1, viewModel.currentPhotos.size)
        assertEquals("test", viewModel.currentPhotos[0])
        assertTrue(viewModel.subscribed.get())
    }

    @Test
    fun onPause() {
        viewModel.onPause()
        assertFalse(viewModel.subscribed.get())
        verifyZeroInteractions(photosProvider)
        onResume()
        assertTrue(viewModel.subscribed.get())
        viewModel.onPause()
        verify(photosProvider).unsubscribePhotoStream()
        assertFalse(viewModel.subscribed.get())
    }

    @Test
    fun updatePhotoStream_notStarted_photoStreamStarted() {
        assertFalse(viewModel.subscribed.get())
        viewModel.updatePhotoStream()
        assertTrue(viewModel.subscribed.get())
        verify(photosProvider, times(1)).subscribePhotoStream(anyy(PhotosProvider.PhotosStream::class.java))
    }

    @Test
    fun updatePhotoStream_started_photoStreamStopped() {
        updatePhotoStream_notStarted_photoStreamStarted()
        viewModel.updatePhotoStream()
        assertFalse(viewModel.subscribed.get())
        verify(photosProvider).unsubscribePhotoStream()
    }

    @Test
    fun onCleared() {
        viewModel.onCleared()
        verify(photosProvider).stopPhotoService()
    }

    private fun <T> anyy(type : Class<T>): T {
        Mockito.any(type)
        return uninitialized()
    }

    private fun <T> uninitialized(): T = null as T
}