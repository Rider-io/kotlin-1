// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.util.indexing.diagnostic.dto

data class JsonProjectIndexingHistory(
  val projectName: String,
  val numberOfFileProviders: Int,
  val totalNumberOfFiles: Int,
  val totalNumberOfTooLargeFiles: PositiveInt,
  val totalNumberOfFailedToLoadFiles: PositiveInt,
  val totalNumberOfFailedToIndexFiles: PositiveInt,
  val times: JsonProjectIndexingHistoryTimes,
  val totalStatsPerFileType: List<JsonStatsPerFileType>,
  val totalStatsPerIndexer: List<JsonStatsPerIndexer>,
  val fileProviderStatistics: List<JsonFileProviderIndexStatistics>
) {
  data class JsonStatsPerFileType(
    val fileType: String,
    val partOfTotalIndexingTime: JsonPercentages,
    val partOfTotalContentLoadingTime: JsonPercentages,
    val totalNumberOfFiles: Int,
    val totalFilesSize: JsonFileSize,
    val indexingSpeed: JsonProcessingSpeed,
    val biggestContributors: List<JsonBiggestFileTypeContributor>
  ) {
    data class JsonBiggestFileTypeContributor(
      val providerName: String,
      val numberOfFiles: Int,
      val totalFilesSize: JsonFileSize,
      val partOfTotalIndexingTimeOfThisFileType: JsonPercentages
    )
  }

  data class JsonStatsPerIndexer(
    val indexId: String,
    val partOfTotalIndexingTime: JsonPercentages,
    val totalNumberOfFiles: Int,
    val totalFilesSize: JsonFileSize,
    val indexingSpeed: JsonProcessingSpeed
  )
}