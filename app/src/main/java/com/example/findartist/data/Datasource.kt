package com.example.findartist.data

import com.example.findartist.R
import com.example.findartist.model.ArtistItemList

class Datasource {

    fun loadArtistsList(): List<ArtistItemList> {
        return listOf<ArtistItemList>(
            ArtistItemList(R.drawable.profile1, R.string.name1, R.string.job1, R.string.rate2, R.string.description1),
            ArtistItemList(R.drawable.profile3, R.string.name2, R.string.job2, R.string.rate2, R.string.description1),
            ArtistItemList(R.drawable.profile2, R.string.name3, R.string.job2, R.string.rate3, R.string.description1),
            ArtistItemList(R.drawable.profile3, R.string.name4, R.string.job3, R.string.rate1, R.string.description1),
            ArtistItemList(R.drawable.profile1, R.string.name5, R.string.job1, R.string.rate2, R.string.description1),
            ArtistItemList(R.drawable.profile2, R.string.name6, R.string.job3, R.string.rate3, R.string.description1),
        )
    }
}