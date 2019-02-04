import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class TestCacheSpeed
{
	public static void main(String[] args) throws IOException
	{
		Scanner s = new Scanner(System.in);
		System.out.println("Please specify cache size [1, 5000]:");
		int cachesize = s.nextInt();
		while(cachesize < 1 || cachesize > 5000)
		{
			System.out.println("Please give a number between 1 and 5000 (inclusive):");
			cachesize = s.nextInt();
		}
		//initialize with your cache implementation
		Cache<String, String> cache = new Cache<>(cachesize);

		File startFIle = new File(System.getProperty("user.dir"));
		
		//give path to the dat file
		String dataFile = new String();
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(startFIle);
		System.out.println("App is still running. Behind all open windows in the middle of your screen there is a " +
				"FileChooser.\nSelect your Data file and then your Requests file.");
		chooser.setDialogTitle("Select Data File");
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			dataFile = chooser.getSelectedFile().getAbsolutePath();
		}
		else
		{
			System.exit(1);
		}

		//give path to the workload file
		String requestsFile = new String();
		chooser.setDialogTitle("Select Requests File");
		if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			requestsFile = chooser.getSelectedFile().getAbsolutePath();
		}
		else
		{
			System.exit(1);
		}

		DataSource dataSource = new DataSource(dataFile);
		WorkloadReader requestReader = new WorkloadReader(requestsFile);

		System.out.println("Do you want full execution report? (Y)es, (N)o");
		String ans = s.next();
		while(!ans.equalsIgnoreCase("y") && !ans.equalsIgnoreCase("n"))
		{
			System.out.println("Yes, I am checking if you're giving wrong answer.");
			ans = s.nextLine();
		}
		boolean shouldGiveFullReport = ans.equalsIgnoreCase("y");
		
		String key = null;		
		long numberOfRequests = 0;
		
		/*start performance test*/
		
		//track current time
		long startTime = System.currentTimeMillis();
		long totalTimeMemoryReads = 0;
		
		while ((key = requestReader.nextRequest()) != null) 
		{
			numberOfRequests++;
			String data = (String)cache.lookUp(key);
			if (data == null) //data not in cache
			{
				long startTimeMemoryRead = System.currentTimeMillis();
				data = dataSource.readItem(key);
				totalTimeMemoryReads += System.currentTimeMillis() - startTimeMemoryRead;
				if (data == null)
				{
					throw new IllegalArgumentException("DID NOT FIND DATA WITH KEY " + key +". Have you set up files properly?");
				}
				else
				{
					cache.store(key, data);
				}
			}			
		}

		/*speed test finished*/
		long duration = System.currentTimeMillis() - startTime;

		System.out.println("dataset: " + dataFile);
		System.out.println("requests: " + requestsFile);
		System.out.println("cache size: " + cachesize + " items (" + cachesize*1f/numberOfRequests + "% of all items)");
		System.out.println("output:\n-----------------------------------------------------------------");
		System.out.println("Read " + numberOfRequests + " items in " + duration + " ms");
		System.out.println("Stats: lookups " + cache.getNumberOfLookUps() + ", hits " + cache.getHits() + ", " +
				"hit-ratio " + cache.getHitRatio());
		System.out.println("*****************************************************************");
		if(shouldGiveFullReport)
		{
			System.out.println("Cache time " + (duration - totalTimeMemoryReads) + " ms");
			System.out.println("Biggest list created " + List.maxSize);
			System.out.println("Total cache blocks filled " + cache.getSize());
			System.out.println("Hash table size " + cache.getHashSize() + ", collisions " + List.collisions + " and " +
					"total " +
					"empty " +
					"blocks " + cache
					.getEmpty());
			System.out.println("Total cache instructions called: Add " + cache.getAdd() + ", Remove " + cache.getRemove
					() + ", LookUps " + cache.getNumberOfLookUps());
		}
		requestReader.close();
	}
}
