/* - Coeus web framework -------------------------
 *
 * Licensed under the Apache License, Version 2.0.
 *
 * Author: Spiros Tzavellas
 */
package com.tzavellas.coeus.http.multipart

import java.io.File
import java.util.{ Map => JMap }
import javax.servlet.{ ServletContext, ServletContextEvent }
import javax.servlet.http.HttpServletRequest
import scala.collection.mutable.ArrayBuffer
import org.apache.commons.fileupload.disk.{ DiskFileItem, DiskFileItemFactory }
import org.apache.commons.fileupload.servlet.{ FileCleanerCleanup, ServletFileUpload }
import com.tzavellas.coeus.util.internal.MultiMap

/**
 * An implementation of {@code MultipartRequestParser} that uses Apache Commons fileupload.
 * 
 * <p>This implementation stores the files smaller that {@code sizeThreshold} in memory and
 * the files larger than {@code sizeThreshold} in temporary files under the {@code repository}
 * directory. Any temporary files created for a request are automatically deleted at the end of
 * the request's execution.</p>
 * 
 * <p>If {@code repository} is {@code null} (the default) then the directory returned by
 * {@code System.getProperty("java.io.tmpdir")} is used to store any temporary files.</p>
 * 
 * <p>The default size threshold for in memory files is 10KB.</p>
 * 
 * <p>This class requires commons-fileupload and commons-io.</p>
 * 
 * @param sizeThreshold the size threshold after which the files are stored on disk
 * @param repository the directory under which the temporary files are created
 */
class CommonsMultipartRequestParser(sizeThreshold: Int = 10240, repository: File = null) extends MultipartRequestParser {
  
  private val upload = new ServletFileUpload(new DiskFileItemFactory(sizeThreshold, repository))  
  
  def init(context: ServletContext) {
    (new FileCleanerCleanup).contextInitialized(new ServletContextEvent(context))
  }
  
  def destroy(context: ServletContext) {
    (new FileCleanerCleanup).contextDestroyed(new ServletContextEvent(context))
  }
    
  def parse(request: HttpServletRequest): MultipartHttpServletRequest = {
    val items = upload.parseRequest(request).iterator.asInstanceOf[java.util.Iterator[DiskFileItem]]
    val files = new MultiMap[String, FormFile]
    val params = new MultiMap[String, String]
    
    while (items.hasNext) {
      val item = items.next
      if (item.isFormField) {
        params.add(item.getFieldName, item.getString)
      } else {
        files.add(item.getFieldName, new CommonsFormFile(item))
      }
    }
    new MultipartHttpServletRequest(request, paramsMap(params), paramsMap(files))
  }
  
  def cleanupFiles(request: MultipartHttpServletRequest) {
    val files = request.getFileMap.values.iterator
    while (files.hasNext) {
      files.next.foreach { _.delete() }
    }
  }
  
  private def paramsMap[T](mmap: MultiMap[String, T])(implicit m: Manifest[T]) = {
    val jmap = new java.util.HashMap[String, Array[T]](mmap.size)
    for ((key, values) <- mmap) {
      jmap.put(key, values.toArray(m))
    }
    jmap
  }
}