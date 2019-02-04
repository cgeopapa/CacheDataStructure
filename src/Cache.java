import java.io.IOException;

public class Cache<K, V> implements CacheInterface<K, V>
{
    private HashTable<K, V> hashTable;
    private long hits = 0, misses = 0, lookUps = 0, add = 0;

    public Cache(int size) throws IOException
    {
        hashTable = new HashTable<>(size);
    }

    public V lookUp(K key)
    {
        V data = hashTable.get(key);
        lookUps++;
        if(data == null)
        {
            misses++;
        }
        else
        {
            hits++;
        }
        return data;
    }

    public void store(K key, V value)
    {
        add++;
        hashTable.put(key, value);
    }

    public double getHitRatio()
    {
        return (double)(hits)/lookUps;
    }

    public long getHits()
    {
        return hits;
    }

    public long getMisses()
    {
        return misses;
    }

    public long getNumberOfLookUps()
    {
        return lookUps;
    }

    public long getAdd()
    {
        return add;
    }

    public int getRemove()
    {
        return hashTable.getRemove();
    }

    public long getSize()
    {
        return hashTable.getSize();
    }

    public int getHashSize()
    {
        return hashTable.getHashSize();
    }
    public int getEmpty()
    {
        return hashTable.getEmpty();
    }
}
