package br.com.devjmcn.shoplist.domain.usecases.allShopListViewUseCase

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AllShopListViewUseCaseImplTest {
    //GIVEN
    @Mock
    lateinit var allShopListViewUseCaseImpl: AllShopListViewUseCaseImpl

    @Test
    fun `should return false when name shop list is empty`() {
        //WHEN
        `when`(allShopListViewUseCaseImpl.shopNameIsValid(anyString()))
            .thenReturn(false)

        //THEN
        assertEquals(false, allShopListViewUseCaseImpl.shopNameIsValid(anyString()))
    }

    @Test
    fun `should return true when name shop list is not empty`(){
        val validName = "test"
        //WHEN
        `when`(allShopListViewUseCaseImpl.shopNameIsValid(validName)).thenReturn(true)

        //THEN
        assertEquals(true, allShopListViewUseCaseImpl.shopNameIsValid(validName))
    }
}