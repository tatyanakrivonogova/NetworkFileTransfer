import java.text.DecimalFormat;

public class SpeedController implements Runnable
{
    private static final int PERIOD = 3000;
    private final long id;
    private boolean isRunning;

    public SpeedController(int _id)
    {
        id = _id;
        isRunning = false;
    }

    public void finish()
    {
        if (isRunning)
        {
            isRunning = false;
            synchronized (this)
            {
                this.notifyAll();
            }
        }
    }

    @Override
    public void run()
    {
        isRunning = true;
        long startTime = System.currentTimeMillis();
        long currentTime;
        long lastTime = startTime;

        try
        {
            long lastBytesReceivedCount = 0;
            while (!Thread.currentThread().isInterrupted() && isRunning)
            {
                synchronized (this)
                {
                    this.wait(PERIOD);
                }

                currentTime = System.currentTimeMillis();

                double totalTime = ((double) currentTime - startTime) / 1000.0;
                double totalDataReceived = (double) DataController.getTotalBytesReceived(id) / (1024 * 1024);
                DecimalFormat decimalFormat = new DecimalFormat("####.##");

                double lastInterval = (double) (currentTime - lastTime) / 1000;
                double recentDataReceived = (double) (DataController.getTotalBytesReceived(id) - lastBytesReceivedCount) / (1024 * 1024);

                System.out.println(id + " client : total speed " + decimalFormat.format(totalDataReceived / totalTime) + " mb/sec");
                System.out.println(id + " client : current speed " + decimalFormat.format(recentDataReceived / lastInterval) + " mb/sec");

                lastBytesReceivedCount = DataController.getTotalBytesReceived(id);
                lastTime = currentTime;
            }
        } catch (InterruptedException ex)
        {
            System.out.println("Error during receiving file!");
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}