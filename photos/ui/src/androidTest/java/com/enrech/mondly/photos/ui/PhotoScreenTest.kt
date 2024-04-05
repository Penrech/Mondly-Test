package com.enrech.mondly.photos.ui

import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.hilt.navigation.compose.hiltViewModel
import com.enrech.mondly.core.domain.exception.NoInternetException
import com.enrech.mondly.photos.ui.screen.PhotoScreen
import com.enrech.mondly.photos.ui.screen.PhotosViewModel
import com.enrech.mondly.testing.HiltTestComponentActivity
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//Adding some UI tests, this could be widely expanded based on business requirements
@HiltAndroidTest
class PhotoScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestComponentActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun alwaysDisplayLoading_onInit() {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            PhotoScreen(navigator = EmptyDestinationsNavigator)
        }

        val tag = rule.activity.getString(R.string.loading_element_tag)
        val listTag = rule.activity.getString(R.string.valid_list_tag)

        //Assert
        rule.onNodeWithTag(listTag).isDisplayed()
        rule.onAllNodesWithTag(tag).onFirst().isDisplayed()
    }

    @Test
    fun displayNetworkError_ifInit_and_networkFailure() {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            val viewModel: PhotosViewModel = hiltViewModel()
            viewModel.mockScreenState {
                copy(
                    isInitializing = false,
                    error = NoInternetException()
                )
            }
            PhotoScreen(EmptyDestinationsNavigator, viewModel)
        }

        val errorScreenTag = rule.activity.getString(R.string.error_screen_tag)
        val networkErrorIconTag = rule.activity.getString(R.string.network_error_icon_tag)
        val generalErrorIconTag = rule.activity.getString(R.string.general_error_icon_tag)

        //Assert
        rule.onNodeWithTag(errorScreenTag).isDisplayed()
        rule.onNodeWithTag(networkErrorIconTag).isDisplayed()
        rule.onNodeWithTag(generalErrorIconTag).isNotDisplayed()
    }

    @Test
    fun displayGeneralError_ifInit_and_no_networkFailure() {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            val viewModel: PhotosViewModel = hiltViewModel()
            viewModel.mockScreenState {
                copy(
                    isInitializing = false,
                    error = NoInternetException()
                )
            }
            PhotoScreen(EmptyDestinationsNavigator, viewModel)
        }

        val errorScreenTag = rule.activity.getString(R.string.error_screen_tag)
        val networkErrorIconTag = rule.activity.getString(R.string.network_error_icon_tag)
        val generalErrorIconTag = rule.activity.getString(R.string.general_error_icon_tag)

        //Assert
        rule.onNodeWithTag(errorScreenTag).isDisplayed()
        rule.onNodeWithTag(networkErrorIconTag).isNotDisplayed()
        rule.onNodeWithTag(generalErrorIconTag).isDisplayed()
    }

    @Test
    fun displayEmptyScreen_ifInit_and_noPhotos() {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            val viewModel: PhotosViewModel = hiltViewModel()
            viewModel.mockScreenState {
                copy(
                    isInitializing = false,
                )
            }
            PhotoScreen(EmptyDestinationsNavigator, viewModel)
        }

        val emptyScreenTag = rule.activity.getString(R.string.empty_screen_tag)

        //Assert
        rule.onNodeWithTag(emptyScreenTag).isDisplayed()
    }
}