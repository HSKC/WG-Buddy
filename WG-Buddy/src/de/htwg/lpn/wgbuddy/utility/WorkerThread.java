package de.htwg.lpn.wgbuddy.utility;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

public class WorkerThread extends Thread
{
	private Callable<Message> callable;
	private ProgressDialog pd;
	private Handler handler;

	public WorkerThread(Callable<Message> callable, ProgressDialog pd, Handler handler)
	{
		this.callable = callable;
		this.pd = pd;
		this.handler = handler;
	}

	@Override
	public void run()
	{
		Message message = null;

		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Message> future = executor.submit(callable);

		try
		{
			message = future.get();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pd.dismiss();
		handler.sendMessage(message);
	}
}
