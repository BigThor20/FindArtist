package com.example.findartist.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ArtistItemList(@DrawableRes val profilePhotoResourceId: Int,
                          @StringRes val nameResourceId: Int,
                          @StringRes val jobResourceId: Int,
                          @StringRes val rateResourceId: Int,
                          @StringRes val descriptionResourceId: Int,
) {
}