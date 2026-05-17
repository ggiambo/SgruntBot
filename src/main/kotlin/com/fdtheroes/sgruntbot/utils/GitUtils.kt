package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.models.NameValuePair
import com.fdtheroes.sgruntbot.persistence.NameValuePairRepository
import org.kohsuke.github.GHCommit
import org.kohsuke.github.GitHubBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.jvm.optionals.getOrElse

interface IGitUtils {
    fun getDeltaFromLatestDeployment(): Iterable<GHCommit>
    fun getLatestCommitMessages(nrOfCommits: Int): List<String>
    fun updateDeployedHash()
}

@Service
@Profile("docker")
class GitUtilsDocker : IGitUtils {
    override fun getDeltaFromLatestDeployment() = emptyList<GHCommit>()
    override fun getLatestCommitMessages(nrOfCommits: Int) = emptyList<String>()
    override fun updateDeployedHash() = Unit
}

@Service
@Profile("!docker")
class GitUtils(
    @param:Value($$"${GH_TOKEN}") private val gitHubToken: String,
    private val nameValuePairRepository: NameValuePairRepository,
) : IGitUtils {

    private val dateFormat = DateTimeFormatter.ofPattern("YYYY.MM.dd HH:mm")

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

    override fun getDeltaFromLatestDeployment(): Iterable<GHCommit> {
        val latestDeployedHash = getLatestDeployedHash()
        return repository.listCommits().takeWhile { it.shA1 != latestDeployedHash }
    }

    override fun getLatestCommitMessages(nrOfCommits: Int): List<String> {
        return repository
            .listCommits()
            .take(nrOfCommits)
            .map {
                val commitTime = LocalDateTime.ofInstant(it.commitDate.toInstant(), ZoneId.systemDefault())
                val message = it.commitShortInfo.message.lines().first()
                "${dateFormat.format(commitTime)}: $message"
            }
    }

    private fun getLatestDeployedHash(): String {
        val latestDeployedVersionHash = nameValuePairRepository
            .findById(NameValuePair.NameValuePairName.GIT_HASH)
        return latestDeployedVersionHash.getOrElse { initDeployedVersion() }.value
    }

    override fun updateDeployedHash() {
        nameValuePairRepository.save(NameValuePair(NameValuePair.NameValuePairName.GIT_HASH, runningVersionHash))
    }

    private fun initDeployedVersion(): NameValuePair {
        return nameValuePairRepository.save(NameValuePair(NameValuePair.NameValuePairName.GIT_HASH, runningVersionHash))
    }

}