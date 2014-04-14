package org.delafer.recoder.model.enums;

import org.delafer.recoder.model.enums.factory.Register;

public enum SampleSize {
	BPS_8(8, 1),
	BPS_16(16, 2),
	BPS_24(24, 3),
	BPS_32(32, 4),
	BPS_64(64, 5),
	BPS_OTHER(-1, -1);

	int bps;
	int id;

	SampleSize(int bps, int id) {
		this.bps = bps;
		this.id = id;
		Register.register(this, bps);
	}

	public int getBps() {
		return bps;
	}

	public int getId() {
		return id;
	}

	public static SampleSize getBySize(int sampleSize, SampleSize defValue) {
		SampleSize ret = (SampleSize)Register.getByKey(BPS_OTHER, sampleSize);
		return (ret != null) ? ret : defValue;
	}

}
