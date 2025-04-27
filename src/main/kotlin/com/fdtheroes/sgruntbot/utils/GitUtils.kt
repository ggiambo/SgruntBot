package com.fdtheroes.sgruntbot.utils

import com.fdtheroes.sgruntbot.models.NameValuePair
import com.fdtheroes.sgruntbot.persistence.NameValuePairRepository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.revwalk.RevCommit
import org.springframework.stereotype.Service
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.jvm.optionals.getOrElse

@Service
class GitUtils(private val nameValuePairRepository: NameValuePairRepository) {

    private val repository = Git.open(File("."))
    private val runningVersionHash by lazy {
        repository
            .log()
            .setMaxCount(1)
            .call()
            .first().id.name
    }

    fun getDeltaFromLatestDeployment(): Iterable<RevCommit> {
        return repository
            .log()
            .addRange(ObjectId.fromString(getLatestDeployedHash()), ObjectId.fromString(runningVersionHash))
            .call()
    }

    fun getLatestCommitMessages(nrOfCommits: Int): List<String> {
        return Git.open(File("."))
            .log()
            .setMaxCount(nrOfCommits)
            .call()
            .map {
                val commitTime = LocalDateTime.ofEpochSecond(it.commitTime.toLong(), 0, ZoneOffset.UTC)
                "${commitTime}: ${it.fullMessage}"
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