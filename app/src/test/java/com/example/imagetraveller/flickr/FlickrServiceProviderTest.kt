package com.example.imagetraveller.flickr

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.photos.Photo
import com.flickr4java.flickr.photos.PhotoList
import com.flickr4java.flickr.photos.PhotosInterface
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.eq
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnit
import org.mockito.quality.Strictness

class FlickrServiceProviderTest {

    @get:Rule
    var mockitoRule = MockitoJUnit.rule().strictness(Strictness.STRICT_STUBS)

    @Mock
    lateinit var flickr: Flickr
    @Mock
    private lateinit var photosInterface: PhotosInterface

    private lateinit var provider: FlickrProvider

    val lat = "lat"
    val long = "long"

    @Before
    fun setup() {
        doReturn(photosInterface).`when`(flickr).photosInterface
        provider = FlickrProvider(flickr)
    }

    @Test
    fun getImage_noResults_emptyResult() {
        doReturn(null).`when`(photosInterface).search(any(), eq(5), eq(1))
        provider.getImage(lat, long).test().assertValueCount(0).assertComplete()
    }

    @Test
    fun getImage_oneResult_SuccesfulResult() {
        val photoList: PhotoList<Photo> = PhotoList()
        val photo = mock(Photo::class.java)

        photoList.add(photo)
        doReturn(photoList).`when`(photosInterface).search(any(), eq(5), eq(1))

        provider.getImage(lat, long).test().assertValueCount(1).assertValue(photo)
    }
}