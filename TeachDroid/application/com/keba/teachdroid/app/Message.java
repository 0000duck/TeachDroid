package com.keba.teachdroid.app;

import java.util.Date;

public class Message {
	protected MessageTypes	mMessageType;
	protected int			mImageId;
	protected String		mMessageText;
	protected boolean		mConfirmed;
	private int				mId;			// corresponds to the hashcode of a
											// KMessage
	private java.util.Date	mDate;

	public Message() {
		this("DefaultMessage");
	}

	public Message(String _msgText) {
		this(_msgText, MessageTypes.DEFAULT);
	}

	public Message(String _msgText, MessageTypes _msgType) {
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

	public boolean getConfirmed() {
		return mConfirmed;
	}

	public void setConfirmed(boolean _confirmed) {
		mConfirmed = _confirmed;
	}

	@Override
	public String toString() {
		return mMessageText;
	}

	public int getID() {
		return mId;
	};

	public int getImageID() {
		return mImageId;
	}

	public void setID(int _id) {
		mId = _id;
	}

	public void setDate(Date _d) {
		mDate = _d;
	}

	public Date getDate() {
		return mDate;
	}
}
