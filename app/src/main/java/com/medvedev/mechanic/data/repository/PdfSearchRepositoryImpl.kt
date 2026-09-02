package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.docs.search.PdfSearchIndexDataSource
import com.medvedev.mechanic.data.error.toDomain
import com.medvedev.mechanic.domain.document.PdfSearchIndex
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.repository.PdfSearchRepository
import com.medvedev.mechanic.domain.result.Result
import javax.inject.Inject

class PdfSearchRepositoryImpl @Inject constructor(
    private val dataSource: PdfSearchIndexDataSource,
) : PdfSearchRepository {
    override suspend fun loadIndex(path: String): Result<PdfSearchIndex, DomainError> =
        when (val result = dataSource.load(path)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
}
