import java.util.HashMap;

public class DataController
{
    private static final HashMap<Long, Long> totalCounters = new HashMap<>();

    public static void addNewCounter(long id)
    {
        synchronized (totalCounters)
        {
            totalCounters.put(id, 0L);
        }
    }

    public static long getTotalBytesReceived(long id)
    {
        synchronized (totalCounters)
        {
            return totalCounters.get(id);
        }
    }

    public static void addToTotal(long id, long addition)
    {
        synchronized (totalCounters)
        {
            totalCounters.put(id, totalCounters.get(id) + addition);
        }
    }

    public static void removeCounter(long id)
    {
        synchronized (totalCounters)
        {
            totalCounters.remove(id);
        }
    }

}