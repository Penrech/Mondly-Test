package com.enrech.mondly.main.ui.navigation

import com.enrech.mondly.photos.ui.PhotosNavGraph
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.Route

object RootNavGraphs: NavGraphSpec {
    override val route: String = "root"

    override val destinationsByRoute: Map<String, DestinationSpec<*>> = emptyMap()

    override val startRoute: Route = PhotosNavGraph
}