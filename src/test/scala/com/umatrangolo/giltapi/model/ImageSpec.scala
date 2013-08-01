package com.umatrangolo.giltapi.model

import java.net.URL
import org.scalatest.WordSpec

class ImageSpec extends WordSpec {

  private val TestImage = Image(new URL("http://www.gilt.com"), 11, 10)

  "An Image" when {
    "contructed" should {
      "reject a width < 0" in { intercept[IllegalArgumentException] { TestImage.copy(width = -1) } }
      "reject a height < 0" in { intercept[IllegalArgumentException] { TestImage.copy(height = -1) } }
      "reject a null url" in { intercept[IllegalArgumentException] { TestImage.copy(url = null) } }
    }
    "at runtime" should {
      "should have a sound equals/hashcode" in {
        assert(TestImage === TestImage.copy())
        assert(TestImage.## === TestImage.copy().##)
      }
    }
  }

  private val TestImageKey = ImageKey(11, 10)

  "An ImageKey" when {
    "constructed" should {
      "reject a width < 0" in { intercept[IllegalArgumentException] { TestImageKey.copy(width = -1) } }
      "reject a height < 0" in { intercept[IllegalArgumentException] { TestImageKey.copy(height = -1) } }
    }
    "at runtime" should {
      "should have a sound equals/hashcode" in {
        assert(TestImageKey === TestImageKey.copy())
        assert(TestImageKey.## === TestImageKey.copy().##)
      }
    }
  }
}
