package de.htwg.lpn.wgbuddy.utility;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Worker Thread, um Aufgaben nicht im UI-Thread abzuarbeiten.
 */
public class WorkerThread extends Thread
{
	private Callable<Message> callable;
	private ProgressDialog pd;
	private Handler handler;

	/**
	 * Konstruktor
	 * 
	 * @param callable
	 *            Code der in eigenem Thread ausgeführt werden soll.
	 * @param pd
	 *            ProgressDialog, der gerade angezeigt wird und anschließend
	 *            ausgeblendet werden soll.
	 * @param handler
	 *            Handler, der anschließend ausgeführt wird.
	 */
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
			Log.d("log_tag", "Error future get data " + e.toString() + e.getMessage());
		}
		catch (ExecutionException e)
		{
			Log.d("log_tag", "Error future get data " + e.toString() + e.getMessage());
		}

		// ProgressDialog ausblenden und Handler mit Message aufrufen.
		pd.dismiss();
		handler.sendMessage(message);
	}
}
