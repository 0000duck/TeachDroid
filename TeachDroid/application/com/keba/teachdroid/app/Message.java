package com.keba.teachdroid.app;

import java.util.Date;
import java.util.HashMap;

import com.keba.kemro.serviceclient.alarm.KMessage;

public class Message {
	protected MessageTypes	mMessageType;
	protected int			mImageId;
	protected String		mMessageText;
	protected boolean		mConfirmed;
	private int				mId;			// corresponds to the hashcode of a
											// KMessage
	private java.util.Date	mDate;

	private KMessage		mRawData;

	static {
		initMsgTypeMap();
	}

	@Deprecated
	public Message() {
		this("NO_TEXT", MessageTypes.DEFAULT);
	}


	private Message(String _msgText, MessageTypes _msgType) {
		mMessageText = _msgText;
		mMessageType = _msgType;
		mConfirmed = false;
		switch (mMessageType) {
		case DEFAULT:
			mImageId = R.drawable.ic_info_message;
			break;
		case ALARM:
			mImageId = R.drawable.ic_alarm_message;
			break;
		case WARNING:
			mImageId = R.drawable.ic_default_message;
			break;
		case DEBUG:
			mImageId = R.drawable.ic_drawer;
			break;
		default:
			break;
		}
	}

	/**
	 * @param _rawMessage
	 */
	public Message(KMessage _rawMessage) {
		this(_rawMessage.toString(), getMessageType(_rawMessage));
		mRawData = _rawMessage;
		setID(_rawMessage.hashCode());
		setDate(new Date(_rawMessage.getTimeMSec()));
	}

	public boolean getConfirmed() {
		return mConfirmed;
	}

	private static MessageTypes getMessageType(KMessage _data) {
		long classId = _data.getBuffer().getClassId() & 0xFFFFFFFFL;
		if (classId < -1) {
			classId = -2L * classId + 1L;
		}

		classId = mTypeMap.get(classId);

		// if (classId > 32)
		// return MessageTypes.DEFAULT;

		switch ((int) classId) {
		case 1:
		case 2:
		case 3:
		case 6:
		case 7:
		case 10:
			return MessageTypes.ALARM;
		case 4:
		case 8:
		case 11:
			return MessageTypes.WARNING;

		default:
			return MessageTypes.DEFAULT;
		}
	}

	private static void initMsgTypeMap() {
		mTypeMap = new HashMap<Long, Long>();

		for (long i = 1; i <= 32; i++) {
			mTypeMap.put(i, i);
			mTypeMap.put((1L << (i - 1)), i);
		}
	}

	private static HashMap<Long, Long>	mTypeMap;


	public void setConfirmed(boolean _confirmed) {
		mConfirmed = _confirmed;
	}

	@Override
	public String toString() {
		return mMessageText;
	}

	/**
	 * The {@link Object#hashCode()} of the corresponding {@link KMessage}
	 * 
	 * @return
	 */
	public int getID() {
		return mId;
	};

	public int getImageID() {
		return mImageId;
	}

	protected void setID(int _id) {
		mId = _id;
	}

	protected void setDate(Date _d) {
		mDate = _d;
	}

	public Date getDate() {
		return mDate;
	}

	/**
	 * @return
	 */
	public boolean isAlarm() {
		return getMessageType(mRawData) == MessageTypes.ALARM;
	}

	/**
	 * @return
	 */
	public boolean isWarning() {
		return getMessageType(mRawData) == MessageTypes.WARNING;
	}
}
