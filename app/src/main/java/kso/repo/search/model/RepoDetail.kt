package kso.repo.search.model

import androidx.room.Embedded

data class RepoDetail(
    @Embedded val repo: Repo,
    @Embedded val owner: Owner
)