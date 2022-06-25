package kso.repo.search.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kso.repo.search.ui.detail.RepoDetailPage
import kso.repo.search.UserDetailPage
import kso.repo.search.viewModel.RepoDetailPageViewModel
import kso.repo.search.viewModel.UserDetailPageViewModel
import kso.repo.search.viewModel.HomePageViewModel
import kso.repo.search.ui.home.HomePage
import kso.repo.search.ui.searchBox.SearchBoxPage
import kso.repo.search.viewModel.SearchBoxViewModel

enum class NavPath(
    val route: String,
) {
    HomePage(route = "home_page"),
    SearchBoxPage(route = "search_box_page"),
    UserDetail(route = "user_detail"),
    RepoDetail(route = "repo_detail")
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun AppNavHost(navHostController: NavHostController, scaffoldState: ScaffoldState) {

    NavHost(
        navController = navHostController,
        startDestination =
        "${NavPath.HomePage.route}?login={login}"
    ) {

        composable(

            "${NavPath.HomePage.route}?login={login}", arguments = listOf(
                navArgument("login") {
                    type = NavType.StringType
                    defaultValue = "wayneeseguin"
                })

        ) {
            val homePageViewModel: HomePageViewModel = hiltViewModel()

            HomePage(
                navHostController = navHostController,
                homePageViewModel = homePageViewModel,
            )
        }



        composable(NavPath.SearchBoxPage.route) {
            val userSearchDemoViewModel = hiltViewModel<SearchBoxViewModel>()

            SearchBoxPage(
                navHostController = navHostController,
                userSearchViewModel = userSearchDemoViewModel
            )

        }

        composable(
            "${NavPath.UserDetail.route}?login={login}", arguments = listOf(
                navArgument("login") {
                    type = NavType.StringType
                })
        ) {

            val userDetailViewModel = hiltViewModel<UserDetailPageViewModel>()
            UserDetailPage(
                navHostController = navHostController,
                userDetailViewModel = userDetailViewModel
            )

        }

        composable(
            "${NavPath.RepoDetail.route}?login={login}&repoName={repoName}", arguments = listOf(
                navArgument("login") {
                    type = NavType.StringType
                },
                navArgument("repoName") {
                    type = NavType.StringType
                }
            )
        ) {

            val repoDetailViewModel = hiltViewModel<RepoDetailPageViewModel>()
            RepoDetailPage(
                navHostController = navHostController,
                repoDetailViewModel = repoDetailViewModel
            )

        }
    }

}