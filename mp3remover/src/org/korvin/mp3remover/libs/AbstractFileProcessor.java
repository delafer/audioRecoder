/*
 * @File: AbstractFileProcessor.java
 *
 * Copyright (c) 2005 Verband der Vereine Creditreform.
 * Hellersbergstr. 12, 41460 Neuss, Germany.
 * All rights reserved.
 *
 * @Author: Alexander Tawrowski
 *
 * @Version $Revision: #1 $Date: $
 *
 *
 */
package org.korvin.mp3remover.libs;

import java.io.File;
import java.io.IOException;

/**
 * The Class AbstractFileProcessor.
 * @author Alexander Tawrowski
 */
public abstract class AbstractFileProcessor {

   /** The technical logger to use. */
   private static final Logger logger = new Logger();

   /** The Constant ALL_FILES. */
   private final static String ALL_FILES = "*.*";

   /** The base path. */
   public File rootPath;

   /** The processed file count. */
   private int processedFiles;

   /** The processed dirs. */
   private int processedDirs;

   /** The wildcard. */
   private WildcardMask wildcard;

   /** The recurse subdirs. */
   private boolean recurseSubdirs;

   /** The flag stopped. */
   private boolean flagStopped;


   /** The stop on exception. */
   private boolean stopOnException = false;

   /**
    * Instantiates a new abstract file processor.
    *
    * @param basePath the base path
    */
   public AbstractFileProcessor(String basePath) {
      this(basePath, ALL_FILES);
   }

   /**
    * Instantiates a new abstract file processor.
    *
    * @param basePath the base path
    * @param wildcardMask the wildcard mask
    */
   public AbstractFileProcessor(String basePath, String wildcardMask) {

      this.rootPath = new File(basePath);

      if (!rootPath.exists()) {
         rootPath = new File(FileUtils.correctPath(basePath));
      }

      this.wildcard = new WildcardMask(wildcardMask);
      this.recurseSubdirs = true;
      this.flagStopped = false;
   }

   /**
    * Gets the files processed.
    *
    * @return Returns the processed.
    */
   public int getCountFilesProcessed() {
      return processedFiles;
   }

   /**
    * you could override this method in child classes
    * if this method returns false -> this file will be skipped / omited.
    *
    * @param entry the entry
    * @param fileData the file data
    * @return true, if successful
    */
   public boolean accept(File entry, FileInfo fileData) {
      return true;
   }


   /**
    * you could override this method in child classes
    * if this method returns true -> this file will be skipped / omited.
    *
    * @param entry the entry
    * @param fileData the file data
    * @return true, if successful
    */
   public boolean skip(File entry, FileInfo fileData) {
      return false;
   }

   /**
    * Stop processing.
    */
   public void stopProcessing() {
      this.flagStopped = true;
   }


   /**
    * Process a file.
    *
    * @param file the file
    * @param fileInfo the file info
    * @throws Exception the exception
    * @see stopProcessing()
    */
   public abstract void processFile(File file, FileInfo fileInfo) throws Exception;

