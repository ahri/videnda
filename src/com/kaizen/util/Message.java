package com.kaizen.util;

import android.content.Context;
import android.widget.Toast;

public class Message
{
        public static void popup(Context context, String str)
        {
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
        }
}
