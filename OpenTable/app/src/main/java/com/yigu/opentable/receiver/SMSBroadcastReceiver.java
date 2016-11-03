package com.yigu.opentable.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSBroadcastReceiver extends BroadcastReceiver{
	
	private static MessageListener messageListener;
	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public SMSBroadcastReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			for (Object pdu : pdus) {
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdu);
				//String sender = smsMessage.getDisplayOriginatingAddress();
				String content = smsMessage.getDisplayMessageBody();
				//long date = smsMessage.getTimestampMillis();
				//Date timeDate = new Date(date);
				//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				//String time = simpleDateFormat.format(timeDate);
				messageListener.onReceived(content);
			}
		}
	}

	
	public interface MessageListener{
		public void onReceived(String message);
	}
	
	public void setOnReceivedMessageListener(MessageListener messageListener){
		this.messageListener = messageListener;
	}
	
	
	
}