   /**
    * Start.
    */
   public void start() {

      try {
         listContents(rootPath, true);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   /**
    * List contents.
    *
    * @param entry the entry
    * @param recurse the recurse
    * @throws IOException
    */
   private void listContents(File entry, boolean recurse) throws IOException {

      if (flagStopped || entry==null || !entry.exists()) return ;

      if (entry.isDirectory()) {

         if (!recurse) return ;

         this.processedDirs++;

         final String[] children = entry.list();

         if (null != children)
         for (String element : children) {
            listContents(new File(entry, element), this.recurseSubdirs);
         }
      } else {
         doFileIntern(entry);
      }
   }

   /**
    * process file internally.
    *
    * @param entry the entry
    * @throws IOException
    */
   private void doFileIntern(File entry) throws IOException {

      final String fullName = entry.getCanonicalPath();

      if (!wildcard.accept(fullName)) return ;

      FileInfo fileData = new FileInfo(entry);
      if (skip(entry, fileData) || !accept(entry, fileData)) return ;

      try {
         processFile(entry, fileData);
         processedFiles++;
      } catch (Exception e) {
         logger.error("Error processing file", e);
         if (stopOnException) stopProcessing();
      }


   }

   /**
    * The Class FileInfo.
    */
   public class FileInfo {

      /** The file. */
      private File file;

      /** The full name. */
      private String fullName;


      private final String getFullPath(File file) {
         if (file==null) return "";
         try {
            return file.getCanonicalPath();
         } catch (Exception e) {
         }
         return file.getAbsolutePath();
      }

      /**
       * Instantiates a new file info.
       *
       * @param file the file
       * @throws IOException
       */
      public FileInfo(File file) throws IOException {
         this.file = file;
         this.fullName = getFullPath(file);
      }

      /**
       * Gets the name minus the path from a full filename.
       *
       * @return the file name
       */
      public String getFileName() {
         return FileUtils.getFileName(fullName);
      }

      /**
       * Gets the base name, minus the full path and extension, from a full filename.
       *
       * @return the file name base
       */
      public String getFileNameBase() {
         return FileUtils.getBaseName(fullName);
      }

      /**
       * Gets the extension of a filename.
       * @return extension of a filename.
       */
      public String getExtension() {
         return FileUtils.getExtension(fullName);
      }


      /**
       * Gets the file.
       *
       * @return Returns the file.
       */
      public File getFile() {
         return file;
      }

      /**
       * Gets the file size.
       * @return the file size
       */
      public long getFileSize() {
         return file.length();
      }

      /**
       *  Gets the name with extension (if exists) <br>
       *  plus the path from a full filename.
       *
       * @return Returns the fullName with path
       */
      public String getNameWithPath() {
         return fullName;
      }

      /**
       * Does the work of getting the path.
       *
       * @return the path
       */
      public String getPath() {
         return file.getParentFile().getPath();
      }


      /**
       * Gets the relative path (without base Path).
       *
       * @return the path relative
       */
      public String getPathRelative() {
         String fullPath = getPath();


         String relativePath = fullPath.startsWith(rootPath.getPath())
               ? fullPath.substring(rootPath.getPath().length() + 1)
               : fullPath;

         return relativePath;
      }

   }


   /**
    * The Class WildcardMask.
    */
   private static class WildcardMask {

      /** The wildcard. */
      private String wildcard;

      /** The no filter. */
      private boolean noFilter;

      /**
       * Instantiates a new wildcard mask.
       *
       * @param mask the mask
       */
      public WildcardMask(String mask) {
         if (mask==null || mask.isEmpty()) mask = ALL_FILES;
         this.wildcard = mask;
         this.noFilter = ALL_FILES.equals(mask) || "*".equals(mask);
      }

      /**
       * Accept.
       *
       * @param name the name
       * @return true, if successful
       */
      public boolean accept(String name) {
         return noFilter ? true : FileUtils.wildcardMatch(name, wildcard);
      }

   }



   /**
    * Checks if is recurse sub directories.
    *
    * @return Returns the recurseSubdirs.
    */
   public boolean isRecurseSubDirectories() {

      return recurseSubdirs;
   }


   /**
    * Default value is true (on).
    *
    * @param recurseSubdirs The recurseSubdirs to set.
    */
   public void setRecurseSubDirectories(boolean recurseSubdirs) {

      this.recurseSubdirs = recurseSubdirs;
   }


   /**
    * Checks if is stop on exception.
    *
    * @return Returns the stopOnException.
    */
   public boolean isStopOnException() {

      return stopOnException;
   }


   /**
    * Sets the stop on exception.
    *
    * @param stopOnException The stopOnException to set.
    */
   public void setStopOnException(boolean stopOnException) {

      this.stopOnException = stopOnException;
   }


   /**
    * @return Returns the processedDirs.
    */
   public int getProcessedDirsCount() {

      return processedDirs;
   }

}
