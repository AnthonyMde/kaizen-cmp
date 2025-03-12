package com.makapp.kaizen.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

private const val DEFAULT_TRANSITION_DURATION = 300

fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultEnterTransition(): EnterTransition =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Start,
        tween(DEFAULT_TRANSITION_DURATION)
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultPopEnterTransition(): EnterTransition =
    slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.End,
        tween(DEFAULT_TRANSITION_DURATION)
    )

fun AnimatedContentTransitionScope<NavBackStackEntry>.defaultPopExitTransition(): ExitTransition =
    slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.End,
        tween(DEFAULT_TRANSITION_DURATION)
    )
