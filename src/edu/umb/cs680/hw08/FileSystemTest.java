package edu.umb.cs680.hw08;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class FileSystemTest {
	// setting up object
	private static Directory root;
	private static Directory home;
	private static Directory applications;
	private static Directory code;
	private static File a;
	private static File b;
	private static File c;
	private static File d;
	private static File e;
	private static File f;
	private static Link x;
	private static Link y;
	private static Link z;

	@BeforeAll
	public static void setUp() {
		// TODO Auto-generated method stub
		root = new Directory(null, "root", 0, LocalDateTime.of(LocalDate.of(2019, 8, 15), LocalTime.of(12, 00, 00)));
		applications = new Directory(root, "applications", 0,
				LocalDateTime.of(LocalDate.of(2019, 9, 15), LocalTime.of(13, 00, 40)));
		home = new Directory(root, "home", 0, LocalDateTime.of(LocalDate.of(2019, 10, 15), LocalTime.of(20, 30, 00)));
		code = new Directory(home, "code", 0, LocalDateTime.of(LocalDate.of(2019, 11, 15), LocalTime.of(20, 31, 00)));

		a = new File(applications, "a", 2048, LocalDateTime.of(LocalDate.of(2019, 9, 15), LocalTime.of(13, 01, 00)));
		b = new File(applications, "b", 1024, LocalDateTime.of(LocalDate.of(2019, 9, 15), LocalTime.of(13, 10, 00)));

		c = new File(home, "c", 2048, LocalDateTime.of(LocalDate.of(2019, 10, 15), LocalTime.of(21, 32, 45)));
		d = new File(home, "d", 4096, LocalDateTime.of(LocalDate.of(2019, 9, 15), LocalTime.of(21, 45, 18)));

		e = new File(code, "e", 1024, LocalDateTime.of(LocalDate.of(2019, 11, 15), LocalTime.of(20, 40, 59)));
		f = new File(code, "f", 2048, LocalDateTime.of(LocalDate.of(2019, 11, 15), LocalTime.of(20, 45, 51)));
		// symbolic Link
		x = new Link(home, "x", 0, LocalDateTime.of(LocalDate.of(2019, 11, 17), LocalTime.of(20, 46, 00)),
				applications);
		y = new Link(code, "y", 0, LocalDateTime.of(LocalDate.of(2019, 9, 16), LocalTime.of(20, 39, 00)), b);

		// adding root directory, as same as C drive or D drive in Window Explorer file
		// system
		// we must add application and home directory to rootDirs.
		FileSystem.getFileSystem().appendRoot(root);
		// FileSystem.getFileSystem().appendRoot(home);

		applications.appendChild(a);
		applications.appendChild(b);

		root.appendChild(applications);
		root.appendChild(home);

		home.appendChild(code);
		home.appendChild(c);
		home.appendChild(d);
		home.appendChild(x);

		code.appendChild(e);
		code.appendChild(f);
		code.appendChild(y);
	}

	// convert FSElement data fields into array String
	private String[] FSElementToString(FSElement element) {
		String[] result = { element.getName(), element.getCreationTime(), Integer.toString(element.getSize()),
				element.getParent().getName() };
		return result;
	}

	// convert root String, root has null parent
	private String[] rootToString(Directory element) {
		String[] result = { element.getName(), element.getCreationTime(), Integer.toString(element.getSize()) };
		return result;
	}

	@Test
	public void check_Symbolic_Link_Points_To_Empty_File_Or_Directory() {
		try {
			Link z = new Link(code, "z", 0, LocalDateTime.of(LocalDate.of(2019, 9, 16), LocalTime.of(20, 39, 00)),
					null);
		} catch (NullPointerException ex) {
			assertEquals(ex.getMessage(), "File or Directory do not exist");
		}
	}

	@Test
	public void checkInstanceOfDirectory() {
		assertTrue(root instanceof Directory);
		assertTrue(code instanceof Directory);
		assertTrue(home instanceof Directory);
		assertTrue(applications instanceof Directory);
	}

	@Test
	public void checkInstanceOfFSElement() {
		// Directories
		assertTrue(root instanceof FSElement);
		assertTrue(code instanceof FSElement);
		assertTrue(home instanceof FSElement);
		assertTrue(applications instanceof FSElement);

		// Files
		assertTrue(a instanceof FSElement);
		assertTrue(b instanceof FSElement);
		assertTrue(c instanceof FSElement);
		assertTrue(d instanceof FSElement);
		assertTrue(e instanceof FSElement);
		assertTrue(f instanceof FSElement);

	}

	@Test
	public void checkInstanceOfFile() {
		assertTrue(a instanceof File);
		assertTrue(b instanceof File);
		assertTrue(c instanceof File);
		assertTrue(d instanceof File);
		assertTrue(e instanceof File);
		assertTrue(f instanceof File);
	}

	@Test
	public void verifygetterMethodsOfHomeDirectory() {
		String[] expected = { "home", "2019-10-15T20:30", "0", "root" };
		assertArrayEquals(expected, FSElementToString(home));
	}

	@Test
	public void verifygetterMethodsOfRootDirectory() {
		String[] expected = { "root", "2019-08-15T12:00", "0" };
		assertArrayEquals(expected, rootToString(root));
	}

	@Test
	public void verifygetterMethodsOfCodeDirectory() {
		String[] expected = { "code", "2019-11-15T20:31", "0", "home" };
		assertArrayEquals(expected, FSElementToString(code));
	}

	@Test
	public void verify_File_e_and_f_are_a_children_of_code_directory() {
		assertEquals(f.getParent(), code);
		assertEquals(e.getParent(), code);
	}

	@Test
	public void verify_File_a_and_b_are_a_children_of_applications_directory() {
		assertEquals(a.getParent(), applications);
		assertEquals(b.getParent(), applications);
	}

	@Test
	public void verify_File_c_and_d_are_a_children_of_home_directory() {
		assertEquals(c.getParent(), home);
		assertEquals(d.getParent(), home);
	}

	@Test
	public void verify_directory_home_and_applications_are_a_children_of_root_directory() {
		assertEquals(home.getParent(), root);
		assertEquals(applications.getParent(), root);

	}

	// checking the singleton of class FileSystem
	@Test
	public void checking_the_singleton_of_FileSystem_class() {
		ArrayList<Directory> expected = new ArrayList<>();
		expected.add(root);

		FileSystem a = FileSystem.getFileSystem();

		assertEquals(expected, FileSystem.getFileSystem().getRootDirs());
		assertEquals(a.hashCode(), FileSystem.getFileSystem().hashCode());
	}

	@Test
	public void verify_children_of_applications_directory() {
		int num_of_children_expected = 2;
		assertEquals(num_of_children_expected, applications.countChildren());

		assertEquals(applications.getChildren().get(0).hashCode(), a.hashCode());
		assertEquals(applications.getChildren().get(1).hashCode(), b.hashCode());

	}

	@Test
	public void verify_children_of_home_directory() {
		int num_of_children_expected = 4;
		assertEquals(num_of_children_expected, home.countChildren());

		assertEquals(home.getChildren().get(0).hashCode(), code.hashCode());
		assertEquals(home.getChildren().get(1).hashCode(), c.hashCode());
		assertEquals(home.getChildren().get(2).hashCode(), d.hashCode());

	}

	@Test
	public void verify_children_of_code_directory() {
		int num_of_children_expected = 3;
		assertEquals(num_of_children_expected, code.countChildren());

		assertEquals(code.getChildren().get(0).hashCode(), e.hashCode());
		assertEquals(code.getChildren().get(1).hashCode(), f.hashCode());
	}

	@Test
	public void checking_total_size_of_root() {
		int root_total_size = a.getSize() + b.getSize() + c.getSize() + d.getSize() + e.getSize() + f.getSize()
				+ x.getSize() + y.getSize();
		assertEquals(root_total_size, root.getTotalSize());
	}

	@Test
	public void checking_total_size_of_applications() {
		int applications_total_size = a.getSize() + b.getSize();
		assertEquals(applications_total_size, applications.getTotalSize());
	}

	@Test
	public void checking_total_size_of_code() {
		int code_total_size = e.getSize() + f.getSize() + y.getSize();
		assertEquals(code_total_size, code.getTotalSize());
	}

	@Test
	public void checking_total_size_of_home() {
		int home_total_size = e.getSize() + f.getSize() + c.getSize() + d.getSize() + x.getSize()
				+ y.getSize();
		assertEquals(home_total_size, home.getTotalSize());
	}
	
	@Test
	public void checking_the_target_of_link_y() {
		//b = new File(applications, "b", 1024, LocalDateTime.of(LocalDate.of(2019, 9, 15), LocalTime.of(13, 10, 00)));
		String[] expected = { "b", "2019-09-15T13:10", "1024", "applications" };
		
		assertEquals(y.getTarget().getName(), b.getName());
		assertArrayEquals(expected, FSElementToString(b));
	}
	@Test
	public void checking_the_target_of_link_x() {
		//b = new File(applications, "b", 1024, LocalDateTime.of(LocalDate.of(2019, 9, 15), LocalTime.of(13, 10, 00)));
		String[] expected = { "applications", "2019-09-15T13:00:40", "0", "root" };
		assertEquals(x.getTarget().getName(), applications.getName());
		assertArrayEquals(expected, FSElementToString(applications));
	}
	
}
