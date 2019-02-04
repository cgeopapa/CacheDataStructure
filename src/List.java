public class List
{
	private class ListItem
	{
		private ListItem older, newer;
		private CacheBlock data;

		private ListItem(CacheBlock c)
		{
			data = c;
		}
	}

	public static int collisions = 0;
	public static int maxSize = 0;
	private ListItem mru, lru;
	private int size;

	public List()
	{
		mru = lru = null;
		size = 0;
	}

	private boolean isEmpty()
	{
		return (mru == null);
	}

	public void Add(CacheBlock data)	//As MRU(first)
	{
		size++;
		maxSize = size > maxSize? size : maxSize;
		ListItem i = new ListItem (data);

		if(isEmpty())
		{
			mru = lru = i;
			i.older = i.newer = null;
		}
		else
		{
			collisions++;
			mru.newer = i;
			i.older = mru;
			i.newer = null;
			mru = i;
		}
	}

	public void RemoveLRU()	//LRU (last)
	{
		size--;
		if(mru == lru)
		{
			mru = lru = null;
		}
		else
		{
			lru = lru.newer;
			lru.older = null;
		}
	}

	public ListItem MoveFirst(ListItem i)
	{
		if (i == mru)
		{
			return i;
		}
		else
		{
			i.newer.older = i.older;
			if (i.older == null)
			{
				lru = i.newer;
			}
			else
			{
				i.older.newer = i.newer;
			}
			mru.newer = i;
			i.older = mru;
			i.newer = null;
			mru = i;
			return i;
		}
	}

	public <K> CacheBlock Get(K item)
	{
		if(isEmpty())
		{
			return null;
		}
		ListItem m = mru;
		ListItem l = lru;
		do
		{
			if(m.data.key.equals(item))
			{
				return MoveFirst(m).data;
			}
			else if(l.data.key.equals(item))
			{
				return MoveFirst(l).data;
			}
			m = m.older;
			l = l.newer;
		}while(!(m == l || m.newer == l));
		return null;
	}

	public int getSize()
	{
		return size;
	}
}
