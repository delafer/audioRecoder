package org.delafer.recoder.test;

import java.io.File;
import java.io.IOException;

import org.delafer.recoder.model.AudioFile;
import org.delafer.recoder.model.FilePersistent;
import org.delafer.recoder.model.ProgressModel;
import org.delafer.recoder.model.ProgressModel.TaskType;

public class CommonTest {

	public CommonTest() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		AudioFile af = new AudioFile("F:\\testing2\\vvDjekobs\\a1.wma");
		System.out.println(af.getBitRate());
//		TaskType[] tt = ProgressModel.TaskType.values();
//		for (TaskType taskType : tt) {
//			System.out.println(taskType.ordinal());
//		}

//		VTStack<String> v = new VTStack<String>(6);
//		System.out.println(v);
//		v.add("1");
//		System.out.println(v);
//		v.add("2");
//		v.add("3");
//		System.out.println(v);
//		v.add("4");
//		v.add("5");
//		v.add("6");
//		System.out.println(v);
//		v.add("7");
//		System.out.println(v);
//		v.add("8");
//		v.add("9");
//		v.add("A");
//		v.add("B");
//		System.out.println(v);

		System.exit(0);

		String a = args[0];
		System.out.println(a);
		try {
			FilePersistent f = new FilePersistent(new File(a));
			System.out.println("baseName: "+f.getBaseName());
			System.out.println("baseNameWithPath: "+f.getBaseNameWithPath());
			System.out.println("extension: "+f.getExtension());
			System.out.println("fullaName: "+f.getFullName());
			System.out.println("fullNameWithPath: "+f.getFullNameWithPath());
			System.out.println("Path: "+f.getPath());

//			System.out.println(new FileFresh("wav"));
//			System.out.println(new FileFresh("mp3"));

			} catch (IOException e) {
			e.printStackTrace();
		}


	}

}
