package com.jayway.eclipse.adt.xml


import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

import java.io.FileNotFoundException

/**
 * Testclass for AndroidManifest.
 *
 */
@RunWith(classOf[JUnitRunner])
class AndroidManifestTest extends Spec with ShouldMatchers {
  
  describe("A AndroidManifest") {
    val manifest = new AndroidManifest("./testresources/AndroidManifest.xml")
    it("should create the xml for a new activity") {
        assert(manifest.createXML("MyActivity", Nil, Nil) != null)
        assert(manifest.createXML("MyActivity", Nil, Nil).toString().contains("MyActivity"))
    }
    
    it("should add intent filters to the xml for a new activity") {
    	val categories = "android.intent.category.DEFAULT" :: "android.intent.category.BROWSABLE" :: Nil
        val actions = "com.example.project.SHOW_CURRENT" :: Nil
    	assert(manifest.createXML("MyActivity", actions, categories) != null)
        assert(manifest.createXML("MyActivity", actions, categories).toString().contains("category android:name=\"android.intent.category.DEFAULT\""))
        assert(manifest.createXML("MyActivity", actions, categories).toString().contains("category android:name=\"android.intent.category.BROWSABLE\""))
        assert(manifest.createXML("MyActivity", actions, categories).toString().contains("action android:name=\"com.example.project.SHOW_CURRENT\""))
    }
 
	it("should load the xml from the given file") {
        assert(manifest.loadXML() != null)
        assert(manifest.loadXML().toString().contains("HelloActivity"))
    }
	
	it("should throw FileNotFoundException when refering to a non existing file") {
		val notExisting = new AndroidManifest("NotExisting.xml")
		evaluating { notExisting.loadXML() } should produce [FileNotFoundException]
	}
	
	it("should return the application node") {
        assert(manifest.getAppNode() != null)
        assert(manifest.getAppNode().toString().startsWith("<application"))
     }
	
	it("should return all activity nodes") {
        assert(manifest.getActivityNodes() != null)
        assert(manifest.getActivityNodes().length === 1)
     }
	
	it("should add the xml for a new activity to the manifest file") {
		val changedXml = manifest.addActivity("MyActivity", Nil, Nil)
		assert(changedXml != null)
        assert(changedXml.toString().contains("MyActivity"))
     }
	
  }
}