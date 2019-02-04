public class CacheBlock<K, V>
{
    public V data;
    public K key;

    public CacheBlock(K key, V data)
    {
        this.key = key;
        this.data = data;
    }
}
