package com.jayway.eclipse.adt.xml

import scala.xml.Node
import scala.xml.NodeSeq
import scala.xml.Utility
import scala.xml.XML

 /**
  * AndroidManifest.
  * Represents a android manifest file, and allows xml manipulation.
  */
class AndroidManifest(fileName : String) {
	
	var current : Node = null;

  /**
   * crate the XML for the new activity
   * @param activityName name of the new activity prefixed with "."
   * @param intentActions List of intent actions, may be empty
   * @param intentCategories List of intent categories, may be empty
   */
  def createXML(activityName: String, intentActions: List[String], intentCategories: List[String]) : Node = {
	val xml =
      <activity android:name={activityName}>
        <intent-filter>
	      {for(action <- intentActions) yield {<action android:name={action} />}}
	      {for(category <- intentCategories) yield {<category android:name={category} />}}
        </intent-filter>
      </activity>
	xml
  }
  
  /**
   * crate the XML for the new activity. convenience method.
   * @param activityName name of the new activity prefixed with "."
   * @param intentActions Array of intent actions, may be empty
   * @param intentCategories Array of intent categories, may be empty
   */
  def createXML(activityName: String, intentActions: Array[String], intentCategories: Array[String]) : Node = {
	createXML(activityName, List.fromArray(intentActions), List.fromArray(intentCategories))
  }
  
  /**
   * load the AndroidManifest.xml form the given fileName
   * @return manifest
   */
  def loadXML() : Node = {
    current = XML.loadFile(fileName)
    current
  }
  
  /**
   * get all activity nodes from the manifest
   * @return activity nodes
   */ 
  def getActivityNodes() : NodeSeq = {
    current \\ "activity"
  }
   
  /**
   * get the application node from the manifest
   * @return application node
   */
  def getAppNode() : NodeSeq = {
    current \\ "application"
  }
  
  /**
   * insert the given activity in the xml
   * @param activity new activity
   * @return new node with inserted activity
   */
  def insertActivity(activity : Node) : Node = {
    val xml = loadXML()
    val sb = new StringBuilder(xml.toString) 
    val index = sb.toString.lastIndexOf("</application>")
    sb.insert(index, activity.toString)
    current = XML.loadString(sb.toString)
    current
  }
  
 /**
  * Add an activity to the manifest.
  * @param activityName name of the activity
  * @param intentActions intent actions
  * @param intentCategories intent categories
  */
  def addActivity(activityName: String, intentActions: List[String], intentCategories: List[String]) : Node = {
    val newActivityNode = createXML(activityName, intentActions, intentCategories)
    insertActivity(newActivityNode)
  }
  
  /**
  * Add an activity to the manifest.
  * @param activityName name of the activity
  * @param intentActions intent actions
  * @param intentCategories intent categories
  */
  def addActivity(activityName: String, intentActions: Array[String], intentCategories: Array[String]) : Node = {
    addActivity(activityName, List.fromArray(intentActions), List.fromArray(intentCategories))
  }
  
 /**
  * Saves the changes back to manifest file.
  */
  def save() {
	  XML.save(fileName, current, "utf-8")
  }
  
  
  
}
