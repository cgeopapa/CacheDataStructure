import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HashTable<K, V>
{
    private int maxSize, curSize, hashSize, timestampDelIndex = 0, timestampWriteIndex = 0;
    private  int[] timestamp;
    private List[] hashTable;
    private int remove = 0;

    public HashTable(int size) throws IOException
    {
        maxSize = size;
        curSize = 0;
        timestamp = new int[size];

        hashSize = size;
        BufferedReader reader = new BufferedReader(new FileReader("dataset/Primes.txt"));
        int prime = Integer.parseInt(reader.readLine());
        while(prime < hashSize/10)
        {
            prime = Integer.parseInt(reader.readLine());
        }
        hashSize = prime;

        hashTable = new List[hashSize];
        for(int i = 0; i < hashSize; i++)
        {
            hashTable[i] = new List();
        }
    }

    public void put(K key, V data)
    {
        if (curSize == maxSize)
        {
            hashTable[timestamp[timestampDelIndex]].RemoveLRU();
            timestampDelIndex = (timestampDelIndex + 1) % maxSize;
            curSize--;
            remove ++;
        }
        CacheBlock<K, V> item = new CacheBlock<>(key, data);
        timestamp[timestampWriteIndex] = hashFunction(key);
        hashTable[timestamp[timestampWriteIndex]].Add(item);
        timestampWriteIndex = (timestampWriteIndex + 1) % maxSize;
        curSize++;
    }

    public V get(K key)
    {
        CacheBlock<K, V> block = hashTable[hashFunction(key)].Get(key);
        if(block == null)
        {
            return null;
        }
        else
        {
            return block.data;
        }
    }

    public int getSize()
    {
        int size = 0;
        for(int i = 0; i < hashSize; i++)
        {
            size += hashTable[i].getSize();
        }
        return size;
    }

    public int getHashSize()
    {
        return hashSize;
    }

    public int getEmpty()
    {
        int size = 0;
        for(int i = 0; i < hashSize; i++)
        {
            if(hashTable[i].getSize() == 0)
            {
                size++;
            }
        }
        return size;
    }

    public int getRemove()
    {
        return remove;
    }

    private int hashFunction(K key)
    {
        return key.hashCode() % hashSize;
    }
}
