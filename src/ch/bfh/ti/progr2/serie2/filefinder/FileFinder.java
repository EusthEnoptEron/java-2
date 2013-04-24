package ch.bfh.ti.progr2.serie2.filefinder;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import sun.misc.Regexp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A class for searching a directory, based on a RegEx pattern.
 */
public class FileFinder {
	private File root;

	private int maxDepth = -1;
	private PatternConstraint constraint = PatternConstraint.FILENAME;

	enum PatternConstraint { PATH, FILENAME }


	/**
	 * Represents the result of a search.
	 */
	static class SearchResult {
		ArrayList<File> files = new ArrayList<>();
		private SearchResult() {}

		/**
		 * Returns the total size of all files in bytes.
		 * @return the total size of all files in bytes
		 */
		public long getSize() {
			long size = 0;
			for(File file: files) {
				size += file.length();
			}

			return size;
		}

		/**
		 * Returns how many files were found.
		 * @return how many files were found
		 */
		public int getCount() {
			return files.size();
		}

		/**
		 * Returns an array containing all the files found.
		 * @return an array containing all the files found
		 */
		public File[] getFiles() {
			return files.toArray(new File[files.size()]);
		}

		private void addFile(File file) {
			files.add(file);
		}

	}

	/**
	 * Creates a new FileFinder bound to a directory.
	 * @param root directory to search
	 * @throws FileNotFoundException
	 */
	public FileFinder(String root) throws FileNotFoundException {
		this(new File(root));
	}

	/**
	 * Creates a new FileFinder bound to a directory.
	 * @param root directory to search
	 * @throws FileNotFoundException
	 */
	public FileFinder(File root) throws FileNotFoundException {
		changeDirectory(root);
	}

	/**
	 * Change CWD (of this class) to dir
	 * @param dir directory to change to
	 * @throws FileNotFoundException if the file was not found
	 */
	public void changeDirectory(File dir) throws FileNotFoundException {
		if(!root.exists()) {
			throw new FileNotFoundException("The root directory you specified does not exist");
		}
		root = dir;
	}

	/**
	 * Returns the directory the file searcher is currently attached to
	 * @return
	 */
	public File getCurrentDirectory() {
		return root;
	}

	/**
	 * Gets the maximal depth. (the number of folders to dive into before giving up)
	 * @return
	 */
	public int getMaxDepth() {
		return maxDepth;
	}

	/**
	 * Sets the maximal depth for this search. < 0: no limit
	 * @param maxDepth
	 */
	public void setMaxDepth(int maxDepth) {
		this.maxDepth = maxDepth;
	}

	/**
	 * Gets the search pattern constraint (whether to apply it to the filename or the pathname)
	 * @return
	 */
	public PatternConstraint getConstraint() {
		return constraint;
	}

	/**
	 * Sets the search pattern constraint (whether to apply it to the filename or the pathname)
	 * @param constraint
	 */
	public void setConstraint(PatternConstraint constraint) {
		this.constraint = constraint;
	}


	/**
	 * Starts a search for a pattern.
	 * @param pattern pattern to search for
	 * @return the result of the search
	 */
	public SearchResult search(String pattern) {
		SearchResult result = new SearchResult();

		// Conduct search operation
		search(root, pattern, result, 0);

		return result;
	}

	// Search node (a directory) for a pattern
	private void search(File node, String pattern, SearchResult result, int level) {
		// Check if we're too deep
		if(maxDepth >= 0 && level > maxDepth) return;

		// Try to get a list of file and loop through them
		File[] files = node.listFiles();
		if(files != null) {
			for(File file: files) {
				if(file.isFile()) {
					// FILE: Check if the file matches the pattern
					// -> Determine if we match the filename or the absolute path
					String str;
					if(constraint.equals(PatternConstraint.FILENAME))
						str = file.getName();
					else
						str = file.getAbsolutePath();

					// Match
					if(str.matches(pattern)) {
						result.addFile(file);
					}
				} else {
					// DIRECTORY: Search the directory
					search(file, pattern, result, level + 1);
				}
			}
		}
	}

}
