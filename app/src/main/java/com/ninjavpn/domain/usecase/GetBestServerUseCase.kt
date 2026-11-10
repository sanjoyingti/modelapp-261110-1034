package com.ninjavpn.domain.usecase

import com.ninjavpn.domain.model.Server
import com.ninjavpn.domain.repository.ServerRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetBestServerUseCase @Inject constructor(
    private val serverRepo: ServerRepository,
) {
    operator fun invoke(): Server? = serverRepo.getAllServers()
        .first()
        .minByOrNull { it.loadPercent } // Lowest load = best
}