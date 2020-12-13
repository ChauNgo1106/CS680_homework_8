package edu.umb.cs680.hw08;

import java.time.LocalDateTime;

public class Link extends FSElement {

	private FSElement target;

	public Link(Directory parent, String name, int size, LocalDateTime creationTime, FSElement target) {
		super(parent, name, size, creationTime);
		if (target == null)
			throw new NullPointerException("File or Directory do not exist");
		else
			this.target = target;

	}

	@Override
	public boolean isDirectory() {
		// TODO Auto-generated method stub
		return false;
	}

	public Directory getParent() {
		return this.parent;
	}

	public String getName() {
		return this.name;
	}

	public int getTargetSize() {
		if (target.isDirectory()) {
			Directory temp = (Directory) target;
			return temp.getTotalSize();
		} else {
			return target.getSize();
		}
	}

	public String getCreationTime() {
		return this.creationTime.toString();
	}

	public int getSize() {
		return this.size;
	}

	public FSElement getTarget() {
		return this.target;
	}
}
