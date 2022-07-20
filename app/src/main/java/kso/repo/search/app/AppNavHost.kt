package kso.repo.search.app

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import kso.repo.search.dataSource.preference.PreferenceProvider
import kso.repo.search.ui.detail.RepoDetailPage
import kso.repo.search.ui.detail.UserDetailPage
import kso.repo.search.ui.home.HomePage
import kso.repo.search.ui.keywordSearch.KeywordSearchPage
import kso.repo.search.ui.repoList.RepoListPage
import kso.repo.search.viewModel.*

enum class NavPath(
    val route: String,
) {
    HomePage(route = "home_page"),
    RepoListPage(route = "repo_list_page"),
    KeywordSearchPage(route = "search_box_page"),
    UserDetailPage(route = "user_detail"),
    RepoDetailPage(route = "repo_detail")
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun AppNavHost(navHostController: NavHostController) {
    val preferenceProvider = PreferenceProvider(LocalContext.current)
    NavHost(
        navController = navHostController,
        startDestination = when(preferenceProvider.getSearchKeyword().isEmpty()){
            true ->
                NavPath.HomePage.route
            else -> {
                "${NavPath.RepoListPage.route}?repoName={repo_name}"
            }
        }

    ) {

        composable(
            NavPath.HomePage.route
        ) {
            val homePageViewModel: HomePageViewModel = hiltViewModel()

            HomePage(
                navHostController = navHostController,
                homePageViewModel = homePageViewModel,
            )
        }

        composable(

            "${NavPath.RepoListPage.route}?repoName={repo_name}", arguments = listOf(
                navArgument("repo_name") {
                    type = NavType.StringType
                    defaultValue = preferenceProvider.getSearchKeyword()
                })

        ) {
            val repoListPageViewModel: RepoListPageViewModel = hiltViewModel()

            RepoListPage(
                navHostController = navHostController,
                repoListPageViewModel = repoListPageViewModel,
            )
        }

        composable(NavPath.KeywordSearchPage.route) {
            val userSearchDemoViewModel = hiltViewModel<KeywordSearchPageViewModel>()

            KeywordSearchPage(
                navHostController = navHostController,
                keywordSearchPageViewModel = userSearchDemoViewModel
            )

        }

        /*composable(
            "${NavPath.KeywordSearchPage.route}?repo={repo}",
            arguments = listOf(
                navArgument("repo") {
                    type = NavType.StringType
                },

                )
        ) {

            val userSearchDemoViewModel = hiltViewModel<KeywordSearchPageViewModel>()

            KeywordSearchPage(
                navHostController = navHostController,
                userSearchViewModel = userSearchDemoViewModel
            )

        }*/


        composable(
            "${NavPath.UserDetailPage.route}?user={user}",
            arguments = listOf(
                navArgument("user") {
                    type = NavType.StringType
                },

                )
        ) {

            val userDetailViewModel = hiltViewModel<UserDetailPageViewModel>()
            UserDetailPage(
                navHostController = navHostController,
                userDetailViewModel = userDetailViewModel
            )

        }

        composable(
            "${NavPath.RepoDetailPage.route}?repo={repo}",
            arguments = listOf(
                navArgument("repo") {
                    type = NavType.StringType
                },

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

