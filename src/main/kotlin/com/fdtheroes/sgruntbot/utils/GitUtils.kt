package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.models.NameValuePair
import com.fdtheroes.sgruntbot.persistence.NameValuePairRepository
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHubBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.jvm.optionals.getOrElse

@Service
class GitUtils(
    @Value("\${GH_TOKEN}") private val gitHubToken: String,
    private val nameValuePairRepository: NameValuePairRepository,
) {

    private val repository = GitHubBuilder()
        .withOAuthToken(gitHubToken)
        .build()
        .getRepository("ggiambo/SgruntBot")

    private val runningVersionHash by lazy {
        repository
            .listCommits()
            .iterator()
            .next()
            .shA1
    }

    fun getDeltaFromLatestDeployment(): Iterable<GHCommit> {
        val latestDeployedHash = getLatestDeployedHash()
        return repository.listCommits().takeWhile { it.shA1 != latestDeployedHash }
    }

    fun getLatestCommitMessages(nrOfCommits: Int): List<String> {
        return repository
            .listCommits()
            .take(nrOfCommits)
            .map {
                val commitTime = LocalDateTime.ofInstant(it.commitDate.toInstant(), ZoneId.systemDefault())
                "${commitTime}: ${it.commitShortInfo.message}"
            }
    }

    private fun getLatestDeployedHash(): String {
        val latestDeployedVersionHash = nameValuePairRepository
            .findById(NameValuePair.NameValuePairName.GIT_HASH)
        return latestDeployedVersionHash.getOrElse { initDeployedVersion() }.value
    }

    fun updateDeployedHash() {
        nameValuePairRepository.save(NameValuePair(NameValuePair.NameValuePairName.GIT_HASH, runningVersionHash))
    }

    private fun initDeployedVersion(): NameValuePair {
        return nameValuePairRepository.save(NameValuePair(NameValuePair.NameValuePairName.GIT_HASH, runningVersionHash))
    }

}