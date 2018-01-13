package com.georgiasoftworks.threadsafe;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends AppCompatActivity {

    private static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>();
    private final int PRODUCE_COUNT = 10000;
    private static int C1_1 = 0;
    private static int C2_1 = 0;
    private static int C3_1 = 0;
    private static int C1_2 = 0;
    private static int C2_2 = 0;
    private static int C3_2 = 0;
    private static int C1_3 = 0;
    private static int C2_3 = 0;
    private static int C3_3 = 0;
    private TextView Count_C1_1;
    private TextView Count_C2_1;
    private TextView Count_C3_1;
    private TextView Count_C1_2;
    private TextView Count_C2_2;
    private TextView Count_C3_2;
    private TextView Count_C1_3;
    private TextView Count_C2_3;
    private TextView Count_C3_3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button Producer1 = (Button)findViewById( R.id.producer1);
        Button Producer2 = (Button)findViewById( R.id.producer2);
        Button Producer3 = (Button)findViewById( R.id.producer3);
        Button ProducerAll = (Button)findViewById( R.id.producerall);
        Count_C1_1 = (TextView)findViewById(R.id.c_1_1);
        Count_C2_1 = (TextView)findViewById(R.id.c_2_1);
        Count_C3_1 = (TextView)findViewById(R.id.c_3_1);
        Count_C1_2 = (TextView)findViewById(R.id.c_1_2);
        Count_C2_2 = (TextView)findViewById(R.id.c_2_2);
        Count_C3_2 = (TextView)findViewById(R.id.c_3_2);
        Count_C1_3 = (TextView)findViewById(R.id.c_1_3);
        Count_C2_3 = (TextView)findViewById(R.id.c_2_3);
        Count_C3_3 = (TextView)findViewById(R.id.c_3_3);


        Producer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Produce(1, PRODUCE_COUNT);
            }
        });
        Producer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Produce(2, PRODUCE_COUNT);
            }
        });
        Producer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Produce(3, PRODUCE_COUNT);
            }
        });
        ProducerAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Produce(-1, PRODUCE_COUNT);
            }
        });

        for (int i = 0; i < 3; i++)
        {
            ConsumerThread consumerThread = new ConsumerThread(i + 1);
            consumerThread.start();
        }
    }

    private void Produce(int id, int count)
    {
        for (int i = 0; i < count; i++)
            try {
                if (id != -1)
                    queue.put(id);
                else {
                    queue.put(1);
                    queue.put(2);
                    queue.put(3);
                }
            }
            catch (InterruptedException e) {
            }
    }

    class ConsumerThread extends Thread
    {
        final int consumerId;

        ConsumerThread(int consumerId)
        {
            super("ConsumerThread " + consumerId);
            this.consumerId = consumerId;
        }

        @Override
        public void run() {
            Log.i("ConsumerThread", "Instance " + consumerId + " started");
            MainActivity.EventHandler handler;

            handler = new MainActivity.EventHandler(Looper.getMainLooper());

            int producerId;
            while (true) {
                try {
                    producerId = queue.take();
                } catch (InterruptedException e) {
                    return;
                }
                Message msg = handler.obtainMessage(1, consumerId, producerId, "");

                handler.sendMessage(msg);
            }
        }
    }

    public class EventHandler extends Handler
    {
        public EventHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);

            switch(msg.what)
            {
                case 1:
                {
                    int consumerId = msg.arg1;
                    int producerId = msg.arg2;
                    //consumer is 1st in our names
                    switch (consumerId)
                    {
                        case 1:
                            switch (producerId)
                            {
                                case 1:
                                    C1_1++;
                                    Count_C1_1.setText(Integer.toString(C1_1));
                                    break;
                                case 2:
                                    C1_2++;
                                    Count_C1_2.setText(Integer.toString(C1_2));
                                    break;
                                case 3:
                                    C1_3++;
                                    Count_C1_3.setText(Integer.toString(C1_3));
                                    break;
                            }
                            break;
                        case 2:
                            switch (producerId)
                            {
                                case 1:
                                    C2_1++;
                                    Count_C2_1.setText(Integer.toString(C2_1));
                                    break;
                                case 2:
                                    C2_2++;
                                    Count_C2_2.setText(Integer.toString(C2_2));
                                    break;
                                case 3:
                                    C2_3++;
                                    Count_C2_3.setText(Integer.toString(C2_3));
                                    break;
                            }
                            break;
                        case 3:
                            switch (producerId)
                            {
                                case 1:
                                    C3_1++;
                                    Count_C3_1.setText(Integer.toString(C3_1));
                                    break;
                                case 2:
                                    C3_2++;
                                    Count_C3_2.setText(Integer.toString(C3_2));
                                    break;
                                case 3:
                                    C3_3++;
                                    Count_C3_3.setText(Integer.toString(C3_3));
                                    break;
                            }
                        break;
                    }
                }
                break;
                default:
                    break;
            }
        }
    }
}
