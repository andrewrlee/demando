package uk.co.optimisticpanda.dropwizard;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class Environment {
	private String name;
	private String link;
	private long id;
	private List<Environment> children = Lists.newArrayList();

	public Environment(String name, String link) {
		this.name = name;
		this.link = link;
	}
	public Environment() {
		//default 
	}

	public Environment(long id, String content, String link) {
		this.name = content;
		this.link = link;
		this.id = id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLink() {
		return link;
	}

	public void addChild(Environment child) {
		children.add(child);
	}

	public void removeChild(long id) {
		Iterator<Environment> iterator = children.iterator();
		while (iterator.hasNext()) {
			Environment next = iterator.next();
			if (next.getId() == id) {
				iterator.remove();
				return;
			}
		}
		throw new RuntimeException("Cannot remove env with id of:" + id + ", from children containing :" + children);
	}

	public Environment getChild(long id) {
		Iterator<Environment> iterator = children.iterator();
		while (iterator.hasNext()) {
			Environment next = iterator.next();
			if (next.getId() == id) {
				return next;
			}
		}
		throw new RuntimeException("Cannot find env with id of:" + id + ", from children containing :" + children);
	}

	public List<Environment> getChildren() {
		return children;
	}

}