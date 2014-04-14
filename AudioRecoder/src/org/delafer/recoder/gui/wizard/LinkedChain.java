package org.delafer.recoder.gui.wizard;

import java.util.TreeMap;
@SuppressWarnings("unchecked")

public class LinkedChain<E> {

	private Chain topParent;
	private Chain lastChild;
	private Chain current;
	private int cnt;

	private TreeMap<Integer, Chain> byKey = new TreeMap<Integer, Chain>();

	private void register(Chain chain) {
		if (chain==null) return ;
		if (chain.key != null) {
			byKey.put(chain.key, chain);
		}
	}

	public void addLastChild(Chain toAdd) {
		if (null == toAdd) return ;

		current = toAdd;

		if (cnt == 0) {
			lastChild = toAdd;
			topParent = toAdd;
			toAdd.parent = null;
			toAdd.child = null;
		} else {
			lastChild.child = toAdd;
			toAdd.parent = lastChild;
			toAdd.child = null;
			lastChild = toAdd;
		}
		register(toAdd);
		cnt++;
	}

	public void addTopParent(Chain toAdd) {
		if (null == toAdd) return ;

		current = toAdd;

		if (cnt == 0) {
			lastChild = toAdd;
			topParent = toAdd;
			toAdd.parent = null;
			toAdd.child = null;
		} else {
			topParent.parent = toAdd;
			toAdd.child = topParent;
			toAdd.parent = null;
			topParent = toAdd;
		}
		register(toAdd);
		cnt++;
	}

	public void add(Chain toAdd) {
		if (null == toAdd) return ;

		if (cnt == 0) {
			lastChild = toAdd;
			topParent = toAdd;
			current = toAdd;
			toAdd.parent = null;
			toAdd.child = null;
		} else {

			Chain tmp = current.child;

			current.child = toAdd;

			if (tmp != null)
			tmp.parent = toAdd;

			toAdd.parent = current;
			toAdd.child = tmp;

			if (tmp==null) lastChild = toAdd;

			current = toAdd;


		}
		register(toAdd);
		cnt++;
	}

	public Chain get(Integer key) {
		if (key==null) return null;
		return byKey.get(key);
	}

	public Chain getTopParent() {
		return topParent;
	}

	public Chain getLastChild() {
		return lastChild;
	}

	public Chain getCurrent() {
		return current;
	}

	public Chain next() {
//		Chain tmp = current;
		if (current != null)
			current = current.getChild();
		return current;
	}

	public Chain previous() {
//		Chain tmp = current;
		if (current != null)
			current = current.getParent();
		return current;
	}

	public Chain newChain(E value, Integer key) {
		return new Chain(value, key);
	}


	public class Chain {
		protected Chain parent;
		protected Chain child;
		protected E value;
		protected Integer key;

		public Chain(E value, Integer key) {
			this.value = value;
			this.key = key;
		}

		public void addChild(Chain toAdd) {
			if (null == toAdd) return ;

			Chain tmp = this.child;

			this.child = toAdd;
			if (tmp!=null)
				tmp.parent = toAdd;
			else
				LinkedChain.this.lastChild = (Chain)toAdd;

			toAdd.child = tmp;
			toAdd.parent = this;

			LinkedChain.this.register(toAdd);
			LinkedChain.this.cnt++;


		}

		public void addParent(Chain toAdd) {
			if (null == toAdd) return ;

			Chain tmp = this.parent;

			this.parent = toAdd;
			if (tmp!=null)
				tmp.child = toAdd;
			else
				LinkedChain.this.topParent = (Chain)toAdd;

			toAdd.child = this;
			toAdd.parent = tmp;

			LinkedChain.this.register(toAdd);
			LinkedChain.this.cnt++;
		}

		public E getValue() {
			return value;
		}

		public Chain getParent() {
			return parent;
		}

		public Chain getChild() {
			return child;
		}

		@Override
		public String toString() {
			return "[" + key+"="+value.getClass().getSimpleName()+" ("+value+")" + "]";
		}

	}


	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Chain current = this.getTopParent();
		while (current != null) {
			if (sb.length()>0) sb.append(",");
			sb.append(current.getValue());
			current = current.getChild();
		}
		return "Chain ["+sb.toString()+"]";
	}

	public void setCurrent(Chain current) {
		this.current = current;
	}


}

